package com.cjburkey.burkeyscomputers;

import java.util.Random;

public class ModUtil {
	
	private static Random rand;
	
	/**
	 * Returns a random number between the minimum value (inclusive)
	 * and the maximum value (exclusive).
	 * 
	 * @param min	The minimum value
	 * @param max	The bound value.
	 * @return	A random number between min (inc) and max (excl).
	 */
	public static int random(int min, int exmax) {
		if (min == exmax) {
			return min;
		}
		min = Math.abs(min);
		exmax = Math.abs(exmax);
		if (min > exmax) {
			int tmp = exmax;
			exmax = min;
			min = tmp;
		}
		if (rand == null) {
			rand = new Random();
		}
		return (rand.nextInt(exmax - min) + min);
	}
	
	public static <T> T[] removeFirstArrayItem(T[] array, T[] out) {
		for (int i = 1; i < array.length; i ++) {
			out[i - 1] = array[i];
		}
		return out;
	}
	
}