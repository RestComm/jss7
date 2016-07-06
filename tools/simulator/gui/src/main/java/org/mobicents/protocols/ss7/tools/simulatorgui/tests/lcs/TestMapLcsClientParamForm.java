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
    private JComboBox cbMapProtocolVersion;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JComboBox cbAddressNature2;
    private JComboBox cbNumberingPlan2;
    private JTextField cbAddress;
    private JTabbedPane tabbedPane;

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

        JLabel label = new JLabel("MAP protocol version");
        label.setBounds(10, 14, 204, 14);
        panel_gen.add(label);

        cbMapProtocolVersion = new JComboBox();
        cbMapProtocolVersion.setBounds(266, 11, 255, 20);
        panel_gen.add(cbMapProtocolVersion);

        JPanel panel_sri = new JPanel();
        panel_sri.setLayout(null);
        tabbedPane.addTab("SRI request", null, panel_sri, null);

        JPanel panelSriDetail = new JPanel();
        panelSriDetail.setLayout(null);
        panelSriDetail.setBorder(new LineBorder(new Color(0, 0, 0)));
        panelSriDetail.setBounds(10, 26, 511, 125);
        panel_sri.add(panelSriDetail);

        JLabel label_10 = new JLabel("Parameters for mlc");
        label_10.setBounds(10, 0, 266, 14);
        panelSriDetail.add(label_10);

        JLabel label_11 = new JLabel("AddressNature");
        label_11.setBounds(10, 28, 174, 14);
        panelSriDetail.add(label_11);

        JLabel label_12 = new JLabel("NumberingPlanType");
        label_12.setBounds(10, 59, 174, 14);
        panelSriDetail.add(label_12);

        JLabel label_13 = new JLabel("NumberingPlan");
        label_13.setBounds(10, 90, 174, 14);
        panelSriDetail.add(label_13);

        cbAddressNature2 = new JComboBox();
        cbAddressNature2.setBounds(194, 25, 307, 20);
        panelSriDetail.add(cbAddressNature2);

        cbNumberingPlan2 = new JComboBox();
        cbNumberingPlan2.setBounds(194, 56, 307, 20);
        panelSriDetail.add(cbNumberingPlan2);

        cbAddress = new JTextField();
        cbAddress.setBounds(194, 87, 307, 20);
        cbAddress.setColumns(10);
        panelSriDetail.add(cbAddress);

        JPanel panel_slr = new JPanel();
        tabbedPane.addTab("SLR request", panel_slr);
        panel_slr.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBounds(10, 26, 511, 94);
        panel_slr.add(panel);

        JLabel label_7 = new JLabel("Parameters for AddressString creation");
        label_7.setBounds(10, 0, 266, 14);
        panel.add(label_7);

        JLabel label_8 = new JLabel("AddressNature");
        label_8.setBounds(10, 28, 174, 14);
        panel.add(label_8);

        JLabel label_9 = new JLabel("NumberingPlan");
        label_9.setBounds(10, 59, 174, 14);
        panel.add(label_9);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(194, 25, 307, 20);
        panel.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(194, 56, 307, 20);
        panel.add(cbNumberingPlan);

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
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature2, this.mapLcsClient.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan2, this.mapLcsClient.getNumberingPlanType());

        cbAddress.setText(this.mapLcsClient.getNumberingPlan());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature2,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan2, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));

        cbAddress.setText("12345678");
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        // check which message viewed as it overrides
        // we can either make certain params general or have specific ones per message
        //
        logger.debug("Selected: "+tabbedPane.getSelectedIndex());
        if ( tabbedPane.getSelectedIndex() == 1) {
            this.mapLcsClient.setAddressNature((AddressNatureType) cbAddressNature2.getSelectedItem());
            this.mapLcsClient.setNumberingPlanType((NumberingPlanMapType) cbNumberingPlan2.getSelectedItem());
        }
        else {
            this.mapLcsClient.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
            this.mapLcsClient.setNumberingPlanType((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());
        }

        this.mapLcsClient.setNumberingPlan(cbAddress.getText());

        return true;
    }
}
