package com.hako.word.vocabulary;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity;
 
 
public class SummaryTab extends ListActivity {
      private String[] hiragana = {"あさ","かばん","にほんご","へや","きに","あさ","かばん","にほんご","へや","きに","あさ","かばん","にほんご","へや","きに","あさ","かばん","にほんご","へや","きに"};
      private String[] kanji = {"朝","鞄","日本語","へや","きに", "朝","鞄","日本語","へや","きに", "朝","鞄","日本語","へや","きに", "朝","鞄","日本語","へや","きに"};
      private String[] mean = {"buổi sáng","cái cặp","tiếng nhật","căn hộ","đất nước", "buổi sáng","cái cặp","tiếng nhật","căn hộ","đất nước", "buổi sáng","cái cặp","tiếng nhật","căn hộ","đất nước", "buổi sáng","cái cặp","tiếng nhật","căn hộ","đất nước"};
      
      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setListAdapter(new VocabularyArrayAdapter(this, hiragana, kanji, mean));
        }
      
      @Override
      protected void onListItemClick(ListView l, View v, int position, long id){
    	String selectedValue = (String) getListAdapter().getItem(position);
  		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
      }
      
}
