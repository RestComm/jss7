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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.Collection;
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
    private List<Mtp2> links = new ArrayList();
    
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
    
    private SelectorFactory selectorFactory;
    protected LinkSelector selector;
    
    //public Mtp3Impl(String name, Mtp1 layer1) {
    public Mtp3(String name) {
        this.name = name;
        
        //creating IO Thread.
        ioThread = new Thread(this, "MTP2");
        ioThread.setPriority(Thread.MAX_PRIORITY);
        
        processor = Executors.newSingleThreadScheduledExecutor();
    }

    public void setSelectorFactory(SelectorFactory selectorFactory) {
	this.selectorFactory = selectorFactory;
	selector = new LinkSelector(selectorFactory.getSelector());
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
    	    Mtp2 link = new Mtp2(this, channel, channel.getCode());
            this.links.add(link);
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
    	    selector.unregister(link);
            link.stopLink();
        }        
    }

    public void run() {
        while (started) {
            try {
        	Collection<Mtp2> selected = selector.select(LinkSelector.READ, 20);
                long thisTickStamp = System.currentTimeMillis();
                for (Mtp2 link : selected) {
                    //link.threadTick(thisTickStamp);
                    link.doRead();
                }
                
                selected = selector.select(LinkSelector.WRITE, 20);
                for (Mtp2 link : selected) {
            	    link.doWrite();
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
    
    public boolean send(byte[] msg) {  
    	//method expects proper message, lets pray its ok :/ - this is a way to be aligned with dialogic cards.
        //selecting link using sls
    	//get sls;
    	byte sls = (byte) _getFromSif_SLS(msg, 1);
        Mtp2 link = this.selectLink(sls);
        
        //creating message
        //byte[] buffer = new byte[5 + msg.length];
        //writeRoutingLabel(buffer, sls);
        
        //buffer[0] = (byte) ((service & 0x0F) | ((subservice & 0x0F) << 4));
        //System.arraycopy(msg, 0, buffer, 5, msg.length);
        //return link.queue(buffer);
        //FIXME: should we copy ?
        
        return link.queue(msg);
    }

    public void linkInService(Mtp2 link) {
        //restart traffic 
        if (logger.isDebugEnabled()) {
    	    logger.debug(String.format("(%s) Sending Traffic Restart Allowed message", link.name));
    	}
        restartTraffic(link);
        
        //create and assign Tester;
        Test tester = new Test(link);
        link.test = tester;
        
        //start tester
        if (logger.isDebugEnabled()) {
    	    logger.debug(String.format("(%s) Starting link test procedure", link.name));
        }
        tester.start();
    }

    /**
     * Notify that link is up.
     * @param link
     */
    public void linkUp(Mtp2 link) {
        linkset.add(link);
        if (linkset.isActive() && mtpUser != null) {
            mtpUser.linkUp();
        }
        logger.info(String.format("(%s) Link now IN_SERVICE", link.name));
    }
    
    public void linkFailed(Mtp2 link) {
        //remove this link from list of active links
        linkset.remove(link);

        //restart initial alignment after T17 expires
        link.failLink();

        //notify mtp user part
        if (!linkset.isActive() && mtpUser != null) {
            mtpUser.linkDown();
        }
    }

    private void restartTraffic(Mtp2 link) {
        int subservice = link.getSubService();
        //if (subservice == -1) {
        subservice = DEFAULT_SUB_SERVICE_TRA;
        //}
        byte[] buffer = new byte[6];
        writeRoutingLabel(buffer, 0,0,0,dpc,opc);
        // buffer[0] = (byte) (_SERVICE_TRA | ( subservice << 4));
        buffer[0] = (byte) 0xC0;
        // H0 and H1, see Q.704 section 15.11.2+
        buffer[5] = 0x17;
        link.queue(buffer);
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
            test = mtpTimer.schedule(this, Mtp3.TIMEOUT_T2_SLTM, TimeUnit.SECONDS);
            if (logger.isDebugEnabled()) {
        	logger.debug(String.format("(%s) Test message acknowledged, Link test passed", link.name));
            }
        }
        
        /**
         * Sends SLTM message using this link.
         * 
         * @param timeout  the amount of time in millesecond for awaiting response.
         */
        public void ping(long timeout) {
            //prepearing test message
            
            writeRoutingLabel(sltm, 0,0,link.sls,dpc,opc);
            sltm[0] = (byte) 0xC1; // 1100 0001
            sltm[5] = 0x11;
            sltm[6] = (byte) (SLTM_PATTERN.length << 4);
            System.arraycopy(SLTM_PATTERN, 0, sltm, 7, SLTM_PATTERN.length);
            
            //sending test message
            link.queue(sltm);
            
            //incremeting number of tries.
            tryCount++;
            
            //scheduling timeout
            test = mtpTimer.schedule(this, timeout, TimeUnit.SECONDS);
            if (logger.isDebugEnabled()) {
        	logger.debug(String.format("(%s) Test request, try number = %d", link.name, tryCount));
            }
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
            	    if (logger.isDebugEnabled()) {
            		logger.debug(String.format("(%s) SLTM message was not acknowledged, Link failed", link.name));
            	    }
                    //second message was not answered, report failure
                    linkFailed(link);
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

            if (logger.isTraceEnabled()) {
                logger.trace(String.format("(%s) Received MSSU [si=" + serviceIndicator + ",ssi=" + subserviceIndicator + ", dpc=" + dpc + ", opc=" + opc + ", sls=" + sls + "]", mtp2.name));
            }
            switch (serviceIndicator) {
                case LINK_MANAGEMENT:
                    int h0 = sif[4] & 0x0f;
                    int h1 = (sif[4] & 0xf0) >>> 4;
                    
                    if (logger.isDebugEnabled()) {
                	logger.debug(String.format("(%s) Signalling network management", mtp2.name));
                    }
                    
                    if (h0 == 0) {
                	if (logger.isDebugEnabled()) {
                	    logger.debug(String.format("(%s) Changeover management", mtp2.name));
                	}
                    } else if (h0 == 7 && h1 == 1) {
                	if (logger.isDebugEnabled()) {
                	    logger.debug(String.format("(%s) TRA received", mtp2.name));
                	}
                    }
                    //FIX ME
                    break;
                case LINK_TESTING:
                    h0 = sif[4] & 0x0f;
                    h1 = (sif[4] & 0xf0) >>> 4;

                    int len = (sif[5] & 0xf0) >>> 4;

                    if (h0 == 1 && h1 == 1) {
                        if (logger.isTraceEnabled()) {
                            logger.trace(String.format("(%s) Received SLTM", mtp2.name));
                        }
                        // receive SLTM from remote end
                        // create response
                        byte[] slta = new byte[len + 7];
    
                        

                        writeRoutingLabel(slta,0,0, sls,dpc,opc);
                        slta[0] = (byte) sio;
                        slta[5] = 0x021;
                        // +1 cause we copy LEN byte also.
                        System.arraycopy(sif, 5, slta, 6, len + 1);

			if (logger.isTraceEnabled()) {
                            logger.trace(String.format("(%s) Responding with SLTA", mtp2.name));
                        }
                        mtp2.queue(slta);
                    } else if (h0 == 1 && h1 == 2) {
                        // receive SLTA from remote end
                        if (logger.isTraceEnabled()) {
                            logger.trace(String.format("(%s) Received SLTA", mtp2.name));
                        }
                        
                        //checking pattern
                        if (checkPattern(sif, SLTM_PATTERN)) {
                    	    //test message is acknowledged
                    	    mtp2.test.ack();
                    	    
                    	    //notify top layer that link is up
                    	    if (!l4IsUp) {
                    		l4IsUp = true;
                    		linkUp(mtp2);
                    	    }
                        }
                    } else {
            		logger.warn(String.format("(%s) Unexpected message type", mtp2.name));
                    }
                    break;
                case SERVICE_SCCP:
                    if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates SCCP");
                    }

                    if (mtpUser != null) {
                        //lets create byte[] which is actuall upper layer msg.
                        //msbBuff.len = sif.len - 4 (routing label), after routing label there should be msg code
                        //byte[] msgBuff = new byte[sif.length - 4];
                        //System.arraycopy(sif, 4, msgBuff, 0, msgBuff.length);
//                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_SCCP, subserviceIndicator, msgBuff);
                    	byte[] message = new byte[sif.length+1];
                    	System.arraycopy(sif, 0, message, 1, sif.length);
                    	message[0] = (byte) sio;
                    	mtpUser.receive(message);
                    }
                    break;
                case SERVICE_ISUP:
                    if (logger.isEnabledFor(Level.WARN) && isL3Debug()) {
                        mtp2.trace("XXX MSU Indicates ISUP");
                    }
                    if (mtpUser != null) {
                        //lets create byte[] which is actuall upper layer msg.
                        //msbBuff.len = sif.len - 4 (routing label), after routing label there should be msg code
                        //byte[] msgBuff = new byte[sif.length - 4];
                        //System.arraycopy(sif, 4, msgBuff, 0, msgBuff.length);
//                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_ISUP, subserviceIndicator, msgBuff);
                    	byte[] message = new byte[sif.length+1];
                    	System.arraycopy(sif, 0, message, 1, sif.length);
                    	message[0] = (byte) sio;
                    	mtpUser.receive(message);
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
    public static final int _getFromSif_DPC(byte[] sif, int shift) {
        int dpc = (sif[0 + shift] & 0xff | ((sif[1 + shift] & 0x3f) << 8));
        return dpc;
    }

    public static final int _getFromSif_OPC(byte[] sif, int shift) {
        int opc = ((sif[1 + shift] & 0xC0) >> 6) | ((sif[2 + shift] & 0xff) << 2) | ((sif[3 + shift] & 0x0f) << 10);
        return opc;
    }

    public static final int _getFromSif_SLS(byte[] sif, int shift) {
        int sls = (sif[3 + shift] & 0xf0) >>> 4;
        return sls;
    }    
    public static final int _getFromSif_SI(byte[] data)
    {
    	
        int serviceIndicator = data[0] & 0x0f;
    	return serviceIndicator;
    }
    
    public static final int _getFromSif_SSI(byte[] data)
    {
    	int subserviceIndicator = (data[0] >> 4) & 0x03;
    	return subserviceIndicator;
    }
    
    public static  void writeRoutingLabel(byte[] data,int si, int ssi, int sls, int dpc,int opc) {
    	data[0] = (byte) (((ssi & 0x03) << 4) | (si & 0x0F));
    	data[1] = (byte) dpc;
    	data[2] = (byte) (((dpc >> 8) & 0x3F) | ((opc & 0x03) << 6));
    	data[3] = (byte) (opc >> 2);
    	data[4] = (byte) (((opc >> 10) & 0x0F) | ((sls & 0x0F) << 4));
        //sif[4] = (byte) (((opc>> 10) & 0x0F) | ((0 & 0x0F) << 4));
    }   
    ////////////////
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
