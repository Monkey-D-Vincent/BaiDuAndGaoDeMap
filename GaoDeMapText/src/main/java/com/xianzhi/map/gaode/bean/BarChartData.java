package com.xianzhi.map.gaode.bean;

import java.io.Serializable;

/**
 * @author liMing
 * @Demo class BarChartData
 * @Description TODO
 * @date 2019-11-07 15:33
 */
public class BarChartData implements Serializable {

	private String name;
	private float value;
	private String unit;
	private int positionX;
	private int width;

	public String getName() {
		return name;
	}

	public float getValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}

	public int getPositionX() {
		return positionX;
	}

	public int getWidth() {
		return width;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
