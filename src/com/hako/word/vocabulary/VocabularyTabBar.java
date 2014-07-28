package com.hako.word.vocabulary;

import com.hako.utils.GlobalData;
import com.hako.word.MainActivity;
import com.hako.word.R;
import com.hako.word.ShowFunctionArrayAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

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
        tabHost = (TabHost)findViewById(android.R.id.tabhost);
        
        // get Button reference
        btnHome = (Button) findViewById(R.id.vocabulary_btn_home);
        btnShowFunction = (Button) findViewById(R.id.vocabulary_btn_down);
        
        // Set TabChangeListener called when tab changed
        tabHost.setOnTabChangedListener(this);
     
        TabHost.TabSpec spec;
        Intent intent;
   
         // SummaryTab: Create  Intents to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SummaryTab.class);
        spec = tabHost.newTabSpec("First").setIndicator("Dạng thu gọn")
                      .setContent(intent);
        tabHost.addTab(spec);
   
        // DetailTab
        intent = new Intent().setClass(this, DetailTab.class);
        spec = tabHost.newTabSpec("Second").setIndicator("Dạng đầy đủ")
                      .setContent(intent);  
        tabHost.addTab(spec);
        
//        tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.bt_detail_tab);
        
        int tabCount = tabHost.getTabWidget().getTabCount();
        
        for (int i = 0; i < tabCount; i++) {
            final View view = tabHost.getTabWidget().getChildTabViewAt(i);
            if ( view != null ) {
                // reduce height of the tab
                view.getLayoutParams().height *= 0.66;
                view.setBackgroundColor(Color.parseColor("#F5F5F5"));
                //  get title text view
                final View textView = view.findViewById(android.R.id.title);
                if ( textView instanceof TextView ) {
                    // just in case check the type

                    // center text
                    ((TextView) textView).setGravity(Gravity.CENTER);
                    // wrap text
                    ((TextView) textView).setSingleLine(false);
                    ((TextView) textView).setTextColor(Color.parseColor("#003300"));
                    ((TextView) textView).setTextSize(20);

                    // explicitly set layout parameters
                    textView.getLayoutParams().height = ViewGroup.LayoutParams.FILL_PARENT;
                    textView.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
                }
            }
        }
        
        tabHost.getTabWidget().setCurrentTab(0);
        tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#ffffff"));
        
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
	        tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#F5F5F5"));
	    } 

	  tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#ffffff"));
	  
  }

}
