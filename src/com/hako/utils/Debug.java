package com.hako.utils;

import android.util.Log;

public final class Debug {
	private Debug() {
	}

	public static void out(Object msg) {
		Log.i("info", msg.toString());
	}
	
	public static void log(Object msg) {
		// red for clear =))
		Log.e("nhatanh-log", msg.toString());
	}
}
