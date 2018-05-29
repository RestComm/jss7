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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import org.apache.log4j.Logger;
import org.restcomm.protocols.ss7.map.api.primitives.AddressNature;
import org.restcomm.protocols.ss7.map.api.primitives.NumberingPlan;
import org.restcomm.protocols.ss7.map.api.service.lsm.LCSEvent;
import org.restcomm.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.restcomm.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.LCSEventType;
import org.restcomm.protocols.ss7.tools.simulator.tests.lcs.TestLcsClientManMBean;
import org.restcomm.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 * @author <a href="mailto:fernando.mendioroz@gmail.com"> Fernando Mendioroz </a>
 * @author <a href="mailto:falonso@csc.om"> Fernando Alonso </a>
 */
public class TestLcsClientParamForm extends JDialog {

    private static final long serialVersionUID = 5428271328162943202L;

    private static Logger logger = Logger.getLogger(TestLcsClientParamForm.class);

    private TestLcsClientManMBean mapLcsClient;
    private JTextField cbNaEsrdAddress;
    private JTabbedPane tabbedPane;
    private JTextField tfNetworkNodeNumberAddress;
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
    private JLabel tfNetworkNodeAddress;
    private JLabel tfNetworkNodeAddressSRI;

    public TestLcsClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("MAP LCS test server settings");
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
        panel.setBounds(26, 23, 511, 120);
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

        JLabel lblNetworknodenumberaddress = new JLabel("NetworkNodeNumberAddress");
        lblNetworknodenumberaddress.setBounds(10, 87, 174, 14);
        panel.add(lblNetworknodenumberaddress);

        tfNetworkNodeNumberAddress = new JTextField();
        tfNetworkNodeNumberAddress.setColumns(10);
        tfNetworkNodeNumberAddress.setBounds(194, 84, 307, 20);
        panel.add(tfNetworkNodeNumberAddress);

        JPanel panel_slr = new JPanel();
        tabbedPane.addTab("SRI response", panel_slr);
        panel_slr.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 24, 511, 104);
        panel_slr.add(panel_1);

        JLabel lblParametersForNetworknodenumber = new JLabel("Network Node Number parameters for auto response");
        lblParametersForNetworknodenumber.setBounds(10, 11, 350, 14);
        panel_1.add(lblParametersForNetworknodenumber);

        JLabel lblNetworknodenumberaddressSRI = new JLabel("Network Node Number address");
        lblNetworknodenumberaddressSRI.setBounds(10, 30, 174, 14);
        panel_1.add(lblNetworknodenumberaddressSRI);

        tfNetworkNodeAddressSRI = new JLabel(""); //filled at reload
        tfNetworkNodeAddressSRI.setBounds(194, 27, 307, 20);
        panel_1.add(tfNetworkNodeAddressSRI);

        JLabel lblAddressnatureNumberingplantypeFrom = new JLabel("NwNodeNumberAddress, AddressNature, NumberingPlanType from General tab");
        lblAddressnatureNumberingplantypeFrom.setBounds(10, 83, 450, 14);
        panel_1.add(lblAddressnatureNumberingplantypeFrom);


        JPanel panel_slr_req = new JPanel();
        tabbedPane.addTab("SLR request", panel_slr_req);
        panel_slr_req.setLayout(null);

        JPanel panel_1A = new JPanel();
        panel_1A.setLayout(null);
        panel_1A.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1A.setBounds(10, 24, 511, 82);
        panel_slr_req.add(panel_1A);

        JLabel lblNetworknodenumberaddressA = new JLabel("Network Node Number address");
        lblNetworknodenumberaddressA.setBounds(10, 11, 174, 14);
        panel_1A.add(lblNetworknodenumberaddressA);

        tfNetworkNodeAddress = new JLabel(""); //filled at reload
        tfNetworkNodeAddress.setBounds(194, 8, 307, 20);
        panel_1A.add(tfNetworkNodeAddress);

        JLabel lblAddressnatureNumberingplantypeFromA = new JLabel("NwNodeNumberAddress, AddressNature, NumberingPlanType from General tab");
        lblAddressnatureNumberingplantypeFromA.setBounds(10, 83, 450, 14);
        panel_1A.add(lblAddressnatureNumberingplantypeFromA);

        JLabel lblMsisdn = new JLabel("MSISDN");
        lblMsisdn.setBounds(10, 39, 174, 14);
        panel_1A.add(lblMsisdn);

        tfMsisdn = new JTextField();
        tfMsisdn.setColumns(10);
        tfMsisdn.setBounds(194, 36, 307, 20);
        panel_1A.add(tfMsisdn);

        JLabel label_5 = new JLabel("NwNodeNumberAddress, AddressNature, NumberingPlanType from General tab");
        label_5.setBounds(10, 64, 450, 14);
        panel_1A.add(label_5);

        JPanel panel_2A = new JPanel();
        panel_2A.setLayout(null);
        panel_2A.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2A.setBounds(8, 324, 511, 104);
        panel_slr_req.add(panel_2A);

        JLabel lblCellGlobalId = new JLabel("Cell Global Id Or Service Area Id or LAI parameters");
        lblCellGlobalId.setBounds(10, 11, 300, 14);
        panel_2A.add(lblCellGlobalId);

        JLabel lblCellid = new JLabel("CellId");
        lblCellid.setBounds(20, 39, 34, 14);
        panel_2A.add(lblCellid);

        tfCellId = new JTextField();
        tfCellId.setColumns(10);
        tfCellId.setBounds(60, 36, 155, 20);
        panel_2A.add(tfCellId);

        JLabel lblMcc = new JLabel("MCC");
        lblMcc.setBounds(243, 67, 34, 14);
        panel_2A.add(lblMcc);

        tfMcc = new JTextField();
        tfMcc.setColumns(10);
        tfMcc.setBounds(283, 64, 155, 20);
        panel_2A.add(tfMcc);

        JLabel lblMnc = new JLabel("MNC");
        lblMnc.setBounds(20, 67, 34, 14);
        panel_2A.add(lblMnc);

        tfMnc = new JTextField();
        tfMnc.setColumns(10);
        tfMnc.setBounds(60, 64, 155, 20);
        panel_2A.add(tfMnc);

        tfLac = new JTextField();
        tfLac.setColumns(10);
        tfLac.setBounds(283, 36, 155, 20);
        panel_2A.add(tfLac);

        JLabel lblLac = new JLabel("LAC");
        lblLac.setBounds(243, 39, 34, 14);
        panel_2A.add(lblLac);

        JPanel panel_3A = new JPanel();
        panel_3A.setLayout(null);
        panel_3A.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_3A.setBounds(10, 117, 511, 193);
        panel_slr_req.add(panel_3A);

        JLabel lblImsi = new JLabel("IMSI");
        lblImsi.setBounds(10, 11, 55, 14);
        panel_3A.add(lblImsi);

        tfImsi = new JTextField();
        tfImsi.setColumns(10);
        tfImsi.setBounds(194, 8, 307, 20);
        panel_3A.add(tfImsi);

        JLabel lblImei = new JLabel("IMEI");
        lblImei.setBounds(10, 37, 55, 14);
        panel_3A.add(lblImei);

        tfImei = new JTextField();
        tfImei.setColumns(10);
        tfImei.setBounds(194, 34, 307, 20);
        panel_3A.add(tfImei);

        JLabel lblHgmlcAddress = new JLabel("H-GMLC Address");
        lblHgmlcAddress.setBounds(10, 65, 147, 14);
        panel_3A.add(lblHgmlcAddress);

        tfHgmlcAddress = new JTextField();
        tfHgmlcAddress.setColumns(10);
        tfHgmlcAddress.setBounds(194, 62, 307, 20);
        panel_3A.add(tfHgmlcAddress);

        JLabel lbAgeOfLocationEstimate = new JLabel("Age Of Location Estimate");
        lbAgeOfLocationEstimate.setBounds(10, 93, 147, 14);
        panel_3A.add(lbAgeOfLocationEstimate);

        tfAgeOfLocationEstimate = new JTextField();
        tfAgeOfLocationEstimate.setColumns(10);
        tfAgeOfLocationEstimate.setBounds(194, 90, 307, 20);
        panel_3A.add(tfAgeOfLocationEstimate);

        JLabel lblLcsReferenceNumber = new JLabel("LCS Reference Number");
        lblLcsReferenceNumber.setBounds(10, 121, 147, 14);
        panel_3A.add(lblLcsReferenceNumber);

        tfLcsReferenceNumber = new JTextField();
        tfLcsReferenceNumber.setColumns(10);
        tfLcsReferenceNumber.setBounds(194, 118, 307, 20);
        panel_3A.add(tfLcsReferenceNumber);

        JLabel lblLcsEvent = new JLabel("LCS Event Type");
        lblLcsEvent.setBounds(10, 153, 147, 14);
        panel_3A.add(lblLcsEvent);

        cbLcsEvent = new JComboBox();
        cbLcsEvent.setBounds(194, 149, 307, 20);
        panel_3A.add(cbLcsEvent);

        JPanel panel_plr = new JPanel();
        panel_plr.setLayout(null);
        tabbedPane.addTab("PLR ", null, panel_plr, null);

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

    public void setData(TestLcsClientManMBean mapLcsClient) {
        this.mapLcsClient = mapLcsClient;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.mapLcsClient.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.mapLcsClient.getNumberingPlanType());
        //General tab
        tfNetworkNodeNumberAddress.setText(this.mapLcsClient.getNetworkNodeNumber());
        //SRI tab
        tfNetworkNodeAddressSRI.setText(this.mapLcsClient.getNetworkNodeNumber());
        /*
        //SLR Response tab
        cbNaEsrdAddress.setText(this.mapLcsClient.getNaESRDAddress());
        */
        //SLR Request tab
        tfNetworkNodeAddress.setText(this.mapLcsClient.getNetworkNodeNumber());
        this.tfAgeOfLocationEstimate.setText(this.mapLcsClient.getAgeOfLocationEstimate().toString());
        this.tfHgmlcAddress.setText(this.mapLcsClient.getHGMLCAddress());
        this.tfImsi.setText(this.mapLcsClient.getIMSI());
        this.tfMsisdn.setText(this.mapLcsClient.getMSISDN());
        this.tfImei.setText(this.mapLcsClient.getIMEI());
        this.tfMcc.setText(this.mapLcsClient.getMCC().toString());
        this.tfMnc.setText(this.mapLcsClient.getMNC().toString());
        this.tfLac.setText(this.mapLcsClient.getLAC().toString());
        this.tfCellId.setText(this.mapLcsClient.getCellId().toString());
        this.tfLcsReferenceNumber.setText(this.mapLcsClient.getLCSReferenceNumber().toString());
        M3uaForm.setEnumeratedBaseComboBox(this.cbLcsEvent, this.mapLcsClient.getLCSEventType());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        String networkNodeNumberAddressSideA = "5980482000";
        //General tab
        tfNetworkNodeNumberAddress.setText(networkNodeNumberAddressSideA);
        //SRI tab
        tfNetworkNodeAddressSRI.setText(networkNodeNumberAddressSideA);

        //SLR request tab
        tfNetworkNodeAddress.setText(networkNodeNumberAddressSideA);
        this.tfAgeOfLocationEstimate.setText("1");
        this.tfCellId.setText("222");
        this.tfHgmlcAddress.setText("200.10.0.1");
        this.tfImsi.setText("748010192837465");
        this.tfMsisdn.setText("59899077937");
        this.tfImei.setText("354449063537030");
        this.tfLac.setText("79010");
        this.tfLcsReferenceNumber.setText("921");
        this.tfMcc.setText("748");
        this.tfMnc.setText("01");
        M3uaForm.setEnumeratedBaseComboBox(this.cbLcsEvent, new LCSEventType(LCSEvent.emergencyCallOrigination.getEvent()));

    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        this.mapLcsClient.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.mapLcsClient.setNumberingPlanType((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        //General tab
        this.mapLcsClient.setNetworkNodeNumber(tfNetworkNodeNumberAddress.getText());
        //SRI tab

        //SLR request tab
        try {
            this.mapLcsClient.setAgeOfLocationEstimate(Integer.valueOf(this.tfAgeOfLocationEstimate.getText()));
            this.mapLcsClient.setLCSReferenceNumber(Integer.valueOf(this.tfLcsReferenceNumber.getText()));
            this.mapLcsClient.setMCC(Integer.valueOf(this.tfMcc.getText()));
            this.mapLcsClient.setMNC(Integer.valueOf(this.tfMnc.getText()));
            this.mapLcsClient.setLAC(Integer.valueOf(this.tfLac.getText()));
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

        return true;
    }
}
