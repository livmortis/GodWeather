package com.example.myweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.example.myweather.activity.ChoosArea;

import android.content.Context;
import android.widget.Toast;

import java.net.HttpURLConnection;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallBackListener listener){
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection=null;
				try{
				

				    URL url=new URL(address);
				    connection=(HttpURLConnection)url.openConnection();
				    
				    connection.setRequestMethod("GET");
				    connection.setConnectTimeout(80000);
				    connection.setReadTimeout(80000);
				    
				   
				    InputStream in=connection.getInputStream();
				    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
				    StringBuilder response=new StringBuilder();
				    String line;
				    while((line=bufferedReader.readLine())!=null){
				    	response.append(line);
				    }
				    if(listener!=null){   //勿忘
				    listener.onFinish(response.toString());
				    }
				}
				catch(Exception e){
					if(listener!=null){  //勿忘
					listener.onError(e);
					}
				}
				finally{                    //勿忘
					if(connection!=null){   //勿忘
					connection.disconnect();
					}
				}
			}
		}).start();   //勿忘
	}
	public interface HttpCallBackListener{        //一个接口却与方法同级，而不是与类同级（新建一个类）
		void onFinish(String response);
		void onError(Exception e);
	}
}
