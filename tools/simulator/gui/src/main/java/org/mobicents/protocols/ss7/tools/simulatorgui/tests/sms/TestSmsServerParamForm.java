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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.NumberingPlanType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.ProcessSsRequestAction;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestSmsServerParamForm extends JDialog {
	private static final long serialVersionUID = 8609564738597978248L;

	private TestSmsServerManMBean smsServer;
	
	private JTextField tbHlrSsn;
	private JTextField tbVlrSsn;

	public TestSmsServerParamForm(JFrame owner) {
		super(owner, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("SMS test server settings");
		setBounds(100, 100, 539, 437);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setLayout(null);
		panel_1.setBounds(10, 42, 511, 94);
		panel.add(panel_1);
		
		JLabel lblParametersForAddressstring = new JLabel("Parameters for AddressString creation");
		lblParametersForAddressstring.setBounds(10, 0, 266, 14);
		panel_1.add(lblParametersForAddressstring);
		
		JLabel label_2 = new JLabel("AddressNature");
		label_2.setBounds(10, 28, 174, 14);
		panel_1.add(label_2);
		
		JLabel label_3 = new JLabel("NumberingPlan");
		label_3.setBounds(10, 59, 174, 14);
		panel_1.add(label_3);
		
		JComboBox cbAddressNature = new JComboBox();
		cbAddressNature.setBounds(194, 25, 307, 20);
		panel_1.add(cbAddressNature);
		
		JComboBox cbNumberingPlan = new JComboBox();
		cbNumberingPlan.setBounds(194, 56, 307, 20);
		panel_1.add(cbNumberingPlan);
		
		JComboBox cbMapProtocolVersion = new JComboBox();
		cbMapProtocolVersion.setBounds(367, 11, 154, 20);
		panel.add(cbMapProtocolVersion);
		
		JLabel lblMapProtocolVersion = new JLabel("MAP protocol version");
		lblMapProtocolVersion.setBounds(10, 14, 204, 14);
		panel.add(lblMapProtocolVersion);
		
		tbHlrSsn = new JTextField();
		tbHlrSsn.setBounds(435, 147, 86, 20);
		panel.add(tbHlrSsn);
		tbHlrSsn.setColumns(10);
		
		tbVlrSsn = new JTextField();
		tbVlrSsn.setColumns(10);
		tbVlrSsn.setBounds(435, 178, 86, 20);
		panel.add(tbVlrSsn);
		
		JLabel lblHlrSsnFor = new JLabel("HLR SSN for outgoing SccpAddress (default value: 6)");
		lblHlrSsnFor.setBounds(10, 150, 415, 14);
		panel.add(lblHlrSsnFor);
		
		JLabel lblVlrSsnFor = new JLabel("VLR SSN for outgoing SccpAddress (default value: 8)");
		lblVlrSsnFor.setBounds(10, 181, 415, 14);
		panel.add(lblVlrSsnFor);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setLayout(null);
		panel_2.setBounds(10, 209, 511, 94);
		panel.add(panel_2);
		
		JLabel lblParametersForSms = new JLabel("Parameters for SMS tpdu origAddress");
		lblParametersForSms.setBounds(10, 0, 266, 14);
		panel_2.add(lblParametersForSms);
		
		JLabel lblTypeofnumber = new JLabel("TypeOfNumber");
		lblTypeofnumber.setBounds(10, 28, 174, 14);
		panel_2.add(lblTypeofnumber);
		
		JLabel lblNumberingplanidentification = new JLabel("NumberingPlanIdentification");
		lblNumberingplanidentification.setBounds(10, 59, 174, 14);
		panel_2.add(lblNumberingplanidentification);
		
		JComboBox cbTypeOfNumber = new JComboBox();
		cbTypeOfNumber.setBounds(194, 25, 307, 20);
		panel_2.add(cbTypeOfNumber);
		
		JComboBox cbNumberingPlanIdentification = new JComboBox();
		cbNumberingPlanIdentification.setBounds(194, 56, 307, 20);
		panel_2.add(cbNumberingPlanIdentification);
		
		JButton button = new JButton("Load default values for side A");
		button.setBounds(10, 324, 246, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Load default values for side B");
		button_1.setBounds(266, 324, 255, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Cancel");
		button_2.setBounds(404, 358, 117, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("Save");
		button_3.setBounds(180, 358, 117, 23);
		panel.add(button_3);
		
		JButton button_4 = new JButton("Reload");
		button_4.setBounds(10, 358, 144, 23);
		panel.add(button_4);
	}

	public void setData(TestSmsServerManMBean smsServer) {
		this.smsServer = smsServer;
		
		this.reloadData();
	}

	private JDialog getJFrame() {
		return this;
	}

	private void reloadData() {
//		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.ussdServer.getMsisdnAddressNature());
//		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.ussdServer.getMsisdnNumberingPlan());
//		M3uaForm.setEnumeratedBaseComboBox(cbProcessSsRequestAction, this.ussdServer.getProcessSsRequestAction());
//
//		tbMsisdnAddress.setText(this.ussdServer.getMsisdnAddress());
//		tbAutoResponseString.setText(this.ussdServer.getAutoResponseString());
//		tbAutoUnstructured_SS_RequestString.setText(this.ussdServer.getAutoUnstructured_SS_RequestString());
//
//		tbDataCodingScheme.setText(((Integer)this.ussdServer.getDataCodingScheme()).toString());
//		tbAlertingPattern.setText(((Integer)this.ussdServer.getAlertingPattern()).toString());
//
//		cbOneNotificationFor100Dialogs.setSelected(this.ussdServer.isOneNotificationFor100Dialogs());
	}

	private void loadDataA() {
//		M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new AddressNatureType(AddressNature.international_number.getIndicator()));
//		M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanType(NumberingPlan.ISDN.getIndicator()));
//		M3uaForm.setEnumeratedBaseComboBox(cbProcessSsRequestAction, new ProcessSsRequestAction(ProcessSsRequestAction.VAL_MANUAL_RESPONSE));
//
//		tbMsisdnAddress.setText("");
//		tbAutoResponseString.setText("");
//		tbAutoUnstructured_SS_RequestString.setText("");
//
//		tbDataCodingScheme.setText("15");
//		tbAlertingPattern.setText("-1");
//
//		cbOneNotificationFor100Dialogs.setSelected(false);
	}

	private void loadDataB() {
		loadDataA();
	}

	private boolean saveData() {
//		int dataCodingScheme = 0;
//		int alertingPattern = 0;
//		try {
//			dataCodingScheme = Integer.parseInt(tbDataCodingScheme.getText());
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(this, "Exception when parsing DataCodingScheme value: " + e.toString());
//			return false;
//		}
//		try {
//			alertingPattern = Integer.parseInt(tbAlertingPattern.getText());
//		} catch (Exception e) {
//			JOptionPane.showMessageDialog(this, "Exception when parsing Alerting Pattern value: " + e.toString());
//			return false;
//		}
//
//		this.ussdServer.setMsisdnAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
//		this.ussdServer.setMsisdnNumberingPlan((NumberingPlanType) cbNumberingPlan.getSelectedItem());
//		this.ussdServer.setProcessSsRequestAction((ProcessSsRequestAction) cbProcessSsRequestAction.getSelectedItem());
//
//		this.ussdServer.setMsisdnAddress(tbMsisdnAddress.getText());
//		this.ussdServer.setAutoResponseString(tbAutoResponseString.getText());
//		this.ussdServer.setAutoUnstructured_SS_RequestString(tbAutoUnstructured_SS_RequestString.getText());
//
//		this.ussdServer.setDataCodingScheme(dataCodingScheme);
//		this.ussdServer.setAlertingPattern(alertingPattern);
//
//		this.ussdServer.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());

		return true;
	}
}

