package com.example.root.rockit;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 *The MainActivity controls the all functions in the start page
 */
public class MainActivity extends AppCompatActivity
{

    private Bitmap pauseBitmap, playBitmap, restartBitmap;
    TextView highScore, score;
    //Widgets
    private FrameLayout game;
    private RelativeLayout gameOverView;
    private LinearLayout gameWidgets;
    private RelativeLayout pausedView;

    private android.os.Handler mainHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //set orientation to landscape
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //Getting the screen resolution into point object
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //Handler for thread
        mainHandler = new android.os.Handler(getApplicationContext().getMainLooper());

        //Matrix for bitmaps
        Matrix pauseMatrix = new Matrix();
        Matrix playMatrix = new Matrix();
        Matrix restartMatrix = new Matrix();

        //Bitmaps for the buttons
        pauseBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.pause);
        playBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.play);
        restartBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.restart);

        //Scaling Pause Bitmap
        int pauseWidth = pauseBitmap.getWidth();
        int pauseHeight = pauseBitmap.getHeight();
        float pauseNewHeight = pauseBitmap.getHeight();
        float pauseNewWidth = pauseBitmap.getWidth();
        float pauseScaleWidth = ((float) pauseNewWidth) / pauseWidth;
        float pauseScaleHeight = ((float) pauseNewHeight) / pauseHeight;
        pauseMatrix.postScale(pauseScaleWidth, pauseScaleHeight);
        pauseBitmap = Bitmap.createBitmap(pauseBitmap, 0, 0, pauseBitmap.getWidth(), pauseBitmap.getHeight(), pauseMatrix, true);

        //Size of start buttons
        int startSize = size.y/3;
        //Scaling Play Bitmap
        int playWidth = playBitmap.getWidth();
        int playHeight = playBitmap.getHeight();
        float playNewHeight = startSize;
        float playNewWidth = startSize;
        float playScaleWidth = ((float) playNewWidth) / playWidth;
        float playScaleHeight = ((float) playNewHeight) / playHeight;
        playMatrix.postScale(playScaleWidth, playScaleHeight);
        playBitmap = Bitmap.createBitmap(playBitmap, 0, 0, playBitmap.getWidth(), playBitmap.getHeight(), playMatrix, true);

        //Scaling Restart Bitmap
        int restartWidth = restartBitmap.getWidth();
        int restartHeight = restartBitmap.getHeight();
        float restartNewHeight = startSize;
        float restartNewWidth = restartNewHeight;
        float restartScaleWidth = ((float) restartNewWidth) / restartWidth;
        float restartScaleHeight = ((float) restartNewHeight) / restartHeight;
        restartMatrix.postScale(restartScaleWidth, restartScaleHeight);
        restartBitmap = Bitmap.createBitmap(restartBitmap, 0, 0, restartBitmap.getWidth(), restartBitmap.getHeight(), restartMatrix, true);

        //Init Views
        //game = new FrameLayout(this);
        final GameView gameView = new GameView(this, size.x, size.y);
        gameView.resume();
        gameWidgets = new LinearLayout(this);
        pausedView = new RelativeLayout(this);
        game = new FrameLayout(this);
        gameOverView = new RelativeLayout(this);

        //Pause button
        ImageButton btnPause = new ImageButton(this);
        btnPause.setImageBitmap(pauseBitmap);
        btnPause.setBackgroundColor(0);
        btnPause.setX(size.x-pauseBitmap.getWidth()-2);
        btnPause.setY(2);

        //Play Button
        ImageButton btnPlay = new ImageButton(this);
        btnPlay.setImageBitmap(playBitmap);
        btnPlay.setBackgroundColor(0);
        btnPlay.setX(size.x/2-playBitmap.getWidth()/2);
        btnPlay.setY(size.y/2-playBitmap.getHeight()/2);

        //Restart Button
        ImageButton btnRestart = new ImageButton(this);
        btnRestart.setImageBitmap(restartBitmap);
        btnRestart.setBackgroundColor(0);
        btnRestart.setX(size.x/2-restartBitmap.getWidth()/2);
        btnRestart.setY(size.y/2-restartBitmap.getHeight()/2);

        //Score
        int scoreSize = 80;
        score = new TextView(this);
        score.setText(gameView.getPlayer().getScore()+"");
        score.setX(size.x/2-scoreSize/2);
        score.setY(10);
        score.setTextSize(scoreSize);
        score.setBackgroundColor(0);
        score.setTextColor(Color.parseColor("#ffffff"));

        //High Score
        int highScoreSize = 50;
        highScore = new TextView(this);
        highScore.setText("Best : "+gameView.getPlayer().getHighScore());
        highScore.setX(btnPlay.getX() + startSize/2 - (highScoreSize*2));//Align to center of playbtn
        highScore.setY(btnPlay.getY()+startSize + highScoreSize/2);//5 px under playbtn
        highScore.setTextSize(highScoreSize);
        highScore.setBackgroundColor(0);
        highScore.setTextColor(Color.parseColor("#ffffff"));

        //On the game view
        gameWidgets.addView(btnPause);
        gameWidgets.addView(score);

        //Paused View
        pausedView.setBackgroundColor(Color.parseColor("#99000000"));
        //pausedView.addView(score);
        pausedView.addView(btnPlay);
        pausedView.addView(highScore);

        //Game
        game.addView(gameView);
        game.addView(gameWidgets);

        //Game Over View
        gameOverView.setBackgroundColor(Color.parseColor("#99000000"));
        gameOverView.addView(btnRestart);

        //Listener for pause button
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameView.pause();
                game.removeView(gameWidgets);
                game.addView(pausedView);
                if(score.getParent() != null)
                {
                    gameWidgets.removeView(score);
                    pausedView.addView(score);
                }
            }
        });

        //Listener for play button
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game.removeView(pausedView);
                game.addView(gameWidgets);
                gameView.resume();
            }
        });

        //Listener for restart button
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //game.removeView(gameOverView);
                //game.addView(gameWidgets);
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        //adding game frame to contentview
        setContentView(game);
    }

    public TextView getScoreView()
    {
        return score;
    }

    public void updateScore(int score)
    {
        this.score.setText(score+"");
    }

    public void updateHighScore(int hScore)
    {
        highScore.setText("Best : "+hScore);
    }

    public TextView getHighScoreText()
    {
        return highScore;
    }

    public FrameLayout getGame()
    {
        return game;
    }

    public android.os.Handler getMainHandler()
    {
        return mainHandler;
    }

    //Game over action
    public void gameOver()
    {
        game.removeView(gameOverView);
        game.removeView(gameWidgets);
        //Clear parent and set score to game over
        if(score.getParent().equals(gameWidgets)) {
            gameWidgets.removeView(score);
        }
        else {
            pausedView.removeView(score);
        }
        gameOverView.addView(score);

        pausedView.removeView(highScore);
        gameOverView.addView(highScore);

        game.addView(gameOverView);
        Log.d("Game Over View:", gameOverView.getParent().toString());
    }
}