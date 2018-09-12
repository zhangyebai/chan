package com.core.chan.concurrent.sync;

import com.core.chan.concurrent.bean.IntWrapper;
import com.core.chan.concurrent.bean.LongWrapper;

import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;


/**
 * 等差数列的前N项和, 3种计算方法
 * 1、单线程直接累加
 * 2、线程池分片累加
 * 3、CountDownLatch分片加锁累加
 *
 *
 * */
public class CountDownLatchChan {

	public static void main(String[] args) {
		IntStream.range(0, 1).forEach(index->test());
	}

	/**
	 * 看了结果,眼泪掉下来
	 * 不过在不加锁的多线程任务下, 比单线程还是快很多的
	 * */
	public static void test(){
		System.out.println("---------------------------------------");
		final int total = 100_000;
		final int slice = 2000;
		long start = System.nanoTime();
		long count = 0;
		for(int index = 0; index < total; ++index){
			count += index;
		}
		long end = System.nanoTime();
		System.out.println("tradition count == " + count + " nano time == " + (end - start));




		final ExecutorService executorService = Executors.newFixedThreadPool(4);
		start = System.nanoTime();
		long sumCount = IntStream.range(0, 50).parallel()
				.mapToObj(index->executorService.submit(() -> {
					long log = 0;
					for (int i = index * slice, size = (index + 1) * slice; i < size; ++i){
						log += i;
					}
					return log;
					})
				).mapToLong(task-> {
					try {
						return task.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					return 0;
				}).reduce(Long::sum).orElse(0);
			//reduce(0, (sum, value)-> sum + value);
		end = System.nanoTime();
		System.out.println("Thread Pool count == " + sumCount + " nano time == " + (end - start));


		final CountDownLatch whistle = new CountDownLatch(1);
		final CountDownLatch runner = new CountDownLatch(50);
		final LongWrapper longWrapper = new LongWrapper(0);
		Lock lock = new ReentrantLock(true);
		start = System.nanoTime();
		IntStream.range(0, 50).parallel().forEach(loop ->
			new Thread(()->{
				try {
					whistle.await();
					long sum = 0;
					for(int i = loop * slice, size = (loop + 1) * slice; i < size; ++i){
						sum += i;
					}
					lock.lock();
					longWrapper.setValue(longWrapper.getValue() + sum);

				} catch (InterruptedException e) {
					e.printStackTrace();
				}finally {
					runner.countDown();
					lock.unlock();
				}
			}).start()
		);
		end = System.nanoTime();
		System.out.println("lambda展开时间: nano time == " + (end - start));

		start = System.nanoTime();
		whistle.countDown();
		try {
			runner.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			System.out.println("等待的线程数目前数量: " + runner.getCount());
		}
		end = System.nanoTime();
		System.out.println("new count == " + longWrapper.getValue() + " nano time == " + (end - start));

		executorService.shutdown();
		System.out.println("---------------------------------------");
	}
}
