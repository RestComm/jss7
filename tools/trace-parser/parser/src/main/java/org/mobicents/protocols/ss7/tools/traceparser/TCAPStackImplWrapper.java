package org.mobicents.protocols.ss7.tools.traceparser;

import org.mobicents.protocols.ss7.sccp.SccpProvider;
import org.mobicents.protocols.ss7.tcap.TCAPStackImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TCAPStackImplWrapper extends TCAPStackImpl {
	
    //for tests only
    public TCAPStackImplWrapper(SccpProvider sccpProvider, int ssn) {
    	super(sccpProvider, ssn);
    	
        this.tcapProvider = new TCAPProviderImplWrapper(sccpProvider, this, ssn);
    }

}
