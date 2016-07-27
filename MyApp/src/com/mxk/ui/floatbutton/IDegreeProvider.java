package com.mxk.ui.floatbutton;

import com.mxk.ui.floatbutton.SatelliteMenu.ExpandState;


/**
 * Interface for providing degrees between satellites. 
 * 
 * @author Siyamed SINIR
 *
 */
public interface IDegreeProvider {
	public float[] getDegrees(int count, float totalDegrees, ExpandState expandState);
	
	public float[] getDegrees(int count, float totalDegrees);
}
