/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;


import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.TCAPStack;

/**
 * @author amit bhayani
 * @author baranowb
 * 
 */
public class TCAPStackImpl implements TCAPStack {

	// default value of idle timeout and after TC_END remove of task.
	public static final long _DIALOG_TIMEOUT = 60000;
	public static final long _INVOKE_TIMEOUT = 30000;
	public static final long _DIALOG_REMOVE_TIMEOUT = 30000;
	// TCAP state data, it is used ONLY on client side
    protected TCAPProviderImpl tcapProvider;
    private SccpProvider sccpProvider;
    private SccpAddress address;
    
    private State state = State.IDLE;
    
	private long dialogTimeout = _DIALOG_TIMEOUT;
	private long invokeTimeout = _INVOKE_TIMEOUT;
	private long removeTimeout = _DIALOG_REMOVE_TIMEOUT;
    private static final Logger logger = Logger.getLogger(TCAPStackImpl.class);

    public TCAPStackImpl() {
        super();

    }
    //for tests only
    public TCAPStackImpl(SccpProvider sccpProvider, int ssn) {
       this(sccpProvider,ssn,DialogIdIndex.INITIAL_SIZE);
    }
    
    
    public TCAPStackImpl(SccpProvider sccpProvider, int ssn, int maxDialogs) {
        this.sccpProvider = sccpProvider;
        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, ssn,maxDialogs);
        this.state = State.CONFIGURED;
    }

    public void start() throws IllegalStateException {
        logger.info("Starting ..." + tcapProvider);
        tcapProvider.start();
    }

    public void stop() {
		this.tcapProvider.stop();
		this.state = State.CONFIGURED;
    }

//    // ///////////////
//    // CONF METHOD //
//    // ///////////////
//    /**
//     *
//     */
//    public void configure(Properties props) throws ConfigurationException {
//    	if (state != State.IDLE) {
//			throw new IllegalStateException("Stack already been configured or is already running!");
//		}
//		//this.sccpProvider = this.sccpStack.getSccpProvider();
//		this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, address);
//		this.state = State.CONFIGURED;
//    }

    /*
     * (non-Javadoc)
     * 
     * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getProvider()
     */
    public TCAPProvider getProvider() {

        return tcapProvider;
    }

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#setDialogIdleTimeout(long)
	 */
	public void setDialogIdleTimeout(long v) {
		if(v<0)
		{
			throw new IllegalArgumentException("Timeout value must be greater or equal to zero.");
		}
		if(v<this.invokeTimeout)
		{
			throw new IllegalArgumentException("Timeout value must be greater or equal to invoke timeout.");
		}
		
		this.dialogTimeout = v;
		
	}
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getDialogIdleTimeout()
	 */
	public long getDialogIdleTimeout() {
		return this.dialogTimeout;
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#setInvokeTimeout(long)
	 */
	public void setInvokeTimeout(long v) {
		if(v<0)
		{
			throw new IllegalArgumentException("Timeout value must be greater or equal to zero.");
		}
		if(v>this.dialogTimeout)
		{
			throw new IllegalArgumentException("Timeout value must be smaller or equal to dialog timeout.");
		}
		this.invokeTimeout = v;
		
	}
	
	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.tcap.api.TCAPStack#getInvokeTimeout()
	 */
	public long getInvokeTimeout() {
		return this.invokeTimeout;
	}
	
	
	private enum State {

        IDLE, CONFIGURED, RUNNING;
    }


	@Override
	public void setDialogRemoveTimeout(long l) {
		if(l<0)
		{
			throw new IllegalArgumentException("Dialog remove timeout value must be greater or equal to zero.");
		}
		this.removeTimeout = l;
	}
	@Override
	public long getDialogRemoveTimeout() {
		return this.removeTimeout;
	}

}
