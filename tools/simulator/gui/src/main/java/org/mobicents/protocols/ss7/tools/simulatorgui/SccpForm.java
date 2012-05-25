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

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.level2.GlobalTitleType;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManMBean;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class SccpForm extends JDialog {

	private SccpManMBean sccp;
	
	private static final long serialVersionUID = 7571177143420596631L;
	private JTextField tbRemoteSpc;
	private JTextField tbLocalSpc;
	private JTextField tbNi;
	private JTextField tbRemoteSsn;
	private JTextField tbLocalSsn;
	private JTextField tbTranslationType;
	private JComboBox cbGlobalTitleType;
	private JComboBox cbAddressNature;
	private JComboBox cbNumberingPlan;

	public SccpForm(JFrame owner) {
		super(owner, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("SCCP settings");
		setBounds(100, 100, 590, 479);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblDpc = new JLabel("Remote Spc");
		lblDpc.setBounds(10, 14, 134, 14);
		panel.add(lblDpc);
		
		tbRemoteSpc = new JTextField();
		tbRemoteSpc.setColumns(10);
		tbRemoteSpc.setBounds(154, 11, 129, 20);
		panel.add(tbRemoteSpc);
		
		tbLocalSpc = new JTextField();
		tbLocalSpc.setColumns(10);
		tbLocalSpc.setBounds(154, 42, 129, 20);
		panel.add(tbLocalSpc);
		
		JLabel lblOpc = new JLabel("Local Spc");
		lblOpc.setBounds(10, 45, 134, 14);
		panel.add(lblOpc);
		
		tbNi = new JTextField();
		tbNi.setColumns(10);
		tbNi.setBounds(154, 73, 129, 20);
		panel.add(tbNi);
		
		tbRemoteSsn = new JTextField();
		tbRemoteSsn.setColumns(10);
		tbRemoteSsn.setBounds(154, 104, 129, 20);
		panel.add(tbRemoteSsn);
		
		JLabel lblNetworkIndicatpr = new JLabel("Network indicator");
		lblNetworkIndicatpr.setBounds(10, 76, 112, 14);
		panel.add(lblNetworkIndicatpr);
		
		JLabel lblRemoteSsn = new JLabel("Remote Ssn");
		lblRemoteSsn.setBounds(10, 107, 112, 14);
		panel.add(lblRemoteSsn);
		
		JButton button = new JButton("Load default values for side A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataA();
			}
		});
		button.setBounds(10, 383, 245, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Load default values for side B");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataB();
			}
		});
		button_1.setBounds(265, 383, 234, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Reload");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadData();
			}
		});
		button_2.setBounds(10, 417, 144, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("Save");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveData()) {
					getJFrame().dispose();
				}
			}
		});
		button_3.setBounds(255, 417, 117, 23);
		panel.add(button_3);
		
		JButton button_4 = new JButton("Cancel");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getJFrame().dispose();
			}
		});
		button_4.setBounds(382, 417, 117, 23);
		panel.add(button_4);
		
		tbLocalSsn = new JTextField();
		tbLocalSsn.setColumns(10);
		tbLocalSsn.setBounds(154, 135, 129, 20);
		panel.add(tbLocalSsn);
		
		JLabel lblLocalSsn = new JLabel("Local Ssn");
		lblLocalSsn.setBounds(10, 138, 112, 14);
		panel.add(lblLocalSsn);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(10, 166, 564, 159);
		panel.add(panel_1);
		
		JLabel lblParametersForCreating = new JLabel("Parameters for creating SccpAddress (when routing on GT)");
		lblParametersForCreating.setBounds(10, 0, 468, 14);
		panel_1.add(lblParametersForCreating);
		
		JLabel lblTranslationType = new JLabel("Translation type");
		lblTranslationType.setBounds(10, 119, 136, 14);
		panel_1.add(lblTranslationType);
		
		JLabel label_2 = new JLabel("AddressNature");
		label_2.setBounds(10, 57, 136, 14);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("NumberingPlan");
		label_3.setBounds(10, 88, 136, 14);
		panel_1.add(label_3);
		
		tbTranslationType = new JTextField();
		tbTranslationType.setColumns(10);
		tbTranslationType.setBounds(156, 116, 130, 20);
		panel_1.add(tbTranslationType);
		
		cbAddressNature = new JComboBox();
		cbAddressNature.setBounds(156, 54, 294, 20);
		panel_1.add(cbAddressNature);
		
		cbNumberingPlan = new JComboBox();
		cbNumberingPlan.setBounds(156, 85, 294, 20);
		panel_1.add(cbNumberingPlan);
		
		JLabel lblGlobaltitleType = new JLabel("GlobalTitle type");
		lblGlobaltitleType.setBounds(10, 29, 136, 14);
		panel_1.add(lblGlobaltitleType);
		
		cbGlobalTitleType = new JComboBox();
		cbGlobalTitleType.setBounds(156, 26, 398, 20);
		panel_1.add(cbGlobalTitleType);
	}

	public void setData(SccpManMBean sccp) {
		this.sccp = sccp;
		
		this.reloadData();
	}

	private JDialog getJFrame() {
		return this;
	}

	private void reloadData() {
		M3uaForm.setEnumeratedBaseComboBox(cbGlobalTitleType, this.sccp.getGlobalTitleType());
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.sccp.getAddressNature());
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.sccp.getNumberingPlan());

		tbRemoteSpc.setText(((Integer) this.sccp.getRemoteSpc()).toString());
		tbLocalSpc.setText(((Integer) this.sccp.getLocalSpc()).toString());
		tbNi.setText(((Integer) this.sccp.getNi()).toString());
		tbRemoteSsn.setText(((Integer) this.sccp.getRemoteSsn()).toString());
		tbLocalSsn.setText(((Integer) this.sccp.getLocalSsn()).toString());
		tbTranslationType.setText(((Integer) this.sccp.getTranslationType()).toString());
	}

	private void loadDataA() {
		M3uaForm.setEnumeratedBaseComboBox(cbGlobalTitleType, new GlobalTitleType(GlobalTitleType.VAL_TT_NP_ES_NOA));
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new AddressNatureType(AddressNature.international_number.getIndicator()));
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanType(NumberingPlan.ISDN.getIndicator()));

		tbRemoteSpc.setText("2");
		tbLocalSpc.setText("1");
		tbNi.setText("2");
		tbRemoteSsn.setText("8");
		tbLocalSsn.setText("8");
		tbTranslationType.setText("0");
	}

	private void loadDataB() {
		M3uaForm.setEnumeratedBaseComboBox(cbGlobalTitleType, new GlobalTitleType(GlobalTitleType.VAL_TT_NP_ES_NOA));
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new AddressNatureType(AddressNature.international_number.getIndicator()));
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanType(NumberingPlan.ISDN.getIndicator()));

		tbRemoteSpc.setText("1");
		tbLocalSpc.setText("2");
		tbNi.setText("2");
		tbRemoteSsn.setText("8");
		tbLocalSsn.setText("8");
		tbTranslationType.setText("0");
	}

	private boolean saveData() {
		int remoteSpc = 0;
		int localSpc = 0;
		int ni = 0;
		int remoteSsn = 0;
		int localSsn = 0;
		int translationType = 0;
		try {
			remoteSpc = Integer.parseInt(tbRemoteSpc.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Dpc value: " + e.toString());
			return false;
		}
		try {
			localSpc = Integer.parseInt(tbLocalSpc.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Opc value: " + e.toString());
			return false;
		}
		try {
			ni = Integer.parseInt(tbNi.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Network indicator value: " + e.toString());
			return false;
		}
		try {
			remoteSsn = Integer.parseInt(tbRemoteSsn.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing RemoteSsn value: " + e.toString());
			return false;
		}
		try {
			localSsn = Integer.parseInt(tbLocalSsn.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing LocalSsn value: " + e.toString());
			return false;
		}
		try {
			translationType = Integer.parseInt(tbTranslationType.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing TranslationType value: " + e.toString());
			return false;
		}

		this.sccp.setRemoteSpc(remoteSpc);
		this.sccp.setLocalSpc(localSpc);
		this.sccp.setNi(ni);
		this.sccp.setRemoteSsn(remoteSsn);
		this.sccp.setLocalSsn(localSsn);
		this.sccp.setTranslationType(translationType);

		this.sccp.setGlobalTitleType((GlobalTitleType) cbGlobalTitleType.getSelectedItem());
		this.sccp.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
		this.sccp.setNumberingPlan((NumberingPlanType) cbNumberingPlan.getSelectedItem());

		return true;
	}
}

