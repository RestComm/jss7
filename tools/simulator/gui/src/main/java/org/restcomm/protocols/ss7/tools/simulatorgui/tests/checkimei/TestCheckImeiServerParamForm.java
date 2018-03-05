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

package org.restcomm.protocols.ss7.tools.simulatorgui.tests.checkimei;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.restcomm.protocols.ss7.map.api.service.mobility.imei.EquipmentStatus;
import org.restcomm.protocols.ss7.tools.simulator.tests.checkimei.EquipmentStatusType;
import org.restcomm.protocols.ss7.tools.simulator.tests.checkimei.TestCheckImeiServerManMBean;
import org.restcomm.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 * @author mnowa
 *
 */
public class TestCheckImeiServerParamForm extends JDialog {

    private static final long serialVersionUID = 6774133718469410382L;

    private TestCheckImeiServerManMBean checkImeiServer;

    private JComboBox cbEquipmentStatus;
    private JCheckBox cbOneNotificationFor100Dialogs;

    public TestCheckImeiServerParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CheckImei test server settings");
        setBounds(100, 100, 539, 508);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblEquipmentStatus = new JLabel("EquipmentStatus");
        lblEquipmentStatus.setBounds(10, 50, 174, 20);
        panel.add(lblEquipmentStatus);

        cbEquipmentStatus = new JComboBox();
        cbEquipmentStatus.setBounds(200, 50, 321, 20);
        panel.add(cbEquipmentStatus);


        cbOneNotificationFor100Dialogs = new JCheckBox("One notification for 100 dialogs");
        cbOneNotificationFor100Dialogs.setBounds(10, 100, 511, 23);
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

    }

    public void setData(TestCheckImeiServerManMBean checkImeiServer) {
        this.checkImeiServer = checkImeiServer;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {

        M3uaForm.setEnumeratedBaseComboBox(cbEquipmentStatus, this.checkImeiServer.getAutoEquipmentStatus());
        cbOneNotificationFor100Dialogs.setSelected(this.checkImeiServer.isOneNotificationFor100Dialogs());
    }

    private void loadDataA() {

        M3uaForm.setEnumeratedBaseComboBox(cbEquipmentStatus, new EquipmentStatusType(EquipmentStatus.whiteListed.getCode()));
        cbOneNotificationFor100Dialogs.setSelected(false);

    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        this.checkImeiServer.setAutoEquipmentStatus((EquipmentStatusType) cbEquipmentStatus.getSelectedItem());
        this.checkImeiServer.setOneNotificationFor100Dialogs(cbOneNotificationFor100Dialogs.isSelected());

        return true;
    }

}
