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

package org.restcomm.protocols.ss7.tools.simulatorgui.tests.checkimei;

import javax.management.Notification;
import javax.swing.JFrame;

import org.restcomm.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiServerManMBean;
import org.restcomm.protocols.ss7.tools.simulatorgui.TestingForm;

import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
*
* @author mnowa
*
*/
public class TestCheckImeiServerForm extends TestingForm {

    private static final long serialVersionUID = -7570287405831015297L;

    private TestCheckImeiServerManMBean checkImeiServer;

    private JLabel lbState;
    private JLabel lbMessage;
    private JLabel lbResult;

    public TestCheckImeiServerForm(JFrame owner) {
        super(owner);

        JPanel panel = new JPanel();
        panel_c.add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] {  0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JLabel label_1 = new JLabel("Operation result");
        GridBagConstraints gbc_label_1 = new GridBagConstraints();
        gbc_label_1.anchor = GridBagConstraints.EAST;
        gbc_label_1.insets = new Insets(0, 0, 5, 5);
        gbc_label_1.gridx = 0;
        gbc_label_1.gridy = 1;
        panel.add(label_1, gbc_label_1);

        lbResult = new JLabel("-");
        GridBagConstraints gbc_lbResult = new GridBagConstraints();
        gbc_lbResult.insets = new Insets(0, 0, 5, 0);
        gbc_lbResult.gridx = 1;
        gbc_lbResult.gridy = 1;
        panel.add(lbResult, gbc_lbResult);

        JLabel label_3 = new JLabel("Message received");
        GridBagConstraints gbc_label_3 = new GridBagConstraints();
        gbc_label_3.anchor = GridBagConstraints.EAST;
        gbc_label_3.insets = new Insets(0, 0, 5, 5);
        gbc_label_3.gridx = 0;
        gbc_label_3.gridy = 2;
        panel.add(label_3, gbc_label_3);

        lbMessage = new JLabel("-");
        GridBagConstraints gbc_lbMessage = new GridBagConstraints();
        gbc_lbMessage.insets = new Insets(0, 0, 5, 0);
        gbc_lbMessage.gridx = 1;
        gbc_lbMessage.gridy = 2;
        panel.add(lbMessage, gbc_lbMessage);

        lbState = new JLabel("-");
        GridBagConstraints gbc_lbState = new GridBagConstraints();
        gbc_lbState.gridx = 1;
        gbc_lbState.gridy = 3;
        panel.add(lbState, gbc_lbState);
    }

    public void setData(TestCheckImeiServerManMBean checkImeiServer) {
        this.checkImeiServer = checkImeiServer;
    }

    @Override
    public void sendNotif(Notification notif) {
        super.sendNotif(notif);

        String msg = notif.getMessage();

        final String requestPrefix = "Rcvd: CheckImeiReq: ";
        final String responsePrefix = "Sent CheckImeiResponse: ";
        if (msg != null) {
            if (msg.startsWith(requestPrefix)) {
                String s1 = msg.substring(requestPrefix.length());
                this.lbMessage.setText(s1);
                return;
            } else if (msg.startsWith(responsePrefix)) {
                String s1 = msg.substring(responsePrefix.length());
                this.lbResult.setText(s1);
            }
        }
    }

    @Override
    public void refreshState() {
        super.refreshState();

        String s1 = this.checkImeiServer.getCurrentRequestDef();
        this.lbState.setText(s1);
    }

}
