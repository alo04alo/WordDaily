package com.hako.word.viewPicture;

import java.util.List;
import java.util.Random;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.MainActivity;
import com.hako.word.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPictureActivity extends Activity{
	
	private Button btnAnswer1;
	private Button btnAnswer2;
	private Button btnAnswer3;
	private Button btnAnswer4;
	private Button btnHome;
	private Button btnShowFuction;
	private Button btnRecommend;
	private Button btnAudio;
	private TextView tvWord;
	private ImageView imgWord;	
	
	private int count = 0;
	private int positionTrueAnswer;
	private List<Word> words;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_picture_view);
		
		btnHome = (Button) findViewById(R.id.view_picture_btn_home);
		btnShowFuction = (Button) findViewById(R.id.view_picture_btn_down);
		
		btnAnswer1 = (Button) findViewById(R.id.view_picture_answer1);
		btnAnswer2 = (Button) findViewById(R.id.view_picture_answer2);
		btnAnswer3 = (Button) findViewById(R.id.view_picture_answer3);
		btnAnswer4 = (Button) findViewById(R.id.view_picture_answer4);
		
		btnRecommend = (Button) findViewById(R.id.view_picture_btn_recommend);
		btnAudio = (Button) findViewById(R.id.view_picture_btn_audio);
		
		tvWord = (TextView) findViewById(R.id.view_picture_tv);
		imgWord = (ImageView) findViewById(R.id.view_picture_img);
		
		// get number of words from DB
		words = WordHandle.getRandomListWord(GlobalData.current_lesson, GlobalData.WORD_INCLUDE_IMAGE, GlobalData.WORD_LIMIT);
		
		loadNewQuestion();
		
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));				
			}
		});
		
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
		
		btnRecommend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showText();				
			}
		});
		
		btnAudio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String fileName = words.get(count - 1).romaji + ".mp3";
				GlobalData.playAudioFromAsset(ViewPictureActivity.this, fileName);
			}
		});
		
		btnShowFuction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {			
				
			}
		});

	}

	protected void loadNewQuestion() {
		// hide text
		hideText();
		
		if (count == GlobalData.WORD_LIMIT) {
			// show result screen
			Toast.makeText(getApplicationContext(), "Waitting final screen here,.....", Toast.LENGTH_LONG).show();
			return;
		}
		
		Bitmap bitmap = GlobalData.getImageFromRaw(this, words.get(count).romaji);
		imgWord.setImageBitmap(bitmap);
		
		// set text for answer buttons
		setTextForAnswers();
		// increase value of count variable
		count++;
	}
		
	
	private void showText() {
		tvWord.setVisibility(View.VISIBLE);
		tvWord.setText(words.get(count - 1).mean_vi);
	}
	
	private void hideText() {
		tvWord.setVisibility(View.GONE);		
	}
	
	private void setTextForAnswers() {		
		// set true answer
		int number_of_answer = 4;
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
			GlobalData.setAnimationForButton(this, trueButton);
		} else {
			trueButton = getButtonFromId(positionTrueAnswer);
			falseButton = getButtonFromId(selectedAnswer);
			answer = falseButton.getText().toString();
			GlobalData.setAnimationForButton(this, falseButton, trueButton);
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