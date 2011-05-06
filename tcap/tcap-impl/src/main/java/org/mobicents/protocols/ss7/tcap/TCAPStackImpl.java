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

    private TCAPProviderImpl tcapProvider;
    private SccpProvider sccpProvider;
    private SccpAddress address;
    
    private State state = State.IDLE;
    private static final Logger logger = Logger.getLogger(TCAPStackImpl.class);

    public TCAPStackImpl() {
        super();

    }
    //for tests only
    public TCAPStackImpl(SccpProvider sccpProvider, int ssn) {
        this.sccpProvider = sccpProvider;
        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, ssn);
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

    private enum State {

        IDLE, CONFIGURED, RUNNING;
    }
}
