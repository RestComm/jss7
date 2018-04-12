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

package org.restcomm.protocols.ss7.tools.simulator.management;

import javax.management.NotificationEmitter;

import org.apache.log4j.Level;
import org.restcomm.protocols.ss7.tools.simulator.common.ConfigurationData;
import org.restcomm.protocols.ss7.tools.simulator.level1.M3uaMan;
import org.restcomm.protocols.ss7.tools.simulator.level2.SccpMan;
import org.restcomm.protocols.ss7.tools.simulator.level3.CapMan;
import org.restcomm.protocols.ss7.tools.simulator.level3.MapMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.ati.TestAtiClientMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.ati.TestAtiServerMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.cap.TestCapScfMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.cap.TestCapSsfMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiClientMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiServerMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsClientMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsServerMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.sms.TestSmsClientMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.sms.TestSmsServerMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientMan;
import org.restcomm.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerMan;

/**
*
* @author sergey vetyutnev
*
*/
public interface TesterHostInterface extends TesterHostMBean, NotificationEmitter {

    static String SIMULATOR_HOME_VAR = "SIMULATOR_HOME";

    M3uaMan getM3uaMan();

    SccpMan getSccpMan();

    MapMan getMapMan();

    CapMan getCapMan();

    TestUssdClientMan getTestUssdClientMan();

    TestUssdServerMan getTestUssdServerMan();

    TestSmsClientMan getTestSmsClientMan();

    TestSmsServerMan getTestSmsServerMan();

    TestCapSsfMan getTestCapSsfMan();

    TestCapScfMan getTestCapScfMan();

    TestAtiClientMan getTestAtiClientMan();

    TestAtiServerMan getTestAtiServerMan();

    TestCheckImeiClientMan getTestCheckImeiClientMan();

    TestCheckImeiServerMan getTestCheckImeiServerMan();

    TestMapLcsClientMan getTestMapLcsClientMan();

    TestMapLcsServerMan getTestMapLcsServerMan();

    boolean isNeedQuit();

    void checkStore();

    void execute();

    void sendNotif(String source, String msg, Throwable e, Level logLevel);

    void sendNotif(String source, String msg, String userData, Level logLevel);

    ConfigurationData getConfigurationData();

    void markStore();

    String getPersistDir();

}
