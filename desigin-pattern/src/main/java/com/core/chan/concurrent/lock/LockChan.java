package com.core.chan.concurrent.lock;

import com.core.chan.concurrent.bean.IntWrapper;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

public class LockChan {

	public static void main(String[] args) {
		test();
	}

	public static void test(){
		final IntWrapper count = new IntWrapper(0);
		Lock lock = new ReentrantLock(true);
		IntStream.range(0, 10).forEach(index->{
			Thread thread = new Thread(()->{
				try {
					Thread.sleep(new Random().nextInt(500));
				}catch (InterruptedException ignored){

				}
				lock.lock();
				try {
					count.setValue(count.getValue() + 1);
				}finally {
					lock.unlock();
				}
			});
			thread.start();
			try {
				thread.join();
			}catch (InterruptedException ex){

			}
		});
		System.out.println(count.getValue());
	}
}
