package com.hako.word.lesson;

import com.hako.base.DatabaseHandler;
import com.hako.word.MainActivity;
import com.hako.word.R;
import com.hako.word.WordSupport;
import com.hako.word.vocabulary.VocabularyTabBar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SubLesson extends Activity {
	int current_lesson;
	Button btnLessonName;
	Button btnHome;
	Button btnNextLesson;
	Button btnBackLesson;
	
	Button btnVocabulary;
	Button btnMatchWord;
	Button btnSelectPicture;
	Button btnListenWord;
	Button btnViewPicture;
	Button btnTest;
	
	DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_lesson_view);
		// get currentID lesson
		current_lesson = ((WordSupport)getApplication()).current_lesson;
		
		btnHome = (Button) findViewById(R.id.btn_home);
		btnLessonName = (Button) findViewById(R.id.btn_down);
		btnNextLesson = (Button) findViewById(R.id.btn_next);
		btnBackLesson = (Button) findViewById(R.id.btn_previous);
		
		btnVocabulary = (Button) findViewById(R.id.btn_vocabulary);
		btnMatchWord = (Button) findViewById(R.id.btn_matching_word);
		btnSelectPicture = (Button) findViewById(R.id.btn_selecting_picture);
		btnListenWord = (Button) findViewById(R.id.btn_listening_word);
		btnViewPicture = (Button) findViewById(R.id.btn_view_picture);
		btnTest = (Button) findViewById(R.id.btn_test);
		
		btnLessonName.setText("Bài " + current_lesson);
		
		db =  new DatabaseHandler(getApplication());
		db.openDataBase();
		
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
			}
		});
		
		btnNextLesson.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get currentID lesson
				current_lesson = ((WordSupport)getApplication()).current_lesson;
				// set new currentID lesson
				if (current_lesson == db.NumberOfLesson()) {
					((WordSupport) getApplication()).current_lesson = 1;
				} else {
					((WordSupport) getApplication()).current_lesson = current_lesson + 1;
				}
				
//				String title = 
				btnLessonName.setText("Bài " + ((WordSupport) getApplication()).current_lesson);
			}
		});
		
		btnBackLesson.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				// get currentID lesson
				current_lesson = ((WordSupport)getApplication()).current_lesson;
				// set new currentID lesson
				if (current_lesson == 1) {
					((WordSupport) getApplication()).current_lesson = db.NumberOfLesson();
				} else {
					((WordSupport) getApplication()).current_lesson = current_lesson - 1;
				}				
				btnLessonName.setText("Bài " + ((WordSupport) getApplication()).current_lesson);
			}
		});
		
		btnLessonName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// show dialog to list all lessons
				
			}
		});
		
		btnVocabulary.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				checkEnableLesson();
				startActivity(new Intent(getApplicationContext(), VocabularyTabBar.class));
				
			}
		});
	}

	protected void checkEnableLesson() {
		// TODO Auto-generated method stub
		
	}

}
