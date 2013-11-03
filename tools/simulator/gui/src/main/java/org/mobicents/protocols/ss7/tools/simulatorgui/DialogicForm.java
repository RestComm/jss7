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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.mobicents.protocols.ss7.tools.simulator.level1.DialogicManMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DialogicForm extends JDialog {

    private DialogicManMBean dialogic;

    private static final long serialVersionUID = -3683159465393950167L;
    private JTextField tbSourceModuleId;
    private JTextField tbDestinationModuleId;

    public DialogicForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 534, 290);
        setTitle("Dialogic settings");

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblSourceModuleId = new JLabel("Source module Id");
        lblSourceModuleId.setBounds(10, 17, 207, 14);
        panel.add(lblSourceModuleId);

        tbSourceModuleId = new JTextField();
        tbSourceModuleId.setColumns(10);
        tbSourceModuleId.setBounds(242, 14, 129, 20);
        panel.add(tbSourceModuleId);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 186, 254, 23);
        panel.add(button);

        JButton button_1 = new JButton("Reload");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_1.setBounds(10, 220, 144, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Save");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_2.setBounds(274, 220, 117, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Cancel");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_3.setBounds(401, 220, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Load default values for side B");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_4.setBounds(274, 186, 244, 23);
        panel.add(button_4);

        JLabel lblDestinationModuleId = new JLabel("Destination module Id");
        lblDestinationModuleId.setBounds(10, 45, 207, 14);
        panel.add(lblDestinationModuleId);

        tbDestinationModuleId = new JTextField();
        tbDestinationModuleId.setColumns(10);
        tbDestinationModuleId.setBounds(242, 42, 129, 20);
        panel.add(tbDestinationModuleId);
    }

    public void setData(DialogicManMBean dialogic) {
        this.dialogic = dialogic;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        tbSourceModuleId.setText(((Integer) this.dialogic.getSourceModuleId()).toString());
        tbDestinationModuleId.setText(((Integer) this.dialogic.getDestinationModuleId()).toString());
    }

    private void loadDataA() {
        tbSourceModuleId.setText("61");
        tbDestinationModuleId.setText("34");
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {

        int sourceModuleId = 0;
        int destinationModuleId = 0;
        try {
            sourceModuleId = Integer.parseInt(tbSourceModuleId.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing SourceModuleId value: " + e.toString());
            return false;
        }
        try {
            destinationModuleId = Integer.parseInt(tbDestinationModuleId.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Exception when parsing DestinationModuleId value: " + e.toString());
            return false;
        }

        this.dialogic.setSourceModuleId(sourceModuleId);
        this.dialogic.setDestinationModuleId(destinationModuleId);

        return true;
    }
}
