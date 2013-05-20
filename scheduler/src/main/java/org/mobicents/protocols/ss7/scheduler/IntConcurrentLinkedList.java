/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.scheduler;

import java.util.Iterator;

/**
 *
 * @author oifa yulian
 */
public class IntConcurrentLinkedList<E> {
    // main list
    private Node<E> head = new Node<E>(-1, null);
    private Node<E> tail = new Node<E>(-1, null);

    // cached garbage items
    private Node<E> cacheHead = new Node<E>(-1, null);
    private Node<E> cacheTail = new Node<E>(-1, null);

    private Node<E> tempNode;
    private Object searchNode;

    private Integer size = 0;
    private Integer cacheSize = 0;

    private ListIterator iterator = new ListIterator();

    private Lock lock = new Lock();

    public IntConcurrentLinkedList() {
        head.next = tail;
        tail.previous = head;

        cacheHead.next = cacheTail;
        cacheTail.previous = cacheHead;
    }

    public boolean contains(int key) {
        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && key != tempNode.key)
            tempNode = tempNode.next;

        if (tempNode.element == null) {
            releaseAccess();
            return true;
        }

        releaseAccess();
        return false;
    }

    public boolean add(E value, int key) {
        if (value == null)
            return false;

        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && key != tempNode.key)
            tempNode = tempNode.next;

        if (tempNode.element != null) {
            releaseAccess();
            return false;
        }

        if (cacheSize == 0)
            // need new node
            tempNode = new Node(key, value);
        else {
            // obtain node from cache decrease cache size
            tempNode = cacheHead.next;
            cacheHead.next = tempNode.next;
            cacheHead.next.previous = cacheHead;

            tempNode.key = key;
            tempNode.element = value;
            cacheSize--;
        }

        // connect to main queue
        tail.previous.next = tempNode;
        tempNode.previous = tail.previous;

        tempNode.next = tail;
        tail.previous = tempNode;
        size++;

        releaseAccess();
        return true;
    }

    public boolean offer(E value, int key) {
        if (value == null)
            return false;

        aquireAccess();

        if (cacheSize == 0)
            // need new node
            tempNode = new Node(key, value);
        else {
            // obtain node from cache decrease cache size
            tempNode = cacheHead.next;
            cacheHead.next = tempNode.next;
            cacheHead.next.previous = cacheHead;

            tempNode.key = key;
            tempNode.element = value;
            cacheSize--;
        }

        // connect to main queue
        tail.previous.next = tempNode;
        tempNode.previous = tail.previous;

        tempNode.next = tail;
        tail.previous = tempNode;
        size++;

        releaseAccess();
        return true;
    }

    public E get(int key) {
        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && key != tempNode.key)
            tempNode = tempNode.next;

        E result = tempNode.element;
        releaseAccess();
        return result;
    }

    public E remove(int key) {
        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && key != tempNode.key)
            tempNode = tempNode.next;

        if (tempNode.element == null) {
            releaseAccess();
            return null;
        }

        tempNode.previous.next = tempNode.next;
        tempNode.next.previous = tempNode.previous;
        size--;

        // return to cache
        tempNode.next = cacheHead.next;
        cacheHead.next = tempNode;

        tempNode.previous = cacheHead;
        tempNode.next.previous = tempNode;

        E result = tempNode.element;
        releaseAccess();
        return result;
    }

    public void clear() {
        aquireAccess();

        if (size > 0) {
            cacheSize += size;
            size = 0;

            tail.previous.next = cacheHead.next;
            cacheHead.next.previous = tail.previous;

            cacheHead.next = head.next;
            head.next.previous = cacheHead;

            head.next = tail;
            tail.previous = head;
        }

        releaseAccess();
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Iterator<E> iterator() {
        iterator.currNode = head;
        return iterator;
    }

    private void aquireAccess() {
        try {
            lock.lock();
        } catch (InterruptedException e) {

        }
    }

    private void releaseAccess() {
        lock.unlock();
    }

    private class Node<E> {
        int key;
        E element;
        Node<E> next;
        Node<E> previous;

        Node(int key, E element) {
            this.key = key;
            this.element = element;
        }
    }

    private class ListIterator implements Iterator<E> {
        private Node<E> currNode;

        public ListIterator() {
            this.currNode = head;
        }

        public boolean hasNext() {
            return currNode.next.element != null;
        }

        public E next() {
            aquireAccess();

            E result = currNode.next.element;
            currNode = currNode.next;

            releaseAccess();
            return result;
        }

        public void remove() {
            aquireAccess();

            tempNode = currNode;
            currNode = currNode.next;

            tempNode.previous.next = tempNode.next;
            tempNode.next.previous = tempNode.previous;
            size--;

            // return to cache
            tempNode.next = cacheHead.next;
            cacheHead.next = tempNode;

            tempNode.previous = cacheHead;
            tempNode.next.previous = tempNode;

            releaseAccess();
        }
    }

    private class Lock {
        protected boolean locked;

        public Lock() {
            locked = false;
        }

        public synchronized void lock() throws InterruptedException {
            while (locked)
                wait();
            locked = true;
        }

        public synchronized void unlock() {
            locked = false;
            notify();
        }
    }
}
