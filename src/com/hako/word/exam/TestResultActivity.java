package com.hako.word.exam;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.hako.base.Word;
import com.hako.utils.GlobalData;
import com.hako.word.R;

public class TestResultActivity extends Activity{
	String[] questions;
	String[] rightAnswers;
	String[] selectedAnsers;
	List<Word> data;
	int score = 0;
	
	ListView lvResult;		
	TextView tvScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_result_view);
		
		tvScore = (TextView) findViewById(R.id.test_result_tv_score);
		lvResult = (ListView) findViewById(R.id.test_result_lv);
		
		data = GlobalData.testData;
		questions = new String[data.size()];
		rightAnswers = new String[data.size()];
		selectedAnsers = new String[data.size()];
		
		for (int index = 0; index < data.size(); index++) {
			questions[index] = data.get(index).mean_vi;
			rightAnswers[index] = data.get(index).hiragana;
			selectedAnsers[index] = data.get(index).choose_answer;
			
			if (data.get(index).hiragana == data.get(index).choose_answer) {
				score += 1;
			}
			
		}
		
		// display score
		tvScore.setText(score + " / " + data.size());
		
		lvResult.setAdapter(new TestResultAdapter(this, questions, rightAnswers, selectedAnsers));
	}

}
