package com.myapp.tracyweather.util;

public interface HttpCallbackListener {
	
	void onFinish(String response);
	
	void onEorror(Exception e);

}
