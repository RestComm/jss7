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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.management.Notification;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapSsfManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.TestingForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapSsfForm extends TestingForm {

    private static final long serialVersionUID = 841129095300363570L;

    private TestCapSsfManMBean capSsf;
    private JButton btCloseDialog;
    private JButton btInitialDp;
    private JButton btAssistRequestInstructions;
    private JLabel lbState;
    private JButton btApplyChargingReport;
    private JButton btEventReportBCSM;
    private JPanel panel_1;
    private JLabel lbResult;

    public TestCapSsfForm(JFrame owner) {
        super(owner);

        JPanel panel = new JPanel();
        panel_c.add(panel, BorderLayout.NORTH);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        btCloseDialog = new JButton("Close Dialog");
        btCloseDialog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                closeDialog();
            }
        });
        GridBagConstraints gbc_btCloseDialog = new GridBagConstraints();
        gbc_btCloseDialog.insets = new Insets(0, 0, 5, 5);
        gbc_btCloseDialog.gridx = 1;
        gbc_btCloseDialog.gridy = 0;
        panel.add(btCloseDialog, gbc_btCloseDialog);

        btInitialDp = new JButton("InitialDp");
        btInitialDp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendInitialDP();
            }
        });
        GridBagConstraints gbc_btInitialDp = new GridBagConstraints();
        gbc_btInitialDp.insets = new Insets(0, 0, 5, 5);
        gbc_btInitialDp.gridx = 2;
        gbc_btInitialDp.gridy = 0;
        panel.add(btInitialDp, gbc_btInitialDp);

        btAssistRequestInstructions = new JButton("AssistRequestInstructions");
        btAssistRequestInstructions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                sendAssistRequestInstructions();
            }
        });
        GridBagConstraints gbc_btAssistRequestInstructions = new GridBagConstraints();
        gbc_btAssistRequestInstructions.insets = new Insets(0, 0, 5, 0);
        gbc_btAssistRequestInstructions.gridx = 3;
        gbc_btAssistRequestInstructions.gridy = 0;
        panel.add(btAssistRequestInstructions, gbc_btAssistRequestInstructions);

        btApplyChargingReport = new JButton("ApplyChargingReport");
        btApplyChargingReport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendApplyChargingReport();
            }
        });
        GridBagConstraints gbc_btApplyChargingReport = new GridBagConstraints();
        gbc_btApplyChargingReport.insets = new Insets(0, 0, 5, 5);
        gbc_btApplyChargingReport.gridx = 1;
        gbc_btApplyChargingReport.gridy = 1;
        panel.add(btApplyChargingReport, gbc_btApplyChargingReport);

        btEventReportBCSM = new JButton("EventReportBCSM");
        btEventReportBCSM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendEventReportBCSM();
            }
        });
        GridBagConstraints gbc_btEventReportBCSM = new GridBagConstraints();
        gbc_btEventReportBCSM.insets = new Insets(0, 0, 5, 5);
        gbc_btEventReportBCSM.gridx = 2;
        gbc_btEventReportBCSM.gridy = 1;
        panel.add(btEventReportBCSM, gbc_btEventReportBCSM);

        panel_1 = new JPanel();
        panel_c.add(panel_1, BorderLayout.SOUTH);
        panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.Y_AXIS));

        lbResult = new JLabel("-");
        lbResult.setHorizontalAlignment(SwingConstants.LEFT);
        panel_1.add(lbResult);

        lbState = new JLabel("-");
        panel_1.add(lbState);

        this.setDialogClosed();
    }

    public void setData(TestCapSsfManMBean capSsf) {
        this.capSsf = capSsf;
    }

    private void closeDialog() {
        String res = this.capSsf.closeCurrentDialog();
        this.lbResult.setText(res);
        setDialogClosed();
    }

    private void sendInitialDP() {
        String msg = "";
        String res = this.capSsf.performInitialDp(msg);
        this.lbResult.setText(res);
    }

    private void sendAssistRequestInstructions() {
        String msg = "";
        String res = this.capSsf.performAssistRequestInstructions(msg);
        this.lbResult.setText(res);
    }

    private void sendApplyChargingReport() {
        String msg = "";
        String res = this.capSsf.performApplyChargingReport(msg);
        this.lbResult.setText(res);
    }

    private void sendEventReportBCSM() {
        String msg = "";
        String res = this.capSsf.performEventReportBCSM(msg);
        this.lbResult.setText(res);
    }

    private void setDialogOpenedClosed(boolean opened) {
        this.btInitialDp.setEnabled(!opened);
        this.btAssistRequestInstructions.setEnabled(!opened);

        this.btApplyChargingReport.setEnabled(opened);
        this.btEventReportBCSM.setEnabled(opened);
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

        String s1 = this.capSsf.getCurrentRequestDef();
        this.lbState.setText(s1);
    }
}
