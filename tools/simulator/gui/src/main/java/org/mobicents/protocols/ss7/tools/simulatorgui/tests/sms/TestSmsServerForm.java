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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.management.Notification;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsServerForm extends TestingForm {

    private static final long serialVersionUID = 7219729321344799776L;

    private TestSmsServerManMBean smsServer;

    private JTextField tbMessage;
    private JTextField tbDestIsdnNumber;
    private JTextField tbOrigIsdnNumber;
    private JTextField tbImsi;
    private JTextField tbVlrNumber;
    private JLabel lbMessage;
    private JLabel lbResult;
    private JLabel lbState;

    public TestSmsServerForm(JFrame owner) {
        super(owner);

        JPanel panel = new JPanel();
        panel_c.add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JLabel label = new JLabel("Message text");
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(0, 0, 5, 5);
        gbc_label.anchor = GridBagConstraints.EAST;
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

        JLabel lblDestinationIsdmNumber = new JLabel("Destination ISDN number");
        GridBagConstraints gbc_lblDestinationIsdmNumber = new GridBagConstraints();
        gbc_lblDestinationIsdmNumber.anchor = GridBagConstraints.EAST;
        gbc_lblDestinationIsdmNumber.insets = new Insets(0, 0, 5, 5);
        gbc_lblDestinationIsdmNumber.gridx = 0;
        gbc_lblDestinationIsdmNumber.gridy = 1;
        panel.add(lblDestinationIsdmNumber, gbc_lblDestinationIsdmNumber);

        tbDestIsdnNumber = new JTextField();
        tbDestIsdnNumber.setColumns(10);
        GridBagConstraints gbc_tbDestIsdnNumber = new GridBagConstraints();
        gbc_tbDestIsdnNumber.insets = new Insets(0, 0, 5, 0);
        gbc_tbDestIsdnNumber.fill = GridBagConstraints.HORIZONTAL;
        gbc_tbDestIsdnNumber.gridx = 1;
        gbc_tbDestIsdnNumber.gridy = 1;
        panel.add(tbDestIsdnNumber, gbc_tbDestIsdnNumber);

        JLabel lblOriginationIsdnNumber = new JLabel("Origination ISDN number");
        GridBagConstraints gbc_lblOriginationIsdnNumber = new GridBagConstraints();
        gbc_lblOriginationIsdnNumber.anchor = GridBagConstraints.EAST;
        gbc_lblOriginationIsdnNumber.insets = new Insets(0, 0, 5, 5);
        gbc_lblOriginationIsdnNumber.gridx = 0;
        gbc_lblOriginationIsdnNumber.gridy = 2;
        panel.add(lblOriginationIsdnNumber, gbc_lblOriginationIsdnNumber);

        tbOrigIsdnNumber = new JTextField();
        tbOrigIsdnNumber.setColumns(10);
        GridBagConstraints gbc_tbOrigIsdnNumber = new GridBagConstraints();
        gbc_tbOrigIsdnNumber.insets = new Insets(0, 0, 5, 0);
        gbc_tbOrigIsdnNumber.fill = GridBagConstraints.HORIZONTAL;
        gbc_tbOrigIsdnNumber.gridx = 1;
        gbc_tbOrigIsdnNumber.gridy = 2;
        panel.add(tbOrigIsdnNumber, gbc_tbOrigIsdnNumber);

        JLabel lblImsi = new JLabel("IMSI");
        GridBagConstraints gbc_lblImsi = new GridBagConstraints();
        gbc_lblImsi.anchor = GridBagConstraints.EAST;
        gbc_lblImsi.insets = new Insets(0, 0, 5, 5);
        gbc_lblImsi.gridx = 0;
        gbc_lblImsi.gridy = 3;
        panel.add(lblImsi, gbc_lblImsi);

        tbImsi = new JTextField();
        tbImsi.setColumns(10);
        GridBagConstraints gbc_tbImsi = new GridBagConstraints();
        gbc_tbImsi.insets = new Insets(0, 0, 5, 0);
        gbc_tbImsi.fill = GridBagConstraints.HORIZONTAL;
        gbc_tbImsi.gridx = 1;
        gbc_tbImsi.gridy = 3;
        panel.add(tbImsi, gbc_tbImsi);

        JLabel lblVlrNumber = new JLabel("VLR number");
        GridBagConstraints gbc_lblVlrNumber = new GridBagConstraints();
        gbc_lblVlrNumber.anchor = GridBagConstraints.EAST;
        gbc_lblVlrNumber.insets = new Insets(0, 0, 5, 5);
        gbc_lblVlrNumber.gridx = 0;
        gbc_lblVlrNumber.gridy = 4;
        panel.add(lblVlrNumber, gbc_lblVlrNumber);

        tbVlrNumber = new JTextField();
        tbVlrNumber.setColumns(10);
        GridBagConstraints gbc_tbVlrNumber = new GridBagConstraints();
        gbc_tbVlrNumber.insets = new Insets(0, 0, 5, 0);
        gbc_tbVlrNumber.fill = GridBagConstraints.HORIZONTAL;
        gbc_tbVlrNumber.gridx = 1;
        gbc_tbVlrNumber.gridy = 4;
        panel.add(tbVlrNumber, gbc_tbVlrNumber);

        lbState = new JLabel("-");
        GridBagConstraints gbc_lbState = new GridBagConstraints();
        gbc_lbState.insets = new Insets(0, 0, 5, 5);
        gbc_lbState.gridx = 1;
        gbc_lbState.gridy = 8;
        panel.add(lbState, gbc_lbState);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 5;
        panel.add(panel_1, gbc_panel_1);

        JButton btnSendSriforsm = new JButton("Send SRIForSM");
        btnSendSriforsm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendSRIForSM();
            }
        });
        btnSendSriforsm.setBounds(0, 0, 164, 23);
        panel_1.add(btnSendSriforsm);

        JButton btnSendSriforsmmtforwardsm = new JButton("Send SRIForSM + MtForwardSM");
        btnSendSriforsmmtforwardsm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendSRIForSM_Mtforwardsm();
            }
        });
        btnSendSriforsmmtforwardsm.setBounds(174, 0, 234, 23);
        panel_1.add(btnSendSriforsmmtforwardsm);

        JButton btnSendMtforwardsm = new JButton("Send MtForwardSM");
        btnSendMtforwardsm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendMtforwardsm();
            }
        });
        btnSendMtforwardsm.setBounds(414, 0, 180, 23);
        panel_1.add(btnSendMtforwardsm);

        JLabel label_1 = new JLabel("Operation result");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 6;
        panel.add(label_1, gbc_label_1);

        lbResult = new JLabel("-");
        GridBagConstraints gbc_lbResult = new GridBagConstraints();
        gbc_lbResult.insets = new Insets(0, 0, 5, 0);
        gbc_lbResult.gridx = 1;
        gbc_lbResult.gridy = 6;
        panel.add(lbResult, gbc_lbResult);

        JLabel label_2 = new JLabel("Message received");
        GridBagConstraints gbc_label_2 = new GridBagConstraints();
        gbc_label_2.insets = new Insets(0, 0, 0, 5);
        gbc_label_2.gridx = 0;
        gbc_label_2.gridy = 7;
        panel.add(label_2, gbc_label_2);

        lbMessage = new JLabel("-");
        GridBagConstraints gbc_lbMessage = new GridBagConstraints();
        gbc_lbMessage.gridx = 1;
        gbc_lbMessage.gridy = 7;
        panel.add(lbMessage, gbc_lbMessage);
    }

    public void setData(TestSmsServerManMBean smsServer) {
        this.smsServer = smsServer;
    }

    private void sendSRIForSM() {
        this.lbMessage.setText("");
        String msg = this.tbDestIsdnNumber.getText();
        String res = this.smsServer.performSRIForSM(msg);
        this.lbResult.setText(res);
    }

    private void sendMtforwardsm() {
        this.lbMessage.setText("");
        String msg = this.tbMessage.getText();
        String destImsi = this.tbImsi.getText();
        String vlrNumber = this.tbVlrNumber.getText();
        String origIsdnNumber = this.tbOrigIsdnNumber.getText();
        String res = this.smsServer.performMtForwardSM(msg, destImsi, vlrNumber, origIsdnNumber);
        this.lbResult.setText(res);
    }

    private void sendSRIForSM_Mtforwardsm() {
        this.lbMessage.setText("");
        String msg = this.tbMessage.getText();
        String destIsdnNumber = this.tbDestIsdnNumber.getText();
        String origIsdnNumber = this.tbOrigIsdnNumber.getText();
        String res = this.smsServer.performSRIForSM_MtForwardSM(msg, destIsdnNumber, origIsdnNumber);
        this.lbResult.setText(res);
    }

    @Override
    public void sendNotif(Notification notif) {
        super.sendNotif(notif);

        // if (notif.getMessage().startsWith("CurDialog: Rcvd: procUnstrSsReq: ")) {
        // String s1 = notif.getMessage().substring(17);
        // this.lbMessage.setText(s1);
        // }
        //
        // if (notif.getMessage().startsWith("CurDialog: Rcvd: unstrSsResp: ")) {
        // String s1 = notif.getMessage().substring(17);
        // this.lbMessage.setText(s1);
        // }
    }

    @Override
    public void refreshState() {
        super.refreshState();

        String s1 = this.smsServer.getCurrentRequestDef();
        this.lbState.setText(s1);
    }
}
