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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author oifa yulian
 */
public class ConcurrentLinkedList<E> implements BlockingQueue<E> {
    // main list
    private Node<E> head = new Node<E>(null);
    private Node<E> tail = new Node<E>(null);

    // cached garbage items
    private Node<E> cacheHead = new Node<E>(null);
    private Node<E> cacheTail = new Node<E>(null);

    private Node<E> tempNode;
    private Object searchNode;

    private Integer size = 0;
    private Integer cacheSize = 0;

    // notifiers
    private Notifier notifierHead = new Notifier();
    private Notifier notifierTail = new Notifier();

    // cached garbage items
    private Notifier notifierCacheHead = new Notifier();
    private Notifier notifierCacheTail = new Notifier();

    private Integer notifierSize = 0;
    private Integer cacheNotifierSize = 0;

    private ListIterator iterator = new ListIterator();

    private Lock lock = new Lock();

    public ConcurrentLinkedList() {
        head.next = tail;
        tail.previous = head;

        cacheHead.next = cacheTail;
        cacheTail.previous = cacheHead;

        notifierHead.next = notifierTail;
        notifierTail.previous = notifierHead;

        notifierCacheHead.next = notifierCacheTail;
        notifierCacheTail.previous = notifierCacheHead;
    }

    public boolean contains(Object o) {
        if (o == null)
            return false;

        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && !o.equals(tempNode.element))
            tempNode = tempNode.next;

        if (tempNode.element == null) {
            releaseAccess();
            return true;
        }

        releaseAccess();
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        aquireAccess();

        Iterator<? extends Object> e = c.iterator();
        while (e.hasNext()) {
            searchNode = e.next();
            if (searchNode == null) {
                releaseAccess();
                return false;
            }

            tempNode = head.next;
            while (tempNode.element != null && !searchNode.equals(tempNode.element))
                tempNode = tempNode.next;

            if (tempNode.element == null) {
                releaseAccess();
                return false;
            }
        }

        releaseAccess();
        return true;
    }

    public boolean add(E value) {
        if (value == null)
            return false;

        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && !value.equals(tempNode.element))
            tempNode = tempNode.next;

        if (tempNode.element != null) {
            releaseAccess();
            return false;
        }

        if (cacheSize == 0)
            // need new node
            tempNode = new Node(value);
        else {
            // obtain node from cache decrease cache size
            tempNode = cacheHead.next;
            cacheHead.next = tempNode.next;
            cacheHead.next.previous = cacheHead;

            tempNode.element = value;
            cacheSize--;
        }

        // connect to main queue
        tail.previous.next = tempNode;
        tempNode.previous = tail.previous;

        tempNode.next = tail;
        tail.previous = tempNode;
        size++;

        if (notifierSize > 0)
            updateNotifier(value);

        releaseAccess();
        return true;
    }

    public boolean addAll(Collection<? extends E> c) {
        Boolean result = false;
        for (E item : c)
            result |= add(item);

        return result;
    }

    public void put(E o) {
        offer(o);
    }

    public boolean offer(E o, long timeout, TimeUnit unit) {
        return offer(o);
    }

    public boolean offer(E value) {
        if (value == null)
            return false;

        aquireAccess();

        if (cacheSize == 0)
            // need new node
            tempNode = new Node(value);
        else {
            // obtain node from cache decrease cache size
            tempNode = cacheHead.next;
            cacheHead.next = tempNode.next;
            cacheHead.next.previous = cacheHead;

            tempNode.element = value;
            cacheSize--;
        }

        // connect to main queue
        tail.previous.next = tempNode;
        tempNode.previous = tail.previous;

        tempNode.next = tail;
        tail.previous = tempNode;
        size++;

        if (notifierSize > 0)
            updateNotifier(value);

        releaseAccess();
        return true;
    }

    public E peek() {
        aquireAccess();

        E currValue = head.next.element;

        releaseAccess();
        return currValue;
    }

    public E element() throws NoSuchElementException {
        aquireAccess();

        if (size == 0) {
            releaseAccess();
            throw new NoSuchElementException();
        }

        E currValue = head.next.element;

        releaseAccess();
        return currValue;
    }

    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            a = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);

        aquireAccess();

        tempNode = head.next;
        int i = 0;
        Object[] result = a;

        while (tempNode.element != null && i < a.length) {
            result[i++] = tempNode.element;
            tempNode = tempNode.next;
        }

        releaseAccess();

        while (i < a.length)
            result[i] = null;

        return a;
    }

    public Object[] toArray() {
        aquireAccess();

        Object[] result = new Object[size];
        tempNode = head.next;
        int i = 0;

        while (i < size) {
            result[i++] = tempNode.element;
            tempNode = tempNode.next;
        }

        releaseAccess();
        return result;
    }

    public E poll() {
        aquireAccess();

        if (size == 0) {
            releaseAccess();
            return null;
        }

        // disconnect from main list
        tempNode = head.next;
        head.next = tempNode.next;
        head.next.previous = head;

        // connect to cache list
        tempNode.next = cacheHead.next;
        cacheHead.next = tempNode;

        tempNode.previous = cacheHead;
        tempNode.next.previous = tempNode;

        // update counters
        E currValue = tempNode.element;
        cacheSize++;
        size--;

        releaseAccess();
        return currValue;
    }

    public E take() {
        aquireAccess();

        if (size == 0) {
            Notifier<E> tempNotifier;
            if (cacheNotifierSize == 0)
                // need new node
                tempNotifier = new Notifier();
            else {
                // obtain node from cache decrease cache size
                tempNotifier = notifierCacheHead.next;
                notifierCacheHead.next = tempNotifier.next;
                notifierCacheHead.next.previous = notifierCacheHead;

                cacheNotifierSize--;
            }

            // store it in notifiers list
            // connect to main queue
            notifierTail.previous.next = tempNotifier;
            tempNotifier.previous = notifierTail.previous;

            tempNotifier.next = notifierTail;
            notifierTail.previous = tempNotifier;
            notifierSize++;

            while (size == 0) {
                // release lock so others will be able to add nodes and wait
                releaseAccess();

                synchronized (tempNotifier.lock) {
                    try {
                        tempNotifier.lock.wait();
                    } catch (InterruptedException e) {

                    }
                }

                aquireAccess();

                if (size != 0) {
                    // take back notifier
                    tempNotifier.previous.next = tempNotifier.next;
                    tempNotifier.next.previous = tempNotifier.previous;

                    tempNotifier.next = notifierCacheHead.next;
                    notifierCacheHead.next.previous = tempNotifier;
                    notifierCacheHead.next = tempNotifier;
                    tempNotifier.previous = notifierCacheHead;
                    cacheNotifierSize++;
                }
            }
        }

        // disconnect from main list
        tempNode = head.next;
        head.next = tempNode.next;
        head.next.previous = head;

        // connect to cache list
        tempNode.next = cacheHead.next;
        cacheHead.next = tempNode;

        tempNode.previous = cacheHead;
        tempNode.next.previous = tempNode;

        // update counters
        E currValue = tempNode.element;
        cacheSize++;
        size--;

        releaseAccess();
        return currValue;
    }

    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        aquireAccess();

        if (size == 0) {
            Notifier<E> tempNotifier;
            if (cacheNotifierSize == 0)
                // need new node
                tempNotifier = new Notifier();
            else {
                // obtain node from cache decrease cache size
                tempNotifier = notifierCacheHead.next;
                notifierCacheHead.next = tempNotifier.next;
                notifierCacheHead.next.previous = notifierCacheHead;

                cacheNotifierSize--;
            }

            // store it in notifiers list
            // connect to main queue
            notifierTail.previous.next = tempNotifier;
            tempNotifier.previous = notifierTail.previous;

            tempNotifier.next = notifierTail;
            notifierTail.previous = tempNotifier;
            notifierSize++;

            while (size == 0) {
                // release lock so others will be able to add nodes and wait
                releaseAccess();

                synchronized (tempNotifier.lock) {
                    try {
                        switch (unit) {
                            case MICROSECONDS:
                                tempNotifier.lock.wait(timeout / 1000L, (int) ((timeout * 1000L) % 1000000L));
                                break;
                            case MILLISECONDS:
                                tempNotifier.lock.wait(timeout);
                                break;
                            case NANOSECONDS:
                                tempNotifier.lock.wait(timeout / 1000000L, (int) (timeout % 1000000L));
                                break;
                            case SECONDS:
                                tempNotifier.lock.wait(timeout * 1000L);
                                break;
                        }
                    } catch (InterruptedException e) {

                    }
                }

                aquireAccess();

                if (size == 0) {
                    releaseAccess();
                    throw new InterruptedException();
                }
            }

            // take back notifier
            tempNotifier.previous.next = tempNotifier.next;
            tempNotifier.next.previous = tempNotifier.previous;

            tempNotifier.next = notifierCacheHead.next;
            notifierCacheHead.next.previous = tempNotifier;
            notifierCacheHead.next = tempNotifier;
            tempNotifier.previous = notifierCacheHead;
            cacheNotifierSize++;
        }

        // disconnect from main list
        tempNode = head.next;
        head.next = tempNode.next;
        head.next.previous = head;

        // connect to cache list
        tempNode.next = cacheHead.next;
        cacheHead.next = tempNode;

        tempNode.previous = cacheHead;
        tempNode.next.previous = tempNode;

        // update counters
        E currValue = tempNode.element;
        cacheSize++;
        size--;

        releaseAccess();
        return currValue;
    }

    public E remove() throws NoSuchElementException {
        aquireAccess();

        if (size == 0) {
            releaseAccess();
            throw new NoSuchElementException();
        }

        // disconnect from main list
        tempNode = head.next;
        head.next = tempNode.next;
        head.next.previous = head;

        // connect to cache list
        tempNode.next = cacheHead.next;
        cacheHead.next = tempNode;

        tempNode.previous = cacheHead;
        tempNode.next.previous = tempNode;

        // update counters
        E currValue = tempNode.element;
        cacheSize++;
        size--;

        releaseAccess();
        return currValue;
    }

    public boolean remove(Object o) {
        aquireAccess();

        tempNode = head.next;
        while (tempNode.element != null && !o.equals(tempNode.element))
            tempNode = tempNode.next;

        if (tempNode.element == null) {
            releaseAccess();
            return false;
        }

        tempNode.previous.next = tempNode.next;
        tempNode.next.previous = tempNode.previous;
        size--;

        // return to cache
        tempNode.next = cacheHead.next;
        cacheHead.next = tempNode;

        tempNode.previous = cacheHead;
        tempNode.next.previous = tempNode;

        releaseAccess();
        return true;
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

    public int drainTo(Collection<? super E> c) {
        if (size == 0)
            return 0;

        if (c == null)
            throw new NullPointerException();

        if (c.equals(this))
            throw new IllegalArgumentException();

        aquireAccess();

        Node<E> tempFirst = head.next;
        Node<E> tempLast = tail.previous;
        int tempCount = size;

        head.next = tail;
        tail.previous = head;
        size = 0;

        releaseAccess();

        Node<E> tempNode = tempFirst;
        for (int i = tempCount; i > 0; i--, tempNode = tempNode.next)
            c.add(tempNode.element);

        aquireAccess();

        if (tempCount > 0) {
            cacheSize += tempCount;

            tempLast.next = cacheHead.next;
            cacheHead.next.previous = tempLast;

            cacheHead.next = tempFirst;
            tempFirst.previous = cacheHead;
        }

        releaseAccess();
        return tempCount;
    }

    public int drainTo(Collection<? super E> c, int maxElements) {
        if (size == 0)
            return 0;

        if (c == null)
            throw new NullPointerException();

        if (c.equals(this))
            throw new IllegalArgumentException();

        aquireAccess();

        tempNode = head.next;
        int count = 0;
        while (tempNode.element != null && count < maxElements) {
            c.add(tempNode.element);
            count++;
            tempNode = tempNode.next;
        }

        size -= count;
        if (count > 0) {
            cacheSize += count;

            Node<E> firstNode = head.next;
            head.next = tempNode.next;
            head.next.previous = head;

            tempNode.next = cacheHead.next;
            cacheHead.next.previous = tempNode;

            cacheHead.next = firstNode;
            firstNode.previous = cacheHead;
        }

        releaseAccess();
        return count;
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

    public int remainingCapacity() {
        return Integer.MAX_VALUE;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    private void updateNotifier(E newElement) {
        if (notifierSize == 0)
            return;

        notifierSize--;
        Notifier tempNotifier = notifierHead.next;
        synchronized (tempNotifier.lock) {
            tempNotifier.lock.notify();
        }
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

    private class Notifier<E> {
        private Object lock = new Object();
        Notifier next;
        Notifier previous;
    }

    private class Node<E> {
        E element;
        Node<E> next;
        Node<E> previous;

        Node(E element) {
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
