/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2018, Telestax Inc and individual contributors
 * by the @authors tag.
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
package org.restcomm.protocols.ss7.tools.simulatorgui.tests.lcs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;

import org.restcomm.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSPriority;
import org.restcomm.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.restcomm.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.restcomm.protocols.ss7.map.api.service.lsm.AreaType;
import org.restcomm.protocols.ss7.map.api.service.lsm.OccurrenceInfo;
import org.restcomm.protocols.ss7.map.api.service.lsm.ResponseTimeCategory;

import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.TestLcsServerManMBean;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.LCSPriorityEnumerated;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.LocationEstimateTypeEnumerated;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.AreaTypeEnumerated;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.LCSClientTypeEnumerated;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.PrivacyCheckRelatedActionEnumerated;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.OccurrenceInfoEnumerated;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.ResponseTimeCategoryEnumerated;

import org.restcomm.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */
public class TestLcsServerParamForm extends JDialog {

    private static final long serialVersionUID = 5428271328162943202L;

    private static Logger logger = Logger.getLogger(TestLcsServerParamForm.class);

    private TestLcsServerManMBean mapLcsServer;
    private JTextField cbAddress;
    private JTabbedPane tabbedPane;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JTextField tfMcc;
    private JTextField tfMnc;
    private JTextField tfLac;
    private JTextField tfCellId;
    private JTextField tfImsi;
    private JTextField tfMsisdn;
    private JTextField tfImei;
    private JTextField tfHgmlcAddress;
    private JTextField tfLcsReferenceNumber;


    // PSL Request
    private JComboBox locEstimateType;
    private JComboBox lcsClientType;
    private JTextField lcsServiceTypeID;
    private JTextField tfDataCodingScheme;
    private JTextField tfCodeWordUSSDString;
    private JComboBox callSessionUnrelated;
    private JComboBox callSessionRelated;
    private JComboBox areaType;
    private JComboBox occurrenceInfo;
    private JTextField intervalTime;
    private JCheckBox moLrShortCircuitIndicator;
    private JComboBox lcsPriority;
    private JTextField tfReptAmmount;
    private JTextField tfReptInterval;
    private JTextField horizontalAccuracy;
    private JTextField verticalAccuracy;
    private JCheckBox verticalCoordinateRequest;
    private JComboBox responseTimeCategory;
    private JTextField lmsi;

    // SLR Response
    private JTextField cbNaEsrdAddress;

    private JPanel createTab(JTabbedPane parent, String name) {
        JPanel tab = new JPanel();
        parent.addTab(name, null, tab, null);
        tab.setLayout(null);
        return tab;
    }

    private JPanel createSection(JPanel tab, String name, int y_pos, int height) {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBounds(26, y_pos, 511, height);
        tab.add(panel);

        JLabel label = new JLabel(name);
        label.setBounds(10, 0, 266, 14);
        panel.add(label);

        return panel;
    }

    private JComboBox createCombo(JPanel section, String name, int y_pos) {

        JLabel label = new JLabel(name);
        label.setBounds(10, y_pos, 174, 14);
        section.add(label);

        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(194, y_pos, 307, 20);
        section.add(comboBox);

        return comboBox;
    }

    private JTextField createTextField(JPanel section, String name, int y_pos) {

        JLabel label = new JLabel(name);
        label.setBounds(10, y_pos, 174, 14);
        section.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(194, y_pos, 307, 20);
        textField.setColumns(10);
        section.add(textField);

        return textField;
    }

    private JTextField createSmallTextField(JPanel section, String name, int x_pos, int y_pos) {

        int calculated_x = x_pos * 10 + (x_pos - 1) * 175;

        JLabel label = new JLabel(name);
        label.setBounds(calculated_x, y_pos, 75, 14);
        section.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(calculated_x + 75, y_pos, 75, 20);
        textField.setColumns(10);
        section.add(textField);

        return textField;
    }

    private void createLabel(JPanel section, String name, int y_pos) {

        JLabel label = new JLabel(name);
        label.setBounds(10, y_pos, 450, 14);
        section.add(label);
    }

    private JCheckBox createCheckbox(JPanel section, String name, int y_pos) {

        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setBounds(10, y_pos, 450, 20);
        section.add(checkBox);

        return checkBox;
    }

    public TestLcsServerParamForm(JFrame owner) {
        super(owner, true);

        int bottomOfPage = 800;
        int lineSeparation = 22;
        int sectionSeparation = 5;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("MAP LCS test client settings");
        setBounds(100, 100, 640, bottomOfPage);
        getContentPane().setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 634, bottomOfPage - 100);
        getContentPane().add(tabbedPane);

        // General TAB
        JPanel panel_gen = createTab(tabbedPane, "General");

        JPanel panel = createSection(panel_gen, "Parameters for AddressString creation", 23, lineSeparation * 5);
        cbAddressNature = createCombo(panel, "AddressNature", lineSeparation);
        cbNumberingPlan = createCombo(panel, "NumberingPlanType", lineSeparation * 2);
        cbAddress = createTextField(panel, "Digits", lineSeparation * 3);
        tfDataCodingScheme = createTextField(panel, "Data Coding Scheme(15-GSM7,72-UCS2)", lineSeparation * 4);

        // SRI Request TAB
        JPanel panel_sri = createTab(tabbedPane, "SRI request");
        JPanel panelSriDetail = createSection(panel_sri, "MLC Number parameters", 23, lineSeparation * 2);
        createLabel(panelSriDetail, "AddressNature, NumberingPlan and NumberingPlanType from General tab", lineSeparation);

        // PSL Request TAB
        JPanel panel_plr = createTab(tabbedPane, "PSL Request");

        JPanel panel_plr_1 = createSection(panel_plr, "PRS Loc Type", sectionSeparation, lineSeparation * 3);
        locEstimateType = createCombo(panel_plr_1, "PRS Loc Estimate Type", lineSeparation);
        createLabel(panel_plr_1, "Deferred Loc Estimate Type set to null", lineSeparation * 2);

        JPanel panel_3 = createSection(panel_plr, "Remaining Params", 2 * sectionSeparation + lineSeparation * 3, lineSeparation * 9);
        createLabel(panel_3, "MLC is created from General Tab data", lineSeparation);
        tfImsi = createTextField(panel_3, "IMSI", lineSeparation * 2);
        tfMsisdn = createTextField(panel_3, "MSISDN", lineSeparation * 3);
        tfImei = createTextField(panel_3, "IMEI", lineSeparation * 4);
        tfLcsReferenceNumber = createTextField(panel_3, "LCS Reference Number", lineSeparation * 5);
        tfHgmlcAddress = createTextField(panel_3, "H-GMLC Address", lineSeparation * 6);
        lcsServiceTypeID = createTextField(panel_3, "LCS Service Type ID", lineSeparation * 7);
        moLrShortCircuitIndicator = createCheckbox(panel_3, "moLr Short Circuit Indicator", lineSeparation * 8);

        // SLR Response TAB
        JPanel panel_slr_resp = createTab(tabbedPane, "SLR response");
        JPanel panelSlrRespDetail = createSection(panel_slr_resp, "NA-ESRD parameters for auto response", 23, lineSeparation * 3);
        cbNaEsrdAddress = createTextField(panelSlrRespDetail, "NA-ESRD address", lineSeparation);
        createLabel(panelSlrRespDetail, "AddressNature, NumberingPlanType from General tab", lineSeparation * 2);

        // LCS Client ID Partially harcoded
        JPanel panel_4 = createSection(panel_plr, "LCS Client ID", 3 * sectionSeparation + lineSeparation * 12, lineSeparation * 4);
        lcsClientType = createCombo(panel_4, "LCS Client Type", lineSeparation);
        createLabel(panel_4, "LCSClientExternalID, LCSClientInternalID, LCSClientName set to null", lineSeparation * 2);
        createLabel(panel_4, "AddressString, APN, LCSRequestorID set to null", lineSeparation * 3);

        JPanel panel_5 = createSection(panel_plr, "LCS Codeword", 4 * sectionSeparation + lineSeparation * 16, lineSeparation * 3);
        createLabel(panel_5, "Data Coding Scheme from General Tab", lineSeparation);
        tfCodeWordUSSDString = createTextField(panel_5, "USSD String", lineSeparation * 2);

        JPanel panel_6 = createSection(panel_plr, "LCS Privacy Check", 5 * sectionSeparation + lineSeparation * 19, lineSeparation * 3);
        callSessionUnrelated = createCombo(panel_6, "Call Session Unrelated", lineSeparation);
        callSessionRelated = createCombo(panel_6, "Call Session Related", lineSeparation * 2);

        JPanel panel_7 = createSection(panel_plr, "AreaEventInfo Id, Type, Occurrence, Interval", 6 * sectionSeparation + lineSeparation * 22, lineSeparation * 4);
        areaType = createCombo(panel_7, "areaType", lineSeparation);
        tfMcc = createSmallTextField(panel_7, "MCC", 1, lineSeparation * 2);
        tfMnc = createSmallTextField(panel_7, "MNC", 2, lineSeparation * 2);
        tfLac = createSmallTextField(panel_7, "LAC", 1, lineSeparation * 3);
        tfCellId = createSmallTextField(panel_7, "Cell Id", 2, lineSeparation * 3);
        occurrenceInfo = createCombo(panel_7, "Occurrence Info", lineSeparation * 3);
        intervalTime = createTextField(panel_7, "Interval time", lineSeparation * 4);

        JPanel panel_8 = createSection(panel_plr, "PeriodicLDRInfo", 8 * sectionSeparation + lineSeparation * 32, lineSeparation * 2);
        tfReptAmmount = createSmallTextField(panel_8, "Rept Ammount", 1, lineSeparation);
        tfReptInterval = createSmallTextField(panel_8, "Rept Interval", 2, lineSeparation);

        JPanel panel_9 = createSection(panel_plr, "LCSPriority", 9 * sectionSeparation + lineSeparation * 34, lineSeparation * 2);
        lcsPriority = createCombo(panel_9, "LCSPriority", lineSeparation);

        JPanel panel_10 = createSection(panel_plr, "LCS QoS", 10 * sectionSeparation + lineSeparation * 36, lineSeparation * 5);
        horizontalAccuracy = createSmallTextField(panel_10, "Horizontal accuracy", 1, lineSeparation);
        verticalAccuracy = createSmallTextField(panel_10, "Vertical accuracy", 2, lineSeparation);
        verticalCoordinateRequest = createCheckbox(panel_10, "Vertical Coordinate Request", lineSeparation * 2);
        responseTimeCategory = createCombo(panel_10, "Response Time Category", lineSeparation * 3);

        JPanel panel_11 = createSection(panel_plr, "LMSI", 11 * sectionSeparation + lineSeparation * 40, lineSeparation * 2);
        lmsi = createSmallTextField(panel_11, "LMSI", 1, lineSeparation);

    /*
            null, // Cond round 2 - MAPExtensionContainer extensionContainer,
            null, // Cond round 2 -SupportedGADShapes supportedGADShapes,
            null // Cond round 2 - ReportingPLMNList reportingPLMNList
    */

        JButton button = new JButton("Load default values for side A");
        button.setBounds(10, bottomOfPage - 90, 246, 23);
        getContentPane().add(button);

        JButton button_3 = new JButton("Load default values for side B");
        button_3.setBounds(266, bottomOfPage - 90, 255, 23);
        getContentPane().add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.setBounds(404, bottomOfPage - 60, 117, 23);
        getContentPane().add(button_4);

        JButton button_2 = new JButton("Save");
        button_2.setBounds(180, bottomOfPage - 60, 117, 23);
        getContentPane().add(button_2);

        JButton button_1 = new JButton("Reload");
        button_1.setBounds(10, bottomOfPage - 60, 144, 23);
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

    public void setData(TestLcsServerManMBean mapLcsServer) {
        this.mapLcsServer = mapLcsServer;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.mapLcsServer.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.mapLcsServer.getNumberingPlanType());
        this.tfDataCodingScheme.setText(this.mapLcsServer.getDataCodingScheme().toString());
        // PSL tab
        //SRI tab
        cbAddress.setText(this.mapLcsServer.getNumberingPlan());

        //PSL Request tab
        this.tfHgmlcAddress.setText(this.mapLcsServer.getHGMLCAddress());
        this.tfImsi.setText(this.mapLcsServer.getIMSI());
        this.tfMsisdn.setText(this.mapLcsServer.getMSISDN());
        this.tfImei.setText(this.mapLcsServer.getIMEI());
        this.tfLcsReferenceNumber.setText(this.mapLcsServer.getLCSReferenceNumber().toString());
        this.lcsServiceTypeID.setText(this.mapLcsServer.getLcsServiceTypeID().toString());
        this.moLrShortCircuitIndicator.setSelected(this.mapLcsServer.getMoLrShortCircuitIndicator());
        M3uaForm.setEnumeratedBaseComboBox(locEstimateType, this.mapLcsServer.getLocEstimateType());
        M3uaForm.setEnumeratedBaseComboBox(lcsClientType, this.mapLcsServer.getLcsClientTypeEnumerated());
        this.tfCodeWordUSSDString.setText(this.mapLcsServer.getCodeWordUSSDString().toString());
        M3uaForm.setEnumeratedBaseComboBox(callSessionUnrelated, this.mapLcsServer.getCallSessionUnrelated());
        M3uaForm.setEnumeratedBaseComboBox(callSessionRelated, this.mapLcsServer.getCallSessionRelated());
        M3uaForm.setEnumeratedBaseComboBox(areaType, this.mapLcsServer.getAreaType());
        this.tfMcc.setText(this.mapLcsServer.getMCC().toString());
        this.tfMnc.setText(this.mapLcsServer.getMNC().toString());
        this.tfLac.setText(this.mapLcsServer.getLAC().toString());
        this.tfCellId.setText(this.mapLcsServer.getCellId().toString());
        M3uaForm.setEnumeratedBaseComboBox(occurrenceInfo, this.mapLcsServer.getOccurrenceInfo());
        this.intervalTime.setText(this.mapLcsServer.getIntervalTime().toString());
        this.tfReptAmmount.setText(this.mapLcsServer.getReportingAmount().toString());
        this.tfReptInterval.setText(this.mapLcsServer.getReportingInterval().toString());
        this.lmsi.setText(this.mapLcsServer.getLMSI().toString());

        //SLR Response tab
        cbNaEsrdAddress.setText(this.mapLcsServer.getNaESRDAddress());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        this.tfDataCodingScheme.setText("15");
        //SRI Request tab
        cbAddress.setText("5980482910");

        //PSL Request tab
        this.tfHgmlcAddress.setText("200.10.0.1");

        this.tfImsi.setText("748010192837465");
        this.tfMsisdn.setText("59899077937");
        this.tfImei.setText("354449063537030");

        this.tfLcsReferenceNumber.setText("921");

        this.lcsServiceTypeID.setText("5");

        this.moLrShortCircuitIndicator.setSelected(false);

        M3uaForm.setEnumeratedBaseComboBox(lcsPriority, new LCSPriorityEnumerated(LCSPriority.normalPriority.getCode()));

        this.tfCodeWordUSSDString.setText("CW");

        this.tfReptAmmount.setText("3");
        this.tfReptInterval.setText("30");

        M3uaForm.setEnumeratedBaseComboBox(locEstimateType, new LocationEstimateTypeEnumerated(LocationEstimateType.currentLocation.getType()));
        M3uaForm.setEnumeratedBaseComboBox(lcsClientType, new LCSClientTypeEnumerated(LCSClientType.emergencyServices.getType()));

        M3uaForm.setEnumeratedBaseComboBox(callSessionUnrelated, new PrivacyCheckRelatedActionEnumerated(PrivacyCheckRelatedAction.allowedWithoutNotification.getAction()));
        M3uaForm.setEnumeratedBaseComboBox(callSessionRelated, new PrivacyCheckRelatedActionEnumerated(PrivacyCheckRelatedAction.allowedWithNotification.getAction()));

        M3uaForm.setEnumeratedBaseComboBox(areaType, new AreaTypeEnumerated(AreaType.locationAreaId.getType()));
        this.tfMcc.setText("748");
        this.tfMnc.setText("01");
        this.tfLac.setText("79010");
        this.tfCellId.setText("222");
        M3uaForm.setEnumeratedBaseComboBox(occurrenceInfo, new OccurrenceInfoEnumerated(OccurrenceInfo.oneTimeEvent.getInfo()));
        this.intervalTime.setText("60");

        this.horizontalAccuracy.setText("50");
        this.verticalAccuracy.setText("200");
        this.verticalCoordinateRequest.setSelected(false);
        M3uaForm.setEnumeratedBaseComboBox(responseTimeCategory, new ResponseTimeCategoryEnumerated((ResponseTimeCategory.lowdelay.getCategory())));

        this.lmsi.setText("09876543");

        //SLR Response tab
        cbNaEsrdAddress.setText("11114444");

    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        this.mapLcsServer.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.mapLcsServer.setNumberingPlanType((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        this.mapLcsServer.setLocEstimateType((LocationEstimateTypeEnumerated) locEstimateType.getSelectedItem());
        //SRI tab
        this.mapLcsServer.setNumberingPlan(cbAddress.getText());

        //PSL tab
        try {
            this.mapLcsServer.setLCSReferenceNumber(Integer.valueOf(this.tfLcsReferenceNumber.getText()));
            this.mapLcsServer.setMCC(Integer.valueOf(this.tfMcc.getText()));
            this.mapLcsServer.setMNC(Integer.valueOf(this.tfMnc.getText()));
            this.mapLcsServer.setLAC(Integer.valueOf(this.tfLac.getText()));
            this.mapLcsServer.setCellId(Integer.valueOf(this.tfCellId.getText()));
            this.mapLcsServer.setLcsServiceTypeID(Integer.valueOf(this.lcsServiceTypeID.getText()));
            this.mapLcsServer.setReportingAmount(Integer.valueOf(this.tfReptAmmount.getText()));
            this.mapLcsServer.setReportingInterval(Integer.valueOf(this.tfReptInterval.getText()));
            this.mapLcsServer.setDataCodingScheme(Integer.valueOf(this.tfDataCodingScheme.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "value: " + e.toString() + "Not valid, must be a number");
            return false;
        }
        this.mapLcsServer.setHGMLCAddress(this.tfHgmlcAddress.getText());
        this.mapLcsServer.setIMEI(this.tfImei.getText());
        this.mapLcsServer.setIMSI(this.tfImsi.getText());
        this.mapLcsServer.setMSISDN(this.tfMsisdn.getText());
        this.mapLcsServer.setMoLrShortCircuitIndicator(this.moLrShortCircuitIndicator.isSelected());
        this.mapLcsServer.setLcsClientTypeEnumerated((LCSClientTypeEnumerated) lcsClientType.getSelectedItem());
        this.mapLcsServer.setCodeWordUSSDString(this.tfCodeWordUSSDString.getText());
        this.mapLcsServer.setCallSessionUnrelated((PrivacyCheckRelatedActionEnumerated) callSessionUnrelated.getSelectedItem());
        this.mapLcsServer.setCallSessionRelated((PrivacyCheckRelatedActionEnumerated) callSessionRelated.getSelectedItem());
        this.mapLcsServer.setAreaType((AreaTypeEnumerated) areaType.getSelectedItem());

        //SLR Response tab
        this.mapLcsServer.setNaESRDAddress(cbNaEsrdAddress.getText());

        return true;
    }
}
