package com.myapp.tracyweather.util;

import com.myapp.tracyweather.db.MyWeatherDB;
import com.myapp.tracyweather.model.City;
import com.myapp.tracyweather.model.County;
import com.myapp.tracyweather.model.Province;

import android.R.bool;
import android.R.string;
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

}
