package com.core.chan.concurrent.condition;

import com.core.chan.concurrent.bean.IntWrapper;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 通过ReentrantLock 和 Condition 让两个子线程奇偶交替增加target number
 *
 * */

public class ConditionChan {
	public static final int LOOP = 10;

	public static void test(){
		final Lock lock = new ReentrantLock(false);
		final Condition odd = lock.newCondition();
		final Condition even = lock.newCondition();
		final IntWrapper intWrapper = new IntWrapper(0);

		/**
		 * step 1 : get lock
		 * step 2 : execute condition, go into notify or await
		 * step 3 : release lock
		 *
		 * step 4 : break condition
		 * step 5 : wake up each other and exit sub-thread
		 *
		 * */

		Thread oddThread = new Thread(()->{
			for(int index = 0; index < LOOP; ++index){
				lock.lock();
				try{
					if (intWrapper.getValue() % 2 == 0) {
						intWrapper.setValue(intWrapper.getValue() + 1);
						System.out.println(intWrapper.getValue() + "---------" + Thread.currentThread().getName());
						even.signal();
					} else {
						odd.await();
					}

				}catch (InterruptedException ex){

				}finally {
					lock.unlock();
				}
			}
			lock.lock();
			try {
				even.signalAll();
			}finally {
				lock.unlock();
			}

		});
		Thread evenThread = new Thread(()->{
			for(int index = 0; index < LOOP; ++index){
				lock.lock();
				try{
					if (intWrapper.getValue() % 2 == 1) {
						intWrapper.setValue(intWrapper.getValue() + 1);
						System.out.println(intWrapper.getValue() + "---------" + Thread.currentThread().getName());
						odd.signal();
					} else {
						even.await();
					}
				}catch (InterruptedException ex){

				}finally {
					lock.unlock();
				}
			}
			lock.lock();
			try {
				odd.signalAll();
			}finally {
				lock.unlock();
			}
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
