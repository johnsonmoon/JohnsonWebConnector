package com.xuyihao.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.xuyihao.URLConnectors.HttpUtil;

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
		HttpUtil httpUtil = new HttpUtil();
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
		httpUtil.fileUpload(fileNames, "http://localhost:8080/EBwebTest/Upload");
	}

}
