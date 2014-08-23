package com.hako.word.vocabulary;

import java.util.List;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.Debug;
import com.hako.utils.GlobalData;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
 
public class SummaryFragment extends ListFragment {
	private List<Word> words; 
	private String[] hiragana;
	private String[] kanji;
	private String[] mean;
	private String[] romaji;
	
	@Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  int current_lesson = GlobalData.current_lesson;
      words = WordHandle.getListWord(current_lesson);
      hiragana = new String[words.size()];
      kanji = new String[words.size()];
      mean = new String[words.size()];
      romaji = new String[words.size()];
      for(int index = 0; index < words.size(); index++){
    	  hiragana[index] = words.get(index).hiragana;
    	  kanji[index] = words.get(index).kanji;
    	  mean[index] = words.get(index).mean_vi;
    	  romaji[index] = words.get(index).romaji;
      }
      setListAdapter(new VocabularyArrayAdapter(getActivity(), hiragana, kanji, mean, romaji));
      
	 }


	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (romaji[position] != null)
			GlobalData.playAudioFromAsset(getActivity(), romaji[position] + ".mp3");
		else
			Toast.makeText(getActivity(), "Hiện tại hệ thống chưa cập nhật file nghe của từ vựng này!", 0);
	}


	
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//            Bundle savedInstanceState) {
//        View rootView = inflater.inflate(R.layout.vocabulary_summary_fragment, container, false);
//        return rootView;
//    }
}
