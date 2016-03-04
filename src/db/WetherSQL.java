package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
public class WetherSQL extends SQLiteOpenHelper{
	public WetherSQL(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public final static String CREATE_PROVICE="create table province("
			+"id integer primary key autoincrement,"//不是int
			+"province_name text,"             //不是String
			+"province_code text)";
	public final static String CREATE_CITIES="create table cities("
			+"id integer primary key autoincrement,"
			+"city_name text,"
			+"city_code text,"
			+"province_id integer)";
	public final static String CREATE_COUNTRY="create table country("
			+"id integer primary key autoincrement,"
			+"country_name text,"
			+"country_code text,"
			+"city_id integer)";
	
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_PROVICE);
		db.execSQL(CREATE_CITIES);
		db.execSQL(CREATE_COUNTRY);
	}
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		
	}

}
