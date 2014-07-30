package com.hako.base;

import java.util.List;

import com.hako.utils.GlobalData;

public class WordHandle {
	public static DatabaseHandler db = GlobalData.db;
	
	public static List<Word> getListWord(int lessonId, int type){
		return db.getListWord(lessonId, type);
	}
	
	public static List<Word> getListWord(int lessonId){
		return db.getListWord(lessonId);
	}
	
	public static List<Word> getRandomListWord(int lessonId, int kind, int limit){
		return db.getRandomListWord(lessonId, kind, limit);
	}
}
