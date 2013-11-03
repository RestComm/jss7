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

package org.mobicents.protocols.ss7.tools.simulatorgui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;
import org.mobicents.protocols.ss7.tools.simulator.level1.BIpChannelType;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaExchangeType;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaFunctionality;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaIPSPType;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class M3uaForm extends JDialog {

    private M3uaManMBean m3ua;

    private static final long serialVersionUID = -655124100358576731L;
    private JComboBox cbIPChannelType;
    private JComboBox cbSctpIsServer;
    private JTextField tbLocalHost;
    private JTextField tbLocalPort;
    private JTextField tbRemoteHost;
    private JTextField tbRemotePort;
    private JTextField tbSctpExtraHostAddresses;
    private JTextField tbM3uaDpc;
    private JTextField tbM3uaOpc;
    private JTextField tbM3uaSi;
    private JTextField tbM3uaRoutingContext;
    private JTextField tbM3uaNetworkAppearance;
    private JComboBox cbM3uaFunctionality;
    private JLabel lblMuaExchangeType;
    private JComboBox cbM3uaExchangeType;
    private JComboBox cbM3uaIPSPType;
    private JCheckBox cbStorePcapTrace;
    private JTextField tbLocalHost2;
    private JTextField tbRemoteHost2;
    private JTextField tbLocalPort2;
    private JTextField tbRemotePort2;
    private JTextField tbM3uaDpc2;
    private JTextField tbM3uaOpc2;

    public M3uaForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 662, 586);
        setTitle("M3UA settings");

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        cbIPChannelType = new JComboBox();
        cbIPChannelType.setBounds(197, 11, 129, 20);
        panel.add(cbIPChannelType);

        JLabel lblIpChannelType = new JLabel("IP channel type");
        lblIpChannelType.setBounds(10, 14, 129, 14);
        panel.add(lblIpChannelType);

        JLabel lblSctpRole = new JLabel("SCTP role");
        lblSctpRole.setBounds(10, 45, 129, 14);
        panel.add(lblSctpRole);

        tbLocalHost = new JTextField();
        tbLocalHost.setBounds(197, 73, 212, 20);
        panel.add(tbLocalHost);
        tbLocalHost.setColumns(10);

        cbSctpIsServer = new JComboBox();
        cbSctpIsServer.setBounds(197, 42, 129, 20);
        panel.add(cbSctpIsServer);

        JLabel lblLocalHost = new JLabel("SCTP local host");
        lblLocalHost.setBounds(10, 76, 129, 14);
        panel.add(lblLocalHost);

        JButton btnCance = new JButton("Cancel");
        btnCance.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        btnCance.setBounds(401, 517, 117, 23);
        panel.add(btnCance);

        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        btnSave.setBounds(274, 517, 117, 23);
        panel.add(btnSave);

        JButton btLoadA = new JButton("Load default values for side A");
        btLoadA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        btLoadA.setBounds(10, 483, 254, 23);
        panel.add(btLoadA);

        JButton btLoadB = new JButton("Load default values for side B");
        btLoadB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        btLoadB.setBounds(274, 483, 244, 23);
        panel.add(btLoadB);

        JButton btReload = new JButton("Reload");
        btReload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        btReload.setBounds(10, 517, 144, 23);
        panel.add(btReload);

        tbLocalPort = new JTextField();
        tbLocalPort.setColumns(10);
        tbLocalPort.setBounds(197, 104, 129, 20);
        panel.add(tbLocalPort);

        JLabel lblLocalPort = new JLabel("SCTP local port");
        lblLocalPort.setBounds(10, 107, 129, 14);
        panel.add(lblLocalPort);

        tbRemoteHost = new JTextField();
        tbRemoteHost.setColumns(10);
        tbRemoteHost.setBounds(197, 135, 212, 20);
        panel.add(tbRemoteHost);

        JLabel lblRemoteHost = new JLabel("SCTP remote host");
        lblRemoteHost.setBounds(10, 138, 129, 14);
        panel.add(lblRemoteHost);

        JLabel lblRemotePort = new JLabel("SCTP remote port");
        lblRemotePort.setBounds(10, 169, 129, 14);
        panel.add(lblRemotePort);

        tbRemotePort = new JTextField();
        tbRemotePort.setColumns(10);
        tbRemotePort.setBounds(197, 166, 129, 20);
        panel.add(tbRemotePort);

        JLabel lblSctpExtraHost = new JLabel("SCTP extra host addresses");
        lblSctpExtraHost.setBounds(10, 197, 165, 14);
        panel.add(lblSctpExtraHost);

        tbSctpExtraHostAddresses = new JTextField();
        tbSctpExtraHostAddresses.setColumns(10);
        tbSctpExtraHostAddresses.setBounds(197, 194, 275, 20);
        panel.add(tbSctpExtraHostAddresses);

        JLabel lblMuaFunctionality = new JLabel("M3UA functionality");
        lblMuaFunctionality.setBounds(10, 225, 129, 14);
        panel.add(lblMuaFunctionality);

        cbM3uaFunctionality = new JComboBox();
        cbM3uaFunctionality.setBounds(197, 222, 129, 20);
        panel.add(cbM3uaFunctionality);

        JLabel lblMuaIpspType = new JLabel("M3UA IPSP type");
        lblMuaIpspType.setBounds(10, 253, 129, 14);
        panel.add(lblMuaIpspType);

        cbM3uaIPSPType = new JComboBox();
        cbM3uaIPSPType.setBounds(197, 250, 129, 20);
        panel.add(cbM3uaIPSPType);

        lblMuaExchangeType = new JLabel("M3UA exchange type");
        lblMuaExchangeType.setBounds(10, 281, 129, 14);
        panel.add(lblMuaExchangeType);

        cbM3uaExchangeType = new JComboBox();
        cbM3uaExchangeType.setBounds(197, 278, 129, 20);
        panel.add(cbM3uaExchangeType);

        JLabel lblMuaDpc = new JLabel("M3UA dpc");
        lblMuaDpc.setBounds(10, 309, 129, 14);
        panel.add(lblMuaDpc);

        tbM3uaDpc = new JTextField();
        tbM3uaDpc.setColumns(10);
        tbM3uaDpc.setBounds(197, 306, 129, 20);
        panel.add(tbM3uaDpc);

        JLabel lblMuaOpc = new JLabel("M3UA opc");
        lblMuaOpc.setBounds(10, 337, 129, 14);
        panel.add(lblMuaOpc);

        tbM3uaOpc = new JTextField();
        tbM3uaOpc.setColumns(10);
        tbM3uaOpc.setBounds(197, 334, 129, 20);
        panel.add(tbM3uaOpc);

        JLabel lblMuaServiceIndicator = new JLabel("M3UA service indicator");
        lblMuaServiceIndicator.setBounds(10, 365, 165, 14);
        panel.add(lblMuaServiceIndicator);

        tbM3uaSi = new JTextField();
        tbM3uaSi.setColumns(10);
        tbM3uaSi.setBounds(197, 362, 129, 20);
        panel.add(tbM3uaSi);

        JLabel lblMuaRoutingContext = new JLabel("M3UA routing context");
        lblMuaRoutingContext.setBounds(10, 393, 165, 14);
        panel.add(lblMuaRoutingContext);

        tbM3uaRoutingContext = new JTextField();
        tbM3uaRoutingContext.setColumns(10);
        tbM3uaRoutingContext.setBounds(197, 390, 129, 20);
        panel.add(tbM3uaRoutingContext);

        tbM3uaNetworkAppearance = new JTextField();
        tbM3uaNetworkAppearance.setColumns(10);
        tbM3uaNetworkAppearance.setBounds(197, 418, 129, 20);
        panel.add(tbM3uaNetworkAppearance);

        JLabel lblMuaNetworkAppearance = new JLabel("M3UA network appearance");
        lblMuaNetworkAppearance.setBounds(10, 421, 177, 14);
        panel.add(lblMuaNetworkAppearance);

        cbStorePcapTrace = new JCheckBox("Storing all transmitted/received data into MsgLog_*.pcap file");
        cbStorePcapTrace.setBounds(10, 444, 508, 25);
        panel.add(cbStorePcapTrace);

        tbLocalHost2 = new JTextField();
        tbLocalHost2.setColumns(10);
        tbLocalHost2.setBounds(431, 72, 212, 20);
        panel.add(tbLocalHost2);

        tbRemoteHost2 = new JTextField();
        tbRemoteHost2.setColumns(10);
        tbRemoteHost2.setBounds(431, 134, 212, 20);
        panel.add(tbRemoteHost2);

        tbLocalPort2 = new JTextField();
        tbLocalPort2.setColumns(10);
        tbLocalPort2.setBounds(431, 103, 129, 20);
        panel.add(tbLocalPort2);

        tbRemotePort2 = new JTextField();
        tbRemotePort2.setColumns(10);
        tbRemotePort2.setBounds(431, 165, 129, 20);
        panel.add(tbRemotePort2);

        tbM3uaDpc2 = new JTextField();
        tbM3uaDpc2.setColumns(10);
        tbM3uaDpc2.setBounds(431, 305, 129, 20);
        panel.add(tbM3uaDpc2);

        tbM3uaOpc2 = new JTextField();
        tbM3uaOpc2.setColumns(10);
        tbM3uaOpc2.setBounds(431, 333, 129, 20);
        panel.add(tbM3uaOpc2);
    }

    public void setData(M3uaManMBean m3ua) {
        this.m3ua = m3ua;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        setEnumeratedBaseComboBox(cbIPChannelType, this.m3ua.getSctpIPChannelType());

        cbSctpIsServer.removeAllItems();
        cbSctpIsServer.addItem("Client");
        cbSctpIsServer.addItem("Server");
        if (this.m3ua.isSctpIsServer())
            cbSctpIsServer.setSelectedIndex(1);
        else
            cbSctpIsServer.setSelectedIndex(0);

        tbLocalHost.setText(this.m3ua.getSctpLocalHost());
        tbLocalPort.setText(((Integer) this.m3ua.getSctpLocalPort()).toString());
        tbRemoteHost.setText(this.m3ua.getSctpRemoteHost());
        tbRemotePort.setText(((Integer) this.m3ua.getSctpRemotePort()).toString());
        tbLocalHost2.setText(this.m3ua.getSctpLocalHost2());
        tbLocalPort2.setText(((Integer) this.m3ua.getSctpLocalPort2()).toString());
        tbRemoteHost2.setText(this.m3ua.getSctpRemoteHost2());
        tbRemotePort2.setText(((Integer) this.m3ua.getSctpRemotePort2()).toString());
        tbSctpExtraHostAddresses.setText(this.m3ua.getSctpExtraHostAddresses());

        setEnumeratedBaseComboBox(cbM3uaFunctionality, this.m3ua.getM3uaFunctionality());
        setEnumeratedBaseComboBox(cbM3uaIPSPType, this.m3ua.getM3uaIPSPType());
        setEnumeratedBaseComboBox(cbM3uaExchangeType, this.m3ua.getM3uaExchangeType());
        tbM3uaDpc.setText(((Integer) this.m3ua.getM3uaDpc()).toString());
        tbM3uaOpc.setText(((Integer) this.m3ua.getM3uaOpc()).toString());
        tbM3uaDpc2.setText(((Integer) this.m3ua.getM3uaDpc2()).toString());
        tbM3uaOpc2.setText(((Integer) this.m3ua.getM3uaOpc2()).toString());
        tbM3uaSi.setText(((Integer) this.m3ua.getM3uaSi()).toString());
        tbM3uaRoutingContext.setText(((Long) this.m3ua.getM3uaRoutingContext()).toString());
        tbM3uaNetworkAppearance.setText(((Long) this.m3ua.getM3uaNetworkAppearance()).toString());

        this.cbStorePcapTrace.setSelected(this.m3ua.getStorePcapTrace());
    }

    private void loadDataA() {
        this.setEnumeratedBaseComboBox(cbIPChannelType, new BIpChannelType(BIpChannelType.VAL_TCP));

        cbSctpIsServer.removeAllItems();
        cbSctpIsServer.addItem("Client");
        cbSctpIsServer.addItem("Server");
        cbSctpIsServer.setSelectedIndex(0);

        tbLocalHost.setText("127.0.0.1");
        tbLocalPort.setText("8011");
        tbRemoteHost.setText("127.0.0.1");
        tbRemotePort.setText("8012");
        tbLocalHost2.setText("");
        tbLocalPort2.setText("0");
        tbRemoteHost2.setText("");
        tbRemotePort2.setText("0");
        tbSctpExtraHostAddresses.setText("");

        setEnumeratedBaseComboBox(cbM3uaFunctionality, new M3uaFunctionality(M3uaFunctionality.VAL_IPSP));
        setEnumeratedBaseComboBox(cbM3uaIPSPType, new M3uaIPSPType(M3uaIPSPType.VAL_CLIENT));
        setEnumeratedBaseComboBox(cbM3uaExchangeType, new M3uaExchangeType(M3uaExchangeType.VAL_SE));
        tbM3uaDpc.setText("2");
        tbM3uaOpc.setText("1");
        tbM3uaDpc2.setText("0");
        tbM3uaOpc2.setText("0");
        tbM3uaSi.setText("3");
        tbM3uaRoutingContext.setText("101");
        tbM3uaNetworkAppearance.setText("102");

        this.cbStorePcapTrace.setSelected(false);
    }

    private void loadDataB() {
        setEnumeratedBaseComboBox(cbIPChannelType, new BIpChannelType(BIpChannelType.VAL_TCP));

        cbSctpIsServer.removeAllItems();
        cbSctpIsServer.addItem("Client");
        cbSctpIsServer.addItem("Server");
        cbSctpIsServer.setSelectedIndex(1);

        tbLocalHost.setText("127.0.0.1");
        tbLocalPort.setText("8012");
        tbRemoteHost.setText("127.0.0.1");
        tbRemotePort.setText("8011");
        tbLocalHost2.setText("");
        tbLocalPort2.setText("0");
        tbRemoteHost2.setText("");
        tbRemotePort2.setText("0");
        tbSctpExtraHostAddresses.setText("");

        setEnumeratedBaseComboBox(cbM3uaFunctionality, new M3uaFunctionality(M3uaFunctionality.VAL_IPSP));
        setEnumeratedBaseComboBox(cbM3uaIPSPType, new M3uaIPSPType(M3uaIPSPType.VAL_SERVER));
        setEnumeratedBaseComboBox(cbM3uaExchangeType, new M3uaExchangeType(M3uaExchangeType.VAL_SE));
        tbM3uaDpc.setText("1");
        tbM3uaOpc.setText("2");
        tbM3uaDpc2.setText("0");
        tbM3uaOpc2.setText("0");
        tbM3uaSi.setText("3");
        tbM3uaRoutingContext.setText("101");
        tbM3uaNetworkAppearance.setText("102");

        this.cbStorePcapTrace.setSelected(false);
    }

    private boolean saveData() {

        int localPort = 0;
        int remotePort = 0;
        int localPort2 = 0;
        int remotePort2 = 0;
        int dpc = 0;
        int opc = 0;
        int dpc2 = 0;
        int opc2 = 0;
        int si = 0;
        long routingContext = 0;
        long networkAppearance = 0;
        try {
            localPort = Integer.parseInt(tbLocalPort.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Local port value: " + e.toString());
            return false;
        }
        try {
            localPort2 = Integer.parseInt(tbLocalPort2.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Local port 2 value: " + e.toString());
            return false;
        }
        try {
            remotePort = Integer.parseInt(tbRemotePort.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Remote port value: " + e.toString());
            return false;
        }
        try {
            remotePort2 = Integer.parseInt(tbRemotePort2.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Remote port 2 value: " + e.toString());
            return false;
        }
        try {
            dpc = Integer.parseInt(tbM3uaDpc.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Dpc value: " + e.toString());
            return false;
        }
        try {
            dpc2 = Integer.parseInt(tbM3uaDpc2.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Dpc 2 value: " + e.toString());
            return false;
        }
        try {
            opc = Integer.parseInt(tbM3uaOpc.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Opc value: " + e.toString());
            return false;
        }
        try {
            opc2 = Integer.parseInt(tbM3uaOpc2.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Opc 2 value: " + e.toString());
            return false;
        }
        try {
            si = Integer.parseInt(tbM3uaSi.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Service indicator value: " + e.toString());
            return false;
        }
        try {
            routingContext = Integer.parseInt(tbM3uaRoutingContext.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Routing context value: " + e.toString());
            return false;
        }
        try {
            networkAppearance = Integer.parseInt(tbM3uaNetworkAppearance.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Network appearance value: " + e.toString());
            return false;
        }

        this.m3ua.setSctpIPChannelType((BIpChannelType) cbIPChannelType.getSelectedItem());

        if (cbSctpIsServer.getSelectedIndex() == 0)
            this.m3ua.setSctpIsServer(false);
        else
            this.m3ua.setSctpIsServer(true);

        this.m3ua.setSctpLocalHost(tbLocalHost.getText());
        this.m3ua.setSctpLocalPort(localPort);
        this.m3ua.setSctpRemoteHost(tbRemoteHost.getText());
        this.m3ua.setSctpRemotePort(remotePort);
        this.m3ua.setSctpLocalHost2(tbLocalHost2.getText());
        this.m3ua.setSctpLocalPort2(localPort2);
        this.m3ua.setSctpRemoteHost2(tbRemoteHost2.getText());
        this.m3ua.setSctpRemotePort2(remotePort2);
        this.m3ua.setSctpExtraHostAddresses(tbSctpExtraHostAddresses.getText());

        this.m3ua.setM3uaFunctionality((M3uaFunctionality) cbM3uaFunctionality.getSelectedItem());
        this.m3ua.setM3uaIPSPType((M3uaIPSPType) cbM3uaIPSPType.getSelectedItem());
        this.m3ua.setM3uaExchangeType((M3uaExchangeType) cbM3uaExchangeType.getSelectedItem());

        this.m3ua.setM3uaDpc(dpc);
        this.m3ua.setM3uaOpc(opc);
        this.m3ua.setM3uaDpc2(dpc2);
        this.m3ua.setM3uaOpc2(opc2);
        this.m3ua.setM3uaSi(si);
        this.m3ua.setM3uaRoutingContext(routingContext);
        this.m3ua.setM3uaNetworkAppearance(networkAppearance);

        this.m3ua.setStorePcapTrace(this.cbStorePcapTrace.isSelected());

        return true;
    }

    public static void setEnumeratedBaseComboBox(JComboBox cb, EnumeratedBase defaultValue) {
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
}
