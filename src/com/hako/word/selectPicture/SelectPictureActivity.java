package com.hako.word.selectPicture;

import java.util.List;
import java.util.Random;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.MainActivity;
import com.hako.word.R;
import com.hako.word.viewPicture.ViewPictureActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPictureActivity extends Activity {
	Button btn1;
	Button btn2;
	Button btn3;
	Button btn4;
	
	ImageView img1;
	ImageView img2;
	ImageView img3;
	ImageView img4;
	
	Button btnRecommend;
	Button btnAudio;
	Button btnHome;
	
	TextView tvRecommend;
	
	List<Word> words;
	private int count = 0;
	private int positionTrueAnswer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.select_picture_view);
		
		btn1 = (Button) findViewById(R.id.select_picture_btn1);
		btn2 = (Button) findViewById(R.id.select_picture_btn2);
		btn3 = (Button) findViewById(R.id.select_picture_btn3);
		btn4 = (Button) findViewById(R.id.select_picture_btn4);
		
		img1 = (ImageView) findViewById(R.id.select_picture_img1);
		img2 = (ImageView) findViewById(R.id.select_picture_img2);
		img3 = (ImageView) findViewById(R.id.select_picture_img3);
		img4 = (ImageView) findViewById(R.id.select_picture_img4);		
		
		btnRecommend = (Button) findViewById(R.id.select_picture_btn_recommend);
		btnAudio = (Button) findViewById(R.id.select_picture_btn_audio);
		btnHome = (Button) findViewById(R.id.select_picture_btn_home);
		
		tvRecommend = (TextView) findViewById(R.id.select_picture_tv);
		
		// get number of words from DB
		words = WordHandle.getRandomListWord(GlobalData.current_lesson, GlobalData.WORD_INCLUDE_IMAGE, GlobalData.WORD_LIMIT);		
		
		loadNewQuestion();
		
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(1);
			}
		});
		
		img1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(1);
			}
		});
		
		btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(2);

			}
		});
		
		img2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(2);

			}
		});
		
		btn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(3);
			}
		});
		
		img3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(3);
			}
		});
		
		btn4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(4);
			}
		});
		
		img4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handleAnswer(4);
			}
		});
		
		btnAudio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				String fileName = words.get(count - 1).romaji + ".mp3";
				GlobalData.playAudioFromAsset(SelectPictureActivity.this, fileName);
			}
		});
		
		btnRecommend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showText(tvRecommend, words.get(count - 1).hiragana);
			}
		});
		
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));
			}
		});
	}
	
	protected void handleAnswer(int selectedAnswer) {
		Button trueButton;
		Button falseButton;
		String answer = null;
		if (selectedAnswer == positionTrueAnswer) {
			trueButton = getButtonFromId(selectedAnswer); 	
			answer = trueButton.getText().toString();
			GlobalData.setAnimationForButton(this, trueButton);
		} else {
			trueButton = getButtonFromId(positionTrueAnswer);
			falseButton = getButtonFromId(selectedAnswer);			
			answer = falseButton.getText().toString();
			GlobalData.setAnimationForButton(this, falseButton, trueButton);
		}
		
		words.get(count - 1).choose_answer = answer;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {	
				// load new question
				loadNewQuestion();
			}
		}, 1000);
	}

	private void loadNewQuestion() {
		if (count == GlobalData.TEST_LIMIT) {
			// show result screen
			Toast.makeText(getApplicationContext(), "Waitting Final Screen..... :))", Toast.LENGTH_LONG).show();
			return;
		}
		
		// hide Text recommend
		hideText(tvRecommend);
		
		// play audio
		String fileName = words.get(count).romaji + ".mp3";
		GlobalData.playAudioFromAsset(SelectPictureActivity.this, fileName);
		
		// set image for answer image button
		setImageForAnswers();
		// increase value of count variable
		count++;
		
	}

	private void setImageForAnswers() {
		// set true answer
		int min = 1;
		int max = 4;
		Random random = new Random();

		positionTrueAnswer = random.nextInt(max - min + 1) + min;					
		setImageForButton(positionTrueAnswer, GlobalData.getImageFromRaw(getApplicationContext(), words.get(count).romaji));
		setTextForButton(positionTrueAnswer, words.get(count).romaji);
		
		int[] numbers = GlobalData.getRandomThreeNumber(0, GlobalData.TEST_LIMIT - 1, count);		
		int index = 0;
		
		for (int i = min; i <= max; i++) {
			if (i != positionTrueAnswer) {
				setImageForButton(i, GlobalData.getImageFromRaw(getApplicationContext(), words.get(numbers[index]).romaji));
				setTextForButton(i, words.get(numbers[index]).romaji);
				index++;
			}
		}
	}

	private void setImageForButton(int id, Bitmap bm) {	
		ImageView img = getImageViewFromId(id);	
		img.setImageBitmap(bm);
	}
	
	private void setTextForButton(int id, String text) {	
		Button btn = getButtonFromId(id);
		btn.setText(text);
		// text is exist but can not see
		btn.setTextSize(0);
	}
	
	private Button getButtonFromId(int id){
		switch (id) {
		case 1:
			return btn1;
		case 2:
			return btn2;
		case 3:
			return btn3;
		case 4:
			return btn4;
		default:
			return null;
		}
	}
	
	private ImageView getImageViewFromId(int id){
		switch (id) {
		case 1:
			return img1;
		case 2:
			return img2;
		case 3:
			return img3;
		case 4:
			return img4;
		default:
			return null;
		}
	}

	private void showText(TextView tv, String text) {
		tv.setVisibility(View.VISIBLE);
		tv.setText(text);
	}
	
	private void hideText(TextView tv) {
		tv.setVisibility(View.GONE);		
	}
	
}
