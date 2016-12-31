package com.example.root.rockit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

/**
 * Created by root on 12/16/16.
 * The Player class +controls the player+
 */

public class Player
{
    //3X3 matrix for bitmap
    private Matrix matrix;

    //Bitmap to get character from image
    private Bitmap bitmap;

    //coordinates
    private int x;
    private int y;

    //the player's score
    private int score;
    private static int highScore;

    //motion speed of the character
    private int speed;

    //if player is moving
    private boolean boosting;

    private int tilt;
    private final int tiltLimit = 45;

    //Gravity Value to add gravity effect on the ship
    private final int GRAVITY = -10;

    //Controlling Y coordinate so that ship won't go outside the screen
    private int maxY;
    private int minY;

    //creating a rect object
    private Rect detectCollision;

    //Limit the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;

    //constructor
    public Player(Context context, int screenX, int screenY)
    {
        speed = 1;
        tilt = 0;
        score = 0;
        matrix = new Matrix();

        //Getting bitmap from drawable ship resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float newHeight = (float)screenY/(14f);
        float newWidth = newHeight + 30;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        //coordinates
        x = bitmap.getWidth();
        y = maxY/2;

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        //calculating maxY
        maxY = screenY - bitmap.getHeight(); //- (bitmap.getHeight()/2) - 40;
        //top edge's y point is 0 so min y will always bzero
        minY = 0;

        boosting = false;

        //initializing rect object
        detectCollision = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    //setting boosting true
    public void startBoosting()
    {
        boosting = true;
    }

    //setting boosting false
    public void stopBoosting()
    {
        boosting = false;
    }

    //setting boosting false
    public void tiltUp()
    {
        tilt -= 1;
        //check if it tilts less than -45
        if(tilt < -tiltLimit)
        {
            tilt = -tiltLimit;
        }
        //matrix.setSkew(x+5, y+5);
    }

    //setting boosting false
    public void tiltDown()
    {
        tilt += 1;
        //check if it tilts more than 45
        if(tilt > tiltLimit)
        {
            tilt = tiltLimit;
        }
        //matrix.setSkew(x, y+5);
    }

    //Rules of the player for the game loop
    public void update()
    {
        //Move ship
        //x--;
        //if the ship is boosting
        if (boosting) {
            //tilting speeding up the ship
            speed += 2;
            tiltUp();
        } else {
            //tilting and slowing down if not boosting
            speed -= 5;
            tiltDown();
        }
        //controlling the top speed
        if (speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //if the speed is less than min speed
        //controlling it so that it won't stop completely
        if (speed < MIN_SPEED)
        {
            speed = MIN_SPEED;
        }

        //moving the ship down
        y -= speed + GRAVITY;

        //but controlling it also so that it won't go off the screen
        if (y < minY)
        {
            y = minY;
        }
        if (y > maxY)
        {
            y = maxY;
        }

        //Adding the top, left, bottom and right to the rect object
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    //adding a setter to x coordinate so that we can change it after collision
    public void setX(int x)
    {
        this.x = x;
    }

    /*
    * These are getters you can generate it autmaticallyl
    * right click on editor -> generate -> getters
    * */

    //one more getter for getting the rect object
    public Rect getDetectCollision()
    {
        return detectCollision;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    public int getMaxY()
    {
        return maxY;
    }

    public int getMinY()
    {
        return minY;
    }

    public int getTilt()
    {
        return tilt;
    }

    public int getSpeed()
    {
        return speed;
    }

    public Matrix getMatrix()
    {
        return matrix;
    }

    public void setScore()
    {
        score++;
    }

    public void resetScore()
    {
        score = 0;
    }

    public void setHighScore(int bscore)
    {
        highScore = bscore;
    }

    public int getHighScore()
    {
        return highScore;
    }

    public int getScore()
    {
        return score;
    }
}
