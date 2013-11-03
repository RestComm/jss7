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
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.ProcessSsRequestAction;
import org.mobicents.protocols.ss7.tools.simulator.tests.ussd.TestUssdServerManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestUssdServerParamForm extends JDialog {

    private static final long serialVersionUID = 1589538832800021188L;

    private TestUssdServerManMBean ussdServer;

    private JTextField tbMsisdnAddress;
    private JTextField tbDataCodingScheme;
    private JTextField tbAlertingPattern;
    private JTextField tbAutoResponseString;
    private JTextField tbAutoUnstructured_SS_RequestString;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JComboBox cbProcessSsRequestAction;
    private JCheckBox cbOneNotificationFor100Dialogs;

    public TestUssdServerParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("USSD test server settings");
        setBounds(100, 100, 539, 508);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setLayout(null);
        panel_1.setBounds(10, 11, 511, 121);
        panel.add(panel_1);

        JLabel label = new JLabel("Msisdn value");
        label.setBounds(10, 0, 79, 14);
        panel_1.add(label);

        JLabel label_1 = new JLabel("String");
        label_1.setBounds(10, 22, 46, 14);
        panel_1.add(label_1);

        JLabel label_2 = new JLabel("AddressNature");
        label_2.setBounds(10, 53, 103, 14);
        panel_1.add(label_2);

        JLabel label_3 = new JLabel("NumberingPlan");
        label_3.setBounds(10, 84, 103, 14);
        panel_1.add(label_3);

        tbMsisdnAddress = new JTextField();
        tbMsisdnAddress.setColumns(10);
        tbMsisdnAddress.setBounds(123, 19, 378, 20);
        panel_1.add(tbMsisdnAddress);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(123, 50, 266, 20);
        panel_1.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(123, 81, 266, 20);
        panel_1.add(cbNumberingPlan);

        JLabel lblDataCodingScheme = new JLabel("Data coding scheme (15-GSM7, 72-UCS2)");
        lblDataCodingScheme.setBounds(10, 143, 298, 14);
        panel.add(lblDataCodingScheme);

        tbDataCodingScheme = new JTextField();
        tbDataCodingScheme.setColumns(10);
        tbDataCodingScheme.setBounds(423, 140, 86, 20);
        panel.add(tbDataCodingScheme);

        tbAlertingPattern = new JTextField();
        tbAlertingPattern.setColumns(10);
        tbAlertingPattern.setBounds(423, 171, 86, 20);
        panel.add(tbAlertingPattern);

        JLabel label_5 = new JLabel("Alerting pattern value (-1 means does not use AlertingPattern)");
        label_5.setBounds(10, 174, 403, 14);
        panel.add(label_5);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 412, 246, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, 412, 255, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Cancel");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_2.setBounds(404, 446, 117, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, 446, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Reload");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_4.setBounds(10, 446, 144, 23);
        panel.add(button_4);

        JLabel lblProcessssrequestaction = new JLabel("The action when processing ProcessSsRequest");
        lblProcessssrequestaction.setBounds(10, 200, 499, 23);
        panel.add(lblProcessssrequestaction);

        cbProcessSsRequestAction = new JComboBox();
        cbProcessSsRequestAction
                .setToolTipText("<html>\r\nAction which be performed when ProcessSsUnstructuredRequest has been received. When manual response user must manually send a response or SsUnstructuredRequest to the UssdClient. \r\n<br>\r\nOther actions are: auto sending \"AutoResponseString\" as a response, \r\n<br>\r\nauto sending \"AutoUnstructured_SS_RequestString\" as a SsUnstructuredRequest and then auto sending \"AutoResponseString\" as a response to SsUnstructured response\r\n</html>");
        cbProcessSsRequestAction.setBounds(10, 234, 511, 20);
        panel.add(cbProcessSsRequestAction);

        tbAutoResponseString = new JTextField();
        tbAutoResponseString.setColumns(10);
        tbAutoResponseString.setBounds(10, 287, 511, 20);
        panel.add(tbAutoResponseString);

        JLabel lblAutuString = new JLabel("String of auto processUnsructuresSsResponse");
        lblAutuString.setBounds(10, 265, 324, 14);
        panel.add(lblAutuString);

        JLabel lblStringOfAuto = new JLabel("String of auto unsructuresSsRequest");
        lblStringOfAuto.setBounds(10, 318, 339, 14);
        panel.add(lblStringOfAuto);

        tbAutoUnstructured_SS_RequestString = new JTextField();
        tbAutoUnstructured_SS_RequestString.setColumns(10);
        tbAutoUnstructured_SS_RequestString.setBounds(10, 343, 511, 20);
        panel.add(tbAutoUnstructured_SS_RequestString);

        cbOneNotificationFor100Dialogs = new JCheckBox(
                "One notification for 100 dialogs (recommended for the auto sending mode)");
        cbOneNotificationFor100Dialogs.setBounds(10, 370, 511, 23);
        panel.add(cbOneNotificationFor100Dialogs);
    }

    public void setData(TestUssdServerManMBean ussdServer) {
        this.ussdServer = ussdServer;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.ussdServer.getMsisdnAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.ussdServer.getMsisdnNumberingPlan());
        M3uaForm.setEnumeratedBaseComboBox(cbProcessSsRequestAction, this.ussdServer.getProcessSsRequestAction());

        tbMsisdnAddress.setText(this.ussdServer.getMsisdnAddress());
        tbAutoResponseString.setText(this.ussdServer.getAutoResponseString());
        tbAutoUnstructured_SS_RequestString.setText(this.ussdServer.getAutoUnstructured_SS_RequestString());

        tbDataCodingScheme.setText(((Integer) this.ussdServer.getDataCodingScheme()).toString());
        tbAlertingPattern.setText(((Integer) this.ussdServer.getAlertingPattern()).toString());

        cbOneNotificationFor100Dialogs.setSelected(this.ussdServer.isOneNotificationFor100Dialogs());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbProcessSsRequestAction, new ProcessSsRequestAction(
                ProcessSsRequestAction.VAL_MANUAL_RESPONSE));

        tbMsisdnAddress.setText("");
        tbAutoResponseString.setText("");
        tbAutoUnstructured_SS_RequestString.setText("");

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

        this.ussdServer.setMsisdnAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.ussdServer.setMsisdnNumberingPlan((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        this.ussdServer.setProcessSsRequestAction((ProcessSsRequestAction) cbProcessSsRequestAction.getSelectedItem());

        this.ussdServer.setMsisdnAddress(tbMsisdnAddress.getText());
        this.ussdServer.setAutoResponseString(tbAutoResponseString.getText());
        this.ussdServer.setAutoUnstructured_SS_RequestString(tbAutoUnstructured_SS_RequestString.getText());

        this.ussdServer.setDataCodingScheme(dataCodingScheme);
        this.ussdServer.setAlertingPattern(alertingPattern);

        this.ussdServer.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());

        return true;
    }
}
