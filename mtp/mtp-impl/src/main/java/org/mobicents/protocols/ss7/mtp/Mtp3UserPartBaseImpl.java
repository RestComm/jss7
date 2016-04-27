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

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitor;
import org.mobicents.ss7.congestion.ExecutorCongestionMonitorImpl;

// lic dep 1

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public abstract class Mtp3UserPartBaseImpl implements Mtp3UserPart {

    private static final Logger logger = Logger.getLogger(Mtp3UserPartBaseImpl.class);

    private static final String LICENSE_PRODUCT_NAME = "Mobicents-jSS7";

    protected static final String ROUTING_LABEL_FORMAT = "routingLabelFormat"; // we do not store this value
    protected static final String USE_LSB_FOR_LINKSET_SELECTION = "useLsbForLinksetSelection";

    private int maxSls = 32;
    private int slsFilter = 0x1F;

    // The count of threads that will be used for message delivering to
    // Mtp3UserPartListener's
    // For single thread model this value should be equal 1
    protected int deliveryTransferMessageThreadCount = Runtime.getRuntime().availableProcessors() * 2;
    // RoutingLabeFormat option
    private RoutingLabelFormat routingLabelFormat = RoutingLabelFormat.ITU;
    // If set to true, lowest bit of SLS is used for loadbalancing between Linkset else highest bit of SLS is used.
    private boolean useLsbForLinksetSelection = false;

    protected boolean isStarted = false;

    private CopyOnWriteArrayList<Mtp3UserPartListener> userListeners = new CopyOnWriteArrayList<Mtp3UserPartListener>();
    // a thread pool for delivering Mtp3TransferMessage messages
    private ExecutorService[] msgDeliveryExecutors;
    // a thread for delivering PAUSE, RESUME and STATUS messages
    private ScheduledExecutorService msgDeliveryExecutorSystem;
    private int[] slsTable = null;
    private ExecutorCongestionMonitorImpl executorCongestionMonitor = null;

    private Mtp3TransferPrimitiveFactory mtp3TransferPrimitiveFactory = null;

    private final String productName;

    public Mtp3UserPartBaseImpl(String productName) {
        if(productName == null){
            this.productName = LICENSE_PRODUCT_NAME;
        } else {
            this.productName = productName;
        }
    }

    public int getDeliveryMessageThreadCount() {
        return this.deliveryTransferMessageThreadCount;
    }

    public void setDeliveryMessageThreadCount(int deliveryMessageThreadCount) throws Exception {
        if (deliveryMessageThreadCount > 0 && deliveryMessageThreadCount <= 100)
            this.deliveryTransferMessageThreadCount = deliveryMessageThreadCount;
    }

    @Override
    public void addMtp3UserPartListener(Mtp3UserPartListener listener) {
        this.userListeners.add(listener);
    }

    @Override
    public void removeMtp3UserPartListener(Mtp3UserPartListener listener) {
        this.userListeners.remove(listener);
    }

    @Override
    public RoutingLabelFormat getRoutingLabelFormat() {
        return this.routingLabelFormat;
    }

    @Override
    public void setRoutingLabelFormat(RoutingLabelFormat routingLabelFormat) throws Exception {
        if (routingLabelFormat != null)
            this.routingLabelFormat = routingLabelFormat;
    }

    @Override
    public boolean isUseLsbForLinksetSelection() {
        return useLsbForLinksetSelection;
    }

    @Override
    public void setUseLsbForLinksetSelection(boolean useLsbForLinksetSelection) throws Exception {
        this.useLsbForLinksetSelection = useLsbForLinksetSelection;
    }

    /*
     * For classic MTP3 this value is maximum SIF length minus routing label length. This method should be overloaded if
     * different message length is supported.
     */
    @Override
    public int getMaxUserDataLength(int dpc) {
        switch (this.routingLabelFormat) {
            case ITU:
                // For PC_FORMAT_14, the MTP3 Routing Label takes 4 bytes - OPC/DPC
                // = 16 bits each and SLS = 4 bits
                return 272 - 4;
            case ANSI_Sls8Bit:
                // For PC_FORMAT_24, the MTP3 Routing Label takes 6 bytes - OPC/DPC
                // = 24 bits each and SLS = 8 bits
                return 272 - 7;
            default:
                // TODO : We don't support rest just yet
                return -1;

        }
    }

    @Override
    public Mtp3TransferPrimitiveFactory getMtp3TransferPrimitiveFactory() {
        return this.mtp3TransferPrimitiveFactory;
    }

    @Override
    public ExecutorCongestionMonitor getExecutorCongestionMonitor() {
        return executorCongestionMonitor;
    }

    public void start() throws Exception {
        // lic dep 2

        if (this.isStarted)
            return;

        if (!(this.routingLabelFormat == RoutingLabelFormat.ITU || this.routingLabelFormat == RoutingLabelFormat.ANSI_Sls8Bit)) {
            throw new Exception("Invalid PointCodeFormat set. We support only ITU or ANSI now");
        }

        switch (this.routingLabelFormat) {
            case ITU:
                this.maxSls = 16;
                this.slsFilter = 0x0f;
                break;
            case ANSI_Sls5Bit:
                this.maxSls = 32;
                this.slsFilter = 0x1f;
                break;
            case ANSI_Sls8Bit:
                this.maxSls = 256;
                this.slsFilter = 0xff;
                break;
            default:
                throw new Exception("Invalid SLS length");
        }

        this.slsTable = new int[maxSls];

        this.mtp3TransferPrimitiveFactory = new Mtp3TransferPrimitiveFactory(this.routingLabelFormat);

        this.createSLSTable(this.deliveryTransferMessageThreadCount);

        this.msgDeliveryExecutors = new ExecutorService[this.deliveryTransferMessageThreadCount];
        for (int i = 0; i < this.deliveryTransferMessageThreadCount; i++) {
            this.msgDeliveryExecutors[i] = Executors.newFixedThreadPool(1, new DefaultThreadFactory("Mtp3-DeliveryExecutor-"
                    + i));
        }
        this.msgDeliveryExecutorSystem = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(
                "Mtp3-DeliveryExecutorSystem"));

        this.executorCongestionMonitor = new ExecutorCongestionMonitorImpl(productName, msgDeliveryExecutors);

        this.isStarted = true;

        this.startThreadMonitoring();
    }

    public void stop() throws Exception {

        if (!this.isStarted)
            return;

        this.isStarted = false;

        for (ExecutorService es : this.msgDeliveryExecutors) {
            es.shutdown();
        }
        this.msgDeliveryExecutorSystem.shutdown();
        this.executorCongestionMonitor = null;
    }

    private void startThreadMonitoring() {
        ExecutorCongestionMonitorImpl monitor = this.executorCongestionMonitor;
        if (isStarted && monitor != null) {
            monitor.monitor();
            ExecutorCongestionMonitorHandler handler = new ExecutorCongestionMonitorHandler();
            this.msgDeliveryExecutorSystem.schedule(handler, 1000, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Deliver an incoming message to the local user
     *
     * @param msg
     * @param effectiveSls For the thread selection (for message delivering)
     */
    protected void sendTransferMessageToLocalUser(Mtp3TransferPrimitive msg, int seqControl) {
        if (this.isStarted) {
            MsgTransferDeliveryHandler hdl = new MsgTransferDeliveryHandler(msg);

            seqControl = seqControl & slsFilter;
            this.msgDeliveryExecutors[this.slsTable[seqControl]].execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3TransferPrimitive=%s but Mtp3UserPart is not started. Message will be dropped", msg));
        }
    }

    protected void sendPauseMessageToLocalUser(Mtp3PausePrimitive msg) {
        if (this.isStarted) {
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3PausePrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    protected void sendResumeMessageToLocalUser(Mtp3ResumePrimitive msg) {
        if (this.isStarted) {
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3ResumePrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    protected void sendStatusMessageToLocalUser(Mtp3StatusPrimitive msg) {
        if (this.isStarted) {
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3StatusPrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    protected void sendEndCongestionMessageToLocalUser(Mtp3EndCongestionPrimitive msg) {
        if (this.isStarted) {
            MsgSystemDeliveryHandler hdl = new MsgSystemDeliveryHandler(msg);
            this.msgDeliveryExecutorSystem.execute(hdl);
        } else {
            logger.error(String.format(
                    "Received Mtp3EndCongestionPrimitive=%s but MTP3 is not started. Message will be dropped", msg));
        }
    }

    private void createSLSTable(int minimumBoundThread) {
        int stream = 0;
        for (int i = 0; i < maxSls; i++) {
            if (stream >= minimumBoundThread) {
                stream = 0;
            }
            slsTable[i] = stream++;
        }
    }

    private class MsgTransferDeliveryHandler implements Runnable {

        private Mtp3TransferPrimitive msg;

        public MsgTransferDeliveryHandler(Mtp3TransferPrimitive msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (isStarted) {
                try {
                    for (Mtp3UserPartListener lsn : userListeners) {
                        lsn.onMtp3TransferMessage(this.msg);
                    }
                } catch (Exception e) {
                    logger.error("Exception while delivering a system messages to the MTP3-user: " + e.getMessage(), e);
                }
            } else {
                logger.error(String.format(
                        "Received Mtp3TransferPrimitive=%s but Mtp3UserPart is not started. Message will be dropped", msg));
            }
        }
    }

    private class MsgSystemDeliveryHandler implements Runnable {

        Mtp3Primitive msg;

        public MsgSystemDeliveryHandler(Mtp3Primitive msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            if (isStarted) {
                try {
                    for (Mtp3UserPartListener lsn : userListeners) {
                        if (this.msg.getType() == Mtp3Primitive.PAUSE)
                            lsn.onMtp3PauseMessage((Mtp3PausePrimitive) this.msg);
                        if (this.msg.getType() == Mtp3Primitive.RESUME)
                            lsn.onMtp3ResumeMessage((Mtp3ResumePrimitive) this.msg);
                        if (this.msg.getType() == Mtp3Primitive.STATUS)
                            lsn.onMtp3StatusMessage((Mtp3StatusPrimitive) this.msg);
                        if (this.msg.getType() == Mtp3Primitive.END_CONGESTION)
                            lsn.onMtp3EndCongestionMessage((Mtp3EndCongestionPrimitive) this.msg);
                    }
                } catch (Exception e) {
                    logger.error("Exception while delivering a payload messages to the MTP3-user: " + e.getMessage(), e);
                }
            } else {
                logger.error(String.format(
                        "Received Mtp3Primitive=%s but Mtp3UserPart is not started. Message will be dropped", msg));
            }
        }
    }

    private class ExecutorCongestionMonitorHandler implements Runnable {
        @Override
        public void run() {
            startThreadMonitoring();
        }
    }
}
