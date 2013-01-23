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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap;

import javax.management.Notification;
import javax.swing.JFrame;

import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapScfManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestCapScfForm extends TestingForm {

	private static final long serialVersionUID = 1917238827266265536L;

	private TestCapScfManMBean capScf;
	private JLabel lbResult;
	private JButton btCloseDialog;
	private JButton btInitiateCallAttempt;
	private JLabel lbState;

	public TestCapScfForm(JFrame owner) {
		super(owner);
		
		JPanel panel = new JPanel();
		panel_c.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		btCloseDialog = new JButton("Close Dialog");
		btCloseDialog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}
		});
		GridBagConstraints gbc_btCloseDialog = new GridBagConstraints();
		gbc_btCloseDialog.insets = new Insets(0, 0, 5, 5);
		gbc_btCloseDialog.gridx = 1;
		gbc_btCloseDialog.gridy = 0;
		panel.add(btCloseDialog, gbc_btCloseDialog);
		
		btInitiateCallAttempt = new JButton("initiateCallAttempt");
		GridBagConstraints gbc_btInitiateCallAttempt = new GridBagConstraints();
		gbc_btInitiateCallAttempt.insets = new Insets(0, 0, 5, 0);
		gbc_btInitiateCallAttempt.gridx = 2;
		gbc_btInitiateCallAttempt.gridy = 0;
		panel.add(btInitiateCallAttempt, gbc_btInitiateCallAttempt);
		
		lbResult = new JLabel("-");
		GridBagConstraints gbc_lbResult = new GridBagConstraints();
		gbc_lbResult.insets = new Insets(0, 0, 5, 0);
		gbc_lbResult.gridwidth = 2;
		gbc_lbResult.gridx = 1;
		gbc_lbResult.gridy = 5;
		panel.add(lbResult, gbc_lbResult);
		
		lbState = new JLabel("-");
		GridBagConstraints gbc_lbState = new GridBagConstraints();
		gbc_lbState.gridwidth = 2;
		gbc_lbState.insets = new Insets(0, 0, 0, 5);
		gbc_lbState.gridx = 1;
		gbc_lbState.gridy = 6;
		panel.add(lbState, gbc_lbState);

		this.setDialogClosed();
	}

	public void setData(TestCapScfManMBean capScf) {
		this.capScf = capScf;
	}

	private void closeDialog() {
		String res = this.capScf.closeCurrentDialog();
		this.lbResult.setText(res);
		setDialogClosed();
	}

	private void setDialogOpenedClosed(boolean opened) {
		this.btInitiateCallAttempt.setEnabled(!opened);

//		this.btCloseDialog.setEnabled(opened);
	}

	private void setDialogOpened() {
		setDialogOpenedClosed(true);
	}

	private void setDialogClosed() {
		setDialogOpenedClosed(false);
	}

	@Override
	public void sendNotif(Notification notif) {
		super.sendNotif(notif);

		if (notif.getMessage().startsWith("DlgClosed:")) {
			this.setDialogClosed();
		}
		if (notif.getMessage().startsWith("DlgStarted:") || notif.getMessage().startsWith("DlgAccepted:")) {
			this.setDialogOpened();
		}
	}

	@Override
	public void refreshState() {
		super.refreshState();

		String s1 = this.capScf.getCurrentRequestDef();
		this.lbState.setText(s1);
	}

}
