package com.example.almin.library.model;

public class NeedsComment {
	private String id;           //����id
	private String useraccount;  //�û��˺�
	private String username;     //�û�����
	private String lack_asset;   //������Դ����
	private String date;         //��������
	private String comment;      //����
	private String read_state;   //�Ƿ��Ѷ�
	private String is_add;       //�Ƿ��Ѿ�����
	
	public String getUsername() {
		return username;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUseraccount() {
		return useraccount;
	}

	public void setUseraccount(String useraccount) {
		this.useraccount = useraccount;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getLack_asset() {
		return lack_asset;
	}
	
	public void setLack_asset(String lack_asset) {
		this.lack_asset = lack_asset;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getRead_state() {
		return read_state;
	}
	
	public void setRead_state(String read_state) {
		this.read_state = read_state;
	}

	public String getIs_add() {
		return is_add;
	}

	public void setIs_add(String is_add) {
		this.is_add = is_add;
	}

}
