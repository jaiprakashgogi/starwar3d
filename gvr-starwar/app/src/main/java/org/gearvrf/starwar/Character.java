package org.gearvrf.starwar;

import org.joml.Vector3f;

// This is the player
public class Character {
	Vector3f mPosition = new Vector3f();
	Vector3f mSpeed = new Vector3f();
	Vector3f mDeltaPerFrame = new Vector3f();

	static float mRangeX = 4;
	static float mRangeY = 2;

	float mSpeedFactor = 2f;

	public Character() {
	}

	public void onInputChange(float speedX, float speedY) {
		mSpeed.x = speedX * mSpeedFactor;
		mSpeed.y = -speedY * mSpeedFactor;

		mDeltaPerFrame.set(mSpeed);
		mDeltaPerFrame.mul(1f / 60 /* FPS */);
	}

	public void resetPos() {
		mPosition.set(0, 0, 0);
	}

	public void onStep() {
		mPosition.add(mDeltaPerFrame);

		mPosition.x =  Math.max(-mRangeX, mPosition.x);
		mPosition.x =  Math.min(mRangeX, mPosition.x);

		mPosition.y =  Math.max(-mRangeY, mPosition.y);
		mPosition.y =  Math.min(mRangeY, mPosition.y);
	}

	public Vector3f getPosition() {
		return mPosition;
	}
}
