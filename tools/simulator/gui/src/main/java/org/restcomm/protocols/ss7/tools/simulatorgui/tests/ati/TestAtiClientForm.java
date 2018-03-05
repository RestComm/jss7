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

package org.restcomm.protocols.ss7.tools.simulatorgui.tests.ati;

import javax.management.Notification;
import javax.swing.JFrame;

import org.restcomm.protocols.ss7.tools.simulator.tests.ati.TestAtiClientManMBean;
import org.restcomm.protocols.ss7.tools.simulatorgui.TestingForm;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
*
* @author sergey vetyutnev
*
*/
public class TestAtiClientForm extends TestingForm {

    private static final long serialVersionUID = -2286948400971450286L;

    private TestAtiClientManMBean atiClient;

    private JTextField tbAddress;
    private JLabel lbMessage;
    private JLabel lbResult;
    private JLabel lbState;

    public TestAtiClientForm(JFrame owner) {
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
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.gridx = 1;
        gbc_panel_1.gridy = 3;
        panel.add(panel_1, gbc_panel_1);

        JButton btSend = new JButton("Send ATI request");
        btSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendAti();
            }
        });
        btSend.setBounds(12, 0, 214, 25);
        panel_1.add(btSend);

        JLabel label_6 = new JLabel("Operation result");
        GridBagConstraints gbc_label_6 = new GridBagConstraints();
        gbc_label_6.insets = new Insets(0, 0, 5, 5);
        gbc_label_6.gridx = 0;
        gbc_label_6.gridy = 5;
        panel.add(label_6, gbc_label_6);

        lbResult = new JLabel("-");
        GridBagConstraints gbc_lbResult = new GridBagConstraints();
        gbc_lbResult.insets = new Insets(0, 0, 5, 0);
        gbc_lbResult.gridx = 1;
        gbc_lbResult.gridy = 5;
        panel.add(lbResult, gbc_lbResult);

        JLabel label_8 = new JLabel("Message received");
        GridBagConstraints gbc_label_8 = new GridBagConstraints();
        gbc_label_8.insets = new Insets(0, 0, 5, 5);
        gbc_label_8.gridx = 0;
        gbc_label_8.gridy = 6;
        panel.add(label_8, gbc_label_8);

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

    public void setData(TestAtiClientManMBean atiClient) {
        this.atiClient = atiClient;

//        this.tbRefNum.setText("1");
//        this.tbSegmCnt.setText("0");
//        this.tbSegmNum.setText("0");
    }

    private void sendAti() {
        this.lbMessage.setText("");
        String addr = this.tbAddress.getText();

//        int refNum = 0;
//        int segmCnt = 0;
//        int segmNum = 0;
//        try {
//            refNum = Integer.parseInt(this.tbRefNum.getText());
//            segmCnt = Integer.parseInt(this.tbSegmCnt.getText());
//            segmNum = Integer.parseInt(this.tbSegmNum.getText());
//        } catch (Exception e) {
//            this.lbResult.setText("Error when parsing RefNum, SegmCnt or SegmNum: " + e.getMessage());
//            return;
//        }

        String res = this.atiClient.performAtiRequest(addr);

        this.lbResult.setText(res);
    }

    @Override
    public void sendNotif(Notification notif) {
        super.sendNotif(notif);

//        if (notif.getMessage().startsWith("Rcvd: mtReq: ")) {
//            String s1 = notif.getMessage().substring(13);
//            this.lbMessage.setText(s1);
//        }
    }

    @Override
    public void refreshState() {
        super.refreshState();

        String s1 = this.atiClient.getCurrentRequestDef();
        this.lbState.setText(s1);
    }
}
