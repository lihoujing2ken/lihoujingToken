package com.m520it.googleken.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  13:17
 * @desc 关闭流的工具类
 */
public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
