package com.hako.word.vocabulary;

import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class VocabularyArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] hiragana;
	private final String[] kanji;
	private final String[] mean;
	private final String[] romaji;
	
	
	public VocabularyArrayAdapter(Context context, String[] hiragana, String[] kanji, String[] mean, String[] romaji) {
		super(context, R.layout.vocabulary_summary_tab, hiragana);
		this.context = context;
		this.hiragana = hiragana;
		this.kanji = kanji;
		this.mean = mean;
		this.romaji = romaji;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.vocabulary_summary_tab, parent, false);
		TextView tvHira = (TextView) rowView.findViewById(R.id.tvHiragana);
		TextView tvKanji = (TextView) rowView.findViewById(R.id.tvKanji);
		TextView tvMean = (TextView) rowView.findViewById(R.id.tvMean);
		tvHira.setText(hiragana[position]);
		if (kanji[position] != null)
			tvKanji.setText("( " + kanji[position] + " )");
		tvMean.setText(mean[position]);
		final String audioName = romaji[position] + ".mp3";
//		final Drawable drawable_auto = context.getResources().getDrawable(R.drawable.bt_auto);
//        final Drawable drawable_auto_press = context.getResources().getDrawable(R.drawable.bt_auto_press);
		Button audio = (Button) rowView.findViewById(R.id.voca_bt_audio);
		audio.setOnClickListener(new View.OnClickListener() {
			@Override
            public void onClick(View v) {
            	GlobalData.playAudioFromAsset(context, audioName);
//            	audio.setCompoundDrawablesWithIntrinsicBounds(null, drawable_auto_press, null, null);
            }
        });
		return rowView;
	}
}
