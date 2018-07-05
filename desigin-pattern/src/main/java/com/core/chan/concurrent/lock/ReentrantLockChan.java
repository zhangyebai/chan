package com.core.chan.concurrent.lock;

import com.core.chan.concurrent.bean.IntWrapper;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class ReentrantLockChan {

	public static void main(String[] args) {
		//test();
		final Lock lock = new ReentrantLock(true);
		lock.lock();
		Thread thread = interrupt(lock);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// thread.interrupt();
		//lock.unlock();
		try {
			//thread.interrupt();
			System.out.println(((ReentrantLock) lock).hasQueuedThreads());
			thread.interrupt();
			thread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 注意 <code>lock.lock();</code>和<code>lock.lockInterruptibly();</code>产生的不同效果
	 * 证明了可重入锁可以比synchronized更直接的响应中断(通过lockInterruptibly实现)
	 * 不同点:
	 * 		1、lock在响应中断时必须先得到锁, 然后在响应中断,否在一直在等待锁
	 * 		2、lockInterruptibly可以在得不到锁的情况下响应中断
	 *
	 * */
	public static Thread interrupt(final Lock lock){
		Thread thread = new Thread(()->{
			try {
				System.out.println("sub thread prepare to get lock");
				//lock.lockInterruptibly();
				lock.lock();
				System.out.println("sub thread get lock");
				for(;;){
					Thread.sleep(20);
				}
			} catch (InterruptedException ignored) {
				System.out.println("响应中断消息" + ignored.getMessage());
			}finally {
				if(((ReentrantLock) lock).isHeldByCurrentThread()) {
					lock.unlock();
					System.out.println("释放锁");
				}
			}
		});
		thread.start();
		return thread;
	}


	public static void test(){
		final IntWrapper count = new IntWrapper(0);
		Lock lock = new ReentrantLock(true);
		ReadWriteLock rwLock = new ReentrantReadWriteLock(true);
		IntStream.range(0, 10000).parallel().forEach(index->{
			Thread thread = new Thread(()->{
				try {
					Thread.sleep(new Random().nextInt(1));
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
			}catch (InterruptedException ignored){

			}
		});
		System.out.println(count.getValue());


		final IntWrapper unsafe = new IntWrapper(0);
		IntStream.range(0, 10000).parallel().forEach(index->{
			Thread thread = new Thread(()->unsafe.setValue(unsafe.getValue() + 1));
			thread.start();
			try {
				thread.join();
			}catch (InterruptedException ignored){

			}

		});
		System.out.println("unsafe value == " + unsafe.getValue());
	}
}
