package com.example.myweather;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import service.BackgroundUpdate;
import service.BroadCast;
import util.HttpUtil;
import util.Uitlity;
import util.HttpUtil.HttpCallBackListener;

public class Weather extends Activity implements OnClickListener {
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private TextView tv6;
	private LinearLayout ll;
	private Button bu1;
	private Button bu2;
	private Button bu3;
	private Button bu4;
	private String address1;
	private String address2;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weathershow);
		tv1=(TextView)findViewById(R.id.title);
		tv2=(TextView)findViewById(R.id.punish_date);
		tv3=(TextView)findViewById(R.id.time);
		tv4=(TextView)findViewById(R.id.describe);
		tv5=(TextView)findViewById(R.id.temp1);
		tv6=(TextView)findViewById(R.id.temp2);
		ll=(LinearLayout)findViewById(R.id.linear);
		bu1=(Button)findViewById(R.id.back);
		bu2=(Button)findViewById(R.id.choose);
		bu3=(Button)findViewById(R.id.change_city);
		bu4=(Button)findViewById(R.id.refresh);

		String countryCode=getIntent().getStringExtra("countryCode");
		if(countryCode==null){
			showWeather();
		}
		else{
			queryWeatherCode(countryCode);
		}
		bu1.setOnClickListener(this);
		bu2.setOnClickListener(this);
		bu3.setOnClickListener(this);
		bu4.setOnClickListener(this);
		
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.change_city:
			Intent intent=new Intent(Weather.this,ChoosArea.class);
			intent.putExtra("isfromweater", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh:
			SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(Weather.this);
			String weatherCode=sp.getString("weatherCode", "");
			queryWeatherInfo(weatherCode);
			break;
		default:
			break;
		}
	}
	public void queryWeatherCode(String countryCode){
		String address1="www.area.com."+countryCode+".xml";
		handleWeatherInfo(address1);
		
	}
	public void queryWeatherInfo(String weatherCode){
		String address2="www.weather.com."+weatherCode+".html";
		Intent intent=new Intent(this,BackgroundUpdate.class);
		startService(intent);
	}
	public void handleWeatherInfo(final String address){
		
			HttpUtil hu=new HttpUtil();
			hu.sendHttpRequest(address, new HttpCallBackListener() {
				@Override
				public void onFinish(String response) {
					if(address.equals(address1)){
					    String[] array=response.split("\\|");
					    String weatherCode=array[1];
					    queryWeatherInfo(weatherCode);
					}
					if(address.equals(address1)){
						handleWeatherInfo(response);
						runOnUiThread(new Runnable() {
							public void run() {
								showWeather();
							}
						});
					}
					
				}
				@Override
				public void onError(Exception e) {
					e.printStackTrace();
				}
			});
		

	}
	public void showWeather(){
		SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(this);
		tv1.setText(sf.getString("cityName", ""));
		tv5.setText(sf.getString("temp1", ""));
		tv6.setText(sf.getString("temp2", ""));
		tv4.setText(sf.getString("weatherDescribe", ""));
		tv3.setText(sf.getString("ptime", ""));
		tv2.setText("发布时间："+new Date());
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
