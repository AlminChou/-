package com.example.almin.library.model;

public class Process {
	private String processId; //����ID
	private String assetid;   //�ʲ�ID
	private String username;  //�����û�����
	private String state;     //����״̬ �����룬ά�ޣ����ϣ�
	private String message;   //������������˵��
	private String isAccept;  //�Ƿ��Ѿ�ͨ��
	private String repairState; //ά��״̬
	private String date; //��������
	
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
