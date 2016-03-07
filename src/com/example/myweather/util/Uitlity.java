package com.example.myweather.util;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.myweather.model.City;
import com.example.myweather.model.ControlSQL;
import com.example.myweather.model.Country;
import com.example.myweather.model.Province;

public class Uitlity {
	public synchronized static boolean handleProviceResponse(String response,ControlSQL control){  //为何有synchronized
	    if(!TextUtils.isEmpty(response)){
	       String[] proList=response.split(",");
	       if(proList!=null&&proList.length>0){
	    	   for(String i:proList){
	    		   String[] pro=i.split("\\|");
	    		   Province province=new Province();
	    		   province.setProvinceName(pro[1]);
	    		   province.setProvinceCode(pro[0]);
	    		   control.saveProvince(province);
	    		   
	    	   }
	    	   return true;
	       }
	    }
	    return false;
	}
	public static boolean handleCityResponse(String response,ControlSQL control,int provinceID){
		if(!TextUtils.isEmpty(response)){
			String[] citList=response.split(",");
			if(citList!=null&&citList.length>0){
			    for(String j:citList){
				    String[] cit=j.split("\\|");
				    City city=new City();
				    city.setCityName(cit[1]);
				    city.setCityCode(cit[0]);
				    city.setProvince_id(provinceID);
				    control.saveCity(city);
				    
			    }
			    return true;
			}
		}
		return false;
	}
	public static boolean handCountryResponse(String response,ControlSQL control,int cityID){
		if(!TextUtils.isEmpty(response)){
			String[] conList=response.split(",");
			if(conList!=null&&conList.length>0){
			    for(String l:conList){
				    String[] con=l.split("\\|");
				    Country country=new Country();
				    country.setCountryName(con[1]);
				    country.setCountryCode(con[0]);
				    country.setCity_id(cityID);
				    control.saveCountry(country);
				   
			    }
			    return true;
			}
		}
		return false;
	}
	//以上是解析城市列表数据，由于比较简单是手动解析（加分割变数组）。以下是解析天气数据，比较复杂，且是JSON格式所以用JSONObject解析。
	public static void handleWeatherResponse(Context context,String response){
		
		try {
			JSONObject json=new JSONObject(response);
			JSONObject weatherInfo=json.getJSONObject("weatherinfo");
			String cityName=weatherInfo.getString("city");
			String weatherCode=weatherInfo.getString("cityid");
			String temp1=weatherInfo.getString("temp1");
			String temp2=weatherInfo.getString("temp2");
			String weatherDescribe=weatherInfo.getString("weather");
			String ptime=weatherInfo.getString("ptime");
			Boolean selectedCity=true;
			savePreferences(context,cityName,weatherCode,temp1,temp2,weatherDescribe,ptime,selectedCity);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void savePreferences(Context context,String a,String b,String c,String d,String e,String f,Boolean g){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor spf=PreferenceManager.getDefaultSharedPreferences(context).edit();
		spf.putString("cityName", a);
		spf.putString("weatherCode", b);
		spf.putString("temp1", c);
		spf.putString("temp2", d);
		spf.putString("weatherDescribe", e);
		spf.putString("ptime", f);
		spf.putString("date", sdf.format(new Date()));
		spf.putBoolean("selectedCity", g);
		spf.commit();
	}
}
