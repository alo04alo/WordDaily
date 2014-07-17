package com.hako.word.vocabulary;

import com.hako.word.R;

import android.content.Context;
import android.widget.ArrayAdapter;

public class VocabularyArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	
	public VocabularyArrayAdapter(Context context, String[] values) {
		super(context, R.layout.vocabulary_summary_tab, values);
		this.context = context;
		this.values = values;
		// TODO Auto-generated constructor stub
	}

}
