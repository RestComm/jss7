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
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
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
        label_3.setBounds(10, 84, 266, 14);
        panelSriDetail.add(label_3);

        JPanel panel_slr = new JPanel();
        tabbedPane.addTab("SLR request", panel_slr);
        panel_slr.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 24, 511, 104);
        panel_slr.add(panel_1);

        JLabel lblParametersForNetworknodenumber = new JLabel("Network Node Number parameters");
        lblParametersForNetworknodenumber.setBounds(10, 11, 266, 14);
        panel_1.add(lblParametersForNetworknodenumber);

        JLabel lblNetworknodenumberaddress = new JLabel("NetworkNodeNumberAddress");
        lblNetworknodenumberaddress.setBounds(10, 45, 174, 14);
        panel_1.add(lblNetworknodenumberaddress);

        tfNetworkNodeAddress = new JTextField();
        tfNetworkNodeAddress.setColumns(10);
        tfNetworkNodeAddress.setBounds(194, 42, 307, 20);
        panel_1.add(tfNetworkNodeAddress);

        JLabel lblAddressnatureNumberingplantypeFrom = new JLabel("AddressNature, NumberingPlanType from General tab");
        lblAddressnatureNumberingplantypeFrom.setBounds(10, 83, 266, 14);
        panel_1.add(lblAddressnatureNumberingplantypeFrom);

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
        //SLR tab
        tfNetworkNodeAddress.setText(this.mapLcsClient.getNetworkNodeNumberAddress());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        //SRI tab
        cbAddress.setText("12345678");
        //SLR tab
        tfNetworkNodeAddress.setText("4444455555");
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
        return true;
    }
}