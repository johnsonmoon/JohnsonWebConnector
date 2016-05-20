package com.xuyihao.test;

/**
 * created by xuyihao on 2016/5/14
 * @author Johnson
 * @attention 测试类,所有注释中的代码都可以通过测试
 * @description 主类,主函数
 * */
public class Test {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "/new/[电影天堂www.dy2018.com]遗落战境BD国英双语中英双字.mkv");
		MultiThreadDownUtil down = new MultiThreadDownUtil("http://127.0.0.1:8080/EBwebTest/Download", map, "/home/johnson/Desktop/new.rmvb", 5);
		if(down.download()){
			down.printCompleteRate(3);
		}else{
			System.out.println("下载出错!");
		}
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "new/down2.doc");
		System.out.println(downUtil.downloadByPost("/home/johnson/Desktop/hahahahah.doc", "http://127.0.0.1:8080/EBwebTest/Download", map));
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "new/down.txt");
		byte[] result = downUtil.downloadByPost("http://127.0.0.1:8080/EBwebTest/Download", map);
		File file = new File("/home/johnson/Desktop/newFileJohnson.txt");
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			out.write(result);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		/*
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX, "http://115.28.192.61:8088/EBwebTest/Accounts");
		HashMap<String, String> map = new HashMap<>();
		map.put("das", "duagdbiyacgbiab");
		map.put("daa", "daudohau");
		map.put("dad", "dadgvy");
		String[] uploadFiles = new String[2];
		uploadFiles[0] = "/home/johnson/Desktop/uploadFile.txt";
		uploadFiles[1] = "/home/johnson/Desktop/uploadFile2.txt";
		System.out.println(httpUtil.multipleFileUploadWithParameters("http://127.0.0.1:8080/EBwebTest/Upload", uploadFiles, FileType.Text_TXT, map));
		*/
		
		/*
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX, "http://115.28.192.61:8088/EBwebTest/Accounts");
		HashMap<String, String> map = new HashMap<>();
		map.put("das", "duagdbiyacgbiab");
		map.put("daa", "daudohau");
		map.put("dad", "dadgvy");
		System.out.println(httpUtil.singleFileUploadWithParameters("http://127.0.0.1:8080/EBwebTest/IPContentTest", "/home/johnson/Desktop/uploadFile2.txt", FileType.Text_TXT, map));
		*/
		
		/*
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX, "http://115.28.192.61:8088/EBwebTest/Accounts");
		while(true){
			HashMap<String, String> map = new HashMap<>();
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
			System.out.println(httpUtil.executePost("http://115.28.192.61:8088/EBwebTest/Accounts", map));
		}
		*/
			
		/*
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX, "http://115.28.192.61:8088/EBwebTest/Accounts");
		HashMap<String, String> map = new HashMap<>();
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
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX, "http://115.28.192.61:8088/EBwebTest/Accounts");
		String fileName = "";
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			fileName = br.readLine();
		}catch(IOException e){
			e.printStackTrace();
		}
		httpUtil.singleFileUpload(fileName, "http://115.28.192.61:8088/EBwebTest/Upload", MIME_FileType.Text_TXT);
		*/
		
		/*
		HttpUtil httpUtil = new HttpUtil(Platform.LINUX, "http://115.28.192.61:8088/EBwebTest/Accounts");
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
		httpUtil.multipleFileUpload(fileNames, "http://127.0.0.1:8080/EBwebTest/Upload", MIME_FileType.Text_TXT);
		*/
	}	
}