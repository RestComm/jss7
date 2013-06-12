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

/**
 *
 * @author oifa yulian
 */
public class IntConcurrentHashMap<E> {
    private IntConcurrentLinkedList<E>[] lists = new IntConcurrentLinkedList[16];

    public IntConcurrentHashMap() {
        for (int i = 0; i < 16; i++)
            lists[i] = new IntConcurrentLinkedList<E>();
    }

    public boolean contains(int key) {
        return lists[getHash(key)].contains(key);
    }

    public boolean add(E value, int key) {
        if (value == null)
            return false;

        lists[getHash(key)].add(value, key);
        return true;
    }

    public boolean offer(E value, int key) {
        if (value == null)
            return false;

        lists[getHash(key)].offer(value, key);
        return true;
    }

    public E get(int key) {
        return lists[getHash(key)].get(key);
    }

    public E remove(int key) {
        return lists[getHash(key)].remove(key);
    }

    private int getHash(int key) {
        // 4 bits allows 16 values
        // use bits 1,2 and 6,7
        return key & 0x3 + (key >> 3) & 0xC;
    }
}
