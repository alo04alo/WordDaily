package com.hako.base;

import java.util.List;

import com.hako.utils.GlobalData;

public class WordHandle {
	public static DatabaseHandler db = GlobalData.db;
	
	public static List<Word> getListWord(int lessonId, int type, int amount){
		return db.getListWord(lessonId, type, amount);
	}
	
	public static List<Word> getListWord(int lessonId, int amount){
		return db.getListWord(lessonId, amount);
	}
}
