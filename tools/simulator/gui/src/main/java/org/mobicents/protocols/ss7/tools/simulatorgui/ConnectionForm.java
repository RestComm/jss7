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

package org.mobicents.protocols.ss7.tools.simulatorgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintWriter;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.mobicents.protocols.ss7.tools.simulator.level1.DialogicManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHostMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ati.TestAtiClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ati.TestAtiServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapScfManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapSsfManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsServerManMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class ConnectionForm extends JFrame {

    private static final long serialVersionUID = 1892971654619519775L;
    private JPanel contentPane;
    private JTextField tbUrl;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JRadioButton rbLocal;
    private JLabel lblHostName;
    private JTextField tbAppName;

    // public static void main(String[] args) {
    // EventQueue.invokeLater(new Runnable() {
    // public void run() {
    // try {
    // ConnectionForm frame = new ConnectionForm();
    // frame.setVisible(true);
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // }
    // });
    // }

    public ConnectionForm() {
        setResizable(false);
        setTitle("Connecting to a testerHost ...");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 212);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        rbLocal = new JRadioButton("Create a local testerHost");
        rbLocal.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (tbUrl != null) {
                    if (e.getStateChange() == 1) {
                        tbUrl.setEnabled(false);
                        tbAppName.setEnabled(true);
                    } else {
                        tbUrl.setEnabled(true);
                        tbAppName.setEnabled(false);
                    }
                }
            }
        });
        buttonGroup.add(rbLocal);
        rbLocal.setSelected(true);
        rbLocal.setBounds(6, 47, 239, 23);
        panel.add(rbLocal);

        JRadioButton rbRemote = new JRadioButton("Connect to the existing testerHost via JMX");
        buttonGroup.add(rbRemote);
        rbRemote.setBounds(6, 73, 293, 23);
        panel.add(rbRemote);

        tbUrl = new JTextField();
        tbUrl.setEnabled(false);
        tbUrl.setText("service:jmx:rmi:///jndi/rmi://localhost:9999/server");
        tbUrl.setBounds(6, 103, 398, 20);
        panel.add(tbUrl);
        tbUrl.setColumns(10);

        JButton btStart = new JButton("Start");
        btStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (rbLocal.isSelected()) {
                    startLocal(tbAppName.getText());
                } else {
                    startRemote(tbAppName.getText(), tbUrl.getText());
                }
            }
        });
        btStart.setBounds(138, 130, 143, 23);
        panel.add(btStart);

        lblHostName = new JLabel("Host name");
        lblHostName.setBounds(10, 11, 87, 14);
        panel.add(lblHostName);

        tbAppName = new JTextField();
        tbAppName.setBounds(114, 8, 150, 20);
        panel.add(tbAppName);
        tbAppName.setColumns(10);
    }

    public void setAppName(String appName) {
        tbAppName.setText(appName);
    }

    private void startLocal(String appName) {
        // creating a testerHost

        String sim_home = System.getenv(TesterHost.SIMULATOR_HOME_VAR);
        if (sim_home != null)
            sim_home += File.separator + "data";
        TesterHost host = new TesterHost(appName, sim_home);

        // starting the main form
        SimulatorGuiForm frame = new SimulatorGuiForm();
        host.addNotificationListener(frame, null, null);
        frame.startHost(appName + "-local", false, host, host, host.getM3uaMan(), host.getDialogicMan(), host.getSccpMan(),
                host.getMapMan(), host.getCapMan(), host.getTestUssdClientMan(), host.getTestUssdServerMan(),
                host.getTestSmsClientMan(), host.getTestSmsServerMan(), host.getTestCapScfMan(), host.getTestCapSsfMan(),
                host.getTestAtiClientMan(), host.getTestAtiServerMan(), host.getTestCheckImeiClientMan() , host.getTestCheckImeiServerMan(),
                host.getTestMapLcsClientMan(),host.getTestMapLcsServerMan());
        frame.setVisible(true);

        // closing the connection form
        this.dispose();
    }

    private void startRemote(String appName, String urlString) {
        try {
            JMXServiceURL url = new JMXServiceURL(urlString);
            JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

            // checking of existence the target domain
            String tagDomain = "SS7_Simulator_";
//            String tagDomain = "SS7_Simulator_" + appName;
            String[] domains = mbsc.getDomains();
            boolean found = false;
            for (String domain : domains) {
                if (domain.startsWith(tagDomain)) {
                    tagDomain = domain;
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Remote domain has not found: " + tagDomain);
                return;
            }

            ObjectName mbeanNameTesterHost = new ObjectName(tagDomain + ":type=TesterHost");
            TesterHostMBean host = JMX.newMBeanProxy(mbsc, mbeanNameTesterHost, TesterHostMBean.class, true);
            ObjectName mbeanNameM3ua = new ObjectName(tagDomain + ":type=M3uaMan");
            M3uaManMBean m3ua = JMX.newMBeanProxy(mbsc, mbeanNameM3ua, M3uaManMBean.class, false);
            ObjectName mbeanNameDialogic = new ObjectName(tagDomain + ":type=DialogicMan");
            DialogicManMBean dialogic = JMX.newMBeanProxy(mbsc, mbeanNameDialogic, DialogicManMBean.class, false);
            ObjectName mbeanNameSccp = new ObjectName(tagDomain + ":type=SccpMan");
            SccpManMBean sccp = JMX.newMBeanProxy(mbsc, mbeanNameSccp, SccpManMBean.class, false);
            ObjectName mbeanNameMap = new ObjectName(tagDomain + ":type=MapMan");
            MapManMBean map = JMX.newMBeanProxy(mbsc, mbeanNameMap, MapManMBean.class, false);
            ObjectName mbeanNameCap = new ObjectName(tagDomain + ":type=CapMan");
            CapManMBean cap = JMX.newMBeanProxy(mbsc, mbeanNameCap, CapManMBean.class, false);
            ObjectName mbeanNameUssdClient = new ObjectName(tagDomain + ":type=TestUssdClientMan");
            TestUssdClientManMBean ussdClient = JMX.newMBeanProxy(mbsc, mbeanNameUssdClient, TestUssdClientManMBean.class,
                    false);
            ObjectName mbeanNameUssdServer = new ObjectName(tagDomain + ":type=TestUssdServerMan");
            TestUssdServerManMBean ussdServer = JMX.newMBeanProxy(mbsc, mbeanNameUssdServer, TestUssdServerManMBean.class,
                    false);
            ObjectName mbeanNameSmsClient = new ObjectName(tagDomain + ":type=TestSmsClientMan");
            TestSmsClientManMBean smsClient = JMX.newMBeanProxy(mbsc, mbeanNameSmsClient, TestSmsClientManMBean.class, false);
            ObjectName mbeanNameSmsServer = new ObjectName(tagDomain + ":type=TestSmsServerMan");
            TestSmsServerManMBean smsServer = JMX.newMBeanProxy(mbsc, mbeanNameSmsServer, TestSmsServerManMBean.class, false);
            ObjectName mbeanNameCapScf = new ObjectName(tagDomain + ":type=TestCapScfMan");
            TestCapScfManMBean capScf = JMX.newMBeanProxy(mbsc, mbeanNameCapScf, TestCapScfManMBean.class, false);
            ObjectName mbeanNameCapSsf = new ObjectName(tagDomain + ":type=TestCapSsfMan");
            TestCapSsfManMBean capSsf = JMX.newMBeanProxy(mbsc, mbeanNameCapSsf, TestCapSsfManMBean.class, false);
            ObjectName mbeanNameAtiClient = new ObjectName(tagDomain + ":type=TestAtiClientMan");
            TestAtiClientManMBean atiClient = JMX.newMBeanProxy(mbsc, mbeanNameAtiClient, TestAtiClientManMBean.class, false);
            ObjectName mbeanNameAtiServer = new ObjectName(tagDomain + ":type=TestAtiServerMan");
            TestAtiServerManMBean atiServer = JMX.newMBeanProxy(mbsc, mbeanNameAtiServer, TestAtiServerManMBean.class, false);
            ObjectName mbeanNameCheckImeiClient = new ObjectName(tagDomain + ":type=TestCheckImeiClientMan");
            TestCheckImeiClientManMBean checkImeiClient = JMX.newMBeanProxy(mbsc, mbeanNameCheckImeiClient, TestCheckImeiClientManMBean.class, false);
            ObjectName mbeanNameCheckImeiServer = new ObjectName(tagDomain + ":type=TestCheckImeiServerMan");
            TestCheckImeiServerManMBean checkImeiServer = JMX.newMBeanProxy(mbsc, mbeanNameCheckImeiServer, TestCheckImeiServerManMBean.class, false);
            ObjectName mbeanNameMapLcsClient = new ObjectName(tagDomain + ":type=TestMapLcsClientMan");
            TestMapLcsClientManMBean checkMapLcsClient = JMX.newMBeanProxy(mbsc, mbeanNameMapLcsClient, TestMapLcsClientManMBean.class, false);
            ObjectName mbeanNameMapLcsServer = new ObjectName(tagDomain + ":type=TestMapLcsServerMan");
            TestMapLcsServerManMBean checkMapLcsServer = JMX.newMBeanProxy(mbsc, mbeanNameMapLcsServer, TestMapLcsServerManMBean.class, false);

            // checking if MBean is workable
            host.getInstance_L1_Value();

            // starting the main form
            SimulatorGuiForm frame = new SimulatorGuiForm();
            mbsc.addNotificationListener(mbeanNameTesterHost, frame, null, null);
            frame.startHost(appName + "-remote", true, null, host, m3ua, dialogic, sccp, map, cap, ussdClient, ussdServer,
                    smsClient, smsServer, capScf, capSsf, atiClient, atiServer, checkImeiClient, checkImeiServer,
                    checkMapLcsClient,checkMapLcsServer);
            frame.setVisible(true);

            // closing the connection form
            this.dispose();

        } catch (Exception e) {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter pw = new PrintWriter(baos);
            e.printStackTrace(pw);
            pw.close();
            String s1 = baos.toString();

            // JOptionPane.showMessageDialog(this, "Exception: " + e.toString() + "/n");
            // e.printStackTrace();

            ErrorForm fm = new ErrorForm("Exception: " + e.toString() + "/n" + s1, this);
            fm.setVisible(true);
        }
    }
}
