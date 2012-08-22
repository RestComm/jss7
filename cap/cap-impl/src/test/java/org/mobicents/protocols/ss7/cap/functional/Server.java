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

package org.mobicents.protocols.ss7.cap.functional;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.cap.api.CAPParameterFactory;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.cap.api.errors.CAPErrorMessageFactory;
import org.mobicents.protocols.ss7.inap.api.INAPParameterFactory;
import org.mobicents.protocols.ss7.isup.ISUPParameterFactory;
import org.mobicents.protocols.ss7.map.api.MAPParameterFactory;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class Server extends EventTestHarness  {

	private static Logger logger = Logger.getLogger(Server.class);

	private CAPFunctionalTest runningTestCase;
	private SccpAddress thisAddress;
	private SccpAddress remoteAddress;

	protected CAPStack capStack;
	protected CAPProvider capProvider;

	protected CAPParameterFactory capParameterFactory;
	protected CAPErrorMessageFactory capErrorMessageFactory;
	protected MAPParameterFactory mapParameterFactory;
	protected INAPParameterFactory inapParameterFactory;
	protected ISUPParameterFactory isupParameterFactory;

//	private boolean _S_recievedDialogRequest;
//	private boolean _S_recievedInitialDp;
//
//	private int dialogStep;
//	private long savedInvokeId;
//	private String unexpected = "";
//
//	private FunctionalTestScenario step;

	Server(CAPStack capStack, CAPFunctionalTest runningTestCase, SccpAddress thisAddress, SccpAddress remoteAddress) {
		super(logger);
		this.capStack = capStack;
		this.runningTestCase = runningTestCase;
		this.thisAddress = thisAddress;
		this.remoteAddress = remoteAddress;
		this.capProvider = this.capStack.getCAPProvider();

		this.capParameterFactory = this.capProvider.getCAPParameterFactory();
		this.capErrorMessageFactory = this.capProvider.getCAPErrorMessageFactory();
		this.mapParameterFactory = this.capProvider.getMAPParameterFactory();
		this.inapParameterFactory = this.capProvider.getINAPParameterFactory();
		this.isupParameterFactory = this.capProvider.getISUPParameterFactory();

		this.capProvider.addCAPDialogListener(this);
		this.capProvider.getCAPServiceCircuitSwitchedCall().addCAPServiceListener(this);

		this.capProvider.getCAPServiceCircuitSwitchedCall().acivate();
		this.capProvider.getCAPServiceSms().acivate();
		this.capProvider.getCAPServiceSms().acivate();
	}

	public void debug(String message) {
		this.logger.debug(message);
	}
	
	public void error(String message, Exception e){
		this.logger.error(message, e);
	}
}

