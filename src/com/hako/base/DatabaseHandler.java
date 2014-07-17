package com.hako.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.hako.utils.Debug;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.webkit.WebChromeClient.CustomViewCallback;

public class DatabaseHandler extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "words.sqlite";
	private static final String TABLE_NAME = "lessons";
	private static final String DATABASE_PATH = "/data/data/com.hako.word/databases/";
	private SQLiteDatabase myDB;
	private final Context ctx;
	public static final String TAG = "Database";
	private String sql;
	
	private static final String ID = "id";
	private static final String TITLE = "title";
	private static final String DISCRIPTION = "discription";
	private static final String IMG = "img";
	private static final String SCORE = "score";
	private static final String ISLOCK = "is_lock";

	private static final String[] COLUMNS = { ID, TITLE, DISCRIPTION, IMG, SCORE, ISLOCK};
	private static final String CREATE_TABLE_LESSONS = "CREATE TABLE "
			+ TABLE_NAME + "("+ ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT,"
			+ DISCRIPTION + " TEXT," + IMG + " TEXT," + SCORE + " INTEGER,"
			+ ISLOCK + " BOOLEAN" + ")";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.ctx = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_LESSONS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		this.onCreate(db);
	}

	public synchronized void close() {
		if (myDB != null)
			myDB.close();
		super.close();
	}

	public boolean checkAndCopyDatabase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			Log.d(TAG, "database already exist!");
			return true;
		} else {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				Log.d(TAG, "Error copying database");
			}
			return false;
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DATABASE_PATH + DATABASE_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
		}
		if (checkDB != null)
			checkDB.close();
		return checkDB != null ? true : false;
	}
	
	public boolean checkDatabaseExist(){
		String myPath = DATABASE_PATH + DATABASE_NAME;
		File file = new File(myPath);
		if(file.exists()){
			return true;
		}else{
			return false;
		}
		
	}

	private void copyDataBase() throws IOException {
		InputStream myInput = ctx.getAssets().open(DATABASE_NAME);
		String outFileName = DATABASE_PATH + DATABASE_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[2 * 1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public SQLiteDatabase openDataBase() throws SQLException {
		String myPath = DATABASE_PATH + DATABASE_NAME;
		try {
			myDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			return myDB;
		} catch (Exception e) {
			Debug.out("Can't open database");
			return null;
		}

	}

	public Lesson getLesson(int id) {
		Cursor cursor = null;
		try {
			cursor = myDB.query(TABLE_NAME, COLUMNS, ID + " = " + id, null,
					null, null, null);
		} catch (Exception e) {
			Debug.out(e.toString());
		}
		Lesson lesson = new Lesson();
		if (cursor.moveToFirst()) {
			lesson.id = cursor.getInt(0);
			lesson.title = cursor.getString(1);
			lesson.discription = cursor.getString(2);
			lesson.img = cursor.getString(3);
			lesson.score = cursor.getInt(4);
			lesson.is_lock = cursor.getInt(5);
		}
		return lesson;
	}

	/**
	 * 
	 * @return many questions
	 */
	public List<Lesson> getAllLesson() {
		List<Lesson> lessons = new ArrayList<Lesson>();
		String sql = "select * from " + TABLE_NAME;
		Debug.out(sql);
		Cursor cusor = myDB.rawQuery(sql, null);
		if (cusor != null && cusor.getCount() > 0) {
			if (cusor.moveToFirst()) {
				do {
					Lesson lesson = new Lesson();
					lesson.id = cusor.getInt(cusor.getColumnIndex(ID));
					lesson.title = cusor.getString(cusor.getColumnIndex(TITLE));
					lesson.discription = cusor.getString(cusor.getColumnIndex(DISCRIPTION));
					lesson.img = cusor.getString(cusor.getColumnIndex(IMG));
					lesson.score = cusor.getInt(cusor.getColumnIndex(SCORE));
					lesson.is_lock = cusor.getInt(cusor.getColumnIndex(ISLOCK));
					lessons.add(lesson);
				} while (cusor.moveToNext());
			}
		}
		return lessons;
	}
	
	/**
	 * for calculate number of lessons
	 * @return
	 */
	public int NumberOfLesson() {
		String sql = "select COUNT (*) from " + TABLE_NAME;
		Cursor cursor = myDB.rawQuery(sql, null);
		cursor.moveToFirst();
	    int count = cursor.getInt(0);
		cursor.close();
		return count;
	}

//	public int updateOneQuestion(Question question) {
//		ContentValues values = new ContentValues();
//		values.put(COUNT, question.getCount() + 1);
//		return myDB.update(TABLE_NAME, values, ID + " = ?",
//				new String[] { String.valueOf(question.getId()) });
//	}
}