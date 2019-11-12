package com.xianzhi.map.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author liMing
 * @Demo class CarMovingBean
 * @Description TODO
 * @date 2019-11-11 13:13
 */
public class CarMovingBean implements Serializable {

	private List<DataBean> data;

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean {
		/**
		 * latitude : 39.995825
		 * longitude : 116.47676
		 */

		private double latitude;
		private double longitude;

		public double getLatitude() {
			return latitude;
		}

		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}

		public double getLongitude() {
			return longitude;
		}

		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
	}
}
