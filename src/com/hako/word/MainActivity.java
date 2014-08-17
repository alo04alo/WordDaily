package com.hako.word;

import java.util.ArrayList;
import java.util.List;

import com.hako.base.DatabaseHandler;
import com.hako.base.Lesson;
import com.hako.utils.GlobalData;
import com.hako.word.R;
import com.hako.word.exam.ExamTabActivity;
import com.hako.word.lesson.SubLesson;
import com.hako.word.vocabulary.VocabularyTabBar;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
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
		// open database and save db in GlobalData.
		db = GlobalData.openDatabase(this); 		
		
		// Get Lessons from DB
		List<Lesson> lessons = db.getAllLesson();
		GlobalData.lessons = lessons; // Save lessons in GlobalData
		
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
            	GlobalData.current_lesson = position + 1;
            	if (GlobalData.checkEnableLesson() == true) {
	            	startActivity(new Intent(MainActivity.this, SubLesson.class));
				} else {
					// show dialog to require testing 
					dialogRequireTest = createDialogRequireTest();
					dialogRequireTest.show();
					
				}
            }
        });
	}
	
	Button btnOKRequire;
	Button btnCancelRequire;
	AlertDialog dialogLesson;
	Dialog dialogRequireTest;
	TextView tvRequireTest;
	
	protected Dialog createDialogRequireTest() {
		ContextThemeWrapper wrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Dialog);
		
		LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(LAYOUT_INFLATER_SERVICE);			
		
		View viewDialog = (View) inflater.inflate(R.layout.common_dialog_require_test, null);
		
		AlertDialog.Builder builder = new AlertDialog.Builder(wrapper);
		builder.setView(viewDialog);
		builder.setCancelable(true);
		builder.setInverseBackgroundForced(true);				
		
		final Dialog alertDialog = builder.create();
		
		tvRequireTest = (TextView) viewDialog.findViewById(R.id.tvRequireTest);		
		tvRequireTest.setText("Bạn chưa vượt qua bài kiểm tra số " + (GlobalData.current_lesson - 1));
		

		btnOKRequire = (Button) viewDialog.findViewById(R.id.btnOKRequire);
		btnCancelRequire = (Button) viewDialog.findViewById(R.id.btnCancelRequire);
		
		btnOKRequire.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// show Test Screen
				if (GlobalData.current_lesson > 0)
					GlobalData.current_lesson = GlobalData.current_lesson - 1;
				startActivity(new Intent(getApplicationContext(), ExamTabActivity.class));
			}
		});
		
		btnCancelRequire.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
				
			}
		});			
		
		return alertDialog;
	}
		
	public Bitmap getImageFromResource(String name){
		return this.getImageFromResource(name, 2.5, 2.5);
	}
	
	public Bitmap getImageFromResource(String name, double w, double h){
		
		int drawableResourceId = this.getResources().getIdentifier(name, "drawable", this.getPackageName());
		Bitmap image = BitmapFactory.decodeResource(this.getResources(), drawableResourceId);
		if (w > 0 && h > 0){
			image = GlobalData.scaleImage(image, w, h);
		} 
		return image;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
