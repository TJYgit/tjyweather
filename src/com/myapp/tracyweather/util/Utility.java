package com.myapp.tracyweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.myapp.tracyweather.db.MyWeatherDB;
import com.myapp.tracyweather.model.City;
import com.myapp.tracyweather.model.County;
import com.myapp.tracyweather.model.Province;

import android.R.bool;
import android.R.string;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class Utility {
	
	/**
	 * 解析和处理服务器返回的省级数据
	 * @param myWeatherDB
	 * @param response
	 * @return
	 */
	
	public synchronized static boolean handleProvincesResponse(MyWeatherDB myWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces=response.split(",");
			if(allProvinces!=null && allProvinces.length>0){
				for(String p:allProvinces){
					String[] array=p.split("\\|");
					Province province=new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					myWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析和处理服务器返回的城市数据
	 * @param myWeatherDB
	 * @param response
	 * @param provinceId
	 * @return
	 */
	public static boolean handleCitiesResponse(MyWeatherDB myWeatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities=response.split(",");
			if(allCities!=null&&allCities.length>0){
				for(String c:allCities){
					String[] array=c.split("\\|");
					City city=new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					myWeatherDB.saveCity(city);
					
				}
				return true;
			}
		}
		return false;
	}
	
	public static boolean handleCountiesResponse(MyWeatherDB myWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties=response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String c:allCounties){
					String[] array=c.split("\\|");
					County county=new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					myWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 解析服务器返回的JSON数据，并将解析出的数据存储到本地
	 * @param context
	 * @param response
	 */
	public static void handleWeatherResponse(Context context,String response){
		try {
			JSONObject jsonObject=new JSONObject(response);
			JSONArray weather=jsonObject.getJSONArray("HeWeather data service 3.0");
			JSONObject basic=weather.getJSONObject(0);
			JSONObject weatherInfo=basic.getJSONObject("basic");
			JSONObject now=basic.getJSONObject("now");
			//int length=forcast.length();
			String cityName=weatherInfo.getString("city");
			String weatherCode=weatherInfo.getString("id");
			String temp1=now.getString("tmp");
			String temp2=now.getString("tmp");
			JSONObject desp=now.getJSONObject("cond");
			String weatherDesp=desp.getString("txt");
			JSONObject time=weatherInfo.getJSONObject("update");
			String publishTime=time.getString("loc");
			saveWeatherInfo(context,cityName,weatherCode,temp1,temp2,weatherDesp,publishTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将服务器返回的天气信息存储到SharedPreferences中
	 * @param context
	 * @param cityName
	 * @param weatherCode
	 * @param temp1
	 * @param temp2
	 * @param weatherDesp
	 * @param publishTime
	 */
	private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1,
			String temp2, String weatherDesp, String publishTime) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
		SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("citySelected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time",publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
		
	}

}
