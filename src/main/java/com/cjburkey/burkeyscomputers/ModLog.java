package com.cjburkey.burkeyscomputers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModLog {
	
	public static final Logger logger = LogManager.getLogger(ModInfo.MOD_ID);
	
	public static void info(Object msg) {
		logger.info(prepare(msg));
	}
	
	public static void info(Object msg, Object... args) {
		logger.info(prepare(msg), args);
	}
	
	public static void error(Object msg) {
		logger.error(prepare(msg));
	}
	
	public static void error(Object msg, Object... args) {
		logger.error(prepare(msg), args);
	}
	
	private static String prepare(Object msg) {
		if (msg == null) {
			return "null";
		}
		return msg.toString();
	}
	
}