package com.wenzhoong.edition_5;

import java.util.concurrent.atomic.AtomicReference;


public class LockFreeLinkedList<E> implements SafeLinkedList<E> {
	
	private AtomicReference<Node<E>> head;
	
	public LockFreeLinkedList() {
		this.head = new AtomicReference<Node<E>>();
	}
	
	class Node<E> {
		private Node<E> next;
		private E value;
		
		Node(E value, Node<E> next){
			this.value = value;
			this.next= next;
		}
		
		Node(E value){
			this.value = value;
		}
		
		public E getValue() {
			return value;
		}
		
		public void setValue(E e) {
			this.value = e;
		}
		
		public Node<E> getNext() {
			return this.next;
		}
		
		public void setNext(Node<E> node) {
			this.next = node;
		}
		
	}
	
	//add element at the head of the list.
	public void push(E e) {
		Node<E> newNode = new Node<>(e);
		Node<E> temp;
		do {
			temp = head.get();
			newNode.setNext(temp);
//			System.out.println(Thread.currentThread().getName());
		} while(!head.compareAndSet(temp, newNode));
	}
	
	public int numOf() {
		Node<E> temp = head.get();
		int count = 0;
		while (temp != null) {
			count++;
			temp = temp.next;
		}
		return count;
	}
	
	public Node<E> pop() {
		Node<E> newNode;
		Node<E> temp;
		if(head.get() == null) return null;
		do {
			temp = head.get();
			newNode = temp.next;
		} while(!head.compareAndSet(temp, newNode));
		return temp;
	}

	public boolean search(E e) {
		Node<E> temp = head.get();
		while(temp != null) {
			if(temp.value.equals(e)) return true;
			temp = temp.next;
		}
		return false;
	}
	
	public void empty() {
		head.set(null);
	}

}
