package com.example.root.rockit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by root on 12/17/16.
 * The Obstacle class +controls the obstacle+
 */

public class Obstacle
{
    //3X3 matrix for bitmap
    private Matrix matrix;

    //Bitmap to get character from image
    private Bitmap bitmap;
    private Player player;

    private int height;

    private int x;
    private int y;
    private int speed;

    private int maxX;
    private int maxY;
    private int minX;
    private int minY;

    private boolean isVisible;

    //private int upX;
    private int newX;
    private int upY;
    private int downY;

    //Limit the bounds of the obstacle's speed
    private final int MIN_SPEED = 10;
    private final int MAX_SPEED = 30;

    private int topWalls, bottomWalls, wallsPerY;

    Random generator;

    //creating a rect object
    private Rect detectCollisionTop, detectCollisionBottom;

    //constructor
    public Obstacle(Context context, Player player, int screenX, int screenY, int xA)
    {
        matrix = new Matrix();

        maxX = screenX;
        maxY = screenY;
        minX = 0;
        minY = 0;

        generator = new Random();

        this.player = player;
        //Getting bitmap from drawable gun resource
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wall);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newHeight = screenY/6;
        int newWidth = newHeight;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // recreate the new Bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        wallsPerY = (screenY - player.getBitmap().getHeight()*3)/bitmap.getHeight();
        topWalls = generator.nextInt(wallsPerY-1)+1;
        bottomWalls = wallsPerY - getTopWalls();

        upY = 0;
        downY = maxY - bitmap.getHeight();

        x = xA;
        newX = x;

        speed = 10;
        isVisible = true;

        //initializing rect object
        detectCollisionTop = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight()*topWalls);

        //initializing rect object
        detectCollisionBottom = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight()*bottomWalls);

    }

    public void reset(int neX)
    {
        x = neX;
        isVisible = true;
        //height = generator.nextInt(90);
    }

    public void scroll()
    {
        x -= speed;
        if(getTrailX() < minX)
        {
            isVisible = false;
        }
    }

    //Logic for the game loop
    public void update()
    {
        scroll();

        //Adding the top, left, bottom and right to the rect object
        detectCollisionTop.left = x;
        detectCollisionTop.top = upY;
        detectCollisionTop.right = x + bitmap.getWidth();
        detectCollisionTop.bottom = upY + bitmap.getHeight()*topWalls;

        //Adding the top, left, bottom and right to the rect object
        detectCollisionBottom.left = x;
        detectCollisionBottom.top = maxY - bitmap.getHeight()*bottomWalls;
        detectCollisionBottom.right = x + bitmap.getWidth();
        detectCollisionBottom.bottom = maxY;
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
    public Rect getDetectCollisionTop()
    {
        return detectCollisionTop;
    }

    public Rect getDetectCollisionBottom()
    {
        return detectCollisionBottom;
    }

    public int getTopWalls()
    {
        return topWalls;
    }


    public int getBottomWalls()
    {
        //Random no
        return bottomWalls;
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public int getX()
    {
        return x;
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public int getUpY(int block)
    {
        //1 for 1st block
        //if(block > 0)
        //{
        return upY + (bitmap.getHeight() * block);
        //}
        //else return upY;

    }

    public int getDownY(int block)
    {
        //1 for 1st block
        //if(block > 0)
        //{
        return downY - (bitmap.getHeight() * block );
        //}
        //else return downY;
    }

    public int getSpeed()
    {
        return speed;
    }

    public int getTrailX()
    {
        return x + bitmap.getWidth();
    }
}
