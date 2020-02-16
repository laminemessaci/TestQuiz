package com.lamine.testqiz.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.lamine.testqiz.model.ItemRowScore;
import com.lamine.testqiz.R;
import com.lamine.testqiz.viewer.ScoreAdapter;
import com.lamine.testqiz.model.ScoreData;

import java.util.Collections;
import java.util.Comparator;

import static com.lamine.testqiz.model.ScoreData.mItemRowScores;

public class ScoreActivity extends AppCompatActivity {

    //Methods
    public static Comparator<ItemRowScore> ComparatorNom = new Comparator<ItemRowScore>() {

        @Override
        public int compare(ItemRowScore e1, ItemRowScore e2) {
            return e1.getPseudo().compareTo(e2.getPseudo());
        }
    };
    public static Comparator<ItemRowScore> ComparatorScore = new Comparator<ItemRowScore>() {

        @Override
        public int compare(ItemRowScore e1, ItemRowScore e2) {
            return e2.getScore().compareTo(e1.getScore());
        }
    };

    private ImageButton mBtnName;
    private ImageButton mBtnScore;
    private ImageButton mBtnDelete;
    private ListView mListView;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        mListView = findViewById(R.id.list_view);
        mBtnName = findViewById(R.id.activity_score_sortName_btn);
        mBtnScore = findViewById(R.id.activity_score_sortScore_btn);
        mBtnDelete = findViewById(R.id.activity_score_sortScore_reset);

        ScoreData.loadData(this);
        final ScoreAdapter adapter = new ScoreAdapter(this, mItemRowScores);
        mListView.setAdapter(adapter);


        mBtnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sort by name
                Collections.sort(mItemRowScores, ComparatorNom);
                adapter.notifyDataSetChanged();
            }
        });

        mBtnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sort by score
                Collections.sort(mItemRowScores, ComparatorScore);
                adapter.notifyDataSetChanged();
            }
        });

        mBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScoreData.clearData(ScoreActivity.this);
                adapter.notifyDataSetChanged();
            }
        });
    }

}
