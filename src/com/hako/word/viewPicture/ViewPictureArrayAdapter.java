package com.hako.word.viewPicture;

import java.util.List;

import com.hako.word.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ViewPictureArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] gameName;
	
	
	public ViewPictureArrayAdapter(Context context, String[] gameName) {
		super(context, R.layout.common_dialog_function_item, gameName);	
		this.context = context;
		this.gameName = gameName;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.common_dialog_function_item, parent, false);
		
		TextView tvGameName = (TextView) rowView.findViewById(R.id.common_tv_function);
		tvGameName.setText(gameName[position]);
		return rowView;
	}

	

}
