package com.mxk.ui.floatbutton;

import com.mxk.ui.floatbutton.SatelliteMenu.ExpandState;



/**
 * Default provider for degrees between satellites. For number of satellites up to 3
 * tries to keep satellites centered in the given total degrees. For number equal and
 * bigger than four, distirbutes evenly using min and max degrees. 
 *  
 * @author Siyamed SINIR
 *
 */
public class DefaultDegreeProvider implements IDegreeProvider
{
    public float[] getDegrees(int count, float totalDegrees)
    {
        return getDegrees(count, totalDegrees, ExpandState.STATE_LEFT_BOTTOM);
    }

    @Override
    public float[] getDegrees(int count, float totalDegrees, ExpandState expandState)
    {
        // TODO Auto-generated method stub
        float startDegree = 0f;
        int rotateDirection = 1;
        switch (expandState.ordinal())
        {
            case 0 :
                startDegree = 0f;
                rotateDirection = -1;
                break;
            case 1 :
                startDegree = 180f;
                rotateDirection = 1;
                break;
            case 2 :
                startDegree = 0f;
                rotateDirection = 1;
                break;
            case 3 :
                startDegree = 180f;
                rotateDirection = -1;
                break;

            default :
                break;
        }
        if (count < 1)
        {
            return new float[]{};
        }
        else if (count == 1)
        {
            return new float[]{startDegree};
        }

        float[] result = null;
        result = new float[count];
        float delta = totalDegrees / (count - 1) * rotateDirection;

        for (int index = 0; index < count; index++)
        {
            result[index] = startDegree + index * delta;
        }

        return result;
    }

}
