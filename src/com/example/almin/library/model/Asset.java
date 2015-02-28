package com.example.almin.library.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class Asset {
	private String name;         //资产物品名字
	private String id;           //资产id
	private String category;     //资产类别
	private String state;        //占用状态（空闲，占用，报废，申请，维修？）
	private String owner;        //占有者姓名
	private String purchase_date;//入库日期
	private String description;  //资产具体描述

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPurchase_date() {
		return purchase_date;
	}

	public void setPurchase_date(String purchase_date) {
		this.purchase_date = purchase_date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static HashMap<Integer, List<Asset>> decodeAssetsFromJson(String assets){
		List<Asset> allAssets = new Gson().fromJson(assets,  new TypeToken<ArrayList<Asset>>(){}.getType());  
		HashMap<Integer, List<Asset>> hashMap = new HashMap<Integer, List<Asset>>();
		List<Asset> list=new ArrayList<Asset>();
		List<Asset> list1=new ArrayList<Asset>();
		List<Asset> list2=new ArrayList<Asset>();
		List<Asset> list3=new ArrayList<Asset>();
		List<Asset> list4=new ArrayList<Asset>();
		List<Asset> list5=new ArrayList<Asset>();

		for(int i=0;i<allAssets.size();i++){
			if(allAssets.get(i).getCategory().equalsIgnoreCase("电器")){
				list.add(allAssets.get(i));
				System.out.println("电器");
			}else if(allAssets.get(i).getCategory().equalsIgnoreCase("家具")){
				System.out.println("家具");
				list1.add(allAssets.get(i));
			}else if(allAssets.get(i).getCategory().equalsIgnoreCase("杂物")){
				list2.add(allAssets.get(i));
				System.out.println("杂物");
			}else if(allAssets.get(i).getCategory().equalsIgnoreCase("车辆")){
				list3.add(allAssets.get(i));
				System.out.println("车辆");
			}else if(allAssets.get(i).getCategory().equalsIgnoreCase("房屋")){
				list4.add(allAssets.get(i));
				System.out.println("房屋");
			}else if(allAssets.get(i).getCategory().equalsIgnoreCase("文具")){
				list5.add(allAssets.get(i));
				System.out.println("文具");
			}
		}


		hashMap.put(0, list);
		hashMap.put(1, list1);
		hashMap.put(2, list2);
		hashMap.put(3, list3);
		hashMap.put(4, list4);
		hashMap.put(5, list5);
		allAssets.clear();
		allAssets=null;
		return hashMap;
	}

	//	public static List<Asset> decodeAssetsFromJson(String assets){
	//		List<Asset> list;
	//		list=new Gson().fromJson(assets,  new TypeToken<ArrayList<Asset>>(){}.getType());  
	//		return list;
	//	}
}
