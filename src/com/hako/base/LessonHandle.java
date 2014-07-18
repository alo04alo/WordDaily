package com.hako.base;
import java.util.ArrayList;
import java.util.List;	

final public class LessonHandle {
	public List<Lesson> lessons;
	
	public LessonHandle(){
		this.lessons = new ArrayList<Lesson>();
	}
	
	public int getNumberLesson(){
		return this.lessons.size();
	}
	
}
