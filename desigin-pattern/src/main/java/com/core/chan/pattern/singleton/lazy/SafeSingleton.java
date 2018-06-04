package com.core.chan.pattern.singleton.lazy;

public class SafeSingleton {
	private static SafeSingleton safeSingleton;
	private SafeSingleton(){}

	public static synchronized SafeSingleton getSingleton(){
		safeSingleton = null == safeSingleton ? new SafeSingleton() : safeSingleton;
		return safeSingleton;
	}
}
