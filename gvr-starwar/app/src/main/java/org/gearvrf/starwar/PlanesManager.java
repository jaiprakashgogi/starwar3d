package org.gearvrf.starwar;

import android.media.Image;

import java.io.IOException;
import java.util.ArrayList;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRScript;
import org.gearvrf.utility.Log;

public class PlanesManager extends GVRScript implements PositionListener {

	private static final String TAG = "PlanesManager";

	ArrayList<Planes> planeArray;
	private int count = 20;
	Score mScore;
	GVRContext mGVRContext;

	@Override
	public void onInit(GVRContext _GVRContext) throws Throwable {
		// TODO Auto-generated method stub
		// Add plane objects
		mGVRContext = _GVRContext;
		mScore = new Score(this);
		planeArray = new ArrayList<Planes>();

		for(int i=0; i<count; i++){
			Planes myPlane = new Planes(mGVRContext, i);
			myPlane.setPositionListener(this);
			planeArray.add(myPlane);
		}
	}

	public int getCount() {
		return count;
	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub

	}


	@Override
	public void onPositionChange(int index, double[] _position) {
		//Log.i(TAG, "onPositionChange");
		mScore.updatePositionChange(index, _position);
	}

	@Override
	public void onPlaneShot(int index) {
		mScore.updatePlaneShot(index);
		Planes hitPlane = planeArray.get(index);
		try {
			//hitPlane.finalize();
			Planes myPlane = new Planes(mGVRContext, index);
			myPlane.setPositionListener(this);
			planeArray.add(index, myPlane);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	@Override
	public void onPlaneHit(int index) {
		Log.i(TAG, "onPlaneHit: " + index);
		mScore.updatePlaneHit(index);
		Planes hitPlane = planeArray.get(index);
		try {
			//hitPlane.finalize();
			Planes myPlane = new Planes(mGVRContext, index);
			myPlane.setPositionListener(this);
			planeArray.add(index, myPlane);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}
}
