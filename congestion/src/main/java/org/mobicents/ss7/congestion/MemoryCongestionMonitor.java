/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.ss7.congestion;

import javolution.util.FastList;

import org.apache.log4j.Logger;

/**
 * @author amit bhayani
 *
 */
public class MemoryCongestionMonitor implements CongestionMonitor {
    private static final Logger logger = Logger.getLogger(MemoryCongestionMonitor.class);

    private static final String SOURCE = "MEMORY";

    private final FastList<CongestionListener> listeners = new FastList<CongestionListener>();

    private double maxMemory;
    private volatile double percentageOfMemoryUsed;

    private volatile boolean memoryTooHigh = false;

    private int backToNormalMemoryThreshold;

    private int memoryThreshold;

    public MemoryCongestionMonitor() {
        maxMemory = Runtime.getRuntime().maxMemory() / (double) 1024;
    }

    /**
     * @param backToNormalPercentageOfMemoryUsed the backToNormalPercentageOfMemoryUsed to set
     */
    public void setBackToNormalMemoryThreshold(int backToNormalMemoryThreshold) {
        this.backToNormalMemoryThreshold = backToNormalMemoryThreshold;
        if (logger.isInfoEnabled()) {
            logger.info("Back To Normal Memory threshold set to " + backToNormalMemoryThreshold + "%");
        }
    }

    /**
     * @return the backToNormalPercentageOfMemoryUsed
     */
    public int getBackToNormalMemoryThreshold() {
        return backToNormalMemoryThreshold;
    }

    /**
     * @param memoryThreshold the memoryThreshold to set
     */
    public void setMemoryThreshold(int memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
        if (logger.isInfoEnabled()) {
            logger.info("Memory threshold set to " + this.memoryThreshold + "%");
        }
    }

    /**
     * @return the memoryThreshold
     */
    public int getMemoryThreshold() {
        return memoryThreshold;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.congestion.CongestionMonitor#monitor()
     */
    @Override
    public void monitor() {
        Runtime runtime = Runtime.getRuntime();

        double allocatedMemory = runtime.totalMemory() / (double) 1024;
        double freeMemory = runtime.freeMemory() / (double) 1024;

        double totalFreeMemory = freeMemory + (maxMemory - allocatedMemory);

        this.percentageOfMemoryUsed = (((double) 100) - ((totalFreeMemory / maxMemory) * ((double) 100)));

        if (this.memoryTooHigh) {
            if (this.percentageOfMemoryUsed < this.backToNormalMemoryThreshold) {
                logger.warn("Memory used: " + percentageOfMemoryUsed + "% < to the back to normal memory threshold : "
                        + this.backToNormalMemoryThreshold);
                this.memoryTooHigh = false;

                // Lets notify the listeners
                for (FastList.Node<CongestionListener> n = listeners.head(), end = listeners.tail(); (n = n.getNext()) != end;) {
                    CongestionListener listener = n.getValue();
                    listener.onCongestionFinish(SOURCE);
                }
            }
        } else {
            if (this.percentageOfMemoryUsed > memoryThreshold) {
                logger.warn("Memory used: " + percentageOfMemoryUsed + "% > to the memory threshold : " + this.memoryThreshold);
                this.memoryTooHigh = true;

                // Lets notify the listeners
                for (FastList.Node<CongestionListener> n = listeners.head(), end = listeners.tail(); (n = n.getNext()) != end;) {
                    CongestionListener listener = n.getValue();
                    listener.onCongestionStart(SOURCE);
                }
            }
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.congestion.CongestionMonitor#addCongestionListener(
     * org.mobicents.ss7.congestion.CongestionListener)
     */
    @Override
    public void addCongestionListener(CongestionListener listener) {
        this.listeners.add(listener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.congestion.CongestionMonitor#removeCongestionListener
     * (org.mobicents.ss7.congestion.CongestionListener)
     */
    @Override
    public void removeCongestionListener(CongestionListener listener) {
        this.listeners.remove(listener);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.congestion.CongestionMonitor#getSource()
     */
    @Override
    public String getSource() {
        return SOURCE;
    }

}
