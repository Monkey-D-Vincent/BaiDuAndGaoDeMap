package com.xianzhi.map.gaode.util.util;

import java.math.BigDecimal;

/**
 * @author liMing
 * @Demo class StringUtil
 * @Description TODO
 * @date 2019-11-07 15:34
 */
public class StringUtil {
	public static String NumericScaleByFloor(String numberValue, int scale) {
		return new BigDecimal(numberValue).setScale(scale, BigDecimal.ROUND_FLOOR).toString();
	}
}
