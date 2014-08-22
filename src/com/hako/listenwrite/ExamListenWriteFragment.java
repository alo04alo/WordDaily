package com.hako.listenwrite;

import com.hako.utils.Debug;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class ExamListenWriteFragment extends ListenWriteFragment { 
	
	public static final String TIME_USED_TAG = "com.hako.listenwrite.timeused";
	public static final String RECOMMEND_USED_TAG = "com.hako.listenwrite.recommendused";
	public static final String MAX_WORD_TAG = "com.hako.listenwrite.maxword";
	public static final String WORD_PASSED_TAG = "com.hako.listenwrite.wordpassed";
	
	private static final String LIFE_REMAIN_TAG = "com.hako.listenwrite.liferemain";
	
	private long mTimeUsed;
	private int mRecommendUsed;
	private int mLifeRemainOnWord;
	private int mWordPassed;
	private boolean mGamePausing;
	
	private FragmentCallbacks mCallback;

	private AlertDialog mAlertDialog;
	private GameTimer mGameTimer;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		try {
			mCallback = (FragmentCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() 
            		+ " must implement ExamListenWriteFragment.FragmentCallback");
        }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState != null) {
			mTimeUsed = savedInstanceState.getLong(TIME_USED_TAG, 0);
			mRecommendUsed = savedInstanceState.getInt(RECOMMEND_USED_TAG, 0);
			mWordPassed = savedInstanceState.getInt(WORD_PASSED_TAG, 0);
			mLifeRemainOnWord = savedInstanceState.getInt(LIFE_REMAIN_TAG, 0);
			Debug.log("save instance not null, time used = " + mTimeUsed);
		} else {
			resetGameParameters();
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		pauseGame();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (mGamePausing) {
			resumeGame();
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putInt(RECOMMEND_USED_TAG, mRecommendUsed);
		outState.putLong(TIME_USED_TAG, mTimeUsed);
		outState.putInt(LIFE_REMAIN_TAG, mLifeRemainOnWord);
		outState.putInt(WORD_PASSED_TAG, mWordPassed);
	}
	
	@Override
	public void restartGame() {
		pauseGame();
		resetGameParameters();
		
		Debug.log("restart game");
		super.restartGame();
	}
	
	public void pauseGame() {
		Debug.log("pause game");
		mGameTimer.cancel();
		mGamePausing = true;
	}
	
	public void resumeGame() {
		Debug.log("resume game");
		mGamePausing = false;
		mGameTimer = new GameTimer(mCallback.getMaxTime() - mTimeUsed, 1000);
		mGameTimer.start();
	}

	@Override
	protected void onFragmentLoadFinish() {
		super.onFragmentLoadFinish();
		resumeGame();
	}
	
	@Override
	protected void onAnswerItemClick(AdapterView<?> adapter, View view,
			int position) {
		// TODO Auto-generated method stub
		if (!checkAnswerCorrect(position)) {
			mLifeRemainOnWord--;
			checkNextWordIfNeed();
			Debug.log("answer wrong");
		} else {
			super.onAnswerItemClick(adapter, view, position);
			Debug.log("answer correct");
		}
	}

	@Override
	protected void onButtonHelperClick() {
		// TODO Auto-generated method stub

		if (mRecommendUsed >= mCallback.getMaxRecommend()) {
			Toast.makeText(getActivity(), "Bạn đã sử dụng hết số lượt trợ giúp", 
					Toast.LENGTH_LONG).show();
			return;
		}
		if (mAlertDialog != null && mAlertDialog.isShowing()) {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	mAlertDialog = builder.setTitle("Cảnh báo")
    		   .setMessage("Sử dụng trợ giúp sẽ khiến bạn được ít sao hơn khi qua bài ?")
    		   .setIcon(android.R.drawable.ic_dialog_alert)
    		   .setPositiveButton("OK", new DialogInterface.OnClickListener() {
    			   
    			   @Override
    			   public void onClick(DialogInterface dialog, int which) {
    				   // TODO Auto-generated method stub
    				   ExamListenWriteFragment.super.onButtonHelperClick();
    				   mRecommendUsed ++;
    			   }
    		   })
    		   .setNegativeButton("Cancel", null)
    		   .show();
	}
	
	@Override
	protected void gameOver() {
		mGameTimer.cancel();
		Debug.log("game timer canceled");
		
		Bundle bundle = new Bundle();
		
		bundle.putInt(MAX_WORD_TAG, getMaxWord());
		bundle.putInt(WORD_PASSED_TAG, mWordPassed);
		bundle.putInt(RECOMMEND_USED_TAG, mRecommendUsed);
		bundle.putLong(TIME_USED_TAG, mTimeUsed);
		
		mCallback.onGameOver(bundle);
	}
	
	@Override
	protected void nextWord() {
		// TODO Auto-generated method stub
		if (mLifeRemainOnWord > 0) {
			Debug.log("new word passed " + mLifeRemainOnWord);
			mWordPassed++;
		}
		mLifeRemainOnWord = mCallback.getMaxLifeOnWord();
		super.nextWord();
	}
	
	@Override
	protected void checkNextWordIfNeed() {
		// TODO Auto-generated method stub
		if (mLifeRemainOnWord <= 0) {
			nextWord();
			return;
		}
		super.checkNextWordIfNeed();
	}

	/**
	 * 
	 */
	private void resetGameParameters() {
		mWordPassed = 0;
		mTimeUsed = 0;
		mRecommendUsed = 0;
		mLifeRemainOnWord = 0;
	}
	
	/**
	 * @author anhtn
	 */
	private class GameTimer extends CountDownTimer {
		
		public GameTimer(long totalTime, long tickTime) {
			super(totalTime, tickTime);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			mTimeUsed = mCallback.getMaxTime();
			gameOver();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub
			mTimeUsed = mCallback.getMaxTime() - millisUntilFinished;
			mCallback.onTimerTick(millisUntilFinished);
		}
		
	}
	
	/**
	 * activity use fragment must implements all below methods
	 * @author anhtn
	 */
	public static interface FragmentCallbacks 
			extends ListenWriteFragment.FragmentCallbacks {
		
		/**
		 * called by fragment when first created
		 * @return the maximum number of helps
		 */
		public int getMaxRecommend();
		
		/**
		 * 
		 * @return
		 */
		public int getMaxLifeOnWord();
		
		/**
		 * called by fragment when first created
		 * @return the maximum length of time on a game
		 */
		public long getMaxTime();
		
		/**
		 * called after every second since timer started
		 * @param millisUntilFinished milis seconds untils exam timeout
		 */
		public void onTimerTick(long millisUntilFinished);
	}
	
}
