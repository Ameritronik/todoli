package com.codepath.todoli;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;

public class todoli extends AppCompatActivity{

    ViewGroup startContainer;
    Scene mStart;
    Scene mList;
    Transition transitionMgr;
    private static int DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todoli);
        transitionMgr = TransitionInflater.from(this).inflateTransition(R.transition.transition);

        startContainer = (ViewGroup) findViewById(R.id.todoli);
        mStart = Scene.getSceneForLayout(startContainer, R.layout.activity_todoli,this);
        TransitionManager.go(mStart, transitionMgr);
        startContainer = (ViewGroup) findViewById(R.id.todoli);
        mList = Scene.getSceneForLayout(startContainer, R.layout.activity_main,this);

    }

    public void BtnClicked(View view){
        final Intent i= new Intent(this, MainActivity.class);

        TransitionManager.go(mList, transitionMgr);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(i);
                finish();
            }
        }, DELAY);

    }

}
