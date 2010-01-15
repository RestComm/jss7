/*
 * The Java Call Control API for CAMEL 2
 *
 * The source code contained in this file is in in the public domain.
 * It can be used in any project or product without prior permission,
 * license or royalty payments. There is  NO WARRANTY OF ANY KIND,
 * EXPRESS, IMPLIED OR STATUTORY, INCLUDING, WITHOUT LIMITATION,
 * THE IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE,
 * AND DATA ACCURACY.  We do not warrant or make any representations
 * regarding the use of the software or the  results thereof, including
 * but not limited to the correctness, accuracy, reliability or
 * usefulness of the software.
 */

package org.mobicents.ss7.sccp.impl;



import org.mobicents.ss7.sccp.SccpListener;
import org.mobicents.ss7.sccp.SccpParameterFactory;
import org.mobicents.ss7.sccp.SccpProvider;
import org.mobicents.ss7.sccp.SccpUnitDataFactory;
import org.mobicents.ss7.sccp.impl.parameter.SccpParameterFactoryImpl;
import org.mobicents.ss7.sccp.impl.ud.SccpUnitDataFactoryImpl;

/**
 *
 * @author Oleg Kulikov
 * @author baranowb
 */
public abstract class SccpProviderImpl implements SccpProvider {

	
	protected SccpListener listener;
	protected SccpUnitDataFactory udFactory;
	protected SccpParameterFactory paramFactory;

	
	public SccpProviderImpl() {
		super();
		this.paramFactory = new SccpParameterFactoryImpl();
		this.udFactory = new SccpUnitDataFactoryImpl();
	}

	public SccpListener getListener() {
		
		return listener;
	}

	public void removeListener() {
		this.listener = null;
		
	}
	public void setSccpListener(SccpListener listener) {
		this.listener = listener;
		
	}
	public SccpParameterFactory getSccpParameterFactory() {
		
		return paramFactory;
	}

	public SccpUnitDataFactory getUnitDataFactory() {
		
		return udFactory;
	}
    
}
