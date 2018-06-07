package com.core.chan.concurrent.condition;

import com.core.chan.concurrent.bean.IntWrapper;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionChan {


	public static void test(){
		final Lock lock = new ReentrantLock(true);
		final Condition odd = lock.newCondition();
		final Condition even = lock.newCondition();
		final IntWrapper intWrapper = new IntWrapper(0);

		Thread oddThread = new Thread(()->{
			for(int index = 0; index < 10; ++index){
				lock.lock();
				try{
					if (intWrapper.getValue() % 2 == 0) {
						intWrapper.setValue(intWrapper.getValue() + 1);
						System.out.println(intWrapper.getValue());
						even.signal();
					} else {
						odd.await();
					}
				}catch (InterruptedException ex){

				}finally {
					lock.unlock();
				}
			}
			// even.signalAll();
		});
		Thread evenThread = new Thread(()->{
			for(int index = 0; index < 10; ++index){
				lock.lock();
				try{
					if (intWrapper.getValue() % 2 == 1) {
						intWrapper.setValue(intWrapper.getValue() + 1);
						System.out.println(intWrapper.getValue());
						odd.signal();
					} else {
						even.await();
					}
				}catch (InterruptedException ex){

				}finally {
					lock.unlock();
				}
			}
			// odd.signalAll();
		});

		oddThread.start();
		evenThread.start();
		try {
			oddThread.join();
			evenThread.join();
		}catch (InterruptedException ex){

		}
	}
}
