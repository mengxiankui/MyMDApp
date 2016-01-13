package com.example.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import com.mxk.baseframe.common.CacheDirUtil;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.SparseArray;

public class AddressUtil {

	private Context context;
	private SQLiteDatabase db;
	private String databaseFilename;

	public static final int NAME = 0;
	public static final int ID = 1;

	public AddressUtil(Context context) {
		super();
		this.context = context;
		init();
	}

	public void uninit() {
		if (null != db) {
			db.close();
		}
	}

	private void init() {
		try {
			File dir = CacheDirUtil.getDiskCacheDir(context,
					"china_province_city_zone");
			// 将要存放于的文件夹
			String DATABASE_FILENAME = "china_province_city_zone"; // 文件名
			databaseFilename = dir.getAbsolutePath() + File.separator
					+ DATABASE_FILENAME;
			// 如果/sdcard/testdb目录中存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/testdb目录中不存在
			// test.db文件，则从asset\db目录中复制这个文件到
			// SD卡的目录（/sdcard/testdb）
			if (!(new File(databaseFilename)).exists()) {
				// 获得封装testDatabase.db文件的InputStream对象
				AssetManager asset = context.getAssets();
				InputStream is = asset.open("db/china_province_city_zone");
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// 开始复制testDatabase.db文件
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
				asset.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获取省的地址列表
	public SparseArray<ArrayList<String>> getProvince() {

		String sql = "select ProName, ProSort from T_Province";
		Cursor c = null;
		SparseArray<ArrayList<String>> provinceData = new SparseArray<ArrayList<String>>(
				2);
		// List provinceList = null;
		try {
			openDatabaseIfNeeded();
			c = db.rawQuery(sql, null);
			ArrayList<String> provinceList1 = new ArrayList<String>();
			ArrayList<String> provinceList2 = new ArrayList<String>();
			while (c.moveToNext()) {
				provinceList1.add(c.getString(0));
				provinceList2.add(c.getString(1));
			}
			provinceData.put(NAME, provinceList1);
			provinceData.put(ID, provinceList2);
		} catch (Exception e) {
			Log.d("WineStock", "getProvince:" + e.getMessage());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return provinceData;
	}

	// 获取对应省下面城市的列表,id-->指对应省的ID
	public SparseArray<ArrayList<String>> getCityByPid(int id) {
		String sql = "select CityName, CitySort from T_City where ProID= " + id;
		Cursor c = null;
		SparseArray<ArrayList<String>> cityData = new SparseArray<ArrayList<String>>(
				2);
		try {
			openDatabaseIfNeeded();
			c = db.rawQuery(sql, null);
			ArrayList<String> cityList1 = new ArrayList<String>();
			ArrayList<String> cityList2 = new ArrayList<String>();
			while (c.moveToNext()) {
				cityList1.add(c.getString(0));
				cityList2.add(c.getString(1));
			}
			cityData.put(NAME, cityList1);
			cityData.put(ID, cityList2);

		} catch (Exception e) {
			Log.d("WineStock", "getCityByPid:" + e.getMessage());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return cityData;
	}

	// 获取对应市下面区的列表,//file-->数据库文件,id-->指对应市的ID
	public SparseArray<ArrayList<String>> getAreaByPid(int id) {
		String sql = "select ZoneName  from T_Zone where CityID= " + id;
		Cursor c = null;
		SparseArray<ArrayList<String>> areaData = new SparseArray<ArrayList<String>>(
				2);
		try {
			openDatabaseIfNeeded();
			c = db.rawQuery(sql, null);
			ArrayList<String> areaList = new ArrayList<String>();
			ArrayList<String> emptyList = new ArrayList<String>();
			while (c.moveToNext()) {
				areaList.add(c.getString(0));
			}
			areaData.put(NAME, areaList);
			areaData.put(ID, emptyList);
			
		} catch (Exception e) {
			Log.d("WineStock", "getAreaByPid:" + e.getMessage());
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return areaData;
	}

	private void openDatabaseIfNeeded() {
		// TODO Auto-generated method stub
		if (null == db || !db.isOpen()) {
			db = SQLiteDatabase.openDatabase(databaseFilename, null,
					SQLiteDatabase.OPEN_READONLY);
		}

	}

}