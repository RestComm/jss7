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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

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
    private final static int SERVICE_SCCP = 3;
    private final static int SERVICE_ISUP = 5;
    
    /** Reference to the layer 4 */
    protected MtpUser mtpUser;
    
    /**
     * I/O thread and Thread factory. 
     * 
     * This thread should have maximum available priority to prevent transmissions error.
     */
    protected Thread ioThread = null;
    
    /**
     * Event processor thread and Factory, this should be as low as possible.
     */
    private final ScheduledExecutorService processor;
    
    //private ScheduledExecutorService mtpTimer = Utils.getMtpTimer();
    /**
     * Flag indicating if we should notify upper layer
     */
    private boolean l4IsUp = false;
    //FIXME: MOVE THIS TO LINKSET?
    protected volatile boolean started = false;
    
    private int dpc;
    private int opc;
    
    /** List of signaling channels wrapped with MTP2*/
    private List<Mtp2> links;
    
    /** Active links */
    private Linkset linkset = new Linkset();
    
    // ss7 has subservice as 1, q704 shows the same iirc
    //private int subservice = -1;
    //private int service;
    private static final int SERVICE_SLTM = 0;
    private static final int SERVICE_TRA = 1;
    private static final int DEFAULT_SUB_SERVICE_TRA = 0xC;    
    
    // //////////////////////////
    // SLTM section for inits //
    // //////////////////////////
    
    private final static byte[] SLTM_PATTERN = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x0F};
    
    protected String name;
    private Logger logger = Logger.getLogger(Mtp3.class);

    private ScheduledExecutorService mtpTimer = Utils.getMtpTimer();
    
    //public Mtp3Impl(String name, Mtp1 layer1) {
    public Mtp3(String name) {
        this.name = name;
        
        //creating IO Thread.
        ioThread = new Thread(this, "MTP2");
        ioThread.setPriority(Thread.MAX_PRIORITY);
        
        processor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Assigns originated point code
     * 
     * @param opc the originated point code
     */
    protected void setOpc(int opc) {
        this.opc = opc;
    }

    /**
     * Assigns destination point code.
     * 
     * @param dpc destination point code in decimal format.
     */
    protected void setDpc(int dpc) {
        this.dpc = dpc;
    }
    
    /**
     * Assigning channels.
     * 
     * @param channels the list of signaling channels.
     */
    protected void setChannels(List<Mtp1> channels) {
        //creating mtp layer 2 for each channel specified
        for (Mtp1 channel : channels) {
            this.links.add(new Mtp2(this, channel));
        }
    }
    
    /**
     * This should be called upper layer to set listener.
     * @param layer4
     */
    public void setUserPart(MtpUser mtpUser) {
        this.mtpUser = mtpUser;
    }

    /**
     * Starts linkset.
     * 
     * @throws java.io.IOException
     */
    public void start() throws IOException {
        //starting main thread
        started = true;
        ioThread.start();
        
        //starting links
        for (Mtp2 link : links) {
            link.startLink();
        }        
    }

    public void stop() throws IOException {
        started = false;
        for (Mtp2 link : links) {
            link.startLink();
        }        
    }

    public void run() {
        while (started) {
            try {
                long thisTickStamp = System.currentTimeMillis();
                for (Mtp2 link : links) {
                    link.threadTick(thisTickStamp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        //creating handler for received message and schedule execution
        MessageHandler handler = new MessageHandler(sio, sif, mtp2);
        processor.execute(handler);
    }
    
    /**
     * Selects link for transmission using link selection indicator.
     * 
     * @param sls signaling link selection indicator.
     * @return selected link.
     */
    private Mtp2 selectLink(byte sls) {
        return null;
    }
    
    public boolean send(byte sls, byte linksetId, int service, int subservice, byte[] msg) {        
        //selecting link using sls
        Mtp2 link = selectLink(sls);
        
        //creating message
        byte[] buffer = new byte[5 + msg.length];
        writeRoutingLabel(buffer, sls);
        
        buffer[0] = (byte) ((service & 0x0F) | ((subservice & 0x0F) << 4));
        System.arraycopy(msg, 0, buffer, 5, msg.length);
        return link.queue(buffer);
    }

    public void linkInService(Mtp2 link) {
        //restart traffic 
        restartTraffic(link);
        
        //create and assign Tester;
        Test tester = new Test(link);
        link.test = tester;
        
        //start tester
        tester.start();
    }

    /**
     * Notify that link is up.
     * @param link
     */
    public void linkUp(Mtp2 link) {
        linkset.add(link);
    }
    
    public void linkFailed(Mtp2 link) {
        //remove this link from list of active links
        linkset.remove(link);

        //restart initial alignment after T17 expires
        link.start_T17();

        //notify mtp user part
        if (!linkset.isActive()) {
            mtpUser.linkDown();
        }
    }

    private void restartTraffic(Mtp2 link) {
        int subservice = link.getSubService();
        //if (subservice == -1) {
        subservice = DEFAULT_SUB_SERVICE_TRA;
        //}
        byte[] buffer = new byte[6];
        writeRoutingLabel(buffer, 0);
        // buffer[0] = (byte) (_SERVICE_TRA | ( subservice << 4));
        buffer[0] = (byte) 0xC0;
        // H0 and H1, see Q.704 section 15.11.2+
        buffer[5] = 0x17;
        link.queue(buffer);
    }

    private void writeRoutingLabel(byte[] sif, int sls) {
        sif[1] = (byte) dpc;
        sif[2] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
        sif[3] = (byte) (opc >> 2);
        //sif[4] = (byte) (((opc >> 10) & 0x0F) | ((mtp2.getSls() & 0x0F) << 4));
        sif[4] = (byte) (((opc>> 10) & 0x0F) | ((0 & 0x0F) << 4));
    }   
    
    // -6 cause we have in sif, sio+label+len of pattern - thats 1+4+1 = 6
    private final static int SIF_PATTERN_OFFSET = 6;

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

    protected class Test implements Runnable {
        private Mtp2 link;
        private int tryCount;
        
        //SLTM message buffer;
        private byte[] sltm = new byte[7 + SLTM_PATTERN.length];
        
        private ScheduledFuture test;
        
        protected Test(Mtp2 link) {
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
            test.cancel(false);
        }
        
        /**
         * This methods should be called to acknowledge that current tests is passed.
         * 
         */
        public void ack() {
            //disable current awaiting handler
            test.cancel(false);
            //reset number of tryies;
            tryCount = 0;
            //shcedule next ping
            test = mtpTimer.schedule(this, Mtp3.TIMEOUT_T2_SLTM, TimeUnit.MILLISECONDS);
        }
        
        /**
         * Sends SLTM message using this link.
         * 
         * @param timeout  the amount of time in millesecond for awaiting response.
         */
        public void ping(long timeout) {
            //prepearing test message
            sltm[0] = (byte) 0xC1; // 1100 0001
            writeRoutingLabel(sltm, 0);
            sltm[5] = 0x11;
            sltm[6] = (byte) (SLTM_PATTERN.length << 4);
            System.arraycopy(SLTM_PATTERN, 0, sltm, 7, SLTM_PATTERN.length);
            
            //sending test message
            link.queue(sltm);
            
            //incremeting number of tries.
            tryCount++;
            
            //scheduling timeout
            test = mtpTimer.schedule(this, timeout, TimeUnit.MILLISECONDS);
        }
        
        public void run() {
            switch (tryCount) {
                case 0 :
                    //sending first test message
                    ping(Mtp3.TIMEOUT_T1_SLTM);
                    break;
                case 1 :
                    //first message was not answered, sending second
                    ping(Mtp3.TIMEOUT_T1_SLTM);
                    break;
                case 2 :
                    //second message was not answered, report failure
                    linkFailed(link);
                    link.start_T17();
            }
        }
    }
    
    
    
    
    private final class MessageHandler implements Runnable {

        private Mtp2 mtp2;
        private int sio;
        private byte[] sif;

        public MessageHandler(int sio, byte[] sif, Mtp2 link) {
            this.mtp2 = link;
            this.sio = sio;
            this.sif = sif;
        }
        
        public void run() {

            int subserviceIndicator = (sio >> 4) & 0x03;
            int serviceIndicator = sio & 0x0f;

            // int dpc = (sif[0] & 0xff | ((sif[1] & 0x3f) << 8));
            // int opc = ((sif[1] & 0xC0) >> 6) | ((sif[2] & 0xff) << 2) | ((sif[3]
            // & 0x0f) << 10);
            // int sls = (sif[3] & 0xf0) >>> 4;
            int dpc = _getFromSif_DPC(sif, 0);
            int opc = _getFromSif_OPC(sif, 0);
            int sls = _getFromSif_SLS(sif, 0);

            if (logger.isTraceEnabled() && isL3Debug()) {
                // logger.debug("Received MSSU [si=" + si + ", dpc=" + dpc +
                // ", opc=" + opc + ", sls=" + sls + "]");
                mtp2.trace("Received MSSU [si=" + serviceIndicator + ",ssi=" + subserviceIndicator + ", dpc=" + dpc + ", opc=" + opc + ", sls=" + sls + "]");
            }
            switch (serviceIndicator) {
                case LINK_MANAGEMENT:
                    //FIX ME
                    break;
                case LINK_TESTING:
                    int h0 = sif[4] & 0x0f;
                    int h1 = (sif[4] & 0xf0) >>> 4;

                    int len = (sif[5] & 0xf0) >>> 4;

                    if (h0 == 1 && h1 == 1) {
                        if (logger.isDebugEnabled() && isL3Debug()) {
                            mtp2.trace("Received SLTM");
                        }
                        // receive SLTM from remote end
                        // create response
                        byte[] slta = new byte[len + 7];
                        slta[0] = (byte) sio;

                        writeRoutingLabel(slta, 0);
                        slta[5] = 0x021;
                        // +1 cause we copy LEN byte also.
                        System.arraycopy(sif, 5, slta, 6, len + 1);

                        if (logger.isTraceEnabled() && isL3Debug()) {
                            if (logger.isDebugEnabled()) {
                                // logger.debug("Link(" + name + ") SLTA received");
                                // lets validate SLTA we send. this is inverted
                                // procedure that we do on SLTA
                                int remote_OPC = opc;
                                int remote_DPC = dpc;
                                int remote_SLS = sls;

                                // now lets get that from sif. shift by one, we include
                                // SIO in byte[] buff
                                int slta_OPC = _getFromSif_OPC(slta, 1);
                                int slta_DPC = _getFromSif_DPC(slta, 1);
                                int slta_SLS = _getFromSif_SLS(slta, 1);
                                // check pattern?

                                if (remote_OPC != slta_DPC || remote_DPC != slta_OPC || remote_SLS != slta_SLS) {
                                    mtp2.trace("Failed check on sending SLTA, values do not match, remote SLTM/SLTA check will fail\n" + "remote OPC = " + remote_OPC + ", SLTA DPC = " + slta_DPC + ", remote DPC = " + remote_DPC + ", SLTA OPC = " + slta_OPC + ", remote SLS = " + remote_SLS + ", SLTA SLS = " + slta_SLS);

                                // FIXME: add failure to SLTM checks here?
                                }

                            }

                            mtp2.trace("Responding with SLTA");
                        }
                        //FIXME: check for return flag
                        mtp2.queue(slta);
                    } else if (h0 == 1 && h1 == 2) {
                        // receive SLTA from remote end
                        if (logger.isTraceEnabled() && isL3Debug()) {

                            mtp2.trace("Received SLTA");
                        }
                        // stop Q707 timer T1
                        mtp2.stop_T1_SLTM();
                        StringBuilder sb = new StringBuilder();
                        // check contidions for acceptance
                        boolean accepted = true;
/*                        if (opc != mtp2.getLinkSet().getDpc()) {
                            if (logger.isTraceEnabled() && isL3Debug()) {
                                sb.append("\nSLTA Acceptance failed, OPC = ").append(opc).append(" ,of SLTA does not match local DPC = ").append(
                                        mtp2.getLinkSet().getDpc());
                            }
                            accepted = false;
                        }

                        if (dpc != mtp2.getLinkSet().getOpc()) {
                            if (logger.isTraceEnabled() && isL3Debug()) {
                                sb.append("\nSLTA Acceptance failed, DPC = ").append(dpc).append(" ,of SLTA does not match local OPC = ").append(
                                        mtp2.getLinkSet().getOpc());
                            }
                            accepted = false;
                        }
*/
                        if (!checkPattern(sif, SLTM_PATTERN)) {
                            if (logger.isTraceEnabled() && isL3Debug()) {
                                sb.append("\nSLTA Acceptance failed, sif pattern = ").append(Arrays.toString(sif)).append(
                                        " ,of SLTA does not match local pattern = ").append(Arrays.toString(SLTM_PATTERN));
                            }
                            accepted = false;
                        }

                        if (sls != mtp2.getSls()) {
                            if (logger.isTraceEnabled() && isL3Debug()) {
                                sb.append("\nSLTA Acceptance failed, sls  = ").append(sls).append(" ,of SLTA does not match local sls = ").append(
                                        mtp2.getSls());
                            }
                            accepted = false;
                        }

                        if (accepted) {

                            // reset counter.
                            mtp2.restartSltmTries();


                            if (!l4IsUp && mtpUser != null) {
                                if (logger.isTraceEnabled() && isL3Debug()) {
                                    mtp2.trace("XXX Notify layer 4 on success SLTM handshake");
                                }
                                l4IsUp = true;
                                mtpUser.linkUp();
                            }
                        } else {
                            if (logger.isTraceEnabled() && isL3Debug()) {
                                mtp2.trace("SLTA acceptance failed!!! Reason: " + sb);
                            }
                            //ignore it
                        }

                    } else {

                        if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                            mtp2.trace("XXX Unexpected message type");
                        }
                    }
                    break;
                case SERVICE_SCCP:
                    if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates SCCP");
                    }

                    if (mtpUser != null) {
                        //lets create byte[] which is actuall upper layer msg.
                        //msbBuff.len = sif.len - 4 (routing label), after routing label there should be msg code
                        byte[] msgBuff = new byte[sif.length - 4];
                        System.arraycopy(sif, 4, msgBuff, 0, msgBuff.length);
//                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_SCCP, subserviceIndicator, msgBuff);
                    }
                    break;
                case SERVICE_ISUP:
                    if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates ISUP");
                    }
                    if (mtpUser != null) {
                        //lets create byte[] which is actuall upper layer msg.
                        //msbBuff.len = sif.len - 4 (routing label), after routing label there should be msg code
                        byte[] msgBuff = new byte[sif.length - 4];
                        System.arraycopy(sif, 4, msgBuff, 0, msgBuff.length);
//                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_ISUP, subserviceIndicator, msgBuff);
                    }
                    break;
                default:
                    if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates UNKNOWN SERVICE!!!!!!!!!!!: " + Utils.dump(sif, sif.length, false));
                    }
                    break;
            }
        }
    }
    // //////////////////
    // Helper methods //
    // //////////////////
    private static final int _getFromSif_DPC(byte[] sif, int shift) {
        int dpc = (sif[0 + shift] & 0xff | ((sif[1 + shift] & 0x3f) << 8));
        return dpc;
    }

    private static final int _getFromSif_OPC(byte[] sif, int shift) {
        int opc = ((sif[1 + shift] & 0xC0) >> 6) | ((sif[2 + shift] & 0xff) << 2) | ((sif[3 + shift] & 0x0f) << 10);
        return opc;
    }

    private static final int _getFromSif_SLS(byte[] sif, int shift) {
        int sls = (sif[3 + shift] & 0xf0) >>> 4;
        return sls;
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
