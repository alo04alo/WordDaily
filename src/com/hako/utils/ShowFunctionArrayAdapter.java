package com.hako.utils;

import com.hako.word.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ShowFunctionArrayAdapter extends ArrayAdapter<String>{

	private Context context;
	private String[] functionName;
	
	public ShowFunctionArrayAdapter(Context context, String[] functionName) {
		super(context, R.layout.common_dialog_function_item, functionName);
		this.context = context;
		this.functionName = functionName;				
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView =  inflater.inflate(R.layout.common_dialog_function_item, parent, false);
		
		TextView tvFunctionName =  (TextView) rowView.findViewById(R.id.common_tv_function);
		tvFunctionName.setText(functionName[position]);
		
		return rowView;
		
	}
	
	

}
