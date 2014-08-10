package com.hako.word.exam;

import java.util.List;
import java.util.Random;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Exam1Activity extends Activity{
	
	private Button btnAnswer1;
	private Button btnAnswer2;
	private Button btnAnswer3;
	private Button btnAnswer4;
	private TextView tvQuestion;
	
	private List<Word> words;
	private int count = 0;
	private int positionTrueAnswer;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_1_tab);
		
		btnAnswer1 = (Button) findViewById(R.id.exam_1_answer1);
		btnAnswer2 = (Button) findViewById(R.id.exam_1_answer2);
		btnAnswer3 = (Button) findViewById(R.id.exam_1_answer3);
		btnAnswer4 = (Button) findViewById(R.id.exam_1_answer4);
		
		tvQuestion = (TextView) findViewById(R.id.exam_1_tvQuesion);
		words = WordHandle.getRandomListWord(GlobalData.current_lesson, GlobalData.WORD_INCLUDE_IMAGE, GlobalData.TEST_LIMIT);
		
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
	}
	protected void loadNewQuestion() {
		if (count == GlobalData.TEST_LIMIT) {
			// show result screen
			Toast.makeText(getApplicationContext(), "Waitting Final Screen..... :))", Toast.LENGTH_LONG).show();
			return;
		}
		
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
		// result = true if the answer is true else false
		String answer = null;
		Button trueButton;
		Button falseButton;
		
		if (selectedAnswer == positionTrueAnswer){
			trueButton = getButtonFromId(selectedAnswer); 
			answer = trueButton.getText().toString();
			GlobalData.setAnimationForButton(this, trueButton, true); // set Animation if correct answer
		} else {
			trueButton = getButtonFromId(positionTrueAnswer);
			falseButton = getButtonFromId(selectedAnswer);
			answer = falseButton.getText().toString();
			GlobalData.setAnimationForButton(this, falseButton, trueButton); // set Animation if fail answer
		}
		
		words.get(count-1).choose_answer = answer;
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {	
				// load new question
				loadNewQuestion();
			}
		}, 1000);
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
	
}
