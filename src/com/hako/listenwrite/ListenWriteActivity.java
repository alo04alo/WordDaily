package com.hako.listenwrite;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.Debug;
import com.hako.utils.GlobalData;
import com.hako.word.R;

public class ListenWriteActivity extends ActionBarActivity { 
	
	private static final int MAX_WORD = 20;
	private static final int MAX_ANSWER = 10; 
	private static final int DELAY_BEFORE_NEXT_WORD = 1000;
	
	private GridView gridview;
	private ViewFlipper viewFlipper;
	private ArrayAdapter<String> adapter;
	private TextView tvResult;
	private Button btnHelper, btnAudio;
	private View pbWrapperFlipper; 
	private View pbWrapperMain;
	
	private List<Word> mWordList;
	private int mWordIndex;
	
	private Integer[] mAnswers;
	private int mAnswerCorrect;
	
	private class LoadWordTask extends AsyncTask<Integer, Void, List<Word>> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected List<Word> doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			return WordHandle.getRandomListWord(arg0[0], WordHandle.KIND_HAS_IMAGE, MAX_WORD);
		}
		
		@Override
		protected void onPostExecute(List<Word> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			mWordList = result;
			nextWord();
			pbWrapperMain.setVisibility(View.GONE);
		}
		
	}
	
	private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pbWrapperFlipper.setVisibility(View.VISIBLE);
		}
		
		@Override
	    protected Bitmap doInBackground(String... urls) {
			Debug.log("load image url: " + urls[0]);
			return GlobalData.getImageFromRaw(getApplicationContext(), urls[0]);
	    }

		@Override
	    protected void onPostExecute(Bitmap data) {
	    	super.onPostExecute(data);
	    	
    		LayoutInflater inflater = (LayoutInflater) 
    				ListenWriteActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		
    		ImageView imgView = (ImageView) inflater.inflate(
    				R.layout.listenwrite_viewflipper_item, null);
			imgView.setImageBitmap(data);
			viewFlipper.addView(imgView);
			
			pbWrapperFlipper.setVisibility(View.INVISIBLE);
			
			final int count = viewFlipper.getChildCount();
			if (count > 1) {
				viewFlipper.showNext();
				ImageView iv = (ImageView) viewFlipper.getChildAt(count - 2);
				iv.setImageBitmap(null);
			}
	    }
	    
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen_write);
		
		tvResult = (TextView) findViewById(R.id.listenwrite_txt_result);
		viewFlipper = (ViewFlipper) findViewById(R.id.listenwrite_ViewFlipper);
		pbWrapperFlipper = findViewById(R.id.listenwrite_pbWrapperFlipper);
		pbWrapperMain = findViewById(R.id.listenwrite_pbWrapperMain);
		
		mWordIndex = -1;
		mAnswerCorrect = 0;
		mAnswers = new Integer[MAX_ANSWER];
		
		adapter = new ArrayAdapter<String>(this, R.layout.listenwrite_grid_item);
		gridview = (GridView) findViewById(R.id.listenwrite_GridView);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if (mAnswers[position] < 0) return;

				char selectChar = ((TextView) view).getText().charAt(0);
				updateAnswerTextView(selectChar, mAnswers[position]);

				mAnswers[position] = -1;
				mAnswerCorrect ++;
				if (mAnswerCorrect >= tvResult.getText().length()) {
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							nextWord();
						}
					}, DELAY_BEFORE_NEXT_WORD);
				}
			}
		});
		
		btnHelper = (Button) findViewById(R.id.listenwrite_btn_help);
		btnHelper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				int min = mWordList.get(mWordIndex).hiragana.length();
				String text = null;
				int minIndex = -1;
				
				for (int i = 0; i < mAnswers.length; i++) {
					if (mAnswers[i] < 0) continue;
					if (mAnswers[i] < min) {
						min = mAnswers[i];
						text = adapter.getItem(i);
						minIndex = i;
					}
				}
				
				if (minIndex < 0) return;
				
				mAnswerCorrect ++;
				mAnswers[minIndex] = -1;
				updateAnswerTextView(text.charAt(0), min);
				
				if (mAnswerCorrect >= tvResult.getText().length()) {
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							nextWord();
						}
					}, DELAY_BEFORE_NEXT_WORD);
				}
			}
		});
		
		btnAudio = (Button) findViewById(R.id.listenwrite_btn_audio);
		btnAudio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				
			}
		});
		
		LoadWordTask task = new LoadWordTask();
		task.execute(1);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/**
	 * 
	 */
	private void nextWord() {
		final Random random = new Random();
		GlobalData.fillArray(mAnswers, -1);
		tvResult.setText(null);
		mAnswerCorrect = 0;
		adapter.clear();
		
		ArrayList<Integer> producers = new ArrayList<Integer>();
		for (int i = 0; i < MAX_ANSWER; i++) {
			producers.add(i);
		}
		
		final Word currentWord = mWordList.get(++mWordIndex);
		final char[] hiragana = currentWord.hiragana.toCharArray();
		Debug.log(currentWord.hiragana);
		
		for (int i = 0; i < hiragana.length; i++) {
			final int index = random.nextInt(producers.size());			
			mAnswers[producers.get(index)] = i;
			producers.remove(index);
			tvResult.append("_");
		}

		List<Character> charList = new ArrayList<Character>();
		for (char character : GlobalData.charArray) {
			if (currentWord.hiragana.contains(Character.toString(character))) {
				// ignore character that exist in result string
				continue;
			}
			charList.add(character);
		}
		
		for (int i = 0; i < mAnswers.length; i++) {
			if (mAnswers[i] < 0) {
				int index = random.nextInt(charList.size());
				adapter.add(Character.toString(charList.get(index)));
				charList.remove(index);
			} else {
				adapter.add(Character.toString(hiragana[mAnswers[i]]));
			}
		}
		
		LoadImageTask task = new LoadImageTask();
		task.execute(currentWord.romaji);		
	}

	/**
	 * 
	 */
	private void updateAnswerTextView(char newChar, int pos) {
		char[] data = tvResult.getText().toString().toCharArray();
		if (pos >= 0 && pos < data.length) {
			data[pos] = newChar;
		}
		tvResult.setText(String.valueOf(data));
	}
	
}
