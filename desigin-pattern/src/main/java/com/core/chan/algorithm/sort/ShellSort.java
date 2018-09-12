package com.core.chan.algorithm.sort;

public class ShellSort {

	/**
	 * step 1 : 希尔增量[slice, slice, slice ...], 划分数组单元->[其本质是划分数据索引]
	 * step 2 : 单元插入排序, cell
	 * step 3 : goto 2
	 * */
	public static void sort(int[] src){
		final int length = src.length;
		if(length <= 0){
			return;
		}
		/// step 1
		for(int slice = length / 2; slice > 0; slice /= 2){
			/// step 2
			for(int cell = slice; cell < length; ++cell){
				int unit = cell;
				for(; ((unit - slice) >= 0) && (src[unit - slice] > src[unit]);){
					int swap = src[unit - slice];
					src[unit - slice] = src[unit];
					src[unit] = swap;
					unit -= slice;
				}
			}
		}
	}
}
