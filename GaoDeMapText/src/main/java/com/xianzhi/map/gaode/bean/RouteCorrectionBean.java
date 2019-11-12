package com.xianzhi.map.gaode.bean;

import java.io.Serializable;

/**
 * @author liMing
 * @Demo class RouteCorrectionListBean
 * @Description TODO
 * @date 2019-11-08 17:32
 */
public class RouteCorrectionBean implements Serializable {

	private String timeContent;
	private String disContent;
	/*单位：秒*/
	private int time;
	/*单位：米*/
	private int dis;

	public String getTimeContent() {
		return timeContent;
	}

	public void setTimeContent(String timeContent) {
		this.timeContent = timeContent;
	}

	public String getDisContent() {
		return disContent;
	}

	public void setDisContent(String disContent) {
		this.disContent = disContent;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getDis() {
		return dis;
	}

	public void setDis(int dis) {
		this.dis = dis;
	}
}
