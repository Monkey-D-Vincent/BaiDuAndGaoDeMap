package com.xianzhi.map.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author liMing
 * @Demo class RouteCorrectionListBean
 * @Description TODO
 * @date 2019-11-11 13:14
 */
public class RouteCorrectionListBean implements Serializable {

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
		 * rearing : 44.0
		 * speed : 37.0
		 * time : 1470212510269
		 */

		private double latitude;
		private double longitude;
		private float rearing;
		private float speed;
		private long time;

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

		public float getRearing() {
			return rearing;
		}

		public void setRearing(float rearing) {
			this.rearing = rearing;
		}

		public float getSpeed() {
			return speed;
		}

		public void setSpeed(float speed) {
			this.speed = speed;
		}

		public long getTime() {
			return time;
		}

		public void setTime(long time) {
			this.time = time;
		}
	}
}
