package com.example.myweather.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.example.myweather.receiver.BroadCast;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.HttpUtil.HttpCallBackListener;
import com.example.myweather.util.Uitlity;

public class BackgroundUpdate extends Service{
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressLint("NewApi")
	public int onStartCommand(Intent intent,int flags,int startId){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				update();
			}
		}).start();
		AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
		int tim=8*60*60*1000;
		long time=SystemClock.elapsedRealtime()+tim;
		Intent i=new Intent(this,BroadCast.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, i, 0);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pi);
		return super.onStartCommand(intent, flags, startId);
	}
		
		
		

	
	private void update(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode=prefs.getString("weatherCode", "");
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
	    HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Uitlity.handleWeatherResponse(BackgroundUpdate.this, response);
				
				
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				e.printStackTrace();
			}
		});
		
	}


}
