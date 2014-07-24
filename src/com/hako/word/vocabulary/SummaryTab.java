package com.hako.word.vocabulary;

import java.util.List;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.app.ListActivity;
 
 
public class SummaryTab extends ListActivity {
	  private List<Word> words; 
	  private String[] hiragana;
	  private String[] kanji;
	  private String[] mean;
	  
      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          
          int current_lesson = GlobalData.current_lesson;
          words = WordHandle.getListWord(current_lesson, 0);
          hiragana = new String[words.size()];
          kanji = new String[words.size()];
          mean = new String[words.size()];
          for(int index = 0; index < words.size(); index++){
        	  hiragana[index] = words.get(index).hiragana;
        	  kanji[index] = words.get(index).kanji;
        	  mean[index] = words.get(index).mean_vi;
          }
          setListAdapter(new VocabularyArrayAdapter(this, hiragana, kanji, mean));
        }
      
      @Override
      protected void onListItemClick(ListView l, View v, int position, long id){
    	String selectedValue = (String) getListAdapter().getItem(position);
  		Toast.makeText(this, selectedValue, Toast.LENGTH_SHORT).show();
      }
}
