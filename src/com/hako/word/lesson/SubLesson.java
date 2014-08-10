package com.hako.word.lesson;

import java.util.List;

import com.hako.base.DatabaseHandler;
import com.hako.base.Lesson;
import com.hako.matchword.MatchWordActivity;
import com.hako.utils.GlobalData;
import com.hako.word.MainActivity;
import com.hako.word.R;
import com.hako.word.exam.ExamTabActivity;
import com.hako.word.selectPicture.SelectPictureActivity;
import com.hako.word.viewPicture.ViewPictureActivity;
import com.hako.word.vocabulary.VocabularyTabBar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	
	Button btnOKRequire;
	Button btnCancelRequire;
	
	AlertDialog dialogLesson;
	Dialog dialogRequireTest;
	TextView tvRequireTest;
	List<Lesson> lessons;
	DatabaseHandler db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_lesson_view);
		// get currentID lesson
		current_lesson = GlobalData.current_lesson;
		lessons = GlobalData.lessons;
		
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
				current_lesson = GlobalData.current_lesson;
				// set new currentID lesson
				if (current_lesson == db.NumberOfLesson()) {
					GlobalData.current_lesson = 1;
				} else {
					GlobalData.current_lesson = current_lesson + 1;
				}
				
				btnLessonName.setText("Bài " + GlobalData.current_lesson);
			}
		});
		
		btnBackLesson.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				// get currentID lesson
				current_lesson = GlobalData.current_lesson;
				// set new currentID lesson
				if (current_lesson == 1) {
					GlobalData.current_lesson = db.NumberOfLesson();
				} else {
					GlobalData.current_lesson = current_lesson - 1;
				}				
				btnLessonName.setText("Bài " + GlobalData.current_lesson);
			}
		});
		
		btnLessonName.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// show dialog to list all lessons
				dialogLesson = createDialogLesson();
				dialogLesson.show();				
				
			}
		});
		
		btnVocabulary.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (checkEnableLesson() == true) {
					startActivity(new Intent(getApplicationContext(), VocabularyTabBar.class));
				} else {
					// show dialog to require testing 
					dialogRequireTest = createDialogRequireTest();
					dialogRequireTest.show();
					
				}				
			}
		});
		
		/**
		 * author:nhat anh
		 * start MatchWordActivity
		 */
		btnMatchWord.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SubLesson.this, MatchWordActivity.class));
			}
		});
		
		btnViewPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (checkEnableLesson() == true) {
					startActivity(new Intent(getApplicationContext(), ViewPictureActivity.class));
				} else {
					// show dialog to require testing 
					dialogRequireTest = createDialogRequireTest();
					dialogRequireTest.show();
					
				}				
			}
		});
		
		btnSelectPicture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SelectPictureActivity.class));
			}
		});
		
		btnTest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (checkEnableLesson() == true) {
					startActivity(new Intent(getApplicationContext(), ExamTabActivity.class));
				} else {
					// show dialog to require testing 
					dialogRequireTest = createDialogRequireTest();
					dialogRequireTest.show();
					
				}
			}
		});
	}

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
				Toast.makeText(getApplicationContext(), "Show test screen", Toast.LENGTH_LONG).show();
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

	protected AlertDialog createDialogLesson() {				
		String titleLesson[] = new String[lessons.size()];
		String descriptionLesson[] = new String[lessons.size()];	
		
		for (int index = 0; index < lessons.size(); index++) {
			Lesson lesson = lessons.get(index);
			titleLesson[index] = lesson.title;
			descriptionLesson[index] = lesson.discription;
		}		
            
        ContextThemeWrapper wrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo);
        LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(LAYOUT_INFLATER_SERVICE);
        
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(wrapper);  
        
        View convertView = (View) inflater.inflate(R.layout.sub_lesson_dialog, null);
        alertDialogBuilder.setView(convertView);
        alertDialogBuilder.setCancelable(true);      
        alertDialogBuilder.setInverseBackgroundForced(true);
        
        final AlertDialog alertDialog = alertDialogBuilder.create();
        
        ListView lv = (ListView) convertView.findViewById(R.id.lvSubLesson);
        lv.setAdapter(new SubLessonArrayAdapter(this, titleLesson, descriptionLesson));        
        lv.setClickable(true);
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GlobalData.current_lesson = position + 1;
                btnLessonName.setText("Bài " + GlobalData.current_lesson);     
                // hide dialog
                alertDialog.cancel();
            }
        });
        
		return alertDialog;
	}

	protected Boolean checkEnableLesson() {
		Lesson lesson = lessons.get(GlobalData.current_lesson - 1);
		if (GlobalData.current_lesson == 1) {
			return true;
		} 
		else if (lesson.is_lock == 1) {
			return true;
		} else {
			return false;
		}
	}

}
