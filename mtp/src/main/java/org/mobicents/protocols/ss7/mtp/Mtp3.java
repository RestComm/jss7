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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

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
    
    protected MtpUser mtpUser;
    protected List<LinkSet> linkSets = new ArrayList<LinkSet>();
    private final MTPThreadFactory mainThreadFactory;
    protected Thread mainThread = null;
    /**
     * Event processor thread, this should be as low as possible :)
     */
    private final MTPThreadFactory processorThreadFactory;
    private final ScheduledExecutorService _PROCESSOR;
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

    //public Mtp3Impl(String name, Mtp1 layer1) {
    public Mtp3(String name) {
        this.name = name;
        this.mainThreadFactory = new MTPThreadFactory("MTP3TickThread[" + name + "]", Thread.MAX_PRIORITY);
        this.processorThreadFactory = new MTPThreadFactory("MTP3ProcessorThread[" + name + "]", Thread.MIN_PRIORITY);
        this._PROCESSOR = Executors.newSingleThreadScheduledExecutor(processorThreadFactory);
    }

    protected void setOpc(int opc) {
        this.opc = opc;
    }

    protected void setDpc(int dpc) {
        this.dpc = dpc;
    }
    
    /**
     * Assigning channels.
     * 
     * @param channels the list of signaling channels.
     */
    protected void setChannels(List<Mtp1> channels) {
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

    public void setLinkSets(List<LinkSet> linkSets) {

        //FIXME: should this be better?
        //just add, sort and fix indexes if needed to optimize searches.
        for (LinkSet ls : linkSets) {
            this.linkSets.add(ls);
            for (Mtp2 mtp2 : ls.getLinks()) {
                mtp2.setLayer3(this);
                T1Action_SLTM sltm1 = new T1Action_SLTM();
                T2Action_SLTM sltm2 = new T2Action_SLTM();
                sltm1.mtp2 = mtp2;
                sltm2.mtp2 = mtp2;
                mtp2.setT1_SLTMTimerAction(sltm1);
                mtp2.setT2_SLTMTimerAction(sltm2);
            }
        }

        Collections.sort(this.linkSets, new LinkSetComparator());
        for (int i = 0; i < this.linkSets.size(); i++) {
            LinkSet ls = this.linkSets.get(i);
            if (i != ls.getId()) {
                throw new IllegalArgumentException("Linkset ID[" + ls.getId() + "] of link[" + ls.getName() + "] does not equal index, change id to be in range of linkset count <0,n-1>.");
            }
        }

    }

    public void start() throws IOException {
        //initilaize debugger
        Utils.getInstance().startDebug();

        //starting links
        for (Mtp2 link : links) {
            link._startLink();
        }
        
        for (LinkSet ls : this.linkSets) {
            for (Mtp2 mtp2 : ls.getLinks()) {

                mtp2._startLink();

            }
        }

        manageLinkSet();
        started = true;
        mainThread = mainThreadFactory.newThread(this);

        mainThread.start();

    }

    public void stop() {
        started = false;
        for (LinkSet ls : this.linkSets) {
            for (Mtp2 mtp2 : ls.getLinks()) {
                mtp2._stopLink();
            }
        }
        mainThread.stop();
        Utils.getInstance().stopDebug();
    }

    public void run() {
        while (started) {
            try {
                long thisTickStamp = System.currentTimeMillis();

                for (LinkSet ls : this.linkSets) {
                    ls.threadTick(thisTickStamp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 
     * @param sio
     *            service information octet.
     * @param msg
     *            service information field;
     */
    public void onMessage(int sio, byte[] sif, Mtp2 mtp2) {

        ProcessorClass pc = new ProcessorClass();
        pc.sio = sio;
        pc.sif = sif;
        pc.mtp2 = mtp2;
        _PROCESSOR.execute(pc);
    }
    private final int counter = 0;

    public boolean send(byte sls, byte linksetId, int service, int subservice, byte[] msg) {

        if (!this.l4IsUp) {
            //??
            return false;
        }
        Mtp2 mtp2 = findLink(sls, linksetId);
        byte[] buffer = new byte[5 + msg.length];
        writeRoutingLabel(buffer, mtp2);
        buffer[0] = (byte) ((service & 0x0F) | ((subservice & 0x0F) << 4));
        System.arraycopy(msg, 0, buffer, 5, msg.length);
        if (isL3Debug()) {
            mtp2.trace("Scheduling MSU: " + Utils.dump(buffer, buffer.length, false));
        }
        return mtp2.queue(buffer);

    }

    private Mtp2 findLink(byte sls, byte linksetId) {

        LinkSet ls = findLinkSet(linksetId);
        List<Mtp2> activeLinksInLinkSet = ls.getActiveLinks();
        Mtp2 found = null;
        if (sls > 15 || sls < 0 || sls > activeLinksInLinkSet.size()) {
            if (isL3Debug()) {
                Utils.getInstance().append("Passed sls value[" + sls + "] is not good, determine arbitrary.");
            }

            int index = counter % activeLinksInLinkSet.size();
            found = activeLinksInLinkSet.get(index);
        } else {
            found = activeLinksInLinkSet.get(sls);
        }

        return found;
    }

    private LinkSet findLinkSet(byte linksetId) {
        if (linksetId > this.linkSets.size() || linksetId < 0) {
            if (isL3Debug()) {
                Utils.getInstance().append("Passed linkset value[" + linksetId + "] is not good, determine arbitrary.");
            }
            linksetId = (byte) (linksetId % this.linkSets.size());
        }

        return this.linkSets.get(linksetId);
    }

    protected void manageLinkSet() {

        for (LinkSet linkSet : this.linkSets) {
            if (linkSet.getActiveLinks().size() != linkSet.getLinks().size()) //if(this.linkSet_notactive.size()>0)
            {
                linkSet.activateLink();
                break;
            }
        }
    }

    public void linkInService(Mtp2 mtp2) {
        this.resetForInservice(mtp2);
        //this.activeLinksInLinkSet.add(mtp2);
        sendTRA(mtp2);
        sendSLTM(mtp2);
        manageLinkSet();
    // schedule SLTM.
    // start ACK supervision and redo of SLTM procedure after time.
    }

    public void linkFailed(Mtp2 mtp2) {

        resetForInservice(mtp2);
        //this.activeLinksInLinkSet.remove(mtp2);
        //mtp2.start_T17();
        //this.linkSet_notactive.put(mtp2.getSls(),mtp2);
        //if(this.activeLinksInLinkSet.size()==0)
        boolean hasUplinks = false;
        for (LinkSet linkSet : this.linkSets) {
            if (linkSet.getActiveLinks().size() > 0) {
                hasUplinks = true;
                break;
            }
        }
        if (!hasUplinks) {
            this.l4IsUp = false;
            if (this.mtpUser != null) {
                this.mtpUser.linkDown();
            }
        }
        manageLinkSet();
    }

    private void resetForInservice(Mtp2 mtp2) {
        mtp2.stop_T1_SLTM();
        mtp2.stop_T2_SLTM();
        mtp2.restartSltmTries();

    }

    private void sendTRA(Mtp2 mtp2) {
        int subservice = mtp2.getSubService();
        //if (subservice == -1) {
        subservice = DEFAULT_SUB_SERVICE_TRA;
        //}
        byte[] buffer = new byte[6];
        writeRoutingLabel(buffer, mtp2);
        // buffer[0] = (byte) (_SERVICE_TRA | ( subservice << 4));
        buffer[0] = (byte) 0xC0;
        // H0 and H1, see Q.704 section 15.11.2+
        buffer[5] = 0x17;
        if (logger.isDebugEnabled() && isL3Debug()) {
            // logger.debug("Link (" + name + ") Queue SLTM");
            mtp2.trace("Queue TRA");
        }
        mtp2.queue(buffer);
    }

    private void sendSLTM(Mtp2 mtp2) {
        //lets stop
        //int subservice = mtp2.getSubService();
        //if (subservice == -1) {
        //	subservice = _DEFAULT_SUB_SERVICE_TRA;
        //}
        byte[] sltm = new byte[7 + SLTM_PATTERN.length];
        //sltm[0] = (byte) (_SERVICE_SLTM | ( subservice << 4));
        // sltm[0] = _SERVICE_SLTM;
        sltm[0] = (byte) 0xC1; // 1100 0001
        writeRoutingLabel(sltm, mtp2);
        sltm[5] = 0x11;
        sltm[6] = (byte) (SLTM_PATTERN.length << 4);
        System.arraycopy(SLTM_PATTERN, 0, sltm, 7, SLTM_PATTERN.length);

        if (logger.isDebugEnabled() && isL3Debug()) {
            // logger.debug("Link (" + name + ") Queue SLTM");
            mtp2.trace("Queue SLTM");
        }
        mtp2.queue(sltm);
        mtp2.incrementSltmTries();

        if (!mtp2.isT1_SLTM()) {
            mtp2.start_T1_SLTM();
        //restart?
        }
        mtp2.start_T2_SLTM();

    }

    private void writeRoutingLabel(byte[] sif, Mtp2 mtp2) {
        LinkSet ls = mtp2.getLinkSet();
        int destinationPointCode = ls.getDpc();
        int originationPointCode = ls.getOpc();
        sif[1] = (byte) destinationPointCode;
        sif[2] = (byte) (((destinationPointCode >> 8) & 0x3F) | ((originationPointCode & 0x03) << 6));
        sif[3] = (byte) (originationPointCode >> 2);
        //sif[4] = (byte) (((originationPointCode >> 10) & 0x0F) | ((mtp2.getSls() & 0x0F) << 4));
        sif[4] = (byte) (((originationPointCode >> 10) & 0x0F) | ((0 & 0x0F) << 4));
    }    // -6 cause we have in sif, sio+label+len of pattern - thats 1+4+1 = 6
    private final static int SIF_PATTERN_OFFSET = 6;

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

    // /////////////////////////////
    // Timers and Future handles //
    // /////////////////////////////
    private class T1Action_SLTM implements Runnable {

        private Mtp2 mtp2;

        public void run() {
            // so we can cleanly reschedule.
            //T1_SLTM = null;
            performSLTARetryProcedure(mtp2);
        }
    }

    private class T2Action_SLTM implements Runnable {

        private Mtp2 mtp2;

        public void run() {
            sendSLTM(mtp2);
        }
    }

    private void performSLTARetryProcedure(Mtp2 mtp2) {
        if (mtp2.getSltmTries() == 1) {
            // we have second chance.
            if (logger.isEnabledFor(Level.ERROR) && isL3Debug()) {
                mtp2.trace("No valid SLTA received within Q.707_T1, trying again.");
            }
            sendSLTM(mtp2);
        } else {
            // this is failure, link must go down.
            if (logger.isEnabledFor(Level.ERROR) && isL3Debug()) {
                mtp2.trace("No valid SLTA received within Q.707_T1, faulting link.....");
            }

            mtp2.restartSltmTries();
            mtp2.failLink();
            //this will start IAM once more and rest buffers.
            mtp2.start_T17();

        }

    }

    private final class ProcessorClass implements Runnable {

        private Mtp2 mtp2;
        private int sio;
        private byte[] sif;

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

                        writeRoutingLabel(slta, mtp2);
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

                            // logger.debug("Responding with SLTA");
                            if (logger.isDebugEnabled() && isL3Debug()) {
                                mtp2.trace("Responding with SLTA");
                            }
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
                        if (opc != mtp2.getLinkSet().getDpc()) {
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
                            performSLTARetryProcedure(mtp2);
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
                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_SCCP, subserviceIndicator, msgBuff);
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
                        mtpUser.receive(mtp2.getSls(), mtp2.getLinkSet().getId(), SERVICE_ISUP, subserviceIndicator, msgBuff);
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

    private class LinkSetComparator implements Comparator<LinkSet> {

        public int compare(LinkSet o1, LinkSet o2) {
            // TODO Auto-generated method stub
            if (o2 == null) {
                return 1;
            } else if (o1 == null) {
                return -1;
            } else {
                return o1.getId() - o2.getId();
            }

        }
    }
}
