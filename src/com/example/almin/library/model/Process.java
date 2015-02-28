package com.example.almin.library.model;

public class Process {
	private String processId; //请求ID
	private String assetid;   //资产ID
	private String username;  //请求用户姓名
	private String state;     //请求状态 （申请，维修，报废）
	private String message;   //请求想问留言说明
	private String isAccept;  //是否已经通过
	private String repairState; //维修状态
	private String date; //请求日期
	
	public String getAssetid() {
		return assetid;
	}
	
	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public void setAssetid(String assetid) {
		this.assetid = assetid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public String getIsAccept() {
		return isAccept;
	}

	public void setIsAccept(String isAccept) {
		this.isAccept = isAccept;
	}

	public String getRepairState() {
		return repairState;
	}

	public void setRepairState(String repairState) {
		this.repairState = repairState;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
