package com.schibsted.spain.friends.configuration;

public final class Router {
	private Router(){
		//Prevents instantiation
	}

	public static final String USERNAME_FROM = "usernameFrom";
	public static final String USERNAME_TO = "usernameTo";
	public static final String X_PASS = "X-Password";
	public static final String USERNAME = "username";
	public static final String REQUEST = "/request";
	public static final String ACCEPT = "/accept";
	public static final String DECLINE = "/decline";
	public static final String LIST = "/list";
	public static final String FRIENDSHIP_REQUEST_MAPPING = "/friendship";
	public static final String SIGN_UP_REQUEST_MAPPING = "/signup";
}
