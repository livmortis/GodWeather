package com.example.myweather.model;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.myweather.db.WetherSQL;

public class ControlSQL {
	private SQLiteDatabase db;
	private static ControlSQL controlSQL;
	public static final String DB_NAME="baby_weather_SQL";
	public static final int VERSION=1;
	public ControlSQL(Context context){  // public ControlSQL(WetherSQL wetherOpenHelper){   //SQLiteOpenHelper的对象在这里就建好。
		WetherSQL wetherOpenHelper=new WetherSQL(context, DB_NAME, null, VERSION);//注意四个参数。
		db=wetherOpenHelper.getWritableDatabase();
	}
	public synchronized static ControlSQL getInstance(Context context){     //synchronized
		if (controlSQL==null){
		    controlSQL=new ControlSQL(context);
		}
		return controlSQL;
	}
	public void saveProvince(Province province){
		if(province!=null){//检验参数！
		    ContentValues values=new ContentValues();
		    values.put("province_name", province.getProvinceName());
		    values.put("province_code", province.getProvinceCode());
		    db.insert("province", null, values);
		}
	}
	public List<Province> loadProvince(){     //loadXXX()的返回值必须是一组元素为对象的集合，不然如果是数组集合<String>等于只读取了数据表中XXX_name一行。
		Cursor cursor=db.query("province",null,null,null,null,null,null);//这里其余六个都是null即可，因为不用限定省列表（只有一个）。
		List<Province> list=new ArrayList<Province>();
		
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setProvinceId(cursor.getInt(cursor.getColumnIndex("id")));//注意columnIndex的含义。
			    province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			    province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			    list.add(province);
			}
			while(cursor.moveToNext());
		}
		return list;
	}
	public void saveCity(City city){
		if(city!=null){
		    ContentValues values=new ContentValues();
		    values.put("city_name",city.getCityName());
		    values.put("city_code", city.getCityCode());
		    values.put("province_id", city.getProvince_id());
		    db.insert("cities", null, values);
		}
	}
	public List<City> loadCity(int provincesid){
		//City city=new City();绝对不能在这里！！！！！！！！！！！！！！！！！！！！！！！
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("cities", null,"province_id=?", new String[]{String.valueOf(provincesid)}, null, null, null);//错误情况：new string[]{province_id}
		if(cursor.moveToFirst()){
			do{
				City city=new City();
			    city.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
		        city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
		        city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
		        city.setProvince_id(provincesid);//city.setProvince_id(cursor.getInt(cursor.getColumnIndex("province_id"))); 不必再循环从数据库提取了，因为每个城市的都一样，都等于传入的参数provincesid。
		        list.add(city);
		        
			}
			while(cursor.moveToNext());
		}
		return list;
	}
	public void saveCountry(Country country){
		if(country!=null){
		    ContentValues values=new ContentValues();
		    values.put("country_name", country.getCountryName());//无id列，因为是自增长的。
		    values.put("country_code", country.getCountryCode());
		    values.put("city_id", country.getCity_id());
		    db.insert("country", null, values);
		}
	}
	public List<Country> loadCountry(int citysid){
		//Country country=new Country();     不可以在这里！！！！！！！！！
		List<Country> list=new ArrayList<Country>();
		Cursor cursor=db.query("country", null, "city_id=?", new String[]{String.valueOf(citysid)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Country country=new Country(); 
				country.setCountryId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCity_id(citysid);
				list.add(country);
			}
			while(cursor.moveToNext());
		}
	
	    return list;
	}
	
}
