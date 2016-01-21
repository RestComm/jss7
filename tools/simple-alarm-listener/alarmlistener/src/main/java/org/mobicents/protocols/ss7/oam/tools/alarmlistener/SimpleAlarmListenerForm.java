/*
 * TeleStax, Open Source Cloud Communications  Copyright 2012
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

package org.mobicents.protocols.ss7.oam.tools.alarmlistener;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JRadioButton;
import javax.swing.JList;
import javax.swing.ButtonGroup;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;

import javax.swing.ListSelectionModel;

import org.mobicents.protocols.ss7.oam.common.alarm.AlarmMessage;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmNotification;
import org.mobicents.protocols.ss7.oam.common.alarm.AlarmProviderMBean;
import org.mobicents.protocols.ss7.oam.common.alarm.CurrentAlarmList;
import org.mobicents.protocols.ss7.oam.common.statistics.CounterProviderManagementMBean;
import org.mobicents.protocols.ss7.oam.common.tcap.TcapManagementJmxMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SimpleAlarmListenerForm extends JFrame implements NotificationListener {
    private JTextField tbUrl;
    private JTextField tbAlarmName;
    private JTextField tbStatName;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JButton btConnect;
    private JButton btDisconnect;
    private JRadioButton rbLocal;
    private JRadioButton rbRemote;
    private JList lstProc;
    private BeanProcess[] beanList;
    private JButton btClear;
    private JButton btGetCurrentalarmlist;
    private JButton btStatProc;
    private AlarmProviderMBean alarmHost;
    private CounterProviderManagementMBean statHost;
    private TcapManagementJmxMBean tcapMan;
    private CustomJTable tbAlarm;
    private DefaultTableModel model = new DefaultTableModel();
    private JMXConnector jmxc;
    private JTextField tbTemplate;

    public SimpleAlarmListenerForm() {
        setResizable(false);
        setTitle("Simple alarm listener");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1141, 759);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        tbUrl = new JTextField();
        tbUrl.setText("service:jmx:rmi:///jndi/rmi://localhost:9998/server");
        tbUrl.setColumns(10);
        tbUrl.setBounds(139, 205, 436, 20);
        panel.add(tbUrl);

        JLabel lblHostUrl = new JLabel("Host url");
        lblHostUrl.setBounds(10, 208, 119, 14);
        panel.add(lblHostUrl);

        tbAlarmName = new JTextField();
        tbAlarmName.setText("AlarmHost");
        tbAlarmName.setColumns(10);
        tbAlarmName.setBounds(210, 233, 119, 20);
        panel.add(tbAlarmName);

        JLabel lblBeanName = new JLabel("Bean names");
        lblBeanName.setBounds(10, 236, 119, 14);
        panel.add(lblBeanName);

        btConnect = new JButton("Connect");
        btConnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                connect(tbAlarmName.getText(), tbStatName.getText(), tbUrl.getText());
            }
        });
        btConnect.setBounds(139, 264, 143, 23);
        panel.add(btConnect);

        btDisconnect = new JButton("Disconnect");
        btDisconnect.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                disconnect();
            }
        });
        btDisconnect.setEnabled(false);
        btDisconnect.setBounds(292, 264, 182, 23);
        panel.add(btDisconnect);

        rbLocal = new JRadioButton("Local processes");
        rbLocal.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    setConnectOptEnabled(true);
                }
            }
        });
        buttonGroup.add(rbLocal);
        rbLocal.setSelected(true);
        rbLocal.setBounds(6, 7, 194, 23);
        panel.add(rbLocal);

        lstProc = new JList<BeanProcess>();
        reloadLocalProcesslist();
        lstProc.setBounds(10, 37, 876, 131);
        panel.add(lstProc);

        rbRemote = new JRadioButton("Remote processes");
        rbRemote.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                    setConnectOptEnabled(false);
                }
            }
        });
        buttonGroup.add(rbRemote);
        rbRemote.setBounds(10, 175, 248, 23);
        panel.add(rbRemote);

        btClear = new JButton("Clear");
        btClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btClear.setEnabled(false);
        btClear.setBounds(139, 298, 143, 23);
        panel.add(btClear);

        btGetCurrentalarmlist = new JButton("GetCurrentAlarmList");
        btGetCurrentalarmlist.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getCurrentAlarmList();
            }
        });
        btGetCurrentalarmlist.setEnabled(false);
        btGetCurrentalarmlist.setBounds(292, 298, 182, 23);
        panel.add(btGetCurrentalarmlist);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 333, 1135, 398);
        panel.add(scrollPane);

        tbAlarm = new CustomJTable();
        tbAlarm.setRowSelectionAllowed(false);
        tbAlarm.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tbAlarm.setFillsViewportHeight(true);
        tbAlarm.setBorder(new LineBorder(new Color(0, 0, 0)));

        tbAlarm.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Cleared", "TimeAlarm", "TimeClear",
                "Severity", "Source", "objectName", "objectPath", "problemName", "cause" }) {
            Class[] columnTypes = new Class[] { String.class, String.class, String.class, String.class, String.class,
                    String.class, String.class, String.class, String.class };

            public Class getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }

            boolean[] columnEditables = new boolean[] { false, false, false, false, false, false, false, false, false };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        tbAlarm.getColumnModel().getColumn(0).setMinWidth(30);
        tbAlarm.getColumnModel().getColumn(0).setMaxWidth(50);
        tbAlarm.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbAlarm.getColumnModel().getColumn(1).setPreferredWidth(30);
        tbAlarm.getColumnModel().getColumn(2).setPreferredWidth(30);
        tbAlarm.getColumnModel().getColumn(3).setMinWidth(30);
        tbAlarm.getColumnModel().getColumn(3).setMaxWidth(100);
        tbAlarm.getColumnModel().getColumn(3).setPreferredWidth(40);
        tbAlarm.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbAlarm.getColumnModel().getColumn(5).setPreferredWidth(60);
        tbAlarm.getColumnModel().getColumn(6).setPreferredWidth(160);
        tbAlarm.getColumnModel().getColumn(7).setPreferredWidth(100);
        tbAlarm.getColumnModel().getColumn(8).setPreferredWidth(40);

        scrollPane.setViewportView(tbAlarm);

        JLabel lblAmarm = new JLabel("Alarm");
        lblAmarm.setBounds(139, 236, 67, 14);
        panel.add(lblAmarm);

        JLabel lblStatistic = new JLabel("Statistic");
        lblStatistic.setBounds(336, 236, 67, 14);
        panel.add(lblStatistic);

        tbStatName = new JTextField();
        tbStatName.setText("CounterHost");
        tbStatName.setColumns(10);
        tbStatName.setBounds(407, 233, 119, 20);
        panel.add(tbStatName);

        btStatProc = new JButton("Statistic processing");
        btStatProc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                statProcessing();
            }
        });
        btStatProc.setEnabled(false);
        btStatProc.setBounds(478, 297, 171, 25);
        panel.add(btStatProc);

        JButton btReloadLocalPRocesses = new JButton("Reload local processes");
        btReloadLocalPRocesses.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                reloadLocalProcesslist();
            }
        });
        btReloadLocalPRocesses.setBounds(212, 6, 203, 25);
        panel.add(btReloadLocalPRocesses);

        tbTemplate = new JTextField();
        tbTemplate.setEditable(false);
        tbTemplate.setText("Template for JBoss: service:jmx:rmi://localhost/jndi/rmi://localhost:1090/jmxconnector");
        tbTemplate.setColumns(10);
        tbTemplate.setBounds(587, 204, 516, 20);
        panel.add(tbTemplate);

        model = (DefaultTableModel) tbAlarm.getModel();
    }

    private void reloadLocalProcesslist() {
        this.createLocalProcessesList();
        lstProc.setListData(beanList);
    }

    private void statProcessing() {
        StatProcessForm fm = new StatProcessForm(this.statHost);
        fm.setModal(true);
        fm.setVisible(true);
    }

    private void setConnectOptEnabled(boolean isLocal) {
        if (this.lstProc == null) {
            return;
        }

        this.lstProc.setEnabled(isLocal);

        this.btConnect.setEnabled(true);
        this.btDisconnect.setEnabled(false);
        this.btClear.setEnabled(false);
        this.btStatProc.setEnabled(false);
        this.btGetCurrentalarmlist.setEnabled(false);

        this.tbAlarmName.setEnabled(!isLocal);
        this.tbStatName.setEnabled(!isLocal);
        this.tbUrl.setEnabled(!isLocal);

        this.rbLocal.setEnabled(true);
        this.rbRemote.setEnabled(true);

        this.tbAlarm.setEnabled(false);
    }

    private void setConnectOptDisabled() {
        this.lstProc.setEnabled(false);

        this.btConnect.setEnabled(false);
        this.btDisconnect.setEnabled(true);
        this.btClear.setEnabled(true);
        this.btStatProc.setEnabled(true);
        this.btGetCurrentalarmlist.setEnabled(true);

        this.tbAlarmName.setEnabled(false);
        this.tbStatName.setEnabled(false);
        this.tbUrl.setEnabled(false);

        this.rbLocal.setEnabled(false);
        this.rbRemote.setEnabled(false);

        this.tbAlarm.setEnabled(true);
    }

    private void createLocalProcessesList() {
        ArrayList<BeanProcess> lst = new ArrayList<BeanProcess>();

        List<VirtualMachineDescriptor> list = VirtualMachine.list();
        for (VirtualMachineDescriptor vmd : list) {
            String desc = vmd.toString();
            try {
                BeanProcess bp = new BeanProcess();
                bp.vm = VirtualMachine.attach(vmd);

                int i2 = desc.indexOf(":");
                if (i2 > 0)
                    desc = desc.substring(i2 + 1);

                bp.name = desc;
                lst.add(bp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AttachNotSupportedException e) {
                e.printStackTrace();
            }
        }

        beanList = new BeanProcess[lst.size()];
        lst.toArray(beanList);
    }

    private void connect(String alarmName, String statName, String urlString) {
        String CONNECTOR_ADDRESS_PROPERTY = "com.sun.management.jmxremote.localConnectorAddress";

        try {
            if (this.rbLocal.isSelected()) {
                int i1 = this.lstProc.getSelectedIndex();
                if (i1 < 0 || i1 > this.beanList.length)
                    return;
                VirtualMachine vm = this.beanList[i1].vm;

                Properties props = vm.getAgentProperties();
                String connectorAddress = props.getProperty(CONNECTOR_ADDRESS_PROPERTY);
                if (connectorAddress == null) {
                    props = vm.getSystemProperties();
                    String home = props.getProperty("java.home");
                    String agent = home + File.separator + "lib" + File.separator + "management-agent.jar";
                    vm.loadAgent(agent);
                    props = vm.getAgentProperties();
                    connectorAddress = props.getProperty(CONNECTOR_ADDRESS_PROPERTY);
                }
                JMXServiceURL url = new JMXServiceURL(connectorAddress);
                jmxc = JMXConnectorFactory.connect(url);
            } else {
                JMXServiceURL url = new JMXServiceURL(urlString);
                jmxc = JMXConnectorFactory.connect(url, null);
            }

            MBeanServerConnection mbsc = jmxc.getMBeanServerConnection();

            // checking of existence the target domain
            String tagDomain = "org.mobicents.ss7";
            String[] domains = mbsc.getDomains();
            boolean found = false;
            for (String domain : domains) {
                if (tagDomain.equals(domain)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Remote domain has not found: " + tagDomain);
                jmxc.close();
                return;
            }

            // alarm connecting
            ObjectName mbeanNameSs7ManagementAlarm = new ObjectName(tagDomain + ":layer=ALARM,type=Management,name=" + alarmName);
            alarmHost = JMX.newMBeanProxy(mbsc, mbeanNameSs7ManagementAlarm, AlarmProviderMBean.class, true);

            // checking if MBean is workable
            String s1 = alarmHost.getAlarmProviderObjectPath();

            mbsc.addNotificationListener(mbeanNameSs7ManagementAlarm, this, null, null);

            // stat connecting
            ObjectName mbeanNameSs7ManagementStat = new ObjectName(tagDomain + ":layer=COUNTER,type=Management,name=" + statName);
            ObjectName mbeanNameTcapManagement = new ObjectName(tagDomain + ":layer=TCAP,type=Management,name=TcapStack");
            statHost = JMX.newMBeanProxy(mbsc, mbeanNameSs7ManagementStat, CounterProviderManagementMBean.class, true);
            tcapMan = JMX.newMBeanProxy(mbsc, mbeanNameTcapManagement, TcapManagementJmxMBean.class, true);

            // checking if MBean is workable
            String[] ss1 = statHost.getCounterDefSetList();
            tcapMan.setStatisticsEnabled(true);

            setConnectOptDisabled();
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

    private void disconnect() {
        try {
            this.jmxc.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        this.jmxc = null;
        this.alarmHost = null;
        this.statHost = null;

        setConnectOptEnabled(this.rbLocal.isSelected());
        this.btConnect.setEnabled(true);
        this.btDisconnect.setEnabled(false);
        this.btClear.setEnabled(false);
        this.btStatProc.setEnabled(false);
        this.btGetCurrentalarmlist.setEnabled(false);
    }

    private void clear() {
        model.getDataVector().clear();
        model.newRowsAdded(new TableModelEvent(model));
    }

    private void getCurrentAlarmList() {
        CurrentAlarmList cal = null;
        if (this.alarmHost != null) {
            try {
                cal = this.alarmHost.getCurrentAlarmList();
            } catch (Throwable e) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintWriter pw = new PrintWriter(baos);
                e.printStackTrace(pw);
                pw.close();
                String s1 = baos.toString();

                ErrorForm fm = new ErrorForm("Exception: " + e.toString() + "/n" + s1, this);
                fm.setVisible(true);
            }
        }

        if (cal != null) {
            for (AlarmMessage alm : cal.getCurrentAlarmList()) {
                this.insertAlarm(alm);
            }
        }
    }

    private class BeanProcess {
        public VirtualMachine vm;
        public String name;

        @Override
        public String toString() {
            return name;
        }
    }

    private void insertAlarm(AlarmMessage alm) {
        Vector newRow = new Vector();

        if (alm.getIsCleared())
            newRow.add("+");
        else
            newRow.add("");
        newRow.add(getDateString(alm.getTimeAlarm()));
        newRow.add(getDateString(alm.getTimeClear()));
        newRow.add(alm.getAlarmSeverity().toString());
        newRow.add(alm.getAlarmSource());
        newRow.add(alm.getObjectName());
        newRow.add(alm.getObjectPath());
        newRow.add(alm.getProblemName());
        newRow.add(alm.getCause());

        model.getDataVector().add(0, newRow);

        model.newRowsAdded(new TableModelEvent(model));
    }

    private String getDateString(Calendar dt) {
        if (dt == null)
            return "";

        SimpleDateFormat stf = new SimpleDateFormat();
        SimpleDateFormat stf2 = new SimpleDateFormat(stf.toPattern() + ":ss");
        String s1 = stf2.format(dt.getTime());

        return s1;
    }

    @Override
    public void handleNotification(Notification notification, Object handback) {
        AlarmNotification an = (AlarmNotification) notification;
        insertAlarm(an.getAlarmMessage());
    }
}
