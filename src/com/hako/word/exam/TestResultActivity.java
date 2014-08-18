package com.hako.word.exam;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hako.base.Word;
import com.hako.utils.GlobalData;
import com.hako.word.R;
import com.hako.word.lesson.SubLesson;

public class TestResultActivity extends Activity{
	String[] questions;
	String[] rightAnswers;
	String[] selectedAnsers;
	List<Word> data;
	int score = 0;
	
	ListView lvResult;		
	TextView tvScore;
	RatingBar rbStar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_result_view);
		
		tvScore = (TextView) findViewById(R.id.test_result_tv_score);
		lvResult = (ListView) findViewById(R.id.test_result_lv);
		rbStar = (RatingBar) findViewById(R.id.test_result_rb_star);
		
		data = GlobalData.testData;
		questions = new String[data.size()];
		rightAnswers = new String[data.size()];
		selectedAnsers = new String[data.size()];
		
		for (int index = 0; index < data.size(); index++) {
			if (GlobalData.currentExam == 1) {
				questions[index] = data.get(index).mean_vi;
				rightAnswers[index] = data.get(index).hiragana;
				if (data.get(index).hiragana == data.get(index).choose_answer) {
					score += 1;
				}
			} else if (GlobalData.currentExam == 3) {
				questions[index] = data.get(index).hiragana;
				rightAnswers[index] = data.get(index).mean_vi;
				if (data.get(index).mean_vi == data.get(index).choose_answer) {
					score += 1;
				}
			}			
			
			selectedAnsers[index] = data.get(index).choose_answer;
						
			
		}
		
		// set value for ratingbar	
		if ((score % 5) == 0) {
			rbStar.setRating(score /5);
		} else {
			rbStar.setRating((int)(score / 5) + 1);
		}		
		
		// display score
		tvScore.setText(score + " / " + data.size());
		
		lvResult.setAdapter(new TestResultAdapter(this, questions, rightAnswers, selectedAnsers));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(TestResultActivity.this, SubLesson.class));
	}

}
