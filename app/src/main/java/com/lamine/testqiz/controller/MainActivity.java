package com.lamine.testqiz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lamine.testqiz.model.ItemRowScore;
import com.lamine.testqiz.R;
import com.lamine.testqiz.model.ScoreData;
import com.lamine.testqiz.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.lamine.testqiz.controller.GameActivity.BUNDLE_EXTRA_SCORE;
import static com.lamine.testqiz.model.ScoreData.mItemRowScores;


public class MainActivity extends AppCompatActivity {

    public static final int GAME_ACTIVITY_ID = 12;
    public static final int SCORE_ACTIVITY_ID = 24;
    public static final String PREF_KEY_SCORE = "PKS";
    public static final String PREF_KEY_FIRSTNAME = "PKF";

    //Here we handle a date format to show it on the items
    Date now = new Date();
    SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE);
    String date = sfd.format(now);

    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private Button mScoreButton;
    private Button mQuitButton;
    private User mUser;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         *Connector between view and controller
         */
        mUser = new User();
        mPreferences = getPreferences(MODE_PRIVATE);

        mGreetingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);
        mScoreButton = (Button) findViewById(R.id.activity_main_score_btn);
        mQuitButton = (Button) findViewById(R.id.activity_main_quit_btn);

        //Data is loaded whe, application start
        ScoreData.loadData(this);

        /*this method check if user changer is Edit text*/
        mNameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // This is where we'll check the user input
                mPlayButton.setEnabled(s.toString().length() != 0);

                mPlayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String firstname = mNameInput.getText().toString();
                        mUser.setFirstName(firstname);

                        //Stock the firstname (user text input) in the shared preferences
                        mPreferences.edit().putString(PREF_KEY_FIRSTNAME, mUser.getFirstName()).apply();

                        // The user just clicked
                        Intent gameActivity = new Intent(MainActivity.this, GameActivity.class);
                        startActivityForResult(gameActivity, GAME_ACTIVITY_ID);
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(true);

            }

        });

        /*
         * Button score is not valid at start when none data is stored*/
        if (mItemRowScores.size() == 0)
            mScoreButton.setVisibility(View.GONE);

//        This here where we handle the access to the Score view
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scoreActivity = new Intent(MainActivity.this, ScoreActivity.class);
                startActivityForResult(scoreActivity, SCORE_ACTIVITY_ID);
            }
        });

        //Handle the closure of the application with the quit button
        mQuitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //If you want to quit click here all activity will finish and don't run in background
                endApp();
            }
        });
    }

    /*
     * The method @endApp allow the player to stop the application
     * Two options :
     * Yes: the application is killed it does not run in background
     * No: the application resume*/
    private void endApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Quitter le jeu")
                .setMessage("Êtes-tu sûr ?")
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onResume();
                    }
                })
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //End of current activity
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                //This allow to kil the application efficiently (but we keep all the data saved)
                                System.exit(RESULT_CANCELED);
                            }
                        }, 1000);
                    }
                })
                .create()
                .show();
    }

    /*
     * After a first interaction with the apk @onActivityResult stock in memory name and score*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (GAME_ACTIVITY_ID == requestCode && RESULT_OK == resultCode) {
            //Fetch the score from the Intent
            int score = data.getIntExtra (BUNDLE_EXTRA_SCORE, 0);
            //Stock the score in shared preferences
            mPreferences.edit ().putInt (PREF_KEY_SCORE, score).apply ();
            /**When a round of game is played in mainActivity we can see the score button*
             *And the methods greetUser() is used here just after the game result
             */
            mScoreButton.setVisibility (View.VISIBLE);
            greetUser ();
        }

    }


    private void greetUser() {
        String firstname = mPreferences.getString(PREF_KEY_FIRSTNAME, null);

        if (null != firstname) {
            int score = mPreferences.getInt(PREF_KEY_SCORE, 0);

            String fulltext = "Bon retour parmi nous, " + firstname
                    + "!\nTon dernier score était de " + score
                    + ", feras-tu mieux la prochaine fois ?";

            //This is where we can store both name and score after the game is over
            scoreHandler(firstname, score);

            mGreetingText.setText(fulltext);
            mNameInput.setText(firstname);
            mNameInput.setSelection(firstname.length());
            mPlayButton.setEnabled(true);
        }
    }

    private void scoreHandler(String name, int score) {
        //Here we avoid to put more than 5 items on the screen's device
        if (mItemRowScores.size() == 5)
            mItemRowScores.remove(0);

        //Here we had the new name and score of the game every time the user go back to the menu automatically
        mItemRowScores.add(new ItemRowScore (name, String.valueOf(score), date));
        //Here the data is saved (Look to Score data class in the model folder)
        ScoreData.saveData(this);
    }

}


