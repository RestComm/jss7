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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap;

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

import org.mobicents.protocols.ss7.cap.api.primitives.EventTypeBCSM;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.EventTypeBCSMType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNatureOfAddressIndicator;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNatureOfAddressIndicatorType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNumberingPlanIndicator;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.IsupNumberingPlanIndicatorType;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapSsfManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapSsfParamForm extends JDialog {

    private TestCapSsfManMBean capSsf;

    private JTabbedPane tabbedPane;
    private JComboBox cbCapApplicationContext;
    private JComboBox cbIdpEventTypeBCSM;
    private JTextField tfSkey;
    private JComboBox cbClgNAI;
    private JComboBox cbClgNPI;
    private JTextField tfClgAddress;
    private JComboBox cbClgTON;
    private JComboBox cbCldBcdNAI;
    private JComboBox cbCldBcdTON;
    private JTextField tfCldBcdAddress;
    private JComboBox cbCldNAI;
    private JComboBox cbCldNPI;
    private JTextField tfCldAddress;
    private JCheckBox idpUseCalledPartyNumberIndicator;
    private JComboBox cbMscAddrNAI;
    private JComboBox cbMscAddrNPI;
    private JTextField tfMscAddressAddress;



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

    private JCheckBox createCheckbox(JPanel section,String name, int y_pos) {

        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setBounds(10, y_pos, 450, 20);
        section.add(checkBox);

        return checkBox;
    }

    public TestCapSsfParamForm(JFrame owner) {
        super(owner, true);

        int bottomOfPage=640;
        int lineSeparation = 22;
        int sectionSeparation = 5;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CAP SSF test settings");
        setBounds(100, 100, 600, bottomOfPage);
        getContentPane().setLayout(null);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setBounds(0, 0, 560, bottomOfPage-100);
        getContentPane().add(tabbedPane);

        // General TAB
        JPanel panel_gen = createTab(tabbedPane,"General");

        JPanel panel = createSection(panel_gen,"Parameters for TCAP dialog creation",23,lineSeparation*2);
        cbCapApplicationContext = createCombo(panel,"CAP application context",lineSeparation);

        // IDP Request TAB
        JPanel panel_idp = createTab(tabbedPane,"InitialDP request");

        JPanel panel_idp_skey = createSection(panel_idp,"Service Key",sectionSeparation,lineSeparation*2);
        tfSkey = cretateTextField(panel_idp_skey,"Skey",lineSeparation);

        JPanel panel_idp_event = createSection(panel_idp,"Parameters for EventTypeBCSM creation",2*sectionSeparation+lineSeparation*2,lineSeparation*2);
        cbIdpEventTypeBCSM = createCombo(panel_idp_event,"Event Type BCSM",lineSeparation);

        JPanel panel_idp_clg = createSection(panel_idp,"Parameters for CallingPartyNumber creation",3*sectionSeparation+lineSeparation*4,lineSeparation*4);
        cbClgNAI = createCombo(panel_idp_clg,"NatureOfAddresIndicator",lineSeparation);
        cbClgNPI = createCombo(panel_idp_clg,"NumberingPlanIndicator",lineSeparation*2);
        tfClgAddress = cretateTextField(panel_idp_clg,"Address",lineSeparation*3);

        JPanel panel_idp_cld_bcd = createSection(panel_idp,"Parameters for CalledPartyBCDNumber creation",4*sectionSeparation+lineSeparation*8,lineSeparation*4);
        cbCldBcdNAI = createCombo(panel_idp_cld_bcd,"AddressNature",lineSeparation);
        cbCldBcdTON = createCombo(panel_idp_cld_bcd,"TypeOfNumber",lineSeparation*2);
        tfCldBcdAddress = cretateTextField(panel_idp_cld_bcd,"Address",lineSeparation*3);

        JPanel panel_idp_cld = createSection(panel_idp,"Parameters for CalledPartyNumber creation",5*sectionSeparation+lineSeparation*12,lineSeparation*5);
        cbCldNAI = createCombo(panel_idp_cld,"AddressNature",lineSeparation);
        cbCldNPI = createCombo(panel_idp_cld,"NumberingPlan",lineSeparation*2);
        tfCldAddress = cretateTextField(panel_idp_cld,"Address",lineSeparation*3);
        idpUseCalledPartyNumberIndicator = createCheckbox(panel_idp_cld,"Use CalledPartyNumber instead of CalledPartyBCDNumber Indicator",lineSeparation*4);

        JPanel panel_idp_msc = createSection(panel_idp,"Parameters for MscAddress creation",6*sectionSeparation+lineSeparation*17,lineSeparation*4);
        cbMscAddrNAI = createCombo(panel_idp_msc,"AddressNature",lineSeparation);
        cbMscAddrNPI = createCombo(panel_idp_msc,"NumberingPlan",lineSeparation*2);
        tfMscAddressAddress = cretateTextField(panel_idp_msc,"Address",lineSeparation*3);

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

    public void setData(TestCapSsfManMBean capSsf) {
        this.capSsf = capSsf;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbCapApplicationContext, this.capSsf.getCapApplicationContext());

        //InitialDP Request tab
        tfSkey.setText(""+this.capSsf.getServiceKey());
        M3uaForm.setEnumeratedBaseComboBox(cbIdpEventTypeBCSM, this.capSsf.getIdpEventTypeBCSM());
        M3uaForm.setEnumeratedBaseComboBox(cbClgNAI, this.capSsf.getCallingPartyNumberNatureOfAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbClgNPI, this.capSsf.getCallingPartyNumberNumberingPlan());
        tfClgAddress.setText(this.capSsf.getCallingPartyNumberAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbCldBcdNAI, this.capSsf.getCalledPartyBCDNumberAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbCldBcdTON, this.capSsf.getCalledPartyBCDNumberNumberingPlan());
        this.idpUseCalledPartyNumberIndicator.setSelected(this.capSsf.isUseCldInsteadOfCldBCDNumber());
        tfCldBcdAddress.setText(this.capSsf.getCalledPartyBCDNumberAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbCldNAI, this.capSsf.getCalledPartyNumberNatureOfAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbCldNPI, this.capSsf.getCalledPartyNumberNumberingPlan());
        tfCldAddress.setText(this.capSsf.getCalledPartyNumberAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbMscAddrNAI, this.capSsf.getMscAddressNatureOfAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbMscAddrNPI, this.capSsf.getMscAddressNumberingPlan());
        tfMscAddressAddress.setText(this.capSsf.getMscAddressAddress());

    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbCapApplicationContext, new CapApplicationContextSsf(
                CapApplicationContextSsf.VAL_CAP_V1_gsmSSF_to_gsmSCF));
        tfSkey.setText("10");
        M3uaForm.setEnumeratedBaseComboBox(cbIdpEventTypeBCSM,
                new EventTypeBCSMType(EventTypeBCSM.collectedInfo.getCode()));
        tfClgAddress.setText("11111111");
        M3uaForm.setEnumeratedBaseComboBox(cbClgNAI,
                new IsupNatureOfAddressIndicatorType(IsupNatureOfAddressIndicator.internationalNumber.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbClgNPI,
                new IsupNumberingPlanIndicatorType(IsupNumberingPlanIndicator.ISDN.getCode()));
        tfCldBcdAddress.setText("22222222");
        M3uaForm.setEnumeratedBaseComboBox(cbCldBcdNAI,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbCldBcdTON,
                new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        tfCldAddress.setText("33333333");
        this.idpUseCalledPartyNumberIndicator.setSelected(false);
        M3uaForm.setEnumeratedBaseComboBox(cbCldNAI,
                new IsupNatureOfAddressIndicatorType(IsupNatureOfAddressIndicator.internationalNumber.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbCldNPI,
                new IsupNumberingPlanIndicatorType(IsupNumberingPlanIndicator.ISDN.getCode()));
        M3uaForm.setEnumeratedBaseComboBox(cbMscAddrNAI,
                new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbMscAddrNPI,
                new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        tfMscAddressAddress.setText("55555555");
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        this.capSsf.setCapApplicationContext((CapApplicationContextSsf) cbCapApplicationContext.getSelectedItem());
        this.capSsf.setIdpEventTypeBCSM((EventTypeBCSMType) cbIdpEventTypeBCSM.getSelectedItem());
        this.capSsf.setCallingPartyNumberNatureOfAddress((IsupNatureOfAddressIndicatorType) cbClgNAI.getSelectedItem());
        this.capSsf.setCallingPartyNumberNumberingPlan((IsupNumberingPlanIndicatorType) cbClgNPI.getSelectedItem());
        this.capSsf.setCallingPartyNumberAddress(tfClgAddress.getText());
        try {
            this.capSsf.setServiceKey(Integer.valueOf(this.tfSkey.getText()));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "value: " + e.toString() + "Not valid, must be a number");
            return false;
        }
        this.capSsf.setCalledPartyBCDNumberAddress(tfCldBcdAddress.getText());
        this.capSsf.setCalledPartyBCDNumberAddressNature((AddressNatureType) cbCldBcdNAI.getSelectedItem());
        this.capSsf.setCalledPartyBCDNumberNumberingPlan((NumberingPlanMapType) cbCldBcdTON.getSelectedItem());
        this.capSsf.setUseCldInsteadOfCldBCDNumber(this.idpUseCalledPartyNumberIndicator.isSelected());
        this.capSsf.setCalledPartyNumberAddress(tfCldAddress.getText());
        this.capSsf.setCalledPartyNumberNatureOfAddress((IsupNatureOfAddressIndicatorType) cbCldNAI.getSelectedItem());
        this.capSsf.setCalledPartyNumberNumberingPlan((IsupNumberingPlanIndicatorType) cbCldNPI.getSelectedItem());
        this.capSsf.setMscAddressAddress(tfMscAddressAddress.getText());
        this.capSsf.setMscAddressNatureOfAddress((AddressNatureType) cbMscAddrNAI.getSelectedItem());
        this.capSsf.setMscAddressNumberingPlan((NumberingPlanMapType) cbMscAddrNPI.getSelectedItem());

        return true;
    }

}
