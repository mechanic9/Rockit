package com.example.root.rockit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by root on 12/16/16.
 * Runs the game loop
 */

public class GameView extends SurfaceView implements Runnable
{
    Obstacle temp;
    int tempNo;
    //boolean variable to track if the game is playing or not
    volatile boolean playing;

    //the game thread for the loop
    private Thread gameThread = null;

    //adding the player to this class
    private Player player;
    //adding obstacle
    //private Obstacle obstacle;

    private int screenX, screenY;

    //These objects will be used for drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Adding an stars list
    private ArrayList<Star> stars = new
            ArrayList<Star>();


    //Adding an handler
    private ObstacleHandler obstacleHandler;
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();


    //Class constructor
    public GameView(Context context, int screenX, int screenY)
    {
        super(context);

        this.screenX = screenX;
        this.screenY = screenY;

        //initializing player object with screen args
        player = new Player(context, screenX, screenY);
        //syncing obstacle with player above
        //obstacle = new Obstacle(context, player, screenX, screenY);

        //initializing drawing objects
        surfaceHolder = getHolder();
        paint = new Paint();

        //adding 100 stars
        int starNums = 100;
        for (int i = 0; i < starNums; i++)
        {
            Star s = new Star(screenX, screenY);
            stars.add(s);
        }

        obstacleHandler = new ObstacleHandler(context, player, screenX, screenY);
        obstacles = obstacleHandler.getObstacles();
        tempNo = 1;
        temp = obstacles.get(tempNo-1);
    }


    @Override
    public void run()
    {
        while(playing)
        {
            //to update the frame
            update();

            //to draw the frame
            playDraw();

            //to control
            control();
        }
    }

    //Player Controls
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent)
    {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_UP:
                //stopping the boosting when screen is released
                player.stopBoosting();
                break;

            case MotionEvent.ACTION_DOWN:
                //boosting when screen is pressed
                player.startBoosting();
                break;
        }
        return true;
    }

    private void update()
    {
        //updating player position
        player.update();

        obstacleHandler.update();

            //Updating the stars with player speed
            for (Star s : stars)
            {
                s.update(obstacleHandler.getSpeed());
            }

        for(Obstacle o : obstacles)
        {
            //if collision occurrs with player
            if (Rect.intersects(player.getDetectCollision(), o.getDetectCollisionTop())
                    || Rect.intersects(player.getDetectCollision(), o.getDetectCollisionBottom())
                    || player.getY() == player.getMaxY()  //Play touches screen edge
                    || player.getY() == player.getMinY())
            {
                //moving enemy outside the left edge
                //o.setX(-200);
                if(player.getScore() > player.getHighScore())
                {
                    player.setHighScore(player.getScore());
                    //display score
                }

                player.resetScore();
                //Vibrator v = (Vibrator) this.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 500 milliseconds
                /**    Log.v ("Can Vibrate", "YES");
                }
                else
                {
                    Log.v("Can Vibrate", "NO");
                }
               // v.vibrate(10);*/
            }

            //if player left(x) passes obstacle right(x+width)
            else if(player.getX() < o.getTrailX() && player.getX() > o.getX())
            {
                //check for unique obstacle
                if(temp.equals(o))
                {
                    player.setScore();
                    temp = obstacles.get(tempNo++);
                    if(tempNo == obstacles.size())
                    {
                        tempNo = 0;
                    }
                }
            }

        }
    }

    private void playDraw()
    {
        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid())
        {
            //locking the canvas
            canvas = surfaceHolder.lockCanvas();
            //drawing a background color for canvas
            canvas.drawColor(Color.BLACK);
            //Drawing the player

            //setting the paint color to white to draw the stars
            paint.setColor(Color.WHITE);

            //drawing all stars
            for (Star s : stars)
            {
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(), s.getY(), paint);
            }

            //Matrix m = player.getMatrix();
            //m.preRotate(player.getTilt());
            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);
            //drawing all obstacles
            for(Obstacle o : obstacles)
            {
                for(int i = 0; i < o.getTopWalls(); i++)
                {
                    //Drawing top obstacle
                    canvas.drawBitmap(
                            o.getBitmap(),
                            o.getX(),
                            o.getUpY(i),
                            paint);
                }

                for(int i = 0; i < o.getBottomWalls(); i++)
                {
                    //Drawing bottom obstacle
                    canvas.drawBitmap(
                            o.getBitmap(),
                            o.getX(),
                            o.getDownY(i),
                            paint);
                }

                //Score text
                int tsize = 80;
                paint.setTextSize(tsize);
                canvas.drawText(""+player.getScore(), screenX/2-tsize/2, tsize, paint);
            }

            //Unlocking the canvas
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control()
    {
        try
        {
            gameThread.sleep(17);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void pause()
    {
        //when the game is paused
        //setting the variable to false
        playing = false;
        try
        {
            //stopping the thread
            gameThread.join();
        }
        catch (InterruptedException e)
        {

        }
    }

    public Player getPlayer()
    {
        return player;
    }

    public ObstacleHandler getObstacleHandler()
    {
        return obstacleHandler;
    }

    public void setPlaying(boolean play)
    {
        playing = play;
    }

    public void resume()
    {
        //when the game is resumed
        //starting the thread again
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void gameOver()
    {
        pause();
        //display score
    }
}
