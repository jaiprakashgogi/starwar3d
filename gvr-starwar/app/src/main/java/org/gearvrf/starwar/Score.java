package org.gearvrf.starwar;

/**
 * Created by jaiprakashgogi on 7/11/16.
 */

public class Score {
    private int shoot_count;
    private int hit_count;
    PlanesManager pm;

    private double[][] positions;

    Score(PlanesManager _pm){
        pm = _pm;
        shoot_count = 0;
        hit_count = 0;
        positions = new double[pm.getCount()][3];
    }


    public void updatePositionChange(int index, double[] _position) {
        positions[index][0] = _position[0];
        positions[index][1] = _position[1];
        positions[index][2] = _position[2];

    }

    public void updatePlaneShot(int index) {
        shoot_count = shoot_count + 1;
    }

    public void updatePlaneHit(int index) {
        hit_count = hit_count + 1;
    }
}
