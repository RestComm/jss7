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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.lcs;

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
//import javax.swing.JScrollPane;
//import java.awt.Dimension;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
//import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
//import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.LCSEventType;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;
import org.apache.log4j.Logger;

import org.mobicents.protocols.ss7.map.api.service.lsm.LocationEstimateType;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.LocationEstimateTypeEnumerated;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSClientType;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.LCSClientTypeEnumerated;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.PrivacyCheckRelatedActionEnumerated;
import org.mobicents.protocols.ss7.map.api.service.lsm.PrivacyCheckRelatedAction;
import org.mobicents.protocols.ss7.map.api.service.lsm.AreaType;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.AreaTypeEnumerated;

/**
 *
 * @author mrojo2@csc.com
 *
 */
public class TestMapLcsClientParamForm extends JDialog {
    private static final long serialVersionUID = 5428271328162943202L;

    private static Logger logger = Logger.getLogger(TestMapLcsClientParamForm.class);

    private TestMapLcsClientManMBean mapLcsClient;
    private JTextField cbAddress;
    private JTabbedPane tabbedPane;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JTextField tfCellId;
    private JTextField tfMcc;
    private JTextField tfMnc;
    private JTextField tfLac;
    private JTextField tfImsi;
    private JTextField tfMsisdn;
    private JTextField tfImei;
    private JTextField tfHgmlcAddress;
    private JTextField tfLcsReferenceNumber;
    // SLR Response
    private JTextField cbNaEsrdAddress;
    // PSL Request
    private JComboBox locEstimateType;
    private JComboBox lcsClientType;
    private JTextField lcsServiceTypeID;
    private JTextField tfDataCodingScheme;
    private JTextField tfCodeWordUSSDString;
    private JComboBox callSessionUnrelated;
    private JComboBox callSessionRelated;
    private JComboBox areaType;
    private JCheckBox moLrShortCircuitIndicator;
    private JTextField tfReptAmmount;
    private JTextField tfReptInterval;


    private JPanel createTab(JTabbedPane parent,String name) {
        JPanel tab = new JPanel();
        parent.addTab(name,null,tab,null);
        tab.setLayout(null);
        return tab;
    }

    private JPanel createSection(JPanel tab,String name,int y_pos, int height) {
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

    private JComboBox createCombo(JPanel section,String name, int y_pos) {

        JLabel label = new JLabel(name);
        label.setBounds(10, y_pos, 174, 14);
        section.add(label);

        JComboBox comboBox = new JComboBox();
        comboBox.setBounds(194, y_pos, 307, 20);
        section.add(comboBox);

        return comboBox;
    }

    private JTextField cretateTextField(JPanel section,String name, int y_pos) {

        JLabel label = new JLabel(name);
        label.setBounds(10, y_pos, 174, 14);
        section.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(194, y_pos, 307, 20);
        textField.setColumns(10);
        section.add(textField);

        return textField;
    }

    private JTextField cretateSmallTextField(JPanel section,String name, int x_pos, int y_pos) {

        int calculated_x = x_pos*10+(x_pos-1)*175;

        JLabel label = new JLabel(name);
        label.setBounds(calculated_x, y_pos, 75, 14);
        section.add(label);

        JTextField textField = new JTextField();
        textField.setBounds(calculated_x+75, y_pos, 75, 20);
        textField.setColumns(10);
        section.add(textField);

        return textField;
    }

    private void createLabel(JPanel section,String name, int y_pos) {

        JLabel label = new JLabel(name);
        label.setBounds(10, y_pos, 450, 14);
        section.add(label);
    }

    private JCheckBox createCheckbox(JPanel section,String name, int y_pos) {

        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setBounds(10, y_pos, 450, 20);
        section.add(checkBox);

        return checkBox;
    }

    public TestMapLcsClientParamForm(JFrame owner) {
        super(owner, true);

        int bottomOfPage=800;
        int lineSeparation = 22;
        int sectionSeparation = 5;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("MAP LCS test client settings");
        setBounds(100, 100, 640, bottomOfPage);
        getContentPane().setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 634, bottomOfPage-100);
        getContentPane().add(tabbedPane);

        // General TAB
        JPanel panel_gen = createTab(tabbedPane,"General");

        JPanel panel = createSection(panel_gen,"Parameters for AddressString creation",23,lineSeparation*5);
        cbAddressNature = createCombo(panel,"AddressNature",lineSeparation);
        cbNumberingPlan = createCombo(panel,"NumberingPlanType",lineSeparation*2);
        cbAddress = cretateTextField(panel,"NumberingPlan",lineSeparation*3);
        tfDataCodingScheme = cretateTextField(panel,"Data Coding Scheme(15-GSM7,72-UCS2)",lineSeparation*4);

        // SRI Request TAB
        JPanel panel_sri = createTab(tabbedPane,"SRI request");
        JPanel panelSriDetail = createSection(panel_sri,"MLC Number parameters",23,lineSeparation*2);
        createLabel(panelSriDetail,"AddressNature, NumberingPlan and NumberingPlanType from General tab",lineSeparation);


        // SLR Response TAB
        JPanel panel_slr_resp = createTab(tabbedPane,"SLR response");
        JPanel panelSlrRespDetail = createSection(panel_slr_resp,"NA-ESRD parameters for auto response",23,lineSeparation*3);
        cbNaEsrdAddress = cretateTextField(panelSlrRespDetail,"NA-ESRD address",lineSeparation);
        createLabel(panelSlrRespDetail,"AddressNature, NumberingPlanType from General tab",lineSeparation*2);

        // PSL Requested TAB
        JPanel panel_plr = createTab(tabbedPane,"PSL Request");

        JPanel panel_plr_1 = createSection(panel_plr,"PRS Loc Type",sectionSeparation,lineSeparation*3);
        locEstimateType = createCombo(panel_plr_1,"PRS Loc Estimate Type",lineSeparation);
        createLabel(panel_plr_1,"Deferred Loc Estimate Type set to null",lineSeparation*2);

        JPanel panel_3 = createSection(panel_plr,"Remaining Params",2*sectionSeparation+lineSeparation*3,lineSeparation*9);
        createLabel(panel_3,"MLC is created from General Tab data",lineSeparation);
        tfImsi = cretateTextField(panel_3,"IMSI",lineSeparation*2);
        tfMsisdn = cretateTextField(panel_3,"MSISDN",lineSeparation*3);
        tfImei = cretateTextField(panel_3,"IMEI",lineSeparation*4);
        tfLcsReferenceNumber = cretateTextField(panel_3,"LCS Reference Number",lineSeparation*5);
        tfHgmlcAddress = cretateTextField(panel_3,"H-GMLC Address",lineSeparation*6);
        lcsServiceTypeID = cretateTextField(panel_3,"LCS Service Type ID",lineSeparation*7);
        moLrShortCircuitIndicator = createCheckbox(panel_3,"moLr Short Circuit Indicator",lineSeparation*8);

        // LCS Client ID Partially harcoded
        JPanel panel_4 = createSection(panel_plr,"LCS Client ID",3*sectionSeparation+lineSeparation*12,lineSeparation*4);
        lcsClientType = createCombo(panel_4,"LCS Client Type",lineSeparation);
        createLabel(panel_4,"LCSClientExternalID, LCSClientInternalID, LCSClientName set to null",lineSeparation*2);
        createLabel(panel_4,"AddressString, APN, LCSRequestorID set to null",lineSeparation*3);

        JPanel panel_5 = createSection(panel_plr,"LCS Codeword",4*sectionSeparation+lineSeparation*16,lineSeparation*3);
        createLabel(panel_5,"Data Coding Scheme from General Tab",lineSeparation);
        tfCodeWordUSSDString = cretateTextField(panel_5,"USSD String",lineSeparation*2);

        JPanel panel_6 = createSection(panel_plr,"LCS Privacy Check",5*sectionSeparation+lineSeparation*19,lineSeparation*3);
        callSessionUnrelated = createCombo(panel_6,"Call Session Unrelated",lineSeparation);
        callSessionRelated = createCombo(panel_6,"Call Session Related",lineSeparation*2);

        JPanel panel_7 = createSection(panel_plr,"AreaEventInfo Area[0] Ocurrence=OneTimeEvent, intervalTime=10",6*sectionSeparation+lineSeparation*22,lineSeparation*4);
        areaType = createCombo(panel_7,"areaType",lineSeparation);
        tfMcc = cretateSmallTextField(panel_7,"MCC",1,lineSeparation*2);
        tfMnc = cretateSmallTextField(panel_7,"MNC",2,lineSeparation*2);
        tfLac = cretateSmallTextField(panel_7,"LAC",1,lineSeparation*3);
        tfCellId = cretateSmallTextField(panel_7,"Cell Id",2,lineSeparation*3);

        JPanel panel_8 = createSection(panel_plr,"PeriodicLDRInfo",7*sectionSeparation+lineSeparation*26,lineSeparation*2);
        tfReptAmmount = cretateSmallTextField(panel_8,"Rept Ammount",1,lineSeparation);
        tfReptInterval = cretateSmallTextField(panel_8,"Rept Interval",2,lineSeparation);


        /*
            null, // Cond round 2 - LMSI lmsi,
            null, // Cond round 2 - LCSPriority lcsPriority,
            null,  // Cond round 2 - LCSQoS lcsQoS,
            null, // Cond round 2 - MAPExtensionContainer extensionContainer,
            null, // Cond round 2 -SupportedGADShapes supportedGADShapes,
            null // Cond round 2 - ReportingPLMNList reportingPLMNList
        */

        JButton button = new JButton("Load default values for side A");
        button.setBounds(10, bottomOfPage-90, 246, 23);
        getContentPane().add(button);

        JButton button_3 = new JButton("Load default values for side B");
        button_3.setBounds(266, bottomOfPage-90, 255, 23);
        getContentPane().add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.setBounds(404, bottomOfPage-60, 117, 23);
        getContentPane().add(button_4);

        JButton button_2 = new JButton("Save");
        button_2.setBounds(180, bottomOfPage-60, 117, 23);
        getContentPane().add(button_2);

        JButton button_1 = new JButton("Reload");
        button_1.setBounds(10, bottomOfPage-60, 144, 23);
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

    public void setData(TestMapLcsClientManMBean mapLcsClient) {
        this.mapLcsClient = mapLcsClient;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.mapLcsClient.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.mapLcsClient.getNumberingPlanType());
        this.tfDataCodingScheme.setText(this.mapLcsClient.getDataCodingScheme().toString());
        // PSL tab
        //SRI tab
        cbAddress.setText(this.mapLcsClient.getNumberingPlan());

        //PSL Request tab
        this.tfCellId.setText(this.mapLcsClient.getCellId().toString());
        this.tfHgmlcAddress.setText(this.mapLcsClient.getHGMLCAddress());
        this.tfImei.setText(this.mapLcsClient.getIMEI());
        this.tfImsi.setText(this.mapLcsClient.getIMSI());
        this.tfLac.setText(this.mapLcsClient.getLAC().toString());
        this.tfLcsReferenceNumber.setText(this.mapLcsClient.getLCSReferenceNumber().toString());
        this.tfMcc.setText(this.mapLcsClient.getMCC().toString());
        this.tfMnc.setText(this.mapLcsClient.getMNC().toString());
        this.tfMsisdn.setText(this.mapLcsClient.getMSISDN());
        this.lcsServiceTypeID.setText(this.mapLcsClient.getLcsServiceTypeID().toString());
        this.moLrShortCircuitIndicator.setSelected(this.mapLcsClient.getMoLrShortCircuitIndicator());
        M3uaForm.setEnumeratedBaseComboBox(locEstimateType, this.mapLcsClient.getLocEstimateType());
        M3uaForm.setEnumeratedBaseComboBox(lcsClientType, this.mapLcsClient.getLcsClientType());
        this.tfCodeWordUSSDString.setText(this.mapLcsClient.getCodeWordUSSDString().toString());
        M3uaForm.setEnumeratedBaseComboBox(callSessionUnrelated, this.mapLcsClient.getCallSessionUnrelated());
        M3uaForm.setEnumeratedBaseComboBox(callSessionRelated, this.mapLcsClient.getCallSessionRelated());
        M3uaForm.setEnumeratedBaseComboBox(areaType,this.mapLcsClient.getAreaType());
        this.tfReptAmmount.setText(this.mapLcsClient.getReportingAmmount().toString());
        this.tfReptInterval.setText(this.mapLcsClient.getReportingInterval().toString());

        //SLR Response tab
        cbNaEsrdAddress.setText(this.mapLcsClient.getNaESRDAddress());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        this.tfDataCodingScheme.setText("15");
        //SRI Request tab
        cbAddress.setText("12345678");

        //PSL Request tab
        this.tfCellId.setText("222");
        this.tfHgmlcAddress.setText("0.0.0.0");
        this.tfImei.setText("5555544444");
        this.tfImsi.setText("5555544444");
        this.tfLac.setText("1111");
        this.tfLcsReferenceNumber.setText("111");
        this.tfMcc.setText("250");
        this.tfMnc.setText("123");
        this.tfMsisdn.setText("3333344444");
        this.lcsServiceTypeID.setText("5");
        this.moLrShortCircuitIndicator.setSelected(false);
        M3uaForm.setEnumeratedBaseComboBox(locEstimateType, new LocationEstimateTypeEnumerated(LocationEstimateType.currentLocation.getType()));
        M3uaForm.setEnumeratedBaseComboBox(lcsClientType, new LCSClientTypeEnumerated(LCSClientType.emergencyServices.getType()));
        this.tfCodeWordUSSDString.setText("CW");
        M3uaForm.setEnumeratedBaseComboBox(callSessionUnrelated, new PrivacyCheckRelatedActionEnumerated(PrivacyCheckRelatedAction.allowedWithoutNotification.getAction()));
        M3uaForm.setEnumeratedBaseComboBox(callSessionRelated, new PrivacyCheckRelatedActionEnumerated(PrivacyCheckRelatedAction.allowedWithNotification.getAction()));
        M3uaForm.setEnumeratedBaseComboBox(areaType,new AreaTypeEnumerated(AreaType.countryCode.getType()));
        this.tfReptAmmount.setText("10");
        this.tfReptInterval.setText("10");

        //SLR Response tab
        cbNaEsrdAddress.setText("11114444");

    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        this.mapLcsClient.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.mapLcsClient.setNumberingPlanType((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        this.mapLcsClient.setLocEstimateType((LocationEstimateTypeEnumerated) locEstimateType.getSelectedItem());
        //SRI tab
        this.mapLcsClient.setNumberingPlan(cbAddress.getText());

        //PSL tab
        try {
            this.mapLcsClient.setLAC(Integer.valueOf(this.tfLac.getText()));
            this.mapLcsClient.setLCSReferenceNumber(Integer.valueOf(this.tfLcsReferenceNumber.getText()));
            this.mapLcsClient.setMCC(Integer.valueOf(this.tfMcc.getText()));
            this.mapLcsClient.setMNC(Integer.valueOf(this.tfMnc.getText()));
            this.mapLcsClient.setCellId(Integer.valueOf(this.tfCellId.getText()));
            this.mapLcsClient.setLcsServiceTypeID(Integer.valueOf(this.lcsServiceTypeID.getText()));
            this.mapLcsClient.setReportingAmmount(Integer.valueOf(this.tfReptAmmount.getText()));
            this.mapLcsClient.setReportingInterval(Integer.valueOf(this.tfReptInterval.getText()));
            this.mapLcsClient.setDataCodingScheme(Integer.valueOf(this.tfDataCodingScheme.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "value: " + e.toString() + "Not valid, must be a number");
            return false;
        }
        this.mapLcsClient.setHGMLCAddress(this.tfHgmlcAddress.getText());
        this.mapLcsClient.setIMEI(this.tfImei.getText());
        this.mapLcsClient.setIMSI(this.tfImsi.getText());
        this.mapLcsClient.setMSISDN(this.tfMsisdn.getText());
        this.mapLcsClient.setMoLrShortCircuitIndicator(this.moLrShortCircuitIndicator.isSelected());
        this.mapLcsClient.setLcsClientType((LCSClientTypeEnumerated) lcsClientType.getSelectedItem());
        this.mapLcsClient.setCodeWordUSSDString(this.tfCodeWordUSSDString.getText());
        this.mapLcsClient.setCallSessionUnrelated((PrivacyCheckRelatedActionEnumerated)callSessionUnrelated.getSelectedItem());
        this.mapLcsClient.setCallSessionRelated((PrivacyCheckRelatedActionEnumerated)callSessionRelated.getSelectedItem());
        this.mapLcsClient.setAreaType((AreaTypeEnumerated)areaType.getSelectedItem());

        //SLR Response tab
        this.mapLcsClient.setNaESRDAddress(cbNaEsrdAddress.getText());

        return true;
    }
}