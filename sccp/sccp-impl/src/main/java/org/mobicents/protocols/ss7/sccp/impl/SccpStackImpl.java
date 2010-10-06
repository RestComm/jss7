/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.sccp.impl;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
import org.mobicents.protocols.ss7.sccp.Router;
import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.sccp.SccpStack;

/**
 * @author baranowb
 *
 */
public class SccpStackImpl implements SccpStack {

    private static final Logger logger = Logger.getLogger(SccpStackImpl.class);

    private State state = State.IDLE;
    private boolean managed = false;
    //provider ref, this can be real provider or pipe, for tests.
    private AbstractSccpProvider sccpProvider;
    //Router, ref to objec tproviding routing info. It is passed to provider
    //Here we have it for faster access?
    private Router router; 

    public SccpStackImpl(){

    }
    
    /**
     * Constructor for test purposes only!
     * @param sccpProvider
     */
    public SccpStackImpl(SccpProvider sccpProvider){
    		//Oleg, this is not valid way to create stack, via such hack....
            this.sccpProvider = (AbstractSccpProvider)sccpProvider;
            this.state = State.CONFIGURED;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.mobicents.protocols.ss7.sccp.SccpStack#configure(java.util.Properties
     * )
     */
    public void configure(Properties properties) throws ConfigurationException {
            if (state != State.IDLE) {
                    throw new IllegalStateException("Stack already been configured or is already running!");
            }
            this.sccpProvider = new SccpProviderImpl(this);
            this.sccpProvider.configure(properties);
            
            this.state = State.CONFIGURED;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#getSccpProvider()
     */
    public SccpProvider getSccpProvider() {
         
            return sccpProvider;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#start()
     */
    public void start() throws IllegalStateException, StartFailedException {
            logger.info("Starting ...");
            if (state != State.CONFIGURED) {
                    throw new IllegalStateException("Stack has not been configured or is already running!");
            }
            this.sccpProvider.start();
            this.state = State.RUNNING;

    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.sccp.SccpStack#stop()
     */
    public void stop() {
            if (state != State.RUNNING) {
                    throw new IllegalStateException("Stack is not running!");
            }
            this.sccpProvider.stop();
            this.state = State.CONFIGURED;
    }


	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#isManaged()
	 */
	@Override
	public boolean isManaged() {
		
		return this.managed;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#setManaged(boolean)
	 */
	@Override
	public void setManaged(boolean flag) {
		this.managed = flag;
		
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#setRouter(org.mobicents.protocols.ss7.sccp.Router)
	 */
	@Override
	public void setRouter(Router router) {
		//TODO: make router configurable through API(creation and route mgmgt)
		this.router = router;
	}

	/* (non-Javadoc)
	 * @see org.mobicents.protocols.ss7.sccp.SccpStack#getRouter()
	 */
	@Override
	public Router getRouter() {
		return this.router;
	}

	
	
	
	
	
	
	

    private enum State {
            IDLE, CONFIGURED, RUNNING;
    }
}
