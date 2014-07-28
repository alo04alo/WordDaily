package com.hako.word.vocabulary;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.hako.base.Word;
import com.hako.base.WordHandle;
import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
 
public class DetailTab extends FragmentActivity {
      
      private int isAuto = 0; // isAuto = 1 when auto else 0
      private Timer timerAuto = null;
      
      private List<Word> words;
      private int curr_lesson;
      private PageAdapter pageAdapter;
      private Button bt_auto;
      private int currentPage = 0;
      ViewPager pager;
      
      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          
          setContentView(R.layout.vocabulary_detail_tab);
          
          List<Fragment> fragments = getFragments();
          
          pageAdapter = new PageAdapter(getSupportFragmentManager(), fragments);
          
          pager = (ViewPager)findViewById(R.id.viewpager);
          pager.setAdapter(pageAdapter);
          
//          bt_auto = (Button)this.findViewById(R.id.vocabulary_bt_auto);
//          
//          final Drawable drawable_auto = getResources().getDrawable(R.drawable.bt_auto);
//          final Drawable drawable_auto_press = getResources().getDrawable(R.drawable.bt_auto_press); 
//          bt_auto.setOnClickListener(new View.OnClickListener() {
//        	  	
//				@Override
//				public void onClick(View v) {
//					isAuto = 1 - isAuto;
//					currentPage = pager.getCurrentItem();
//					if (isAuto == 1){
//						timerAuto = new Timer();
//						timerAuto.schedule(new TimerTask() {
//
//			                @Override
//			                public void run() {
//			                    runOnUiThread(new Runnable() {
//			                        @Override
//			                        public void run() {
//			                            if (currentPage == words.size()) {
//			                                currentPage = 0;
//			                            }
//			                            pager.setCurrentItem(currentPage++, true);
//			                        }
//			                    });
//			                }
//			            }, 500, 3000);
//					}
//					else {
//						bt_auto.setCompoundDrawablesWithIntrinsicBounds(null, drawable_auto, null, null);
//						if (timerAuto != null)
//							timerAuto.cancel();
//					}
//					  
//				}
//          });
      }
      
      public void buttonClick(View v){
//		  final Drawable drawable_auto = getResources().getDrawable(R.drawable.bt_auto);
//		  final Drawable drawable_auto_press = getResources().getDrawable(R.drawable.bt_auto_press); 
		  if(v.getId() == R.id.vocabulary_bt_auto){
			  	isAuto = 1 - isAuto;
				currentPage = pager.getCurrentItem();
				if (isAuto == 1){
					timerAuto = new Timer();
					timerAuto.schedule(new TimerTask() {
	
		                @Override
		                public void run() {
		                    runOnUiThread(new Runnable() {
		                        @Override
		                        public void run() {
		                            if (currentPage == words.size()) {
		                                currentPage = 0;
		                            }
		                            pager.setCurrentItem(currentPage++, true);
		                        }
		                    });
		                }
		            }, 1000, 2000);
				} else {
					if (timerAuto != null)
						timerAuto.cancel();
				}
			}
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

      private class PageAdapter extends FragmentPagerAdapter {
      	private List<Fragment> fragments;

          public PageAdapter(FragmentManager fm, List<Fragment> fragments) {
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
}