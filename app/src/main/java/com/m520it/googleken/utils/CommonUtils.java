package com.m520it.googleken.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import java.io.File;

/**
 * @author lihoujing2ken
 * @time 2017/1/9  13:17
 * @desc 用于判断APP的状态
 */
public class CommonUtils
{

	/**
	 * 判断包是否安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalled(Context context, String packageName)
	{
		PackageManager manager = context.getPackageManager();
		try
		{
			manager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);

			return true;
		}
		catch (NameNotFoundException e)
		{
			return false;
		}
	}

	/**
	 * 安装应用程序
	 * 
	 * @param context
	 * @param apkFile
	 */
	public static void installApp(Context context, File apkFile)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 打开应用程序
	 * 
	 * @param context
	 * @param packageName
	 */
	public static void openApp(Context context, String packageName)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		context.startActivity(intent);
	}
}
