package com.hako.word.exam;

import com.hako.word.R;
import com.hako.word.vocabulary.SummaryTab;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class ExamTabActivity extends TabActivity{
	TabHost tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_view);
		
		tabHost = getTabHost();
		
		TabHost.TabSpec spec;
		Intent intent;
		
		intent = new Intent().setClass(this, Exam1Activity.class);
        spec = tabHost.newTabSpec("First").setIndicator("Kiểm tra 1")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, Exam2Activity.class);
        spec = tabHost.newTabSpec("First").setIndicator("Kiểm tra 2")
                      .setContent(intent);
        tabHost.addTab(spec);
        
        intent = new Intent().setClass(this, Exam3Activity.class);
        spec = tabHost.newTabSpec("First").setIndicator("Kiểm tra 3")
                      .setContent(intent);
        tabHost.addTab(spec);
	}
}
