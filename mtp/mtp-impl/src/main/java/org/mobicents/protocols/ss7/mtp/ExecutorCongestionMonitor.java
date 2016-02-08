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

import java.util.Date;
import java.util.concurrent.ExecutorService;

import org.mobicents.ss7.congestion.BaseCongestionMonitor;
import org.mobicents.ss7.congestion.CongestionTicketImpl;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ExecutorCongestionMonitor extends BaseCongestionMonitor {
    private static final String SOURCE = "EXECUTOR_";

    private String productName;
    private String source;
    private int currentAlarmLevel = 0;
    private ExecutorService[] executors;

    private double[] delayThreshold = new double[] { 1, 6, 12 };
    private double[] backToNormalMemoryThreshold = new double[] { 0.5, 3, 8 };

    public ExecutorCongestionMonitor(String productName, ExecutorService[] executors) {
        this.productName = productName;
        this.executors = executors;

        this.source = SOURCE + productName;
    }

    @Override
    public void monitor() {
        TestMonitor testMonitor = new TestMonitor();
        testMonitor.startMonitor();
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    protected CongestionTicketImpl generateTicket() {
        return new CongestionTicketImpl(this.source, currentAlarmLevel);
    }

    @Override
    protected int getAlarmLevel() {
        return currentAlarmLevel;
    }

    @Override
    protected void setAlarmLevel(int val) {
        currentAlarmLevel = val;
    }

    private void registerResults(double maxValue) {
        // ..................
        // this.logger.error("*****************: " + maxValue);
        // ..................

        super.applyNewValue(currentAlarmLevel, maxValue, delayThreshold, backToNormalMemoryThreshold, true);
    }

    public class TestMonitor {
        private ExecutorTestMonitor[] monitorList;
        private int cnt;
        private int finished = 0;

        public void startMonitor() {
            cnt = executors.length;
            monitorList = new ExecutorTestMonitor[cnt];
            for (int i1 = 0; i1 < cnt; i1++) {
                ExecutorService ex = executors[i1];
                monitorList[i1] = new ExecutorTestMonitor(this);
                ex.execute(monitorList[i1]);
            }
        }

        public synchronized void monitor() {
            if (++finished >= cnt - 1) {
                double maxValue = 0;
                for (ExecutorTestMonitor testMonitor : monitorList) {
                    if (testMonitor != null) {
                        if (maxValue < testMonitor.getDelay())
                            maxValue = testMonitor.getDelay();
                    }
                }
                registerResults(maxValue);
            }
        }
    }

    public class ExecutorTestMonitor implements Runnable {
        private TestMonitor testMonitor;
        private long startTime;
        private double delay;
        private boolean finished = false;

        public ExecutorTestMonitor(TestMonitor testMonitor) {
            this.testMonitor = testMonitor;
            this.startTime = (new Date()).getTime();
        }

        @Override
        public void run() {
            long endTime = (new Date()).getTime();
            delay = ((double) endTime - startTime) / 1000;
            finished = true;
            testMonitor.monitor();
        }

        public double getDelay() {
            return delay;
        }

        public boolean getFinished() {
            return finished;
        }
    }
}
