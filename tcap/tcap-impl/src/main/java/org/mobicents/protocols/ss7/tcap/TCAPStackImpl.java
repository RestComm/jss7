/**
 * 
 */
package org.mobicents.protocols.ss7.tcap;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ConfigurationException;
import org.mobicents.protocols.StartFailedException;
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
    public TCAPStackImpl(SccpProvider sccpProvider, SccpAddress address) {
        this.sccpProvider = sccpProvider;
        this.tcapProvider = new TCAPProviderImpl(sccpProvider, this, address);
        this.state = State.CONFIGURED;
    }

    public void start() throws IllegalStateException, StartFailedException {
        logger.info("Starting ...");
        if (state != State.CONFIGURED) {
            throw new IllegalStateException("Stack has not been configured or is already running!");
        }
		this.tcapProvider.start();

		this.state = State.RUNNING;


    }

    public void stop() {
    	if (state != State.RUNNING) {
			throw new IllegalStateException("Stack is not running!");
		}
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
