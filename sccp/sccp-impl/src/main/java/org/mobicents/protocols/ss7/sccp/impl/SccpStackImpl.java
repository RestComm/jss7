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

package org.mobicents.protocols.ss7.sccp.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javolution.text.TextBuilder;
import javolution.util.FastMap;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.mtp.Mtp3;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPartListener;
import org.mobicents.protocols.ss7.sccp.LongMessageRule;
import org.mobicents.protocols.ss7.sccp.LongMessageRuleType;
import org.mobicents.protocols.ss7.sccp.Mtp3ServiceAccessPoint;
import org.mobicents.protocols.ss7.sccp.RemoteSignalingPointCode;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.Rule;
import org.mobicents.protocols.ss7.sccp.SccpManagementEventListener;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpResource;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.impl.message.MessageFactoryImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpAddressedMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpSegmentableMessageImpl;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SccpAddressCodec;
import org.mobicents.protocols.ss7.sccp.impl.parameter.SegmentationImpl;
import org.mobicents.protocols.ss7.sccp.impl.router.RouterImpl;
import org.mobicents.protocols.ss7.sccp.parameter.GlobalTitle;
import org.mobicents.protocols.ss7.sccp.parameter.ReturnCauseValue;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 *
 * @author amit bhayani
 * @author baranowb
 * @author sergey vetyutnev
 *
 */
public class SccpStackImpl implements SccpStack, Mtp3UserPartListener {
    protected final Logger logger;

    protected static final String SCCP_MANAGEMENT_PERSIST_DIR_KEY = "sccpmanagement.persist.dir";
    protected static final String USER_DIR_KEY = "user.dir";
    protected static final String PERSIST_FILE_NAME = "management2.xml";
    private static final String TAB_INDENT = "\t";
    private static final String CLASS_ATTRIBUTE = "type";

    private static final String Z_MARGIN_UDT_MSG = "zmarginxudtmessage";
    private static final String REASSEMBLY_TIMER_DELAY = "reassemblytimerdelay";
    private static final String MAX_DATA_MSG = "maxdatamessage";
    private static final String REMOVE_SPC = "removespc";
    private static final String PREVIEW_MODE = "previewMode";
    private static final String SST_TIMER_DURATION_MIN = "ssttimerduration_min";
    private static final String SST_TIMER_DURATION_MAX = "ssttimerduration_max";
    private static final String SST_TIMER_DURATION_INCREASE_FACTOR = "ssttimerduration_increasefactor";

    private static final XMLBinding binding = new XMLBinding();

    // If the XUDT message data length greater this value, segmentation is
    // needed
    // TODO: make it configurable
    protected int zMarginXudtMessage = 240;
    // sccp segmented message reassembling timeout
    // TODO: make it configurable
    protected int reassemblyTimerDelay = 15000;
    // Max available Sccp message data for all messages
    // TODO: make it configurable
    protected int maxDataMessage = 2560;
    // remove PC from calledPartyAddress when sending to MTP3
    // TODO: make it configurable
    private boolean removeSpc = true;
    // min (starting) SST sending interval (millisec)
    // TODO: make it configurable
    protected int sstTimerDuration_Min = 10000;
    // max (after increasing) SST sending interval (millisec)
    // TODO: make it configurable
    protected int sstTimerDuration_Max = 600000;
    // multiplicator of SST sending interval (next interval will be greater the
    // current by sstTimerDuration_IncreaseFactor)
    // TODO: make it configurable
    protected double sstTimerDuration_IncreaseFactor = 1.5;

    private boolean previewMode = false;

    protected volatile State state = State.IDLE;

    // provider ref, this can be real provider or pipe, for tests.
    protected SccpProviderImpl sccpProvider;

    protected RouterImpl router;
    protected SccpResourceImpl sccpResource;

    protected MessageFactoryImpl messageFactory;

    protected SccpManagement sccpManagement;
    protected SccpRoutingControl sccpRoutingControl;

    protected FastMap<Integer, Mtp3UserPart> mtp3UserParts = new FastMap<Integer, Mtp3UserPart>();
    protected ScheduledExecutorService timerExecutors;
    protected FastMap<MessageReassemblyProcess, SccpSegmentableMessageImpl> reassemplyCache = new FastMap<MessageReassemblyProcess, SccpSegmentableMessageImpl>();

    // protected int localSpc;
    // protected int ni = 2;

    protected final String name;

    protected final TextBuilder persistFile = TextBuilder.newInstance();

    protected String persistDir = null;

    private volatile int segmentationLocalRef = 0;
    private volatile int slsCounter = 0;
    private volatile int selectorCounter = 0;

    public SccpStackImpl(String name) {

        binding.setClassAttribute(CLASS_ATTRIBUTE);

        this.name = name;
        this.logger = Logger.getLogger(SccpStackImpl.class.getCanonicalName() + "-" + this.name);

        this.state = State.CONFIGURED;
    }

    public String getName() {
        return this.name;
    }

    public String getPersistDir() {
        return persistDir;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#getSccpProvider()
     */
    public SccpProvider getSccpProvider() {
        return sccpProvider;
    }

    public Map<Integer, Mtp3UserPart> getMtp3UserParts() {
        return mtp3UserParts;
    }

    public void setMtp3UserParts(Map<Integer, Mtp3UserPart> mtp3UserPartsTemp) {
        if (mtp3UserPartsTemp != null) {
            synchronized (this) {
                FastMap<Integer, Mtp3UserPart> newMtp3UserPart = new FastMap<Integer, Mtp3UserPart>();
                newMtp3UserPart.putAll(mtp3UserPartsTemp);
                this.mtp3UserParts = newMtp3UserPart;
            }
        }
    }

    public Mtp3UserPart getMtp3UserPart(int id) {
        return mtp3UserParts.get(id);
    }

    public void setMtp3UserPart(int id, Mtp3UserPart mtp3UserPart) {
        if (mtp3UserPart == null) {
            this.removeMtp3UserPart(id);
        } else {
            synchronized (this) {
                FastMap<Integer, Mtp3UserPart> newMtp3UserPart = new FastMap<Integer, Mtp3UserPart>();
                newMtp3UserPart.putAll(this.mtp3UserParts);
                newMtp3UserPart.put(id, mtp3UserPart);
                this.mtp3UserParts = newMtp3UserPart;
            }
        }
    }

    public void removeMtp3UserPart(int id) {
        synchronized (this) {
            FastMap<Integer, Mtp3UserPart> newMtp3UserPart = new FastMap<Integer, Mtp3UserPart>();
            newMtp3UserPart.putAll(this.mtp3UserParts);
            newMtp3UserPart.remove(id);
            this.mtp3UserParts = newMtp3UserPart;
        }
    }

    public void setRemoveSpc(boolean removeSpc) {
        this.removeSpc = removeSpc;

        this.store();
    }

    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;

        this.store();
    }

    public void setSstTimerDuration_Min(int sstTimerDuration_Min) {
        // 5-10 seconds
        if (sstTimerDuration_Min < 5000)
            sstTimerDuration_Min = 5000;
        if (sstTimerDuration_Min > 10000)
            sstTimerDuration_Min = 10000;
        this.sstTimerDuration_Min = sstTimerDuration_Min;

        this.store();
    }

    public void setSstTimerDuration_Max(int sstTimerDuration_Max) {
        // 10-20 minutes
        if (sstTimerDuration_Max < 600000)
            sstTimerDuration_Max = 600000;
        if (sstTimerDuration_Max > 1200000)
            sstTimerDuration_Max = 1200000;
        this.sstTimerDuration_Max = sstTimerDuration_Max;

        this.store();
    }

    public void setSstTimerDuration_IncreaseFactor(double sstTimerDuration_IncreaseFactor) {
        // acceptable factor from 1 to 4
        if (sstTimerDuration_IncreaseFactor < 1)
            sstTimerDuration_IncreaseFactor = 1;
        if (sstTimerDuration_IncreaseFactor > 4)
            sstTimerDuration_IncreaseFactor = 4;
        this.sstTimerDuration_IncreaseFactor = sstTimerDuration_IncreaseFactor;

        this.store();
    }

    public boolean isRemoveSpc() {
        return this.removeSpc;
    }

    public boolean isPreviewMode() {
        return this.previewMode;
    }

    public int getSstTimerDuration_Min() {
        return this.sstTimerDuration_Min;
    }

    public int getSstTimerDuration_Max() {
        return this.sstTimerDuration_Max;
    }

    public double getSstTimerDuration_IncreaseFactor() {
        return this.sstTimerDuration_IncreaseFactor;
    }

    public int getZMarginXudtMessage() {
        return zMarginXudtMessage;
    }

    public void setZMarginXudtMessage(int zMarginXudtMessage) {
        // value from 160 to 255 bytes
        if (zMarginXudtMessage < 160)
            zMarginXudtMessage = 160;
        if (zMarginXudtMessage > 255)
            zMarginXudtMessage = 255;
        this.zMarginXudtMessage = zMarginXudtMessage;

        this.store();
    }

    public int getMaxDataMessage() {
        return maxDataMessage;
    }

    public void setMaxDataMessage(int maxDataMessage) {
        // from 2560 to 3952 bytes
        if (maxDataMessage < 2560)
            maxDataMessage = 2560;
        if (maxDataMessage > 3952)
            maxDataMessage = 3952;
        this.maxDataMessage = maxDataMessage;

        this.store();
    }

    public int getReassemblyTimerDelay() {
        return this.reassemblyTimerDelay;
    }

    public void setReassemblyTimerDelay(int reassemblyTimerDelay) {
        // from 10 to 20 seconds
        if (reassemblyTimerDelay < 10000)
            reassemblyTimerDelay = 10000;
        if (reassemblyTimerDelay > 20000)
            reassemblyTimerDelay = 20000;
        this.reassemblyTimerDelay = reassemblyTimerDelay;

        this.store();
    }

    public synchronized int newSegmentationLocalRef() {
        return ++this.segmentationLocalRef;
    }

    public synchronized int newSls() {
        if (++this.slsCounter > 255)
            this.slsCounter = 0;
        return this.slsCounter;
    }

    public synchronized boolean newSelector() {
        if (++this.selectorCounter > 1)
            this.selectorCounter = 0;
        return (this.selectorCounter == 1);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
     */
    public void start() throws IllegalStateException {
        logger.info("Starting ...");

        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(SCCP_MANAGEMENT_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY)))
                    .append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        }

        logger.info(String.format("SCCP Management configuration file path %s", persistFile.toString()));

        try {
            this.load();
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the Sccp Management configuration file. \n%s", e.getMessage()));
        }

        this.messageFactory = new MessageFactoryImpl(this);

        this.sccpProvider = new SccpProviderImpl(this);
        // FIXME: make this configurable
        this.sccpManagement = new SccpManagement(name, sccpProvider, this);
        this.sccpRoutingControl = new SccpRoutingControl(sccpProvider, this);

        this.sccpManagement.setSccpRoutingControl(sccpRoutingControl);
        this.sccpRoutingControl.setSccpManagement(sccpManagement);

        this.router = new RouterImpl(this.name, this);
        this.router.setPersistDir(this.persistDir);
        this.router.start();

        this.sccpResource = new SccpResourceImpl(this.name);
        this.sccpResource.setPersistDir(this.persistDir);
        this.sccpResource.start();

        logger.info("Starting routing engine...");
        this.sccpRoutingControl.start();
        logger.info("Starting management ...");
        this.sccpManagement.start();
        logger.info("Starting MSU handler...");

        this.timerExecutors = Executors.newScheduledThreadPool(1);

        for (FastMap.Entry<Integer, Mtp3UserPart> e = this.mtp3UserParts.head(), end = this.mtp3UserParts.tail(); (e = e
                .getNext()) != end;) {
            Mtp3UserPart mup = e.getValue();
            mup.addMtp3UserPartListener(this);
        }

        for (SccpManagementEventListener lstr : this.sccpProvider.managementEventListeners) {
            try {
                lstr.onServiceStarted();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStarted", ee);
            }
        }

        this.state = State.RUNNING;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
     */
    public void stop() {
        logger.info("Stopping ...");
        // stateLock.lock();
        // try
        // {
        this.state = State.IDLE;
        // executor = null;
        //
        // layer3exec = null;

        for (SccpManagementEventListener lstr : this.sccpProvider.managementEventListeners) {
            try {
                lstr.onServiceStopped();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onServiceStopped", ee);
            }
        }

        for (FastMap.Entry<Integer, Mtp3UserPart> e = this.mtp3UserParts.head(), end = this.mtp3UserParts.tail(); (e = e
                .getNext()) != end;) {
            Mtp3UserPart mup = e.getValue();
            mup.removeMtp3UserPartListener(this);
        }

        logger.info("Stopping management...");
        this.sccpManagement.stop();
        logger.info("Stopping routing engine...");
        this.sccpRoutingControl.stop();
        logger.info("Stopping MSU handler...");

        this.sccpResource.stop();

        this.router.stop();

        synchronized (reassemplyCache) {
            this.timerExecutors.shutdownNow();
            reassemplyCache.clear();
        }

        this.store();

        // }finally
        // {
        // stateLock.unlock();
        // }

    }

    public Router getRouter() {
        return this.router;
    }

    public SccpResource getSccpResource() {
        return sccpResource;
    }

    protected enum State {
        IDLE, CONFIGURED, RUNNING;
    }

    public int calculateUdtFieldsLengthWithoutData(int calledPartyLen, int callingPartyLen) {
        // 8 = 2 (fixed fields length) + 3 (variable fields pointers) + 3
        // (variable fields lengths)
        return 8 + calledPartyLen + callingPartyLen;
    }

    public int calculateXudtFieldsLengthWithoutData(int calledPartyLen, int callingPartyLen, boolean segmented,
            boolean importancePresense) {
        // 10 = 3 (fixed fields length) + 4 (variable fields pointers) + 3
        // (variable fields lengths)
        int res = 10 + calledPartyLen + callingPartyLen;
        if (segmented || importancePresense)
            res++; // optional part present - adding End of optional parameters
        if (segmented)
            res += 6;
        if (importancePresense)
            res += 3;

        return res;
    }

    public int calculateXudtFieldsLengthWithoutData2(int calledPartyLen, int callingPartyLen) {
        int res = 254 - (3 + calledPartyLen + callingPartyLen);
        return res;
    }

    public int calculateLudtFieldsLengthWithoutData(int calledPartyLen, int callingPartyLen, boolean segmented,
            boolean importancePresense) {
        // 15 = 3 (fixed fields length) + 8 (variable fields pointers) + 4 (variable fields lengths)
        int res = 15 + calledPartyLen + callingPartyLen;
        if (segmented || importancePresense)
            res++; // optional part present - adding End of optional parameters
        if (segmented)
            res += 6;
        if (importancePresense)
            res += 3;

        return res;
    }

    protected void send(SccpDataMessageImpl message) throws IOException {

        if (this.state != State.RUNNING) {
            logger.error("Trying to send SCCP message from SCCP user but SCCP stack is not RUNNING");
            return;
        }

        if (message.getCalledPartyAddress() == null || message.getCallingPartyAddress() == null || message.getData() == null
                || message.getData().length == 0) {
            throw new IOException("Message to send must has filled CalledPartyAddress, CallingPartyAddress and data fields");
        }

        try {
            this.sccpRoutingControl.routeMssgFromSccpUser(message);
        } catch (IOException e) {
            // log here Exceptions from MTP3 level
            logger.error("IOException when sending the message to MTP3 level: " + e.getMessage(), e);
            e.printStackTrace();
            throw e;
        }
    }

    protected int getMaxUserDataLength(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {

        GlobalTitle gt = calledPartyAddress.getGlobalTitle();
        int dpc = calledPartyAddress.getSignalingPointCode();
        int ssn = calledPartyAddress.getSubsystemNumber();

        if (calledPartyAddress.getAddressIndicator().pcPresent()) {
            if (this.router.spcIsLocal(dpc)) {
                if (ssn > 0) {
                    // local destination - unlimited length
                    return this.getMaxDataMessage();
                } else if (gt != null) {
                    return getMaxUserDataLengthForGT(calledPartyAddress, callingPartyAddress);
                } else {
                    return 0;
                }
            } else {
                return getMaxUserDataLengthForDpc(dpc, calledPartyAddress, callingPartyAddress);
            }
        } else {
            if (gt != null) {
                return getMaxUserDataLengthForGT(calledPartyAddress, callingPartyAddress);
            } else {
                return 0;
            }
        }
    }

    private int getMaxUserDataLengthForDpc(int dpc, SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {

        LongMessageRule lmr = this.router.findLongMessageRule(dpc);
        LongMessageRuleType lmrt = LongMessageRuleType.LongMessagesForbidden;
        if (lmr != null)
            lmrt = lmr.getLongMessageRuleType();
        Mtp3ServiceAccessPoint sap = this.router.findMtp3ServiceAccessPoint(dpc, 0);
        if (sap == null) {
            return 0;
        }
        Mtp3UserPart mup = this.getMtp3UserPart(sap.getMtp3Id());
        if (mup == null) {
            return 0;
        }

        try {
            int fieldsLen = 0;
            byte[] cdp = SccpAddressCodec.encode(calledPartyAddress, this.isRemoveSpc());
            byte[] cnp = SccpAddressCodec.encode(callingPartyAddress, this.isRemoveSpc());
            switch (lmrt) {
                case LongMessagesForbidden:
                    fieldsLen = this.calculateUdtFieldsLengthWithoutData(cdp.length, cnp.length);
                    break;
                case LudtEnabled:
                case LudtEnabled_WithSegmentationField:
                    fieldsLen = this.calculateLudtFieldsLengthWithoutData(cdp.length, cnp.length, true, true);
                    break;
                case XudtEnabled:
                    fieldsLen = this.calculateXudtFieldsLengthWithoutData(cdp.length, cnp.length, true, true);
                    int fieldsLen2 = this.calculateXudtFieldsLengthWithoutData2(cdp.length, cnp.length);
                    if (fieldsLen > fieldsLen2)
                        fieldsLen = fieldsLen2;
                    break;
            }

            int availLen = mup.getMaxUserDataLength(dpc) - fieldsLen;
            if ((lmrt == LongMessageRuleType.LongMessagesForbidden || lmrt == LongMessageRuleType.XudtEnabled)
                    && availLen > 255)
                availLen = 255;
            if (lmrt == LongMessageRuleType.XudtEnabled)
                availLen *= 16;
            if (availLen > this.getMaxDataMessage())
                availLen = this.getMaxDataMessage();
            return availLen;

        } catch (IOException e) {
            // this can not occur
            e.printStackTrace();
            return 0;
        }
    }

    private int getMaxUserDataLengthForGT(SccpAddress calledPartyAddress, SccpAddress callingPartyAddress) {

        Rule rule = this.router.findRule(calledPartyAddress, false);
        if (rule == null) {
            return 0;
        }
        SccpAddress translationAddressPri = this.router.getRoutingAddress(rule.getPrimaryAddressId());
        if (translationAddressPri == null) {
            return 0;
        }

        return getMaxUserDataLengthForDpc(translationAddressPri.getSignalingPointCode(), calledPartyAddress,
                callingPartyAddress);
    }

    protected void broadcastChangedSsnState(int affectedSsn, boolean inService) {
        this.sccpManagement.broadcastChangedSsnState(affectedSsn, inService);
    }

    public void removeAllResourses() {

        if (this.state != State.RUNNING) {
            return;
        }

        this.router.removeAllResourses();
        this.sccpResource.removeAllResourses();

        for (SccpManagementEventListener lstr : this.sccpProvider.managementEventListeners) {
            try {
                lstr.onRemoveAllResources();
            } catch (Throwable ee) {
                logger.error("Exception while invoking onRemoveAllResources", ee);
            }
        }
    }

    public void onMtp3PauseMessage(Mtp3PausePrimitive msg) {

        logger.warn(String.format("Rx : %s", msg));

        if (this.state != State.RUNNING) {
            logger.error("Cannot consume MTP3 PASUE message as SCCP stack is not RUNNING");
            return;
        }

        sccpManagement.handleMtp3Pause(msg.getAffectedDpc());
    }

    public void onMtp3ResumeMessage(Mtp3ResumePrimitive msg) {
        logger.warn(String.format("Rx : %s", msg));

        if (this.state != State.RUNNING) {
            logger.error("Cannot consume MTP3 RESUME message as SCCP stack is not RUNNING");
            return;
        }

        sccpManagement.handleMtp3Resume(msg.getAffectedDpc());
    }

    public void onMtp3StatusMessage(Mtp3StatusPrimitive msg) {
        logger.warn(String.format("Rx : %s", msg));
        if (this.state != State.RUNNING) {
            logger.error("Cannot consume MTP3 STATUS message as SCCP stack is not RUNNING");
            return;
        }

        sccpManagement.handleMtp3Status(msg.getCause(), msg.getAffectedDpc(), msg.getCongestionLevel());
    }

    public void onMtp3TransferMessage(Mtp3TransferPrimitive mtp3Msg) {

        if (this.state != State.RUNNING) {
            logger.error("Received MTP3TransferPrimitive from lower layer but SCCP stack is not RUNNING");
            return;
        }

        SccpMessageImpl msg = null;
        try {
            // checking if incoming dpc is local
            if (!this.isPreviewMode() && !this.router.spcIsLocal(mtp3Msg.getDpc())) {

                // incoming dpc is not local - trying to find the target SAP and
                // send a message to MTP3 (MTP transit function)
                int dpc = mtp3Msg.getDpc();
                int sls = mtp3Msg.getSls();

                RemoteSignalingPointCode remoteSpc = this.getSccpResource().getRemoteSpcByPC(dpc);
                if (remoteSpc == null) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format("Incoming Mtp3 Message for nonlocal dpc=%d. But RemoteSpc is not found", dpc));
                    }
                    return;
                }
                if (remoteSpc.isRemoteSpcProhibited()) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String
                                .format("Incoming Mtp3 Message for nonlocal dpc=%d. But RemoteSpc is Prohibited", dpc));
                    }
                    return;
                }
                Mtp3ServiceAccessPoint sap = this.router.findMtp3ServiceAccessPoint(dpc, sls);
                if (sap == null) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format("Incoming Mtp3 Message for nonlocal dpc=%d / sls=%d. But SAP is not found",
                                dpc, sls));
                    }
                    return;
                }
                Mtp3UserPart mup = this.getMtp3UserPart(sap.getMtp3Id());
                if (mup == null) {
                    if (logger.isEnabledFor(Level.WARN)) {
                        logger.warn(String.format(
                                "Incoming Mtp3 Message for nonlocal dpc=%d / sls=%d. no matching Mtp3UserPart found", dpc, sls));
                    }
                    return;
                }

                mup.sendMessage(mtp3Msg);
                return;
            }

            // process only SCCP messages
            if (mtp3Msg.getSi() != Mtp3._SI_SERVICE_SCCP) {
                logger.warn(String
                        .format("Received Mtp3TransferPrimitive from lower layer with Service Indicator=%d which is not SCCP. Dropping this message",
                                mtp3Msg.getSi()));
                return;
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(mtp3Msg.getData());
            DataInputStream in = new DataInputStream(bais);
            int mt = in.readUnsignedByte();
            msg = ((MessageFactoryImpl) sccpProvider.getMessageFactory()).createMessage(mt, mtp3Msg.getOpc(), mtp3Msg.getDpc(),
                    mtp3Msg.getSls(), in);

            if (logger.isDebugEnabled()) {
                logger.debug(String.format("Rx : SCCP message from MTP %s", msg));
            }

            // when segmented messages - make a reassembly operation
            if (msg instanceof SccpSegmentableMessageImpl) {
                SccpSegmentableMessageImpl sgmMsg = (SccpSegmentableMessageImpl) msg;
                SegmentationImpl segm = (SegmentationImpl) sgmMsg.getSegmentation();
                if (segm != null) {
                    // segmentation info is present - segmentation is possible
                    if (segm.isFirstSegIndication() && segm.getRemainingSegments() == 0) {

                        // the single segment - no reassembly is needed
                        sgmMsg.setReceivedSingleSegment();
                    } else {

                        // multiple segments - reassembly is needed
                        if (segm.isFirstSegIndication()) {

                            // first segment
                            sgmMsg.setReceivedFirstSegment();
                            MessageReassemblyProcess msp = new MessageReassemblyProcess(segm.getSegmentationLocalRef(),
                                    sgmMsg.getCallingPartyAddress());
                            synchronized (this.reassemplyCache) {
                                this.reassemplyCache.put(msp, sgmMsg);
                            }
                            sgmMsg.setMessageReassemblyProcess(msp);
                            msp.startTimer();
                            return;
                        } else {

                            // nonfirst segment
                            MessageReassemblyProcess msp = new MessageReassemblyProcess(segm.getSegmentationLocalRef(),
                                    sgmMsg.getCallingPartyAddress());
                            SccpSegmentableMessageImpl sgmMsgFst = null;
                            synchronized (this.reassemplyCache) {
                                sgmMsgFst = this.reassemplyCache.get(msp);
                            }
                            if (sgmMsgFst == null) {
                                // previous segments cache is not found -
                                // discard a segment
                                if (logger.isEnabledFor(Level.WARN)) {
                                    logger.warn(String
                                            .format("Reassembly function failure: received a non first segment without the first segement having recieved. SccpMessageSegment=%s",
                                                    msg));
                                }
                                return;
                            }
                            if (sgmMsgFst.getRemainingSegments() - 1 != segm.getRemainingSegments()) {
                                // segments bad order
                                synchronized (this.reassemplyCache) {
                                    this.reassemplyCache.remove(msp);
                                    MessageReassemblyProcess mspMain = sgmMsgFst.getMessageReassemblyProcess();
                                    if (mspMain != null)
                                        mspMain.stopTimer();
                                }
                                if (logger.isEnabledFor(Level.WARN)) {
                                    logger.warn(String
                                            .format("Reassembly function failure: when receiving a next segment message order is missing. SccpMessageSegment=%s",
                                                    msg));
                                }
                                this.sccpRoutingControl.sendSccpError(sgmMsgFst, ReturnCauseValue.CANNOT_REASEMBLE);
                                return;
                            }

                            if (sgmMsgFst.getRemainingSegments() == 1) {
                                // last segment
                                synchronized (this.reassemplyCache) {
                                    MessageReassemblyProcess mspMain = sgmMsgFst.getMessageReassemblyProcess();
                                    if (mspMain != null)
                                        mspMain.stopTimer();
                                    this.reassemplyCache.remove(msp);
                                }
                                if (sgmMsgFst.getRemainingSegments() != 1)
                                    return;

                                sgmMsgFst.setReceivedNextSegment(sgmMsg);
                                msg = sgmMsgFst;
                            } else {
                                // not last segment
                                sgmMsgFst.setReceivedNextSegment(sgmMsg);
                                return;
                            }
                        }
                    }
                }
            }

            if (msg instanceof SccpAddressedMessageImpl) {
                // CR or connectionless messages
                SccpAddressedMessageImpl msgAddr = (SccpAddressedMessageImpl) msg;

                // adding OPC into CallingPartyAddress if it is absent there and
                // "RouteOnSsn"
                SccpAddress addr = msgAddr.getCallingPartyAddress();
                if (addr != null
                        && addr.getAddressIndicator().getRoutingIndicator() == RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN) {
                    if (!addr.getAddressIndicator().pcPresent()) {
                        msgAddr.setCallingPartyAddress(new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, msgAddr
                                .getIncomingOpc(), null, addr.getSubsystemNumber()));
                    }
                }

                sccpRoutingControl.routeMssgFromMtp(msgAddr);
            } else {
                // TODO: implement non-addresses message processing (these are
                // connected-oriented messages in the connected phase)
                logger.warn(String
                        .format("Rx SCCP message which is not instance of SccpAddressedMessage or SccpSegmentableMessage. Will be dropped. Message=",
                                msg));
            }
        } catch (IOException e) {
            logger.error("IOException while decoding SCCP message: " + e.getMessage(), e);
        }
    }

    public class MessageReassemblyProcess implements Runnable {
        private int segmentationLocalRef;
        private SccpAddress callingPartyAddress;

        private Future timer;

        public MessageReassemblyProcess(int segmentationLocalRef, SccpAddress callingPartyAddress) {
            this.segmentationLocalRef = segmentationLocalRef;
            this.callingPartyAddress = callingPartyAddress;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            if (!(obj instanceof MessageReassemblyProcess))
                return false;
            MessageReassemblyProcess x = (MessageReassemblyProcess) obj;
            if (this.segmentationLocalRef != x.segmentationLocalRef)
                return false;

            if (this.callingPartyAddress == null || x.callingPartyAddress == null)
                return false;

            return this.callingPartyAddress.equals(x.callingPartyAddress);
        }

        @Override
        public int hashCode() {
            return this.segmentationLocalRef;
        }

        public void startTimer() {
            this.timer = timerExecutors.schedule(this, reassemblyTimerDelay, TimeUnit.MILLISECONDS);
        }

        public void stopTimer() {
            if (this.timer != null) {
                this.timer.cancel(false);
                this.timer = null;
            }
        }

        public void run() {
            SccpSegmentableMessageImpl msg = null;
            synchronized (reassemplyCache) {
                msg = reassemplyCache.remove(this);
                if (msg == null)
                    return;

                msg.cancelSegmentation();
            }

            try {
                sccpRoutingControl.sendSccpError(msg, ReturnCauseValue.CANNOT_REASEMBLE);
            } catch (IOException e) {
                logger.warn("IOException when sending an error message", e);
            }
        }
    }

    /**
     * Persist
     */
    public void store() {

        // TODO : Should we keep reference to Objects rather than recreating
        // everytime?
        try {
            XMLObjectWriter writer = XMLObjectWriter.newInstance(new FileOutputStream(persistFile.toString()));
            writer.setBinding(binding);
            // Enables cross-references.
            // writer.setReferenceResolver(new XMLReferenceResolver());
            writer.setIndentation(TAB_INDENT);

            writer.write(this.zMarginXudtMessage, Z_MARGIN_UDT_MSG, Integer.class);
            writer.write(this.reassemblyTimerDelay, REASSEMBLY_TIMER_DELAY, Integer.class);
            writer.write(this.maxDataMessage, MAX_DATA_MSG, Integer.class);
            writer.write(this.removeSpc, REMOVE_SPC, Boolean.class);
            writer.write(this.previewMode, PREVIEW_MODE, Boolean.class);
            writer.write(this.sstTimerDuration_Min, SST_TIMER_DURATION_MIN, Integer.class);
            writer.write(this.sstTimerDuration_Max, SST_TIMER_DURATION_MAX, Integer.class);
            writer.write(this.sstTimerDuration_IncreaseFactor, SST_TIMER_DURATION_INCREASE_FACTOR, Double.class);

            writer.close();
        } catch (Exception e) {
            this.logger.error(
                    String.format("Error while persisting the Sccp Resource state in file=%s", persistFile.toString()), e);
        }
    }

    /**
     * Load and create LinkSets and Link from persisted file
     *
     * @throws Exception
     */
    protected void load() throws FileNotFoundException {
        XMLObjectReader reader = null;
        try {
            reader = XMLObjectReader.newInstance(new FileInputStream(persistFile.toString()));

            reader.setBinding(binding);
            this.zMarginXudtMessage = reader.read(Z_MARGIN_UDT_MSG, Integer.class);
            this.reassemblyTimerDelay = reader.read(REASSEMBLY_TIMER_DELAY, Integer.class);
            this.maxDataMessage = reader.read(MAX_DATA_MSG, Integer.class);
            this.removeSpc = reader.read(REMOVE_SPC, Boolean.class);
            Boolean b1 = reader.read(PREVIEW_MODE, Boolean.class);
            if (b1 != null)
                this.previewMode = b1;

            this.sstTimerDuration_Min = reader.read(SST_TIMER_DURATION_MIN, Integer.class);
            this.sstTimerDuration_Max = reader.read(SST_TIMER_DURATION_MAX, Integer.class);
            this.sstTimerDuration_IncreaseFactor = reader.read(SST_TIMER_DURATION_INCREASE_FACTOR, Double.class);

            reader.close();
        } catch (XMLStreamException ex) {
            // this.logger.info(
            // "Error while re-creating Linksets from persisted file", ex);
        }
    }
}
