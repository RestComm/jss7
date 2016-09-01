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

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.LCSEventType;
import org.mobicents.protocols.ss7.tools.simulator.tests.lcs.TestMapLcsClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;
import org.apache.log4j.Logger;

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
    private JTextField tfNetworkNodeAddress;
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
    private JTextField tfAgeOfLocationEstimate;
    private JComboBox cbLcsEvent;
    private JTextField tfLcsReferenceNumber;
    // SLR Response
    private JTextField cbNaEsrdAddress;

    public TestMapLcsClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("MAP LCS test client settings");
        setBounds(100, 100, 640, 584);
        getContentPane().setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 634, 465);
        getContentPane().add(tabbedPane);

        JPanel panel_gen = new JPanel();
        tabbedPane.addTab("General", null, panel_gen, null);
        panel_gen.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBounds(26, 23, 511, 94);
        panel_gen.add(panel);

        JLabel label = new JLabel("Parameters for AddressString creation");
        label.setBounds(10, 0, 266, 14);
        panel.add(label);

        JLabel label_1 = new JLabel("AddressNature");
        label_1.setBounds(10, 28, 174, 14);
        panel.add(label_1);

        JLabel label_2 = new JLabel("NumberingPlan");
        label_2.setBounds(10, 59, 174, 14);
        panel.add(label_2);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(194, 25, 307, 20);
        panel.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(194, 56, 307, 20);
        panel.add(cbNumberingPlan);

        JPanel panel_sri = new JPanel();
        panel_sri.setLayout(null);
        tabbedPane.addTab("SRI request", null, panel_sri, null);

        JPanel panelSriDetail = new JPanel();
        panelSriDetail.setLayout(null);
        panelSriDetail.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelSriDetail.setBounds(10, 24, 511, 109);
        panel_sri.add(panelSriDetail);

        JLabel label_10 = new JLabel("MLC Number parameters");
        label_10.setBounds(10, 11, 266, 14);
        panelSriDetail.add(label_10);

        JLabel label_13 = new JLabel("NumberingPlan");
        label_13.setBounds(10, 45, 174, 14);
        panelSriDetail.add(label_13);

        cbAddress = new JTextField();
        cbAddress.setBounds(194, 42, 307, 20);
        cbAddress.setColumns(10);
        panelSriDetail.add(cbAddress);

        JLabel label_3 = new JLabel("AddressNature, NumberingPlanType from General tab");
        label_3.setBounds(10, 84, 350, 14);
        panelSriDetail.add(label_3);

        JPanel panel_slr = new JPanel();
        tabbedPane.addTab("SLR request", panel_slr);
        panel_slr.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 24, 511, 82);
        panel_slr.add(panel_1);

        JLabel lblNetworknodenumberaddress = new JLabel("Network Node Number address");
        lblNetworknodenumberaddress.setBounds(10, 11, 174, 14);
        panel_1.add(lblNetworknodenumberaddress);

        tfNetworkNodeAddress = new JTextField();
        tfNetworkNodeAddress.setColumns(10);
        tfNetworkNodeAddress.setBounds(194, 8, 307, 20);
        panel_1.add(tfNetworkNodeAddress);

        JLabel lblAddressnatureNumberingplantypeFrom = new JLabel("AddressNature, NumberingPlanType from General tab");
        lblAddressnatureNumberingplantypeFrom.setBounds(10, 83, 350, 14);
        panel_1.add(lblAddressnatureNumberingplantypeFrom);

        JLabel lblMsisdn = new JLabel("MSISDN");
        lblMsisdn.setBounds(10, 39, 174, 14);
        panel_1.add(lblMsisdn);

        tfMsisdn = new JTextField();
        tfMsisdn.setColumns(10);
        tfMsisdn.setBounds(194, 36, 307, 20);
        panel_1.add(tfMsisdn);

        JLabel label_5 = new JLabel("AddressNature, NumberingPlanType from General tab");
        label_5.setBounds(10, 64, 350, 14);
        panel_1.add(label_5);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2.setBounds(8, 324, 511, 104);
        panel_slr.add(panel_2);

        JLabel lblCellGlobalId = new JLabel("Cell Global Id Or Service Area Id or LAI parameters");
        lblCellGlobalId.setBounds(10, 11, 282, 14);
        panel_2.add(lblCellGlobalId);

        JLabel lblCellid = new JLabel("CellId");
        lblCellid.setBounds(20, 39, 34, 14);
        panel_2.add(lblCellid);

        tfCellId = new JTextField();
        tfCellId.setColumns(10);
        tfCellId.setBounds(60, 36, 155, 20);
        panel_2.add(tfCellId);

        JLabel lblMcc = new JLabel("MCC");
        lblMcc.setBounds(243, 67, 34, 14);
        panel_2.add(lblMcc);

        tfMcc = new JTextField();
        tfMcc.setColumns(10);
        tfMcc.setBounds(283, 64, 155, 20);
        panel_2.add(tfMcc);

        JLabel lblMnc = new JLabel("MNC");
        lblMnc.setBounds(20, 67, 34, 14);
        panel_2.add(lblMnc);

        tfMnc = new JTextField();
        tfMnc.setColumns(10);
        tfMnc.setBounds(60, 64, 155, 20);
        panel_2.add(tfMnc);

        tfLac = new JTextField();
        tfLac.setColumns(10);
        tfLac.setBounds(283, 36, 155, 20);
        panel_2.add(tfLac);

        JLabel lblLac = new JLabel("LAC");
        lblLac.setBounds(243, 39, 34, 14);
        panel_2.add(lblLac);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_3.setBounds(10, 117, 511, 193);
        panel_slr.add(panel_3);

        JLabel lblImsi = new JLabel("IMSI");
        lblImsi.setBounds(10, 11, 55, 14);
        panel_3.add(lblImsi);

        tfImsi = new JTextField();
        tfImsi.setColumns(10);
        tfImsi.setBounds(194, 8, 307, 20);
        panel_3.add(tfImsi);

        JLabel lblImei = new JLabel("IMEI");
        lblImei.setBounds(10, 37, 55, 14);
        panel_3.add(lblImei);

        tfImei = new JTextField();
        tfImei.setColumns(10);
        tfImei.setBounds(194, 34, 307, 20);
        panel_3.add(tfImei);

        JLabel lblHgmlcAddress = new JLabel("H-GMLC Address");
        lblHgmlcAddress.setBounds(10, 65, 147, 14);
        panel_3.add(lblHgmlcAddress);

        tfHgmlcAddress = new JTextField();
        tfHgmlcAddress.setColumns(10);
        tfHgmlcAddress.setBounds(194, 62, 307, 20);
        panel_3.add(tfHgmlcAddress);

        JLabel lbAgeOfLocationEstimate = new JLabel("Age Of Location Estimate");
        lbAgeOfLocationEstimate.setBounds(10, 93, 147, 14);
        panel_3.add(lbAgeOfLocationEstimate);

        tfAgeOfLocationEstimate = new JTextField();
        tfAgeOfLocationEstimate.setColumns(10);
        tfAgeOfLocationEstimate.setBounds(194, 90, 307, 20);
        panel_3.add(tfAgeOfLocationEstimate);

        JLabel lblLcsReferenceNumber = new JLabel("LCS Reference Number");
        lblLcsReferenceNumber.setBounds(10, 121, 147, 14);
        panel_3.add(lblLcsReferenceNumber);

        tfLcsReferenceNumber = new JTextField();
        tfLcsReferenceNumber.setColumns(10);
        tfLcsReferenceNumber.setBounds(194, 118, 307, 20);
        panel_3.add(tfLcsReferenceNumber);

        JLabel lblLcsEvent = new JLabel("LCS Event Type");
        lblLcsEvent.setBounds(10, 153, 147, 14);
        panel_3.add(lblLcsEvent);

        cbLcsEvent = new JComboBox();
        cbLcsEvent.setBounds(194, 149, 307, 20);
        panel_3.add(cbLcsEvent);

        // SLR Response
        JPanel panel_slr_resp = new JPanel();
        panel_slr_resp.setLayout(null);
        tabbedPane.addTab("SLR response", null, panel_slr_resp, null);

        JPanel panelSlrRespDetail = new JPanel();
        panelSlrRespDetail.setLayout(null);
        panelSlrRespDetail.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelSlrRespDetail.setBounds(10, 24, 511, 109);
        panel_slr_resp.add(panelSlrRespDetail);

        JLabel lblNaEsrdParameters = new JLabel("NA-ESRD parameters");
        lblNaEsrdParameters.setBounds(10, 11, 266, 14);
        panelSlrRespDetail.add(lblNaEsrdParameters);

        JLabel lblNaEsrdAddress = new JLabel("NA-ESRD address");
        lblNaEsrdAddress.setBounds(10, 45, 174, 14);
        panelSlrRespDetail.add(lblNaEsrdAddress);

        cbNaEsrdAddress = new JTextField();
        cbNaEsrdAddress.setBounds(194, 42, 307, 20);
        cbNaEsrdAddress.setColumns(10);
        panelSlrRespDetail.add(cbNaEsrdAddress);

        JLabel labelSLRResp = new JLabel("AddressNature, NumberingPlanType from General tab");
        labelSLRResp.setBounds(10, 84, 350, 14);
        panelSlrRespDetail.add(labelSLRResp);



        JPanel panel_plr = new JPanel();
        panel_plr.setLayout(null);
        tabbedPane.addTab("PLR request", null, panel_plr, null);

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
        //SRI tab
        cbAddress.setText(this.mapLcsClient.getNumberingPlan());
        //SLR Request tab
        tfNetworkNodeAddress.setText(this.mapLcsClient.getNetworkNodeNumberAddress());
        this.tfAgeOfLocationEstimate.setText(this.mapLcsClient.getAgeOfLocationEstimate().toString());
        this.tfCellId.setText(this.mapLcsClient.getCellId().toString());
        this.tfHgmlcAddress.setText(this.mapLcsClient.getHGMLCAddress());
        this.tfImei.setText(this.mapLcsClient.getIMEI());
        this.tfImsi.setText(this.mapLcsClient.getIMSI());
        this.tfLac.setText(this.mapLcsClient.getLAC().toString());
        this.tfLcsReferenceNumber.setText(this.mapLcsClient.getLCSReferenceNumber().toString());
        this.tfMcc.setText(this.mapLcsClient.getMCC().toString());
        this.tfMnc.setText(this.mapLcsClient.getMNC().toString());
        this.tfMsisdn.setText(this.mapLcsClient.getMSISDN());
        M3uaForm.setEnumeratedBaseComboBox(this.cbLcsEvent, this.mapLcsClient.getLCSEventType());
        //SLR Response tab
        cbNaEsrdAddress.setText(this.mapLcsClient.getNaESRDAddress());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        //SRI Request tab
        cbAddress.setText("12345678");
        //SLR Request tab
        tfNetworkNodeAddress.setText("4444455555");
        this.tfAgeOfLocationEstimate.setText("100");
        this.tfCellId.setText("222");
        this.tfHgmlcAddress.setText("0.0.0.0");
        this.tfImei.setText("5555544444");
        this.tfImsi.setText("5555544444");
        this.tfLac.setText("1111");
        this.tfLcsReferenceNumber.setText("111");
        this.tfMcc.setText("250");
        this.tfMnc.setText("123");
        this.tfMsisdn.setText("3333344444");
        M3uaForm.setEnumeratedBaseComboBox(this.cbLcsEvent, new LCSEventType(LCSEvent.emergencyCallOrigination.getEvent()));
        //SLR Response tab
        cbNaEsrdAddress.setText("11114444");

    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        this.mapLcsClient.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.mapLcsClient.setNumberingPlanType((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        //SRI tab
        this.mapLcsClient.setNumberingPlan(cbAddress.getText());
        //SLR tab
        this.mapLcsClient.setNetworkNodeNumberAddress(tfNetworkNodeAddress.getText());
        try {
            this.mapLcsClient.setAgeOfLocationEstimate(Integer.valueOf(this.tfAgeOfLocationEstimate.getText()));
            this.mapLcsClient.setLAC(Integer.valueOf(this.tfLac.getText()));
            this.mapLcsClient.setLCSReferenceNumber(Integer.valueOf(this.tfLcsReferenceNumber.getText()));
            this.mapLcsClient.setMCC(Integer.valueOf(this.tfMcc.getText()));
            this.mapLcsClient.setMNC(Integer.valueOf(this.tfMnc.getText()));
            this.mapLcsClient.setCellId(Integer.valueOf(this.tfCellId.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "value: " + e.toString() + "Not valid, must be a number");
            return false;
        }
        this.mapLcsClient.setHGMLCAddress(this.tfHgmlcAddress.getText());
        this.mapLcsClient.setIMEI(this.tfImei.getText());
        this.mapLcsClient.setIMSI(this.tfImsi.getText());
        this.mapLcsClient.setMSISDN(this.tfMsisdn.getText());
        this.mapLcsClient.setLCSEventType((LCSEventType) cbLcsEvent.getSelectedItem());

        //SLR Response tab
        this.mapLcsClient.setNaESRDAddress(cbNaEsrdAddress.getText());

        return true;
    }
}