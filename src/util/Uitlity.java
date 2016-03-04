package util;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.Spanned;
import android.text.TextUtils;
import model.City;
import model.ControlSQL;
import model.Country;
import model.Province;

public class Uitlity {
	private String[] proList;
    private String[] citList;
    private String[] conList;
	public boolean handleProviceResponse(String response,ControlSQL control){
	    if(response!=null){
	       proList=response.toString().split(",");
	       if(proList!=null&&proList.length>0){
	    	   for(String i:proList){
	    		   String[] pro=i.split("\\|");
	    		   Province province=new Province();
	    		   province.setProvinceName(pro[1]);
	    		   province.setProvinceCode(pro[0]);
	    		   control.saveProvince(province);
	    		   return true;
	    	   }
	       }
	    }
	    return false;
	}
	public boolean handleCityResponse(String response,ControlSQL control,int provinceID){
		if(TextUtils.isEmpty(response.toString())){
			citList=response.toString().split(",");
			for(String j:citList){
				String[] cit=j.split("\\|");
				City city=new City();
				city.setCityName(cit[1]);
				city.setCityCode(cit[0]);
				city.setProvince_id(provinceID);
				control.saveCity(city);
				return true;
			}
		}
		return false;
		
	}
	public boolean handCountryResponse(String response,ControlSQL control,int cityID){
		if(TextUtils.isEmpty(response.toString())){
			conList=response.toString().split(",");
			for(String l:conList){
				String[] con=l.split("\\|");
				Country country=new Country();
				country.setCountryeName(con[1]);
				country.setCountryCode(con[0]);
				country.setCity_id(cityID);
				control.saveCountry(country);
				return true;
			}
		}
		return false;
	}
	public void handleWeatherResponse(Context context,String response){
		
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
	public void savePreferences(Context context,String a,String b,String c,String d,String e,String f,Boolean g){
		SharedPreferences.Editor spf=PreferenceManager.getDefaultSharedPreferences(context).edit();
		spf.putString("cityName", a);
		spf.putString("weatherCode", b);
		spf.putString("temp1", c);
		spf.putString("temp2", d);
		spf.putString("weatherDescribe", e);
		spf.putString("ptime", f);
		spf.putBoolean("selectedCity", g);
		spf.commit();
	}
	
	
	
	
	
	
	
	
	
	
}
