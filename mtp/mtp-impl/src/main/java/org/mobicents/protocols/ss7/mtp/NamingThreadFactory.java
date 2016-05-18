/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012.
 * and individual contributors
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

package org.mobicents.protocols.ss7.mtp;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread factory which names threads by "pool-<basename>-thread-n".
 * This is a replacement for Executors.defaultThreadFactory() to be able to identify pools.
 * Optionally a delegate thread factory can be given which creates the Thread
 * object itself, if no delegate has been given, Executors.defaultThreadFactory is used.
 * @author pocsaji.miklos@alerant.hu
 *
 */
public class NamingThreadFactory implements ThreadFactory {

    private ThreadFactory delegate;
    private String baseName;
    private AtomicInteger index;

    public NamingThreadFactory(String baseName) {
        this(baseName, null);
    }

    public NamingThreadFactory(String baseName, ThreadFactory delegate) {
        this.baseName = baseName;
        this.delegate = delegate;
        if (this.delegate == null) {
            this.delegate = Executors.defaultThreadFactory();
        }
        this.index = new AtomicInteger(1);
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = "pool-" + baseName + "-thread-" + index.getAndIncrement();
        Thread ret = delegate.newThread(r);
        ret.setName(name);
        return ret;
    }
}
