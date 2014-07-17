package com.hako.word;

import android.app.Application;

public class WordSupport extends Application {
	int current_lesson;

	public int getCurrent_lesson() {
		return current_lesson;
	}

	public void setCurrent_lesson(int current_lesson) {
		this.current_lesson = current_lesson;
	}
}
