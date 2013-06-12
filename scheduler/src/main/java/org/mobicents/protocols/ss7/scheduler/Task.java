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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Scheduling task.
 *
 * @author kulikov
 */
public abstract class Task implements Runnable {
    protected Scheduler scheduler;

    private volatile boolean isActive = true;
    private volatile boolean isHeartbeat = true;

    private final Object LOCK = new Object();

    private AtomicBoolean inQueue0 = new AtomicBoolean(false);
    private AtomicBoolean inQueue1 = new AtomicBoolean(false);

    public Task(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler scheduler() {
        return this.scheduler;
    }

    public void storedInQueue0() {
        inQueue0.set(true);
    }

    public void storedInQueue1() {
        inQueue1.set(true);
    }

    public void removeFromQueue0() {
        inQueue0.set(false);
    }

    public void removeFromQueue1() {
        inQueue1.set(false);
    }

    public Boolean isInQueue0() {
        return inQueue0.get();
    }

    public Boolean isInQueue1() {
        return inQueue1.get();
    }

    /**
     * Current queue of this task.
     *
     * @return the value of queue
     */
    public abstract int getQueueNumber();

    /**
     * Executes task.
     *
     * @return dead line of next execution
     */
    public abstract long perform();

    /**
     * Cancels task execution
     */
    public void cancel() {
        synchronized (LOCK) {
            this.isActive = false;
            // no needs to remove , since it will not run anyway.
            // otherwise search on 2 queues is required

            /*
             * if(isHeartbeat) scheduler.heartBeatQueue.remove(this); else scheduler.taskQueues[getQueueNumber()].remove(this);
             */
        }
    }

    // call should not be synchronized since can run only once in queue cycle
    public void run() {
        if (this.isActive) {
            try {
                perform();

                // submit next partition
                // if (chain != null) chain.continueExecution();
            } catch (Exception e) {

            }
        }

        scheduler.notifyCompletion();
    }

    protected void activate(Boolean isHeartbeat) {
        synchronized (LOCK) {
            this.isActive = true;
            this.isHeartbeat = isHeartbeat;
        }
    }

    public boolean isActive() {
        return this.isActive;
    }
}
