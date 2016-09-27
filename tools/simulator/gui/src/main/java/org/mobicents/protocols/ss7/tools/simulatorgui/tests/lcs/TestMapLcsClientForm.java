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
import javax.swing.JOptionPane;

//import org.mobicents.protocols.ss7.tools.simulator.tests.checkimei.CheckImeiClientAction;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;

/**
*
* @author falonso@csc.com
*
*/
public class TestMapLcsClientForm extends TestingForm {

    private static final long serialVersionUID = 6864080004816461791L;

    private TestMapLcsClientManMBean mapLcsClient;


    private JLabel lbMessage;
    private JLabel lbResult;
    private JLabel lbState;
    private JTextField tbAddress;


    public TestMapLcsClientForm(JFrame owner) {
    super(owner);

        JPanel panel = new JPanel();
        panel_c.add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JLabel lblImsiMsisdn = new JLabel("  IMSI / MSISDN digits");
        GridBagConstraints gbc_lblImsiMsisdn = new GridBagConstraints();
        gbc_lblImsiMsisdn.anchor = GridBagConstraints.EAST;
        gbc_lblImsiMsisdn.insets = new Insets(0, 0, 5, 5);
        gbc_lblImsiMsisdn.gridx = 0;
        gbc_lblImsiMsisdn.gridy = 0;
        panel.add(lblImsiMsisdn, gbc_lblImsiMsisdn);

        tbAddress = new JTextField();
        tbAddress.setColumns(10);
        GridBagConstraints gbc_tbAddress = new GridBagConstraints();
        gbc_tbAddress.fill = GridBagConstraints.HORIZONTAL;
        gbc_tbAddress.insets = new Insets(0, 0, 5, 0);
        gbc_tbAddress.gridx = 1;
        gbc_tbAddress.gridy = 0;
        panel.add(tbAddress, gbc_tbAddress);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 3;
        panel.add(panel_1, gbc_panel_1);

        JButton btnSendRoutingInfoForLCSRequest = new JButton("SendRoutingInfoForLCSRequest");
        btnSendRoutingInfoForLCSRequest.setBounds(0, 11, 250, 23);
        panel_1.add(btnSendRoutingInfoForLCSRequest);

        btnSendRoutingInfoForLCSRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRoutingInfoForLCSRequest();
            }
        });
        /*
        // SLR Request
        JButton btnSubscriberLocationReportRequest = new JButton("SubscriberLocationReportRequest");
        btnSubscriberLocationReportRequest.setBounds(192, 10, 202, 25);
        panel_1.add(btnSubscriberLocationReportRequest);

        btnSubscriberLocationReportRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                subscriberLocationReportRequest();
            }
        });
        */
        // PSL Request
        JButton btnProvideSubscriberLocationRequest = new JButton("ProvideSubscriberLocationRequest");
        btnProvideSubscriberLocationRequest.setBounds(300, 10, 250, 25);
        panel_1.add(btnProvideSubscriberLocationRequest);

        btnProvideSubscriberLocationRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                provideSubscriberLocationRequest();
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

    public void setData(TestMapLcsClientManMBean mapLcsClient) {
        this.mapLcsClient = mapLcsClient;

        /*tbImei.setText(mapLcsClient.getImei());

        if (mapLcsClient.getmapLcsClientAction().intValue() != mapLcsClientAction.VAL_MANUAL_OPERATION) {
            btnSendCheckImei.setEnabled(false);
            tbImei.setEnabled(false);
            btCloseCurrentDialog.setEnabled(false);
        } else {
            btnSendCheckImei.setEnabled(true);
            tbImei.setEnabled(true);
            btCloseCurrentDialog.setEnabled(true);
        }*/
    }

    private void sendRoutingInfoForLCSRequest() {

        String address = this.tbAddress.getText();
        if (address.length()<5) {
            JOptionPane.showMessageDialog(this, "IMSI/MSISDN must be at least 5 digits");
            return;
        }
        this.lbMessage.setText("");
        String res = this.mapLcsClient.performSendRoutingInfoForLCSRequest(address);
        this.lbResult.setText(res);
    }
    private void subscriberLocationReportRequest() {
        this.lbMessage.setText("");
        String res = this.mapLcsClient.performSubscriberLocationReportRequest();
        this.lbResult.setText(res);
    }
    private void provideSubscriberLocationRequest() {
        this.lbMessage.setText("");
        String res = this.mapLcsClient.performProvideSubscriberLocationRequest();
        this.lbResult.setText(res);
    }

    private void closeCurrentDialog() {
        this.lbMessage.setText("");
        /* String res = this.mapLcsClient.closeCurrentDialog();
        this.lbResult.setText(res); */
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

        String s1 = this.mapLcsClient.getCurrentRequestDef();
        this.lbState.setText(s1);
    }
}
