package com.cbnu.sweng.randombox.dictation_user.dictation_user.ui.practice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.cbnu.sweng.randombox.dictation_user.dictation_user.R;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.adapter.PracticeTypeAdapter;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.model.PracticeTypeModel;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.model.Student;
import com.cbnu.sweng.randombox.dictation_user.dictation_user.ui.base.BaseDrawerActivity;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectPracticeTypeActivity extends BaseDrawerActivity {

    @BindView(R.id.rvPractice) RecyclerView rvPractice;

    public static final String[] titles = {"유아용", "유아용", "유아용", "초등학생용"};
    public static final Integer[] images = {R.drawable.practice1, R.drawable.practice2, R.drawable.practice3, R.drawable.practice4};
    private int numOfCard = 4;
    private ArrayList<PracticeTypeModel> practiceTypeModels = new ArrayList<>();
    private PracticeTypeAdapter practiceTypeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_practice_type);

        ButterKnife.bind(this);

        initModels();
        setupView();
    }

    private void setupView() {
        StaggeredGridLayoutManager MyLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        MyLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        rvPractice.setHasFixedSize(true);
        rvPractice.setLayoutManager(MyLayoutManager);
        practiceTypeAdapter = new PracticeTypeAdapter(this, practiceTypeModels);
        rvPractice.setAdapter(practiceTypeAdapter);
        practiceTypeAdapter.notifyDataSetChanged();
    }

    private void initModels() {
        practiceTypeModels.clear();
        for (int i = 0; i < numOfCard; i++) {
            PracticeTypeModel practiceTypeModel = new PracticeTypeModel();
            practiceTypeModel.setTitle(titles[i]);
            practiceTypeModel.setImage(images[i]);
            practiceTypeModels.add(practiceTypeModel);
        }
    }
}
