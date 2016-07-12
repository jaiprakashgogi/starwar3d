package org.gearvrf.starwar;

import org.joml.Vector3f;

// This is the player
public class Character {
	Vector3f mPosition = new Vector3f();
	Vector3f mSpeed = new Vector3f();
	Vector3f mDeltaPerFrame = new Vector3f();

	float mSpeedFactor = 5f;

	public Character() {
	}

	public void onInputChange(float speedX, float speedY) {
		mSpeed.x = speedX * mSpeedFactor;
		mSpeed.y = -speedY * mSpeedFactor;

		mDeltaPerFrame.set(mSpeed);
		mDeltaPerFrame.mul(1f / 60 /* FPS */);
	}

	public void onStep() {
		mPosition.add(mDeltaPerFrame);
	}

	public Vector3f getPosition() {
		return mPosition;
	}
}
