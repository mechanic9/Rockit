package com.example.root.rockit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by root on 12/29/16.
 */

public class ViewMask extends SurfaceView
{

    //These objects will be used for drawing
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Class constructor
    public ViewMask(Context context)
    {
        super(context);
    }

    @Override
    public void draw(Canvas canvas)
    {

        //checking if surface is valid
        if (surfaceHolder.getSurface().isValid())
        {
            canvas = surfaceHolder.lockCanvas();

            //drawing a background color for canvas
            canvas.drawARGB(60, 255, 255, 255);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
}
