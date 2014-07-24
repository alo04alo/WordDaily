package com.hako.matchword;

import java.util.List;

import com.hako.base.Word;
import com.hako.utils.Debug;
import com.hako.word.R;
import com.hako.word.R.id;
import com.hako.word.R.layout;
import com.hako.word.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ListView;

public class MatchWordActivity extends ActionBarActivity {
	
	private ArrayAdapter<String> adapter_viet;
	private ArrayAdapter<String> adapter_japan;
	private List<Word> listWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_word);
		
		adapter_viet = new ArrayAdapter<String>(this, R.layout.matchword_list_item);
		adapter_viet.setNotifyOnChange(true);
		final ListView listView_viet = (ListView) 
				findViewById(R.id.matchword_list_vie);
		listView_viet.setAdapter(adapter_viet);
		listView_viet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				Debug.out("viet item clicked" + position);
				TextView tv = (TextView) listView_viet.getChildAt(position);
			}
		});
		
		adapter_japan = new ArrayAdapter<String>(this, R.layout.matchword_list_item); 
		final ListView listView_japan = (ListView) 
				findViewById(R.id.matchword_list_japan);
		listView_japan.setAdapter(adapter_japan);
		listView_japan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, 
					int position, long id) {
				Debug.out("japan item clicked" + position);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match_word, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
