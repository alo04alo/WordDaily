package com.hako.listenwrite;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import com.hako.listenwrite.ListenWriteFragment.FragmentCallbacks;
import com.hako.utils.Debug;
import com.hako.word.R;

public class ListenWriteActivity extends ActionBarActivity
		implements ExamListenWriteFragment.FragmentCallbacks { 
	
	private View pbWrapperMain;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_listen_write);
		
		pbWrapperMain = findViewById(R.id.listenwrite_pbWrapperMain);
	}
	
	@Override
	public void onFragmentStartLoading() {
		// TODO Auto-generated method stub
		pbWrapperMain.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onFragmentLoadFinish() {
		// TODO Auto-generated method stub
		pbWrapperMain.setVisibility(View.GONE);
	}

	@Override
	public void onGameOver(Bundle bundle) {
		// TODO Auto-generated method stub
		Debug.log(bundle);
	}

	@Override
	public void onTimerTick(long millisUntilFinished) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getMaxWordExpected() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int getMaxRecommend() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public long getMaxTime() {
		// TODO Auto-generated method stub
		return 180000;
	}

	@Override
	public int getMaxLifeOnWord() {
		// TODO Auto-generated method stub
		return 2;
	}

}
