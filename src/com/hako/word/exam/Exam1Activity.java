package com.hako.word.exam;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.hako.base.CountDownTimerWithPause;
import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.R;
import com.hako.word.lesson.SubLesson;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Exam1Activity extends Activity {
	
	private Button btnAnswer1;
	private Button btnAnswer2;
	private Button btnAnswer3;
	private Button btnAnswer4;
	private TextView tvQuestion;
	private TextView tvTimer;
	
	private List<Word> words;
	private int count = 0;
	private int positionTrueAnswer;
	
	private CountDownTimer timer;
	private CountDownTimerWithPause examTimer;
	private boolean isCounting = false;	
	private int totalTestTime = 180000;
	private boolean isPaused = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_1_tab);
		
		btnAnswer1 = (Button) findViewById(R.id.exam_1_answer1);
		btnAnswer2 = (Button) findViewById(R.id.exam_1_answer2);
		btnAnswer3 = (Button) findViewById(R.id.exam_1_answer3);
		btnAnswer4 = (Button) findViewById(R.id.exam_1_answer4);
		
		tvQuestion = (TextView) findViewById(R.id.exam_1_tvQuesion);
		tvTimer = (TextView) findViewById(R.id.exam_1_timer);
		
		words = WordHandle.getRandomListWord(GlobalData.current_lesson, GlobalData.WORD_INCLUDE_IMAGE, GlobalData.TEST_LIMIT);
		GlobalData.currentExam = 1;
		GlobalData.testData = words;			
		examTimer =  createTimer(totalTestTime, false);
		examTimer.pause();		
		
		loadNewQuestion();

		btnAnswer1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {		
				if (!examTimer.isRunning()) {
					examTimer.resume();
				}
				handleAnswer(1);
			}
		});

		btnAnswer2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				if (!examTimer.isRunning()) {
					examTimer.resume();
				}
				handleAnswer(2);		
			}
		});

		btnAnswer3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				if (!examTimer.isRunning()) {
					examTimer.resume();
				}
				handleAnswer(3);
			}
		});

		btnAnswer4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				if (!examTimer.isRunning()) {
					examTimer.resume();
				}
				handleAnswer(4);
			}
		});
	}
				

	@Override
	public void onBackPressed() {
		examTimer.pause();
		AlertDialog.Builder al = new AlertDialog.Builder(this)	
			.setTitle("Are you sure finish test ?")
			.setIcon(R.drawable.ico_warning)		
			.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					examTimer.cancel();
					startActivity(new Intent(Exam1Activity.this, SubLesson.class));
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					examTimer.resume();
				}
		});
		Dialog dialog = al.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(false);
		dialog.show();
	}



	protected void loadNewQuestion() {
		if (count == GlobalData.TEST_LIMIT) {
			// show result screen
			startActivity(new Intent(Exam1Activity.this, TestResultActivity.class));
			return;
		}
		isCounting = false;
		// reset all answer buttons
		resetButton();
		// set text for question textview
		tvQuestion.setText(words.get(count).mean_vi);
		// set text for answer buttons
		setTextForAnswers();
		// increase value of count variable
		count++;
	}

	private void setTextForAnswers() {		
		// set true answer
		int number_of_answer = 4; // default number answer
		Random random = new Random();
		Button button;

		positionTrueAnswer = random.nextInt(number_of_answer) + 1;
		button = getButtonFromId(positionTrueAnswer);
		button.setText(words.get(count).hiragana);

		int[] numbers = GlobalData.getRandomThreeNumber(0, GlobalData.TEST_LIMIT - 1, count);		
		int index = 0;
		
		for (int i = 1; i <= number_of_answer; i++) {
			if (i != positionTrueAnswer) {
				button = getButtonFromId(i);
				button.setText(words.get(numbers[index]).hiragana);
				index++;
			}
		}
	}

	private void handleAnswer(int selectedAnswer) {		
		if (isCounting == false) {
			timer = new MyCountDownTimer(1000, 100);
			timer.start();
			isCounting = true;
		}
		
		String answer = null;
		Button selectedButton;		
		resetButton();
		selectedButton = getButtonFromId(selectedAnswer);
		selectedButton.setBackgroundResource(R.drawable.btn_answer_selected_background);
		answer = selectedButton.getText().toString();		
		
		GlobalData.testData.get(count-1).choose_answer = answer;		
	}
	
	private void resetButton() {
		for (int i = 1; i <= 4; i++) {
			Button btn = getButtonFromId(i);
			btn.setBackgroundResource(R.drawable.btn_answer_background);
		}
	}
	
	private Button getButtonFromId(int id){
		switch (id) {
		case 1:
			return btnAnswer1;
		case 2:
			return btnAnswer2;
		case 3:
			return btnAnswer3;
		case 4:
			return btnAnswer4;
		default:
			return null;
		}
	}	
	
	@Override
	protected void onPause() {
		super.onPause();
		isPaused = true;
		examTimer.pause();
	}



	@Override
	protected void onResume() {
		super.onResume();
		if (isPaused == true) {
			examTimer.resume();
			isPaused = false;
		}
	}

    private CountDownTimerWithPause createTimer(long millisOnTimer, boolean isRunning) {
      
        return new CountDownTimerWithPause(millisOnTimer, 1000, isRunning) {
	        public void onTick(long millisUntilFinished) {
	        	long millis = millisUntilFinished;  
                String hms = String.format("%02d:%02d",  
                    TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),  
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));  
                tvTimer.setText(hms);  
	        }
	        
	        public void onFinish() {
	          startActivity(new Intent(Exam1Activity.this, TestResultActivity.class));
	        }
        }.create();
    }
	
	
	public class MyCountDownTimer extends CountDownTimer {	
		  public MyCountDownTimer(long startTime, long interval) {
		   super(startTime, interval);		   
		  }
		 
		  @Override
		  public void onFinish() {
			  loadNewQuestion();
		  }
		 
		  @Override
		  public void onTick(long millisUntilFinished) {			                          
		  }
	 }	
}

