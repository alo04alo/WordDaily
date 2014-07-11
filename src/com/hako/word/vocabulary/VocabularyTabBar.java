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
        spec = tabHost.newTabSpec("First").setIndicator("")
                      .setContent(intent);
        tabHost.addTab(spec);
   
        // DetailTab
        intent = new Intent().setClass(this, DetailTab.class);
        spec = tabHost.newTabSpec("Second").setIndicator("")
                      .setContent(intent);  
        tabHost.addTab(spec);
        
        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.bt_detail_tab);
        
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.bt_summary_tab_select);
     }

  @Override
  public void onTabChanged(String tabId) {
	  tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.bt_summary_tab);
//	  tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.bt_detail_tab);
	  if (tabHost.getCurrentTab() == 0){
		  tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.bt_summary_tab_select);
	  } else {
		  tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.bt_detail_tab_select);
	  }
	  
  }

}
