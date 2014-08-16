package com.hako.word.exam;

import java.util.List;
import java.util.Random;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.R;
import com.hako.word.exam.Exam1Activity.MyCountDownTimer;
import com.hako.word.viewPicture.ViewPictureActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings.Global;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Exam3Activity extends Activity{
	
	private Button btnAnswer1;
	private Button btnAnswer2;
	private Button btnAnswer3;
	private Button btnAnswer4;
	private Button btnQuestion;
	
	private List<Word> words;
	private int count = 0;
	private int positionTrueAnswer;
	
	private CountDownTimer timer;
	private boolean isCounting = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_3_tab);
		
		btnAnswer1 = (Button) findViewById(R.id.exam_3_answer1);
		btnAnswer2 = (Button) findViewById(R.id.exam_3_answer2);
		btnAnswer3 = (Button) findViewById(R.id.exam_3_answer3);
		btnAnswer4 = (Button) findViewById(R.id.exam_3_answer4);
		
		btnQuestion = (Button) findViewById(R.id.exam_3_audio);
		words = WordHandle.getRandomListWord(GlobalData.current_lesson, GlobalData.WORD_INCLUDE_IMAGE, GlobalData.TEST_LIMIT);
		GlobalData.currentExam = 3;
		GlobalData.testData = words;
		loadNewQuestion();

		btnAnswer1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				handleAnswer(1);
			}
		});

		btnAnswer2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {		
				handleAnswer(2);		
			}
		});

		btnAnswer3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				handleAnswer(3);
			}
		});

		btnAnswer4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				handleAnswer(4);
			}
		});
		
		btnQuestion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String fileName = words.get(count - 1).romaji + ".mp3";
				GlobalData.playAudioFromAsset(Exam3Activity.this, fileName);
			}
		});
	}
	protected void loadNewQuestion() {
		if (count == GlobalData.TEST_LIMIT) {
			// show result screen
			startActivity(new Intent(Exam3Activity.this, TestResultActivity.class));
			return;
		}
		
		isCounting = false;
		// reset all answer buttons
		resetButton();
		
		GlobalData.playAudioFromAsset(this, words.get(count).romaji + ".mp3");
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
		button.setText(words.get(count).mean_vi);

		int[] numbers = GlobalData.getRandomThreeNumber(0, GlobalData.TEST_LIMIT - 1, count);		
		int index = 0;
		
		for (int i = 1; i <= number_of_answer; i++) {
			if (i != positionTrueAnswer) {
				button = getButtonFromId(i);
				button.setText(words.get(numbers[index]).mean_vi);
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
