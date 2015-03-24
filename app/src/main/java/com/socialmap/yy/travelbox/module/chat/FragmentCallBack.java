package com.socialmap.yy.travelbox.module.chat;


import com.socialmap.yy.travelbox.module.chat.activity.MainActivity;
import com.socialmap.yy.travelbox.module.chat.service.XXService;

public  interface FragmentCallBack {
	public  XXService getService();

	public MainActivity getMainActivity();
}

