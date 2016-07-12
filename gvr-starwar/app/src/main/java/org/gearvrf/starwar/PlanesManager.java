package org.gearvrf.starwar;

import java.util.ArrayList;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRCameraRig;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRRenderData.GVRRenderingOrder;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRScript;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRCameraSceneObject;
import org.gearvrf.starwar.GamepadInput.GamepadListener;
import org.gearvrf.utility.Log;
import org.joml.Vector3f;

public class PlanesManager extends GVRScript implements PositionListener {

	private static final String TAG = "PlanesManager";

	private SampleActivity mActivity;

	ArrayList<Planes> planeArray;
	private int count = 5;
	Score mScore;
	GVRContext mGVRContext;

	// Character
	Character mPlayer = new Character();

	public PlanesManager(SampleActivity activity) {
		mActivity = activity;
		mActivity.mGamepad.setGamepadXYListener(mGamepadListener);
	}

	@Override
	public void onInit(GVRContext gvrContext) throws Throwable {
		// TODO Auto-generated method stub
		// Add plane objects
		mGVRContext = gvrContext;
		mScore = new Score(this);
		planeArray = new ArrayList<Planes>();
		for (int i = 0; i < count; i++) {
			Planes myPlane = new Planes(mGVRContext, i);
			myPlane.setPositionListener(this);
			planeArray.add(myPlane);
		}

		GVRTexture texture = gvrContext.loadTexture(new GVRAndroidResource(mGVRContext, R.drawable.pointer));
		GVRSceneObject pointer = new GVRSceneObject(gvrContext, 4.0f, 2.0f, texture);
		pointer.getTransform().setPosition(0, 0, -1f);
		pointer.getTransform().setScale(0.2f, 0.2f, 0.2f);
		pointer.getRenderData().setDepthTest(false);
		pointer.getRenderData().setRenderingOrder(GVRRenderingOrder.OVERLAY);
		gvrContext.getMainScene().getMainCameraRig().addChildObject(pointer);
		
		GVRCameraSceneObject cameraObject = new GVRCameraSceneObject(
                gvrContext, 3.6f, 2.0f, mActivity.getCamera());
		cameraObject.setUpCameraForVrMode(1); 
		cameraObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
		gvrContext.getMainScene().getMainCameraRig().addChildObject(cameraObject);
	}

	public int getCount() {
		return count;
	}

	@Override
	public void onStep() {
		mPlayer.onStep();
		Vector3f playerPos = mPlayer.getPosition();
		updateCameraPos(playerPos);
	}

	@Override
	public void onPositionChange(int index, double[] _position) {
		// Log.i(TAG, "onPositionChange");
		mScore.updatePositionChange(index, _position);
	}

	@Override
	public void onPlaneShot(int index) {
		mScore.updatePlaneShot(index);
		Planes hitPlane = planeArray.get(index);
		try {
			// hitPlane.finalize();
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
			// hitPlane.finalize();
			Planes myPlane = new Planes(mGVRContext, index);
			myPlane.setPositionListener(this);
			planeArray.add(index, myPlane);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	private GamepadListener mGamepadListener = new GamepadListener() {
		@Override
		public void onAxisData(int axisID, float x, float y) {
			Log.d(TAG, "AxisData %f %f", x, y);
			mPlayer.onInputChange(x, y);
		}

		@Override
		public boolean onButtonUp(int keycode) {
			return false;
		}

		@Override
		public boolean onButtonDown(int keycode) {
			return false;
		}
	};

	public void onPause() {
		// TODO Auto-generated method stub
	}

	protected void updateCameraPos(Vector3f playerPos) {
		Log.d(TAG, "updateCameraPos %s", playerPos);
		GVRScene mainScene = mGVRContext.getMainScene();
		GVRCameraRig rig = mainScene.getMainCameraRig();
		rig.getTransform().setPosition(playerPos.x, playerPos.y, playerPos.z);
	}
}
