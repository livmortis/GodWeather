package com.example.myweather.receiver;

import com.example.myweather.service.BackgroundUpdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Intent i=new Intent(context,BackgroundUpdate.class);
		context.startService(i);
	}

}
