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
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPictureActivity extends Activity{
	
	Button btnAnswer1;
	Button btnAnswer2;
	Button btnAnswer3;
	Button btnAnswer4;
	
	Button btnHome;
	Button btnShowFuction;
	
	Button btnRecommend;
	Button btnAudio;
	
	TextView tvWord;
	
	ImageView imgWord;	
	
	int amountWords = 0;
	int type = 0;
	int count = 0;
		
	List<Word> words;
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
		
		
		// only get words which has picture
		type = 1;
		// set amount of words need to get
		amountWords = 20;
		
		// get number of words from DB
		words = WordHandle.getListWord(GlobalData.current_lesson, type, amountWords);
		
		
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
				loadNewQuestion();
			}
		});
		
		btnAnswer2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				loadNewQuestion();
			}
		});
		
		btnAnswer3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				loadNewQuestion();
			}
		});
		
		btnAnswer4.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {			
				loadNewQuestion();
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
				Toast.makeText(getApplicationContext(), words.get(count - 1).romaji, Toast.LENGTH_SHORT).show();
				playAudio(words.get(count - 1).romaji + ".mp3");
			}
		});

	}

	protected void loadNewQuestion() {
		// hide text
		hideText();
		
		if (count == amountWords) {
			// show result screen
			Toast.makeText(getApplicationContext(), "Full rui nho", Toast.LENGTH_LONG).show();
			return;
		}
//			Toast.makeText(getApplicationContext(), words.get(count).romaji, Toast.LENGTH_SHORT).show();
		
		Bitmap bitmap = GlobalData.getImageFromRaw(this, words.get(count).romaji);
		imgWord.setImageBitmap(bitmap);
		
		// set text for answer buttons
		setTextForAnswers();
		// increase value of count variable
		count++;
	}
	
	
	private void playAudio(String fileName) {
		MediaPlayer player = new MediaPlayer();
		try {
			AssetFileDescriptor assetFile = getAssets().openFd(fileName);		
			player.setDataSource(assetFile.getFileDescriptor(), assetFile.getStartOffset(), assetFile.getLength());
			player.seekTo(0);		
			player.start();
			Runtime.getRuntime().gc();
		} catch (Exception e) {
			// Error handling
		}
		
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
		int min = 1;
		int max = 4;
		Random random = new Random();
		
		int positionTrueAnswer = random.nextInt(max - min + 1) + min;					
		setTextForButton(positionTrueAnswer, words.get(count).hiragana);
		
		int[] numbers = getRandomThreeNumber(0, amountWords - 1, count);		
		int index = 0;
		
		// set remain answers
		for (int i = min; i <= max; i++) {
			if (i != positionTrueAnswer) {
				setTextForButton(i, words.get(numbers[index]).hiragana);
				index++;
			}
		}
	}
	
	private int[] getRandomThreeNumber(int min, int max, int ignore) {
		
		Random random = new Random();
		
		int[]  number = new int[3];
		number[0] = random.nextInt(max - min + 1) + min;
		number[1] = random.nextInt(max - min + 1) + min;
		number[2] = random.nextInt(max - min + 1) + min;
		
		if ((number[0] != number[1]) && (number[0] != number[2]) && (number[1] != number[2])) {
			return number;
		} else {
			return getRandomThreeNumber(min, max, ignore);
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

}
