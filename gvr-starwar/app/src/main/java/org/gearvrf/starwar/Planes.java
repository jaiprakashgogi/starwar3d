package org.gearvrf.starwar;

import java.io.IOException;
import java.util.Random;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRDrawFrameListener;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;

import android.util.Log;

import org.gearvrf.GVRRenderData.GVRRenderingOrder;

public class Planes extends GVRSceneObject implements GVRDrawFrameListener {
	private double[] curr_position;
	private double[] velocity; // in cm/sec
	private float scale;
	private boolean isVisible;
	private GVRScene scene;
	private int id;
	public static final String TAG = "Planes";
	private PositionListener mPositionListener;

	public Planes(GVRContext _gvrContext, GVRMesh _mesh, GVRTexture texture) {
		super(_gvrContext, _mesh, texture);
		// TODO Auto-generated constructor stub
		scene = _gvrContext.getMainScene();
		_gvrContext.registerDrawFrameListener(this);
		curr_position = new double[3];
		velocity = new double[3];
		scale = 0.01f;
		isVisible = true;
		// choosing a random position
		UniformHemisphereSampler();
		scene.addSceneObject(this);
		//Log.i(TAG, "Constructor 2");
	}

	public void setPositionListener(PositionListener _PositionListener) {
		mPositionListener = _PositionListener;
	}

	public Planes(GVRContext _gvrContext, int _id) throws IOException {
		//choose a random mesh
		/*super(_gvrContext, _gvrContext.loadMesh(
				new GVRAndroidResource(_gvrContext.getContext(), "obj" + new Random().nextInt(9) + 1 + ".obj")), 
				_gvrContext.loadTexture(new GVRAndroidResource(_gvrContext
                        .getContext(), "tex" + new Random().nextInt(9) + 1 + ".jpg")));*/
		this(_gvrContext, _gvrContext.loadMesh(
				new GVRAndroidResource(_gvrContext.getContext(), "obj" + new Random().nextInt(4) + ".obj")), 
				_gvrContext.loadTexture(new GVRAndroidResource(_gvrContext
                        .getContext(), "tex1.jpg")));
		this.id = _id;
		//Log.i(TAG, "Constructor 1");

	}

	@Override
	public void onDrawFrame(float arg0) {
		// TODO Auto-generated method stub
		if (isVisible) {
			curr_position[0] = curr_position[0] + velocity[0] * 0.01;
			curr_position[1] = curr_position[1] + velocity[1] * 0.01;
			curr_position[2] = curr_position[2] + velocity[2] * 0.01;
			getTransform().setPosition((float) curr_position[0], (float) curr_position[1], (float) curr_position[2]);
			if( Math.sqrt(curr_position[0] * curr_position[0] + curr_position[1] * curr_position[1] + curr_position[2] *curr_position[2]) < 0.10){
				isVisible = false;
				mPositionListener.onPlaneHit(id);
			}
			mPositionListener.onPositionChange(id, curr_position);
		}
		
		if(!isVisible) {
			scene.removeSceneObject(this);
		}
	}

	double myRandom() {
		int MAX = 10000;
		Random rn = new Random();
		double answer = (double) (rn.nextInt(MAX) + 1);
		return answer / MAX;
	}

	void UniformHemisphereSampler() {
		Log.i(TAG, "UniformHemisphereSampler");
		double Xi1 = myRandom();
		double Xi2 = myRandom();
		double theta = Math.acos(Xi1);
		double phi = 2.0 * Math.PI * Xi2;

		double xs = - Math.sin(theta) * Math.cos(phi);
		double ys = - Math.sin(theta) * Math.sin(phi);
		double zs = - Math.cos(theta);
		double dist = myRandom() / 2.0 + 9.0;
		//scale = (float) (myRandom() / 2.0 + 0.5);
		
		curr_position[0] = dist * xs;
		curr_position[1] = dist * ys;
		curr_position[2] = dist * zs;
		getTransform().setPosition((float) curr_position[0], (float) curr_position[1], (float) curr_position[2]);
		getTransform().setScale(scale, scale, scale);
        getRenderData().setDepthTest(false);
        getRenderData().setRenderingOrder(GVRRenderingOrder.OVERLAY);

		velocity[0] = -xs;
		velocity[1] = -ys;
		velocity[2] = -zs;

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}
}
