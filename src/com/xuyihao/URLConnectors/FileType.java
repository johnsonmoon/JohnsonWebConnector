package com.xuyihao.URLConnectors;

/**
 * created by xuyihao on 2016/5/15
 * @author johnson
 * @description 文件类型的枚举
 * */
public enum FileType {
	
	Image_JPEG("Content-Type: image/jpeg"), 
	Package_WAR("Content-Type: application/octet-stream"),
	Text_TXT("Content-Type: text/plain"),
	Microsoft_WORD("Content-Type: application/msword"),
	Microsoft_WORDX("Content-Type: application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
	Video_AVI("Content-Type: video/avi"),
	Audio_MP3("Content-Type: audio/mp3"),
	Dot_SQL("Content-Type: application/octet-stream"),
	Video_MP4("Content-Type: video/mp4"),
	Image_PNG("Content-Type: image/png"),
	Microsoft_EXCEL("Content-Type: application/vnd.ms-excel"),
	Dot_LRC("Content-Type: application/octet-stream"),
	Audio_WMA("Content-Type: audio/x-ms-wma"),
	Audio_RMVB("Content-Type: application/octet-stream"),
	Microsoft_VISIO("Content-Type: application/vnd.ms-visio.drawing"),
	Package_ZIP("Content-Type: application/octet-stream"),
	Package_RAR("Content-Type: application/octet-stream"),
	Image_GIF("Content-Type: image/gif");
	
	
	private final String value;
	
	private FileType(String v){
		this.value = v;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
