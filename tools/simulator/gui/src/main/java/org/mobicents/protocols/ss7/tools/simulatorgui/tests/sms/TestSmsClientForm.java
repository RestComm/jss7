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

import javax.management.Notification;
import javax.swing.JFrame;

import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;
import javax.swing.JButton;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestSmsClientForm extends TestingForm {

	private static final long serialVersionUID = 812642028723533391L;

	private TestSmsClientManMBean smsClient; 

	private JTextField tbMessage;
	private JTextField tbDestIsdnNumber;
	private JTextField tbOrigIsdnNumber;
	private JLabel lbState;
	private JLabel lbMessage;

	public TestSmsClientForm(JFrame owner) {
		super(owner);
		
		JPanel panel = new JPanel();
		panel_c.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel label = new JLabel("Message text");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel.add(label, gbc_label);
		
		tbMessage = new JTextField();
		tbMessage.setColumns(10);
		GridBagConstraints gbc_tbMessage = new GridBagConstraints();
		gbc_tbMessage.insets = new Insets(0, 0, 5, 0);
		gbc_tbMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_tbMessage.gridx = 1;
		gbc_tbMessage.gridy = 0;
		panel.add(tbMessage, gbc_tbMessage);
		
		JLabel label_1 = new JLabel("Destination ISDN number");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		panel.add(label_1, gbc_label_1);
		
		tbDestIsdnNumber = new JTextField();
		tbDestIsdnNumber.setColumns(10);
		GridBagConstraints gbc_tbDestIsdnNumber = new GridBagConstraints();
		gbc_tbDestIsdnNumber.insets = new Insets(0, 0, 5, 0);
		gbc_tbDestIsdnNumber.fill = GridBagConstraints.HORIZONTAL;
		gbc_tbDestIsdnNumber.gridx = 1;
		gbc_tbDestIsdnNumber.gridy = 1;
		panel.add(tbDestIsdnNumber, gbc_tbDestIsdnNumber);
		
		JLabel label_2 = new JLabel("Origination ISDN number");
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 2;
		panel.add(label_2, gbc_label_2);
		
		tbOrigIsdnNumber = new JTextField();
		tbOrigIsdnNumber.setColumns(10);
		GridBagConstraints gbc_tbOrigIsdnNumber = new GridBagConstraints();
		gbc_tbOrigIsdnNumber.insets = new Insets(0, 0, 5, 0);
		gbc_tbOrigIsdnNumber.fill = GridBagConstraints.HORIZONTAL;
		gbc_tbOrigIsdnNumber.gridx = 1;
		gbc_tbOrigIsdnNumber.gridy = 2;
		panel.add(tbOrigIsdnNumber, gbc_tbOrigIsdnNumber);
		
		JButton btnSendMoforwardsm = new JButton("Send MoForwardSM");
		GridBagConstraints gbc_btnSendMoforwardsm = new GridBagConstraints();
		gbc_btnSendMoforwardsm.insets = new Insets(0, 0, 5, 0);
		gbc_btnSendMoforwardsm.gridx = 1;
		gbc_btnSendMoforwardsm.gridy = 3;
		panel.add(btnSendMoforwardsm, gbc_btnSendMoforwardsm);
		
		JLabel label_3 = new JLabel("Operation result");
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 4;
		panel.add(label_3, gbc_label_3);
		
		JLabel lbResult = new JLabel("-");
		GridBagConstraints gbc_lbResult = new GridBagConstraints();
		gbc_lbResult.insets = new Insets(0, 0, 5, 0);
		gbc_lbResult.gridx = 1;
		gbc_lbResult.gridy = 4;
		panel.add(lbResult, gbc_lbResult);
		
		JLabel label_4 = new JLabel("Message received");
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 5;
		panel.add(label_4, gbc_label_4);
		
		lbMessage = new JLabel("-");
		GridBagConstraints gbc_lbMessage = new GridBagConstraints();
		gbc_lbMessage.insets = new Insets(0, 0, 5, 0);
		gbc_lbMessage.gridx = 1;
		gbc_lbMessage.gridy = 5;
		panel.add(lbMessage, gbc_lbMessage);
		
		lbState = new JLabel("-");
		GridBagConstraints gbc_lbState = new GridBagConstraints();
		gbc_lbState.gridx = 1;
		gbc_lbState.gridy = 6;
		panel.add(lbState, gbc_lbState);
	}

	public void setData(TestSmsClientManMBean smsClient) {
		this.smsClient = smsClient;
	}

	@Override
	public void sendNotif(Notification notif) {
		super.sendNotif(notif);

//		if (notif.getMessage().startsWith("CurDialog: Rcvd: procUnstrSsReq: ")) {
//			String s1 = notif.getMessage().substring(17);
//			this.lbMessage.setText(s1);
//		}

//		if (notif.getMessage().startsWith("CurDialog: Rcvd: unstrSsResp: ")) {
//			String s1 = notif.getMessage().substring(17);
//			this.lbMessage.setText(s1);
//		}
	}

	@Override
	public void refreshState() {
		super.refreshState();

		String s1 = this.smsClient.getCurrentRequestDef();
		this.lbState.setText(s1);
	}
}
