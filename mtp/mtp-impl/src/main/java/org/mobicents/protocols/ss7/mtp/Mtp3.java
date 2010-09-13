/*
 * Mobicents, Communications Middleware
 * 
 * Copyright (c) 2008, Red Hat Middleware LLC or third-party
 * contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Middleware LLC.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 *
 * Boston, MA  02110-1301  USA
 */
package org.mobicents.protocols.ss7.mtp;

import java.io.IOException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.SelectorProvider;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * 
 * @author kulikov
 * @author baranowb
 */
public class Mtp3 implements Runnable {

    public final static int TIMEOUT_T1_SLTM = 12;
    public final static int TIMEOUT_T2_SLTM = 90;
    private final static int LINK_MANAGEMENT = 0;
    private final static int LINK_TESTING = 1;
    public final static int _SI_SERVICE_SCCP = 3;
    public final static int _SI_SERVICE_ISUP = 5;
    
    private Mtp3Listener mtp3Listener;
    /**
     * Flag indicating if we should notify upper layer
     */
    private boolean l4IsUp = false;
    
    //FIXME: MOVE THIS TO LINKSET?
    protected volatile boolean started = false;
    
    private int dpc;
    private int opc;
    
    /** List of signaling channels wrapped with MTP2*/
    private List<Mtp2> links = new ArrayList();
    
    /** Active links */
    private Linkset linkset = new Linkset();    
    // ss7 has subservice as 1, q704 shows the same iirc
    //private int subservice = -1;
    //private int service;
    private static final int DEFAULT_SUB_SERVICE_TRA = 0xC;    
    
    // //////////////////////////
    // SLTM pattern for inits //
    // //////////////////////////
    private final static byte[] SLTM_PATTERN = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x0F};
    
    protected String name;
    private StreamSelector selector;
    
    private MTPScheduler executor = new MTPScheduler();

    private Logger logger = Logger.getLogger(Mtp3.class);
    
    //public Mtp3Impl(String name, Mtp1 layer1) {
    public Mtp3(String name) {
        this.name = name;
        try {
            selector = SelectorProvider.getSelector("org.mobicents.media.server.impl.resource.zap.Selector");
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /** (non-Javadoc)
     * @see org.mobicents.protocols.ss7.mtp.Mtp3#addMtp3Listener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
     */
    public void addMtp3Listener(Mtp3Listener lst) {
        if (this.mtp3Listener != null) {
            throw new IllegalStateException("Listener already present.");
        }
        this.mtp3Listener = lst;
        
    }

    /* (non-Javadoc)
     * @see org.mobicents.protocols.ss7.mtp.Mtp3#removeMtp3Listener(org.mobicents.protocols.ss7.mtp.Mtp3Listener)
     */
    public void removeMtp3Listener(Mtp3Listener lst) {
        if (lst != this.mtp3Listener) {
            throw new IllegalArgumentException("Wrong listener passed. Its not registered in this object!");
        }
        this.mtp3Listener = null;
        
    }

    /* (non-Javadoc)
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

    /**
     * Starts linkset.
     * 
     * @throws java.io.IOException
     */
    public void start() throws IOException {
        //starting links
        for (Mtp2 link : links) {
            link.start();
        }        
    }

    public void stop() throws IOException {
        started = false;
        for (Mtp2 link : links) {
            //selector.unregister(link);
            link.stop();
        }        
    }

    public void run() {
        try {
            Collection<SelectorKey> selected = selector.selectNow(StreamSelector.OP_READ, 20);
            for (SelectorKey key : selected) {
                ((Mtp1) key.getStream()).getLink().doRead();
            }
                
            selected = selector.selectNow(StreamSelector.OP_WRITE, 20);
            for (SelectorKey key : selected) {
                ((Mtp1) key.getStream()).getLink().doWrite();
            }
                
            this.executor.tick();
                
            for (Mtp2 link : links) {
                link.scheduler.tick();
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    /**
     * This method is called when new MSU is detected.
     * 
     * @param sio
     *            service information octet.
     * @param msg
     *            service information field;
     */
    public void onMessage(int sio, byte[] sif, Mtp2 mtp2) {
            int subserviceIndicator = (sio >> 4) & 0x0F;
            int serviceIndicator = sio & 0x0F;

            // int dpc = (sif[0] & 0xff | ((sif[1] & 0x3f) << 8));
            // int opc = ((sif[1] & 0xC0) >> 6) | ((sif[2] & 0xff) << 2) | ((sif[3]
            // & 0x0f) << 10);
            // int sls = (sif[3] & 0xf0) >>> 4;
            int dpc = dpc(sif, 1);
            int opc = opc(sif, 1);
            int sls = sls(sif, 1);

            //FIXME: change back to trace
            if (logger.isTraceEnabled()) {
                logger.trace(
                        String.format("(%s) Received MSSU [si=" + serviceIndicator + ",ssi=" + subserviceIndicator + ", dpc=" + dpc + ", opc=" + opc + ", sls=" + sls + "] data: ", mtp2.getName()) + Arrays.toString(sif));
            }
            switch (serviceIndicator) {
                case LINK_MANAGEMENT:
                    int h0 = sif[5] & 0x0f;
                    int h1 = (sif[5] & 0xf0) >>> 4;
                    
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
                    //FIX ME
                    break;
                case LINK_TESTING:
                    h0 = sif[5] & 0x0f;
                    h1 = (sif[5] & 0xf0) >>> 4;

                    int len = (sif[6] & 0xf0) >>> 4;
                    System.out.println("Length=" +len);
                    if (h0 == 1 && h1 == 1) {
                        if (logger.isTraceEnabled()) {
                            logger.trace(String.format("(%s) Received SLTM", mtp2.getName()));
                        }
                        // receive SLTM from remote end
                        // create response
                        byte[] slta = new byte[len + 7];


                        //change order of opc/dpc in SLTA !
                        writeRoutingLabel(slta, 0, 0, sls, opc, dpc);
                        slta[0] = (byte) sio;
                        slta[5] = 0x021;
                        // +1 cause we copy LEN byte also.
                        System.arraycopy(sif, 6, slta, 6, len + 1);

                        if (logger.isTraceEnabled()) {
                            logger.trace(String.format("(%s) Responding with SLTA", mtp2.getName()));
                        }
                        mtp2.send(slta, slta.length);
                    } else if (h0 == 1 && h1 == 2) {
                        // receive SLTA from remote end
                        if (logger.isTraceEnabled()) {
                            logger.trace(String.format("(%s) Received SLTA", mtp2.getName()));
                        }

                        //checking pattern
                        if (checkPattern(sif, SLTM_PATTERN)) {
                            //test message is acknowledged
                            mtp2.sltmTest.ack();

                            //notify top layer that link is up
                            if (!l4IsUp) {
                                l4IsUp = true;
                                linkUp(mtp2);
                            }
                        } else {
                            //logger.info("SLTA pattern does not match: \n"+Arrays.toString(sif)+"\n"+Arrays.toString(SLTM_PATTERN));
                        }
                    } else {
                        logger.warn(String.format("(%s) Unexpected message type", mtp2.getName()));
                    }
                    break;
                case _SI_SERVICE_SCCP:
                    if (logger.isEnabledFor(Level.TRACE) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates SCCP");
                        
                    }

                    if (mtp3Listener != null) {
                        //lets create byte[] which is actuall upper layer msg.
                        //msbBuff.len = sif.len - 4 (routing label), after routing label there should be msg code
                        //byte[] msgBuff = new byte[sif.length - 4];
                        //System.arraycopy(sif, 4, msgBuff, 0, msgBuff.length);
//                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_SCCP, subserviceIndicator, msgBuff);
                        byte[] message = new byte[sif.length + 1];
                        System.arraycopy(sif, 0, message, 1, sif.length);
                        message[0] = (byte) sio;
                        try {
                            mtp3Listener.receive(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case _SI_SERVICE_ISUP:
                    if (logger.isEnabledFor(Level.TRACE) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates ISUP");
                        
                    }
                    if (mtp3Listener != null) {
                        //lets create byte[] which is actuall upper layer msg.
                        //msbBuff.len = sif.len - 4 (routing label), after routing label there should be msg code
                        //byte[] msgBuff = new byte[sif.length - 4];
                        //System.arraycopy(sif, 4, msgBuff, 0, msgBuff.length);
//                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_ISUP, subserviceIndicator, msgBuff);
//                        byte[] message = new byte[sif.length + 1];
//                        System.arraycopy(sif, 0, message, 1, sif.length);
//                        message[0] = (byte) sio;
                        try {
                            mtp3Listener.receive(sif);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates UNKNOWN SERVICE!!!!!!!!!!!: " + Utils.dump(sif, sif.length, false));
                    }
                    break;
            }
        //creating handler for received message and schedule execution
//        MessageHandler handler = new MessageHandler(sio, sif, mtp2);
//        handler.run();
//        processor.execute(handler);
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
        //method expects proper message, lets pray its ok :/ - this is a way to be aligned with dialogic cards.
        //selecting link using sls
        //get sls;
        byte sls = (byte) sls(msg, 1);
        Mtp2 link = this.selectLink(sls);
        
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("MTP3 passes MSU to layer 2, (%s) ", link != null ? link.getName() : "NO LINK"));
        }
        if (link == null) {
            return false;
        }
        
        return link.send(msg, len);
    }

    public void linkInService(Mtp2 link) {
        //restart traffic 
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("(%s) Sending Traffic Restart Allowed message", link.getName()));
        }
        restartTraffic(link);

        //create and assign Tester;
        if (link.sltmTest == null) {
            SLTMTest tester = new SLTMTest(link);
            link.sltmTest =(tester);

            //start tester
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("(%s) Starting link test procedure", link.getName()));
            }
            tester.start();
        } else {
            link.sltmTest.start();
        }
    }

    /**
     * Notify that link is up.
     * @param link
     */
    private void linkUp(Mtp2 link) {
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
        //remove this link from list of active links
        linkset.remove(link);

        //restart initial alignment after T17 expires
        link.fail();

        //notify mtp user part
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

    /* (non-Javadoc)
     * @see org.mobicents.protocols.ss7.mtp.Mtp2Listener#registerLink(org.mobicents.protocols.ss7.mtp.Mtp2)
     */
    public void registerLink(Mtp2 mtp2) {
        try {
            mtp2.getLayer1().register(selector);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see org.mobicents.protocols.ss7.mtp.Mtp2Listener#unregisterLink(org.mobicents.protocols.ss7.mtp.Mtp2)
     */
    public void unregisterLink(Mtp2 mtp2) {
    }

    private void restartTraffic(Mtp2 link) {
        //int subservice = link.getSubService();
        //if (subservice == -1) {
        int subservice = DEFAULT_SUB_SERVICE_TRA;
        //}
        byte[] buffer = new byte[6];
        writeRoutingLabel(buffer, 0, 0, 0, dpc, opc);
        // buffer[0] = (byte) (_SERVICE_TRA | ( subservice << 4));
        buffer[0] = (byte) 0xC0;
        // H0 and H1, see Q.704 section 15.11.2+
        buffer[5] = 0x17;
        link.send(buffer, buffer.length);
    }    // -6 cause we have in sif, sio+label+len of pattern - thats 1+4+1 = 6
    private final static int SIF_PATTERN_OFFSET = 7;

    /**
     * Performs test message check.
     * 
     * @param sif the response test message
     * @param pattern expected message content
     * @return true if message content matches to specfied pattern
     */
    private boolean checkPattern(byte[] sif, byte[] pattern) {
        if (sif.length - SIF_PATTERN_OFFSET != pattern.length) {
            return false;
        }
        for (int i = 0; i < pattern.length; i++) {
            if (sif[i + SIF_PATTERN_OFFSET] != pattern[i]) {
                return false;
            }
        }
        return true;
    }

    protected class SLTMTest extends MTPTask {

        private Mtp2 link;
        private int tryCount;        //SLTM message buffer;
        private byte[] sltm = new byte[7 + SLTM_PATTERN.length];
        
        private SLTMTest(Mtp2 link) {
            this.link = link;
        }

        /**
         * 
         */
        public void start() {
            //reset count of tries
            tryCount = 0;

            //sending test message to the remote terminal
            run();
        }
        
        public void stop() {
            //disable handler
            cancel();
        }

        /**
         * This methods should be called to acknowledge that current tests is passed.
         * 
         */
        public void ack() {
            //disable current awaiting handler
            cancel();
            //reset number of tryies;
            tryCount = 0;
            //shcedule next ping
            executor.schedule(this, Mtp3.TIMEOUT_T2_SLTM * 1000);
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("(%s) Test message acknowledged, Link test passed", link.getName()));
            }
        }

        /**
         * Sends SLTM message using this link.
         * 
         * @param timeout  the amount of time in millesecond for awaiting response.
         */
        public void ping(long timeout) {
            //prepearing test message
            
            writeRoutingLabel(sltm, 0, 0, link.getSls(), dpc, opc);
            sltm[0] = (byte) 0xC1; // 1100 0001
            sltm[5] = 0x11;
            sltm[6] = (byte) (SLTM_PATTERN.length << 4);
            System.arraycopy(SLTM_PATTERN, 0, sltm, 7, SLTM_PATTERN.length);

            //sending test message
            link.send(sltm, sltm.length);

            //incremeting number of tries.
            tryCount++;

            //scheduling timeout
            executor.schedule(this, (int)(timeout * 1000));
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("(%s) Test request, try number = %d", link.getName(), tryCount));
            }
        }
        
        public void perform() {
            switch (tryCount) {
                case 0:
                    //sending first test message
                    ping(Mtp3.TIMEOUT_T1_SLTM);
                    break;
                case 1:
                    //first message was not answered, sending second
                    ping(Mtp3.TIMEOUT_T1_SLTM);
                    break;
                case 2:
                    if (logger.isDebugEnabled()) {
                        logger.debug(String.format("(%s) SLTM message was not acknowledged, Link failed", link.getName()));
                    }
                    //second message was not answered, report failure
                    linkFailed(link);
            }
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
        //see Q.704.14.2 
        int subserviceIndicator = (data[0] >> 4) & 0x0F;
        return subserviceIndicator;
    }
    
    public static void writeRoutingLabel(byte[] data, int si, int ssi, int sls, int dpc, int opc) {
        //see Q.704.14.2
        data[0] = (byte) (((ssi & 0x0F) << 4) | (si & 0x0F));
        data[1] = (byte) dpc;
        data[2] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
        data[3] = (byte) (opc >> 2);
        data[4] = (byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));
        //sif[4] = (byte) (((opc>> 10) & 0x0F) | ((0 & 0x0F) << 4));
    }    ////////////////
    // Debug Part //
    ////////////////
    private boolean l3Debug;

    public boolean isL3Debug() {
        return l3Debug;
    }

    public void setL3Debug(boolean l3Debug) {
        this.l3Debug = l3Debug;
    }
}
