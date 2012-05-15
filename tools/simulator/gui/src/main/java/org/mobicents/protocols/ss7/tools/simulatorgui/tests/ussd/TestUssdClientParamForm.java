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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.ussd;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestUssdClientParamForm extends JDialog {

	private static final long serialVersionUID = -2576986645852329809L;

	private TestUssdClientManMBean ussdClient;

	private JTextField tbMsisdnAddress;
	private JComboBox cbAddressNature;
	private JComboBox cbNumberingPlan;
	private JTextField tbDataCodingScheme;
	private JTextField tbAlertingPattern;

	public TestUssdClientParamForm(JFrame owner) {
		super(owner, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("USSD test client settings");
		setBounds(100, 100, 537, 308);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(10, 11, 511, 131);
		panel.add(panel_1);
		panel_1.setLayout(null);
		
		JLabel lblMsiswdn = new JLabel("Msisdn value");
		lblMsiswdn.setBounds(10, 0, 79, 14);
		panel_1.add(lblMsiswdn);
		
		JLabel lblString = new JLabel("String");
		lblString.setBounds(10, 22, 46, 14);
		panel_1.add(lblString);
		
		JLabel lblNewLabel = new JLabel("AddressNature");
		lblNewLabel.setBounds(10, 50, 136, 14);
		panel_1.add(lblNewLabel);
		
		JLabel lblNumberingplan = new JLabel("NumberingPlan");
		lblNumberingplan.setBounds(10, 81, 136, 14);
		panel_1.add(lblNumberingplan);
		
		tbMsisdnAddress = new JTextField();
		tbMsisdnAddress.setBounds(99, 19, 245, 20);
		panel_1.add(tbMsisdnAddress);
		tbMsisdnAddress.setColumns(10);

		cbAddressNature = new JComboBox();
		cbAddressNature.setBounds(156, 47, 294, 20);
		panel_1.add(cbAddressNature);

		cbNumberingPlan = new JComboBox();
		cbNumberingPlan.setBounds(156, 78, 294, 20);
		panel_1.add(cbNumberingPlan);
		
		JLabel lblDataCodingScheme = new JLabel("Data coding scheme");
		lblDataCodingScheme.setBounds(10, 153, 149, 14);
		panel.add(lblDataCodingScheme);
		
		JLabel lblNewLabel_1 = new JLabel("Alerting pattern value (-1 means does not use AlertingPattern)");
		lblNewLabel_1.setBounds(10, 184, 384, 14);
		panel.add(lblNewLabel_1);
		
		tbDataCodingScheme = new JTextField();
		tbDataCodingScheme.setBounds(423, 150, 86, 20);
		panel.add(tbDataCodingScheme);
		tbDataCodingScheme.setColumns(10);
		
		tbAlertingPattern = new JTextField();
		tbAlertingPattern.setColumns(10);
		tbAlertingPattern.setBounds(423, 181, 86, 20);
		panel.add(tbAlertingPattern);
		
		JButton button = new JButton("Load default values for side A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataA();
			}
		});
		button.setBounds(10, 212, 256, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Load default values for side B");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataB();
			}
		});
		button_1.setBounds(276, 212, 245, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Reload");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadData();
			}
		});
		button_2.setBounds(10, 246, 144, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("Save");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveData()) {
					getJFrame().dispose();
				}
			}
		});
		button_3.setBounds(277, 246, 117, 23);
		panel.add(button_3);
		
		JButton button_4 = new JButton("Cancel");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getJFrame().dispose();
			}
		});
		button_4.setBounds(404, 246, 117, 23);
		panel.add(button_4);
	}

	public void setData(TestUssdClientManMBean ussdClient) {
		this.ussdClient = ussdClient;
		
		this.reloadData();
	}

	private JDialog getJFrame() {
		return this;
	}

	private void reloadData() {
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.ussdClient.getMsisdnAddressNature());
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.ussdClient.getMsisdnNumberingPlan());

		tbMsisdnAddress.setText(this.ussdClient.getMsisdnAddress());

		tbDataCodingScheme.setText(((Integer)this.ussdClient.getDataCodingScheme()).toString());
		tbAlertingPattern.setText(((Integer)this.ussdClient.getAlertingPattern()).toString());
	}

	private void loadDataA() {
		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new AddressNatureType(AddressNature.international_number.getIndicator()));
		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanType(NumberingPlan.ISDN.getIndicator()));

		tbMsisdnAddress.setText("");

		tbDataCodingScheme.setText("15");
		tbAlertingPattern.setText("-1");
	}

	private void loadDataB() {
		loadDataA();
	}

	private boolean saveData() {
		int dataCodingScheme = 0;
		int alertingPattern = 0;
		try {
			dataCodingScheme = Integer.parseInt(tbDataCodingScheme.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing DataCodingScheme value: " + e.toString());
			return false;
		}
		try {
			alertingPattern = Integer.parseInt(tbAlertingPattern.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Alerting Pattern value: " + e.toString());
			return false;
		}

		this.ussdClient.setMsisdnAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
		this.ussdClient.setMsisdnNumberingPlan((NumberingPlanType) cbNumberingPlan.getSelectedItem());
		this.ussdClient.setMsisdnAddress(tbMsisdnAddress.getText());

		this.ussdClient.setDataCodingScheme(dataCodingScheme);
		this.ussdClient.setAlertingPattern(alertingPattern);

		return true;
	}
}

