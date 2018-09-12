package com.core.chan.magic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListModifyException {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Integer v = (Integer) iterator.next();
			if (v == 2) {
				list.remove(v);
				System.out.println("removed 2");
				// break; 可以看到异常发生在下一次迭代的时候
			} else {
				System.out.println(v);
			}
		}
	}
}
