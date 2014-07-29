package com.hako.word.exam;

import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class ExamTabActivity extends TabActivity implements OnTabChangeListener{
	TabHost tabHost;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam_view);
		
		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		
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
        
        int tabCount = tabHost.getTabWidget().getTabCount();
        for (int i = 0; i < tabCount; i++) {
            View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
                // reduce height of the tab
                view.getLayoutParams().height *= 1.16;
                view.setBackgroundColor(Color.parseColor(GlobalData.COLOR_BACKGROUND_TAB_BAR));
                //  get title text view
                View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {
                    // just in case check the type

                    // center text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    // wrap text
                    ((TextView) textView).setSingleLine(false);
                    ((TextView) textView).setTextColor(Color.parseColor("#ffffff"));
                    ((TextView) textView).setTextSize(20);

                    // explicitly set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        }
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
        View textView = tabHost.getTabWidget().getChildTabViewAt(0).findViewById(android.R.id.title);
        if ( textView instanceof TextView ) {
            ((TextView) textView).setTextColor(Color.parseColor(GlobalData.COLOR_TEXT_TAB_BAR));
        }
	}
	
	@Override
    public void onTabChanged(String tabId) {
	  for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
	    {
	        tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor(GlobalData.COLOR_BACKGROUND_TAB_BAR));
	        View textView = tabHost.getTabWidget().getChildTabViewAt(i).findViewById(android.R.id.title);
	        if ( textView instanceof TextView ) {
                ((TextView) textView).setTextColor(Color.parseColor("#ffffff"));
            }
	    } 

	  tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffff"));
	  View textView = tabHost.getTabWidget().getChildTabViewAt(tabHost.getCurrentTab()).findViewById(android.R.id.title);
      if ( textView instanceof TextView ) {
          ((TextView) textView).setTextColor(Color.parseColor(GlobalData.COLOR_TEXT_TAB_BAR));
      }
	}
}