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
import org.gearvrf.IPickEvents;
import org.gearvrf.scene_objects.GVRCameraSceneObject;
import org.gearvrf.starwar.GamepadInput.GamepadListener;
import org.gearvrf.utility.Log;
import org.joml.Vector3f;
import org.gearvrf.GVRPicker;
import android.view.MotionEvent;

public class PlanesManager extends GVRScript implements PositionListener {
    
        public class PickHandler implements IPickEvents
        {
            public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo)
            {
                //sceneObj.getRenderData().getMaterial().setColor(LOOKAT_COLOR_MASK_R, LOOKAT_COLOR_MASK_G, LOOKAT_COLOR_MASK_B);
                mPickedObject = (Planes)sceneObj;
                //Log.e("DDD", "onEnter");
            }
            public void onExit(GVRSceneObject sceneObj)
            {
                mPickedObject = null;
                //Log.e("DDD", "onExit");
                //sceneObj.getRenderData().getMaterial().setColor(1.0f, 1.0f, 1.0f);
            }
            public void onNoPick(GVRPicker picker){}
            public void onPick(GVRPicker picker) {}
            public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) {}      
        }

        private Planes mPickedObject = null;
        private IPickEvents mPickHandler;
        private GVRScene mScene;
        
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

		mScene = gvrContext.getMainScene();
		
		GVRTexture texture = gvrContext.loadTexture(new GVRAndroidResource(mGVRContext, R.drawable.pointer));
		GVRSceneObject pointer = new GVRSceneObject(gvrContext, 4.0f, 2.0f, texture);
		pointer.getTransform().setPosition(0, 0, -1f);
		pointer.getTransform().setScale(0.2f, 0.2f, 0.2f);
		pointer.getRenderData().setDepthTest(false);
		pointer.getRenderData().setRenderingOrder(GVRRenderingOrder.OVERLAY);
		mScene.getMainCameraRig().addChildObject(pointer);
		
		GVRCameraSceneObject cameraObject = new GVRCameraSceneObject(
                gvrContext, 3.6f, 2.0f, mActivity.getCamera());
		cameraObject.setUpCameraForVrMode(1); 
		cameraObject.getTransform().setPosition(0.0f, 0.0f, -4.0f);
		mScene.getMainCameraRig().addChildObject(cameraObject);
		
	        mPickHandler = new PickHandler();
	        mScene.getEventReceiver().addListener(mPickHandler);
	        mScene.getMainCameraRig().getOwnerObject().attachComponent(new GVRPicker(gvrContext, mScene));
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
			hitPlane.finalize();
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
	
	public void onTouchEvent(MotionEvent event) {
	    switch (event.getAction() & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN:
	        //Log.e("DDD", "ACTION_DOWN");
	        if (mPickedObject!=null) {
	            mScene.removeSceneObject(mPickedObject);
	            onPlaneShot(mPickedObject.id);
	         }
	        break;
	     case MotionEvent.ACTION_CANCEL:
	     case MotionEvent.ACTION_UP:
	     case MotionEvent.ACTION_MOVE:
	     default:
	        break;
	     }
	 }

	protected void updateCameraPos(Vector3f playerPos) {
		Log.d(TAG, "updateCameraPos %s", playerPos);
		GVRScene mainScene = mGVRContext.getMainScene();
		GVRCameraRig rig = mainScene.getMainCameraRig();
		rig.getTransform().setPosition(playerPos.x, playerPos.y, playerPos.z);
	}
}
