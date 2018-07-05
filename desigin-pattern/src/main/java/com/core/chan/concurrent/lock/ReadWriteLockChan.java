package com.core.chan.concurrent.lock;

import com.core.chan.concurrent.bean.LongWrapper;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class ReadWriteLockChan {
	public static final LongWrapper LONG_WRAPPER = new LongWrapper(0);
	public static final ReadWriteLock READ_WRITE_LOCK = new ReentrantReadWriteLock(true);

	public static void main(String[] args) {
		IntStream.range(0, 5).forEach(index->new Thread(()->{
			for(;;){
				System.out.println(read() + " -- THREAD ID == " + Thread.currentThread().getId());
				try {
					Thread.sleep(new Random().nextInt(10));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start());

		IntStream.range(0, 2).forEach(index->new Thread(()->{
			for(;;) {
				write(new Random().nextInt(1000));
				try {
					Thread.sleep(new Random().nextInt(200));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start());
	}

	public static long read(){
		READ_WRITE_LOCK.readLock().lock();
		try {
			return LONG_WRAPPER.getValue();
		}finally {
			READ_WRITE_LOCK.readLock().unlock();
		}

	}

	public static void write(long value){
		READ_WRITE_LOCK.writeLock().lock();
		try {
			LONG_WRAPPER.setValue(value);
		}finally {
			READ_WRITE_LOCK.writeLock().unlock();
		}

	}
}
