package com.xuyihao.url.connectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import com.xuyihao.url.enums.MIME_FileType;
import com.xuyihao.url.enums.Platform;

/**
 * Created by xuyihao on 2016/5/14.
 * @author johnson
 * @description 网络请求工具类
 * @function 发送GET POST请求, 接收字符串返回值
 * @attention 添加会话(session)支持
 */
public class HttpUtil {
	 /**
     * fields
     * */
	private Platform platform = Platform.LINUX;//平台的枚举,默认为linux
	private String PathSeparator;//路径分隔符，根据不同平台确定
	private final String end = "\r\n";
	private final String twoHyphens = "--";
	private final String boundary = "---------------------------7e0dd540448";
	private String sessionID = "";
	
    /**
     * constructor
     * */
	public HttpUtil(){
		
	}
	
	/**
	 * constructor2
	 * @param platform 区分系统的枚举
	 * @description 通过此构造器可以区分使用此工具的系统平台
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
	 * @constructor3
	 * @author johnson
	 * @description 通过此构造器可以区分使用此工具的平台，并且获取相应URL服务器的会话sessionID
	 * @param platform 区分系统的枚举
	 * @param actionURL 需要获取会话sessionID的URL
	 * @attention 不一定会获取到URL的session
	 * */
	public HttpUtil(Platform platform, String actionURL){
		this.platform = platform;
		//根据不同平台确定路径分隔符
		if(this.platform == Platform.WINDOWS){
			this.PathSeparator = "\\";
		}else if(this.platform == Platform.LINUX){
			this.PathSeparator = "/";
		}else{
			this.PathSeparator = "\\";//默认设置为windows格式
		}
		this.getSessionIDFromCookie(actionURL);
	}
	
	/**
	 * @author johnson
	 * @method getSessionIDFromCookie
	 * @description 执行从cookie获取会话sessionID的方法，用于保持与服务器的会话
	 * @param 
	 * @param 
	 * @attention 执行次方法后执行其他请求方法将会提交cookie，保持会话
	 * @return true if successfully
	 * */
	public boolean getSessionIDFromCookie(String actionURL){
		boolean flag = false;
		try {
			URL url = new URL(actionURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			String cookieValue = connection.getHeaderField("set-cookie");
			if(cookieValue != null){
				this.sessionID = cookieValue.substring(0, cookieValue.indexOf(";"));
				flag = true;
			}else{
				flag = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	/**
	 * @author johnson
	 * @method getSessionID
	 * @description 获取此工具类的sessionID
	 * @return String
	 * */
	public String getSessionID(){
		return this.sessionID;
	}
	
	/**
	 * @author johnson
	 * @method invalidateSessionID
	 * @description 使工具类中的sessionID无效，即删除会话信息
	 * */
	public void invalidateSessionID(){
		this.sessionID = "";
	}
	
	/**
	 * @author johnson
	 * @method executePost
	 * @description 执行发送post请求的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求数据段中的参数,以HashMap<String, String>形式传入key=value值
	 * @attention 
	 * @return String("" if no response get)
	 * */
	public String executePost(String actionURL, HashMap<String, String> parameters){
		String response = "";
		try{
			URL url = new URL(actionURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Charset", "UTF-8");;
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
			//设置请求数据内容
			String requestContent = "";
			Set<String> keys = parameters.keySet();
			for(String key : keys){
				requestContent = requestContent + key + "=" + parameters.get(key) + "&";
			}
			requestContent = requestContent.substring(0, requestContent.lastIndexOf("&"));
			DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
			ds.writeBytes(requestContent);
			ds.flush();
			try{
	        	//获取URL的响应
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	        	String s = "";
	        	String temp = "";
	        	while((temp = reader.readLine()) != null){
	        		s += temp;
	        	}
	            response = s;
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
		return response;
	}
	
	/**
	 * @author johnson
	 * @method executeGet
	 * @description 执行发送get请求的方法	
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求URL后跟着的具体参数,以HashMap<String, String>形式传入key=value值
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa)
	 * @return String("" if no response get)
	 * */
	public String executeGet(String actionURL, HashMap<String, String> parameters){
		String response = "";
		try{
			String trueRequestURL = actionURL;
			trueRequestURL += "?";
			Set<String> keys = parameters.keySet();
			for(String key : keys){
				trueRequestURL = trueRequestURL + key + "=" + parameters.get(key) + "&";
			}
			trueRequestURL = trueRequestURL.substring(0, trueRequestURL.lastIndexOf("&"));
			URL url = new URL(trueRequestURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			//如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
			try{
	        	//获取URL的响应
	        	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
	        	String s = "";
	        	String temp = "";
	        	while((temp = reader.readLine()) != null){
	        		s += temp;
	        	}
	            response = s;
	            reader.close();
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("No response get!!!");
	        }
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Request failed!");
		}
		return response;
	}
	
	/**
	 * @method singleFileUpload 模拟提交表单数据上传单个文件的方法
	 * @author johnson
	 * @description 用来向指定url上传文件的方法
	 * @param uploadFile 上传文件的路径字符串
	 * @param actionURL 上传文件的URL
	 * @param fileType 文件类型(枚举类型)
	 * @attention 上传文件name为file(服务器解析)
	 * @return String("" if no response get)
	 * */
	public String singleFileUpload(String actionURL, String uploadFile, MIME_FileType fileType){
		String response = "";
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
          //如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
            //获取请求内容输出流                        
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            String fileName = uploadFile.substring(uploadFile.lastIndexOf(this.PathSeparator) + 1);
        	//开始写表单格式内容
            ds.writeBytes(twoHyphens + boundary + end);
        	ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"");
        	//防止中文乱码
        	ds.write(fileName.getBytes());
        	ds.writeBytes("\"" + end);
        	ds.writeBytes("Content-Type: " + fileType.getValue() + end);
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
	        		s += temp;
	        	}
	            response = s;
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
        return response;
	}
		
	/**
     * @method multipleFileUpload 模拟提交表单数据上传多个文件的方法
     * @description	用来向指定url上传文件的方法,可以上传多个文件
     * @param uploadFiles 上传文件的路径字符串数组,表示多个文件
     * @param actionURL 上传文件的URL地址包括URL
     * @param fileType 文件类型(枚举类型)
     * @attention 上传文件name为file0,file1,file2,以此类推(服务器解析)
     * @return String("" if no response get)
     * */
    public String multipleFileUpload(String actionURL, String[] uploadFiles, MIME_FileType fileType){
        String response = "";
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
          //如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
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
            	ds.writeBytes("Content-Type: " + fileType.getValue() + end);
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
                response = s;
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
        return response;
    }
    
    /**
     * @author johnson
     * @method singleFileUploadWithParameters
     * @description 集上传单个文件与传递参数于一体的方法
     * @param actionURL 上传文件的URL地址包括URL
     * @param fileType 文件类型(枚举类型)
     * @param uploadFile 上传文件的路径字符串
     * @param parameters 跟文件一起传输的参数(HashMap)
     * @return String("" if no response get)
     * @attention 上传文件name为file(服务器解析)
     * */
    public String singleFileUploadWithParameters(String actionURL, String uploadFile, MIME_FileType fileType, HashMap<String, String> parameters){
    	String response = "";
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
            //如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
            //获取请求内容输出流                        
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
            String fileName = uploadFile.substring(uploadFile.lastIndexOf(this.PathSeparator) + 1);
        	//开始写表单格式内容
            //写参数
            Set<String> keys = parameters.keySet();
            for(String key : keys){
            	ds.writeBytes(twoHyphens + boundary + end);
            	ds.writeBytes("Content-Disposition: form-data; name=\"");
            	ds.write(key.getBytes());
            	ds.writeBytes("\"" + end);
            	ds.writeBytes(end);
            	ds.write(parameters.get(key).getBytes());
            	ds.writeBytes(end);
            }
            //写文件
            ds.writeBytes(twoHyphens + boundary + end);
        	ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\"; " + "filename=\"");
        	//防止中文乱码
        	ds.write(fileName.getBytes());
        	ds.writeBytes("\"" + end);
        	ds.writeBytes("Content-Type: " + fileType.getValue() + end);
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
	        		s += temp;
	        	}
	            response = s;
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
    	return response;
    }
    
    /**
     * @author johnson
     * @method singleFileUploadWithParameters
     * @description 集上传多个文件与传递参数于一体的方法
     * @param actionURL 上传文件的URL地址包括URL
     * @param fileType 文件类型(枚举类型)
     * @param uploadFiles 上传文件的路径字符串数组,表示多个文件
     * @param parameters 跟文件一起传输的参数(HashMap)
     * @return String("" if no response get)
     * @attention 上传文件name为file0,file1,file2,以此类推(服务器解析)
     * */
    public String multipleFileUploadWithParameters(String actionURL, String[] uploadFiles, MIME_FileType fileType, HashMap<String, String> parameters){
    	String response = "";
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
            //如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
            //获取请求内容输出流                        
            DataOutputStream ds = new DataOutputStream(connection.getOutputStream());
        	//开始写表单格式内容
            //写参数
            Set<String> keys = parameters.keySet();
            for(String key : keys){
            	ds.writeBytes(twoHyphens + boundary + end);
            	ds.writeBytes("Content-Disposition: form-data; name=\"");
            	ds.write(key.getBytes());
            	ds.writeBytes("\"" + end);
            	ds.writeBytes(end);
            	ds.write(parameters.get(key).getBytes());
            	ds.writeBytes(end);
            }
            //写文件
            for(int i=0; i < uploadFiles.length; i++){
            	String uploadFile = uploadFiles[i];
            	String fileName = uploadFile.substring(uploadFile.lastIndexOf(this.PathSeparator) + 1);
            	ds.writeBytes(twoHyphens + boundary + end);
            	ds.writeBytes("Content-Disposition: form-data; " + "name=\"file"+i+"\"; " + "filename=\"");
            	//防止中文乱码
            	ds.write(fileName.getBytes());
            	ds.writeBytes("\"" + end);            	
            	ds.writeBytes("Content-Type: " + fileType.getValue() + end);
            	ds.writeBytes(end);
            	//读取磁盘文件
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
	           response = s;
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
    	return response;
    }
}