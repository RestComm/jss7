/*
 * TeleStax, Open Source Cloud Communications  Copyright 2017.
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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap;

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
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextScf;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupCauseIndicatorCauseValue;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupCauseIndicatorCauseValueType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupCauseIndicatorCodingStandard;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupCauseIndicatorCodingStandardType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupCauseIndicatorLocation;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupCauseIndicatorLocationType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNatureOfAddressIndicator;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNatureOfAddressIndicatorType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNumberingPlanIndicator;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNumberingPlanIndicatorType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapScfManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapScfParamForm extends JDialog {

    private TestCapScfManMBean capScf;

    private JComboBox cbCapApplicationContext;
    private JTabbedPane tabbedPane;
    private JComboBox cbConDraNAI;
    private JComboBox cbConDraNPI;
    private JTextField tfConDraAddress;
    private JComboBox cbRelCauseValue;
    private JComboBox cbRelCodingStandard;
    private JComboBox cbRelLocation;

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

    public TestCapScfParamForm(JFrame owner) {
        super(owner, true);

        int bottomOfPage=640;
        int lineSeparation = 22;
        int sectionSeparation = 5;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CAP SCF test settings");
        setBounds(100, 100, 550, bottomOfPage);
        getContentPane().setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 560, bottomOfPage-100);
        getContentPane().add(tabbedPane);

        // General TAB
        JPanel panel_gen = createTab(tabbedPane,"General");

        JPanel panel = createSection(panel_gen,"Parameters for TCAP dialog",23,lineSeparation*2);
        cbCapApplicationContext = createCombo(panel,"CAP application context",lineSeparation);

        // CON Request TAB
        JPanel panel_con = createTab(tabbedPane,"Connect request");
        JPanel panel_con_dra = createSection(panel_con,"Parameters for DestinationRoutingAddress creation",sectionSeparation,lineSeparation*4);
        cbConDraNAI = createCombo(panel_con_dra,"AddressNature",lineSeparation);
        cbConDraNPI = createCombo(panel_con_dra,"NumberingPlan",lineSeparation*2);
        tfConDraAddress = cretateTextField(panel_con_dra,"Address",lineSeparation*3);

        // REL Request TAB
        JPanel panel_rel = createTab(tabbedPane,"Release request");
        JPanel panel_rel_cause = createSection(panel_rel,"Parameters for Release CauseIndicators creation",sectionSeparation,lineSeparation*4);
        cbRelCauseValue = createCombo(panel_rel_cause,"CuaseValue",lineSeparation);
        cbRelCodingStandard = createCombo(panel_rel_cause,"CodingStandard",lineSeparation*2);
        cbRelLocation = createCombo(panel_rel_cause,"Location",lineSeparation*3);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, bottomOfPage-90, 246, 23);
        getContentPane().add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, bottomOfPage-90, 255, 23);
        getContentPane().add(button_1);

        JButton button_2 = new JButton("Cancel");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_2.setBounds(404, bottomOfPage-60, 117, 23);
        getContentPane().add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, bottomOfPage-60, 117, 23);
        getContentPane().add(button_3);

        JButton button_4 = new JButton("Reload");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_4.setBounds(10, bottomOfPage-60, 144, 23);
        getContentPane().add(button_4);
    }

    public void setData(TestCapScfManMBean capScf) {
        this.capScf = capScf;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbCapApplicationContext, this.capScf.getCapApplicationContext());
        M3uaForm.setEnumeratedBaseComboBox(cbConDraNAI, this.capScf.getConnectDestinationRoutingAddressNatureOfAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbConDraNPI, this.capScf.getConnectDestinationRoutingAddressNumberingPlan());
        tfConDraAddress.setText(this.capScf.getConnectDestinationRoutingAddressAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbRelCauseValue, this.capScf.getReleaseCauseValue());
        M3uaForm.setEnumeratedBaseComboBox(cbRelCodingStandard, this.capScf.getReleaseCauseCodingStandardIndicator());
        M3uaForm.setEnumeratedBaseComboBox(cbRelLocation, this.capScf.getReleaseCauseLocationIndicator());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbCapApplicationContext, new CapApplicationContextScf(
                CapApplicationContextScf.VAL_CAP_V4_capscf_ssfGeneric));
        tfConDraAddress.setText("7777777");
        M3uaForm.setEnumeratedBaseComboBox(cbConDraNAI,
                new IsupNatureOfAddressIndicatorType(IsupNatureOfAddressIndicator.internationalNumber.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbConDraNPI,
                new IsupNumberingPlanIndicatorType(IsupNumberingPlanIndicator.ISDN.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbRelCauseValue,
                new IsupCauseIndicatorCauseValueType(IsupCauseIndicatorCauseValue.normalUnspecified.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbRelCodingStandard,
                new IsupCauseIndicatorCodingStandardType(IsupCauseIndicatorCodingStandard.ITUT.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbRelLocation,
                new IsupCauseIndicatorLocationType(IsupCauseIndicatorLocation.internationalNetwork.getCode()));
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        this.capScf.setCapApplicationContext((CapApplicationContextScf) cbCapApplicationContext.getSelectedItem());
        this.capScf.setConnectDestinationRoutingAddressAddress(tfConDraAddress.getText());
        this.capScf.setConnectDestinationRoutingAddressNatureOfAddress((IsupNatureOfAddressIndicatorType) cbConDraNAI.getSelectedItem());
        this.capScf.setConnectDestinationRoutingAddressNumberingPlan((IsupNumberingPlanIndicatorType) cbConDraNPI.getSelectedItem());
        this.capScf.setReleaseCauseValue((IsupCauseIndicatorCauseValueType) cbRelCauseValue.getSelectedItem());
        this.capScf.setReleaseCauseCodingStandardIndicator((IsupCauseIndicatorCodingStandardType) cbRelCodingStandard.getSelectedItem());
        this.capScf.setReleaseCauseLocationIndicator((IsupCauseIndicatorLocationType) cbRelLocation.getSelectedItem());
        return true;
    }
}
