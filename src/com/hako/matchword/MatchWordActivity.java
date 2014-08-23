package com.hako.matchword;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.Debug;
import com.hako.word.R;
import com.hako.word.R.id;
import com.hako.word.R.layout;
import com.hako.word.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MatchWordActivity extends ActionBarActivity {
	
	private static final int MAX_WORD = 15;
	private static final int MAX_WORD_SHOW = 6; 
	private static final int MAX_HEART = 3;
	private static final int MAX_CHAR_IN_WORD = 50;
	private static final long TIMER_LONG = 240000;
	private static final long TIMER_DELAY = 1000;
	
	private ListView listView_vie;
	private	ListView listView_jp;
	private TextView tvClock;
	
	private ArrayAdapter<String> mAdapter_vie;
	private ArrayAdapter<String> mAdapter_jp;
	
	private List<Word> mWordList;
	int[] mSelected_jp, mSelected_vie;
	List<Integer> mUnselected_jp, mUnselected_vie;
	
	private int mHeartRemain;
	private int mAnswerCorrect;
	
	private Random mRandom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_word);
		
		mRandom = new Random();
		mHeartRemain = MAX_HEART;
		mAnswerCorrect = 0;
		
		mUnselected_jp = new ArrayList<Integer>();
		mUnselected_vie = new ArrayList<Integer>();
		for (int i = 0; i < MAX_WORD; i++) {
			mUnselected_jp.add(i);
			mUnselected_vie.add(i);
		}
		
		mSelected_jp = new int[MAX_WORD_SHOW];
		mSelected_vie = new int[MAX_WORD_SHOW];
		for (int i = 0; i < MAX_WORD_SHOW; i++) {
			mSelected_jp[i] = -1;
			mSelected_vie[i] = -1;
		}
		
		tvClock = (TextView) findViewById(R.id.matchword_tv_timer);
		
		mAdapter_jp = new ArrayAdapter<String>(this, R.layout.matchword_list_item);
		listView_jp = (ListView) findViewById(R.id.matchword_list_jpan);
		listView_jp.setAdapter(mAdapter_jp);
		listView_jp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				if (!mAdapter_jp.getItem(position).isEmpty()) {
					checkAnswerCorrect();
				} else {
					listView_jp.setItemChecked(-1, true);
				}
				updateListViewHightlight(listView_jp);
			}
		});
		
		mAdapter_vie = new ArrayAdapter<String>(this, R.layout.matchword_list_item);
		listView_vie = (ListView) findViewById(R.id.matchword_list_vie);
		listView_vie.setAdapter(mAdapter_vie);
		listView_vie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				if (!mAdapter_vie.getItem(position).isEmpty()) {
					checkAnswerCorrect();
				} else {
					listView_vie.setItemChecked(-1, true);
				}
				updateListViewHightlight(listView_vie);
			}
		});
		
		initListWord();
		initListViewValues();
		
		new CountDownTimer(TIMER_LONG, TIMER_DELAY) {

			public void onTick(long millisUntilFinished) {
				int timeRemain = (int)(millisUntilFinished / 1000);
				int minute = timeRemain / 60;
				int second = timeRemain - (minute * 60);
				String text = minute + ":";
				if (second < 10) 
					text += "0";
				text += second;
				tvClock.setText(text);
		    }

		    public void onFinish() {
		    	tvClock.setText("0:00");
		    	 gameOver();
		    }
		}.start();
	}

	/**
	 * 
	 */
	private void gameOver() {
		Toast.makeText(this, "Game Over", Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 
	 */
	private void destroyAHeart() {
		mHeartRemain --;
		if (mHeartRemain < 0) {
			return;
		}
		switch (mHeartRemain) {
		case 0: findViewById(R.id.matchword_ivheart1).setVisibility(View.INVISIBLE);
		case 1: findViewById(R.id.matchword_ivheart2).setVisibility(View.INVISIBLE);
		case 2: findViewById(R.id.matchword_ivheart3).setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * 			
	 */
	private void checkAnswerCorrect() {
		int jpanIndex = listView_jp.getCheckedItemPosition();
		int vieIndex = listView_vie.getCheckedItemPosition();
		
		if (jpanIndex == ListView.INVALID_POSITION 
				|| vieIndex == ListView.INVALID_POSITION) {
			Debug.out("invalid item selected, return");
			return;
		} else {
			listView_jp.setItemChecked(-1, true);
			listView_vie.setItemChecked(-1, true);
			
			updateListViewHightlight(listView_vie);
			updateListViewHightlight(listView_jp);
		}
		
		if (mSelected_jp[jpanIndex] == mSelected_vie[vieIndex]) {
			// check if all words matched, win game
			if (++mAnswerCorrect >= MAX_WORD) {
				gameOver();
			}
			
			mSelected_jp[jpanIndex] = getSingleRandomItem(mUnselected_jp);
			mSelected_vie[vieIndex] = getSingleRandomItem(mUnselected_vie);
			
			String newJpanItem = "";
			String newVieItem = "";
			if (mSelected_vie[vieIndex] != -1) {
				if (!hasAtLeastOnePairMatch()) {
					int src = mRandom.nextInt(MAX_WORD_SHOW);
					mUnselected_vie.add(mSelected_vie[vieIndex]);
					mSelected_vie[vieIndex] = mSelected_jp[src];
				}
				newJpanItem = mWordList.get(mSelected_jp[jpanIndex]).hiragana;
				newVieItem = mWordList.get(mSelected_vie[vieIndex]).mean_vi;
			}
			
			updateAdapter(mAdapter_jp, newJpanItem, jpanIndex);
			updateAdapter(mAdapter_vie, newVieItem, vieIndex);
		}
		else {
			destroyAHeart();
			if (mHeartRemain < 0) {
				gameOver();
			}
		}
	}
	
	/**
	 * 
	 */
	private void initListWord() {
		List<Word> allWords = WordHandle.getListWord(1);
		List<Word> removeWords = new ArrayList<Word>();
		mWordList = new ArrayList<Word>();
		for (Word w : allWords) {
			if (w.mean_vi.length() > MAX_CHAR_IN_WORD) {
				removeWords.add(w);
			}
		}
		for (Word w : removeWords) {
			allWords.remove(w);
		}
		for (int i = 0; i < MAX_WORD; i++) {
			int pos = mRandom.nextInt(allWords.size());
			mWordList.add(allWords.get(pos));
			allWords.remove(pos);
		}
	}
	
	/**
	 * set start values for two listviews
	 */
	private void initListViewValues() {
		getMultiRandomItems(mUnselected_jp, mSelected_jp);
		for (int index : mSelected_jp) {
			if (index < 0) continue;
			mAdapter_jp.add(mWordList.get(index).hiragana);
		}
		
		getMultiRandomItems(mUnselected_vie, mSelected_vie);
		if (!hasAtLeastOnePairMatch()) {
			int src = mRandom.nextInt(MAX_WORD_SHOW);
			int target = mRandom.nextInt(MAX_WORD_SHOW);
			
			mUnselected_vie.add(mSelected_vie[target]);
			mSelected_vie[target] = mSelected_jp[src];
		}
		for (int index : mSelected_vie) {
			if (index < 0) continue;
			mAdapter_vie.add(mWordList.get(index).mean_vi);
		}
	}

	/**
	 * 
	 * @param adapter
	 * @param val
	 * @param pos
	 */
	private void updateAdapter(ArrayAdapter<String> adapter, String val, int pos) {
		String item = adapter.getItem(pos);
		adapter.remove(item);
		adapter.insert(val, pos);
	}
	
	private void updateListViewHightlight(ListView listView) {
		int pos = listView.getCheckedItemPosition();
		for (int i = 0; i < listView.getCount(); i++) {
			if (i == pos) {
				listView.getChildAt(i).setBackgroundResource(
						R.drawable.matchword_item_selector);
				continue;
			}
			listView.getChildAt(i).setBackgroundResource(
					R.drawable.matchword_textview_bg);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	private boolean hasAtLeastOnePairMatch() {
		for (int i = 0; i < mSelected_jp.length; i++) {
			for (int j = 0; j < mSelected_vie.length; j++) {
				if (mSelected_jp[i] == mSelected_vie[j]) {
					Debug.out(i + "=" + j);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 */
	private void getMultiRandomItems(List<Integer> list, int[] target) {
		for (int i = 0; i < target.length; i++) {
			target[i] = getSingleRandomItem(list);
		}
	}
	
	/**
	 * @param list: list to choose an index, items in list can be choosed
	 * when and only when state if state of it is valid
	 * @return return a random index in list and -1 if no index valid
	 */
	private int getSingleRandomItem(List<Integer> list) {
		if (list.size() <= 0) {
			return -1;
		} else if (list.size() == 1) {
			int index = list.get(0);
			list.remove(0);
			return index;
		} else {
			int location = mRandom.nextInt(list.size());
			int index =  list.get(location);
			list.remove(location);
			return index;
		}
	}
}
