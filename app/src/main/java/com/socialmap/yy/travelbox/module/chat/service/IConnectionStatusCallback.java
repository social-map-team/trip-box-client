package com.socialmap.yy.travelbox.module.chat.service;

public interface IConnectionStatusCallback {
	public void connectionStatusChanged(int connectedState, String reason);
}
