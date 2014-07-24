package com.hako.word.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.Debug;
import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
 
public class DetailTab extends FragmentActivity {
	
	  
	  private static int THRESHOLD = 6;
      private static float mLastTouchX;
      private static float mLastTouchY; 
      private List<Word> words;
      private int curr_lesson;
      private int curr_word = 1;
      private TextView tv_hiragana;
      private TextView tv_mean_vi;
      private Button bt_auto;
      private Button bt_audio;
      private ImageView iv_word;
      private int isAuto = 0; // isAuto = 1 when auto else 0
      Timer timerAuto = null;
      
      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.vocabulary_detail_tab);
          curr_lesson = GlobalData.current_lesson;
          words = WordHandle.getListWord(curr_lesson, 1);
          tv_hiragana = (TextView)this.findViewById(R.id.vocabulary_tv_hiragana);
          tv_mean_vi = (TextView)this.findViewById(R.id.vocabulary_tv_mean_vi);
          bt_audio = (Button)this.findViewById(R.id.vocabulary_bt_audio);
          bt_auto = (Button)this.findViewById(R.id.vocabulary_bt_auto);
          iv_word = (ImageView)this.findViewById(R.id.vocabulary_iv_word);
          
          Word word = words.get(curr_word);
          tv_hiragana.setText(word.hiragana);
          tv_mean_vi.setText(word.mean_vi);
          Bitmap img_word = null;
          try{
        	  img_word = GlobalData.getImageFromRaw(this, word.romaji);
          }
          catch (Exception e){
        	  img_word = GlobalData.getImageFromDrawable(this, "kasa");
          }
          
          if (img_word == null){
        	  img_word = GlobalData.getImageFromDrawable(this, "kasa");
          }
          iv_word.setImageBitmap(img_word);
          
          final Drawable drawable_auto = getResources().getDrawable(R.drawable.bt_auto);
          final Drawable drawable_auto_press = getResources().getDrawable(R.drawable.bt_auto_press); 
          bt_auto.setOnClickListener(new View.OnClickListener() {
        	  	
				@Override
				public void onClick(View v) {
					isAuto = 1 - isAuto;
					if (isAuto == 1){
						bt_auto.setCompoundDrawablesWithIntrinsicBounds(null, drawable_auto_press, null, null);
						timerAuto = new Timer();
						timerAuto.scheduleAtFixedRate(new TimerTask() {         
				              @Override	
				              public void run() {
				                  runOnUiThread(new Runnable()
				                  {
				                      @Override
				                      public void run()
				                      {
				                    	  curr_word = curr_word + 1;
				    	            	  if (curr_word >= words.size())
				    	            		  curr_word = 0;
				    	            	  Word word = words.get(curr_word);
				    	                  tv_hiragana.setText(word.hiragana);
				    	                  tv_mean_vi.setText(word.mean_vi);
				    	                  Bitmap img_word = null;
				    	                  try{
				    	                	  img_word = GlobalData.getImageFromRaw(DetailTab.this, word.romaji);
				    	                  }
				    	                  catch (Exception e){
				    	                	  img_word = GlobalData.getImageFromDrawable(DetailTab.this, "kasa");
				    	                  }
				    	                  
				    	                  if (img_word == null){
				    	                	  img_word = GlobalData.getImageFromDrawable(DetailTab.this, "kasa");
				    	                  }
				    	                  iv_word.setImageBitmap(img_word);
				                      }
				                  });
				              }
				          }, 1000, 3000);
					}
					else {
						bt_auto.setCompoundDrawablesWithIntrinsicBounds(null, drawable_auto, null, null);
						if (timerAuto != null)
							timerAuto.cancel();
					}
					  
				}
          });
      }
      
      private List<Fragment> getFragments(){
      	List<Fragment> fList = new ArrayList<Fragment>();
      	curr_lesson = GlobalData.current_lesson;
        words = WordHandle.getListWord(curr_lesson, 1);
        for (int index = 0; index < words.size(); index++){
        	Word word = words.get(index);
        	fList.add(DetailFragment.newInstance(word.hiragana, word.mean_vi, word.romaji));
        }
      	return fList;
      }

      private class MyPageAdapter extends FragmentPagerAdapter {
      	private List<Fragment> fragments;

          public MyPageAdapter(FragmentManager fm, List<Fragment> fragments) {
              super(fm);
              this.fragments = fragments;
          }
          @Override
          public Fragment getItem(int position) {
              return this.fragments.get(position);
          }
       
          @Override
          public int getCount() {
              return this.fragments.size();
          }
      }
      
//      @Override
//      public boolean onTouchEvent(MotionEvent ev) {
//    	  if (isAuto == 0){
//    		  final int action = ev.getAction();
//              switch (action) {
//    	          case MotionEvent.ACTION_DOWN: {
//    	              final float x = ev.getX();
//    	              final float y = ev.getY();
//    	              // Remember where we started
//    	              mLastTouchX = x;
//    	              mLastTouchY = y;
//    	              break;
//    	          }
//    	          
//    	          case MotionEvent.ACTION_UP:{
//    	        	  break;
//    	          }
//    	          
//    	          case MotionEvent.ACTION_MOVE: {
//    	              final float x = ev.getX();
//    	              final float y = ev.getY();
//    	              
//    	              // Calculate the distance moved
//    	              final float dx = x - mLastTouchX;
//    	              final float dy = y - mLastTouchY;
//    	              
//    	              if (dx > THRESHOLD){
//    	            	  curr_word = curr_word + 1;
//    	            	  if (curr_word >= words.size())
//    	            		  curr_word = 0;
//    	            	  Word word = words.get(curr_word);
//    	                  tv_hiragana.setText(word.hiragana);
//    	                  tv_mean_vi.setText(word.mean_vi);
//    	                  Bitmap img_word = null;
//    	                  try{
//    	                	  img_word = GlobalData.getImageFromRaw(this, word.romaji);
//    	                  }
//    	                  catch (Exception e){
//    	                	  img_word = GlobalData.getImageFromDrawable(this, "kasa");
//    	                  }
//    	                  
//    	                  if (img_word == null){
//    	                	  img_word = GlobalData.getImageFromDrawable(this, "kasa");
//    	                  }
//    	                  iv_word.setImageBitmap(img_word);
//    	              }
//    	              
//    	              if (dx < -THRESHOLD){
//    	            	  curr_word--;
//    	            	  if (curr_word < 0)
//    	            		  curr_word = words.size() - 1;
//    	            	  Word word = words.get(curr_word);
//    	                  tv_hiragana.setText(word.hiragana);
//    	                  tv_mean_vi.setText(word.mean_vi);
//    	                  Bitmap img_word = null;
//    	                  try{
//    	                	  img_word = GlobalData.getImageFromRaw(this, word.romaji);
//    	                  }
//    	                  catch (Exception e){
//    	                	  img_word = GlobalData.getImageFromDrawable(this, "kasa");
//    	                  }
//    	                  
//    	                  if (img_word == null){
//    	                	  img_word = GlobalData.getImageFromDrawable(this, "kasa");
//    	                  }
//    	                  iv_word.setImageBitmap(img_word);
//    	              }
//    	              // Remember this touch position for the next move event
//    	              mLastTouchX = x;
//    	              mLastTouchY = y;
//    	              break;
//    	          }
//              }
//    	  }
//          
//          return true;
//      }
}