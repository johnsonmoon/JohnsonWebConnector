package com.xuyihao.URLConnectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;


/**
 * Created by xuyihao on 2016/5/14.
 * @author johnson
 * @description 网络请求工具类
 * @attention
 */
public class HttpUtil {
	 /**
     * fields
     * */
	private Platform platform = Platform.LINUX;//平台的枚举,默认为linux
	private String PathSeparator;//路径分隔符，根据不同平台确定

    /**
     * constructor
     * */
	public HttpUtil(){
		
	}
	
	/**
	 * constructor2
	 * */
	public HttpUtil(Platform platform){
		
		this.platform = platform;
		//根据不同平台确定路径分隔符
		if(this.platform == Platform.WINDOWS){
			this.PathSeparator = "\\";
		}else if(this.platform == Platform.LINUX){
			this.PathSeparator = "/";
		}else{
			this.PathSeparator = "\\";//默认设置为windows格式
		}
	}
	
	/**
	 * @author johnson
	 * @method getTest
	 * @description 测试发送get请求的方法
	 * */
	public void getTest(String actionURL){
		try{
			URL url = new URL(actionURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			
			try{
	        	//获取URL的响应
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	        	String s = "";
	        	String temp = "";
	        	while((temp = reader.readLine()) != null){
	        		s += ("\n" + temp);
	        	}
	            System.out.println(s);
	          
	            reader.close();
	            	            
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("No response get!!!");
	        }
			
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Request failed!");
		}
	}
	
	/**
	 * @author johnson
	 * @method 
	 * @description 执行发送get请求的方法	
	 * */
	public void executeGet(String actionURL, HashMap<String, String> parameters){
		try{
			URL url = new URL(actionURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			Set<String> mapKey = parameters.keySet();
			for(String key : mapKey){
				connection.setRequestProperty(key, parameters.get(key));
			}
			
			try{
	        	//获取URL的响应
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	        	String s = "";
	        	String temp = "";
	        	while((temp = reader.readLine()) != null){
	        		s += ("\n" + temp);
	        	}
	            System.out.println(s);
	          
	            reader.close();
	            	            
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("No response get!!!");
	        }
			
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Request failed!");
		}
	}
	
	/**
	 * @method singleFileUpload 模拟提交表单数据上传单个文件的方法
	 * @author johnson
	 * @description 用来向指定url上传文件的方法
	 * @param uploadFile 上传文件的路径字符串
	 * @param actionURL 上传文件的URL
	 * @param fileType 文件类型(枚举类型)
	 * @attention 上传文件name为file
	 * */
	public void singleFileUpload(String uploadFile, String actionURL, FileType fileType){
		String end = "\r\n";
		String twoHyphens = "--";
        String boundary = "---------------------------7e0dd540448";
        try{
        	URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置请求参数
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //获取请求内容输出流                        
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            String fileName = uploadFile.substring(uploadFile.lastIndexOf(this.PathSeparator) + 1);
        	//开始写表单格式内容
            ds.writeBytes(twoHyphens + boundary + end);
        	ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"");
        	//防止中文乱码
        	ds.write(fileName.getBytes());
        	ds.writeBytes("\"" + end);
        	ds.writeBytes(fileType.getValue() + end);
        	ds.writeBytes(end);
        	//根据路径读取文件
        	FileInputStream fis = new FileInputStream(uploadFile);
        	byte[] buffer = new byte[1024];
        	int length = -1;
        	while((length = fis.read(buffer)) != -1){
        		ds.write(buffer, 0, length);
        	}
        	ds.writeBytes(end);
        	fis.close();
        	
        	ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
        	ds.writeBytes(end);
        	ds.flush();
	        
	        try{
	        	//获取URL的响应
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	        	String s = "";
	        	String temp = "";
	        	while((temp = reader.readLine()) != null){
	        		s += ("\n" + temp);
	        	}
	            System.out.println(s);
	          
	            reader.close();
	            	            
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("No response get!!!");
	        }
	        
	        
	        ds.close();

	        
        }catch(IOException e){
        	e.printStackTrace();
        	System.out.println("Request failed!");
        }
	}
		
	/**
     * @method multipleFileUpload 模拟提交表单数据上传多个文件的方法
     * @description	用来向指定url上传文件的方法,可以上传多个文件
     * @param uploadFiles 上传文件的路径字符串数组,表示多个文件
     * @param actionURL 上传文件的URL地址包括URL
     * @param fileType 文件类型(枚举类型)
     * @attention 上传文件name为file0,file1,file2,以此类推
     * */
    public void multipleFileUpload(String[] uploadFiles, String actionURL, FileType fileType){
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "---------------------------7e0dd540448";
        try{
            URL url = new URL(actionURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            //发送post请求需要下面两行
            connection.setDoInput(true);
            connection.setDoOutput(true);
            //设置请求参数
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            //获取请求内容输出流  
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            //添加post数据
            for(int i=0; i < uploadFiles.length; i++){
            	String uploadFile = uploadFiles[i];
            	String fileName = uploadFile.substring(uploadFile.lastIndexOf(this.PathSeparator) + 1);
            	ds.writeBytes(twoHyphens + boundary + end);
            	ds.writeBytes("Content-Disposition: form-data; " + "name=\"file"+i+"\"; " + "filename=\"");
            	//防止中文乱码
            	ds.write(fileName.getBytes());
            	ds.writeBytes("\"" + end);            	
            	ds.writeBytes(fileType.getValue() + end);
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
        	ds.writeBytes(end);
            ds.flush();          
            
            try{
            	//获取URL的响应
            	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            	String s = "";
            	String temp = "";
            	while((temp = reader.readLine()) != null){
            		s += temp;
            	}
                System.out.println(s);
              
                reader.close();
                
            }catch(IOException e){
            	e.printStackTrace();
            	System.out.println("No response get!!!");
            }
            
            ds.close();

            
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Request failed!");
        }  
        
    }
    
    
}
