package com.hako.listenwrite;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

public class ListenWriteFragment extends Fragment {
	
	protected static final int MAX_ANSWER_BUTTON = 10; 
	protected static final int DELAY_BEFORE_NEXT_WORD = 1000;
	
	private GridView gridview;
	private ViewFlipper viewFlipper;
	private ArrayAdapter<String> adapter;
	private TextView tvResult;
	private Button btnHelper, btnAudio;
	private View pbWrapperFlipper; 
	
	private List<Word> mWordList;
	private int mWordIndex;
	private Integer[] mAnswers;
	private int mCharacterCorrect;
	
	private boolean mWaitForLoadNextWordFlag;
	
	private FragmentCallbacks mCallback;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try {
			mCallback = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() 
            		+ " must implement ListenWriteFragment.FragmentCallback");
        }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mWordIndex = -1;
		mCharacterCorrect = 0;
		mAnswers = new Integer[MAX_ANSWER_BUTTON];
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub	
		
		View view = inflater.inflate(R.layout.fragment_listen_write, container, false);
		
		tvResult = (TextView) view.findViewById(R.id.listenwrite_txt_result);
		viewFlipper = (ViewFlipper) view.findViewById(R.id.listenwrite_ViewFlipper);
		gridview = (GridView) view.findViewById(R.id.listenwrite_GridView);
		
		pbWrapperFlipper = view.findViewById(R.id.listenwrite_pbWrapperFlipper);
		
		btnHelper = (Button) view.findViewById(R.id.listenwrite_btn_help);
		btnAudio = (Button) view.findViewById(R.id.listenwrite_btn_audio);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		adapter = new ArrayAdapter<String>(
				getActivity(), R.layout.listenwrite_grid_item);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if (!mWaitForLoadNextWordFlag) {
					onAnswerItemClick(adapter, view, position);
				}
			}
		});
		
		btnHelper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (!mWaitForLoadNextWordFlag) {
					onButtonHelperClick();
				}
			}
		});
		
		btnAudio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				if (!mWaitForLoadNextWordFlag) {
					onButtonAudioClick();
				}
			}
		});
		
		startGame();
	}
	
	/**
	 * 
	 */
	public void startGame() {
		LoadWordTask task = new LoadWordTask();
		task.execute(1);
	}
	
	/**
	 * 
	 */
	public void restartGame() {
		mWordIndex = -1;
		mCharacterCorrect = 0;
		startGame();
	}
	
	/**
	 * 
	 * @return
	 */
	public int getMaxWord() {
		return mWordList.size();
	}
	
	/**
	 * 
	 */
	protected void onFragmentLoadFinish() {
		nextWord();
		mCallback.onFragmentLoadFinish();
	}
	
	/**
	 * 
	 */
	protected void onButtonHelperClick() {
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
		
		mCharacterCorrect ++;
		mAnswers[minIndex] = -1;
		updateAnswerTextView(text.charAt(0), min);
		
		checkNextWordIfNeed();
	}
	
	/**
	 * 
	 */
	protected void onButtonAudioClick() {
		String audioUrl = mWordList.get(mWordIndex).romaji;
		GlobalData.playAudioFromAsset(getActivity(), audioUrl);
	}
	
	/**
	 * 
	 * @param adapter
	 * @param view
	 * @param position
	 */
	protected void onAnswerItemClick(AdapterView<?> adapter, View view, 
			int position) {
		
		if (!checkAnswerCorrect(position)) return;

		char selectChar = ((TextView) view).getText().charAt(0);
		updateAnswerTextView(selectChar, mAnswers[position]);

		mAnswers[position] = -1;
		mCharacterCorrect ++;
		
		checkNextWordIfNeed();
	}
	
	/**
	 * 
	 */
	protected void gameOver() {
		mCallback.onGameOver(null);
	}

	/**
	 * 
	 */
	protected void nextWord() {
		if (mWordIndex + 1 >= getMaxWord()) {
			Debug.log("all word passed, game over");
			gameOver();
			return;
		}
		
		mWaitForLoadNextWordFlag = true;
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				final Random random = new Random();
				GlobalData.fillArray(mAnswers, -1);
				tvResult.setText(null);
				mCharacterCorrect = 0;
				adapter.clear();
				
				ArrayList<Integer> producers = new ArrayList<Integer>();
				for (int i = 0; i < MAX_ANSWER_BUTTON; i++) {
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
				
				mWaitForLoadNextWordFlag = false;
			}
		}, DELAY_BEFORE_NEXT_WORD);
	}
	
	/**
	 * 
	 * @return
	 */
	protected final boolean checkAnswerCorrect(int pos) {
		return mAnswers[pos] >= 0;
	}
	
	/**
	 * 
	 *  * @return
	 */
	protected void checkNextWordIfNeed() {
		if (mCharacterCorrect >= tvResult.getText().length()) {
			nextWord();
		}
	}

	/**
	 * 
	 * @param newChar
	 * @param pos
	 */
	private void updateAnswerTextView(char newChar, int pos) {
		char[] data = tvResult.getText().toString().toCharArray();
		if (pos >= 0 && pos < data.length) {
			data[pos] = newChar;
		}
		tvResult.setText(String.valueOf(data));
	}
	
	/**
	 * @author anhtn
	 */
	private class LoadWordTask extends AsyncTask<Integer, Void, List<Word>> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mCallback.onFragmentStartLoading();
		}

		@Override
		protected List<Word> doInBackground(Integer... arg0) {
			// TODO Auto-generated method stub
			return WordHandle.getRandomListWord(
					arg0[0], WordHandle.KIND_HAS_IMAGE, mCallback.getMaxWordExpected());
		}
		
		@Override
		protected void onPostExecute(List<Word> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			mWordList = result;
			onFragmentLoadFinish();
		}
		
	}
	
	/**
	 * @author anhtn
	 */
	private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pbWrapperFlipper.setVisibility(View.VISIBLE);
		}
		
		@Override
	    protected Bitmap doInBackground(String... urls) {
//			Debug.log("load image url: " + urls[0]);
			return GlobalData.getImageFromRaw(getActivity(), urls[0]);
	    }

		@Override
	    protected void onPostExecute(Bitmap data) {
	    	super.onPostExecute(data);
	    	
    		LayoutInflater inflater = (LayoutInflater) 
    				getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		
    		ImageView imgView = (ImageView) inflater.inflate(
    				R.layout.listenwrite_viewflipper_item, viewFlipper, false);
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
	
	/**
	 * @author anhtn
	 */
	public static interface FragmentCallbacks {
		
		/**
		 * called by fragment when first created
		 * @return the maximum number of expected word that fragment try
		 * to get from database. It may not be similar to the real number
		 * is taken from database
		 * (english by google translate, do not laugh at me -_- )
		 */
		public int getMaxWordExpected();
		
		/**
		 * called when fragent start loading new words from database
		 */
		public void onFragmentStartLoading();
		
		/**
		 * called when all tasks in onCreate of fragment finised
		 */
		public void onFragmentLoadFinish();
		
		/**
		 * called when user answer correct all words or time out
		 * @param bundle info of this game
		 */
		public void onGameOver(Bundle bundle);
	}
	
}
