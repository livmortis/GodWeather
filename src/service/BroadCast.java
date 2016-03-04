package service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCast extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		intent=new Intent(context,BackgroundUpdate.class);
		context.startService(intent);
	}

}
