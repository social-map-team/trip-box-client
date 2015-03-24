package com.socialmap.yy.travelbox;


import com.socialmap.yy.travelbox.chat.activity.MainActivity;
import com.socialmap.yy.travelbox.chat.service.XXService;

public  interface FragmentCallBack {
	public  XXService getService();

	public MainActivity getMainActivity();
}

