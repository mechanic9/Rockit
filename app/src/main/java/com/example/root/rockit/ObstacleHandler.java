package com.example.root.rockit;

import android.content.Context;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by root on 12/27/16.
 */

public class ObstacleHandler
{
    private int wallTotal;
    
    private Obstacle wall1, wall2, wall3, wall4;
    public static final int SCROLL_SPEED = -59;
    public static final int WALL_GAP = 200;
    //Adding an wall list
    private ArrayList<Obstacle> obstacles = new ArrayList<Obstacle>();

    private Obstacle prototype;

    public ObstacleHandler(Context context, Player player, int screenX, int screenY)
    {
        /**
        wall1 = new Obstacle(context, player, screenX, screenY, screenX);
        wall2 = new Obstacle(context, player, screenX, screenY, wall1.getTrailX() + WALL_GAP);
        wall3 = new Obstacle(context, player, screenX, screenY, wall2.getTrailX() + WALL_GAP);
        wall4 = new Obstacle(context, player, screenX, screenY, wall3.getTrailX() + WALL_GAP);
         */
        prototype = new Obstacle(context, player,screenX,screenY, screenX);
        wallTotal = screenX/(prototype.getBitmap().getWidth()+WALL_GAP);

        int x;
        for(int o = 0; o < wallTotal; o++)
        {
            //int wall = o + 1;
            //if this is the first wall
            if(o == 0)
            {
                //x is default
                x = screenX;
                //Add wall
                obstacles.add(new Obstacle(context, player,screenX,screenY,x));
            }
            else
            {
                //x depends on previous wall
                x = obstacles.get(o-1).getTrailX() + WALL_GAP;
                //Add wall
                obstacles.add(new Obstacle(context, player,screenX,screenY,x));

            }
        }

        /**
        obstacles.add(wall1);
        obstacles.add(wall2);
        obstacles.add(wall3);
        obstacles.add(wall4);*/
    }

    public void update()
    {
        // Update our objects
        for(int i = 0; i < wallTotal; i++)
        {
            //update all walls
            obstacles.get(i).update();

            // Check if any of the walls are invisible,
            // and reset accordingly

            //if it's the first wall
            if(i == 0)
            {
                //reset to trail of last obstacle in array
                if (obstacles.get(i).isVisible() == false)
                {
                    obstacles.get(i).reset(obstacles.get(wallTotal-1).getTrailX() + WALL_GAP);
                }
            }
            //for every other wall
            else
            {
                if (obstacles.get(i).isVisible() == false)
                {
                    //Reset to trail of previous
                    obstacles.get(i).reset(obstacles.get(i - 1).getTrailX() + WALL_GAP);
                }
            }
        }
    }

    public ArrayList<Obstacle> getObstacles()
    {
        return obstacles;
    }

    public int getSpeed()
    {
        return obstacles.get(0).getSpeed();
    }

}
