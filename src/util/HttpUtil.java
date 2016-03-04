package util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.HttpURLConnection;

public class HttpUtil {
	private HttpURLConnection connection;
	public String response;
	public void sendHttpRequest(final String address,final HttpCallBackListener listener){
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
				    URL url=new URL(address);
				    connection=(HttpURLConnection)url.openConnection();
				    connection.setReadTimeout(8000);
				    connection.setRequestMethod("GET");
				    connection.setConnectTimeout(8000);
				    InputStream in=connection.getInputStream();
				    BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
				    StringBuilder builder=new StringBuilder();
				    String line=null;
				    if((line=bufferedReader.readLine())!=null){
				    	builder.append(line);
				    }
				    response=builder.toString();
				    if(listener!=null){   //����
				    listener.onFinish(response);
				    }
				}
				catch(Exception e){
					if(listener!=null){  //����
					listener.onError(e);
					}
				}
				finally{                    //����
					if(connection!=null){   //����
					connection.disconnect();
					}
				}
			}
				
			
		}).start();   //����
	}
	public interface HttpCallBackListener{
		public void onFinish(String response);
		public void onError(Exception e);
	}
}
