package com.lamine.testqiz.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lamine.testqiz.model.Question;
import com.lamine.testqiz.model.QuestionBank;
import com.lamine.testqiz.R;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String BUNDLE_EXTRA_SCORE = "BES";
    public static final String BUNDLE_STATE_SCORE = "currentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private TextView mQuestionTextView;
    private Button mAnswer1Btn;
    private Button mAnswer2Btn;
    private Button mAnswer3Btn;
    private Button mAnswer4Btn;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mNumberOfQuestions;
    private int mScore;
    private boolean mEnableTouchEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionBank = this.generateQuestions();

        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mNumberOfQuestions = 6;
            mScore = 0;
        }

        mEnableTouchEvents = true;

        //Wire Widget to connect the controller with the view
        mQuestionTextView = findViewById(R.id.activity_game_question_text);
        mAnswer1Btn = findViewById(R.id.activity_game_answer1_btn);
        mAnswer2Btn = findViewById(R.id.activity_game_answer2_btn);
        mAnswer3Btn = findViewById(R.id.activity_game_answer3_btn);
        mAnswer4Btn = findViewById(R.id.activity_game_answer4_btn);

        //A way to name the button
        mAnswer1Btn.setTag(0);
        mAnswer2Btn.setTag(1);
        mAnswer3Btn.setTag(2);
        mAnswer4Btn.setTag(3);

        mAnswer1Btn.setOnClickListener(this);
        mAnswer2Btn.setOnClickListener(this);
        mAnswer3Btn.setOnClickListener(this);
        mAnswer4Btn.setOnClickListener(this);

        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(final View view) {
        int indexResponse = (int) view.getTag();
        //When the button is touched, it changes color
        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (indexResponse == mCurrentQuestion.getAnswerIndex()) {
            Toast.makeText(this, "Bonne réponse ;)", Toast.LENGTH_SHORT).show();
            mScore++;
        } else
            Toast.makeText(this, "Mauvaise réponse :(", Toast.LENGTH_SHORT).show();

        //The button question are disable 2 seconds until the toast text has not disapeared
        mEnableTouchEvents = false;

        //We hold few second before going to the next question (same time as the toast text duration)
        new Handler ().postDelayed(new Runnable() {
            @Override
            public void run() {
                mEnableTouchEvents = true;
                //decrement number of question
                --mNumberOfQuestions;
                if (mNumberOfQuestions == 0) {
                    //If it's last question End of the game
                    view.setBackgroundColor(0xFFFFFFFF);
                    endGame();
                    //Else display an another question
                } else {
                    //During displaying question the button go back to white
                    view.setBackgroundColor(0xFFFFFFFF);
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
            }
        }, 2000); //Its the same time in millis of LENGTH_SHORT
    }

    //Methods who deal with the user and the screen
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPointerCaptureChanged (boolean hasCapture) {

    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Bien joué !")
                .setMessage("Ton score est de " + mScore + " point(s)")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //End of current activity
                        Intent intent = new Intent();
                        intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .create()
                .show();
    }

    private void displayQuestion(final Question question) {
        mQuestionTextView.setText(question.getQuestion());
        mAnswer1Btn.setText(question.getChoiceList().get(0));
        mAnswer2Btn.setText(question.getChoiceList().get(1));
        mAnswer3Btn.setText(question.getChoiceList().get(2));
        mAnswer4Btn.setText(question.getChoiceList().get(3));
    }


    private QuestionBank generateQuestions() {
        Question question1 = new Question("Combien d'Oscar le film Titanic a-t-il remporté ?",
                Arrays.asList("10", "11", "12", "13"), 1);
        Question question2 = new Question("A partir de quel cactus la Tequila est-elle fabriquée ?",
                Arrays.asList("L' agave", "L' ouzo", "Le paprika", "Le pisang"), 0);
        Question question3 = new Question("Quel est le pays d'origine du cocktail, le Mojito ?",
                Arrays.asList("Mexique", "Honduras", "Brésil", "Cuba"), 3);
        Question question4 = new Question("Quelle est le compoosant principal du verre ?",
                Arrays.asList("Le vitrium", "Le bicarbonate", "Le sable", "La glace"), 2);
        Question question5 = new Question("Dans quelle pays se situe le plus vieux zoo du monde ?",
                Arrays.asList("Budapest", "Amsterdam", "Vienne", "Berlin"), 2);
        Question question6 = new Question("Qui était le dictateur de l'Iraq",
                Arrays.asList("Ben Ladden", "Saddam Hussain", "Khaled", "Nasser"), 1);
        Question question7 = new Question("Combien de continent dénombre-t-on sur la terre ?",
                Arrays.asList("6", "7", "8", "9"), 1);
        Question question8 = new Question("Quel pays est le plus gros producteur d'huile d'olive ?",
                Arrays.asList("Maroc", "Portugal", "Tunisie", "Espagne"), 3);
        Question question9 = new Question("Quel célèbre dictateur dirigea l’URSS du début des années 1920 à 1953 ?",
                Arrays.asList("Staline", "Lénine", "Trotski", "Molotov"), 0);
        Question question10 = new Question("Qui était le dieu de la guerre dans la mythologie grecque ?",
                Arrays.asList("Hermès", "Arès", "Hadès", "Apollon"), 1);
        Question question11 = new Question("Quel est la dans officielle du Brésil ?",
                Arrays.asList("La samba", "Le tango", "Le chacha", "La valse"), 0);
        Question question12 = new Question("Qui est l'actuel président de la France",
                Arrays.asList("Hollande", "Sarkozy", "Chirac", "Macron"), 3);
        Question question13 = new Question("Quel était le nom du petit dragon dans le film Mulan ?",
                Arrays.asList("Honshu", "Kyushu", "Mushu", "Royco"), 2);
        Question question14 = new Question("Quelle est le nom de l'actrice principale d'Alien ?",
                Arrays.asList("Sigourney Weaver", "Sigourney Beaver", "Sigourney Meaver", "Sigourney Ueaver"), 0);
        Question question15 = new Question("Quelle était la profession de Popeye ?",
                Arrays.asList("Militaire", "Cuisinier", "Marin", "Pêcheur"), 2);
        Question question16 = new Question("A quel écrivain doit-on le personnage de Boule-de-Suif ?",
                Arrays.asList("Charles Baudelaires", "Guy de Maupassant", "Alexandre Dumas", "Albert Camus"), 1);
        Question question17 = new Question("De quel pays Tirana est-elle la capitale ?",
                Arrays.asList("L' Ouzbékistan", "La Biélorussie", "L' Albanie", "Le Kirghistan"), 2);
        Question question18 = new Question("De quel groupe Jim Morrison était-il le chanteur ?",
                Arrays.asList("The Cardigans", "The Beattles", "The Who", "The Doors"), 3);
        Question question19 = new Question("Dans quelle ville française se trouve la Cité de l'espace ?",
                Arrays.asList("Poithier", "Toulouse", "Lyon", "Nantes"), 1);
        Question question20 = new Question("En Inde, quels individus \"hors caste\" sont considérés comme impurs ?",
                Arrays.asList("Les puants", "Les parias", "Les sales", "Les intouchables"), 3);
        Question question21 = new Question("Que tient la statue de la Liberté dans sa main droite ?",
                Arrays.asList("Un livre", "Un sac", "Un flambeau", "Un étendard"), 2);


        return new QuestionBank(Arrays.asList(question1,
                question2,
                question3,
                question4,
                question5,
                question6,
                question7,
                question8,
                question9,
                question10,
                question11,
                question12,
                question13,
                question14,
                question15,
                question16,
                question17,
                question18,
                question19,
                question20,
                question21));
    }
}
