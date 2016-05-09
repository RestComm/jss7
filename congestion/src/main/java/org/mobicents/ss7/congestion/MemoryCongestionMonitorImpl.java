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

package org.mobicents.ss7.congestion;

import java.io.Serializable;

/**
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MemoryCongestionMonitorImpl extends BaseCongestionMonitor implements MemoryCongestionMonitor, Serializable {
    private static final String SOURCE = "MEMORY";

    private double maxMemory;
    private volatile double percentageOfMemoryUsed;

    private volatile int memoryAlarmLevel = 0;

    private double[] memoryThreshold = new double[] { 77, 87, 97 };
    private double[] backToNormalMemoryThreshold = new double[] { 72, 82, 92 };

    protected double calculatedAllocatedMemory;
    protected double calculatedFreeMemory;
    protected double calculatedTotalFreeMemory;

    public MemoryCongestionMonitorImpl() {
        maxMemory = Runtime.getRuntime().maxMemory() / (double) 1024;
    }

    /**
     * @param backToNormalPercentageOfMemoryUsed setting the Threshold 1 of alarm clearing
     */
    public void setBackToNormalMemoryThreshold1(double backToNormalMemoryThreshold1) {
        if (Math.abs(this.backToNormalMemoryThreshold[0] - backToNormalMemoryThreshold1) > 0.001 && logger.isInfoEnabled()) {
            logger.info("MemoryCongestionMonitor: Back To Normal Memory threshold 1 set to " + backToNormalMemoryThreshold1
                    + "%");
        }
        this.backToNormalMemoryThreshold[0] = backToNormalMemoryThreshold1;
    }

    /**
     * @param backToNormalPercentageOfMemoryUsed setting the Threshold 2 of alarm clearing
     */
    public void setBackToNormalMemoryThreshold2(double backToNormalMemoryThreshold2) {
        if (Math.abs(this.backToNormalMemoryThreshold[1] - backToNormalMemoryThreshold2) > 0.001 && logger.isInfoEnabled()) {
            logger.info("MemoryCongestionMonitor: Back To Normal Memory threshold 2 set to " + backToNormalMemoryThreshold2
                    + "%");
        }
        this.backToNormalMemoryThreshold[1] = backToNormalMemoryThreshold2;
    }

    /**
     * @param backToNormalPercentageOfMemoryUsed setting the Threshold 3 of alarm clearing
     */
    public void setBackToNormalMemoryThreshold3(double backToNormalMemoryThreshold3) {
        if (Math.abs(this.backToNormalMemoryThreshold[2] - backToNormalMemoryThreshold3) > 0.001 && logger.isInfoEnabled()) {
            logger.info("MemoryCongestionMonitor: Back To Normal Memory threshold 3 set to " + backToNormalMemoryThreshold3
                    + "%");
        }
        this.backToNormalMemoryThreshold[2] = backToNormalMemoryThreshold3;
    }

    /**
     * @return the backToNormalPercentageOfMemoryUsed Threshold 1
     */
    public double getBackToNormalMemoryThreshold1() {
        return backToNormalMemoryThreshold[0];
    }

    /**
     * @return the backToNormalPercentageOfMemoryUsed Threshold 2
     */
    public double getBackToNormalMemoryThreshold2() {
        return backToNormalMemoryThreshold[1];
    }

    /**
     * @return the backToNormalPercentageOfMemoryUsed Threshold 3
     */
    public double getBackToNormalMemoryThreshold3() {
        return backToNormalMemoryThreshold[2];
    }

    /**
     * @param memoryThreshold the memoryThreshold the Threshold 1 to alarm set
     */
    public void setMemoryThreshold1(double memoryThreshold1) {
        if (Math.abs(this.memoryThreshold[0] - memoryThreshold1) > 0.001 && logger.isInfoEnabled()) {
            logger.info("MemoryCongestionMonitor: Memory threshold 1 set to " + memoryThreshold1 + "%");
        }
        this.memoryThreshold[0] = memoryThreshold1;
    }

    /**
     * @param memoryThreshold the memoryThreshold the Threshold 2 to alarm set
     */
    public void setMemoryThreshold2(double memoryThreshold2) {
        if (Math.abs(this.memoryThreshold[1] - memoryThreshold2) > 0.001 && logger.isInfoEnabled()) {
            logger.info("MemoryCongestionMonitor: Memory threshold 2 set to " + memoryThreshold2 + "%");
        }
        this.memoryThreshold[1] = memoryThreshold2;
    }

    /**
     * @param memoryThreshold the memoryThreshold the Threshold 3 to alarm set
     */
    public void setMemoryThreshold3(double memoryThreshold3) {
        if (Math.abs(this.memoryThreshold[2] - memoryThreshold3) > 0.001 && logger.isInfoEnabled()) {
            logger.info("MemoryCongestionMonitor: Memory threshold 3 set to " + memoryThreshold3 + "%");
        }
        this.memoryThreshold[2] = memoryThreshold3;
    }

    /**
     * @return the memoryThreshold the Threshold 1
     */
    public double getMemoryThreshold1() {
        return memoryThreshold[0];
    }

    /**
     * @return the memoryThreshold the Threshold 2
     */
    public double getMemoryThreshold2() {
        return memoryThreshold[1];
    }

    /**
     * @return the memoryThreshold the Threshold 3
     */
    public double getMemoryThreshold3() {
        return memoryThreshold[2];
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

        calculatedAllocatedMemory = allocatedMemory;
        calculatedFreeMemory = freeMemory;
        calculatedTotalFreeMemory = totalFreeMemory;

        this.percentageOfMemoryUsed = (((double) 100) - ((totalFreeMemory / maxMemory) * ((double) 100)));

        super.applyNewValue(memoryAlarmLevel, percentageOfMemoryUsed, memoryThreshold, backToNormalMemoryThreshold, true);
    }

    protected CongestionTicketImpl generateTicket() {
        return new CongestionTicketImpl(SOURCE, memoryAlarmLevel);
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

    @Override
    public int getAlarmLevel() {
        return memoryAlarmLevel;
    }

    @Override
    protected void setAlarmLevel(int val) {
        memoryAlarmLevel = val;
    }

    @Override
    public double getMemoryThreshold_1() {
        return memoryThreshold[0];
    }

    @Override
    public double getMemoryThreshold_2() {
        return memoryThreshold[1];
    }

    @Override
    public double getMemoryThreshold_3() {
        return memoryThreshold[2];
    }

    @Override
    public void setMemoryThreshold_1(double value) throws Exception {
        memoryThreshold[0] = value;
    }

    @Override
    public void setMemoryThreshold_2(double value) throws Exception {
        memoryThreshold[1] = value;
    }

    @Override
    public void setMemoryThreshold_3(double value) throws Exception {
        memoryThreshold[2] = value;
    }

    @Override
    public double getBackToNormalMemoryThreshold_1() {
        return backToNormalMemoryThreshold[0];
    }

    @Override
    public double getBackToNormalMemoryThreshold_2() {
        return backToNormalMemoryThreshold[1];
    }

    @Override
    public double getBackToNormalMemoryThreshold_3() {
        return backToNormalMemoryThreshold[2];
    }

    @Override
    public void setBackToNormalMemoryThreshold_1(double value) throws Exception {
        backToNormalMemoryThreshold[0] = value;
    }

    @Override
    public void setBackToNormalMemoryThreshold_2(double value) throws Exception {
        backToNormalMemoryThreshold[1] = value;
    }

    @Override
    public void setBackToNormalMemoryThreshold_3(double value) throws Exception {
        backToNormalMemoryThreshold[2] = value;
    }

    @Override
    protected String getAlarmDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("maxMemory=");
        sb.append(maxMemory);
        sb.append(", allocatedMemory=");
        sb.append(calculatedAllocatedMemory);
        sb.append(", freeMemory=");
        sb.append(calculatedFreeMemory);
        sb.append(", totalFreeMemory=");
        sb.append(calculatedTotalFreeMemory);
        sb.append(",  percentageOfMemoryUsed=");
        sb.append(percentageOfMemoryUsed);
        return sb.toString();
    }

}
