package com.core.chan.algorithm.sort;


public class SimpleSelectSort {
	public static void sort(int[] src){
		final int length = src.length;
		if(length <= 0){
			return;
		}

		// length - 1是因为 2个数字比较一次就行, 3个数字比较两次就行,
		// 使用的是最大值比较法
		for(int index = 0; index < length - 1; ++index){
			int max = length - index - 1;
			for(int cell = 0; cell < length - index; ++cell){
				if(src[cell] > src[max]){
					max = cell;
				}
			}
			if(max != length - 1){
				int swap = src[length - index - 1];
				src[length - index - 1] = src[max];
				src[max] = swap;
			}
		}
	}
}
