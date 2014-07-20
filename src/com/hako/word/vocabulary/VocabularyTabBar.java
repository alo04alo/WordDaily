package com.hako.word.vocabulary;

import com.hako.utils.GlobalData;
import com.hako.word.MainActivity;
import com.hako.word.R;
import com.hako.word.ShowFunctionArrayAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;

public class VocabularyTabBar extends TabActivity implements OnTabChangeListener{
	
	TabHost tabHost;
	Button btnHome;
	Button btnShowFunction;
	Dialog dialogFunction;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_view);                
         
        // Get TabHost Refference
        tabHost = getTabHost();
        
        // get Button reference
        btnHome = (Button) findViewById(R.id.vocabulary_btn_home);
        btnShowFunction = (Button) findViewById(R.id.vocabulary_btn_down);
        
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
        
        // set event click for button
        btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplicationContext(), MainActivity.class));				
			}
		});
        
        btnShowFunction.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialogFunction = createDialogFunction();
				dialogFunction.show();
			}
		});
     }

  protected Dialog createDialogFunction() {
	  
	  final String[] functions = GlobalData.allFunctions;		
          
      ContextThemeWrapper wrapper = new ContextThemeWrapper(this, android.R.style.Theme_Holo);
      LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(LAYOUT_INFLATER_SERVICE);
      
      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(wrapper);  
      
      View convertView = (View) inflater.inflate(R.layout.common_dialog_function, null);
      alertDialogBuilder.setView(convertView);
      alertDialogBuilder.setCancelable(true);      
      alertDialogBuilder.setInverseBackgroundForced(true);
      
      final Dialog alertDialog = alertDialogBuilder.create();
      
      ListView lv = (ListView) convertView.findViewById(R.id.lvFunction);
      lv.setAdapter(new ShowFunctionArrayAdapter(this, functions));        
      lv.setClickable(true);
      lv.setOnItemClickListener(new OnItemClickListener() {
          public void onItemClick(AdapterView<?> parent, View view, int position, long id) {              
              btnShowFunction.setText(functions[position]);                   
              // hide dialog
              alertDialog.cancel();
          }
      });
      
		return alertDialog;
		
	}

@Override
  public void onTabChanged(String tabId) {
	  for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
		{
	    	if(i==0)
	    		tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.bt_summary_tab);
	    	else if(i==1)
	    		tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.bt_detail_tab);
	    }
	  if (tabHost.getCurrentTab() == 0){
		  tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.bt_summary_tab_select);
	  } else {
		  tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.bt_detail_tab_select);
	  }
	  
  }

}
