package service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import util.HttpUtil;
import util.HttpUtil.HttpCallBackListener;
import util.Uitlity;

public class BackgroundUpdate extends Service{
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	public void startCommand(String address){
		
		HttpUtil hUtil=new HttpUtil();
		hUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				Uitlity ui=new Uitlity();
				ui.handleWeatherResponse(BackgroundUpdate.this, response);
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				
			}
		});
		AlarmManager am=(AlarmManager)getSystemService(ALARM_SERVICE);
		int tim=240*60*1000;
		long time=SystemClock.elapsedRealtime()+tim;
		Intent intent=new Intent(this,BroadCast.class);
		PendingIntent pi=PendingIntent.getBroadcast(this, 0, intent, 0);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, pi);
	}


}
