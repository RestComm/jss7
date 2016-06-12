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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.map;

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

//import org.mobicents.protocols.ss7.tools.simulator.tests.checkimei.CheckImeiClientAction;
import org.mobicents.protocols.ss7.tools.simulator.tests.map.TestMapLcsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;

/**
*
* @author falonso@csc.com
*
*/
public class TestMapLcsClientForm extends TestingForm {

    private static final long serialVersionUID = 6864080004816461791L;

    private TestMapLcsClientManMBean mapLcsClient;

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

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 3;
        panel.add(panel_1, gbc_panel_1);

        JButton btnSendRoutingInfoForLCSRequest = new JButton("SendRoutingInfoForLCSRequest");
        btnSendRoutingInfoForLCSRequest.setBounds(12, 0, 249, 25);
        panel_1.add(btnSendRoutingInfoForLCSRequest);

        btnSendRoutingInfoForLCSRequest.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRoutingInfoForLCSRequest();
            }
        });

        JPanel panel_2 = new JPanel();
        GridBagConstraints gbc_panel_2 = new GridBagConstraints();
        gbc_panel_2.anchor = GridBagConstraints.NORTH;
        gbc_panel_2.insets = new Insets(0, 0, 5, 0);
        gbc_panel_2.fill = GridBagConstraints.HORIZONTAL;
        gbc_panel_2.gridx = 1;
        gbc_panel_2.gridy = 4;
        panel.add(panel_2, gbc_panel_2);

        JLabel lblRefnum = new JLabel("refNum");
        panel_2.add(lblRefnum);

        tbRefNum = new JTextField();
        panel_2.add(tbRefNum);
        tbRefNum.setColumns(10);

        JLabel lblSegmcnt = new JLabel("segmCnt");
        panel_2.add(lblSegmcnt);

        tbSegmCnt = new JTextField();
        panel_2.add(tbSegmCnt);
        tbSegmCnt.setColumns(10);

        JLabel lblSegmnum = new JLabel("segmNum");
        panel_2.add(lblSegmnum);

        tbSegmNum = new JTextField();
        panel_2.add(tbSegmNum);
        tbSegmNum.setColumns(10);

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
        this.lbMessage.setText("");
        String res = this.mapLcsClient.sendRoutingInfoForLCSRequest();
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

        //String s1 = this.mapLcsClient.getCurrentRequestDef();
        this.lbState.setText("No op Refreshed");
    }
}
