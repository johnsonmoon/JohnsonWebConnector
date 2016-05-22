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
		MultiThreadDownUtil d = new MultiThreadDownUtil("http://file01.16sucai.com/d/file/2011/0808/20110808122856159.rar", 4);
		d.download("/home/johnson/Desktop/123444.rar");
		d.printCompleteRate(1);
		*/
		
		/*
		DownUtil down = new DownUtil();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FilePathName", "大悲咒.wma");
		System.out.println(down.downloadByGetSaveToPath("/home/johnson/Desktop/temp/", "http://127.0.0.1:8080/EBwebTest/Download", map));
		*/
		/*
		DownUtil down = new DownUtil();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("FilePathName", "大悲咒.wma");
		System.out.println(down.downloadByPostSaveToPath("/home/johnson/Desktop/temp/", "http://127.0.0.1:8080/EBwebTest/Download", map));
		*/
		
		/*
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "temp/[电影天堂www.dy2018.com]分手大师BD中英双字.rmvb");
		MultiThreadDownUtil down = new MultiThreadDownUtil("http://127.0.0.1:8080/EBwebTest/Download", map, 5);
		if(down.downloadToPath("/home/johnson/Desktop/")){
			down.printCompleteRate(3);
		}else{
			System.out.println("下载出错!");
		}
		*/
		
		/*
		DownUtil down = new DownUtil();
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "001-01.wma");
		System.out.println(down.downloadByPost("/home/johnson/Desktop/001-01.wma", "http://115.28.192.61:8088/EBwebTest/Download", map));
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
		map.put("FilePathName", "temp/CSDN博客Android客户端开发.pdf");
		downUtil.printCompleteRate(1);
		byte[] result = downUtil.downloadByPost("http://127.0.0.1:8080/EBwebTest/Download", map);
		File file = new File("/home/johnson/Desktop/naa.pdf");
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			out.write(result);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		byte[] result = downUtil.downloadByGet("http://127.0.0.1:8080/EBwebTest/Download?FilePathName=temp/CSDN博客Android客户端开发.pdf");
		File file = new File("/home/johnson/Desktop/naabaugsuf.pdf");
		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			out.write(result);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		downUtil.downloadByGet("/home/johnson/Desktop/naabaugsuf.pdf", "http://127.0.0.1:8080/EBwebTest/Download?FilePathName=temp/CSDN博客Android客户端开发.pdf");
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "temp/CSDN博客Android客户端开发.pdf");
		downUtil.downloadByGet("/home/johnson/Desktop/jahdugagi.mp3", "http://127.0.0.1:8080/EBwebTest/Download", map);
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		downUtil.downloadByGetSaveToPath("/home/johnson/Desktop/", "http://127.0.0.1:8080/EBwebTest/Download?FilePathName=temp/CSDN博客Android客户端开发.pdf");
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "temp/[电影天堂www.dy2018.com]分手大师BD中英双字.rmvb");
		downUtil.downloadByGetSaveToPath("/home/johnson/Desktop/", "http://127.0.0.1:8080/EBwebTest/Download", map);
		*/
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "temp/[电影天堂www.dy2018.com]分手大师BD中英双字.rmvb");
		downUtil.downloadByPost("/home/johnson/Desktop/adhuih.rmvb", "http://127.0.0.1:8080/EBwebTest/Download", map);
		*/
		
		
		/*
		DownUtil downUtil = new DownUtil();
		downUtil.printCompleteRate(2);
		HashMap<String, String> map = new HashMap<>();
		map.put("FilePathName", "temp/[电影天堂www.dy2018.com]分手大师BD中英双字.rmvb");
		downUtil.downloadByPostSaveToPath("/home/johnson/Desktop/", "http://127.0.0.1:8080/EBwebTest/Download", map);
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
			System.out.println(httpUtil.executePostByUsual("http://115.28.192.61:8088/EBwebTest/Accounts", map));
		}
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
			System.out.println(httpUtil.executePostByUsual("http://127.0.0.1:8080/EBwebTest/Accounts", map));
		}
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
			System.out.println(httpUtil.executePostByMultipart("http://127.0.0.1:8080/EBwebTest/Accounts", map));
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
		httpUtil.singleFileUpload("http://115.28.192.61:8088/EBwebTest/Upload", fileName, MIME_FileType.Text_txt);
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