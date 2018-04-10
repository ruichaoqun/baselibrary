package com.kapp.library.api.dao;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.kapp.library.KAPPApplication;

public abstract class SQLiteHelper extends DataBase {

	private SQLiteDatabase db;

	public SQLiteHelper() {
		super(KAPPApplication.getContext());
		db = getWritableDatabase();
		initCreateTable();
	}

	private void initCreateTable() {
		try {
			if(db != null){
				if(!db.isOpen())
					openDatabase();
				logger.i(createTable());
				db.execSQL(createTable());
				closeDatabase();
			}
		} catch (Exception e) {
			logger.w(e);
		}
	}

	protected abstract String getTableName();

	/** 插入数据 */
	protected boolean insetValues(ContentValues values) {

		if (db == null)
			return false;
		if(!db.isOpen())
			openDatabase();
		try{
			if (db.insert(getTableName(), null, values) != -1){
				closeDatabase();
				return true;
			}
		}catch(Exception e){
			logger.w(e);
		}finally{
			closeDatabase();
		}
		return false;
	}

	/** 执行Sql 无参 */
	protected boolean execValues(String sql){
		if (db == null || TextUtils.isEmpty(sql))
			return false;
		if (!db.isOpen())
			openDatabase();
		try{
			db.execSQL(sql);
			return true;
		}catch(Exception e){
			logger.w(e);
		}finally{
			closeDatabase();
		}
		return false;
	}

	/** 执行Sql 可带参 */
	protected boolean execValues(String sql, List<String> params) {
		if (db == null || TextUtils.isEmpty(sql))
			return false;
		if (!db.isOpen())
			openDatabase();
		try{
			db.execSQL(sql, getFormatArrays(params));
			return true;
		}catch(Exception e){
			logger.w(e);
		}finally{
			closeDatabase();
		}
		return false;
	}

	/** 删除表 */
	protected boolean deleteTable(String table_name) {
		try {
			if (!TextUtils.isEmpty(table_name) && db != null) {
				if (!db.isOpen())
					openDatabase();
				String sql = String.format("DROP TABLE %s", table_name);
				db.execSQL(sql);
				closeDatabase();
				return true;
			}
		} catch (Exception e) {
			logger.w(e);
		}
		return false;
	}

	/** 批量操作 */
	protected boolean queueOperation(List<String> queues) {
		if (db == null || queues == null) {
			return false;
		}
		try {
			if (!db.isOpen())
				openDatabase();
			for (String sql : queues) {
				db.execSQL(sql);
			}
			closeDatabase();
			return true;
		} catch (Exception e) {
			logger.w(e);
		}
		return false;
	}
	
	/** 查询数据 */
	protected Cursor selectValues(String sql,List<String> params){
		
		Cursor cursor = null;
		try{
			if(db == null)
				return cursor;
			
			if(!db.isOpen())
				openDatabase();
			
			cursor = db.rawQuery(sql, getFormatArrays(params));
			
			//此处数据库关闭请在实现方法体中执行，避免查询的Cursor数据被清空
		}catch(Exception e){
			logger.w(e);
		}
		return cursor;
	}
	
	/** 打开数据库 */
	protected synchronized void openDatabase() {
		try{
			if(db != null){
				if(!db.isOpen())
					db = getWritableDatabase();
			}else{
				db = getWritableDatabase();
			}
		}catch(Exception e){
			logger.w(e);
		}
	}

	/** 关闭数据库 */
	protected synchronized void closeDatabase() {
		try{
			if(db != null)
				db.close();
		}catch(Exception e){
			logger.w(e);
		}
	}
	
	//格式化数据
	private String[] getFormatArrays(List<String> params){
		if(params == null || params.size() == 0)
			return null;
		
		String[] arrays = params.toArray(new String[params.size()]);
		for(String str : arrays){
			logger.i(str);
		}
		return arrays;
	}
	
	/** 释放资源占用 */
	public void closeDbCache(){
		closeDatabase();
		close();
		db = null;
	}

}
