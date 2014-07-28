package com.hako.word.vocabulary;

import com.hako.utils.Debug;
import com.hako.utils.GlobalData;
import com.hako.word.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	public static final String HIRAGANA = "HIRAGANA";
	public static final String MEANVI = "MEANVI";
	public static final String IMG = "IMG";
	public static Context ctx;
	
	public static final DetailFragment newInstance(String hiragana, String meanvi, String img)
	{
		DetailFragment fragment = new DetailFragment();
		Bundle bundle = new Bundle(1);
	    bundle.putString(HIRAGANA, hiragana);
	    bundle.putString(MEANVI, meanvi);
	    bundle.putString(IMG, img);
	    fragment.setArguments(bundle);
	    return fragment;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		this.ctx = container.getContext();
		String hiragana = getArguments().getString(HIRAGANA);
		String meanvi = getArguments().getString(MEANVI);
		String img = getArguments().getString(IMG);
		View view = inflater.inflate(R.layout.vocabulary_detail_fragment, container, false);
		TextView tvHiragana = (TextView)view.findViewById(R.id.vocabulary_tv_hiragana);
		TextView tvMeanVi = (TextView)view.findViewById(R.id.vocabulary_tv_mean_vi);
		ImageView iv_word = (ImageView) view.findViewById(R.id.vocabulary_iv_word);
        Bitmap img_word = getBitmapByName(img);
		iv_word.setImageBitmap(img_word);
		tvHiragana.setText(hiragana);
		tvMeanVi.setText(meanvi);
        return view;
    }
	
	public Bitmap getBitmapByName(String name){
		Bitmap img_word = null;
		try{
	      	  img_word = GlobalData.getImageFromRaw(ctx, name);
	        }
        catch (Exception e){
      	  img_word = GlobalData.getImageFromDrawable(ctx, "kasa");
        }
        if (img_word == null){
      	  img_word = GlobalData.getImageFromDrawable(ctx, "kasa");
        }
        return img_word;
	}
	
}
