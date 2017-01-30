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

import org.apache.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implements scheduler with multi-level priority queue.
 *
 * This scheduler implementation follows to uniprocessor model with "super" thread. The "super" thread includes IO bound thread
 * and one or more CPU bound threads with equal priorities.
 *
 * The actual priority is assigned to task instead of process and can be changed dynamically at runtime using the initial
 * priority level, feedback and other parameters.
 *
 *
 * @author oifa.yulian
 */
public class Scheduler implements SchedulerMBean {
    // MANAGEMENT QUEUE SHOULD CONTAIN ONLY TASKS THAT ARE NOT TIME DEPENDENT
    public static final Integer MANAGEMENT_QUEUE = 0;

    // MTP2/SCTP READ
    public static final Integer L2READ_QUEUE = 1;
    // MTP3/M3UA READ
    public static final Integer L3READ_QUEUE = 2;
    // ISUP / SCCP READ
    public static final Integer L4READ_QUEUE = 3;
    // TCAP READ
    public static final Integer TCAPREAD_QUEUE = 4;
    // MAP/INUP and other APP LAYER READ
    public static final Integer APPREAD_QUEUE = 5;

    // MAP/INUP and other APP LAYER WRITE
    public static final Integer APPWRITE_QUEUE = 6;
    // TCAP WRITE
    public static final Integer TCAPWRITE_QUEUE = 7;
    // ISUP / SCCP WRITE
    public static final Integer L4WRITE_QUEUE = 8;
    // MTP3/M3UA WRITE
    public static final Integer L3WRITE_QUEUE = 9;
    // MTP2/SCTP WRITE
    public static final Integer L2WRITE_QUEUE = 10;

    // INTERNETWORKING OCCURES OVER L3 NO HIGHER LAYERS ARE USED
    // BASICALLY DOEST NOT MATTER WHAT QUEUE WE CHOOSE , IT SHOULD BE BETWEEN L3READ_QUEUE AND L3WRITE_QUEUE
    public static final Integer INTERNETWORKING_QUEUE = 3;

    public static final Integer HEARTBEAT_QUEUE = -1;

    // The clock for time measurement
    private Clock clock;

    // priority queue
    protected OrderedTaskQueue[] taskQueues = new OrderedTaskQueue[11];

    protected OrderedTaskQueue heartBeatQueue;
    // CPU bound threads
    private CpuThread cpuThread;

    // flag indicating state of the scheduler
    private boolean isActive;

    private Logger logger = Logger.getLogger(Scheduler.class);

    /**
     * Creates new instance of scheduler.
     */
    public Scheduler() {
        for (int i = 0; i < taskQueues.length; i++)
            taskQueues[i] = new OrderedTaskQueue();

        heartBeatQueue = new OrderedTaskQueue();

        cpuThread = new CpuThread(String.format("Scheduler"));
    }

    /**
     * Sets clock.
     *
     * @param clock the clock used for time measurement.
     */
    public void setClock(Clock clock) {
        this.clock = clock;
    }

    /**
     * Gets the clock used by this scheduler.
     *
     * @return the clock object.
     */
    public Clock getClock() {
        return clock;
    }

    /**
     * Queues task for execution according to its priority.
     *
     * @param task the task to be executed.
     */
    public void submit(Task task, Integer index) {
        task.activate(false);
        taskQueues[index].accept(task);
    }

    /**
     * Queues task for execution according to its priority.
     *
     * @param task the task to be executed.
     */
    public void submitHeatbeat(Task task) {
        task.activate(true);
        heartBeatQueue.accept(task);
    }

    /**
     * Starts scheduler.
     */
    public void start() {
        if (this.isActive)
            return;

        if (clock == null) {
            throw new IllegalStateException("Clock is not set");
        }

        this.isActive = true;

        logger.info("Starting ");

        cpuThread.activate();

        logger.info("Started ");
    }

    /**
     * Stops scheduler.
     */
    public void stop() {
        if (!this.isActive) {
            return;
        }

        cpuThread.shutdown();

        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
        }

        for (int i = 0; i < taskQueues.length; i++)
            taskQueues[i].clear();

        heartBeatQueue.clear();
    }

    // removed statistics to increase perfomance
    /**
     * Shows the miss rate.
     *
     * @return the miss rate value;
     */
    public double getMissRate() {
        return 0;
    }

    public long getWorstExecutionTime() {
        return 0;
    }

    public void notifyCompletion() {
        cpuThread.notifyCompletion();
    }

    /**
     * Executor thread.
     */
    private class CpuThread extends Thread {
        private volatile boolean active;
        private int currQueue = MANAGEMENT_QUEUE;
        private AtomicInteger activeTasksCount = new AtomicInteger();
        private long cycleStart = 0;
        private int runIndex = 0;
        private ExecutorService eservice;
        private Object LOCK = new Object();

        public CpuThread(String name) {
            super(name);
            int size = Runtime.getRuntime().availableProcessors() * 2;
            eservice = new ThreadPoolExecutor(size, size, 0L, TimeUnit.MILLISECONDS, new ConcurrentLinkedList<Runnable>());
        }

        public void activate() {
            this.active = true;
            this.start();
        }

        public void notifyCompletion() {
            int newValue = activeTasksCount.decrementAndGet();
            if (newValue == 0 && this.active)
                synchronized (LOCK) {
                    LOCK.notify();
                }
        }

        @Override
        public void run() {
            long cycleDuration, cycleDuration2;
            cycleStart = System.nanoTime();

            while (active) {
                while (currQueue <= L2WRITE_QUEUE) {
                    synchronized (LOCK) {
                        if (executeQueue(taskQueues[currQueue]))
                            try {
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                // lets continue
                            }
                    }

                    currQueue++;
                }

                runIndex = (runIndex + 1) % 25;
                if (runIndex == 0) {
                    synchronized (LOCK) {
                        if (executeQueue(heartBeatQueue))
                            try {
                                LOCK.wait();
                            } catch (InterruptedException e) {
                                // lets continue
                            }
                    }
                }

                // sleep till next cycle
                cycleDuration = System.nanoTime() - cycleStart;
                if (cycleDuration < 4000000L) {
                    try {
                        sleep(4L - cycleDuration / 1000000L, (int) ((4000000L - cycleDuration) % 1000000L));
                    } catch (InterruptedException e) {
                        // lets continue
                    }
                }

                // new cycle starts , updating cycle start time by 4ms
                // cycleDuration2=System.nanoTime() - cycleStart;
                cycleStart = cycleStart + 4000000L;
                currQueue = MANAGEMENT_QUEUE;

                // if(cycleDuration2>4100000L)
                // System.out.println("TIME LONGER THEN 4.1MS,DURATION:" + cycleDuration);
                // else if(cycleDuration2<3900000L)
                // System.out.println("TIME SHORTER THEN 3.9MS,DURATION:" + cycleDuration);
            }
        }

        private boolean executeQueue(OrderedTaskQueue currQueue) {
            Task t;
            currQueue.changePool();
            int currQueueSize = currQueue.size();
            activeTasksCount.set(currQueueSize);
            t = currQueue.poll();
            // submit all tasks in current queue
            while (t != null) {
                eservice.execute(t);
                t = currQueue.poll();
            }

            return currQueueSize != 0;
        }

        /**
         * Terminates thread.
         */
        private void shutdown() {
            this.active = false;
        }
    }
}
