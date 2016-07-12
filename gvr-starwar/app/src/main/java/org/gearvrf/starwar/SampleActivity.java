/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.gearvrf.starwar;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.util.Log;

import org.gearvrf.GVRActivity;

public class SampleActivity extends GVRActivity {
	private static final String TAG = "SampleActivity";
    private Camera camera;
    private PlanesManager pm;
	protected GamepadInput mGamepad;

	public SampleActivity() {
		mGamepad = new GamepadInput();
	}

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
		createCameraView();
		pm = new PlanesManager(this);
        setScript(pm, "gvr.xml");
    }

    @Override
	public boolean dispatchGenericMotionEvent(MotionEvent event) {
    	boolean processed = mGamepad.onGenericMotionEvent(event);
    	if (processed)
    		return true;

		return super.dispatchGenericMotionEvent(event);
	}

	@Override
    public boolean dispatchKeyEvent(KeyEvent event) {
		boolean processed = mGamepad.onKeyEvent(event);
		if (processed) {
			return true;
		}

        return super.dispatchKeyEvent(event);
    }

    Camera getCamera() {
        return camera;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pm.onTouchEvent(event);
        return super.onTouchEvent(event);
    }    
    
    @Override
    public void onPause() {
        super.onPause();
        pm.onPause();
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
    
    private boolean checkCameraHardware(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    private long prevTime = 0;
    private PreviewCallback previewCallback = new PreviewCallback() {

        @Override
        /**
         * The byte data comes from the android camera in the yuv format. so we
         * need to convert it to rgba format.
         */
        public void onPreviewFrame(byte[] data, Camera camera) {
            long currentTime = System.currentTimeMillis();
            Log.d(TAG,
                    "Preview Frame rate "
                            + Math.round(1000 / (currentTime - prevTime)));
            prevTime = currentTime;
            camera.addCallbackBuffer(previewCallbackBuffer);
        }
    };
    
    private byte[] previewCallbackBuffer = null;
    private void createCameraView() {

        if (!checkCameraHardware(this)) {
            android.util.Log.d(TAG, "Camera hardware not available.");
            return;
        }

        camera = null;

        try {
            camera = Camera.open();
            if (camera != null) {
                Parameters params = camera.getParameters();

                int bufferSize = params.getPreviewSize().height
                        * params.getPreviewSize().width
                        * ImageFormat
                                .getBitsPerPixel(params.getPreviewFormat()) / 8;
                previewCallbackBuffer = new byte[bufferSize];
                camera.addCallbackBuffer(previewCallbackBuffer);
                camera.setPreviewCallbackWithBuffer(previewCallback);
                camera.startPreview();
            }
        } catch (Exception exception) {
            android.util.Log.d(TAG, "Camera not available or is in use");
        }
    }


}
