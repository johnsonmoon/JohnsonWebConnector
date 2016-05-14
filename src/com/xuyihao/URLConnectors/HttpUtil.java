package com.xuyihao.URLConnectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by xuyihao on 2016/5/14.
 * @author johnson
 * @description 网络请求工具类
 * @attention
 */
public class HttpUtil {
	 /**
     * fileds
     * */

    /**
     * constructor
     * */
	public HttpUtil(){
		
	}

    /**
     * @method fileUpload 模拟提交表单数据上传文件的方法
     * @description	用来向指定url上传文件的方法,可以上传多个文件
     * @param uploadFiles 上传文件的路径字符串数组,表示多个文件
     * @param actionURL 上传文件的URL地址包括URL请求头
     * */
    public void fileUpload(String[] uploadFiles, String actionURL){
        String end = "\n";
        String twoHyphens = "--";
        String boundary = "-------------------HKUGADG524564adgyuyJohnson56484";
        try{
            URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要你下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置请求参数
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            //添加post数据
            for(int i=0; i < uploadFiles.length; i++){
            	String uploadFile = uploadFiles[i];
            	String fileName = uploadFile.substring(uploadFile.lastIndexOf("\\") + 1);
            	ds.writeBytes(twoHyphens + boundary + end);
            	ds.writeBytes("Content-Disposition: form-data; " + "name=\"file"+i+"\"; " + "filename=\"" + fileName + "\"" + end);
            	ds.writeBytes("Content-Type: text/plain" + end);
            	ds.writeBytes(end);
            	
            	FileInputStream fis = new FileInputStream(uploadFile);
            	byte[] buffer = new byte[1024];
            	int length = -1;
            	while((length = fis.read(buffer)) != -1){
            		ds.write(buffer, 0, length);
            	}
            	ds.writeBytes(end);
            	fis.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            ds.flush();
            
            ds.close();
            
            try{
            	//获取URL的响应
            	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            	String s = "";
            	while((s = reader.readLine()) != null){
            		s += ("\n" + s);
            	}
                System.out.println(s);
              
                reader.close();
                
            }catch(IOException e){
            	e.printStackTrace();
            }
            
        }catch (IOException e){
            e.printStackTrace();
        }  
        
    }
    
    
}
