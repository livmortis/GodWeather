package com.example.myweather.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.myweather.service.BackgroundUpdate;
import com.example.myweather.R;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.Uitlity;
import com.example.myweather.util.HttpUtil.HttpCallBackListener;

public class Weather extends Activity implements OnClickListener {
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	private TextView tv5;
	private TextView tv6;
	private LinearLayout ll;
	private Button bu3;
	private Button bu4;
	

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weathershow);
		tv1=(TextView)findViewById(R.id.title);
		tv2=(TextView)findViewById(R.id.punish_time);
		tv3=(TextView)findViewById(R.id.date);
		tv4=(TextView)findViewById(R.id.rainOrsun);
		tv5=(TextView)findViewById(R.id.temp1);
		tv6=(TextView)findViewById(R.id.temp2);
		ll=(LinearLayout)findViewById(R.id.linear);
		bu3=(Button)findViewById(R.id.change_city);
		bu4=(Button)findViewById(R.id.refresh);

		String countryCode=getIntent().getStringExtra("countryCode");
		if(!TextUtils.isEmpty(countryCode)){
			tv2.setText("同步中..");
			ll.setVisibility(View.INVISIBLE);
			tv1.setVisibility(View.INVISIBLE);
			queryWeatherCode(countryCode);
		}
		else{
			
			showWeather();
		}
		bu3.setOnClickListener(this);
		bu4.setOnClickListener(this);
		
	}
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.change_city:
			Intent intent=new Intent(this,ChoosArea.class);
			intent.putExtra("isfromweater", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh:
			tv2.setText("同步中..");
			SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode=sp.getString("weatherCode", "");
			if(!TextUtils.isEmpty(weatherCode)){
			    queryWeatherInfo(weatherCode);
			}
			break;
		default:
			break;
		}
	}
	//			Log.d("Weather", "收到countryCode");

	public void queryWeatherCode(String countryCode){
		String address="http://www.weather.com.cn/data/list3/city"+countryCode+".xml";
		handleWeatherInfo(address,"1");
		

		
	}
	public void queryWeatherInfo(String weatherCode){
		String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		Intent intent=new Intent(this,BackgroundUpdate.class);
		startService(intent);
		
		handleWeatherInfo(address,"2");
		//这里可以执行到
	}
	public void handleWeatherInfo(final String address,final String type){
		
			 HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
				@Override
				public void onFinish(final String response) {
					//Log.d("Weather", "收到countryCode");//这里可以执行到
					if("1".equals(type)){
						
						if(!TextUtils.isEmpty(response)){
					        String[] array=response.split("\\|");
					        if(array!=null&&array.length==2){
					            String weatherCode=array[1];
					            
					            queryWeatherInfo(weatherCode);
					            //Log.d("Weather", "收到countryCode");
					        }
						}
					}
					else if("2".equals(type)){
						//这里就不行了
						Uitlity.handleWeatherResponse(Weather.this, response);
						
						runOnUiThread(new Runnable() {
							public void run() {
								showWeather();
							}
						});
					}
					
				}
				@Override
				public void onError(Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							tv2.setText("同步失败");
						}
					});
				}
				
			});
		

	}
	private void showWeather(){
		SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(this);
		tv1.setText(sf.getString("cityName", ""));
		tv5.setText(sf.getString("temp1", ""));
		tv6.setText(sf.getString("temp2", ""));
		tv4.setText(sf.getString("weatherDescribe", ""));
		tv2.setText("今天"+sf.getString("ptime", "")+"发布");
		tv3.setText(sf.getString("date", ""));
		ll.setVisibility(View.VISIBLE);
		tv1.setVisibility(View.VISIBLE);
		Intent intent=new Intent(this,BackgroundUpdate.class);
		startService(intent);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
