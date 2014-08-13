package com.hako.word.exam;

import java.util.List;

import com.hako.word.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TestResultAdapter extends ArrayAdapter<String> {
	
	private final Context context;
	private final String[] questions;
	private final String[] rightAnswers;
	private final String[] selectedAnswer;
	
	public TestResultAdapter(Context context, String[] questions, String[] rightAnswers, String[] selectedAnsers) {
		super(context, R.layout.test_result_lv_item, questions);
		this.context = context;
		this.questions = questions;
		this.rightAnswers = rightAnswers;
		this.selectedAnswer = selectedAnsers;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.test_result_lv_item, parent, false);
		
		TextView tvQuestion = (TextView) view.findViewById(R.id.test_result_tv_question);
		TextView tvUserAnswer = (TextView) view.findViewById(R.id.test_result_tv_user_choice);
		TextView tvRightAnswer = (TextView) view.findViewById(R.id.test_result_tv_right_answer);
		TextView tvNumberOf = (TextView) view.findViewById(R.id.test_result_tvNumof);
		ImageView imgResult = (ImageView) view.findViewById(R.id.test_result_img_result);
		
		tvNumberOf.setText("Q." + (position + 1));
		tvQuestion.setText(questions[position]);
		tvUserAnswer.setText(selectedAnswer[position]);
		tvRightAnswer.setText(rightAnswers[position]);		
		
		if (selectedAnswer[position] == rightAnswers[position]) {
			imgResult.setImageResource(R.drawable.ico_true);
		} else {
			imgResult.setImageResource(R.drawable.ico_false);
		}
		
		return view;
	}
}
