package com.example.almin;


public class MyConfiguration {
	
	private static final MyConfiguration instance = new MyConfiguration();
	public static final String API_BASE = "http://192.168.1.100:8080/AssetsService/";
	
	
	public static MyConfiguration getInstance() {
		return instance;
	}
	

	public static void init() {//初始化其他常量
		
	}

}
