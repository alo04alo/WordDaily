package com.hako.word.vocabulary;

import java.util.Locale;

import com.hako.utils.GlobalData;
import com.hako.word.R;
import com.hako.word.ShowFunctionArrayAdapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class VocabularyTabBar extends ActionBarActivity implements ActionBar.TabListener {
	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	
	Button btnShowFunction;
	Dialog dialogFunction;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_view);                
        
		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//		actionBar.setBackgroundDrawable(new ColorDrawable(0xff00DDED));
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		setProgressBarVisibility(true);
		
     }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
	    public Fragment getItem(int index) {
	 
	        switch (index) {
	        case 0:
	            // Top Rated fragment activity
	            return new SummaryFragment();
	        case 1:
	            // Movies fragment activity
	            return new SummaryFragment();
	        }
	 
	        return null;
	    }

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.tab_voca_1).toUpperCase(l);
			case 1:
				return getString(R.string.tab_voca_2).toUpperCase(l);
			}
			return null;
		}
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.vocabulary_tab, container,
					false);
			return rootView;
		}
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
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

}
