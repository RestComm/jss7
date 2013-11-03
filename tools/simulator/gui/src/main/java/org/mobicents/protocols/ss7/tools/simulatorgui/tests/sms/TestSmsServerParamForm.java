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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms;

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
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.NumberingPlanIdentificationType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.SmsCodingType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsServerManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TypeOfNumberType;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsServerParamForm extends JDialog {
    private static final long serialVersionUID = 8609564738597978248L;

    private TestSmsServerManMBean smsServer;

    private JTextField tbHlrSsn;
    private JTextField tbVlrSsn;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JComboBox cbMapProtocolVersion;
    private JComboBox cbTypeOfNumber;
    private JComboBox cbNumberingPlanIdentification;
    private JTextField tbServiceCenterAddress;
    private JComboBox cbSmsCodingType;
    private JCheckBox cbSendSrsmdsIfError;
    private JCheckBox cbGprsSupportIndicator;

    public TestSmsServerParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("SMS test server settings");
        setBounds(100, 100, 539, 566);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setLayout(null);
        panel_1.setBounds(10, 42, 511, 94);
        panel.add(panel_1);

        JLabel lblParametersForAddressstring = new JLabel("Parameters for AddressString creation");
        lblParametersForAddressstring.setBounds(10, 0, 266, 14);
        panel_1.add(lblParametersForAddressstring);

        JLabel label_2 = new JLabel("AddressNature");
        label_2.setBounds(10, 28, 174, 14);
        panel_1.add(label_2);

        JLabel label_3 = new JLabel("NumberingPlan");
        label_3.setBounds(10, 59, 174, 14);
        panel_1.add(label_3);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(194, 25, 307, 20);
        panel_1.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(194, 56, 307, 20);
        panel_1.add(cbNumberingPlan);

        cbMapProtocolVersion = new JComboBox();
        cbMapProtocolVersion.setBounds(266, 11, 255, 20);
        panel.add(cbMapProtocolVersion);

        JLabel lblMapProtocolVersion = new JLabel("MAP protocol version");
        lblMapProtocolVersion.setBounds(10, 14, 204, 14);
        panel.add(lblMapProtocolVersion);

        tbHlrSsn = new JTextField();
        tbHlrSsn.setBounds(435, 172, 86, 20);
        panel.add(tbHlrSsn);
        tbHlrSsn.setColumns(10);

        tbVlrSsn = new JTextField();
        tbVlrSsn.setColumns(10);
        tbVlrSsn.setBounds(435, 203, 86, 20);
        panel.add(tbVlrSsn);

        JLabel lblHlrSsnFor = new JLabel("HLR SSN for outgoing SccpAddress (default value: 6)");
        lblHlrSsnFor.setBounds(10, 175, 415, 14);
        panel.add(lblHlrSsnFor);

        JLabel lblVlrSsnFor = new JLabel("VLR SSN for outgoing SccpAddress (default value: 8)");
        lblVlrSsnFor.setBounds(10, 206, 415, 14);
        panel.add(lblVlrSsnFor);

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2.setLayout(null);
        panel_2.setBounds(10, 234, 511, 94);
        panel.add(panel_2);

        JLabel lblParametersForSms = new JLabel("Parameters for SMS tpdu origAddress");
        lblParametersForSms.setBounds(10, 0, 266, 14);
        panel_2.add(lblParametersForSms);

        JLabel lblTypeofnumber = new JLabel("TypeOfNumber");
        lblTypeofnumber.setBounds(10, 28, 174, 14);
        panel_2.add(lblTypeofnumber);

        JLabel lblNumberingplanidentification = new JLabel("NumberingPlanIdentification");
        lblNumberingplanidentification.setBounds(10, 59, 174, 14);
        panel_2.add(lblNumberingplanidentification);

        cbTypeOfNumber = new JComboBox();
        cbTypeOfNumber.setBounds(194, 25, 307, 20);
        panel_2.add(cbTypeOfNumber);

        cbNumberingPlanIdentification = new JComboBox();
        cbNumberingPlanIdentification.setBounds(194, 56, 307, 20);
        panel_2.add(cbNumberingPlanIdentification);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 463, 246, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, 463, 255, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Cancel");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_2.setBounds(404, 497, 117, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, 497, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Reload");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_4.setBounds(10, 497, 144, 23);
        panel.add(button_4);

        JLabel lblOriginationServiceCenter = new JLabel("Origination Service center address string");
        lblOriginationServiceCenter.setBounds(10, 147, 339, 14);
        panel.add(lblOriginationServiceCenter);

        tbServiceCenterAddress = new JTextField();
        tbServiceCenterAddress.setColumns(10);
        tbServiceCenterAddress.setBounds(367, 144, 154, 20);
        panel.add(tbServiceCenterAddress);

        JLabel lblCharacterSetFor = new JLabel("Character set for SMS encoding");
        lblCharacterSetFor.setBounds(10, 342, 194, 14);
        panel.add(lblCharacterSetFor);

        cbSmsCodingType = new JComboBox();
        cbSmsCodingType.setBounds(214, 339, 307, 20);
        panel.add(cbSmsCodingType);

        cbSendSrsmdsIfError = new JCheckBox("Send reportSM-DeliveryStatus if error");
        cbSendSrsmdsIfError.setBounds(8, 367, 513, 25);
        panel.add(cbSendSrsmdsIfError);

        cbGprsSupportIndicator = new JCheckBox("Send GprsSupportIndicator in SRI request");
        cbGprsSupportIndicator.setBounds(8, 397, 513, 25);
        panel.add(cbGprsSupportIndicator);
    }

    public void setData(TestSmsServerManMBean smsServer) {
        this.smsServer = smsServer;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.smsServer.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.smsServer.getNumberingPlan());
        M3uaForm.setEnumeratedBaseComboBox(cbMapProtocolVersion, this.smsServer.getMapProtocolVersion());
        M3uaForm.setEnumeratedBaseComboBox(cbTypeOfNumber, this.smsServer.getTypeOfNumber());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlanIdentification, this.smsServer.getNumberingPlanIdentification());
        M3uaForm.setEnumeratedBaseComboBox(cbSmsCodingType, this.smsServer.getSmsCodingType());

        tbServiceCenterAddress.setText(this.smsServer.getServiceCenterAddress());
        tbHlrSsn.setText(((Integer) this.smsServer.getHlrSsn()).toString());
        tbVlrSsn.setText(((Integer) this.smsServer.getVlrSsn()).toString());
        cbSendSrsmdsIfError.setSelected(this.smsServer.isSendSrsmdsIfError());
        cbGprsSupportIndicator.setSelected(this.smsServer.isGprsSupportIndicator());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbMapProtocolVersion, new MapProtocolVersion(MapProtocolVersion.VAL_MAP_V3));
        M3uaForm.setEnumeratedBaseComboBox(cbTypeOfNumber, new TypeOfNumberType(TypeOfNumber.InternationalNumber.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlanIdentification, new NumberingPlanIdentificationType(
                NumberingPlanIdentification.ISDNTelephoneNumberingPlan.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbSmsCodingType, new SmsCodingType(SmsCodingType.VAL_GSM7));

        tbServiceCenterAddress.setText("");
        tbHlrSsn.setText("6");
        tbVlrSsn.setText("8");
        cbSendSrsmdsIfError.setSelected(false);
        cbGprsSupportIndicator.setSelected(false);
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        int hlrSsn = 0;
        int vlrSsn = 0;
        try {
            hlrSsn = Integer.parseInt(tbHlrSsn.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing hlrSsn value: " + e.toString());
            return false;
        }
        try {
            vlrSsn = Integer.parseInt(tbVlrSsn.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing vlrSsn value: " + e.toString());
            return false;
        }

        this.smsServer.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.smsServer.setNumberingPlan((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        this.smsServer.setMapProtocolVersion((MapProtocolVersion) cbMapProtocolVersion.getSelectedItem());
        this.smsServer.setTypeOfNumber((TypeOfNumberType) cbTypeOfNumber.getSelectedItem());
        this.smsServer.setNumberingPlanIdentification((NumberingPlanIdentificationType) cbNumberingPlanIdentification
                .getSelectedItem());
        this.smsServer.setSmsCodingType((SmsCodingType) cbSmsCodingType.getSelectedItem());

        this.smsServer.setServiceCenterAddress(tbServiceCenterAddress.getText());
        this.smsServer.setHlrSsn(hlrSsn);
        this.smsServer.setVlrSsn(vlrSsn);
        this.smsServer.setSendSrsmdsIfError(cbSendSrsmdsIfError.isSelected());
        this.smsServer.setGprsSupportIndicator(cbGprsSupportIndicator.isSelected());

        return true;
    }
}
