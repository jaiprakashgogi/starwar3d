package org.gearvrf.starwar;

/**
 * Created by jaiprakashgogi on 7/11/16.
 */
public interface PositionListener {
    public void onPositionChange(int index, double[] _position);
    public void onPlaneShot(int index);
    public void onPlaneHit(int index);
}
