package com.hako.word.selectPicture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.MainActivity;
import com.hako.word.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
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
	private boolean flagTrue = false;
	private List<Button>  disibleList;
		
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
		
		img1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						btn1.setBackgroundResource(R.drawable.btn_answer_background_press);
						break;
					case MotionEvent.ACTION_UP:
						handleAnswer(1);
						break;
				}
				return true;
			}
		});
		
		img2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						btn2.setBackgroundResource(R.drawable.btn_answer_background_press);
						break;
					case MotionEvent.ACTION_UP:
						handleAnswer(2);
						break;
				}
				return true;
			}
		});
		
		img3.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						btn3.setBackgroundResource(R.drawable.btn_answer_background_press);
						break;
					case MotionEvent.ACTION_UP:
						handleAnswer(3);
						break;
				}
				return true;
			}
		});

		img4.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						btn4.setBackgroundResource(R.drawable.btn_answer_background_press);
						break;
					case MotionEvent.ACTION_UP:
						handleAnswer(4);
						break;
				}
				return true;
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
		if (flagTrue == true && disibleList.contains(getButtonFromId(selectedAnswer)) == false) {
			Button selected = getButtonFromId(selectedAnswer);
			selected.setBackgroundResource(R.drawable.btn_answer_background);
			return;
		}
		
		Button trueButton;
		Button falseButton;
		ImageView falseImage;
		ImageView trueImage;
		String answer = null;
		if (selectedAnswer == positionTrueAnswer) {
			flagTrue = true;
			
			trueButton = getButtonFromId(selectedAnswer);
			trueImage = getImageViewFromId(selectedAnswer);
			
			trueButton.setEnabled(false);
			trueImage.setEnabled(false);
			
			disibleList.add(trueButton);
			
			answer = trueButton.getText().toString();
			GlobalData.setAnimationForButton(this, trueButton, true);
			
			words.get(count - 1).choose_answer = answer;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {	
					// load new question
					loadNewQuestion();
				}
			}, 1000);
		} else {
			falseImage = getImageViewFromId(selectedAnswer);
			falseButton = getButtonFromId(selectedAnswer);		
			
			disibleList.add(falseButton);
			
			falseButton.setBackgroundColor(Color.RED);
			falseButton.setEnabled(false);
			falseImage.setEnabled(false);
			
			answer = falseButton.getText().toString();
			words.get(count - 1).choose_answer = answer;
			
		}			
	}
	
	private Dialog createDialogResult() {
		ContextThemeWrapper wrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo);
		LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(ContextThemeWrapper.LAYOUT_INFLATER_SERVICE);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(wrapper);
		View view = (View) inflater.inflate(R.layout.common_dialog_game_result, null);
		alertDialogBuilder.setView(view);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setInverseBackgroundForced(true);		
		final Dialog alertDialog = alertDialogBuilder.create();
		alertDialog.setCanceledOnTouchOutside(false);
		
		Button btnReLearn = (Button)view.findViewById(R.id.common_dialog_game_result_re_learn);
		Button btnHome = (Button)view.findViewById(R.id.common_dialog_game_result_home);
		Button btnShare = (Button)view.findViewById(R.id.common_dialog_game_result_share);
		
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SelectPictureActivity.this, MainActivity.class));				
			}
		});
		
		btnReLearn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SelectPictureActivity.this, SelectPictureActivity.class));				
			}
		});

		btnShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
			}
		});
		
		return alertDialog;
	}

	private void loadNewQuestion() {
		if (count == GlobalData.TEST_LIMIT) {
			// show result screen
			createDialogResult().show();
			return;
		}
		
		flagTrue = false;
		
		disibleList = new ArrayList<Button>();
		
		// reset background for all answer buttons
		resetBackgroundButton();
		
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
	
	private void resetBackgroundButton() {
		for (int i = 1; i <= 4; i++) {
			Button bt = getButtonFromId(i);
			ImageView iv = getImageViewFromId(i);
			bt.setBackgroundResource(R.drawable.btn_answer_background);
			bt.setEnabled(true);
			iv.setEnabled(true);
		}
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
