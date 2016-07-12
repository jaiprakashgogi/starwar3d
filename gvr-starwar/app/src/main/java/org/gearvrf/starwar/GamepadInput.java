package org.gearvrf.starwar;

import org.gearvrf.utility.Log;

import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GamepadInput implements InputHandler {
	private static final String TAG = Log.tag(GamepadInput.class);

	public static interface GamepadListener {
		void onAxisData(int axisID, float x, float y);
		boolean onButtonUp(int keyCode);
		boolean onButtonDown(int keyCode);
	}

	protected GamepadListener mGamepadListener;

	public GamepadInput() {
	}
	
	@Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) ==
                InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            // Process all historical movement samples in the batch
            final int historySize = event.getHistorySize();

            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(event, i);
            }

            // Process the current movement sample in the batch (position -1)
            processJoystickInput(event, -1);
            return true;
        }

        return false;
    }

    private void processJoystickInput(MotionEvent event,
            int historyPos) {

        InputDevice inputDevice = event.getDevice();

        // Calculate the horizontal distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat axis, or the right control stick.
        float x = getCenteredAxis(event, inputDevice,
                MotionEvent.AXIS_X, historyPos);
        if (x == 0) {
            x = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_HAT_X, historyPos);
        }
        if (x == 0) {
            x = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_Z, historyPos);
        }

        // Calculate the vertical distance to move by
        // using the input value from one of these physical controls:
        // the left control stick, hat switch, or the right control stick.
        float y = getCenteredAxis(event, inputDevice,
                MotionEvent.AXIS_Y, historyPos);
        if (y == 0) {
            y = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_HAT_Y, historyPos);
        }
        if (y == 0) {
            y = getCenteredAxis(event, inputDevice,
                    MotionEvent.AXIS_RZ, historyPos);
        }

        // Update the ship object based on the new x and y values
        handleXYValues(x, y);
    }

    private void handleXYValues(float x, float y) {
		if (mGamepadListener == null)
			return;

		mGamepadListener.onAxisData(0 /* not used */, x, y);
	}

	private static float getCenteredAxis(MotionEvent event,
            InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range =
                device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value =
                    historyPos < 0 ? event.getAxisValue(axis):
                    event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }
        return 0;
    }

	public void setGamepadXYListener(GamepadListener gamepadXYListener) {
		mGamepadListener = gamepadXYListener;
	}

	@Override
	public boolean onKeyEvent(KeyEvent event) {
		if (mGamepadListener == null)
			return false;

		if (event.getAction() == KeyEvent.ACTION_UP)
			return mGamepadListener.onButtonUp(event.getKeyCode());
		else if (event.getAction() == KeyEvent.ACTION_DOWN)
			return mGamepadListener.onButtonDown(event.getKeyCode());

		return false;
	}
}
