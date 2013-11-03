/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javolution.util.FastList;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.scheduler.Scheduler;
import org.mobicents.protocols.ss7.scheduler.Task;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 *
 * @author kulikov
 * @author baranowb
 */
public class Mtp3 implements Runnable {

    public static final int TIMEOUT_T1_SLTM = 120;
    public static final int TIMEOUT_T2_SLTM = 900;
    private static final int LINK_MANAGEMENT = 0;
    private static final int LINK_TESTING = 1;
    public static final int _SI_SERVICE_SCCP = 3;
    public static final int _SI_SERVICE_ISUP = 5;

    private Mtp3Listener mtp3Listener;
    /**
     * Flag indicating if we should notify upper layer
     */
    private boolean l4IsUp = false;

    // FIXME: MOVE THIS TO LINKSET?
    protected volatile boolean started = false;
    /** defautl value of SSI */
    public static final int DEFAULT_NI = 2;// NATIONAL, as default.

    private int dpc;
    private int opc;
    private int ni = DEFAULT_NI << 2; // << cause its faster for checks.
    /**
     * Local byte[], in which we forge messages.
     */
    private byte[] localFrame = new byte[279];
    /** List of signaling channels wrapped with MTP2 */
    private List<Mtp2> links = new ArrayList();

    /** Active links */
    private Linkset linkset = new Linkset();
    // ss7 has subservice as 1, q704 shows the same iirc
    // private int subservice = -1;
    // private int service;

    // //////////////////////////
    // SLTM pattern for inits //
    // //////////////////////////
    private static final byte[] SLTM_PATTERN = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x0F };

    protected String name;
    private StreamSelector selector;

    private Scheduler scheduler;

    private static final Logger logger = Logger.getLogger(Mtp3.class);

    // public Mtp3Impl(String name, Mtp1 layer1) {
    public Mtp3(String name, Scheduler scheduler) {
        this.scheduler = scheduler;
        this.name = name;
        try {
            selector = SelectorProvider.getSelector("org.mobicents.ss7.hardware.dahdi.Selector");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setScheduler(Scheduler scheduler) {
        if (scheduler != null)
            this.scheduler = scheduler;
    }

    /**
     * Assigns originated point code
     *
     * @param opc the originated point code
     */
    public void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * Assigns destination point code.
     *
     * @param dpc destination point code in decimal format.
     */
    public void setDpc(int dpc) {
        this.dpc = dpc;
    }

    /**
     * @return the dpc
     */
    public int getDpc() {
        return dpc;
    }

    /**
     * @return the opc
     */
    public int getOpc() {
        return opc;
    }

    /**
     * Sets network indicator to be used as part of SIO( actually SSI). It Accepts 2 bit integer.
     *
     * @param ni
     */
    public void setNetworkIndicator(int ni) {
        this.ni = (0x03 & ni) << 2;
    }

    public int getNetworkIndicator() {
        return this.ni >> 2;
    }

    /**
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp3#addMtp3Listener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
     */
    public void addMtp3Listener(Mtp3Listener lst) {
        if (this.mtp3Listener != null) {
            throw new IllegalStateException("Listener already present.");
        }
        this.mtp3Listener = lst;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp3#removeMtp3Listener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
     */
    public void removeMtp3Listener(Mtp3Listener lst) {
        if (lst != this.mtp3Listener) {
            throw new IllegalArgumentException("Wrong listener passed. Its not registered in this object!");
        }
        this.mtp3Listener = null;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp3#setLinks(java.util.List)
     */
    public void setLinks(List<Mtp2> channels) {
        for (Mtp2 link : channels) {
            if (link != null) {
                link.mtp3 = this;
                this.links.add(link);
            }
        }

    }

    public void addLink(Mtp2 mtp2) {
        mtp2.mtp3 = this;
        this.links.add(mtp2);
    }

    public void clearLinks() {
        this.links.clear();
    }

    /**
     * Starts linkset.
     *
     * @throws java.io.IOException
     */
    public void start() throws IOException {
        // starting links
        for (Mtp2 link : links) {
            link.start();
        }
    }

    public void stop() throws IOException {
        started = false;
        for (Mtp2 link : links) {
            // selector.unregister(link);
            link.stop();
        }
    }

    public void run() {
        try {
            FastList<SelectorKey> selected = selector.selectNow(StreamSelector.OP_READ, 20);
            for (FastList.Node<SelectorKey> n = selected.head(), end = selected.tail(); (n = n.getNext()) != end;) {
                ((Mtp2) ((Mtp1) n.getValue().getStream()).getLink()).doRead();
            }

            selected = selector.selectNow(StreamSelector.OP_WRITE, 20);
            for (FastList.Node<SelectorKey> n = selected.head(), end = selected.tail(); (n = n.getNext()) != end;) {
                ((Mtp2) ((Mtp1) n.getValue().getStream()).getLink()).doWrite();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when new MSU is detected.
     *
     * @param sio service information octet.
     * @param msg service information field;
     */

    public void onMessage(Mtp2Buffer rxFrame, Mtp2 mtp2) {
        // | ----------------------- TO L4 --------------------------- |
        // | --------------------- SIF ------------------------ |
        // FSN BSN LEN SIO DPC/OPC,SLS HEADING LEN
        // b1 b0 0e 01 01 80 00 00 21 70 01 02 03 04 05 06 0f CRC CRC
        int sio = rxFrame.frame[3] & 0xFF;
        int subserviceIndicator = (sio >> 4) & 0x0F;
        int serviceIndicator = sio & 0x0F;
        int ni = (subserviceIndicator & 0x0C); // local NI(network Indicator) form msg., 0x0C, since we store it as shifted
                                               // value.
        // int dpc = (sif[0] & 0xff | ((sif[1] & 0x3f) << 8));
        // int opc = ((sif[1] & 0xC0) >> 6) | ((sif[2] & 0xff) << 2) | ((sif[3]
        // & 0x0f) << 10);
        // int sls = (sif[3] & 0xf0) >>> 4;
        int dpc = dpc(rxFrame.frame, 4); // 1 - cause sif contains sio, even though its also passed as arg.
        int opc = opc(rxFrame.frame, 4);
        int sls = sls(rxFrame.frame, 4);

        // check SSI, Q.704 Figure 25, seems like if its bad, we discard.
        if (this.ni != ni) {
            if (logger.isEnabledFor(Level.ERROR)) {
                logger.error(String.format("(%s) Received MSSU with bad SSI, discarding! ni:" + ni + " thisni:" + this.ni
                        + " [si=" + serviceIndicator + ",ssi=" + subserviceIndicator + ", dpc=" + dpc + ", opc=" + opc
                        + ", sls=" + sls + "] data: ", mtp2.getName())
                        + Arrays.toString(rxFrame.frame));
            }
            return;
        }

        switch (serviceIndicator) {
            case LINK_MANAGEMENT:
                int h0 = rxFrame.frame[8] & 0x0f;
                int h1 = (rxFrame.frame[8] & 0xf0) >>> 4;

                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("(%s) Signalling network management", mtp2.getName()));
                }

                if (h0 == 0) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) Changeover management", mtp2.getName()));
                    }
                } else if (h0 == 7 && h1 == 1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) TRA received", mtp2.getName()));
                    }
                }
                // FIX ME
                break;
            case LINK_TESTING:
                h0 = rxFrame.frame[8] & 0x0f;
                h1 = (rxFrame.frame[8] & 0xf0) >>> 4;

                int len = (rxFrame.frame[9] & 0xf0) >>> 4;
                if (h0 == 1 && h1 == 1) {
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) Received SLTM", mtp2.getName()));
                    }
                    // receive SLTM from remote end
                    // create response

                    // change order of opc/dpc in SLTA !
                    writeRoutingLabel(this.localFrame, sio, this.ni, sls, opc, dpc);
                    // slta[0] = (byte) sio;
                    this.localFrame[5] = 0x021;
                    // +1 cause we copy LEN byte also.
                    System.arraycopy(rxFrame.frame, 9, this.localFrame, 6, len + 1);

                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) Responding with SLTA", mtp2.getName()));
                    }
                    mtp2.send(this.localFrame, len + 9);
                } else if (h0 == 1 && h1 == 2) {
                    // receive SLTA from remote end
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) Received SLTA", mtp2.getName()));
                    }

                    // checking pattern
                    if (checkPattern(rxFrame, len, SLTM_PATTERN)) {
                        // test message is acknowledged
                        mtp2.sltmTest.ack();

                        // notify top layer that link is up
                        if (!l4IsUp) {
                            l4IsUp = true;
                            linkUp(mtp2);
                        }
                    } else {
                        if (logger.isEnabledFor(Level.WARN)) {
                            logger.warn("SLTA pattern does not match: \n" + Arrays.toString(rxFrame.frame) + "\n"
                                    + Arrays.toString(SLTM_PATTERN));
                        }
                    }
                } else {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format("(%s) Unexpected message type", mtp2.getName()));
                    }
                }
                break;
            case _SI_SERVICE_SCCP:
                if (logger.isDebugEnabled()) {
                    logger.debug("Received SCCP MSU");
                }

                if (mtp3Listener != null) {

                    // | ----------------------- TO L4 --------------------------- |
                    // | --------------------- SIF ------------------------ |
                    // FSN BSN LEN SIO DPC/OPC,SLS HEADING LEN
                    // b1 b0 0e 01 01 80 00 00 21 70 01 02 03 04 05 06 0f CRC CRC
                    byte[] messageBuffer = new byte[rxFrame.len - 5]; // -5 = FSN(1) + BSN(1) + LEN(1) +2xCRC(1)
                    System.arraycopy(rxFrame.frame, 3, messageBuffer, 0, rxFrame.len - 5);
                    // messageBuffer[0] = (byte) sio;
                    try {
                        mtp3Listener.receive(messageBuffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case _SI_SERVICE_ISUP:
                if (logger.isDebugEnabled()) {
                    logger.debug("Received ISUP MSU");

                }
                if (mtp3Listener != null) {
                    byte[] messageBuffer = new byte[rxFrame.len - 5];
                    System.arraycopy(rxFrame.frame, 3, messageBuffer, 0, rxFrame.len - 5);
                    // messageBuffer[0] = (byte) sio;
                    try {
                        mtp3Listener.receive(messageBuffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                if (logger.isEnabledFor(Level.WARN)) {
                    logger.warn("Received MSU for UNKNOWN SERVICE!!!!!!!!!!!: " + Utils.dump(rxFrame.frame, rxFrame.len, false));
                }
                break;
        }
    }

    /**
     * Selects link for transmission using link selection indicator.
     *
     * @param sls signaling link selection indicator.
     * @return selected link.
     */
    private Mtp2 selectLink(byte sls) {

        return linkset.select(sls);
    }

    public boolean send(byte[] msg, int len) {
        // method expects proper message, lets pray its ok :/ - this is a way to be aligned with dialogic cards.
        // selecting link using sls
        // get sls;
        byte sls = (byte) sls(msg, 1);
        Mtp2 link = this.selectLink(sls);

        if (link == null) {
            return false;
        }

        return link.send(msg, len);
    }

    public void linkInService(Mtp2 link) {
        // restart traffic
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("(%s) Sending TRA(Traffic Restart Allowed) message", link.getName()));
        }

        if (link.mtp2Listener != null) {
            link.mtp2Listener.linkInService();
        }

        restartTraffic(link);

        // create and assign Tester;
        if (link.sltmTest == null) {
            SLTMTest tester = new SLTMTest(link, scheduler);
            link.sltmTest = (tester);

            // start tester
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("(%s) Starting link test procedure(SLTM/SLTA)", link.getName()));
            }
            tester.start();
        } else {
            link.sltmTest.start();
        }
    }

    /**
     * Notify that link is up.
     *
     * @param link
     */
    private void linkUp(Mtp2 link) {

        if (link.mtp2Listener != null) {
            link.mtp2Listener.linkUp();
        }

        linkset.add(link);
        if (linkset.isActive() && this.mtp3Listener != null) {
            try {
                mtp3Listener.linkUp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("(%s) Link now IN_SERVICE", link.getName()));
        }
    }

    public void linkFailed(Mtp2 link) {

        // Call Listener
        if (link.mtp2Listener != null) {
            link.mtp2Listener.linkFailed();
        }

        // FIXME: add debug or trace?
        // remove this link from list of active links
        linkset.remove(link);

        // restart initial alignment after T17 expires
        link.fail();

        // notify mtp user part
        if (!linkset.isActive()) {
            l4IsUp = false;
            if (mtp3Listener != null) {
                try {
                    mtp3Listener.linkDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp2Listener#registerLink(org.mobicents.protocols.ss7.mtp.Mtp2)
     */
    public void registerLink(Mtp2 mtp2) {
        try {
            mtp2.getLayer1().register(selector);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.mtp.Mtp2Listener#unregisterLink(org.mobicents.protocols.ss7.mtp.Mtp2)
     */
    public void unregisterLink(Mtp2 mtp2) {
    }

    private void restartTraffic(Mtp2 link) {
        writeRoutingLabel(this.localFrame, 0, this.ni, 0, dpc, opc);

        // H0 and H1, see Q.704 section 15.11.2+
        this.localFrame[5] = 0x17;
        link.send(this.localFrame, 6);
    }

    private static final int PATTERN_OFFSET = 10;
    private static final int PATTERN_LEN_OFFSET = PATTERN_OFFSET + 2; // +2 becuase frame.len contains 2B for CRC

    private boolean checkPattern(Mtp2Buffer frame, int sltmLen, byte[] pattern) {
        if (sltmLen != pattern.length) {
            return false;
        }
        for (int i = 0; i < pattern.length; i++) {
            if (frame.frame[i + PATTERN_OFFSET] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param i
     * @return
     */

    protected class SLTMTest extends Task {

        private Mtp2 link;
        private int tryCount; // SLTM message buffer;
        private byte[] sltm = new byte[7 + SLTM_PATTERN.length]; // this has to be separate, cause its async to Mtp3.send

        private int ttl = 0;
        private boolean started = false;

        private SLTMTest(Mtp2 link, Scheduler scheduler) {
            super(scheduler);
            this.link = link;
        }

        /**
         *
         */

        public void start() {
            this.started = true;

            // reset count of tries
            tryCount = 0;

            this.activate(true);

            // sending test message to the remote terminal
            ttl = Mtp3.TIMEOUT_T1_SLTM;
            ping();
            scheduler.submitHeatbeat(this);
        }

        public void stop() {
            this.started = false;

            // disable handler
            cancel();
        }

        public int getQueueNumber() {
            return scheduler.HEARTBEAT_QUEUE;
        }

        /**
         * This methods should be called to acknowledge that current tests is passed.
         *
         */
        public void ack() {
            // disable current awaiting handler
            cancel();
            // reset number of tryies;
            tryCount = -1;
            // shcedule next ping
            ttl = Mtp3.TIMEOUT_T2_SLTM;
            scheduler.submitHeatbeat(this);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("(%s) SLTM acknowledged, Link test passed", link.getName()));
            }
        }

        /**
         * Sends SLTM message using this link.
         *
         * @param timeout the amount of time in millesecond for awaiting response.
         */
        public void ping() {
            // prepearing test message

            writeRoutingLabel(sltm, 0x01, ni, link.getSls(), dpc, opc);
            // sltm[0] = (byte) 0x01;
            sltm[5] = 0x11;
            sltm[6] = (byte) (SLTM_PATTERN.length << 4);
            System.arraycopy(SLTM_PATTERN, 0, sltm, 7, SLTM_PATTERN.length);

            // sending test message
            link.send(sltm, sltm.length);

            // incremeting number of tries.
            tryCount++;

            // scheduling timeout
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("(%s) SLTM sent, try number = %d", link.getName(), tryCount));
            }
        }

        public long perform() {
            if (this.started)
                return 0;

            if (ttl > 0) {
                ttl--;
                return 0;
            }

            switch (tryCount) {
                case -1:
                    // sending first message
                    ttl = Mtp3.TIMEOUT_T1_SLTM;
                    ping();
                    scheduler.submitHeatbeat(this);
                    break;
                case 0:
                    // first message was not answered, sending second
                    ttl = Mtp3.TIMEOUT_T1_SLTM;
                    ping();
                    scheduler.submitHeatbeat(this);
                    break;
                case 1:
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) SLTM message was not acknowledged, Link failed", link.getName()));
                    }
                    // second message was not answered, report failure
                    linkFailed(link);
                    this.started = false;
            }

            return 0;
        }
    }

    // //////////////////
    // Helper methods //
    // //////////////////
    public static final int dpc(byte[] sif, int shift) {
        int dpc = (sif[0 + shift] & 0xff | ((sif[1 + shift] & 0x3f) << 8));
        return dpc;
    }

    public static final int opc(byte[] sif, int shift) {
        int opc = ((sif[1 + shift] & 0xC0) >> 6) | ((sif[2 + shift] & 0xff) << 2) | ((sif[3 + shift] & 0x0f) << 10);
        return opc;
    }

    public static final int sls(byte[] sif, int shift) {
        int sls = (sif[3 + shift] & 0xf0) >>> 4;
        return sls;
    }

    public static final int si(byte[] data) {

        int serviceIndicator = data[0] & 0x0f;
        return serviceIndicator;
    }

    public static final int ssi(byte[] data) {
        // see Q.704.14.2
        int subserviceIndicator = (data[0] >> 4) & 0x0F;
        return subserviceIndicator;
    }

    public static void writeRoutingLabel(byte[] data, int si, int ssi, int sls, int dpc, int opc) {
        // see Q.704.14.2
        writeRoutingLabel(0, data, si, ssi, sls, dpc, opc);

    }

    public static void writeRoutingLabel(int shift, byte[] data, int si, int ssi, int sls, int dpc, int opc) {
        // see Q.704.14.2
        data[0 + shift] = (byte) (((ssi & 0x0F) << 4) | (si & 0x0F));
        data[1 + shift] = (byte) dpc;
        data[2 + shift] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
        data[3 + shift] = (byte) (opc >> 2);
        data[4 + shift] = (byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));

    }
}
