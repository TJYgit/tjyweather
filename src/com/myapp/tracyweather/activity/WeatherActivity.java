package com.myapp.tracyweather.activity;


import com.myapp.tracyweather.R;
import com.myapp.tracyweather.util.HttpCallbackListener;
import com.myapp.tracyweather.util.HttpUtil;
import com.myapp.tracyweather.util.Utility;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {

	private LinearLayout weatherInfoLayout;
	private TextView cityNameText;//��ʾ������
	private TextView publishText;//��ʾ����ʱ��
	private TextView weatherDespText;//��ʾ��������
	private TextView temp1Text;//��ʾ����1
	private TextView temp2Text;//��ʾ����2
	private TextView currentDateText;//��ʾ��ǰ����
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//��ʼ�����ؼ�
		weatherInfoLayout=(LinearLayout)findViewById(R.id.weather_info_layout);
		cityNameText=(TextView) findViewById(R.id.city_name);
		publishText=(TextView) findViewById(R.id.publish_text);
		weatherDespText=(TextView) findViewById(R.id.weather_desp);
		temp1Text=(TextView) findViewById(R.id.temp1);
		temp2Text=(TextView) findViewById(R.id.temp2);
		currentDateText=(TextView) findViewById(R.id.current_date);
		String countyCode=getIntent().getStringExtra("county_code");//�õ���һ��intent�����ĸ�����Ϣ
		if(!TextUtils.isEmpty(countyCode)){
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			showWeather();
		}
	}
	
	/**
	 * ��ѯ�ؼ����Ŷ�Ӧ����������
	 */
	private void queryWeatherCode(String countyCode){
		String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}

	/**
	 * ��ѯ�������Ŷ�Ӧ������
	 */
	private void queryWeatherInfo(String weatherCode){
		String address="https://api.heweather.com/x3/weather?cityid=CN"+weatherCode+"&key=1c3dd6a908d1467dbb730e3e6aaffcb6";
		queryFromServer(address, "weatherCode");
	}
	
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(final String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//�ӷ��������ص������н�������������
						String[] array=response.split("\\|");
						if(array!=null&&array.length==2){
							String weatherCode=array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						public void run() {
							showWeather();
						}

					});
				}
				
			}
			
			@Override
			public void onEorror(Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {
						publishText.setText("ͬ��ʧ��");
					}
				});
				
			}
		});
		
	}
	
	private void showWeather() {
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp2", ""));
		temp2Text.setText(prefs.getString("temp1", "")); 
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����"+prefs.getString("publish_time", "")+"����");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
	}
	
}