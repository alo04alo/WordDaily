package com.hako.base;

import java.util.List;

import com.hako.utils.GlobalData;

public class WordHandle {
	public static DatabaseHandler db = GlobalData.db;
	
	public static final int ALL_KIND = -1;
	public static final int KIND_NORMAL = 0;
	public static final int KIND_HAS_IMAGE = 1;
	
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
