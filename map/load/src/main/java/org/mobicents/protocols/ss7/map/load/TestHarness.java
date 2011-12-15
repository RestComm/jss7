/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * 
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free 
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.protocols.ss7.map.load;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;
import org.mobicents.protocols.ss7.indicator.RoutingIndicator;
import org.mobicents.protocols.ss7.m3ua.impl.parameter.ParameterFactoryImpl;
import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;

/**
 * @author abhayani
 * 
 */
public abstract class TestHarness implements MAPDialogListener, MAPServiceSupplementaryListener  {

	private static final Logger logger = Logger.getLogger("map.test");

	protected static final String LOG_FILE_NAME = "log.file.name";
	protected static String logFileName = "maplog.txt";

	protected static int NDIALOGS = 50000;

	protected static int MAXCONCURRENTDIALOGS = 15;

	// MTP Details
	protected final int CLIENT_SPC = 1;
	protected final int SERVET_SPC = 2;
	protected final int NETWORK_INDICATOR = 2;
	protected final int SERVICE_INIDCATOR = 3; //SCCP
	protected final int SSN = 8;

	// M3UA details
	protected final String CLIENT_IP = "127.0.0.1";
	protected final int CLIENT_PORT = 2345;

	protected final String SERVER_IP = "127.0.0.1";
	protected final int SERVER_PORT = 3434;

	protected final int ROUTING_CONTEXT = 100;
	
	protected final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";
	protected final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";
	
	protected final String SERVER_NAME = "testserver";
	
	protected final SccpAddress SCCP_CLIENT_ADDRESS = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, CLIENT_SPC, null, SSN);
	protected final SccpAddress SCCP_SERVER_ADDRESS = new SccpAddress(RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN, SERVET_SPC, null, SSN);
	
	protected final ParameterFactoryImpl factory = new ParameterFactoryImpl();

	protected TestHarness() {
		init();
	}

	public void init() {
		try {
			Properties tckProperties = new Properties();

			InputStream inStreamLog4j = TestHarness.class.getResourceAsStream("/log4j.properties");
			
			System.out.println("Input Stream = " + inStreamLog4j);
			
			Properties propertiesLog4j = new Properties();
			try {
				propertiesLog4j.load(inStreamLog4j);
				PropertyConfigurator.configure(propertiesLog4j);
			} catch (IOException e) {
				e.printStackTrace();
				BasicConfigurator.configure();
			}

			logger.debug("log4j configured");

			String lf = System.getProperties().getProperty(LOG_FILE_NAME);
			if (lf != null) {
				logFileName = lf;
			}

			// If already created a print writer then just use it.
			try {
				logger.addAppender(new FileAppender(new SimpleLayout(), logFileName));
			} catch (FileNotFoundException fnfe) {

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

	}
}
