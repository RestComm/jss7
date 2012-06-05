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
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsClientManMBean;

import java.awt.Color;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestSmsClientParamForm extends JDialog {
	private static final long serialVersionUID = 5428271328162943202L;

	private TestSmsClientManMBean smsClient;

	private JTextField tbServiceCenterAddress;
	private JTextField tbSRIResponseImsi;
	private JTextField tbSRIResponseVlr;
	private JTextField tbSmscSsn;

	public TestSmsClientParamForm(JFrame owner) {
		super(owner, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("SMS test client settings");
		setBounds(100, 100, 539, 478);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel label = new JLabel("MAP protocol version");
		label.setBounds(10, 14, 204, 14);
		panel.add(label);
		
		JComboBox cbMapProtocolVersion = new JComboBox();
		cbMapProtocolVersion.setBounds(367, 11, 154, 20);
		panel.add(cbMapProtocolVersion);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(10, 42, 511, 94);
		panel.add(panel_1);
		
		JLabel label_1 = new JLabel("Parameters for AddressString creation");
		label_1.setBounds(10, 0, 266, 14);
		panel_1.add(label_1);
		
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
		
		JLabel lblDestinationServiceCenter = new JLabel("Destination Service center address string");
		lblDestinationServiceCenter.setBounds(10, 150, 339, 14);
		panel.add(lblDestinationServiceCenter);
		
		tbServiceCenterAddress = new JTextField();
		tbServiceCenterAddress.setColumns(10);
		tbServiceCenterAddress.setBounds(367, 147, 154, 20);
		panel.add(tbServiceCenterAddress);
		
		JLabel lblImsiForAuto = new JLabel("IMSI for auto sendRoutingInfoForSM response");
		lblImsiForAuto.setBounds(10, 178, 339, 14);
		panel.add(lblImsiForAuto);
		
		tbSRIResponseImsi = new JTextField();
		tbSRIResponseImsi.setColumns(10);
		tbSRIResponseImsi.setBounds(367, 175, 154, 20);
		panel.add(tbSRIResponseImsi);
		
		JLabel lblVlrAddressFor = new JLabel("VLR address for auto sendRoutingInfoForSM response");
		lblVlrAddressFor.setBounds(10, 206, 339, 14);
		panel.add(lblVlrAddressFor);
		
		tbSRIResponseVlr = new JTextField();
		tbSRIResponseVlr.setColumns(10);
		tbSRIResponseVlr.setBounds(367, 203, 154, 20);
		panel.add(tbSRIResponseVlr);
		
		JLabel lblSmscSsnFor = new JLabel("SMSC SSN for outgoing SccpAddress (default value: 8)");
		lblSmscSsnFor.setBounds(10, 234, 415, 14);
		panel.add(lblSmscSsnFor);
		
		tbSmscSsn = new JTextField();
		tbSmscSsn.setColumns(10);
		tbSmscSsn.setBounds(435, 231, 86, 20);
		panel.add(tbSmscSsn);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setLayout(null);
		panel_2.setBounds(10, 259, 511, 94);
		panel.add(panel_2);
		
		JLabel label_4 = new JLabel("Parameters for SMS tpdu origAddress");
		label_4.setBounds(10, 0, 266, 14);
		panel_2.add(label_4);
		
		JLabel label_5 = new JLabel("TypeOfNumber");
		label_5.setBounds(10, 28, 174, 14);
		panel_2.add(label_5);
		
		JLabel label_6 = new JLabel("NumberingPlanIdentification");
		label_6.setBounds(10, 59, 174, 14);
		panel_2.add(label_6);
		
		JComboBox cbTypeOfNumber = new JComboBox();
		cbTypeOfNumber.setBounds(194, 25, 307, 20);
		panel_2.add(cbTypeOfNumber);
		
		JComboBox cbNumberingPlanIdentification = new JComboBox();
		cbNumberingPlanIdentification.setBounds(194, 56, 307, 20);
		panel_2.add(cbNumberingPlanIdentification);
		
		JButton button = new JButton("Load default values for side A");
		button.setBounds(10, 374, 246, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Reload");
		button_1.setBounds(10, 408, 144, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Save");
		button_2.setBounds(180, 408, 117, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("Load default values for side B");
		button_3.setBounds(266, 374, 255, 23);
		panel.add(button_3);
		
		JButton button_4 = new JButton("Cancel");
		button_4.setBounds(404, 408, 117, 23);
		panel.add(button_4);
	}

}
