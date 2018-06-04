package com.core.chan.pattern.singleton.lazy;


public class Singleton {
	private static Singleton singleton;
	private Singleton(){}

	public static Singleton getSingleton(){
		singleton = null == singleton ? new Singleton() : singleton;
		return singleton;
	}
}
