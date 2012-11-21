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

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.mobicents.protocols.ss7.tools.simulator.common.EnumeratedBase;
import org.mobicents.protocols.ss7.tools.simulator.level1.BIpChannelType;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaExchangeType;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaFunctionality;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaIPSPType;
import org.mobicents.protocols.ss7.tools.simulator.level1.M3uaManMBean;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

	public M3uaForm(JFrame owner) {
		super(owner, true);
		
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(100, 100, 534, 552);
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
		btnCance.setBounds(401, 490, 117, 23);
		panel.add(btnCance);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveData()) {
					getJFrame().dispose();
				}
			}
		});
		btnSave.setBounds(274, 490, 117, 23);
		panel.add(btnSave);

		JButton btLoadA = new JButton("Load default values for side A");
		btLoadA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataA();
			}
		});
		btLoadA.setBounds(10, 456, 254, 23);
		panel.add(btLoadA);

		JButton btLoadB = new JButton("Load default values for side B");
		btLoadB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataB();
			}
		});
		btLoadB.setBounds(274, 456, 244, 23);
		panel.add(btLoadB);

		JButton btReload = new JButton("Reload");
		btReload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadData();
			}
		});
		btReload.setBounds(10, 490, 144, 23);
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
		tbLocalPort.setText(((Integer)this.m3ua.getSctpLocalPort()).toString());
		tbRemoteHost.setText(this.m3ua.getSctpRemoteHost());
		tbRemotePort.setText(((Integer)this.m3ua.getSctpRemotePort()).toString());
		tbSctpExtraHostAddresses.setText(this.m3ua.getSctpExtraHostAddresses());

		setEnumeratedBaseComboBox(cbM3uaFunctionality, this.m3ua.getM3uaFunctionality());
		setEnumeratedBaseComboBox(cbM3uaIPSPType, this.m3ua.getM3uaIPSPType());
		setEnumeratedBaseComboBox(cbM3uaExchangeType, this.m3ua.getM3uaExchangeType());
		tbM3uaDpc.setText(((Integer)this.m3ua.getM3uaDpc()).toString());
		tbM3uaOpc.setText(((Integer)this.m3ua.getM3uaOpc()).toString());
		tbM3uaSi.setText(((Integer)this.m3ua.getM3uaSi()).toString());
		tbM3uaRoutingContext.setText(((Long)this.m3ua.getM3uaRoutingContext()).toString());
		tbM3uaNetworkAppearance.setText(((Long)this.m3ua.getM3uaNetworkAppearance()).toString());
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
		tbSctpExtraHostAddresses.setText("");

		setEnumeratedBaseComboBox(cbM3uaFunctionality, new M3uaFunctionality(M3uaFunctionality.VAL_IPSP));
		setEnumeratedBaseComboBox(cbM3uaIPSPType, new M3uaIPSPType(M3uaIPSPType.VAL_CLIENT));
		setEnumeratedBaseComboBox(cbM3uaExchangeType, new M3uaExchangeType(M3uaExchangeType.VAL_SE));
		tbM3uaDpc.setText("2");
		tbM3uaOpc.setText("1");
		tbM3uaSi.setText("3");
		tbM3uaRoutingContext.setText("101");
		tbM3uaNetworkAppearance.setText("102");
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
		tbSctpExtraHostAddresses.setText("");

		setEnumeratedBaseComboBox(cbM3uaFunctionality, new M3uaFunctionality(M3uaFunctionality.VAL_IPSP));
		setEnumeratedBaseComboBox(cbM3uaIPSPType, new M3uaIPSPType(M3uaIPSPType.VAL_SERVER));
		setEnumeratedBaseComboBox(cbM3uaExchangeType, new M3uaExchangeType(M3uaExchangeType.VAL_SE));
		tbM3uaDpc.setText("1");
		tbM3uaOpc.setText("2");
		tbM3uaSi.setText("3");
		tbM3uaRoutingContext.setText("101");
		tbM3uaNetworkAppearance.setText("102");
	}

	private boolean saveData() {
		
		int localPort = 0;
		int remotePort = 0;
		int dpc = 0;
		int opc = 0;
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
			remotePort = Integer.parseInt(tbRemotePort.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Remote port value: " + e.toString());
			return false;
		}
		try {
			dpc = Integer.parseInt(tbM3uaDpc.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Dpc value: " + e.toString());
			return false;
		}
		try {
			opc = Integer.parseInt(tbM3uaOpc.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Opc value: " + e.toString());
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
		this.m3ua.setSctpExtraHostAddresses(tbSctpExtraHostAddresses.getText());

		this.m3ua.setM3uaFunctionality((M3uaFunctionality) cbM3uaFunctionality.getSelectedItem());
		this.m3ua.setM3uaIPSPType((M3uaIPSPType) cbM3uaIPSPType.getSelectedItem());
		this.m3ua.setM3uaExchangeType((M3uaExchangeType) cbM3uaExchangeType.getSelectedItem());

		this.m3ua.setM3uaDpc(dpc);
		this.m3ua.setM3uaOpc(opc);
		this.m3ua.setM3uaSi(si);
		this.m3ua.setM3uaRoutingContext(routingContext);
		this.m3ua.setM3uaNetworkAppearance(networkAppearance);
		
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

