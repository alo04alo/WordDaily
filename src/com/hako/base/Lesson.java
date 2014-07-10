package com.hako.base;

public class Lesson {
	public int id;
	public String title;
	public String discription;
	public String img;
	public int score;
	public int is_lock;
	
	public Lesson(int id, String title, String discription, String img, int score, int is_lock){
		this.id = id;
		this.title = title;
		this.discription = discription;
		this.img = img;
		this.score = score;
		this.is_lock = is_lock;
	}
	
	public Lesson(){
		
	}
}
