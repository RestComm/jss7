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

package org.mobicents.protocols.ss7.map.load;

import java.io.FileNotFoundException;
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

/**
 * @author abhayani
 *
 */
public abstract class TestHarness implements MAPDialogListener, MAPServiceSupplementaryListener {

    private static final Logger logger = Logger.getLogger("map.test");

    protected static final String CREATED_DIALOGS = "CreatedScenario";
    protected static final String SUCCESSFUL_DIALOGS = "CompletedScenario";
    protected static final String ERROR_DIALOGS = "FailedScenario";

    protected static final String LOG_FILE_NAME = "log.file.name";
    protected static String logFileName = "maplog.txt";

    protected static int NDIALOGS = 50000;

    protected static int MAXCONCURRENTDIALOGS = 15;

    // MTP Details
    protected static int CLIENT_SPC = 1;
    protected static int SERVET_SPC = 2;
    protected static int NETWORK_INDICATOR = 2;
    protected static int SERVICE_INIDCATOR = 3; // SCCP
    protected static int SSN = 8;

    // M3UA details
    // protected final String CLIENT_IP = "172.31.96.40";
    protected static String CLIENT_IP = "127.0.0.1";
    protected static int CLIENT_PORT = 2345;

    // protected final String SERVER_IP = "172.31.96.41";
    protected static String SERVER_IP = "127.0.0.1";
    protected static int SERVER_PORT = 3434;

    protected static int ROUTING_CONTEXT = 100;

    protected static int DELIVERY_TRANSFER_MESSAGE_THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2;

    protected static int RAMP_UP_PERIOD = -100;

    protected final String SERVER_ASSOCIATION_NAME = "serverAsscoiation";
    protected final String CLIENT_ASSOCIATION_NAME = "clientAsscoiation";

    protected final String SERVER_NAME = "testserver";

    // TCAP Details
    protected static final int MAX_DIALOGS = 500000;

    protected static String SCCP_CLIENT_ADDRESS = null;
    protected static String SCCP_SERVER_ADDRESS = null;

    protected static RoutingIndicator ROUTING_INDICATOR = RoutingIndicator.ROUTING_BASED_ON_DPC_AND_SSN;

    protected final ParameterFactoryImpl factory = new ParameterFactoryImpl();

    protected static int TEST_START_DELAY = 20000;
    protected static int TEST_END_DELAY = 3000;
    protected static int PRINT_WRITER_PERIOD = 2000;

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
            } catch (Exception e) {
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
