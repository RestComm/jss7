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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.management.Notification;
import javax.management.NotificationListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;
import org.mobicents.protocols.ss7.tools.simulator.level1.DialogicManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.CapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_L1;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_L2;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_L3;
import org.mobicents.protocols.ss7.tools.simulator.management.Instance_TestTask;
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
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ati.TestAtiClientForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ati.TestAtiClientParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ati.TestAtiServerForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ati.TestAtiServerParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap.TestCapScfForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap.TestCapScfParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap.TestCapSsfForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap.TestCapSsfParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.checkimei.TestCheckImeiClientForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.checkimei.TestCheckImeiClientParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.checkimei.TestCheckImeiServerForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.checkimei.TestCheckImeiServerParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms.TestSmsClientForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms.TestSmsClientParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms.TestSmsServerForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms.TestSmsServerParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ussd.TestUssdClientForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ussd.TestUssdClientParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ussd.TestUssdServerForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.ussd.TestUssdServerParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.lcs.TestMapLcsClientForm;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.lcs.TestMapLcsClientParamForm;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.lcs.TestMapLcsServerForm;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.tests.lcs.TestMapLcsServerParamForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SimulatorGuiForm extends JFrame implements NotificationListener {

    private static final long serialVersionUID = 3154289048277602010L;

    private TesterHost hostImpl;
    private TesterHostMBean host;
    private M3uaManMBean m3ua;
    private DialogicManMBean dialogic;
    private SccpManMBean sccp;
    private MapManMBean map;
    private CapManMBean cap;
    private boolean isRemote;

    private TestUssdClientManMBean ussdClient;
    private TestUssdServerManMBean ussdServer;
    private TestSmsClientManMBean smsClient;
    private TestSmsServerManMBean smsServer;
    private TestCapScfManMBean capScf;
    private TestCapSsfManMBean capSsf;
    private TestAtiClientManMBean atiClient;
    private TestAtiServerManMBean atiServer;
    private TestCheckImeiClientManMBean checkImeiClient;
    private TestCheckImeiServerManMBean checkImeiServer;
    private TestMapLcsClientManMBean mapLcsClient;
    private TestMapLcsServerManMBean mapLcsServer;

    private TestingForm testingForm;

    private JPanel contentPane;
    private javax.swing.Timer tm;
    private JComboBox cbL1;
    private JLabel lblLayer_1;
    private JComboBox cbL2;
    private JComboBox cbL3;
    private JLabel lblLayer_2;
    private JComboBox cbTestTask;
    private JLabel lblTestingTask;
    private JButton btReload;
    private JButton btSave;
    private JButton btStart;
    private JButton btL1;
    private JButton btL2;
    private JButton btL3;
    private JButton btTestTask;
    private JButton btTermRemote;

    public SimulatorGuiForm() {
        setResizable(false);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (getDefaultCloseOperation() == JDialog.DO_NOTHING_ON_CLOSE) {
                    JOptionPane.showMessageDialog(getJFrame(), "Before exiting you must close a test window form");
                } else {
                    if (hostImpl != null) {
                        hostImpl.quit();
                    }
                }
            }
        });

        setTitle("SS7 Simulator: ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 532, 277);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblLayer = new JLabel("Layer 1");
        lblLayer.setBounds(10, 11, 46, 14);
        panel.add(lblLayer);

        cbL1 = new JComboBox();
        cbL1.setBounds(114, 11, 230, 20);
        panel.add(cbL1);

        lblLayer_1 = new JLabel("Layer 2");
        lblLayer_1.setBounds(10, 42, 46, 14);
        panel.add(lblLayer_1);

        cbL2 = new JComboBox();
        cbL2.setBounds(114, 42, 230, 20);
        panel.add(cbL2);

        cbL3 = new JComboBox();
        cbL3.setBounds(114, 73, 230, 20);
        panel.add(cbL3);

        lblLayer_2 = new JLabel("Layer 3");
        lblLayer_2.setBounds(10, 73, 46, 14);
        panel.add(lblLayer_2);

        cbTestTask = new JComboBox();
        cbTestTask.setBounds(114, 105, 230, 20);
        panel.add(cbTestTask);

        lblTestingTask = new JLabel("Testing task");
        lblTestingTask.setBounds(10, 108, 82, 14);
        panel.add(lblTestingTask);

        btReload = new JButton("Reload");
        btReload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        btReload.setBounds(10, 158, 144, 23);
        panel.add(btReload);

        btSave = new JButton("Save");
        btSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveData();
            }
        });
        btSave.setBounds(164, 158, 144, 23);
        panel.add(btSave);

        btStart = new JButton("Run test");
        btStart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openTest();
            }
        });
        btStart.setBounds(318, 158, 144, 23);
        panel.add(btStart);

        btL1 = new JButton(". . .");
        btL1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (((Instance_L1) cbL1.getSelectedItem()).intValue()) {
                    case Instance_L1.VAL_M3UA: {
                        M3uaForm frame = new M3uaForm(getJFrame());
                        frame.setData(m3ua);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_L1.VAL_DIALOGIC: {
                        DialogicForm frame = new DialogicForm(getJFrame());
                        frame.setData(dialogic);
                        frame.setVisible(true);
                    }
                        break;
                }
            }
        });
        btL1.setBounds(381, 10, 56, 23);
        panel.add(btL1);

        btL2 = new JButton(". . .");
        btL2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (((Instance_L2) cbL2.getSelectedItem()).intValue()) {
                    case Instance_L2.VAL_SCCP: {
                        SccpForm frame = new SccpForm(getJFrame());
                        frame.setData(sccp);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_L2.VAL_ISUP: {
                        // TODO: L2 data edit - ISUP
                    }
                        break;
                }
            }
        });
        btL2.setBounds(381, 41, 56, 23);
        panel.add(btL2);

        btL3 = new JButton(". . .");
        btL3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (((Instance_L3) cbL3.getSelectedItem()).intValue()) {
                    case Instance_L3.VAL_MAP: {
                        MapForm frame = new MapForm(getJFrame());
                        frame.setData(map);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_L3.VAL_CAP: {
                        CapForm frame = new CapForm(getJFrame());
                        frame.setData(cap);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_L3.VAL_INAP: {
                        // TODO: implement it ......
                    }
                        break;
                }
            }
        });
        btL3.setBounds(381, 72, 56, 23);
        panel.add(btL3);

        btTestTask = new JButton(". . .");
        btTestTask.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (((Instance_TestTask) cbTestTask.getSelectedItem()).intValue()) {
                    case Instance_TestTask.VAL_USSD_TEST_CLIENT: {
                        TestUssdClientParamForm frame = new TestUssdClientParamForm(getJFrame());
                        frame.setData(ussdClient);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_USSD_TEST_SERVER: {
                        TestUssdServerParamForm frame = new TestUssdServerParamForm(getJFrame());
                        frame.setData(ussdServer);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_SMS_TEST_CLIENT: {
                        TestSmsClientParamForm frame = new TestSmsClientParamForm(getJFrame());
                        frame.setData(smsClient);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_SMS_TEST_SERVER: {
                        TestSmsServerParamForm frame = new TestSmsServerParamForm(getJFrame());
                        frame.setData(smsServer);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_CAP_TEST_SCF: {
                        TestCapScfParamForm frame = new TestCapScfParamForm(getJFrame());
                        frame.setData(capScf);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_CAP_TEST_SSF: {
                        TestCapSsfParamForm frame = new TestCapSsfParamForm(getJFrame());
                        frame.setData(capSsf);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_ATI_TEST_CLIENT: {
                        TestAtiClientParamForm frame = new TestAtiClientParamForm(getJFrame());
                        frame.setData(atiClient);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_ATI_TEST_SERVER: {
                        TestAtiServerParamForm frame = new TestAtiServerParamForm(getJFrame());
                        frame.setData(atiServer);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_CHECK_IMEI_TEST_CLIENT: {
                        TestCheckImeiClientParamForm frame = new TestCheckImeiClientParamForm(getJFrame());
                        frame.setData(checkImeiClient);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_CHECK_IMEI_TEST_SERVER: {
                        TestCheckImeiServerParamForm frame = new TestCheckImeiServerParamForm(getJFrame());
                        frame.setData(checkImeiServer);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_MAP_LCS_TEST_CLIENT: {
                        TestMapLcsClientParamForm frame = new TestMapLcsClientParamForm(getJFrame());
                        frame.setData(mapLcsClient);
                        frame.setVisible(true);
                    }
                        break;
                    case Instance_TestTask.VAL_MAP_LCS_TEST_SERVER: {
                        TestMapLcsServerParamForm frame = new TestMapLcsServerParamForm(getJFrame());
                        frame.setData(mapLcsServer);
                        frame.setVisible(true);
                    }
                        break;

                // TODO: other tests form options editing
                }
            }
        });
        btTestTask.setBounds(381, 104, 56, 23);
        panel.add(btTestTask);

        btTermRemote = new JButton("Terminate remote host and exit");
        btTermRemote.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                host.quit();
                getJFrame().dispose();
                System.exit(NORMAL);
            }
        });
        btTermRemote.setBounds(10, 192, 297, 23);
        panel.add(btTermRemote);
    }

    private void openTest() {
        // Starting tests
        saveData();

        TestingForm dlg = null;
        switch (((Instance_TestTask) cbTestTask.getSelectedItem()).intValue()) {
            case Instance_TestTask.VAL_USSD_TEST_CLIENT: {
                TestUssdClientForm testUssdClientForm = new TestUssdClientForm(getJFrame());
                testUssdClientForm.setData(ussdClient);
                dlg = testUssdClientForm;
            }
                break;
            case Instance_TestTask.VAL_USSD_TEST_SERVER: {
                TestUssdServerForm testUssdServerForm = new TestUssdServerForm(getJFrame());
                testUssdServerForm.setData(ussdServer);
                dlg = testUssdServerForm;
            }
                break;
            case Instance_TestTask.VAL_SMS_TEST_CLIENT: {
                TestSmsClientForm testSmsClientForm = new TestSmsClientForm(getJFrame());
                testSmsClientForm.setData(smsClient);
                dlg = testSmsClientForm;
            }
                break;
            case Instance_TestTask.VAL_SMS_TEST_SERVER: {
                TestSmsServerForm testSmsServerForm = new TestSmsServerForm(getJFrame());
                testSmsServerForm.setData(smsServer);
                dlg = testSmsServerForm;
            }
                break;
            case Instance_TestTask.VAL_CAP_TEST_SCF: {
                TestCapScfForm testCapScfForm = new TestCapScfForm(getJFrame());
                testCapScfForm.setData(capScf);
                dlg = testCapScfForm;
            }
                break;
            case Instance_TestTask.VAL_CAP_TEST_SSF: {
                TestCapSsfForm testCapSsfForm = new TestCapSsfForm(getJFrame());
                testCapSsfForm.setData(capSsf);
                dlg = testCapSsfForm;
            }
                break;
            case Instance_TestTask.VAL_ATI_TEST_CLIENT: {
                TestAtiClientForm testAtiClientForm = new TestAtiClientForm(getJFrame());
                testAtiClientForm.setData(atiClient);
                dlg = testAtiClientForm;
            }
                break;
            case Instance_TestTask.VAL_ATI_TEST_SERVER: {
                TestAtiServerForm testAtiServerForm = new TestAtiServerForm(getJFrame());
                testAtiServerForm.setData(atiServer);
                dlg = testAtiServerForm;
            }
                break;
            case Instance_TestTask.VAL_CHECK_IMEI_TEST_CLIENT: {
                TestCheckImeiClientForm testCheckImeiClientForm = new TestCheckImeiClientForm(getJFrame());
                testCheckImeiClientForm.setData(checkImeiClient);
                dlg = testCheckImeiClientForm;
            }
                break;
            case Instance_TestTask.VAL_CHECK_IMEI_TEST_SERVER: {
                TestCheckImeiServerForm testCheckImeiServerForm = new TestCheckImeiServerForm(getJFrame());
                testCheckImeiServerForm.setData(checkImeiServer);
                dlg = testCheckImeiServerForm;
            }
                break;
            case Instance_TestTask.VAL_MAP_LCS_TEST_CLIENT: {
                TestMapLcsClientForm testMapLcsClientForm = new TestMapLcsClientForm(getJFrame());
                // falonso: change checkImeiClient, do not push until done so
                testMapLcsClientForm.setData(mapLcsClient);
                dlg = testMapLcsClientForm;
            }
                break;
            case Instance_TestTask.VAL_MAP_LCS_TEST_SERVER: {
                TestMapLcsServerForm testMapLcsServerForm = new TestMapLcsServerForm(getJFrame());
                // falonso: change checkImeiClient, do not push until done so
                testMapLcsServerForm.setData(mapLcsServer);
                dlg = testMapLcsServerForm;
            }
                break;


        // TODO: other tests form options editing
        }
        if (dlg == null) {
            JOptionPane.showMessageDialog(getJFrame(), "No proper test task defined");
            return;
        }

        this.enableButtons(false);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        testingForm = dlg;
        dlg.setData(this, host);
        dlg.setVisible(true);
    }

    public void testingFormClose() {
        testingForm = null;
        this.enableButtons(true);
        setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
    }

    private void enableButtons(boolean enabled) {
        this.btL1.setEnabled(enabled);
        this.btL2.setEnabled(enabled);
        this.btL3.setEnabled(enabled);
        this.btReload.setEnabled(enabled);
        this.btSave.setEnabled(enabled);
        this.btStart.setEnabled(enabled);
        this.btTermRemote.setEnabled(isRemote && enabled);
        this.btTestTask.setEnabled(enabled);

        this.cbL1.setEnabled(enabled);
        this.cbL2.setEnabled(enabled);
        this.cbL3.setEnabled(enabled);
        this.cbTestTask.setEnabled(enabled);
    }

    protected void startHost(String appName, boolean isRemote, final TesterHost hostImpl, TesterHostMBean host, M3uaManMBean m3ua, DialogicManMBean dialogic,
            SccpManMBean sccp, MapManMBean map, CapManMBean cap, TestUssdClientManMBean ussdClient, TestUssdServerManMBean ussdServer,
            TestSmsClientManMBean smsClient, TestSmsServerManMBean smsServer, TestCapScfManMBean capScf, TestCapSsfManMBean capSsf,
            TestAtiClientManMBean atiClient, TestAtiServerManMBean atiServer,
            TestCheckImeiClientManMBean checkImeiClient, TestCheckImeiServerManMBean checkImeiServer,
            TestMapLcsClientManMBean mapLcsClient, TestMapLcsServerManMBean mapLcsServer) {
        setTitle(getTitle() + appName);

        this.hostImpl = hostImpl;
        this.host = host;
        this.m3ua = m3ua;
        this.dialogic = dialogic;
        this.sccp = sccp;
        this.map = map;
        this.cap = cap;
        this.ussdClient = ussdClient;
        this.ussdServer = ussdServer;
        this.smsClient = smsClient;
        this.smsServer = smsServer;
        this.capScf = capScf;
        this.capSsf = capSsf;
        this.atiClient = atiClient;
        this.atiServer = atiServer;
        this.checkImeiClient = checkImeiClient;
        this.checkImeiServer = checkImeiServer;
        this.isRemote = isRemote;
        this.mapLcsClient = mapLcsClient;
        this.mapLcsServer = mapLcsServer;

        this.btTermRemote.setEnabled(isRemote);

        this.tm = new javax.swing.Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (hostImpl != null) {
                    hostImpl.checkStore();
                    hostImpl.execute();

                    // TODO: extra action for updating GUI from host notifications if a host is local
                }
            }
        });
        this.tm.start();

        this.reloadData();
    }

    private JFrame getJFrame() {
        return this;
    }

    private void reloadData() {
        this.setEnumeratedBaseComboBox(cbL1, this.host.getInstance_L1());
        this.setEnumeratedBaseComboBox(cbL2, this.host.getInstance_L2());
        this.setEnumeratedBaseComboBox(cbL3, this.host.getInstance_L3());
        this.setEnumeratedBaseComboBox(cbTestTask, this.host.getInstance_TestTask());
    }

    private void saveData() {
        this.host.setInstance_L1((Instance_L1) cbL1.getSelectedItem());
        this.host.setInstance_L2((Instance_L2) cbL2.getSelectedItem());
        this.host.setInstance_L3((Instance_L3) cbL3.getSelectedItem());
        this.host.setInstance_TestTask((Instance_TestTask) cbTestTask.getSelectedItem());
    }

    private void setEnumeratedBaseComboBox(JComboBox cb, EnumeratedBase defaultValue) {
        cb.removeAllItems();
        EnumeratedBase[] ebb = defaultValue.getList();
        EnumeratedBase dv = null;
        for (EnumeratedBase eb : ebb) {
            cb.addItem(eb);
            if (eb.intValue() == defaultValue.intValue())
                dv = eb;
        }
        if (dv != null)
            cb.setSelectedItem(dv);
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {

        TestingForm fm = testingForm;
        if (fm != null) {
            fm.sendNotif(notification);
        }
    }
}
