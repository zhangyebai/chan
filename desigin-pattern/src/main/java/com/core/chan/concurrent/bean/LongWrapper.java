package com.core.chan.concurrent.bean;

public class LongWrapper {
	private long value;
	public LongWrapper(long value){
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}
}
