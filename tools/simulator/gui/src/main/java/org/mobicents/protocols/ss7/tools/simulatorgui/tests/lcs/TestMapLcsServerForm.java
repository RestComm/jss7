/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.lcs;

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

import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;

/**
*
* @author falonso@csc.com
*
*/
public class TestMapLcsServerForm extends TestingForm {

    private static final long serialVersionUID = 6864080004816461791L;

    private TestMapLcsServerManMBean mapLcsServer;

    private JButton btCloseCurrentDialog;
    private JButton btnSendCheckImei;
    private JLabel lbMessage;
    private JLabel lbResult;
    private JLabel lbState;
    private JTextField tbDestIsdnNumber;
    private JTextField tbImei;
    private JTextField tbMessage;
    private JTextField tbOrigIsdnNumber;
    private JTextField tbRefNum;
    private JTextField tbSegmCnt;
    private JTextField tbSegmNum;

    public TestMapLcsServerForm(JFrame owner) {
    super(owner);

        JPanel panel = new JPanel();
        panel_c.add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 3;
        panel.add(panel_1, gbc_panel_1);

        /*
        JButton btnSendRoutingInfoForLCSResponse = new JButton("SendRoutingInfoForLCSResponse");
        btnSendRoutingInfoForLCSResponse.setBounds(0, 0, 249, 25);
        panel_1.add(btnSendRoutingInfoForLCSResponse);

        btnSendRoutingInfoForLCSResponse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRoutingInfoForLCSResponse();
            }
        });

        JButton btnSubscriberLocationReportResponse = new JButton("SubscriberLocationReportResponse");
        btnSubscriberLocationReportResponse.setBounds(259, 0, 249, 25);
        panel_1.add(btnSubscriberLocationReportResponse);

        btnSubscriberLocationReportResponse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subscriberLocationReportResponse();
            }
        });
        */
        JButton btnSubscriberLocationReportRequest = new JButton("SubscriberLocationReportRequest");
        btnSubscriberLocationReportRequest.setBounds(0, 30, 249, 25);
        panel_1.add(btnSubscriberLocationReportRequest);

        btnSubscriberLocationReportRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subscriberLocationReportRequest();
            }
        });

        JLabel label_3 = new JLabel("Operation result");
        GridBagConstraints gbc_label_3 = new GridBagConstraints();
        gbc_label_3.insets = new Insets(0, 0, 5, 5);
        gbc_label_3.gridx = 0;
        gbc_label_3.gridy = 5;
        panel.add(label_3, gbc_label_3);

        lbResult = new JLabel("-");
        GridBagConstraints gbc_lbResult = new GridBagConstraints();
        gbc_lbResult.insets = new Insets(0, 0, 5, 0);
        gbc_lbResult.gridx = 1;
        gbc_lbResult.gridy = 5;
        panel.add(lbResult, gbc_lbResult);

        JLabel label_4 = new JLabel("Message received");
        GridBagConstraints gbc_label_4 = new GridBagConstraints();
        gbc_label_4.insets = new Insets(0, 0, 5, 5);
        gbc_label_4.gridx = 0;
        gbc_label_4.gridy = 6;
        panel.add(label_4, gbc_label_4);

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

    private void subscriberLocationReportRequest() {
        this.lbMessage.setText("");
        String res = this.mapLcsServer.performSubscriberLocationReportRequest();
        this.lbResult.setText(res);
    }

    private void subscriberLocationReportResponse() {
        this.lbMessage.setText("");
        String res = this.mapLcsServer.subscriberLocationReportResponse();
        this.lbResult.setText(res);
    }

    public void setData(TestMapLcsServerManMBean mapLcsServer) {
        this.mapLcsServer = mapLcsServer;

    }

    protected void sendRoutingInfoForLCSResponse() {
        this.lbMessage.setText("");
        String res = this.mapLcsServer.sendRoutingInfoForLCSResponse();
        this.lbResult.setText(res);
    }

    private void closeCurrentDialog() {
        this.lbMessage.setText("");
    }

    @Override
    public void sendNotif(Notification notif) {
        super.sendNotif(notif);

        String msg = notif.getMessage();
        final String[] prefixes = new String [] { "Rcvd: CheckImeiResp: ", "Sent: CheckImeiRequest: "};
        if (msg != null) {
            for (String prefix: prefixes) {
                if (msg.startsWith(prefix)) {
                    String s1 = msg.substring(prefix.length());
                    this.lbMessage.setText(s1);
                    return;
                }
            }
        }
    }

    @Override
    public void refreshState() {
        super.refreshState();

        super.refreshState();

        String s1 = this.mapLcsServer.getCurrentRequestDef();
        this.lbState.setText(s1);
    }
}
