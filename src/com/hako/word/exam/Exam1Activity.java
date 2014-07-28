package com.hako.word.exam;

import java.util.List;
import java.util.Random;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
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
	private int selectedAnswer;
	
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
				selectedAnswer = 1;
				handleAnswer();
			}
		});

		btnAnswer2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {		
				selectedAnswer = 2;
				handleAnswer();		
			}
		});

		btnAnswer3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				selectedAnswer = 3;
				handleAnswer();
			}
		});

		btnAnswer4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {	
				selectedAnswer = 4;
				handleAnswer();
			}
		});
	}
	protected void loadNewQuestion() {

		if (count == GlobalData.TEST_LIMIT) {
			// show result screen
			Toast.makeText(getApplicationContext(), "Full rui nho", Toast.LENGTH_LONG).show();
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
		int min = 1;
		int max = 4;
		Random random = new Random();

		positionTrueAnswer = random.nextInt(max - min + 1) + min;					
		setTextForButton(positionTrueAnswer, words.get(count).hiragana);

		int[] numbers = getRandomThreeNumber(0, GlobalData.TEST_LIMIT - 1, count);		
		int index = 0;
//		Toast.makeText(getApplicationContext(), numbers[0] + "_" + numbers[1] + "_" + numbers[2], Toast.LENGTH_LONG).show();
		// set remain answers
		for (int i = min; i <= max; i++) {
			if (i != positionTrueAnswer) {
				setTextForButton(i, words.get(numbers[index]).hiragana);
				index++;
			}
		}
	}

	private int[] getRandomThreeNumber(int min, int max, int ignore) {			

		int[]  number = new int[3];
		number[0] = getRandomOneNumber(min, max, ignore);
		number[1] = getRandomOneNumber(min, max, ignore);
		number[2] = getRandomOneNumber(min, max, ignore);

		if ((number[0] != number[1]) && (number[0] != number[2]) && (number[1] != number[2])) {
			return number;
		} else {
			return getRandomThreeNumber(min, max, ignore);
		}

	}

	private int getRandomOneNumber(int min, int max, int ignore) {
		Random random = new Random();
		int number = random.nextInt(max - min + 1) + min;
		if (number != ignore) {
			return number;
		} else {
			return getRandomOneNumber(min, max, ignore);
		}
	}

	private void setTextForButton(int id, String text) {
		switch (id) {
		case 1:
			btnAnswer1.setText(text);
			break;
		case 2:
			btnAnswer2.setText(text);
			break;
		case 3:
			btnAnswer3.setText(text);
			break;
		case 4: btnAnswer4.setText(text);
			break;
		default:
			break;
		}
	}

	private void handleAnswer() {		
		// if the answer is true
		if (selectedAnswer == positionTrueAnswer) {
			switch (selectedAnswer) {
			case 1:
				setColorForButton(btnAnswer1, true);
				break;
			case 2:
				setColorForButton(btnAnswer2, true);
				break;
			case 3:
				setColorForButton(btnAnswer3, true);
				break;
			case 4:
				setColorForButton(btnAnswer4, true);
				break;
			default:
				break;
			}

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {	
					// load new question
					loadNewQuestion();
				}
			}, 1000);
		}
//		 if the answer is fail
		else {
			switch (selectedAnswer) {
			case 1:
				setColorForButton(btnAnswer1, false);
				break;
			case 2:
				setColorForButton(btnAnswer2, false);
				break;
			case 3:
				setColorForButton(btnAnswer3, false);
				break;
			case 4:
				setColorForButton(btnAnswer4, false);
				break;
			default:
				break;
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {	
					// load new question
					loadNewQuestion();
				}
			}, 1000);
		}
	}

	private void setColorForButton(Button button, boolean bool) {
		AnimationDrawable animation = new AnimationDrawable();
		if (bool == true) {
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_border_true_answer), 200);
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_border_true_answer), 200);
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		} else {
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_border_false_answer), 200);
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_border_false_answer), 200);
			animation.addFrame(getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		}
		animation.setOneShot(true);
		button.setBackgroundDrawable(animation);	
		animation.start();
	}
}
