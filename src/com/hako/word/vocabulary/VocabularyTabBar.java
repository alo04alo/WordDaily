package com.hako.word.vocabulary;

import com.hako.word.R;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class VocabularyTabBar extends TabActivity implements OnTabChangeListener{
	
	TabHost tabHost;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_tab);
         
        // Get TabHost Refference
        tabHost = getTabHost();
        
        // Set TabChangeListener called when tab changed
        tabHost.setOnTabChangedListener(this);
     
        TabHost.TabSpec spec;
        Intent intent;
   
         // SummaryTab: Create  Intents to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SummaryTab.class);
        spec = tabHost.newTabSpec("First").setIndicator("Dạng đầy đủ")
                      .setContent(intent);
         
        // Add intent to tab
        tabHost.addTab(spec);
   
        // DetailTab
        intent = new Intent().setClass(this, DetailTab.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Dạng chi tiết")
                      .setContent(intent);  
        tabHost.addTab(spec);
   
        // Set Tab1 as Default tab and change color   
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#FFFFFF"));
        tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#33B5E5"));
        TextView textView0 = (TextView) tabHost.getChildAt(0).findViewById(android.R.id.title);
        textView0.setTextColor(Color.parseColor("#0099CC"));
   
     }

  @Override
  public void onTabChanged(String tabId) {
       
      // Call when tab change: Check current selected tab and change according images
      for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
      {
          tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#33B5E5"));
          TextView textView = (TextView) tabHost.getChildAt(i).findViewById(android.R.id.title);
          textView.setTextColor(Color.parseColor("#FFFFFF"));
      }
	  int currTab = tabHost.getCurrentTab();
	  tabHost.getTabWidget().getChildAt(currTab).setBackgroundColor(Color.parseColor("#FFFFFF"));
	  TextView textView = (TextView) tabHost.getChildAt(currTab).findViewById(android.R.id.title);
      textView.setTextColor(Color.parseColor("#0099CC"));
  }

}
