package com.xuyihao.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX);
		
		String fileName = "";
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			fileName = br.readLine();
		}catch(IOException e){
			e.printStackTrace();
		}
		httpUtil.singleFileUpload(fileName, "http://127.0.0.1:8080/EBwebTest/Upload", FileType.Text_TXT);
		
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
		httpUtil.multipleFileUpload(fileNames, "http://115.28.192.61:8088/EBwebTest/IPContentTest", FileType.Text_TXT);
		*/
	}

}
