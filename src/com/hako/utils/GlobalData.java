package com.hako.utils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import com.hako.base.DatabaseHandler;
import com.hako.base.Lesson;
import com.hako.base.Word;
import com.hako.matchword.MatchWordActivity;
import com.hako.word.R;
import com.hako.word.exam.ExamTabActivity;
import com.hako.word.selectPicture.SelectPictureActivity;
import com.hako.word.viewPicture.ViewPictureActivity;
import com.hako.word.viewPicture.ViewPictureArrayAdapter;
import com.hako.word.vocabulary.VocabularyTabBar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class GlobalData {
	public static final int WORD_LIMIT = 20;
	public static final int TEST_LIMIT = 15;
	public static final int WORD_INCLUDE_IMAGE = 1;
	public static final String COLOR_TEXT_TAB_BAR = "#48698a";
	public static final String COLOR_BACKGROUND_TAB_BAR = "#5b84ad";
	public static final char[] charArray = { 'あ', 'い', 'う', 'え', 'お','か','き','く','け','こ','が','ぎ','ご','げ','ご','さ','し','す','せ','そ','ざ','じ','ず','ぜ','ぞ','た','ち','つ','て','と','だ','ぢ','ず','で','ど','な','に','ぬ','ね','の','は','ひ','ふ','へ','ほ','ば','び','ぶ','べ','ぼ','ぱ','ぴ','ぷ','ぺ','ぽ','ま','み','む','め','も','や','ゆ','よ','ら','り','る','れ','ろ','わ','を','ん','っ'};
	
	public static List<Lesson> lessons;
	public static DatabaseHandler db;	
	public static String[] allFunctions = {"Từ vựng", "Khớp từ", "Chọn tranh", "Nghe từ", "Xem tranh", "Kiểm tra"};
	public static int current_lesson;	
	public static int currentExam = 1;
	
	public static  List<Word> testData;
	
	public static DatabaseHandler openDatabase(Context context){
		db = new DatabaseHandler(context);
		if (db.checkDatabaseExist() == false) {			
			db.checkAndCopyDatabase();
		}
		db.openDataBase();
		return db;
	}
	
	public static Boolean checkEnableLesson() {
		if (current_lesson == 1)
			return true;
		if (lessons.get(current_lesson - 1).is_lock == 1)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @param min
	 * @param max
	 * @param ignore
	 * @return
	 */
	public static int[] getRandomThreeNumber(int min, int max, int ignore) {			

		int[]  number = new int[3];
		number[0] = getRandomOneNumber(min, max, ignore);
		number[1] = getRandomOneNumber(min, max, ignore);
		number[2] = getRandomOneNumber(min, max, ignore);

		if ((number[0] != number[1]) && (number[0] != number[2]) && (number[1] != number[2])) {
			return number;
		} else {
			return getRandomThreeNumber(min, max, ignore);
		}
	}

	public static int getRandomOneNumber(int min, int max, int ignore) {
		Random random = new Random();
		int number = random.nextInt(max - min + 1) + min;
		if (number != ignore) {
			return number;
		} else {
			return getRandomOneNumber(min, max, ignore);
		}
	}
	
	public static void setAnimationForButton(Context context, Button button, boolean answer) {
		// True answer
		AnimationDrawable animation = new AnimationDrawable();
		if (answer == true) {
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_true_answer), 200);
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_true_answer), 200);
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		} else {
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_false_answer), 200);
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_false_answer), 200);
			animation.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		}
		animation.setOneShot(true);
		button.setBackgroundDrawable(animation);	
		animation.start();
	}
	
	public static void setAnimationForButton(Context context, Button falseButton, Button trueButton){
		// False answer
		AnimationDrawable animationForTrue = new AnimationDrawable();
		AnimationDrawable animationForFalse = new AnimationDrawable();
		
		animationForTrue.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_true_answer), 200);
		animationForTrue.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		animationForTrue.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_true_answer), 200);
		animationForTrue.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		
		animationForFalse.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_false_answer), 200);
		animationForFalse.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		animationForFalse.addFrame(context.getResources().getDrawable(R.drawable.common_btn_border_false_answer), 200);
		animationForFalse.addFrame(context.getResources().getDrawable(R.drawable.common_btn_boder_normal_answer), 200);
		
		animationForFalse.setOneShot(true);
		falseButton.setBackgroundDrawable(animationForFalse);	
		animationForFalse.start();
		
		animationForTrue.setOneShot(true);
		trueButton.setBackgroundDrawable(animationForTrue);	
		animationForTrue.start();
	}
	
	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}
	
	public static Bitmap getImageFromDrawable(Context context, String name) {
		Drawable drawable = null;
		try {
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier(name, "drawable", context.getPackageName()));
		} catch (Exception e){
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier("anata", "drawable", context.getPackageName()));
		}
		
		if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }
		
	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);
	    
	    return bitmap;
	}
	
	public static Bitmap getImageFromRaw(Context context, String name) {
//		Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(name, "raw", context.getPackageName()));
		Drawable drawable = null;
		try {
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier(name, "raw", context.getPackageName()));
		} catch (Exception e){
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier("anata", "raw", context.getPackageName()));
		}
		if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }
		
	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
	
	
	public static Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);
	    
	    return bitmap;
	}
	
	public static Bitmap scaleImage(Bitmap image, int newWidth, int newHeight){
		return Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
	}
	
	public static Bitmap scaleImage(Bitmap image, double wScale, double hScale){
		return Bitmap.createScaledBitmap(image, (int)(image.getWidth()*wScale), (int)(image.getHeight()*hScale), true);
	}
	
	public static void playAudioFromAsset(Context context, String fileName) {
		MediaPlayer mediaPlayer = null;
		try{
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
			}
			AssetFileDescriptor descriptor = context.getAssets().openFd(fileName);
		    long start = descriptor.getStartOffset();
		    long end = descriptor.getLength();
		    mediaPlayer.setDataSource(descriptor.getFileDescriptor(), start, end);
		    mediaPlayer.setVolume(8f, 8f);
		    mediaPlayer.prepare();
		    mediaPlayer.start();
		    
		} catch (IllegalArgumentException e) {
			// Error handling
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	        
	}
	
	public static AlertDialog createDialogGames(final Context context) {
		String gameName[] = GlobalData.allFunctions;
		
		ContextThemeWrapper wrapper = new ContextThemeWrapper(context, android.R.style.Theme_Holo);
		LayoutInflater inflater = (LayoutInflater) wrapper.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(wrapper);
		
		View view = (View) inflater.inflate(R.layout.common_dialog_function, null);
		alertDialogBuilder.setView(view);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setInverseBackgroundForced(true);
		
		final AlertDialog alertDialog = alertDialogBuilder.create();		
		
		ListView lv = (ListView) view.findViewById(R.id.lvFunction);
		lv.setAdapter(new ViewPictureArrayAdapter(context, gameName));
		lv.setClickable(true);	
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				((DialogGamesActionListener) context).onItemClick(arg0, arg1, arg2, arg3);
			}
		});
		return alertDialog;
	}
	
	public static void startGameActivity(Context context, String nameGame) {
		if (nameGame == "Từ vựng") {
			context.startActivity(new Intent(context, VocabularyTabBar.class));			
		} else if (nameGame == "Khớp từ") {
			context.startActivity(new Intent(context, MatchWordActivity.class));
		} else if (nameGame == "Chọn tranh") {
			context.startActivity(new Intent(context, SelectPictureActivity.class));
		} else if (nameGame == "Nghe từ") {
//			context.startActivity(new Intent(context, VocabularyTabBar.class));
		} else if (nameGame == "Xem tranh") {
			context.startActivity(new Intent(context, ViewPictureActivity.class));
		} else if (nameGame == "Kiểm tra") {
			context.startActivity(new Intent(context, ExamTabActivity.class));
		}
	}
	
	public static <T> void fillArray(T[] array, T obj) {
		for (int i = 0; i < array.length; i++) {
			array[i] = obj;
		}
	}

	public static <T> void fillList(List<T> list, T obj, int len) {
		for (int i = 0; i < len; i++) {
			list.add(obj);
		}
	}
}
