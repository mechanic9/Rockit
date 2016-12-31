package com.example.root.rockit;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import static com.example.root.rockit.R.layout.activity_main;

/**
 * Created by root on 12/17/16.
 * Controls the functions in the main game page
 */

public class GameActivity extends AppCompatActivity
{

    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        FrameLayout game = new FrameLayout(this);
        gameView = new GameView(this, size.x, size.y);
        gameView.resume();
        LinearLayout gameWidgets = new LinearLayout(this);

        Button pauseBtn = new Button(this);
        pauseBtn.setWidth(200);
        pauseBtn.setText("Pause");
        pauseBtn.setBackgroundColor(0);

        gameWidgets.addView(pauseBtn);

        game.addView(gameView);
        game.addView(gameWidgets);

        //adding game frame to contentview
        setContentView(game);


    }

    //pausing the game when activity is paused
    @Override
    protected void onPause()
    {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume()
    {
        super.onResume();
        gameView.resume();
    }
}
