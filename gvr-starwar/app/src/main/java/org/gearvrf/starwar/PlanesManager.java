package org.gearvrf.starwar;

import java.io.IOException;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRScript;

public class PlanesManager extends GVRScript {

	@Override
	public void onInit(GVRContext mGVRContext) throws Throwable {
		// TODO Auto-generated method stub
		// Add plane objects
		try {
			new Planes(mGVRContext);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onStep() {
		// TODO Auto-generated method stub

	}

}
