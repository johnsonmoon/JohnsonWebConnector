package com.xuyihao.url.connectors;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

/**
 * created by xuyihao on 2016/5/19
 * @author johnson
 * @description 网络资源(文件)下载工具类
 * @attention 发送GET POST请求，接收网络文件
 * @attention 此工具类不支持多线程下载，IO阻塞线程
 * @attention 添加会话(session)支持
 * */
public class DownUtil {
	/**
	 * fields
	 * @author johnson
	 * */
	private String sessionID = "";
	
	/**
	 * constructor
	 * @author johnson
	 * */
	public DownUtil(){
	}
	
	/**
	 * constructor3
	 * @author johnson
	 * @description 获取相应URL服务器的会话sessionID
	 * @param actionURL 需要获取会话sessionID的URL
	 * @attention 不一定会获取到URL的session
	 * */
	public DownUtil(String actionURL){
		this.getSessionIDFromCookie(actionURL);
	}
	
	/**
	 * @author johnson
	 * @method getSessionIDFromCookie
	 * @description 执行从cookie获取会话sessionID的方法，用于保持与服务器的会话
	 * @param actionURL 需要获取会话sessionID的URL
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
	 * @method downloadByGet
	 * @description 执行Get请求下载文件的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求URL后跟着的具体参数,以HashMap<String, String>形式传入key=value值
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return byte[] 返回一个储存文件内容的字节数组
	 * */
	public byte[] downloadByGet(String actionURL, HashMap<String, String> parameters){
		byte[] data = new byte[0];
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
	        	InputStream in = connection.getInputStream();
	        	byte[] b = new byte[1];
	        	while(in.read(b) != -1){
	        		data = this.connectTwoByteArrays(data, b);
	        	}
	        	in.close();
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("No response get!!!");
	        }
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Request failed!");
		}
		return data;
	}
	
	/**
	 * @author johnson
	 * @method downloadByGet
	 * @description 执行Get请求下载文件的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求URL后跟着的具体参数,以HashMap<String, String>形式传入key=value值
	 * @param savePathName 文件在磁盘中的储存路径&文件名
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 最后文件会以savePathName的路径名形式存储到磁盘中
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByGet(String savePathName, String actionURL, HashMap<String, String> parameters){
		boolean flag = false;
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
	        	InputStream in = connection.getInputStream();
	        	File file = new File(savePathName);
	        	FileOutputStream out = new FileOutputStream(file);
	        	byte[] b = new byte[1024];
	        	int length = 0;
	        	while((length = in.read(b)) != -1){
	        		out.write(b, 0, length);
	        	}
	        	in.close();
	        	out.close();
	        	flag = true;
	        }catch(IOException e){
	        	e.printStackTrace();
	        	flag = false;
	        	System.out.println("No response get!!!");
	        }
		}catch(IOException e){
			e.printStackTrace();
			flag = false;
			System.out.println("Request failed!");
		}
		return flag;
	}
	
	/**
	 * @author johnson
	 * @method downloadByPost
	 * @description 执行发送post请求的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求数据段中的参数,以HashMap<String, String>形式传入key=value值
	 * @attention 请求内容在HTTP报文内容中
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return byte[] 返回一个储存文件内容的字节数组
	 * */
	public byte[] downloadByPost(String actionURL, HashMap<String, String> parameters){
		byte[] data = new byte[0];
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
			//防止中文乱码,使用String.getBytes()来获取字节数组
			ds.write(requestContent.getBytes());
			ds.flush();
			try{
	        	//获取URL的响应
				//获取URL的响应
	        	InputStream in = connection.getInputStream();
	        	byte[] b = new byte[1];
	        	while(in.read(b) != -1){
	        		data = this.connectTwoByteArrays(data, b);
	        	}
	        	in.close();
	        }catch(IOException e){
	        	e.printStackTrace();
	        	System.out.println("No response get!!!");
	        }
	        ds.close();
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("Request failed!");
		}
		return data;
	}
	
	/**
	 * @author johnson
	 * @method downloadByPost
	 * @description 执行发送post请求的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求数据段中的参数,以HashMap<String, String>形式传入key=value值
	 * @param savePathName 文件在磁盘中的储存路径&文件名
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 最后文件会以savePathName的路径名形式存储到磁盘中
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByPost(String savePathName, String actionURL, HashMap<String, String> parameters){
		boolean flag = false;
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
			//防止中文乱码,使用String.getBytes()来获取字节数组
			ds.write(requestContent.getBytes());
			ds.flush();
			try{
	        	//获取URL的响应
				InputStream in = connection.getInputStream();
	        	File file = new File(savePathName);
	        	FileOutputStream out = new FileOutputStream(file);
	        	byte[] b = new byte[1024];
	        	int length = 0;
	        	while((length = in.read(b)) != -1){
	        		out.write(b, 0, length);
	        	}
	        	in.close();
	        	out.close();
	        	flag = true;
	        }catch(IOException e){
	        	e.printStackTrace();
	        	flag = false;
	        	System.out.println("No response get!!!");
	        }
	        ds.close();
		}catch(IOException e){
			e.printStackTrace();
			flag = false;
			System.out.println("Request failed!");
		}
		return flag;
	}
	
	/**
	 * @author johnson
	 * @method connectTwoByteArrays
	 * @description 连接两个byte数组的方法
	 * @param front 前面的数组
	 * @param behind 后面的数组
	 * @return byte[] 返回一个新数组
	 * */
	public byte[] connectTwoByteArrays(byte[] front, byte[] behind){
		byte[] total = new byte[front.length + behind.length];
		System.arraycopy(front, 0, total, 0, front.length);
		System.arraycopy(behind, 0, total, front.length, behind.length);
		return total;
	}
}
