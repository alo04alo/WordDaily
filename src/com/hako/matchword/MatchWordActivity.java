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
	
	private ArrayAdapter<String> adapter_viet;
	private ArrayAdapter<String> adapter_japan;
	private ListView listView_viet;
	private	ListView listView_japan;
	private TextView tvClock;
	
	private List<Word> listWord;
	int[] selected_japan, selected_viet;
	List<Integer> unselect_japan, unselect_viet;
	
	private int mHeartRemain;
	private int mAnswerCorrect;
	
	private Random random;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_word);

		random = new Random();
		mHeartRemain = MAX_HEART;
		mAnswerCorrect = 0;
		
		unselect_japan = new ArrayList<Integer>();
		unselect_viet = new ArrayList<Integer>();
		for (int i = 0; i < MAX_WORD; i++) {
			unselect_japan.add(i);
			unselect_viet.add(i);
		}
		
		selected_japan = new int[MAX_WORD_SHOW];
		selected_viet = new int[MAX_WORD_SHOW];
		for (int i = 0; i < MAX_WORD_SHOW; i++) {
			selected_japan[i] = -1;
			selected_viet[i] = -1;
		}
		
		tvClock = (TextView) findViewById(R.id.matchword_tv_timer);
		
		adapter_japan = new ArrayAdapter<String>(this, R.layout.matchword_list_item);
		listView_japan = (ListView) findViewById(R.id.matchword_list_jpan);
		listView_japan.setAdapter(adapter_japan);
		listView_japan.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				if (!adapter_japan.getItem(position).isEmpty()) {
					checkAnswerCorrect();
				} else {
					listView_japan.setItemChecked(-1, true);
				}
				updateListViewHightlight(listView_japan);
			}
		});
		
		adapter_viet = new ArrayAdapter<String>(this, R.layout.matchword_list_item);
		listView_viet = (ListView) findViewById(R.id.matchword_list_vie);
		listView_viet.setAdapter(adapter_viet);
		listView_viet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				if (!adapter_viet.getItem(position).isEmpty()) {
					checkAnswerCorrect();
				} else {
					listView_viet.setItemChecked(-1, true);
				}
				updateListViewHightlight(listView_viet);
			}
		});
		
		initListWord();
		initListViewValues();
		
		new CountDownTimer(240000, 1000) {

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
		int jpanIndex = listView_japan.getCheckedItemPosition();
		int vieIndex = listView_viet.getCheckedItemPosition();
		
		if (jpanIndex == ListView.INVALID_POSITION 
				|| vieIndex == ListView.INVALID_POSITION) {
			Debug.out("invalid item selected, return");
			return;
		} else {
			listView_japan.setItemChecked(-1, true);
			listView_viet.setItemChecked(-1, true);
			
			updateListViewHightlight(listView_viet);
			updateListViewHightlight(listView_japan);
		}
		
		if (selected_japan[jpanIndex] == selected_viet[vieIndex]) {
			// check if all words matched, win game
			if (++mAnswerCorrect >= MAX_WORD) {
				gameOver();
			}
			
			selected_japan[jpanIndex] = getSingleRandomItem(unselect_japan);
			selected_viet[vieIndex] = getSingleRandomItem(unselect_viet);
			
			String newJpanItem = "";
			String newVieItem = "";
			if (selected_viet[vieIndex] != -1) {
				if (!hasAtLeastOnePairMatch()) {
					int src = random.nextInt(MAX_WORD_SHOW);
					unselect_viet.add(selected_viet[vieIndex]);
					selected_viet[vieIndex] = selected_japan[src];
				}
				newJpanItem = listWord.get(selected_japan[jpanIndex]).hiragana;
				newVieItem = listWord.get(selected_viet[vieIndex]).mean_vi;
			}
			
			updateAdapter(adapter_japan, newJpanItem, jpanIndex);
			updateAdapter(adapter_viet, newVieItem, vieIndex);
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
		listWord = new ArrayList<Word>();
		for (Word w : allWords) {
			if (w.mean_vi.length() > MAX_CHAR_IN_WORD) {
				removeWords.add(w);
			}
		}
		for (Word w : removeWords) {
			allWords.remove(w);
		}
		for (int i = 0; i < MAX_WORD; i++) {
			int pos = random.nextInt(allWords.size());
			listWord.add(allWords.get(pos));
			allWords.remove(pos);
		}
	}
	
	/**
	 * set start values for two listviews
	 */
	private void initListViewValues() {
		getMultiRandomItems(unselect_japan, selected_japan);
		for (int index : selected_japan) {
			if (index < 0) continue;
			adapter_japan.add(listWord.get(index).hiragana);
		}
		
		getMultiRandomItems(unselect_viet, selected_viet);
		if (!hasAtLeastOnePairMatch()) {
			int src = random.nextInt(MAX_WORD_SHOW);
			int target = random.nextInt(MAX_WORD_SHOW);
			
			unselect_viet.add(selected_viet[target]);
			selected_viet[target] = selected_japan[src];
		}
		for (int index : selected_viet) {
			if (index < 0) continue;
			adapter_viet.add(listWord.get(index).mean_vi);
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
		for (int i = 0; i < selected_japan.length; i++) {
			for (int j = 0; j < selected_viet.length; j++) {
				if (selected_japan[i] == selected_viet[j]) {
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
			int location = random.nextInt(list.size());
			int index =  list.get(location);
			list.remove(location);
			return index;
		}
	}
}
