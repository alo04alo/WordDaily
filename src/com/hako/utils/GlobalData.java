package com.hako.utils;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.hako.base.DatabaseHandler;
import com.hako.base.Lesson;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class GlobalData {
	public static List<Lesson> lessons;
	public static DatabaseHandler db;
	
	public static DatabaseHandler openDatabase(Context context){
		db = new DatabaseHandler(context);
		if (db.checkDatabaseExist() == false) {			
			db.checkAndCopyDatabase();
		}
		db.openDataBase();
		return db;
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
		Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier(name, "drawable", context.getPackageName()));
		
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
	
}
