package com.onlan.lazymemo.sqlData;

public class MemoInfo {
	private int id;
	private String content;
	private int type;
	private String time;
	private String date;
	private int isalarmclock;
	private int bgcorclor;

	public int getBgcorclor() {
		return bgcorclor;
	}

	public void setBgcorclor(int bgcorclor) {
		this.bgcorclor = bgcorclor;
	}

	public MemoInfo(String content, String time, String date, int type,
			int isalarmclock,int bgcorclor) {
		this.content = content;
		this.type = type;
		this.time = time;
		this.date = date;
		this.isalarmclock = isalarmclock;
		this.bgcorclor=bgcorclor;

	}

	public MemoInfo(int id, String content, String time, String date, int type,
			int isalarmclock,int bgcorclor) {
		this.id = id;
		this.content = content;
		this.type = type;
		this.time = time;
		this.date = date;
		this.isalarmclock = isalarmclock;
		this.bgcorclor=bgcorclor;

	}

	public int getid() {
		return id;
	}

	public void setid(int _id) {
		this.id = _id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIsalarmclock() {
		return isalarmclock;
	}

	public void setIsalarmclock(int isalarmclock) {
		this.isalarmclock = isalarmclock;
	}
}
