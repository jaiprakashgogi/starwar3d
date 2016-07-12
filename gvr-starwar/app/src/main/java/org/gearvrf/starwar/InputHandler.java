package org.gearvrf.starwar;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface InputHandler {
	boolean onGenericMotionEvent(MotionEvent event);

	boolean onKeyEvent(KeyEvent event);
}
