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

package org.mobicents.protocols.ss7.tools.simulatorgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.indicator.NatureOfAddress;
import org.mobicents.protocols.ss7.indicator.NumberingPlan;
import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.level2.GlobalTitleType;
import org.mobicents.protocols.ss7.tools.simulator.level2.NatureOfAddressType;
import org.mobicents.protocols.ss7.tools.simulator.level2.NumberingPlanSccpType;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level2.SccpProtocolVersionType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class SccpForm extends JDialog {

    private SccpManMBean sccp;

    private static final long serialVersionUID = 7571177143420596631L;
    private JTextField tbRemoteSpc;
    private JTextField tbLocalSpc;
    private JTextField tbNi;
    private JTextField tbRemoteSsn;
    private JTextField tbLocalSsn;
    private JTextField tbLocalSsn2;
    private JTextField tbTranslationType;
    private JComboBox cbGlobalTitleType;
    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JComboBox cbSccpProtocolVersion;
    private JTextField tbCallingPartyAddressDigits;
    private JRadioButton rbRouteDpcSsn;
    private JRadioButton rbRouteGt;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JPanel panel_1;

    public SccpForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("SCCP settings");
        setBounds(100, 100, 590, 651);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblDpc = new JLabel("Remote Spc");
        lblDpc.setBounds(10, 14, 134, 14);
        panel.add(lblDpc);

        tbRemoteSpc = new JTextField();
        tbRemoteSpc.setColumns(10);
        tbRemoteSpc.setBounds(154, 11, 129, 20);
        panel.add(tbRemoteSpc);

        tbLocalSpc = new JTextField();
        tbLocalSpc.setColumns(10);
        tbLocalSpc.setBounds(445, 11, 129, 20);
        panel.add(tbLocalSpc);

        JLabel lblOpc = new JLabel("Local Spc");
        lblOpc.setBounds(301, 14, 134, 14);
        panel.add(lblOpc);

        tbNi = new JTextField();
        tbNi.setColumns(10);
        tbNi.setBounds(154, 39, 129, 20);
        panel.add(tbNi);

        tbRemoteSsn = new JTextField();
        tbRemoteSsn.setColumns(10);
        tbRemoteSsn.setBounds(154, 70, 129, 20);
        panel.add(tbRemoteSsn);

        JLabel lblNetworkIndicatpr = new JLabel("Network indicator");
        lblNetworkIndicatpr.setBounds(10, 42, 112, 14);
        panel.add(lblNetworkIndicatpr);

        JLabel lblRemoteSsn = new JLabel("Remote Ssn");
        lblRemoteSsn.setBounds(10, 73, 112, 14);
        panel.add(lblRemoteSsn);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 555, 245, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(265, 555, 234, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Reload");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.setBounds(10, 589, 144, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(255, 589, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_4.setBounds(382, 589, 117, 23);
        panel.add(button_4);

        tbLocalSsn = new JTextField();
        tbLocalSsn.setColumns(10);
        tbLocalSsn.setBounds(445, 70, 53, 20);
        panel.add(tbLocalSsn);

        JLabel lblLocalSsn = new JLabel("Local Ssn");
        lblLocalSsn.setBounds(301, 73, 112, 14);
        panel.add(lblLocalSsn);

        panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setLayout(null);
        panel_1.setBounds(10, 366, 564, 178);
        panel.add(panel_1);

        JLabel lblParametersForCreating = new JLabel("Parameters for creating SccpAddress (when routing on GT)");
        lblParametersForCreating.setBounds(10, 0, 468, 14);
        panel_1.add(lblParametersForCreating);

        JLabel lblTranslationType = new JLabel("Translation type");
        lblTranslationType.setBounds(10, 119, 136, 14);
        panel_1.add(lblTranslationType);

        JLabel label_2 = new JLabel("AddressNature");
        label_2.setBounds(10, 57, 136, 14);
        panel_1.add(label_2);

        JLabel label_3 = new JLabel("NumberingPlan");
        label_3.setBounds(10, 88, 136, 14);
        panel_1.add(label_3);

        tbTranslationType = new JTextField();
        tbTranslationType.setColumns(10);
        tbTranslationType.setBounds(156, 116, 130, 20);
        panel_1.add(tbTranslationType);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(156, 54, 294, 20);
        panel_1.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(156, 85, 294, 20);
        panel_1.add(cbNumberingPlan);

        JLabel lblGlobaltitleType = new JLabel("GlobalTitle type");
        lblGlobaltitleType.setBounds(10, 29, 136, 14);
        panel_1.add(lblGlobaltitleType);

        cbGlobalTitleType = new JComboBox();
        cbGlobalTitleType.setBounds(156, 26, 398, 20);
        panel_1.add(cbGlobalTitleType);

        JLabel lblCallingpartyaddressDigits = new JLabel("CallingPartyAddress digits");
        lblCallingpartyaddressDigits.setBounds(10, 150, 176, 14);
        panel_1.add(lblCallingpartyaddressDigits);

        tbCallingPartyAddressDigits = new JTextField();
        tbCallingPartyAddressDigits.setColumns(10);
        tbCallingPartyAddressDigits.setBounds(219, 147, 231, 20);
        panel_1.add(tbCallingPartyAddressDigits);

        rbRouteDpcSsn = new JRadioButton("Route on DPC and SSN mode");
        buttonGroup.add(rbRouteDpcSsn);
        rbRouteDpcSsn.setBounds(6, 164, 332, 23);
        panel.add(rbRouteDpcSsn);

        rbRouteGt = new JRadioButton("Route on GlobalTitle mode");
        rbRouteGt.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                setRouteOnGtEnabled(e.getStateChange() == 1);
            }
        });
        buttonGroup.add(rbRouteGt);
        rbRouteGt.setBounds(10, 247, 332, 23);
        panel.add(rbRouteGt);

        JLabel lblNewLabel = new JLabel(
                "<html>\r\nCallingPartyAddress:  RoutingIndicator=RoutingOnDpcAndSsn, PC=localSpc, GT=null, SSN=localSsn<br>\r\nCalledPartyAddress:  RoutingIndicator=RoutingOnDpcAndSsn PC=remoteSpc, GT=null, SSN=remoteSsn\r\n</html>");
        lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
        lblNewLabel.setBounds(10, 190, 564, 50);
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel(
                "<html>\r\nCallingPartyAddress:  RoutingIndicator=RoutingOnGt, PC=0, GT=CallingPartyAddress digits, SSN=localSsn<br>\r\nCalledPartyAddress:  RoutingIndicator=RoutingOnGt, PC=0, GT and SSN is supplied by upper levels<br>\r\nAll messages will be routed to remoteSpc except messages with CallingPartyAddress digits or extra local addresses\r\n</html>");
        lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
        lblNewLabel_1.setBounds(10, 277, 564, 78);
        panel.add(lblNewLabel_1);

        JLabel lblSccpProtocolVersion = new JLabel("SCCP protocol version");
        lblSccpProtocolVersion.setBounds(10, 104, 129, 14);
        panel.add(lblSccpProtocolVersion);

        cbSccpProtocolVersion = new JComboBox();
        cbSccpProtocolVersion.setBounds(197, 101, 141, 20);
        panel.add(cbSccpProtocolVersion);

        tbLocalSsn2 = new JTextField();
        tbLocalSsn2.setColumns(10);
        tbLocalSsn2.setBounds(521, 70, 53, 20);
        panel.add(tbLocalSsn2);
    }

    public void setData(SccpManMBean sccp) {
        this.sccp = sccp;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        if (!this.sccp.isRouteOnGtMode()) {
            this.rbRouteDpcSsn.setSelected(true);
            setRouteOnGtEnabled(false);
        } else {
            this.rbRouteGt.setSelected(true);
            setRouteOnGtEnabled(true);
        }

        M3uaForm.setEnumeratedBaseComboBox(cbGlobalTitleType, this.sccp.getGlobalTitleType());
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.sccp.getNatureOfAddress());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.sccp.getNumberingPlan());
        M3uaForm.setEnumeratedBaseComboBox(cbSccpProtocolVersion, this.sccp.getSccpProtocolVersion());

        tbRemoteSpc.setText(((Integer) this.sccp.getRemoteSpc()).toString());
        tbLocalSpc.setText(((Integer) this.sccp.getLocalSpc()).toString());
        tbNi.setText(((Integer) this.sccp.getNi()).toString());
        tbRemoteSsn.setText(((Integer) this.sccp.getRemoteSsn()).toString());
        tbLocalSsn.setText(((Integer) this.sccp.getLocalSsn()).toString());
        tbLocalSsn2.setText(((Integer) this.sccp.getLocalSsn2()).toString());
        tbTranslationType.setText(((Integer) this.sccp.getTranslationType()).toString());

        tbCallingPartyAddressDigits.setText(this.sccp.getCallingPartyAddressDigits());
        // tbExtraLocalAddressDigits.setText(this.sccp.getExtraLocalAddressDigits());
    }

    private void loadDataA() {
        SsnDefaultSelectionForm fmA = new SsnDefaultSelectionForm();
        fmA.setVisible(true);

        int res = fmA.getResult();
        if (res == 0)
            return;
        switch (res) {
            case 1:
                tbRemoteSsn.setText("8");
                tbLocalSsn.setText("8");
                tbLocalSsn2.setText("0");
                break;
            case 2:
                tbRemoteSsn.setText("146");
                tbLocalSsn.setText("146");
                tbLocalSsn2.setText("0");
                break;
        }

        this.rbRouteDpcSsn.setSelected(true);
        setRouteOnGtEnabled(false);

        M3uaForm.setEnumeratedBaseComboBox(cbGlobalTitleType, new GlobalTitleType(GlobalTitleType.VAL_TT_NP_ES_NOA));
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new NatureOfAddressType(NatureOfAddress.INTERNATIONAL.getValue()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanSccpType(NumberingPlan.ISDN_TELEPHONY.getValue()));
        M3uaForm.setEnumeratedBaseComboBox(cbSccpProtocolVersion, new SccpProtocolVersionType(SccpProtocolVersion.ITU.getValue()));

        tbRemoteSpc.setText("2");
        tbLocalSpc.setText("1");
        tbNi.setText("2");
        tbTranslationType.setText("0");

        tbCallingPartyAddressDigits.setText("");
        // tbExtraLocalAddressDigits.setText("");
    }

    private void loadDataB() {
        SsnDefaultSelectionForm fmA = new SsnDefaultSelectionForm();
        fmA.setVisible(true);

        int res = fmA.getResult();
        if (res == 0)
            return;
        switch (res) {
            case 1:
                tbRemoteSsn.setText("8");
                tbLocalSsn.setText("8");
                tbLocalSsn2.setText("0");
                break;
            case 2:
                tbRemoteSsn.setText("146");
                tbLocalSsn.setText("146");
                tbLocalSsn2.setText("0");
                break;
        }

        this.rbRouteDpcSsn.setSelected(true);
        setRouteOnGtEnabled(false);

        M3uaForm.setEnumeratedBaseComboBox(cbGlobalTitleType, new GlobalTitleType(GlobalTitleType.VAL_TT_NP_ES_NOA));
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new NatureOfAddressType(NatureOfAddress.INTERNATIONAL.getValue()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanSccpType(NumberingPlan.ISDN_TELEPHONY.getValue()));
        M3uaForm.setEnumeratedBaseComboBox(cbSccpProtocolVersion, new SccpProtocolVersionType(SccpProtocolVersion.ITU.getValue()));

        tbRemoteSpc.setText("1");
        tbLocalSpc.setText("2");
        tbNi.setText("2");
        tbTranslationType.setText("0");

        tbCallingPartyAddressDigits.setText("");
        // tbExtraLocalAddressDigits.setText("");
    }

    private boolean saveData() {
        int remoteSpc = 0;
        int localSpc = 0;
        int ni = 0;
        int remoteSsn = 0;
        int localSsn = 0;
        int localSsn2 = 0;
        int translationType = 0;
        try {
            remoteSpc = Integer.parseInt(tbRemoteSpc.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Dpc value: " + e.toString());
            return false;
        }
        try {
            localSpc = Integer.parseInt(tbLocalSpc.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Opc value: " + e.toString());
            return false;
        }
        try {
            ni = Integer.parseInt(tbNi.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing Network indicator value: " + e.toString());
            return false;
        }
        try {
            remoteSsn = Integer.parseInt(tbRemoteSsn.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing RemoteSsn value: " + e.toString());
            return false;
        }
        try {
            localSsn = Integer.parseInt(tbLocalSsn.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing LocalSsn value: " + e.toString());
            return false;
        }
        try {
            localSsn2 = Integer.parseInt(tbLocalSsn2.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing LocalSsn2 value: " + e.toString());
            return false;
        }
        try {
            translationType = Integer.parseInt(tbTranslationType.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing TranslationType value: " + e.toString());
            return false;
        }

        this.sccp.setRouteOnGtMode(this.rbRouteGt.isSelected());

        this.sccp.setRemoteSpc(remoteSpc);
        this.sccp.setLocalSpc(localSpc);
        this.sccp.setNi(ni);
        this.sccp.setRemoteSsn(remoteSsn);
        this.sccp.setLocalSsn(localSsn);
        this.sccp.setLocalSsn2(localSsn2);
        this.sccp.setTranslationType(translationType);

        this.sccp.setGlobalTitleType((GlobalTitleType) cbGlobalTitleType.getSelectedItem());
        this.sccp.setNatureOfAddress((NatureOfAddressType) cbAddressNature.getSelectedItem());
        this.sccp.setNumberingPlan((NumberingPlanSccpType) cbNumberingPlan.getSelectedItem());
        this.sccp.setSccpProtocolVersion((SccpProtocolVersionType) cbSccpProtocolVersion.getSelectedItem());

        this.sccp.setCallingPartyAddressDigits(tbCallingPartyAddressDigits.getText());
        // this.sccp.setExtraLocalAddressDigits(tbExtraLocalAddressDigits.getText());

        return true;
    }

    private void setRouteOnGtEnabled(boolean val) {
        this.cbGlobalTitleType.setEnabled(val);
        this.cbAddressNature.setEnabled(val);
        this.cbNumberingPlan.setEnabled(val);
        this.tbTranslationType.setEnabled(val);
        this.tbCallingPartyAddressDigits.setEnabled(val);
        // this.tbExtraLocalAddressDigits.setEnabled(val);
    }
}
