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

package org.restcomm.protocols.ss7.tools.simulatorgui.tests.ussd;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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

import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.restcomm.protocols.ss7.tools.simulator.tests.sms.SRIReaction;
import org.restcomm.protocols.ss7.tools.simulator.tests.ussd.TestUssdClientManMBean;
import org.restcomm.protocols.ss7.tools.simulator.tests.ussd.UssdClientAction;
import org.restcomm.protocols.ss7.tools.simulatorgui.M3uaForm;

import javax.swing.JTabbedPane;

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
    private JCheckBox cbAutoResponseOnUnstructuredSSRequests;
    private JTextField tbResponseString;
    private JTextField tbSRIResponseImsi;
    private JTextField tbSRIResponseVlr;
    private JComboBox cbSRIReaction;
    private JCheckBox cbReturn20PersDeliveryErrors;

    public TestUssdClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("USSD test client settings");
        setBounds(100, 100, 638, 575);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 476, 256, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(276, 476, 245, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Reload");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.setBounds(10, 510, 144, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(277, 510, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_4.setBounds(404, 510, 117, 23);
        panel.add(button_4);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 630, 465);
        panel.add(tabbedPane);

        JPanel panel_gen = new JPanel();
        tabbedPane.addTab("General", null, panel_gen, null);
        panel_gen.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(10, 11, 511, 110);
        panel_gen.add(panel_1);
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
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
        lblDataCodingScheme.setBounds(10, 131, 299, 14);
        panel_gen.add(lblDataCodingScheme);

        tbDataCodingScheme = new JTextField();
        tbDataCodingScheme.setBounds(423, 128, 86, 20);
        panel_gen.add(tbDataCodingScheme);
        tbDataCodingScheme.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Alerting pattern value (-1 means does not use AlertingPattern)");
        lblNewLabel_1.setBounds(10, 162, 384, 14);
        panel_gen.add(lblNewLabel_1);

        tbAlertingPattern = new JTextField();
        tbAlertingPattern.setBounds(423, 159, 86, 20);
        panel_gen.add(tbAlertingPattern);
        tbAlertingPattern.setColumns(10);

        JLabel lblUssdClientMode = new JLabel("Ussd client mode");
        lblUssdClientMode.setBounds(10, 187, 149, 14);
        panel_gen.add(lblUssdClientMode);

        cbUssdClientAction = new JComboBox();
        cbUssdClientAction.setBounds(10, 212, 499, 20);
        panel_gen.add(cbUssdClientAction);
        cbUssdClientAction
                .setToolTipText("<html>\r\nThe mode of UssdClient work. When manual response user can manually send ProcessSsUnstructured request, \r\n<br>\r\nwhen VAL_AUTO_SendProcessUnstructuredSSRequest the tester sends ProcessSsUnstructured requests without dealay (load test)\r\n</html>");

        JLabel lblStringOfAuto = new JLabel("String of auto processUnsructuresSs request");
        lblStringOfAuto.setBounds(10, 243, 324, 14);
        panel_gen.add(lblStringOfAuto);

        tbAutoRequestString = new JTextField();
        tbAutoRequestString.setBounds(10, 265, 511, 20);
        panel_gen.add(tbAutoRequestString);
        tbAutoRequestString.setColumns(10);

        JLabel lblTheCountOf = new JLabel("The count of maximum active MAP dialogs when the auto sending mode");
        lblTheCountOf.setBounds(10, 299, 511, 14);
        panel_gen.add(lblTheCountOf);

        tbMaxConcurrentDialogs = new JTextField();
        tbMaxConcurrentDialogs.setBounds(10, 322, 98, 20);
        panel_gen.add(tbMaxConcurrentDialogs);
        tbMaxConcurrentDialogs.setColumns(10);

        cbOneNotificationFor100Dialogs = new JCheckBox("One notification for 100 dialogs (recommended for the auto sending mode)");
        cbOneNotificationFor100Dialogs.setBounds(10, 349, 511, 23);
        panel_gen.add(cbOneNotificationFor100Dialogs);

        cbAutoResponseOnUnstructuredSSRequests = new JCheckBox("Auto response to incoming UnstructuredSS requests");
        cbAutoResponseOnUnstructuredSSRequests.setBounds(10, 372, 511, 23);
        cbAutoResponseOnUnstructuredSSRequests.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED){
                    tbResponseString.setEnabled(true);
                    //tbResponseString.setText("Please enter response string");
                }
                else if(e.getStateChange() == ItemEvent.DESELECTED){
                    tbResponseString.setEnabled(false);
                }
                validate();
                repaint();
            }
        });
        panel_gen.add(cbAutoResponseOnUnstructuredSSRequests);

        JLabel lblResponseString = new JLabel("Response string");
        lblResponseString.setBounds(10, 395, 324, 14);
        panel_gen.add(lblResponseString);

        tbResponseString = new JTextField();
        tbResponseString.setBounds(10, 418, 511, 20);
        tbResponseString.setColumns(10);
        tbResponseString.setEnabled(false);
        panel_gen.add(tbResponseString);

        JPanel panel_sri = new JPanel();
        tabbedPane.addTab("SRI response", null, panel_sri, null);
        panel_sri.setLayout(null);

        JLabel label = new JLabel("IMSI for auto sendRoutingInfoForSM response");
        label.setBounds(10, 14, 361, 14);
        panel_sri.add(label);

        tbSRIResponseImsi = new JTextField();
        tbSRIResponseImsi.setColumns(10);
        tbSRIResponseImsi.setBounds(482, 11, 137, 20);
        panel_sri.add(tbSRIResponseImsi);

        tbSRIResponseVlr = new JTextField();
        tbSRIResponseVlr.setColumns(10);
        tbSRIResponseVlr.setBounds(482, 42, 137, 20);
        panel_sri.add(tbSRIResponseVlr);

        JLabel label_1 = new JLabel("VLR address for auto sendRoutingInfoForSM response");
        label_1.setBounds(10, 45, 361, 14);
        panel_sri.add(label_1);

        JLabel label_2 = new JLabel("Reaction for SRI request");
        label_2.setBounds(10, 76, 290, 14);
        panel_sri.add(label_2);

        cbSRIReaction = new JComboBox();
        cbSRIReaction.setBounds(310, 73, 309, 20);
        panel_sri.add(cbSRIReaction);

        cbReturn20PersDeliveryErrors = new JCheckBox("Return 20% delivery errors for SRI or MtForwardSM Requests");
        cbReturn20PersDeliveryErrors.setBounds(10, 100, 511, 23);
        panel_sri.add(cbReturn20PersDeliveryErrors);
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
        tbResponseString.setText(this.ussdClient.getAutoResponseString());

        tbDataCodingScheme.setText(((Integer) this.ussdClient.getDataCodingScheme()).toString());
        tbAlertingPattern.setText(((Integer) this.ussdClient.getAlertingPattern()).toString());
        tbMaxConcurrentDialogs.setText(((Integer) this.ussdClient.getMaxConcurrentDialogs()).toString());

        cbOneNotificationFor100Dialogs.setSelected(this.ussdClient.isOneNotificationFor100Dialogs());
        cbAutoResponseOnUnstructuredSSRequests.setSelected(this.ussdClient.isAutoResponseOnUnstructuredSSRequests());

        tbSRIResponseImsi.setText(this.ussdClient.getSRIResponseImsi());
        tbSRIResponseVlr.setText(this.ussdClient.getSRIResponseVlr());
        M3uaForm.setEnumeratedBaseComboBox(cbSRIReaction, this.ussdClient.getSRIReaction());
        cbReturn20PersDeliveryErrors.setSelected(this.ussdClient.isReturn20PersDeliveryErrors());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbUssdClientAction, new UssdClientAction(UssdClientAction.VAL_MANUAL_OPERATION));

        tbMsisdnAddress.setText("");
        tbAutoRequestString.setText("");
        tbResponseString.setText("");
        tbMaxConcurrentDialogs.setText("10");

        tbDataCodingScheme.setText("15");
        tbAlertingPattern.setText("-1");

        cbOneNotificationFor100Dialogs.setSelected(false);
        cbAutoResponseOnUnstructuredSSRequests.setSelected(false);

        tbSRIResponseImsi.setText("");
        tbSRIResponseVlr.setText("");
        M3uaForm.setEnumeratedBaseComboBox(cbSRIReaction, new SRIReaction(SRIReaction.VAL_RETURN_SUCCESS));
        cbReturn20PersDeliveryErrors.setSelected(false);
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
        this.ussdClient.setAutoResponseString(tbResponseString.getText());

        this.ussdClient.setDataCodingScheme(dataCodingScheme);
        this.ussdClient.setAlertingPattern(alertingPattern);
        this.ussdClient.setMaxConcurrentDialogs(maxConcurrentDialogs);

        this.ussdClient.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());
        this.ussdClient.setAutoResponseOnUnstructuredSSRequests(cbAutoResponseOnUnstructuredSSRequests.isSelected());

        this.ussdClient.setSRIResponseImsi(tbSRIResponseImsi.getText());
        this.ussdClient.setSRIResponseVlr(tbSRIResponseVlr.getText());
        this.ussdClient.setSRIReaction((SRIReaction) cbSRIReaction.getSelectedItem());
        this.ussdClient.setReturn20PersDeliveryErrors(cbReturn20PersDeliveryErrors.isSelected());

        return true;
    }
}
