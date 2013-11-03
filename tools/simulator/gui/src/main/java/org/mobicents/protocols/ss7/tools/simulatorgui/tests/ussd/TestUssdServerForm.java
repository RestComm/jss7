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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.ussd;

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

import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.ProcessSsRequestAction;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdServerForm extends TestingForm {

    private static final long serialVersionUID = -3323069535001828614L;

    private TestUssdServerManMBean ussdServer;

    private JTextField tbMessage;
    private JLabel lbMessage;
    private JLabel lbResult;
    private JLabel lbState;
    private JButton btnSendProcessunstructuredresponse;
    private JButton btnSendUnstructuredrequest;
    private JButton btnSendUnstructurednotify;
    private JButton btnCloseCurrentDialog;

    public TestUssdServerForm(JFrame owner) {
        super(owner);

        JPanel panel = new JPanel();
        panel_c.add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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

        btnSendProcessunstructuredresponse = new JButton("Send ProcessUnstructuredResponse");
        btnSendProcessunstructuredresponse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendProcessUnstructuredResponse();
            }
        });
        GridBagConstraints gbc_btnSendProcessunstructuredresponse = new GridBagConstraints();
        gbc_btnSendProcessunstructuredresponse.insets = new Insets(0, 0, 5, 0);
        gbc_btnSendProcessunstructuredresponse.gridx = 1;
        gbc_btnSendProcessunstructuredresponse.gridy = 1;
        panel.add(btnSendProcessunstructuredresponse, gbc_btnSendProcessunstructuredresponse);

        btnSendUnstructuredrequest = new JButton("Send UnstructuredRequest");
        btnSendUnstructuredrequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendUnstructuredRequest();
            }
        });
        GridBagConstraints gbc_btnSendUnstructuredrequest = new GridBagConstraints();
        gbc_btnSendUnstructuredrequest.insets = new Insets(0, 0, 5, 0);
        gbc_btnSendUnstructuredrequest.gridx = 1;
        gbc_btnSendUnstructuredrequest.gridy = 2;
        panel.add(btnSendUnstructuredrequest, gbc_btnSendUnstructuredrequest);

        btnSendUnstructurednotify = new JButton("Send UnstructuredNotify");
        btnSendUnstructurednotify.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendUnstructuredNotify();
            }
        });
        GridBagConstraints gbc_btnSendUnstructurednotify = new GridBagConstraints();
        gbc_btnSendUnstructurednotify.insets = new Insets(0, 0, 5, 0);
        gbc_btnSendUnstructurednotify.gridx = 1;
        gbc_btnSendUnstructurednotify.gridy = 3;
        panel.add(btnSendUnstructurednotify, gbc_btnSendUnstructurednotify);

        btnCloseCurrentDialog = new JButton("Close current Dialog");
        btnCloseCurrentDialog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                closeCurrentDialog();
            }
        });
        GridBagConstraints gbc_btnCloseCurrentDialog = new GridBagConstraints();
        gbc_btnCloseCurrentDialog.insets = new Insets(0, 0, 5, 0);
        gbc_btnCloseCurrentDialog.gridx = 1;
        gbc_btnCloseCurrentDialog.gridy = 4;
        panel.add(btnCloseCurrentDialog, gbc_btnCloseCurrentDialog);

        JLabel label_1 = new JLabel("Operation result");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 5;
        panel.add(label_1, gbc_label_1);

        lbResult = new JLabel("-");
        GridBagConstraints gbc_lbResult = new GridBagConstraints();
        gbc_lbResult.insets = new Insets(0, 0, 5, 0);
        gbc_lbResult.gridx = 1;
        gbc_lbResult.gridy = 5;
        panel.add(lbResult, gbc_lbResult);

        JLabel label_2 = new JLabel("Message received");
        GridBagConstraints gbc_label_2 = new GridBagConstraints();
        gbc_label_2.insets = new Insets(0, 0, 5, 5);
        gbc_label_2.gridx = 0;
        gbc_label_2.gridy = 6;
        panel.add(label_2, gbc_label_2);

        lbMessage = new JLabel("-");
        GridBagConstraints gbc_lbMessage = new GridBagConstraints();
        gbc_lbMessage.insets = new Insets(0, 0, 5, 0);
        gbc_lbMessage.gridx = 1;
        gbc_lbMessage.gridy = 6;
        panel.add(lbMessage, gbc_lbMessage);

        lbState = new JLabel("-");
        GridBagConstraints gbc_lbState = new GridBagConstraints();
        gbc_lbState.gridx = 1;
        gbc_lbState.gridy = 7;
        panel.add(lbState, gbc_lbState);
    }

    public void setData(TestUssdServerManMBean ussdServer) {
        this.ussdServer = ussdServer;

        if (ussdServer.getProcessSsRequestAction().intValue() != ProcessSsRequestAction.VAL_MANUAL_RESPONSE) {
            tbMessage.setEnabled(false);
            btnSendProcessunstructuredresponse.setEnabled(false);
            btnSendUnstructuredrequest.setEnabled(false);
            btnSendUnstructurednotify.setEnabled(false);
            btnCloseCurrentDialog.setEnabled(false);
        }
    }

    private void sendProcessUnstructuredResponse() {
        this.lbMessage.setText("");
        String msg = this.tbMessage.getText();
        String res = this.ussdServer.performProcessUnstructuredResponse(msg);
        this.lbResult.setText(res);
    }

    private void sendUnstructuredRequest() {
        this.lbMessage.setText("");
        String msg = this.tbMessage.getText();
        String res = this.ussdServer.performUnstructuredRequest(msg);
        this.lbResult.setText(res);
    }

    private void sendUnstructuredNotify() {
        this.lbMessage.setText("");
        String msg = this.tbMessage.getText();
        String res = this.ussdServer.performUnstructuredNotify(msg);
        this.lbResult.setText(res);
    }

    private void closeCurrentDialog() {
        this.lbMessage.setText("");
        String res = this.ussdServer.closeCurrentDialog();
        this.lbResult.setText(res);
    }

    @Override
    public void sendNotif(Notification notif) {
        super.sendNotif(notif);

        if (notif.getMessage().startsWith("CurDialog: Rcvd: procUnstrSsReq: ")) {
            String s1 = notif.getMessage().substring(17);
            this.lbMessage.setText(s1);
        }

        if (notif.getMessage().startsWith("CurDialog: Rcvd: unstrSsResp: ")) {
            String s1 = notif.getMessage().substring(17);
            this.lbMessage.setText(s1);
        }
    }

    @Override
    public void refreshState() {
        super.refreshState();

        String s1 = this.ussdServer.getCurrentRequestDef();
        this.lbState.setText(s1);
    }
}
