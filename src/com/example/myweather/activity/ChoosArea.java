package com.example.myweather.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myweather.R;
import com.example.myweather.model.City;
import com.example.myweather.model.ControlSQL;
import com.example.myweather.model.Country;
import com.example.myweather.model.Province;
import com.example.myweather.util.HttpUtil;
import com.example.myweather.util.HttpUtil.HttpCallBackListener;
import com.example.myweather.util.Uitlity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ChoosArea extends Activity{
	private TextView tv;
	private ListView lv;
	
	private static final int LevelPr=0;
	private static final int LevelCi=1;
	private static final int LevelCo=2;
	private int level;
	
	private List<String> areaList=new ArrayList<String>();   //!!!!!!!!!!!!!必须初始化（等号右边）
	//private String clickedArea;
	private Province clickedProvince;
	private City clickedCity;
	//private String clickedCountry;
	private ControlSQL control;
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<Country> countriList;

	
	
	private ArrayAdapter<String> adapter;
	
	private ProgressDialog progressDialog;
	
	private boolean isfromweather;
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		isfromweather=getIntent().getBooleanExtra("isfromweater", false);
		
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
		
		//Boolean is=getIntent().getBooleanExtra("isfromweather", false);        //START
		//Boolean selectedCity=sp.getBoolean("selectedCity", false);
		
		if(sp.getBoolean("selectedCity", false)&&!isfromweather){
		    Intent intent=new Intent(this,Weather.class);
		    startActivity(intent);
		    finish();
		    return;
		}                                                                       //END
		
		
		
		setContentView(R.layout.choos_lay);
		lv=(ListView)findViewById(R.id.area);
		tv=(TextView)findViewById(R.id.title);
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,areaList);
		lv.setAdapter(adapter);
		control=ControlSQL.getInstance(this);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				
				if(level==LevelPr){//if(clickedProvince.所在表==Province){
					clickedProvince=provinceList.get(index);//clickedProvince=areaList.get(position); //如果是"areaList"的被点击项，是String类型，其实是provinc_name.而"provincList"的则是Province类型，可以调用getId而得到province_id。
					
					showCityListFromSQL();
				}
				else if(level==LevelCi){//else if (clickedCity.所在表==City) {
					clickedCity=cityList.get(index);
					showCountryListFromSQL();
				}
				//showProvinceListFromSQL();//必须在点击事件之外！！！
				else if(level==LevelCo){
					Intent intent =new Intent(ChoosArea.this,Weather.class);
					intent.putExtra("countryCode", countriList.get(index).getCountryCode());
					startActivity(intent);
					finish();
				}
			}
		});
	     showProvinceListFromSQL();
	}
	private void showCityListFromSQL(){
		//WetherSQL wetherOpenHelper=new WetherSQL(this, null, null, 1);
		cityList=control.loadCity(clickedProvince.getProvinceId());//不能复制与areaList，因为其是String型集合。
		if(cityList.size()>0){   //通过Province的对象调用其getprovinceId()方法得到了provinc_id。
		    
		    areaList.clear();
		    for(City cc:cityList){
		    	areaList.add(cc.getCityName());
		    }
			tv.setText(clickedProvince.getProvinceName());
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			level=LevelCi;
		}
		else {
			
			showListFromHttp("city",clickedProvince.getProvinceCode());
		}
	}
	private void showCountryListFromSQL(){
		countriList=control.loadCountry(clickedCity.getCityId());
		if(countriList.size()>0){
			areaList.clear();
			for(Country co:countriList){
			    areaList.add(co.getCountryName());
			}
			tv.setText(clickedCity.getCityName());
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			level=LevelCo;

		}
		else{
			showListFromHttp("country",clickedCity.getCityCode());
		}
    }
	private void showProvinceListFromSQL(){
		provinceList=control.loadProvince();
    	if(provinceList.size()>0){
    		areaList.clear();
    		for(Province pp:provinceList){
    			areaList.add(pp.getProvinceName());
    		}
			tv.setText("中国");
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			level=LevelPr;
    	}
    	else{
    		showListFromHttp("province",null);
    	}
		
    }
    public void showListFromHttp(final String type,final String code){
    	//new Thread(new Runnable() {  //sendHttpRequest()方法本身就在支线中，没必要再放入支线。
			//public void run() {
				//try{
			    	//URL url=new URL("www.area.com");   //sendHttpRequest()方法中会把网址变为url，这里不需多此一举，传入网页字符串即可。
    	        String str;
    	        if(!TextUtils.isEmpty(code)){
    	        	str="http://www.weather.com.cn/data/list3/city"+code+".xml";//市和县

    	        	
    	            
    	        }
    	        else{
    	        	str="http://www.weather.com.cn/data/list3/city.xml"; //省
    	        }
    	       showProgressDialog();
			    	
			    	 HttpUtil.sendHttpRequest(str, new HttpCallBackListener(){                //！！！HttpUtil的sendHttpRequest：从服务器请求并下载数据
			    		//showProgressDialog();  //进度条是UI操作，必须在主线程中。
						@Override
						
						public void onFinish(String response) {
							// TODO Auto-generated method stub
							boolean reason=false;
							

							if("province".equals(type)){
								
							    reason=Uitlity.handleProviceResponse(response, control); //！！！Uitlity的handleProviceResponse：下载下来的数据解析并入库
							}
							else if("city".equals(type)){
								
							    reason=Uitlity.handleCityResponse(response, control, clickedProvince.getProvinceId());
							    
							}
							else if("country".equals(type)){
							    reason=Uitlity.handCountryResponse(response, control, clickedCity.getCityId());
							}
							
							if(reason){
							runOnUiThread(new Runnable() {
								public void run() {
									removeProgressDialog();
										if("province".equals(type)){
										    showProvinceListFromSQL();
										}
										else if("city".equals(type)){
											
										    showCityListFromSQL();
										    
										}
										else if("country".equals(type)){
										    showCountryListFromSQL();
										}
									}
									
								
						    	});
							}
						
							
						}
						@Override
						public void onError(Exception e) {
							runOnUiThread(new Runnable() {
								public void run() {
									removeProgressDialog();
									Toast.makeText(ChoosArea.this, "no internet connection", Toast.LENGTH_SHORT).show();
								}
							});
							
						}
			    		
			    	});

					
				//}
				//catch(Exception e){
					
				//}
				
			//}
		//});
    }
	public void showProgressDialog(){
		if(progressDialog==null){
		    progressDialog=new ProgressDialog(this);
		   
		    
		    progressDialog.setMessage("waiting..");
		    progressDialog.setCanceledOnTouchOutside(false);

		}
		 progressDialog.show();

	}
	public void removeProgressDialog(){
		if(progressDialog!=null){
		    progressDialog.dismiss();
		}
	}
	public void onBackPressed(){
		if(level==LevelCo){
			showCityListFromSQL();
		}
		else if(level==LevelCi){
			showProvinceListFromSQL();
		}
		else{
			if(isfromweather){
				Intent intent=new Intent(this,Weather.class);
				startActivity(intent);
			}
			finish();
		}
	}
	
	
	
}
