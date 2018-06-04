package com.core.chan;

import com.core.chan.pattern.singleton.lazy.SafeSingleton;
import com.core.chan.pattern.singleton.lazy.Singleton;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class App {

	public static void main(String[] args) {

		/* *
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
		 * */



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
