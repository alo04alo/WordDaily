package com.hako.word;

import com.hako.base.Lesson;

import android.app.Application;

public class WordSupport extends Application {
	public int current_lesson;
	public Lesson lesson;
	
	public WordSupport(){
		lesson = new Lesson();
	}

}
