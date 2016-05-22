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
 * @attention 由于下载时候的IO阻碍主线程,所以需要使用 getCompleteRate printCompleteRate 等方法时候需要先调用这几个方法,再开启下载方法
 * @attention 如果需要新建线程并使用 getCompleteRate 方法查看进度, 也需要在调用下载的方法之前创建并start()
 * @attention 添加会话(session)支持,在一些需要保持会话状态下载文件的情况下,通过HttpUtil获取的sessionID进行sessionID的初始化
 * @attention 添加查看进度支持,需要调用 getCompleteRate 方法
 * */
public class DownUtil {
	/**
	 * fields
	 * @author johnson
	 * */
	private String sessionID = "";
	private long fileTotalLength = 0;
	private long fileReceiveLength = 0;
	private boolean ableToCaculate = false;
	private boolean downloadComplete = false;
	
	/**
	 * constructor
	 * @author johnson
	 * */
	public DownUtil(){
	}
	
	/**
	 * constructor2
	 * @param httpUtil 已经获取sessionID的HttpUtil工具类,用来初始化本类的sessionID
	 * */
	public DownUtil(HttpUtil httpUtil){
		this.sessionID = httpUtil.getSessionID();
	}
	
	/**
	 * @author johnson
	 * @method setSessionID
	 * @param httpUtil 已经获取sessionID的HttpUtil工具类
	 * @description 通过本包的HttoUtil工具类已经获取的sessionID来对本工具sessionID进行初始化的方法
	 * @attention 只能通过传入HttpUtil工具类来进行初始化,避免直接对sessionID字串进行赋值
	 * @attention 适用于一些只能保持会话状态才能下载文件的情况
	 * */
	public void setSessionID(HttpUtil httpUtil){
		this.sessionID = httpUtil.getSessionID();
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
	 * @attention 直接通过参数actionURL发送请求,用户也可以通过自己设置actionURL后的参数发送请求
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return byte[] 返回一个储存文件内容的字节数组
	 * */
	public byte[] downloadByGet(String actionURL){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
		byte[] data = new byte[0];
		try{
			String trueRequestURL = actionURL;
			URL url = new URL(trueRequestURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			//如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
			}
			try{
	        	//获取URL的响应
	        	InputStream in = connection.getInputStream();
	        	byte[] b = new byte[1];
	        	while(in.read(b) != -1){
	        		data = this.connectTwoByteArrays(data, b);
	        		//receive 1 byte content
	        		this.fileReceiveLength = this.fileReceiveLength + 1;
	        	}
	        	in.close();
	        	this.downloadComplete = true;
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
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return byte[] 返回一个储存文件内容的字节数组
	 * */
	public byte[] downloadByGet(String actionURL, HashMap<String, String> parameters){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
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
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
			}
			try{
	        	//获取URL的响应
	        	InputStream in = connection.getInputStream();
	        	byte[] b = new byte[1];
	        	while(in.read(b) != -1){
	        		data = this.connectTwoByteArrays(data, b);
	        		//receive 1 byte content
	        		this.fileReceiveLength = this.fileReceiveLength + 1;
	        	}
	        	in.close();
	        	this.downloadComplete = true;
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
	 * @param savePathName 文件在磁盘中的储存路径&文件名,文件路径+名称需要自己定义
	 * @attention 直接通过参数actionURL发送请求,用户也可以通过自己设置actionURL后的参数发送请求
	 * @attention 最后文件会以savePathName的路径名形式存储到磁盘中
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByGet(String savePathName, String actionURL){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
		boolean flag = false;
		try{
			String trueRequestURL = actionURL;
			URL url = new URL(trueRequestURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			//如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
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
	        		//received length bytes
	        		this.fileReceiveLength = this.fileReceiveLength + length;
	        	}
	        	in.close();
	        	out.close();
	        	flag = true;
	        	this.downloadComplete = true;
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
	 * @method downloadByGet
	 * @description 执行Get请求下载文件的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求URL后跟着的具体参数,以HashMap<String, String>形式传入key=value值
	 * @param savePathName 文件在磁盘中的储存路径&文件名,文件路径+名称需要自己定义
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 最后文件会以savePathName的路径名形式存储到磁盘中
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByGet(String savePathName, String actionURL, HashMap<String, String> parameters){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
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
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
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
	        		//received length bytes
	        		this.fileReceiveLength = this.fileReceiveLength + length;
	        	}
	        	in.close();
	        	out.close();
	        	flag = true;
	        	this.downloadComplete = true;
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
	 * @method downloadByGetSaveToPath
	 * @description 执行Get请求下载文件的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param savePath 文件在磁盘中的储存路径,文件名会从服务器获得
	 * @attention 直接通过参数actionURL发送请求,用户也可以通过自己设置actionURL后的参数发送请求
	 * @attention 最后文件会存储到savePath路径中,路径需要以参数方式传入,文件名通过服务器获得
	 * @attention 如果没有获取服务器响应传回的文件名,则返回false
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByGetSaveToPath(String savePath, String actionURL){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
		boolean flag = false;
		try{
			String trueRequestURL = actionURL;
			URL url = new URL(trueRequestURL);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod("GET");
			//如果存在会话，则写入会话sessionID到cookie里面
			if(!this.sessionID.equals("")){
				connection.setRequestProperty("cookie", this.sessionID);
			}
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
			}
			String ContentDisposition = connection.getHeaderField("Content-Disposition");
			if(ContentDisposition == null){
				System.out.println("No file name get from the response header!");
				flag = false;
			}else{
				String fileName = ContentDisposition.substring(ContentDisposition.lastIndexOf("filename=\"") + 10);
				fileName = fileName.substring(0, fileName.indexOf("\""));
				try{
		        	//获取URL的响应
		        	InputStream in = connection.getInputStream();
		        	File file = new File(savePath + fileName);
		        	FileOutputStream out = new FileOutputStream(file);
		        	byte[] b = new byte[1024];
		        	int length = 0;
		        	while((length = in.read(b)) != -1){
		        		out.write(b, 0, length);
		        		//received length bytes
		        		this.fileReceiveLength = this.fileReceiveLength + length;
		        	}
		        	in.close();
		        	out.close();
		        	flag = true;
		        	this.downloadComplete = true;
		        }catch(IOException e){
		        	e.printStackTrace();
		        	flag = false;
		        	System.out.println("No response get!!!");
		        }
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
	 * @method downloadByGetSaveToPath
	 * @description 执行Get请求下载文件的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求URL后跟着的具体参数,以HashMap<String, String>形式传入key=value值
	 * @param savePath 文件在磁盘中的储存路径,文件名会从服务器获得
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 最后文件会存储到savePath路径中,路径需要以参数方式传入,文件名通过服务器获得
	 * @attention 如果没有获取服务器响应传回的文件名,则返回false
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByGetSaveToPath(String savePath, String actionURL, HashMap<String, String> parameters){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
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
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
			}
			String ContentDisposition = connection.getHeaderField("Content-Disposition");
			if(ContentDisposition == null){
				System.out.println("No file name get from the response header!");
				flag = false;
			}else{
				String fileName = ContentDisposition.substring(ContentDisposition.lastIndexOf("filename=\"") + 10);
				fileName = fileName.substring(0, fileName.indexOf("\""));
				try{
		        	//获取URL的响应
		        	InputStream in = connection.getInputStream();
		        	File file = new File(savePath + fileName);
		        	FileOutputStream out = new FileOutputStream(file);
		        	byte[] b = new byte[1024];
		        	int length = 0;
		        	while((length = in.read(b)) != -1){
		        		out.write(b, 0, length);
		        		//received length bytes
		        		this.fileReceiveLength = this.fileReceiveLength + length;
		        	}
		        	in.close();
		        	out.close();
		        	flag = true;
		        	this.downloadComplete = true;
		        }catch(IOException e){
		        	e.printStackTrace();
		        	flag = false;
		        	System.out.println("No response get!!!");
		        }
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
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
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
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
			}
			try{
	        	//获取URL的响应
				//获取URL的响应
	        	InputStream in = connection.getInputStream();
	        	byte[] b = new byte[1];
	        	while(in.read(b) != -1){
	        		data = this.connectTwoByteArrays(data, b);
	        		//receive 1 byte content
	        		this.fileReceiveLength = this.fileReceiveLength + 1;
	        	}
	        	in.close();
	        	this.downloadComplete = true;
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
	 * @param savePathName 文件在磁盘中的储存路径&文件名,文件路径+名称需要自己定义
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 最后文件会以savePathName的路径名形式存储到磁盘中
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByPost(String savePathName, String actionURL, HashMap<String, String> parameters){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
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
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
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
	        		//received length bytes
	        		this.fileReceiveLength = this.fileReceiveLength + length;
	        	}
	        	in.close();
	        	out.close();
	        	flag = true;
	        	this.downloadComplete = true;
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
	 * @method downloadByPostSaveToPath
	 * @description 执行发送post请求的方法
	 * @param actionURL 发送get请求的URL地址(例如：http://www.johnson.cc:8080/Test/download)
	 * @param parameters 发送get请求数据段中的参数,以HashMap<String, String>形式传入key=value值
	 * @param savePath 文件在磁盘中的储存路径,文件名会从服务器获得
	 * @attention 最后发送的URL格式为(例如: http://www.johnson.cc:8080/Test/download?file=file1&name=XXX&pwd=aaa) 
	 * @attention 最后文件会存储到savePath路径中,路径需要以参数方式传入,文件名通过服务器获得
	 * @attention 如果没有获取服务器响应传回的文件名,则返回false
	 * @attention 如果存在会话，本方法可以保持会话，如果要消除会话，请使用invalidateSessionID方法
	 * @return boolean 返回true如果接收文件成功
	 * */
	public boolean downloadByPostSaveToPath(String savePath, String actionURL, HashMap<String, String> parameters){
		this.fileTotalLength = 0L;
		this.fileReceiveLength = 0L;
		this.ableToCaculate = false;
		this.downloadComplete = false;
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
			//get the length of the file, if get, set ableToCaculate true
			long getLength = connection.getContentLength();
			if(getLength == -1){
				this.ableToCaculate = false;
				this.fileTotalLength = 0L;
			}else{
				this.ableToCaculate = true;
				this.fileTotalLength = getLength;
			}
			String ContentDisposition = connection.getHeaderField("Content-Disposition");
			if(ContentDisposition == null){
				System.out.println("No file name get from the response header!");
				flag = false;
			}else{
				String fileName = ContentDisposition.substring(ContentDisposition.lastIndexOf("filename=\"") + 10);
				fileName = fileName.substring(0, fileName.indexOf("\""));
				try{
		        	//获取URL的响应
					InputStream in = connection.getInputStream();
		        	File file = new File(savePath + fileName);
		        	FileOutputStream out = new FileOutputStream(file);
		        	byte[] b = new byte[1024];
		        	int length = 0;
		        	while((length = in.read(b)) != -1){
		        		out.write(b, 0, length);
		        		//received length bytes
		        		this.fileReceiveLength = this.fileReceiveLength + length;
		        	}
		        	in.close();
		        	out.close();
		        	flag = true;
		        	this.downloadComplete = true;
		        }catch(IOException e){
		        	e.printStackTrace();
		        	flag = false;
		        	System.out.println("No response get!!!");
		        }
		        ds.close();
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

	/**
	 * @author johnson
	 * @method getCompleteRate
	 * @description get the complete percentage of downloading
	 * @description 下载完成度百分比(double 显示)
	 * @return double percentage
	 * */
	public double getCompleteRate(){
		double flag = 0.0;
		if(!this.ableToCaculate){//如果获取不到文件长度
			if(this.downloadComplete){//如果完成下载
				flag = 1.01;
			}else{
				flag = 0.01;
			}
		}else{
			flag = this.fileReceiveLength * 1.0 / this.fileTotalLength;
		}
		return flag;		
	}
	
	/**
	 * @author johnson
	 * @method printCompleteRate
	 * @description print the Complete Rate of Downloading on the screen by console
	 * @attention 默认一秒显示一次
	 * */
	public void printCompleteRate(){
		new CheckThread(this).start();
	}
	
	/**
	 * @author johnson
	 * @method printCompleteRate
	 * @description print the Complete Rate of Downloading on the screen by console
	 * @param showTimeBySeconds time by seconds 每隔多少秒显示一次
	 * */
	public void printCompleteRate(int showTimeBySeconds){
		new CheckThread(this, showTimeBySeconds).start();
	}
	
	/**
	 * @author johnson
	 * @class CheckThread
	 * @description a thread class which checks the complete rate of downloading
	 * */
	private class CheckThread extends Thread{
		/**
		 * fields
		 * */
		private DownUtil down;
		private int showTime = 1000;
		
		/**
		 * constructor
		 * */
		public CheckThread(DownUtil d){
			this.down = d;
		}
		
		/**
		 * constructor2
		 * */
		public CheckThread(DownUtil d, int showTime){
			this.down = d;
			this.showTime = showTime * 1000;
		}
		
		@Override
		public void run(){
			while(true){
				String rate = String.valueOf(down.getCompleteRate() * 100);
				rate = rate.substring(0, rate.indexOf(".") + 2) + "%";
				System.out.println("Downloading....." + rate);
				try{
					Thread.sleep(this.showTime);
				}catch(Exception e){
					e.printStackTrace();
				}
				if(down.getCompleteRate() >= 1.0){
					System.out.println("Downloading.....100.00%");
					System.out.println("Downloadind suceeded!");
					break;
				}
			}
		}
	}
}
