package com.core.chan.pattern.singleton;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class SafeSingleton {
	private static SafeSingleton safeSingleton;
	private SafeSingleton(){}

	public static synchronized SafeSingleton getSingleton(){
		safeSingleton = null == safeSingleton ? new SafeSingleton() : safeSingleton;
		return safeSingleton;
	}


	public static void test(){
		ConcurrentHashMap<SafeSingleton, String> concurrentHashMap = new ConcurrentHashMap<>(32);
		IntStream.range(0, 100000).parallel().forEach(index->{
			Thread thread = new Thread(()->concurrentHashMap.put(SafeSingleton.getSingleton(),
					LocalDateTime.now().toString()));
			thread.setName("sub thread : " + String.valueOf(index));
			thread.start();
			try {
				thread.join();
			}catch (InterruptedException ex){
				System.out.println(ex.getMessage());
			}
		});

		long start = System.nanoTime();
		IntStream.range(0, 100000).forEach(index->SafeSingleton.getSingleton());
		long end = System.nanoTime();
		System.out.println(concurrentHashMap.size());
		System.out.println(concurrentHashMap);
		System.out.println(end - start);
	}
}
