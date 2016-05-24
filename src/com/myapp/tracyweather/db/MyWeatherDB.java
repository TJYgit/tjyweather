package com.myapp.tracyweather.db;

import java.util.ArrayList;
import java.util.List;

import com.myapp.tracyweather.model.City;
import com.myapp.tracyweather.model.County;
import com.myapp.tracyweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MyWeatherDB {

	public static final String DB_NAME="tracy_weather";
	
	public static final int VERSION=1;
	
	private static MyWeatherDB myWeatherDB;
	
	private SQLiteDatabase db;
	
	/**
	 * 构造方法私有化
	 * @param context
	 */
	
	private MyWeatherDB(Context context) {
		
		WeatherDbOpenHelper dbOpenHelper=new WeatherDbOpenHelper(context, DB_NAME, null, VERSION);
		db=dbOpenHelper.getWritableDatabase();
		
	}
	
	/**
	 * 获取myWeatherDB的实例
	 * @param context
	 * @return
	 */
	private synchronized static MyWeatherDB getInstance(Context context){
		if(myWeatherDB==null){
			myWeatherDB=new MyWeatherDB(context);
		}
		return myWeatherDB;
	}
	
	/**
	 * 将province实例存储到数据库
	 * @param province
	 */
	
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	public List<Province> loadProvinces(){
		List<Province> list=new ArrayList<Province>();
		Cursor cursor=db
				.query("province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do{
				Province province=new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			}while(cursor.moveToNext());
		}
		return list;
	}
	
	/**
	 * 将city实例存储到数据库
	 * @param city
	 */
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	
	/**
	 * 从数据读取某省下所有的城市信息
	 * @param provinceId
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list=new ArrayList<City>();
		Cursor cursor=db.query("City", null, "province_id=?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("provincId")));
				list.add(city);
			}while(cursor.moveToNext());
		}
		if (cursor!=null) {
			cursor.close();
		}
		return list;
	}
	
	
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values=new ContentValues();
			values.put("county_name", county.getCountyId());
			values.put("county_code", county.getCountyId());
			values.put("county_id", county.getCountyId());
			db.insert("County", null, values);
		}
	}
	
	
	
	public List<County> loadCounties(int cityId){
		List<County> list=new ArrayList<County>();
		Cursor cursor=db.query("County", null, "city_id=?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county=new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyId(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyId(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getInt(cursor.getColumnIndex("cityId")));
				list.add(county);
			}while(cursor.moveToNext());
		}
		if (cursor!=null) {
			cursor.close();
		}
		return list;
	}
	
	
}
