package com.hako.word.vocabulary;

import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class VocabularyArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] hiragana;
	private final String[] kanji;
	private final String[] mean;
	
	
	public VocabularyArrayAdapter(Context context, String[] hiragana, String[] kanji, String[] mean) {
		super(context, R.layout.vocabulary_summary_tab, hiragana);
		this.context = context;
		this.hiragana = hiragana;
		this.kanji = kanji;
		this.mean = mean;
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.vocabulary_summary_tab, parent, false);
		TextView tvHira = (TextView) rowView.findViewById(R.id.tvHiragana);
		TextView tvKanji = (TextView) rowView.findViewById(R.id.tvKanji);
		TextView tvMean = (TextView) rowView.findViewById(R.id.tvMean);
		tvHira.setText(hiragana[position]);
		tvKanji.setText("( " + kanji[position] + " )");
		tvMean.setText(mean[position]);
		return rowView;
	}
}
