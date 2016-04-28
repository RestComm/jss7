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

package org.mobicents.protocols.ss7.tcap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javolution.text.TextBuilder;
import javolution.xml.XMLBinding;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
 * @author amit bhayani
 * @author baranowb
 *
 */
public class TCAPStackImpl implements TCAPStack {

    private final Logger logger;

    protected static final String TCAP_MANAGEMENT_PERSIST_DIR_KEY = "tcapmanagement.persist.dir";
    protected static final String USER_DIR_KEY = "user.dir";
    protected static final String PERSIST_FILE_NAME = "management.xml";
    private static final String TAB_INDENT = "\t";
    private static final String CLASS_ATTRIBUTE = "type";

    private static final String DIALOG_IDLE_TIMEOUT = "dialogidletimeout";
    private static final String INVOKE_TIMEOUT = "invoketimeout";
    private static final String MAX_DIALOGS = "maxdialogs";
    private static final String DIALOG_ID_RANGE_START = "dialogidrangestart";
    private static final String DIALOG_ID_RANGE_END = "dialogidrangeend";
    private static final String PREVIEW_MODE = "previewmode";
    private static final String DO_NOT_SEND_PROTOCOL_VERSION = "donotsendprotocolversion";
    private static final String STATISTICS_ENABLED = "statisticsenabled";

    private static final String CONG_CONTROL_BLOCKING_INCOMING_TCAP_MESSAGES = "congControl_blockingIncomingTcapMessages";
    private static final String CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_1 = "congControl_ExecutorDelayThreshold_1";
    private static final String CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_2 = "congControl_ExecutorDelayThreshold_2";
    private static final String CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_3 = "congControl_ExecutorDelayThreshold_3";
    private static final String CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_1 = "congControl_ExecutorBackToNormalDelayThreshold_1";
    private static final String CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_2 = "congControl_ExecutorBackToNormalDelayThreshold_2";
    private static final String CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_3 = "congControl_ExecutorBackToNormalDelayThreshold_3";
    private static final String CONG_CONTROL_MEMORY_THRESHOLD_1 = "congControl_MemoryThreshold_1";
    private static final String CONG_CONTROL_MEMORY_THRESHOLD_2 = "congControl_MemoryThreshold_2";
    private static final String CONG_CONTROL_MEMORY_THRESHOLD_3 = "congControl_MemoryThreshold_3";
    private static final String CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_1 = "congControl_BackToNormalMemoryThreshold_1";
    private static final String CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_2 = "congControl_BackToNormalMemoryThreshold_2";
    private static final String CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_3 = "congControl_BackToNormalMemoryThreshold_3";


    private static final XMLBinding binding = new XMLBinding();

    // default value of idle timeout and after TC_END remove of task.
    public static final long _DIALOG_TIMEOUT = 60000;
    public static final long _INVOKE_TIMEOUT = 30000;
    public static final int _MAX_DIALOGS = 5000;
    public static final long _EMPTY_INVOKE_TIMEOUT = -1;
    // TCAP state data, it is used ONLY on client side
    protected TCAPProviderImpl tcapProvider;
    protected TCAPCounterProviderImpl tcapCounterProvider;
    private SccpProvider sccpProvider;
    private SccpAddress address;

    private final String name;

    protected final TextBuilder persistFile = TextBuilder.newInstance();

    protected String persistDir = null;

    private volatile boolean started = false;

    private long dialogTimeout = _DIALOG_TIMEOUT;
    private long invokeTimeout = _INVOKE_TIMEOUT;
    // TODO: make this configurable
    protected int maxDialogs = _MAX_DIALOGS;

    // TODO: make this configurable
    private long dialogIdRangeStart = 1;
    private long dialogIdRangeEnd = Integer.MAX_VALUE;
    private boolean previewMode = false;
    private boolean doNotSendProtocolVersion = false;
    private boolean statisticsEnabled = false;

    // if true incoming TCAP messages will be blocked (depending on congestion level, from level 2 - new incoming dialogs are
    // rejected, from level 3 - all incoming messages are rejected)
    private boolean congControl_blockingIncomingTcapMessages = false;

    // ExecutorMonitor Thresholds: delays in seconds (between the time when an incoming message has come from a peer and
    // scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor becomes the
    // congestion level 1, 2 or 3
    private double[] congControl_ExecutorDelayThreshold = { 1, 6, 12 };
    // ExecutorMonitor Thresholds: delays in seconds (between the time when an incoming message has come from a peer and
    // scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor resumes to the
    // congestion level 0, 1 or 2
    private double[] congControl_ExecutorBackToNormalDelayThreshold = { 0.5, 3, 8 };
    // MemoryMonitor Thresholds: a percent of occupied memory after which MemoryMonitor becomes the
    // congestion level 1, 2 or 3
    private double[] congControl_MemoryThreshold = new double[] { 77, 87, 97 };
    // MemoryMonitor Thresholds: a percent of occupied memory after which MemoryMonitor resumes to the
    // congestion level 0, 1 or 2
    private double[] congControl_BackToNormalMemoryThreshold = new double[] { 72, 82, 92 };

    private int ssn = -1;

    public TCAPStackImpl(String name) {
        super();
        this.name = name;
        this.logger = Logger.getLogger(TCAPStackImpl.class.getCanonicalName() + "-" + this.name);

        binding.setClassAttribute(CLASS_ATTRIBUTE);

        setPersistFile();
    }

    public TCAPStackImpl(String name, SccpProvider sccpProvider, int ssn) {
        this(name);

        this.sccpProvider = sccpProvider;
        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, ssn);
        this.tcapCounterProvider = new TCAPCounterProviderImpl(this.tcapProvider);

        this.ssn = ssn;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPersistDir() {
        return persistDir;
    }

    @Override
    public int getSubSystemNumber(){
        return this.ssn;
    }

    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
        this.setPersistFile();
    }

    private void setPersistFile() {
        this.persistFile.clear();

        if (persistDir != null) {
            this.persistFile.append(persistDir).append(File.separator).append(this.name).append("_").append(PERSIST_FILE_NAME);
        } else {
            persistFile.append(System.getProperty(TCAP_MANAGEMENT_PERSIST_DIR_KEY, System.getProperty(USER_DIR_KEY))).append(File.separator).append(this.name)
                    .append("_").append(PERSIST_FILE_NAME);
        }
    }

    public void start() throws Exception {
        logger.info("Starting ..." + tcapProvider);

        logger.info(String.format("TCAP Management configuration file path %s", persistFile.toString()));

        try {
            this.load();
        } catch (FileNotFoundException e) {
            logger.warn(String.format("Failed to load the TCAP Management configuration file. \n%s", e.getMessage()));
        }

//        this.checkDialogIdRangeValues();

        if (this.dialogTimeout < 0) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to zero.");
        }

        if (this.dialogTimeout < this.invokeTimeout) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to invoke timeout.");
        }

        if (this.invokeTimeout < 0) {
            throw new IllegalArgumentException("InvokeTimeout value must be greater or equal to zero.");
        }

        this.tcapCounterProvider = new TCAPCounterProviderImpl(this.tcapProvider);
        tcapProvider.start();

        this.started = true;
    }

    private void checkDialogIdRangeValues(long rangeStart, long rangeEnd) {
        if (rangeStart >= rangeEnd)
            throw new IllegalArgumentException("Range start value cannot be equal/greater than Range end value");
        if (rangeStart < 1)
            throw new IllegalArgumentException("Range start value must be greater or equal 1");
        if (rangeEnd > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Range end value must be less or equal " + Integer.MAX_VALUE);
        if ((rangeEnd - rangeStart) < 10000)
            throw new IllegalArgumentException("Range \"end - start\" must has at least 10000 possible dialogs");
        if ((rangeEnd - rangeStart) <= this.maxDialogs)
            throw new IllegalArgumentException("MaxDialog must be less than DialogIdRange");
    }

    public void stop() {
        this.tcapProvider.stop();
        this.started = false;

        this.store();
    }

    /**
     * @return the started
     */
    public boolean isStarted() {
        return this.started;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getProvider()
     */
    public TCAPProvider getProvider() {

        return tcapProvider;
    }

    @Override
    public TCAPCounterProvider getCounterProvider() {
        return tcapCounterProvider;
    }

    public TCAPCounterProviderImpl getCounterProviderImpl() {
        return tcapCounterProvider;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#setDialogIdleTimeout(long)
     */
    public void setDialogIdleTimeout(long v) throws Exception {
        if (!this.started)
            throw new Exception("DialogIdleTimeout parameter can be updated only when TCAP stack is running");

        if (v < 0) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to zero.");
        }
        if (v < this.invokeTimeout) {
            throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to invoke timeout.");
        }

        this.dialogTimeout = v;

        this.store();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getDialogIdleTimeout()
     */
    public long getDialogIdleTimeout() {
        return this.dialogTimeout;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#setInvokeTimeout(long)
     */
    public void setInvokeTimeout(long v) throws Exception {
        if (!this.started)
            throw new Exception("InvokeTimeout parameter can be updated only when TCAP stack is running");

        if (v < 0) {
            throw new IllegalArgumentException("InvokeTimeout value must be greater or equal to zero.");
        }
        if (v > this.dialogTimeout) {
            throw new IllegalArgumentException("InvokeTimeout value must be smaller or equal to dialog timeout.");
        }

        this.invokeTimeout = v;

        this.store();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getInvokeTimeout()
     */
    public long getInvokeTimeout() {
        return this.invokeTimeout;
    }

    public void setMaxDialogs(int v) throws Exception {
        if (!this.started)
            throw new Exception("MaxDialogs parameter can be updated only when TCAP stack is running");

        if (v < 1)
            throw new IllegalArgumentException("At least one Dialog must be accepted");

        if (v >= dialogIdRangeEnd - dialogIdRangeStart)
            throw new IllegalArgumentException("MaxDialog must be less than DialogIdRange");

        maxDialogs = v;

        this.store();
    }

    public int getMaxDialogs() {
        return maxDialogs;
    }

    public void setDialogIdRangeStart(long val) throws Exception {
        if (!this.started)
            throw new Exception("DialogIdRangeStart parameter can be updated only when TCAP stack is running");

        this.checkDialogIdRangeValues(val, this.getDialogIdRangeEnd());
        dialogIdRangeStart = val;

        this.store();
    }

    public void setDialogIdRangeEnd(long val) throws Exception {
        if (!this.started)
            throw new Exception("DialogIdRangeEnd parameter can be updated only when TCAP stack is running");

        this.checkDialogIdRangeValues(this.getDialogIdRangeStart(), val);
        dialogIdRangeEnd = val;

        this.store();
    }

    public long getDialogIdRangeStart() {
        return dialogIdRangeStart;
    }

    public long getDialogIdRangeEnd() {
        return dialogIdRangeEnd;
    }

    public void setPreviewMode(boolean val) throws Exception {
        if (this.started)
            throw new Exception("PreviewMode parameter can be updated only when TCAP stack is NOT running");

        previewMode = val;

//        this.store();
    }

    public boolean getPreviewMode() {
        return previewMode;
    }

    @Override
    public void setDoNotSendProtocolVersion(boolean val) throws Exception {
        if (!this.started)
            throw new Exception("DoNotSendProtocolVersion parameter can be updated only when TCAP stack is running");

        doNotSendProtocolVersion = val;

        this.store();
    }

    @Override
    public boolean getDoNotSendProtocolVersion() {
        return doNotSendProtocolVersion;
    }

    @Override
    public void setStatisticsEnabled(boolean val) throws Exception {
        if (!this.started)
            throw new Exception("StatisticsEnabled parameter can be updated only when TCAP stack is running");

        this.tcapCounterProvider = new TCAPCounterProviderImpl(this.tcapProvider);

        statisticsEnabled = val;

        this.store();
    }

    @Override
    public boolean getStatisticsEnabled() {
        return statisticsEnabled;
    }

    @Override
    public boolean isCongControl_blockingIncomingTcapMessages() {
        return congControl_blockingIncomingTcapMessages;
    }

    @Override
    public void setCongControl_blockingIncomingTcapMessages(boolean value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_blockingIncomingTcapMessages parameter can be updated only when TCAP stack is running");

        congControl_blockingIncomingTcapMessages = value;

        this.store();
    }

    @Override
    public double getCongControl_ExecutorDelayThreshold_1() {
        return congControl_ExecutorDelayThreshold[0];
    }

    @Override
    public double getCongControl_ExecutorDelayThreshold_2() {
        return congControl_ExecutorDelayThreshold[1];
    }

    @Override
    public double getCongControl_ExecutorDelayThreshold_3() {
        return congControl_ExecutorDelayThreshold[2];
    }

    @Override
    public void setCongControl_ExecutorDelayThreshold_1(double value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_ExecutorDelayThreshold_1 parameter can be updated only when TCAP stack is running");

        congControl_ExecutorDelayThreshold[0] = value;

        this.store();
    }

    @Override
    public void setCongControl_ExecutorDelayThreshold_2(double value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_ExecutorDelayThreshold_2 parameter can be updated only when TCAP stack is running");

        congControl_ExecutorDelayThreshold[1] = value;

        this.store();
    }

    @Override
    public void setCongControl_ExecutorDelayThreshold_3(double value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_ExecutorDelayThreshold_3 parameter can be updated only when TCAP stack is running");

        congControl_ExecutorDelayThreshold[2] = value;

        this.store();
    }

    @Override
    public double getCongControl_ExecutorBackToNormalDelayThreshold_1() {
        return congControl_ExecutorBackToNormalDelayThreshold[0];
    }

    @Override
    public double getCongControl_ExecutorBackToNormalDelayThreshold_2() {
        return congControl_ExecutorBackToNormalDelayThreshold[1];
    }

    @Override
    public double getCongControl_ExecutorBackToNormalDelayThreshold_3() {
        return congControl_ExecutorBackToNormalDelayThreshold[2];
    }

    @Override
    public void setCongControl_ExecutorBackToNormalDelayThreshold_1(double value) throws Exception {
        if (!this.started)
            throw new Exception("ExecutorBackToNormalDelayThreshold_1 parameter can be updated only when TCAP stack is running");

        congControl_ExecutorBackToNormalDelayThreshold[0] = value;

        this.store();
    }

    @Override
    public void setCongControl_ExecutorBackToNormalDelayThreshold_2(double value) throws Exception {
        if (!this.started)
            throw new Exception("ExecutorBackToNormalDelayThreshold_2 parameter can be updated only when TCAP stack is running");

        congControl_ExecutorBackToNormalDelayThreshold[1] = value;

        this.store();
    }

    @Override
    public void setCongControl_ExecutorBackToNormalDelayThreshold_3(double value) throws Exception {
        if (!this.started)
            throw new Exception("ExecutorBackToNormalDelayThreshold_3 parameter can be updated only when TCAP stack is running");

        congControl_ExecutorBackToNormalDelayThreshold[2] = value;

        this.store();
    }

    @Override
    public double getCongControl_MemoryThreshold_1() {
        return congControl_MemoryThreshold[0];
    }

    @Override
    public double getCongControl_MemoryThreshold_2() {
        return congControl_MemoryThreshold[1];
    }

    @Override
    public double getCongControl_MemoryThreshold_3() {
        return congControl_MemoryThreshold[2];
    }

    @Override
    public void setCongControl_MemoryThreshold_1(double value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_MemoryThreshold_1 parameter can be updated only when TCAP stack is running");

        congControl_MemoryThreshold[0] = value;

        this.store();
    }

    @Override
    public void setCongControl_MemoryThreshold_2(double value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_MemoryThreshold_2 parameter can be updated only when TCAP stack is running");

        congControl_MemoryThreshold[1] = value;

        this.store();
    }

    @Override
    public void setCongControl_MemoryThreshold_3(double value) throws Exception {
        if (!this.started)
            throw new Exception("CongControl_MemoryThreshold_3 parameter can be updated only when TCAP stack is running");

        congControl_MemoryThreshold[2] = value;

        this.store();
    }

    @Override
    public double getCongControl_BackToNormalMemoryThreshold_1() {
        return congControl_BackToNormalMemoryThreshold[0];
    }

    @Override
    public double getCongControl_BackToNormalMemoryThreshold_2() {
        return congControl_BackToNormalMemoryThreshold[1];
    }

    @Override
    public double getCongControl_BackToNormalMemoryThreshold_3() {
        return congControl_BackToNormalMemoryThreshold[2];
    }

    @Override
    public void setCongControl_BackToNormalMemoryThreshold_1(double value) throws Exception {
        if (!this.started)
            throw new Exception("BackToNormalMemoryThreshold_1 parameter can be updated only when TCAP stack is running");

        congControl_BackToNormalMemoryThreshold[0] = value;

        this.store();
    }

    @Override
    public void setCongControl_BackToNormalMemoryThreshold_2(double value) throws Exception {
        if (!this.started)
            throw new Exception("BackToNormalMemoryThreshold_2 parameter can be updated only when TCAP stack is running");

        congControl_BackToNormalMemoryThreshold[1] = value;

        this.store();
    }

    @Override
    public void setCongControl_BackToNormalMemoryThreshold_3(double value) throws Exception {
        if (!this.started)
            throw new Exception("BackToNormalMemoryThreshold_3 parameter can be updated only when TCAP stack is running");

        congControl_BackToNormalMemoryThreshold[2] = value;

        this.store();
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

            writer.write(this.dialogTimeout, DIALOG_IDLE_TIMEOUT, Long.class);
            writer.write(this.invokeTimeout, INVOKE_TIMEOUT, Long.class);
            writer.write(this.maxDialogs, MAX_DIALOGS, Integer.class);
            writer.write(this.dialogIdRangeStart, DIALOG_ID_RANGE_START, Long.class);
            writer.write(this.dialogIdRangeEnd, DIALOG_ID_RANGE_END, Long.class);

//            writer.write(this.previewMode, PREVIEW_MODE, Boolean.class);

            writer.write(this.doNotSendProtocolVersion, DO_NOT_SEND_PROTOCOL_VERSION, Boolean.class);

            writer.write(this.congControl_blockingIncomingTcapMessages, CONG_CONTROL_BLOCKING_INCOMING_TCAP_MESSAGES,
                    Boolean.class);
            if (this.congControl_ExecutorDelayThreshold != null && this.congControl_ExecutorDelayThreshold.length == 3) {
                writer.write(this.congControl_ExecutorDelayThreshold[0], CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_1, Double.class);
                writer.write(this.congControl_ExecutorDelayThreshold[1], CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_2, Double.class);
                writer.write(this.congControl_ExecutorDelayThreshold[2], CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_3, Double.class);
            }
            if (this.congControl_ExecutorBackToNormalDelayThreshold != null
                    && this.congControl_ExecutorBackToNormalDelayThreshold.length == 3) {
                writer.write(this.congControl_ExecutorBackToNormalDelayThreshold[0],
                        CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_1, Double.class);
                writer.write(this.congControl_ExecutorBackToNormalDelayThreshold[1],
                        CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_2, Double.class);
                writer.write(this.congControl_ExecutorBackToNormalDelayThreshold[2],
                        CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_3, Double.class);
            }
            if (this.congControl_MemoryThreshold != null && this.congControl_MemoryThreshold.length == 3) {
                writer.write(this.congControl_MemoryThreshold[0], CONG_CONTROL_MEMORY_THRESHOLD_1, Double.class);
                writer.write(this.congControl_MemoryThreshold[1], CONG_CONTROL_MEMORY_THRESHOLD_2, Double.class);
                writer.write(this.congControl_MemoryThreshold[2], CONG_CONTROL_MEMORY_THRESHOLD_3, Double.class);
            }
            if (this.congControl_BackToNormalMemoryThreshold != null
                    && this.congControl_BackToNormalMemoryThreshold.length == 3) {
                writer.write(this.congControl_BackToNormalMemoryThreshold[0], CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_1,
                        Double.class);
                writer.write(this.congControl_BackToNormalMemoryThreshold[1], CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_2,
                        Double.class);
                writer.write(this.congControl_BackToNormalMemoryThreshold[2], CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_3,
                        Double.class);
            }

            writer.write(this.statisticsEnabled, STATISTICS_ENABLED, Boolean.class);

            writer.close();
        } catch (Exception e) {
            this.logger.error(
                    String.format("Error while persisting the TCAP Resource state in file=%s", persistFile.toString()), e);
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

            Long vall = reader.read(DIALOG_IDLE_TIMEOUT, Long.class);
            if (vall != null)
                this.dialogTimeout = vall;
            vall = reader.read(INVOKE_TIMEOUT, Long.class);
            if (vall != null)
                this.invokeTimeout = vall;
            Integer vali = reader.read(MAX_DIALOGS, Integer.class);
            if (vali != null)
                this.maxDialogs = vali;
            vall = reader.read(DIALOG_ID_RANGE_START, Long.class);
            if (vall != null)
                this.dialogIdRangeStart = vall;
            vall = reader.read(DIALOG_ID_RANGE_END, Long.class);
            if (vall != null)
                this.dialogIdRangeEnd = vall;

            Boolean volb = reader.read(PREVIEW_MODE, Boolean.class);

            volb = reader.read(DO_NOT_SEND_PROTOCOL_VERSION, Boolean.class);
            if (volb != null)
                this.doNotSendProtocolVersion = volb;

            Boolean valb = reader.read(CONG_CONTROL_BLOCKING_INCOMING_TCAP_MESSAGES, Boolean.class);
            if (valb != null)
                this.congControl_blockingIncomingTcapMessages = valb;

            Double valTH1 = reader.read(CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_1, Double.class);
            Double valTH2 = reader.read(CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_2, Double.class);
            Double valTH3 = reader.read(CONG_CONTROL_EXECUTOR_DELAY_THRESHOLD_3, Double.class);
            Double valTB1 = reader.read(CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_1, Double.class);
            Double valTB2 = reader.read(CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_2, Double.class);
            Double valTB3 = reader.read(CONG_CONTROL_EXECUTOR_BACK_TO_NORMAL_DELAY_THRESHOLD_3, Double.class);
            if (valTH1 != null && valTH2 != null && valTH3 != null && valTB1 != null && valTB2 != null && valTB3 != null) {
                this.congControl_ExecutorDelayThreshold = new double[3];
                this.congControl_ExecutorDelayThreshold[0] = valTH1;
                this.congControl_ExecutorDelayThreshold[1] = valTH2;
                this.congControl_ExecutorDelayThreshold[2] = valTH3;
                this.congControl_ExecutorBackToNormalDelayThreshold = new double[3];
                this.congControl_ExecutorBackToNormalDelayThreshold[0] = valTB1;
                this.congControl_ExecutorBackToNormalDelayThreshold[1] = valTB2;
                this.congControl_ExecutorBackToNormalDelayThreshold[2] = valTB3;
            }

            valTH1 = reader.read(CONG_CONTROL_MEMORY_THRESHOLD_1, Double.class);
            valTH2 = reader.read(CONG_CONTROL_MEMORY_THRESHOLD_2, Double.class);
            valTH3 = reader.read(CONG_CONTROL_MEMORY_THRESHOLD_3, Double.class);
            valTB1 = reader.read(CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_1, Double.class);
            valTB2 = reader.read(CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_2, Double.class);
            valTB3 = reader.read(CONG_CONTROL_BACK_TO_NORMAL_MEMORY_THRESHOLD_3, Double.class);
            if (valTH1 != null && valTH2 != null && valTH3 != null && valTB1 != null && valTB2 != null && valTB3 != null) {
                this.congControl_MemoryThreshold = new double[3];
                this.congControl_MemoryThreshold[0] = valTH1;
                this.congControl_MemoryThreshold[1] = valTH2;
                this.congControl_MemoryThreshold[2] = valTH3;
                this.congControl_BackToNormalMemoryThreshold = new double[3];
                this.congControl_BackToNormalMemoryThreshold[0] = valTB1;
                this.congControl_BackToNormalMemoryThreshold[1] = valTB2;
                this.congControl_BackToNormalMemoryThreshold[2] = valTB3;
            }

            volb = reader.read(STATISTICS_ENABLED, Boolean.class);
            if (volb != null)
                this.statisticsEnabled = volb;

            reader.close();
        } catch (XMLStreamException ex) {
            // this.logger.info(
            // "Error while re-creating Linksets from persisted file", ex);
        }
    }

}
