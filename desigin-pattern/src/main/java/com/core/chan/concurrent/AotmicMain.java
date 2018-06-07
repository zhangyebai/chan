package com.core.chan.concurrent;

import com.core.chan.concurrent.condition.ConditionChan;


public class AotmicMain {
	public static void main(String[] args) {
		ConditionChan.test();
	}

	public static void test(){
		String x = "123";
		String text = "a" + "b" + "c";
		System.out.println(text);
	}
}

