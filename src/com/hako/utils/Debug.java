package com.hako.utils;

import android.util.Log;

public final class Debug {
	private Debug() {
	}

	public static void out(Object msg) {
		Log.i("info", msg.toString());
	}
}
