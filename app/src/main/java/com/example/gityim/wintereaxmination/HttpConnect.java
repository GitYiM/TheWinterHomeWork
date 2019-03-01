package com.example.gityim.wintereaxmination;


import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnect {
    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try{
                    URL url=new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
//                    connection.setDoInput(true);
//                    connection.setDoOutput(true);
                    InputStream in =connection.getInputStream(); //获取字节输入流
                    BufferedReader reader =new BufferedReader(new InputStreamReader(in)); //BufferReader 中定义方法只能接收字符流实例
                    StringBuilder response =new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    in.close();
                    if(listener!=null){
                        listener.onFinish(response.toString());
                    }
                }catch(Exception e){
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

}

