package com.example.myweather;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import model.City;
import model.ControlSQL;
import model.Country;
import model.Province;
import util.HttpUtil;
import util.HttpUtil.HttpCallBackListener;
import util.Uitlity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ChoosArea extends Activity{
	private TextView tv;
	private ListView lv;
	private int i;
	private List<String> areaList;
	//private String clickedArea;
	private Province clickedProvince;
	private City clickedCity;
	//private String clickedCountry;
	private ControlSQL control;
	
	private List<Province> provinceList;
	private List<City> cityList;
	private List<Country> countriList;

	private Boolean reason=false;
	
	ArrayAdapter<String> adapter;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Boolean is=getIntent().getBooleanExtra("isfromweather", false);
		SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
		Boolean selectedCity=sp.getBoolean("selectedCity", false);
		if(!is.equals(true) && selectedCity.equals(true)){
		    Intent intent=new Intent(ChoosArea.this,Weather.class);
		    startActivity(intent);
		}
		setContentView(R.layout.choos_lay);
		lv=(ListView)findViewById(R.id.area);
		tv=(TextView)findViewById(R.id.title);
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,areaList);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if(i==1){//if(clickedProvince.���ڱ�==Province){
					clickedProvince=provinceList.get(position);//clickedProvince=areaList.get(position); //�����"areaList"�ı�������String���ͣ���ʵ��provinc_name.��"provincList"������Province���ͣ����Ե���getId���õ�province_id��
					showCityListFromSQL();
				}
				else if(i==2){//else if (clickedCity.���ڱ�==City) {
					clickedCity=cityList.get(position);
					showCountryListFromSQL();
				}
				//showProvinceListFromSQL();//�����ڵ���¼�֮�⣡����
				else if(i==3){
					Intent intent =new Intent(ChoosArea.this,Weather.class);
					intent.putExtra("countryCode", countriList.get(position).getCountryCode());
					startActivity(intent);
					finish();
				}
			}
		});
		showProvinceListFromSQL();
	}
	public void showCityListFromSQL(){
		//WetherSQL wetherOpenHelper=new WetherSQL(this, null, null, 1);
		control=ControlSQL.getInstance(this);
		if(control.loadCity(clickedProvince.getProvinceId())!=null){   //ͨ��Province�Ķ��������getprovinceId()�����õ���provinc_id��
		    cityList=control.loadCity(clickedProvince.getProvinceId());//���ܸ�����areaList����Ϊ����String�ͼ��ϡ�
		    areaList.clear();
		    for(City cc:cityList){
		    	areaList.add(cc.getCityName());
		    }
			tv.setText(clickedProvince.getProvinceName());
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			i=2;
		}
		else {
			showListFromHttp(2,clickedProvince.getProvinceCode());
		}
	}
    public void showCountryListFromSQL(){
		control=ControlSQL.getInstance(this);
		if(control.loadCountry(clickedCity.getCityId())!=null){
			countriList=control.loadCountry(clickedCity.getCityId());
			for(Country co:countriList){
			    areaList.add(co.getCountryName());
			}
			tv.setText(clickedCity.getCityName());
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			i=3;

		}
		else{
			showListFromHttp(3,clickedCity.getCityCode());
		}
    }
    public void showProvinceListFromSQL(){
		control=ControlSQL.getInstance(this);
    	if(control.loadProvince()!=null){
    		provinceList=control.loadProvince();
    		for(Province pp:provinceList){
    			areaList.add(pp.getProvinceName());
    		}
			tv.setText("�й�");
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
    		i=1;
    	}
    	else{
    		showListFromHttp(1,null);
    	}
		
    }
    public void showListFromHttp(final int k,String code){
    	//new Thread(new Runnable() {  //sendHttpRequest()�����������֧���У�û��Ҫ�ٷ���֧�ߡ�
			//public void run() {
				//try{
			    	//URL url=new URL("www.area.com");   //sendHttpRequest()�����л����ַ��Ϊurl�����ﲻ����һ�٣�������ҳ�ַ������ɡ�
    	        String str;
    	        if(code==null){
    	            str="www.area.com.xml";
    	        }
    	        else{
    	        	str="www.area.com."+code+".xml";
    	        }
    	            
			    	HttpUtil hu=new HttpUtil();
			    	showProgressDialog();
			    	hu.sendHttpRequest(str, new HttpCallBackListener(){
			    		//showProgressDialog();  //��������UI���������������߳��С�
						@Override
						public void onFinish(String response) {
							// TODO Auto-generated method stub
							Uitlity ui=new Uitlity();
							if(k==1){
							    reason=ui.handleProviceResponse(response, control);
							}
							else if(k==2){
							    reason=ui.handleCityResponse(response, control, clickedProvince.getProvinceId());
							}
							else if(k==3){
							    reason=ui.handCountryResponse(response, control, clickedCity.getCityId());
							}
							
							
							runOnUiThread(new Runnable() {
								public void run() {
									if(reason==true){
										if(k==1){
										    showProvinceListFromSQL();
										}
										else if(k==2){
										    showCityListFromSQL();
										}
										else if(k==3){
										    showCountryListFromSQL();
										}
									}
									removeProgressDialog();
								}
							});
						}
						@Override
						public void onError(Exception e) {
							runOnUiThread(new Runnable() {
								public void run() {
									removeProgressDialog();
									Toast.makeText(getBaseContext(), "no internet connection", Toast.LENGTH_SHORT).show();
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
		ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.show();
		progressDialog.setCancelable(false);
		progressDialog.setMessage("waiting..");

	}
	public void removeProgressDialog(){
		ProgressDialog progressDialog=new ProgressDialog(this);
		progressDialog.dismiss();
	}
	public void onBackPressed(){
		if(i==3){
			showCityListFromSQL();
		}
		else if(i==2){
			showProvinceListFromSQL();
		}
		else{
			finish();
		}
	}
	
	
	
	
	
	
	
	
	
	
}
