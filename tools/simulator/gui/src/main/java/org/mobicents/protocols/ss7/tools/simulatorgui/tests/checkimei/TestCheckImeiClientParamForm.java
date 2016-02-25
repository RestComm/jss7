/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2016, Telestax Inc and individual contributors
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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.checkimei;

import java.awt.BorderLayout;
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
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.mobicents.protocols.ss7.tools.simulator.level3.MapProtocolVersion;
import org.mobicents.protocols.ss7.tools.simulator.tests.checkimei.CheckImeiClientAction;
import org.mobicents.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiClientParamForm extends JDialog {


    private static final long serialVersionUID = 1589538832800021188L;

    private TestCheckImeiClientManMBean checkImeiClient;

    private JTextField tbImei;
    private JComboBox cbMapProtocolVersion;
    private JTextField tbMaxConcurrentDialogs;
    private JComboBox cbCheckImeiClientAction;
    private JCheckBox cbOneNotificationFor100Dialogs;

    public TestCheckImeiClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CheckImei test client settings");
        setBounds(100, 100, 539, 508);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setLayout(null);
        panel_1.setBounds(10, 11, 511, 30);
        panel.add(panel_1);

        JLabel lbImei = new JLabel("Imei value");
        lbImei.setBounds(10, 5, 79, 20);
        panel_1.add(lbImei);

        tbImei = new JTextField();
        tbImei.setColumns(15);
        tbImei.setBounds(120, 5, 378, 20);
        panel_1.add(tbImei);

        JLabel lbMapVersion = new JLabel("MAP protocol version");
        lbMapVersion.setBounds(10, 100, 130, 14);
        panel.add(lbMapVersion);

        cbMapProtocolVersion = new JComboBox();
        cbMapProtocolVersion.setBounds(10, 135, 255, 20);
        panel.add(cbMapProtocolVersion);


        JLabel lblTheCountOf = new JLabel("The count of maximum active MAP dialogs when the auto sending mode");
        lblTheCountOf.setBounds(10, 270, 511, 14);
        panel.add(lblTheCountOf);

        tbMaxConcurrentDialogs = new JTextField();
        tbMaxConcurrentDialogs.setBounds(10, 290, 98, 20);
        panel.add(tbMaxConcurrentDialogs);
        tbMaxConcurrentDialogs.setColumns(10);

        cbOneNotificationFor100Dialogs = new JCheckBox("One notification for 100 dialogs (recommended for the auto sending mode)");
        cbOneNotificationFor100Dialogs.setBounds(10, 310, 511, 23);
        panel.add(cbOneNotificationFor100Dialogs);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 412, 246, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, 412, 255, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Cancel");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_2.setBounds(404, 446, 117, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, 446, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Reload");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_4.setBounds(10, 446, 144, 23);
        panel.add(button_4);

        JLabel lblCheckImeiClientMode = new JLabel("CheckImei client mode");
        lblCheckImeiClientMode.setBounds(10, 200, 499, 23);
        panel.add(lblCheckImeiClientMode);

        cbCheckImeiClientAction = new JComboBox();
        cbCheckImeiClientAction
                .setToolTipText("<html>\r\nThe mode of CheckImeiClient work. When manual response user can manually send CheckImei request, \r\n<br>\r\nwhen VAL_AUTO_SendCheckImeiRequest the tester sends CheckImei requests without dealay (load test)\r\n</html>");
        cbCheckImeiClientAction.setBounds(10, 235, 511, 20);
        panel.add(cbCheckImeiClientAction);

    }

    public void setData(TestCheckImeiClientManMBean checkImeiClient) {
        this.checkImeiClient = checkImeiClient;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {

        tbImei.setText(this.checkImeiClient.getImei());
        M3uaForm.setEnumeratedBaseComboBox(cbMapProtocolVersion, this.checkImeiClient.getMapProtocolVersion());
        M3uaForm.setEnumeratedBaseComboBox(cbCheckImeiClientAction, this.checkImeiClient.getCheckImeiClientAction());
        tbMaxConcurrentDialogs.setText(((Integer) this.checkImeiClient.getMaxConcurrentDialogs()).toString());
        cbOneNotificationFor100Dialogs.setSelected(this.checkImeiClient.isOneNotificationFor100Dialogs());
    }

    private void loadDataA() {

        M3uaForm.setEnumeratedBaseComboBox(cbCheckImeiClientAction, new CheckImeiClientAction(
                CheckImeiClientAction.VAL_MANUAL_OPERATION));

        tbImei.setText("");
        M3uaForm.setEnumeratedBaseComboBox(cbMapProtocolVersion, new MapProtocolVersion(MapProtocolVersion.VAL_MAP_V3));
        tbMaxConcurrentDialogs.setText("10");
        M3uaForm.setEnumeratedBaseComboBox(cbCheckImeiClientAction, new CheckImeiClientAction(CheckImeiClientAction.VAL_MANUAL_OPERATION));
        cbOneNotificationFor100Dialogs.setSelected(false);

    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        int maxConcurrentDialogs = 10;
        try {
            maxConcurrentDialogs = Integer.parseInt(tbMaxConcurrentDialogs.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing MaxConcurrentDialogs value: " + e.toString());
            return false;
        }

        this.checkImeiClient.setCheckImeiClientAction((CheckImeiClientAction) cbCheckImeiClientAction.getSelectedItem());
        this.checkImeiClient.setImei(tbImei.getText());
        this.checkImeiClient.setMapProtocolVersion((MapProtocolVersion) cbMapProtocolVersion.getSelectedItem());
        this.checkImeiClient.setMaxConcurrentDialogs(maxConcurrentDialogs);
        this.checkImeiClient.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());

        return true;
    }

}
