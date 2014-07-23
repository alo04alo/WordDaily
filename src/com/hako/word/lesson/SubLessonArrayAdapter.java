package com.hako.word.lesson;

import com.hako.word.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SubLessonArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] titleLesson;
	private final String[] descriptLesson;
	
	public SubLessonArrayAdapter(Context context, String[] titleLesson, String[] descriptLesson) {
		super(context, R.layout.sub_lesson_dialog_item, titleLesson);		
		this.context = context;
		this.titleLesson = titleLesson;
		this.descriptLesson = descriptLesson;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.sub_lesson_dialog_item, parent, false);
		
		TextView tvTitle = (TextView) rowView.findViewById(R.id.tvTitleLesson);
		TextView tvDescript = (TextView) rowView.findViewById(R.id.tvDescriptLesson);
		
		tvTitle.setText(titleLesson[position] + ": ");
		tvDescript.setText(descriptLesson[position]);
		
		return rowView;
		
	}
	
}
