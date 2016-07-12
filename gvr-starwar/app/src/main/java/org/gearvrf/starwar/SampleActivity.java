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

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;

import org.gearvrf.GVRActivity;

public class SampleActivity extends GVRActivity {
	protected GamepadInput mGamepad;

	public SampleActivity() {
		mGamepad = new GamepadInput();
	}

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setScript(new PlanesManager(this), "gvr.xml");
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
}
