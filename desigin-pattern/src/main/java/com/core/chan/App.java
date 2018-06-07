package com.core.chan;


public class App {

	public static int value;
	static {
		value = 2;
	}
	public App(){
		System.out.println(value);
	}
	public static void main(String[] args) {
		App app = new App();
		System.out.println(value);
	}
}
