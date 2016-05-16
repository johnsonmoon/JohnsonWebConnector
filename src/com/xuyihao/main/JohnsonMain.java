package com.xuyihao.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import com.xuyihao.URLConnectors.FileType;
import com.xuyihao.URLConnectors.HttpUtil;
import com.xuyihao.URLConnectors.Platform;

/**
 * created by xuyihao on 2016/5/14
 * @author Johnson
 * @description 主类,主函数
 * */
public class JohnsonMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpUtil httpUtil = new HttpUtil(Platform.WINDOWS);
		
		
		while(true){
			String action = "";
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				action = br.readLine();
							
			}catch(IOException e){
				e.printStackTrace();
			}
			httpUtil.getTest("http://115.28.192.61:8088/EBwebTest/Accounts?action=" + action);
		}
		
		
		/*
		HashMap<String, String> map = new HashMap();
		String key = "";
		String value = "";
		while(!key.equals("done")){
			try{
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				key = br.readLine();
				value = br.readLine();
				
				if(key.equals("done")){
					break;
				}
				
			}catch(IOException e){
				e.printStackTrace();
			}
			map.put(key, value);
		}
		
		httpUtil.executeGet("http://115.28.192.61:8088/EBwebTest/Accounts", map);
		*/
		
		
		/*
		String fileName = "";
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			fileName = br.readLine();
		}catch(IOException e){
			e.printStackTrace();
		}
		httpUtil.singleFileUpload(fileName, "http://127.0.0.1:8080/EBwebTest/Upload", FileType.Audio_MP3);
		*/
		
		/*
		String[] fileNames = new String[2];
		try{
			InputStreamReader reader = new InputStreamReader(System.in);
			BufferedReader br = new BufferedReader(reader);
			for(int i = 0; i <= 1; i++){
				fileNames[i] = br.readLine();
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		httpUtil.multipleFileUpload(fileNames, "http://127.0.0.1:8080/EBwebTest/Upload", FileType.Text_TXT);
		*/
	}

}
