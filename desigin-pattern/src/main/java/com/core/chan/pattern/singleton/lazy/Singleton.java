package com.core.chan.pattern.singleton.lazy;


import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class Singleton {
	private static Singleton singleton;
	private Singleton(){}

	public static Singleton getSingleton(){
		singleton = null == singleton ? new Singleton() : singleton;
		return singleton;
	}

	public static void test(){
		ConcurrentHashMap<Singleton, String> concurrentHashMap = new ConcurrentHashMap<>(32);

		IntStream.range(0, 100000).parallel().forEach(index->{
			Thread thread = new Thread(()->concurrentHashMap.put(Singleton.getSingleton(),
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
		IntStream.range(0, 100000).forEach(index->Singleton.getSingleton());
		long end = System.nanoTime();
		System.out.println(concurrentHashMap.size());
		System.out.println(concurrentHashMap);
		System.out.println(end - start);
	}
}
