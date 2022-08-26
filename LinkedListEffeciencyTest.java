package com.wenzhoong.edition_5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class LinkedListEffeciencyTest {

	public static void main(String[] args) {
		SynLinkedList<Integer> intList1 = new SynLinkedList<>();	
		LockFreeLinkedList<Integer> intList2 = new LockFreeLinkedList<>();

		try {
			for(int i = 0; i<11; i++) {
				System.out.println(addTest(intList2,1000000, 7, 4));
				System.gc();
				intList1.empty();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 50 x 10
	public static <E> List<Long> mainAddTest(SafeLinkedList<E> list, E element) throws InterruptedException {
		List<Long> data = new ArrayList<>(10000);
		
		for(int i = 10000; i<100000; i += 5000) {
			for(int j = 0; j<5; j++) {
				data.add(addTest(list, 100000, element, 5));
				list.empty();
			}
		}
		
	return data;
	}
	
	public static <E> long addTest(SafeLinkedList<E> list, int numOfE, E e, int numOfT) throws InterruptedException {
		SafeLinkedList<E> temp = list;
		CountDownLatch count = new CountDownLatch(numOfT);
		long start = System.currentTimeMillis();

		for (int i = 0; i < numOfT; i++)
			new Thread(() -> {
				for (int j = 0; j < numOfE; j++)
					temp.push(e);
				count.countDown();
			}).start();

		count.await();
		long end = System.currentTimeMillis();
		return end - start;
	}

	public static <E> long addAndSearchTest(SafeLinkedList<E> list, int numOfE, E e, E[] arr) throws InterruptedException {
		SafeLinkedList<E> temp = list;
		int length = arr.length;
		CountDownLatch count = new CountDownLatch(length + 10);
		long start = System.currentTimeMillis();
		
		for(int i = 0; i<10; i++)//线程数待定
			new Thread(()->{
				for(int j = 0; j<numOfE; j++) temp.push(e);
				count.countDown();
 			}).start();
		
		for(int i = 0; i<length; i++) {
			final int index = i;
			new Thread(()->{
				temp.search(arr[index]);
				count.countDown();
 			}).start();}
		
		count.await();
		long end = System.currentTimeMillis();
		return end - start;
	}
}
