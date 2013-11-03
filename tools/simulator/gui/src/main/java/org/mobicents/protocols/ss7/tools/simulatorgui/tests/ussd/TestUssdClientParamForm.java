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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.UssdClientAction;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdClientParamForm extends JDialog {

    private static final long serialVersionUID = -2576986645852329809L;

    private TestUssdClientManMBean ussdClient;

    private JTextField tbMsisdnAddress;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JTextField tbDataCodingScheme;
    private JTextField tbAlertingPattern;
    private JComboBox cbUssdClientAction;
    private JTextField tbAutoRequestString;
    private JTextField tbMaxConcurrentDialogs;
    private JCheckBox cbOneNotificationFor100Dialogs;

    public TestUssdClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("USSD test client settings");
        setBounds(100, 100, 537, 511);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 11, 511, 131);
        panel.add(panel_1);
        panel_1.setLayout(null);

        JLabel lblMsiswdn = new JLabel("Msisdn value");
        lblMsiswdn.setBounds(10, 0, 79, 14);
        panel_1.add(lblMsiswdn);

        JLabel lblString = new JLabel("String");
        lblString.setBounds(10, 22, 46, 14);
        panel_1.add(lblString);

        JLabel lblNewLabel = new JLabel("AddressNature");
        lblNewLabel.setBounds(10, 50, 136, 14);
        panel_1.add(lblNewLabel);

        JLabel lblNumberingplan = new JLabel("NumberingPlan");
        lblNumberingplan.setBounds(10, 81, 136, 14);
        panel_1.add(lblNumberingplan);

        tbMsisdnAddress = new JTextField();
        tbMsisdnAddress.setBounds(99, 19, 245, 20);
        panel_1.add(tbMsisdnAddress);
        tbMsisdnAddress.setColumns(10);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(156, 47, 294, 20);
        panel_1.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(156, 78, 294, 20);
        panel_1.add(cbNumberingPlan);

        JLabel lblDataCodingScheme = new JLabel("Data coding scheme (15-GSM7, 72-UCS2)");
        lblDataCodingScheme.setBounds(10, 153, 299, 14);
        panel.add(lblDataCodingScheme);

        JLabel lblNewLabel_1 = new JLabel("Alerting pattern value (-1 means does not use AlertingPattern)");
        lblNewLabel_1.setBounds(10, 184, 384, 14);
        panel.add(lblNewLabel_1);

        tbDataCodingScheme = new JTextField();
        tbDataCodingScheme.setBounds(423, 150, 86, 20);
        panel.add(tbDataCodingScheme);
        tbDataCodingScheme.setColumns(10);

        tbAlertingPattern = new JTextField();
        tbAlertingPattern.setColumns(10);
        tbAlertingPattern.setBounds(423, 181, 86, 20);
        panel.add(tbAlertingPattern);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 415, 256, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(276, 415, 245, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Reload");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.setBounds(10, 449, 144, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(277, 449, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_4.setBounds(404, 449, 117, 23);
        panel.add(button_4);

        JLabel lblUssdClientMode = new JLabel("Ussd client mode");
        lblUssdClientMode.setBounds(10, 209, 149, 14);
        panel.add(lblUssdClientMode);

        cbUssdClientAction = new JComboBox();
        cbUssdClientAction
                .setToolTipText("<html>\r\nThe mode of UssdClient work. When manual response user can manually send ProcessSsUnstructured request, \r\n<br>\r\nwhen VAL_AUTO_SendProcessUnstructuredSSRequest the tester sends ProcessSsUnstructured requests without dealay (load test)\r\n</html>");
        cbUssdClientAction.setBounds(10, 234, 499, 20);
        panel.add(cbUssdClientAction);

        JLabel lblStringOfAuto = new JLabel("String of auto processUnsructuresSs request");
        lblStringOfAuto.setBounds(10, 265, 324, 14);
        panel.add(lblStringOfAuto);

        tbAutoRequestString = new JTextField();
        tbAutoRequestString.setColumns(10);
        tbAutoRequestString.setBounds(10, 287, 511, 20);
        panel.add(tbAutoRequestString);

        JLabel lblTheCountOf = new JLabel("The count of maximum active MAP dialogs when the auto sending mode");
        lblTheCountOf.setBounds(10, 321, 511, 14);
        panel.add(lblTheCountOf);

        tbMaxConcurrentDialogs = new JTextField();
        tbMaxConcurrentDialogs.setColumns(10);
        tbMaxConcurrentDialogs.setBounds(10, 344, 98, 20);
        panel.add(tbMaxConcurrentDialogs);

        cbOneNotificationFor100Dialogs = new JCheckBox(
                "One notification for 100 dialogs (recommended for the auto sending mode)");
        cbOneNotificationFor100Dialogs.setBounds(10, 371, 511, 23);
        panel.add(cbOneNotificationFor100Dialogs);
    }

    public void setData(TestUssdClientManMBean ussdClient) {
        this.ussdClient = ussdClient;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.ussdClient.getMsisdnAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.ussdClient.getMsisdnNumberingPlan());
        M3uaForm.setEnumeratedBaseComboBox(cbUssdClientAction, this.ussdClient.getUssdClientAction());

        tbMsisdnAddress.setText(this.ussdClient.getMsisdnAddress());
        tbAutoRequestString.setText(this.ussdClient.getAutoRequestString());

        tbDataCodingScheme.setText(((Integer) this.ussdClient.getDataCodingScheme()).toString());
        tbAlertingPattern.setText(((Integer) this.ussdClient.getAlertingPattern()).toString());
        tbMaxConcurrentDialogs.setText(((Integer) this.ussdClient.getMaxConcurrentDialogs()).toString());

        cbOneNotificationFor100Dialogs.setSelected(this.ussdClient.isOneNotificationFor100Dialogs());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbUssdClientAction, new UssdClientAction(UssdClientAction.VAL_MANUAL_OPERATION));

        tbMsisdnAddress.setText("");
        tbAutoRequestString.setText("");
        tbMaxConcurrentDialogs.setText("10");

        tbDataCodingScheme.setText("15");
        tbAlertingPattern.setText("-1");

        cbOneNotificationFor100Dialogs.setSelected(false);
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        int dataCodingScheme = 0;
        int alertingPattern = 0;
        int maxConcurrentDialogs = 10;
        try {
            dataCodingScheme = Integer.parseInt(tbDataCodingScheme.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing DataCodingScheme value: " + e.toString());
            return false;
        }
        try {
            alertingPattern = Integer.parseInt(tbAlertingPattern.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Alerting Pattern value: " + e.toString());
            return false;
        }
        try {
            maxConcurrentDialogs = Integer.parseInt(tbMaxConcurrentDialogs.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing MaxConcurrentDialogs value: " + e.toString());
            return false;
        }

        this.ussdClient.setMsisdnAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.ussdClient.setMsisdnNumberingPlan((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        this.ussdClient.setMsisdnAddress(tbMsisdnAddress.getText());
        this.ussdClient.setUssdClientAction((UssdClientAction) cbUssdClientAction.getSelectedItem());
        this.ussdClient.setAutoRequestString(tbAutoRequestString.getText());

        this.ussdClient.setDataCodingScheme(dataCodingScheme);
        this.ussdClient.setAlertingPattern(alertingPattern);
        this.ussdClient.setMaxConcurrentDialogs(maxConcurrentDialogs);

        this.ussdClient.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());

        return true;
    }
}
