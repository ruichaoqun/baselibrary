package com.kapp.library.api.dao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kapp.library.utils.Logger;

public abstract class DataBase extends SQLiteOpenHelper {

	protected Logger logger = new Logger(this.getClass().getSimpleName());
	protected final static String TABLE_NAME = "SuperTrade.rg";
	protected static int DATABASE_VERSION = 1;

	@SuppressLint("SdCardPath")
	public DataBase(Context context) {
		super(context, TABLE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table test_table(test text,gender integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	protected abstract String createTable();

}
