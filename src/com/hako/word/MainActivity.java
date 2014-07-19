package com.hako.word;

import java.util.ArrayList;
import java.util.List;

import com.hako.base.DatabaseHandler;
import com.hako.base.Lesson;
import com.hako.word.R;
import com.hako.word.lesson.SubLesson;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

public class MainActivity extends Activity {
	
	GridView gridView;
	ArrayList<Item> gridArray = new ArrayList<Item>();
	ListItemGridView customGridAdapter;
	DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// init db
		this.initDB(this);
		
		// Get Lessons from DB
		List<Lesson> lessons = db.getAllLesson();
		Bitmap icon;
		for (int index = 0; index < lessons.size(); index++){
			Lesson lesson = lessons.get(index);
			icon = this.getImageFromResource(lesson.img);
			gridArray.add(new Item(icon,lesson.title));
		}
		
		// Add Lessons to gridview
		gridView = (GridView) findViewById(R.id.gv_lessons);
		customGridAdapter = new ListItemGridView(this, R.layout.home_gridview, gridArray);
		gridView.setAdapter(customGridAdapter);
		
		// Open TabBar when click gridview
		gridView.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            	Intent i = new Intent(MainActivity.this, SubLesson.class);
            	((WordSupport)getApplication()).current_lesson = position + 1;
            	startActivity(i);
            }
        });
	}
	
	public void initDB(Context context){
		db = new DatabaseHandler(context);
		if (db.checkDatabaseExist() == false) {			
			db.checkAndCopyDatabase();
		}
		db.openDataBase();
	}
	
	public Bitmap getImageFromResource(String name){
		int drawableResourceId = this.getResources().getIdentifier(name, "drawable", this.getPackageName());
		return BitmapFactory.decodeResource(this.getResources(), drawableResourceId);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
