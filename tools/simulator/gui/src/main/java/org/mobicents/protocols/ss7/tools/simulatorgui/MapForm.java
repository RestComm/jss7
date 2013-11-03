/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.protocols.ss7.tools.simulatorgui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.MapManMBean;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MapForm extends JDialog {

    private MapManMBean map;

    private static final long serialVersionUID = -2799708291291364182L;
    private JTextField tbOrigReference;
    private JTextField tbDestReference;
    private JTextField tbRemoteAddressDigits;
    private JComboBox cbOrigReferenceAddressNature;
    private JComboBox cbOrigReferenceNumberingPlan;
    private JComboBox cbDestReferenceAddressNature;
    private JComboBox cbDestReferenceNumberingPlan;

    public MapForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("MAP settings");
        setBounds(100, 100, 614, 492);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 402, 254, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(274, 402, 244, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Reload");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.setBounds(10, 436, 144, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(274, 436, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_4.setBounds(401, 436, 117, 23);
        panel.add(button_4);

        JLabel lblRemoteAddressDigits = new JLabel("Remote address digits");
        lblRemoteAddressDigits.setBounds(13, 14, 183, 14);
        panel.add(lblRemoteAddressDigits);

        tbRemoteAddressDigits = new JTextField();
        tbRemoteAddressDigits.setColumns(10);
        tbRemoteAddressDigits.setBounds(206, 11, 270, 20);
        panel.add(tbRemoteAddressDigits);

        JLabel lblIfEmptyRoutingbasedondpcandssn = new JLabel(
                "<html>\r\nIf empty RoutingOnDpcAndSsn is used for CalledPartyAddress (remoteSpc from SCCP)<br>\r\nIf not empty RoutingOnGT is used (address and Ssn a defined in MAP layer)<br>\r\nThis option may be ignored by some test tasks that supply there own digits\r\n</html>");
        lblIfEmptyRoutingbasedondpcandssn.setVerticalAlignment(SwingConstants.TOP);
        lblIfEmptyRoutingbasedondpcandssn.setBounds(13, 39, 588, 56);
        panel.add(lblIfEmptyRoutingbasedondpcandssn);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setLayout(null);
        panel_1.setBounds(10, 106, 511, 108);
        panel.add(panel_1);

        JLabel lblOriginationReference = new JLabel("Origination reference");
        lblOriginationReference.setBounds(10, 0, 147, 14);
        panel_1.add(lblOriginationReference);

        JLabel label_2 = new JLabel("AddressNature");
        label_2.setBounds(10, 53, 136, 14);
        panel_1.add(label_2);

        JLabel label_3 = new JLabel("NumberingPlan");
        label_3.setBounds(10, 84, 136, 14);
        panel_1.add(label_3);

        cbOrigReferenceAddressNature = new JComboBox();
        cbOrigReferenceAddressNature.setBounds(179, 47, 294, 20);
        panel_1.add(cbOrigReferenceAddressNature);

        cbOrigReferenceNumberingPlan = new JComboBox();
        cbOrigReferenceNumberingPlan.setBounds(179, 78, 294, 20);
        panel_1.add(cbOrigReferenceNumberingPlan);

        JLabel lblOriginationReferenceString = new JLabel("Address digits");
        lblOriginationReferenceString.setBounds(10, 22, 169, 14);
        panel_1.add(lblOriginationReferenceString);

        tbOrigReference = new JTextField();
        tbOrigReference.setBounds(203, 19, 270, 20);
        panel_1.add(tbOrigReference);
        tbOrigReference.setColumns(10);

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2.setLayout(null);
        panel_2.setBounds(10, 221, 511, 108);
        panel.add(panel_2);

        JLabel lblDestinationReference = new JLabel("Destination reference");
        lblDestinationReference.setBounds(10, 0, 147, 14);
        panel_2.add(lblDestinationReference);

        JLabel label_1 = new JLabel("AddressNature");
        label_1.setBounds(10, 53, 136, 14);
        panel_2.add(label_1);

        JLabel label_4 = new JLabel("NumberingPlan");
        label_4.setBounds(10, 84, 136, 14);
        panel_2.add(label_4);

        cbDestReferenceAddressNature = new JComboBox();
        cbDestReferenceAddressNature.setBounds(179, 47, 294, 20);
        panel_2.add(cbDestReferenceAddressNature);

        cbDestReferenceNumberingPlan = new JComboBox();
        cbDestReferenceNumberingPlan.setBounds(179, 78, 294, 20);
        panel_2.add(cbDestReferenceNumberingPlan);

        JLabel label_5 = new JLabel("Address digits");
        label_5.setBounds(10, 22, 169, 14);
        panel_2.add(label_5);

        tbDestReference = new JTextField();
        tbDestReference.setBounds(203, 19, 270, 20);
        panel_2.add(tbDestReference);
        tbDestReference.setColumns(10);
    }

    public void setData(MapManMBean map) {
        this.map = map;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbOrigReferenceAddressNature, this.map.getOrigReferenceAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbOrigReferenceNumberingPlan, this.map.getOrigReferenceNumberingPlan());
        M3uaForm.setEnumeratedBaseComboBox(cbDestReferenceAddressNature, this.map.getDestReferenceAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbDestReferenceNumberingPlan, this.map.getDestReferenceNumberingPlan());

        // tbLocalSsn.setText(((Integer) this.map.getLocalSsn()).toString());
        // tbRemoteSsn.setText(((Integer) this.map.getRemoteSsn()).toString());

        tbOrigReference.setText(this.map.getOrigReference());
        tbDestReference.setText(this.map.getDestReference());
        tbRemoteAddressDigits.setText(this.map.getRemoteAddressDigits());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbOrigReferenceAddressNature, new AddressNatureType(
                AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbOrigReferenceNumberingPlan,
                new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbDestReferenceAddressNature, new AddressNatureType(
                AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbDestReferenceNumberingPlan,
                new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));

        // tbLocalSsn.setText("8");
        // tbRemoteSsn.setText("8");

        tbOrigReference.setText("");
        tbDestReference.setText("");
        tbRemoteAddressDigits.setText("");
    }

    private void loadDataB() {
        M3uaForm.setEnumeratedBaseComboBox(cbOrigReferenceAddressNature, new AddressNatureType(
                AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbOrigReferenceNumberingPlan,
                new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbDestReferenceAddressNature, new AddressNatureType(
                AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbDestReferenceNumberingPlan,
                new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));

        // tbLocalSsn.setText("8");
        // tbRemoteSsn.setText("8");

        tbOrigReference.setText("");
        tbDestReference.setText("");
        tbRemoteAddressDigits.setText("");
    }

    private boolean saveData() {
        // int localSsn = 0;
        // int remoteSsn = 0;
        // try {
        // localSsn = Integer.parseInt(tbLocalSsn.getText());
        // } catch (Exception e) {
        // JOptionPane.showMessageDialog(this, "Exception when parsing Local Ssn value: " + e.toString());
        // return false;
        // }
        // try {
        // remoteSsn = Integer.parseInt(tbRemoteSsn.getText());
        // } catch (Exception e) {
        // JOptionPane.showMessageDialog(this, "Exception when parsing Remote Ssn value: " + e.toString());
        // return false;
        // }

        this.map.setOrigReferenceAddressNature((AddressNatureType) cbOrigReferenceAddressNature.getSelectedItem());
        this.map.setOrigReferenceNumberingPlan((NumberingPlanMapType) cbOrigReferenceNumberingPlan.getSelectedItem());
        this.map.setDestReferenceAddressNature((AddressNatureType) cbDestReferenceAddressNature.getSelectedItem());
        this.map.setDestReferenceNumberingPlan((NumberingPlanMapType) cbDestReferenceNumberingPlan.getSelectedItem());

        // this.map.setLocalSsn(localSsn);
        // this.map.setRemoteSsn(remoteSsn);

        this.map.setOrigReference(tbOrigReference.getText());
        this.map.setDestReference(tbDestReference.getText());
        this.map.setRemoteAddressDigits(tbRemoteAddressDigits.getText());

        return true;
    }
}
