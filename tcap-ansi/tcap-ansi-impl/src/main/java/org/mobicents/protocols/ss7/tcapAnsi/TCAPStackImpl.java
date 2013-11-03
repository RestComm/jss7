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

package org.mobicents.protocols.ss7.tcapAnsi;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPCounterProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcapAnsi.api.TCAPStack;

/**
 * @author amit bhayani
 * @author baranowb
 *
 */
public class TCAPStackImpl implements TCAPStack {

    private final Logger logger;

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

    private volatile boolean started = false;

    private long dialogTimeout = _DIALOG_TIMEOUT;
    private long invokeTimeout = _INVOKE_TIMEOUT;
    // TODO: make this configurable
    protected int maxDialogs = _MAX_DIALOGS;

    // TODO: make this configurable
    private long dialogIdRangeStart = 1;
    private long dialogIdRangeEnd = Integer.MAX_VALUE;
    private boolean previewMode = false;
    private boolean statisticsEnabled = false;

    public TCAPStackImpl(String name) {
        super();
        this.name = name;

        this.logger = Logger.getLogger(TCAPStackImpl.class.getCanonicalName() + "-" + this.name);
    }

    public TCAPStackImpl(String name, SccpProvider sccpProvider, int ssn) {
        this.name = name;

        this.logger = Logger.getLogger(TCAPStackImpl.class.getCanonicalName() + "-" + this.name);

        this.sccpProvider = sccpProvider;
        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, ssn);
        this.tcapCounterProvider = new TCAPCounterProviderImpl(this.tcapProvider);
    }

    @Override
    public String getName() {
        return name;
    }

    public void start() throws Exception {
        logger.info("Starting ..." + tcapProvider);

        if (this.getDialogIdRangeStart() >= this.getDialogIdRangeEnd())
            throw new IllegalArgumentException("Range start value cannot be equal/greater than Range end value");
        if (this.getDialogIdRangeStart() < 1)
            throw new IllegalArgumentException("Range start value must be greater or equal 1");
        if (this.getDialogIdRangeEnd() > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Range end value must be less or equal " + Integer.MAX_VALUE);
        if ((this.getDialogIdRangeEnd() - this.getDialogIdRangeStart()) < 10000)
            throw new IllegalArgumentException("Range \"end - start\" must has at least 10000 possible dialogs");
        if ((this.getDialogIdRangeEnd() - this.getDialogIdRangeStart()) <= this.maxDialogs)
            throw new IllegalArgumentException("MaxDialog must be less than DialogIdRange");

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

    public void stop() {
        this.tcapProvider.stop();
        this.started = false;
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
    public void setDialogIdleTimeout(long v) {

        if (this.isStarted()) {
            if (v < 0) {
                throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to zero.");
            }
            if (v < this.invokeTimeout) {
                throw new IllegalArgumentException("DialogIdleTimeout value must be greater or equal to invoke timeout.");
            }
        }

        this.dialogTimeout = v;
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
    public void setInvokeTimeout(long v) {
        if (this.isStarted()) {
            if (v < 0) {
                throw new IllegalArgumentException("InvokeTimeout value must be greater or equal to zero.");
            }
            if (v > this.dialogTimeout) {
                throw new IllegalArgumentException("InvokeTimeout value must be smaller or equal to dialog timeout.");
            }
        }
        this.invokeTimeout = v;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getInvokeTimeout()
     */
    public long getInvokeTimeout() {
        return this.invokeTimeout;
    }

    public void setMaxDialogs(int v) {
        if (v < 1)
            throw new IllegalArgumentException("At least one Dialog must be accepted");
        if (v >= dialogIdRangeEnd - dialogIdRangeStart)
            throw new IllegalArgumentException("MaxDialog must be less than DialogIdRange");

        maxDialogs = v;
    }

    public int getMaxDialogs() {
        return maxDialogs;
    }

    public void setDialogIdRangeStart(long val) {
        dialogIdRangeStart = val;
    }

    public void setDialogIdRangeEnd(long val) {
        dialogIdRangeEnd = val;
    }

    public long getDialogIdRangeStart() {
        return dialogIdRangeStart;
    }

    public long getDialogIdRangeEnd() {
        return dialogIdRangeEnd;
    }

    public void setPreviewMode(boolean val) {
        previewMode = val;
    }

    public boolean getPreviewMode() {
        return previewMode;
    }

    @Override
    public void setStatisticsEnabled(boolean val) {
        if (started) {
            this.tcapCounterProvider = new TCAPCounterProviderImpl(this.tcapProvider);
        }
        statisticsEnabled = val;
    }

    @Override
    public boolean getStatisticsEnabled() {
        return statisticsEnabled;
    }

}
