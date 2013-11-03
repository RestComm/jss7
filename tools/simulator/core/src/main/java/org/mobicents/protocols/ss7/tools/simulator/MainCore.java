/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tools.simulator;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.mobicents.protocols.ss7.tools.simulator.level1.DialogicManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level1.DialogicManStandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManStandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManStandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapManStandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapManStandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHostMBean;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHostStandardMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapScfManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapScfStandardManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapSsfManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapSsfStandardManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsClientStandardManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsServerStandardManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientStandardManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerStandardManMBean;

import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 *
 * @author sergey vetyutnev
 * @author amit bhayani
 */
public class MainCore {

//    public static TesterHost mainGui(String appName) throws Exception {
//        TesterHost host = new TesterHost(appName);
//
//        return host;
//    }

    public static void main(String[] args) throws Exception {

        // ........................
        // SMSC: Hypersonic database

        // HTTP case:
        // MX4J - free http JMX adapter

        // SUN: jmxtools.jar
        // <dependency>
        // <groupId>com.sun.jdmk</groupId>
        // <artifactId>jmxtools</artifactId>
        // <version>1.2.1</version>
        // <scope>provided</scope>
        // </dependency>

        // RMI case:
        // rmiregistry 9999
        // service:jmx:rmi:///jndi/rmi://localhost:9999/server

        // JCONSOLE case:
        // java -Dcom.sun.management.jmxremote Main
        // JMX repository:
        // rmiregistry 9999
        // VISTA:
        // Out-of-the-box it is not possible to connect to a local Java process from the JConsole using Windows Vista.
        // The local process box just remains empty... The cause is Vista's security model.
        // In default case every Java process creates a file in the folder:
        // C:\Users\[LOGIN_NAME]\AppData\Local\Temp\hsperfdata_[LOGIN_NAME],
        // in my case e.g.: C:\Users\adam\AppData\Local\Temp\hsperfdata_adam. The name of the file is the PID.
        // For some strange reasons this directory is write-protected - it is not possible to create neither files nor folders
        // in the hsperfdata_[LOGIN_NAME] directory. It is even not possible to change the write access,
        // even not as an administrator (you can understand now, how secure Vista really is :-)).
        // However if you delete this directory, and create a new one with the same name - then it works perfectly....

        // Custom Notification: http://marxsoftware.blogspot.com/2008/02/publishing-user-objects-in-jmx.html
        // ........................

        // parsing arguments, possible values:
        // name=a1 http=8000 rmi=9999
        int httpPort = -1;
        int rmiPort = -1;
        String appName = "main";
        if (args != null && args.length > 0) {
            for (String s : args) {
                if (s.length() > 5 && s.substring(0, 5).toLowerCase().equals("name=")) {
                    appName = s.substring(5, s.length());
                }
                if (s.length() > 4 && s.substring(0, 4).toLowerCase().equals("rmi=")) {
                    try {
                        int porta = Integer.parseInt(s.substring(4, s.length()));
                        if (porta > 0 && porta < 65000)
                            rmiPort = porta;
                        else
                            System.out.println("Bad value of field \"rmi\"");
                    } catch (Exception e) {
                        System.out.println("Exception when parsing parameter \"rmi\"");
                    }
                }
                if (s.length() > 5 && s.substring(0, 5).toLowerCase().equals("http=")) {
                    try {
                        int porta = Integer.parseInt(s.substring(5, s.length()));
                        if (porta > 0 && porta < 65000)
                            httpPort = porta;
                        else
                            System.out.println("Bad value of field \"http\"");
                    } catch (Exception e) {
                        System.out.println("Exception when parsing parameter \"http\"");
                    }
                }
            }
        }

        MainCore main = new MainCore();
        main.start(appName, httpPort, rmiPort);

    }

    public void start(String appName, int httpPort, int rmiPort) throws MalformedObjectNameException,
            MBeanRegistrationException, InstanceNotFoundException, IOException {
        System.out.println("Application has been loaded...");

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        System.out.println("PlatformMBeanServer has been loaded...");

        // names initializing
        ObjectName adapterName = new ObjectName("SS7_Simulator_" + appName + ":name=htmladapter,port=" + httpPort);
        ObjectName nameTesterHost = new ObjectName("SS7_Simulator_" + appName + ":type=TesterHost");
        ObjectName nameM3uaMan = new ObjectName("SS7_Simulator_" + appName + ":type=M3uaMan");
        ObjectName nameDialogicMan = new ObjectName("SS7_Simulator_" + appName + ":type=DialogicMan");
        ObjectName nameSccpMan = new ObjectName("SS7_Simulator_" + appName + ":type=SccpMan");
        ObjectName nameMapMan = new ObjectName("SS7_Simulator_" + appName + ":type=MapMan");
        ObjectName nameCapMan = new ObjectName("SS7_Simulator_" + appName + ":type=CapMan");
        ObjectName nameUssdClientManMan = new ObjectName("SS7_Simulator_" + appName + ":type=TestUssdClientMan");
        ObjectName nameUssdServerManMan = new ObjectName("SS7_Simulator_" + appName + ":type=TestUssdServerMan");
        ObjectName nameSmsClientManMan = new ObjectName("SS7_Simulator_" + appName + ":type=TestSmsClientMan");
        ObjectName nameSmsServerManMan = new ObjectName("SS7_Simulator_" + appName + ":type=TestSmsServerMan");
        ObjectName nameTestCapSsfMan = new ObjectName("SS7_Simulator_" + appName + ":type=TestCapSsfMan");
        ObjectName nameTestCapScfMan = new ObjectName("SS7_Simulator_" + appName + ":type=TestCapScfMan");

        // HtmlAdaptorServer
        HtmlAdaptorServer adapter = null;
        if (httpPort > 0) {
            adapter = new HtmlAdaptorServer();
            System.out.println("HtmlAdaptorServer has been created...");
        }

        // Tester host initializing
        String sim_home = System.getProperty(TesterHost.SIMULATOR_HOME_VAR);
        if (sim_home != null)
            sim_home += File.separator + "data";
        TesterHost host = new TesterHost(appName, sim_home);

        JMXConnectorServer cs = null;
        Registry reg = null;
        try {
            // registering managed beans
            Object mbean = new TesterHostStandardMBean(host, TesterHostMBean.class, host);
            mbs.registerMBean(mbean, nameTesterHost);

            M3uaManStandardMBean m3uaMBean = new M3uaManStandardMBean(host.getM3uaMan(), M3uaManMBean.class);
            mbs.registerMBean(m3uaMBean, nameM3uaMan);

            DialogicManStandardMBean dialogicMBean = new DialogicManStandardMBean(host.getDialogicMan(), DialogicManMBean.class);
            mbs.registerMBean(dialogicMBean, nameDialogicMan);

            SccpManStandardMBean sccpMBean = new SccpManStandardMBean(host.getSccpMan(), SccpManMBean.class);
            mbs.registerMBean(sccpMBean, nameSccpMan);

            MapManStandardMBean mapMBean = new MapManStandardMBean(host.getMapMan(), MapManMBean.class);
            mbs.registerMBean(mapMBean, nameMapMan);

            CapManStandardMBean capMBean = new CapManStandardMBean(host.getCapMan(), CapManMBean.class);
            mbs.registerMBean(capMBean, nameCapMan);

            TestUssdClientStandardManMBean ussdClientManMBean = new TestUssdClientStandardManMBean(host.getTestUssdClientMan(),
                    TestUssdClientManMBean.class);
            mbs.registerMBean(ussdClientManMBean, nameUssdClientManMan);

            TestUssdServerStandardManMBean ussdServerManMBean = new TestUssdServerStandardManMBean(host.getTestUssdServerMan(),
                    TestUssdServerManMBean.class);
            mbs.registerMBean(ussdServerManMBean, nameUssdServerManMan);

            TestSmsClientStandardManMBean smsClientManMBean = new TestSmsClientStandardManMBean(host.getTestSmsClientMan(),
                    TestSmsClientManMBean.class);
            mbs.registerMBean(smsClientManMBean, nameSmsClientManMan);

            TestSmsServerStandardManMBean smsServerManMBean = new TestSmsServerStandardManMBean(host.getTestSmsServerMan(),
                    TestSmsServerManMBean.class);
            mbs.registerMBean(smsServerManMBean, nameSmsServerManMan);

            TestCapSsfStandardManMBean capSsfManMBean = new TestCapSsfStandardManMBean(host.getTestCapSsfMan(),
                    TestCapSsfManMBean.class);
            mbs.registerMBean(capSsfManMBean, nameTestCapSsfMan);

            TestCapScfStandardManMBean capScfManMBean = new TestCapScfStandardManMBean(host.getTestCapScfMan(),
                    TestCapScfManMBean.class);
            mbs.registerMBean(capScfManMBean, nameTestCapScfMan);

            System.out.println("All beans have been loaded...");

            // starting rmi connector
            if (rmiPort > 0) {
                System.out.println("RMI connector initializing...");
                reg = LocateRegistry.createRegistry(rmiPort);
                JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + rmiPort + "/server");
                cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
                cs.start();
                System.out.println("RMI connector has been started...");
            }

            // starting html connector
            if (httpPort > 0) {
                System.out.println("Html connector initializing...");
                adapter.setPort(httpPort);
                mbs.registerMBean(adapter, adapterName);
                adapter.start();
                System.out.println("Html connector has been started...");
            }

        } catch (Exception ee) {
            System.out.println("Exception when initializing the managed beans or started connectors:");
            ee.printStackTrace();
        }

        System.out.println("Waiting for commands...");

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (host.isNeedQuit())
                break;

            host.checkStore();
            host.execute();
        }
        System.out.println("Terminating...");

        if (httpPort > 0) {
            adapter.stop();
            mbs.unregisterMBean(adapterName);
        }
        if (rmiPort > 0) {
            cs.stop();
        }

        mbs.unregisterMBean(nameTesterHost);
        mbs.unregisterMBean(nameM3uaMan);
        mbs.unregisterMBean(nameDialogicMan);
        mbs.unregisterMBean(nameSccpMan);
        mbs.unregisterMBean(nameMapMan);
        mbs.unregisterMBean(nameCapMan);
        mbs.unregisterMBean(nameUssdClientManMan);
        mbs.unregisterMBean(nameUssdServerManMan);
        mbs.unregisterMBean(nameSmsClientManMan);
        mbs.unregisterMBean(nameSmsServerManMan);
        mbs.unregisterMBean(nameTestCapSsfMan);
        mbs.unregisterMBean(nameTestCapScfMan);

        // Registry.unbind(key);
        UnicastRemoteObject.unexportObject(reg, true);

    }
}
