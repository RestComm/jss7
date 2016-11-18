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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.sms;

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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.smstpdu.NumberingPlanIdentification;
import org.mobicents.protocols.ss7.map.api.smstpdu.TypeOfNumber;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.MtFSMReaction;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.NumberingPlanIdentificationType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.ReportSMDeliveryStatusReaction;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.SRIInformServiceCenter;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.SRIReaction;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.SmsCodingType;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TestSmsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulator.tests.sms.TypeOfNumberType;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestSmsClientParamForm extends JDialog {
    private static final long serialVersionUID = 5428271328162943202L;

    private TestSmsClientManMBean smsClient;

    private JTextField tbServiceCenterAddress;
    private JTextField tbSmscSsn;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JComboBox cbMapProtocolVersion;
    private JComboBox cbTypeOfNumber;
    private JComboBox cbNumberingPlanIdentification;
    private JComboBox cbSmsCodingType;
    private JTextField tbSRIResponseImsi;
    private JTextField tbSRIResponseVlr;
    private JComboBox cbSRIReaction;
    private JComboBox cbSRIInformServiceCenter;
    private JCheckBox cbSRIScAddressNotIncluded;
    private JComboBox cbMtFSMReaction;
    private JCheckBox cbOneNotificationFor100Dialogs;
    private JCheckBox cbReturn20PersDeliveryErrors;
    private JCheckBox cbContinueDialog;
    private JComboBox cbRsmdsReaction;
    private JCheckBox cbStatusReportRequest;
    private JTextField tbNationalLanguageCode;

    public TestSmsClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("SMS test client settings");
        setBounds(100, 100, 640, 584);
        getContentPane().setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 634, 465);
        getContentPane().add(tabbedPane);

        JPanel panel_gen = new JPanel();
        tabbedPane.addTab("General", null, panel_gen, null);
        panel_gen.setLayout(null);

        JLabel label = new JLabel("MAP protocol version");
        label.setBounds(10, 14, 204, 14);
        panel_gen.add(label);

        cbMapProtocolVersion = new JComboBox();
        cbMapProtocolVersion.setBounds(266, 11, 255, 20);
        panel_gen.add(cbMapProtocolVersion);

        JLabel lblSmscSsnFor = new JLabel("SMSC SSN for outgoing SccpAddress (default value: 8)");
        lblSmscSsnFor.setBounds(10, 44, 415, 14);
        panel_gen.add(lblSmscSsnFor);

        tbSmscSsn = new JTextField();
        tbSmscSsn.setColumns(10);
        tbSmscSsn.setBounds(435, 41, 86, 20);
        panel_gen.add(tbSmscSsn);

        cbOneNotificationFor100Dialogs = new JCheckBox("One notification for 100 dialogs (recommended for bulk message mode)");
        cbOneNotificationFor100Dialogs.setBounds(10, 67, 511, 23);
        panel_gen.add(cbOneNotificationFor100Dialogs);

        cbReturn20PersDeliveryErrors = new JCheckBox("Return 20% delivery errors for SRI or MtForwardSM Requests");
        cbReturn20PersDeliveryErrors.setBounds(10, 95, 511, 23);
        panel_gen.add(cbReturn20PersDeliveryErrors);

        JPanel panel_sri = new JPanel();
        tabbedPane.addTab("SRI response", panel_sri);
        panel_sri.setLayout(null);

        JLabel label_7 = new JLabel("IMSI for auto sendRoutingInfoForSM response");
        label_7.setBounds(10, 8, 361, 14);
        panel_sri.add(label_7);

        tbSRIResponseImsi = new JTextField();
        tbSRIResponseImsi.setBounds(482, 5, 137, 20);
        tbSRIResponseImsi.setColumns(10);
        panel_sri.add(tbSRIResponseImsi);

        tbSRIResponseVlr = new JTextField();
        tbSRIResponseVlr.setBounds(482, 36, 137, 20);
        tbSRIResponseVlr.setColumns(10);
        panel_sri.add(tbSRIResponseVlr);

        JLabel label_8 = new JLabel("VLR address for auto sendRoutingInfoForSM response");
        label_8.setBounds(10, 39, 361, 14);
        panel_sri.add(label_8);

        JLabel lblReactionForSri = new JLabel("Reaction for SRI request");
        lblReactionForSri.setBounds(10, 70, 290, 14);
        panel_sri.add(lblReactionForSri);

        JLabel lblSriinformservicecenter = new JLabel("Sending InformServiceCenter for SRI request");
        lblSriinformservicecenter.setBounds(10, 101, 290, 14);
        panel_sri.add(lblSriinformservicecenter);

        cbSRIReaction = new JComboBox();
        cbSRIReaction.setBounds(310, 67, 309, 20);
        panel_sri.add(cbSRIReaction);

        cbSRIInformServiceCenter = new JComboBox();
        cbSRIInformServiceCenter.setBounds(310, 98, 309, 20);
        panel_sri.add(cbSRIInformServiceCenter);

        cbSRIScAddressNotIncluded = new JCheckBox("InformServiceCentre: ServiceCenter Address is not included in MWD");
        cbSRIScAddressNotIncluded.setBounds(10, 128, 457, 23);
        panel_sri.add(cbSRIScAddressNotIncluded);

        JPanel panel_mtfsm = new JPanel();
        tabbedPane.addTab("MtFSM response", null, panel_mtfsm, null);
        panel_mtfsm.setLayout(null);

        JLabel lblReactionForMtfsm = new JLabel("Reaction for MtFSM request");
        lblReactionForMtfsm.setBounds(12, 16, 290, 14);
        panel_mtfsm.add(lblReactionForMtfsm);

        cbMtFSMReaction = new JComboBox();
        cbMtFSMReaction.setBounds(312, 13, 309, 20);
        panel_mtfsm.add(cbMtFSMReaction);
        cbContinueDialog = new JCheckBox("Continue dialog after MtForwardSM receiving (MtForwardSM resnonse in TC-CONTINUE)");
        cbContinueDialog.setBounds(12, 39, 609, 23);
        panel_mtfsm.add(cbContinueDialog);

        JPanel panel_mofsm = new JPanel();
        tabbedPane.addTab("MoFSM request", null, panel_mofsm, null);
        panel_mofsm.setLayout(null);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(12, 13, 511, 94);
        panel_mofsm.add(panel_2);
        panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2.setLayout(null);

        JLabel lblParametersForSms = new JLabel("Parameters for SMS tpdu destAddress");
        lblParametersForSms.setBounds(10, 0, 266, 14);
        panel_2.add(lblParametersForSms);

        JLabel label_5 = new JLabel("TypeOfNumber");
        label_5.setBounds(10, 28, 174, 14);
        panel_2.add(label_5);

        JLabel label_6 = new JLabel("NumberingPlanIdentification");
        label_6.setBounds(10, 59, 174, 14);
        panel_2.add(label_6);

        cbTypeOfNumber = new JComboBox();
        cbTypeOfNumber.setBounds(194, 25, 307, 20);
        panel_2.add(cbTypeOfNumber);

        cbNumberingPlanIdentification = new JComboBox();
        cbNumberingPlanIdentification.setBounds(194, 56, 307, 20);
        panel_2.add(cbNumberingPlanIdentification);

        JLabel label_4 = new JLabel("Character set for SMS encoding");
        label_4.setBounds(12, 121, 194, 14);
        panel_mofsm.add(label_4);

        cbSmsCodingType = new JComboBox();
        cbSmsCodingType.setBounds(216, 118, 307, 20);
        panel_mofsm.add(cbSmsCodingType);

        JPanel panel_1 = new JPanel();
        panel_1.setBounds(12, 151, 511, 94);
        panel_mofsm.add(panel_1);
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setLayout(null);

        JLabel label_1 = new JLabel("Parameters for AddressString creation");
        label_1.setBounds(10, 0, 266, 14);
        panel_1.add(label_1);

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

        JLabel lblDestinationServiceCenter = new JLabel("Destination Service center address string");
        lblDestinationServiceCenter.setBounds(12, 259, 339, 14);
        panel_mofsm.add(lblDestinationServiceCenter);

        tbServiceCenterAddress = new JTextField();
        tbServiceCenterAddress.setBounds(369, 256, 154, 20);
        panel_mofsm.add(tbServiceCenterAddress);
        tbServiceCenterAddress.setColumns(10);

        JLabel lblNationalLanguageShift = new JLabel("National language shift table code (for GSM7 encoding)");
        lblNationalLanguageShift.setBounds(12, 287, 339, 14);
        panel_mofsm.add(lblNationalLanguageShift);

        tbNationalLanguageCode = new JTextField();
        tbNationalLanguageCode.setColumns(10);
        tbNationalLanguageCode.setBounds(369, 284, 75, 20);
        panel_mofsm.add(tbNationalLanguageCode);

        cbStatusReportRequest = new JCheckBox("Sending StatusReportRequest in MO request");
        cbStatusReportRequest.setBounds(12, 308, 609, 23);
        panel_mofsm.add(cbStatusReportRequest);

        JPanel panel_ReportSMDeliveryStatus = new JPanel();
        tabbedPane.addTab("ReportSMDeliveryStatus response", null, panel_ReportSMDeliveryStatus, null);
        panel_ReportSMDeliveryStatus.setLayout(null);

        JLabel lblReactionForReportsmdeliverystatus = new JLabel("Reaction for ReportSMDeliveryStatus request");
        lblReactionForReportsmdeliverystatus.setBounds(10, 28, 271, 14);
        panel_ReportSMDeliveryStatus.add(lblReactionForReportsmdeliverystatus);

        cbRsmdsReaction = new JComboBox();
        cbRsmdsReaction.setBounds(291, 25, 328, 20);
        panel_ReportSMDeliveryStatus.add(cbRsmdsReaction);

        JButton button = new JButton("Load default values for side A");
        button.setBounds(10, 476, 246, 23);
        getContentPane().add(button);

        JButton button_3 = new JButton("Load default values for side B");
        button_3.setBounds(266, 476, 255, 23);
        getContentPane().add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.setBounds(404, 510, 117, 23);
        getContentPane().add(button_4);

        JButton button_2 = new JButton("Save");
        button_2.setBounds(180, 510, 117, 23);
        getContentPane().add(button_2);

        JButton button_1 = new JButton("Reload");
        button_1.setBounds(10, 510, 144, 23);
        getContentPane().add(button_1);
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
    }

    public void setData(TestSmsClientManMBean smsClient) {
        this.smsClient = smsClient;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.smsClient.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.smsClient.getNumberingPlan());
        M3uaForm.setEnumeratedBaseComboBox(cbMapProtocolVersion, this.smsClient.getMapProtocolVersion());
        M3uaForm.setEnumeratedBaseComboBox(cbTypeOfNumber, this.smsClient.getTypeOfNumber());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlanIdentification, this.smsClient.getNumberingPlanIdentification());
        M3uaForm.setEnumeratedBaseComboBox(cbSmsCodingType, this.smsClient.getSmsCodingType());

        M3uaForm.setEnumeratedBaseComboBox(cbSRIReaction, this.smsClient.getSRIReaction());
        M3uaForm.setEnumeratedBaseComboBox(cbSRIInformServiceCenter, this.smsClient.getSRIInformServiceCenter());
        M3uaForm.setEnumeratedBaseComboBox(cbMtFSMReaction, this.smsClient.getMtFSMReaction());
        M3uaForm.setEnumeratedBaseComboBox(cbRsmdsReaction, this.smsClient.getReportSMDeliveryStatusReaction());

        tbServiceCenterAddress.setText(this.smsClient.getServiceCenterAddress());
        tbSRIResponseImsi.setText(this.smsClient.getSRIResponseImsi());
        tbSRIResponseVlr.setText(this.smsClient.getSRIResponseVlr());

        tbSmscSsn.setText(((Integer) this.smsClient.getSmscSsn()).toString());
        tbNationalLanguageCode.setText(((Integer) this.smsClient.getNationalLanguageCode()).toString());

        cbSRIScAddressNotIncluded.setSelected(this.smsClient.isSRIScAddressNotIncluded());
        cbOneNotificationFor100Dialogs.setSelected(this.smsClient.isOneNotificationFor100Dialogs());
        cbReturn20PersDeliveryErrors.setSelected(this.smsClient.isReturn20PersDeliveryErrors());
        cbContinueDialog.setSelected(this.smsClient.isContinueDialog());
        cbStatusReportRequest.setSelected(this.smsClient.isStatusReportRequest());
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

        M3uaForm.setEnumeratedBaseComboBox(cbSRIReaction, new SRIReaction(SRIReaction.VAL_RETURN_SUCCESS));
        M3uaForm.setEnumeratedBaseComboBox(cbSRIInformServiceCenter, new SRIInformServiceCenter(SRIInformServiceCenter.MWD_NO));
        M3uaForm.setEnumeratedBaseComboBox(cbMtFSMReaction, new MtFSMReaction(MtFSMReaction.VAL_RETURN_SUCCESS));
        M3uaForm.setEnumeratedBaseComboBox(cbRsmdsReaction, new ReportSMDeliveryStatusReaction(
                ReportSMDeliveryStatusReaction.VAL_RETURN_SUCCESS));

        tbServiceCenterAddress.setText("");
        tbSRIResponseImsi.setText("");
        tbSRIResponseVlr.setText("");

        tbSmscSsn.setText("8");
        tbNationalLanguageCode.setText("0");

        cbSRIScAddressNotIncluded.setSelected(false);
        cbOneNotificationFor100Dialogs.setSelected(false);
        cbReturn20PersDeliveryErrors.setSelected(false);
        cbContinueDialog.setSelected(false);
        cbStatusReportRequest.setSelected(false);
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        int smscSsn = 0;
        try {
            smscSsn = Integer.parseInt(tbSmscSsn.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing smscSsn value: " + e.toString());
            return false;
        }
        int nationalLanguageCode = 0;
        try {
            nationalLanguageCode = Integer.parseInt(tbNationalLanguageCode.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing nationalLanguageCode value: " + e.toString());
            return false;
        }

        this.smsClient.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.smsClient.setNumberingPlan((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        this.smsClient.setMapProtocolVersion((MapProtocolVersion) cbMapProtocolVersion.getSelectedItem());
        this.smsClient.setTypeOfNumber((TypeOfNumberType) cbTypeOfNumber.getSelectedItem());
        this.smsClient.setNumberingPlanIdentification((NumberingPlanIdentificationType) cbNumberingPlanIdentification
                .getSelectedItem());
        this.smsClient.setSmsCodingType((SmsCodingType) cbSmsCodingType.getSelectedItem());

        this.smsClient.setSRIReaction((SRIReaction) cbSRIReaction.getSelectedItem());
        this.smsClient.setSRIInformServiceCenter((SRIInformServiceCenter) cbSRIInformServiceCenter.getSelectedItem());
        this.smsClient.setMtFSMReaction((MtFSMReaction) cbMtFSMReaction.getSelectedItem());
        this.smsClient.setReportSMDeliveryStatusReaction((ReportSMDeliveryStatusReaction) cbRsmdsReaction.getSelectedItem());

        this.smsClient.setServiceCenterAddress(tbServiceCenterAddress.getText());
        this.smsClient.setSRIResponseImsi(tbSRIResponseImsi.getText());
        this.smsClient.setSRIResponseVlr(tbSRIResponseVlr.getText());

        this.smsClient.setSmscSsn(smscSsn);
        this.smsClient.setNationalLanguageCode(nationalLanguageCode);

        this.smsClient.setSRIScAddressNotIncluded(cbSRIScAddressNotIncluded.isSelected());
        this.smsClient.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());
        this.smsClient.setReturn20PersDeliveryErrors(cbReturn20PersDeliveryErrors.isSelected());
        this.smsClient.setContinueDialog(cbContinueDialog.isSelected());
        this.smsClient.setStatusReportRequest(cbStatusReportRequest.isSelected());

        return true;
    }
}
