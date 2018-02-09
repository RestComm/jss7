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

package org.mobicents.protocols.ss7.tcap.api;

import java.util.List;

import org.mobicents.protocols.ss7.sccp.SccpStack;

/**
 * @author baranowb
 */
public interface TCAPStack {

    /**
     * Returns the name of this stack
     * @return
     */
    String getName();

    /**
     * Set the persist directory to store the xml files
     * @return
     */
    String getPersistDir();

    /**
     * Returns stack provider.
     * @return
     */
    TCAPProvider getProvider();

    TCAPCounterProvider getCounterProvider();

    /**
     * Stops this stack and transport layer(SCCP)
     */
    void stop();

    /**
     * Start stack and transport layer(SCCP)
     *
     * @throws IllegalStateException - if stack is already running or not configured
     * @throws StartFailedException
     */
    void start() throws Exception;

    boolean isStarted();

    /**
     * Sets millisecond value for dialog timeout. It specifies how long dialog can be idle - not receive/send any messages.
     *
     * @param l
     */
    void setDialogIdleTimeout(long l) throws Exception;

    long getDialogIdleTimeout();

    /**
     * Sets the Invoke timeout for this invoke. Peer should respond back within invoke timeout, else stack will callback
     * {@link TCListener#onInvokeTimeout(org.mobicents.protocols.ss7.tcap.asn.comp.Invoke)} for application to take corrective
     * action. InvokeTimeout should always be less than DialogIdleTimeout.
     *
     * @param v
     */
    void setInvokeTimeout(long v) throws Exception;

    long getInvokeTimeout();

    /**
     * Sets the maximum number of dialogs allowed to be alive at a given time. If not set, a default value of 5000 dialogs will
     * be used.
     *
     * Important a: Default value may vary depending on the future implementations or changes to current implementation.
     *
     * Important b: If stack ranges provided, maximum number dialogs naturally cannot be greater than the provided range, thus,
     * it will be normalized to range delta (end - start).
     *
     * @param v number of dialogs
     */
    void setMaxDialogs(int v) throws Exception;

    /**
     *
     * @return Maximum number of allowed concurrent dialogs.
     */
    int getMaxDialogs();

    /**
     * Sets the start of the range of the generated dialog ids.
     */
    void setDialogIdRangeStart(long val) throws Exception;

    /**
     * Sets the end of the range of the generated dialog ids.
     */
    void setDialogIdRangeEnd(long val) throws Exception;

    /**
     *
     * @return starting dialog id within the range
     */
    long getDialogIdRangeStart();

    /**
     *
     * @return ending dialog id within the range
     */
    long getDialogIdRangeEnd();

    /**
     * previewMode is needed for special processing mode. When PreviewMode in TCAP level we have: - stack only listen's incoming
     * messages and sends nothing. send(), close(), sendComponent() and other such methods do nothing. - A TCAP Dialog is
     * temporary. TCAP Dialog is discarded after any incoming message like TC-BEGIN or TC-CONTINUE has been processed - for any
     * incoming messages (including TC-CONTINUE, TC-END, TC-ABORT) a new TCAP Dialog is created (end then deleted). - no timers
     * and timeouts
     *
     * default state: no previewMode
     */
    void setPreviewMode(boolean val) throws Exception;

    /**
     *
     * @return if areviewMode is active
     */
    boolean getPreviewMode();

    void setExtraSsns(List<Integer> extraSsnsNew) throws Exception;

    List<Integer> getExtraSsns();

    boolean isExtraSsnPresent(int ssn);

    String getSubSystemNumberList();

    void setDoNotSendProtocolVersion(boolean val) throws Exception;

    boolean getDoNotSendProtocolVersion();

    /**
     * Set to true to enable statistics.
     *
     * @param val
     */
    void setStatisticsEnabled(boolean val) throws Exception;

    /**
     * Returns true if this stack is gathering statistics
     *
     * @return
     */
    boolean getStatisticsEnabled();

    /**
     * Returns the Sub System Number (SSN) that this TCAP Stack is registered for
     *
     * @return
     */
    int getSubSystemNumber();

    /**
     * @return if true incoming TCAP messages will be blocked (depending on congestion level, from level 2 - new incoming
     *         dialogs are rejected, from level 3 - all incoming messages are rejected)
     */
    boolean isCongControl_blockingIncomingTcapMessages();

    /**
     * @param value if true incoming TCAP messages will be blocked (depending on congestion level, from level 2 - new incoming
     *        dialogs are rejected, from level 3 - all incoming messages are rejected)
     */
    void setCongControl_blockingIncomingTcapMessages(boolean value) throws Exception;

    /**
     * @return ExecutorMonitor Threshold 1: delays in seconds (between the time when an incoming message has come from a peer
     *         and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor
     *         becomes the congestion level 1
     */
    double getCongControl_ExecutorDelayThreshold_1();

    /**
     * @return ExecutorMonitor Threshold 2: delays in seconds (between the time when an incoming message has come from a peer
     *         and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor
     *         becomes the congestion level 2
     */
    double getCongControl_ExecutorDelayThreshold_2();

    /**
     * @return ExecutorMonitor Threshold 3: delays in seconds (between the time when an incoming message has come from a peer
     *         and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor
     *         becomes the congestion level 3
     */
    double getCongControl_ExecutorDelayThreshold_3();

    /**
     * Setting of ExecutorMonitor Threshold 1: delays in seconds (between the time when an incoming message has come from a peer
     * and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor becomes
     * the congestion level 1
     *
     * @param value
     */
    void setCongControl_ExecutorDelayThreshold_1(double value) throws Exception;

    /**
     * Setting of ExecutorMonitor Threshold 2: delays in seconds (between the time when an incoming message has come from a peer
     * and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor becomes
     * the congestion level 2
     *
     * @param value
     */
    void setCongControl_ExecutorDelayThreshold_2(double value) throws Exception;

    /**
     * Setting of ExecutorMonitor Threshold 3: delays in seconds (between the time when an incoming message has come from a peer
     * and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor becomes
     * the congestion level 3
     *
     * @param value
     */
    void setCongControl_ExecutorDelayThreshold_3(double value) throws Exception;

    /**
     * @return ExecutorMonitor Threshold 1: delays in seconds (between the time when an incoming message has come from a peer
     *         and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor
     *         resumes to the congestion level 0
     */
    double getCongControl_ExecutorBackToNormalDelayThreshold_1();

    /**
     * @return ExecutorMonitor Threshold 2: delays in seconds (between the time when an incoming message has come from a peer
     *         and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor
     *         resumes to the congestion level 1
     */
    double getCongControl_ExecutorBackToNormalDelayThreshold_2();

    /**
     * @return ExecutorMonitor Threshold 3: delays in seconds (between the time when an incoming message has come from a peer
     *         and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor
     *         resumes to the congestion level 2
     */
    double getCongControl_ExecutorBackToNormalDelayThreshold_3();

    /**
     * Setting of ExecutorMonitor Threshold 1: delays in seconds (between the time when an incoming message has come from a peer
     * and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor resumes to
     * the congestion level 0
     *
     * @param value
     */
    void setCongControl_ExecutorBackToNormalDelayThreshold_1(double value) throws Exception;

    /**
     * Setting of ExecutorMonitor Threshold 2: delays in seconds (between the time when an incoming message has come from a peer
     * and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor resumes to
     * the congestion level 1
     *
     * @param value
     */
    void setCongControl_ExecutorBackToNormalDelayThreshold_2(double value) throws Exception;

    /**
     * Setting of ExecutorMonitor Threshold 3: delays in seconds (between the time when an incoming message has come from a peer
     * and scheduled for execution and the time when the execution of the message starts) after which ExecutorMonitor resumes to
     * the congestion level 2
     *
     * @param value
     */
    void setCongControl_ExecutorBackToNormalDelayThreshold_3(double value) throws Exception;

    /**
     * @return MemoryMonitor Threshold 1: a percent of occupied memory after which MemoryMonitor becomes the congestion level 1
     */
    double getCongControl_MemoryThreshold_1();

    /**
     * @return MemoryMonitor Threshold 2: a percent of occupied memory after which MemoryMonitor becomes the congestion level 2
     */
    double getCongControl_MemoryThreshold_2();

    /**
     * @return MemoryMonitor Threshold 3: a percent of occupied memory after which MemoryMonitor becomes the congestion level 3
     */
    double getCongControl_MemoryThreshold_3();

    /**
     * Setting of MemoryMonitor Threshold 1: a percent of occupied memory after which MemoryMonitor becomes the congestion level
     * 1
     *
     * @param value
     */
    void setCongControl_MemoryThreshold_1(double value) throws Exception;

    /**
     * Setting of MemoryMonitor Threshold 2: a percent of occupied memory after which MemoryMonitor becomes the congestion level
     * 2
     *
     * @param value
     */
    void setCongControl_MemoryThreshold_2(double value) throws Exception;

    /**
     * Setting of MemoryMonitor Threshold 3: a percent of occupied memory after which MemoryMonitor becomes the congestion level
     * 3
     *
     * @param value
     */
    void setCongControl_MemoryThreshold_3(double value) throws Exception;

    /**
     * @return MemoryMonitor Threshold 1: a percent of occupied memory after which MemoryMonitor resumes to the congestion level
     *         0
     */
    double getCongControl_BackToNormalMemoryThreshold_1();

    /**
     * @return MemoryMonitor Threshold 2: a percent of occupied memory after which MemoryMonitor resumes to the congestion level
     *         1
     */
    double getCongControl_BackToNormalMemoryThreshold_2();

    /**
     * @return MemoryMonitor Threshold 3: a percent of occupied memory after which MemoryMonitor resumes to the congestion level
     *         2
     */
    double getCongControl_BackToNormalMemoryThreshold_3();

    /**
     * Setting of MemoryMonitor Threshold 1: a percent of occupied memory after which MemoryMonitor resumes to the congestion
     * level 0
     *
     * @param value
     */
    void setCongControl_BackToNormalMemoryThreshold_1(double value) throws Exception;

    /**
     * Setting of MemoryMonitor Threshold 2: a percent of occupied memory after which MemoryMonitor resumes to the congestion
     * level 1
     *
     * @param value
     */
    void setCongControl_BackToNormalMemoryThreshold_2(double value) throws Exception;

    /**
     * Setting of MemoryMonitor Threshold 3: a percent of occupied memory after which MemoryMonitor resumes to the congestion
     * level 2
     *
     * @param value
     */
    void setCongControl_BackToNormalMemoryThreshold_3(double value) throws Exception;

    /**
    *
    * @return the TCAPCounterEventsListener object or null it it isn't set (or set to null value)
    */
    TCAPCounterEventsListener getTCAPCounterEventsListener();

    /**
     * Sets the tcapCounterEventsListener attribute to the given value.
     *
     * @param value
     */
    void setTCAPCounterEventsListener(TCAPCounterEventsListener tcapCounterEventsListener);

    /** Set value for slsRange for this TCAP Stack.
     *
     * @param val
     */
    void setSlsRange(String val) throws Exception;

    /**
     * Returns the SlsRange that this TCAP Stack is registered for
     *
     * @return
     */
    String getSlsRange();

    /**
     * Returns the SCCP stack
     *
     * @return
     */
    SccpStack getSccpStack();

}
