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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.mobicents.protocols.ss7.tools.simulator.level3.CapManMBean;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CapForm extends JDialog {

    private CapManMBean cap;

    private static final long serialVersionUID = 6493475378477569651L;
    private JTextField tbRemoteAddressDigits;

    public CapForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CAP settings");
        setBounds(100, 100, 581, 273);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel label_2 = new JLabel("Remote address digits");
        label_2.setBounds(10, 14, 183, 14);
        panel.add(label_2);

        tbRemoteAddressDigits = new JTextField();
        tbRemoteAddressDigits.setColumns(10);
        tbRemoteAddressDigits.setBounds(203, 11, 270, 20);
        panel.add(tbRemoteAddressDigits);

        JLabel label_3 = new JLabel(
                "<html>\r\nIf empty RoutingOnDpcAndSsn is used for CalledPartyAddress (remoteSpc from SCCP)<br>\r\nIf not empty RoutingOnGT is used (address and Ssn a defined in MAP layer)<br>\r\nThis option may be ignored by some test tasks that supply there own digits\r\n</html>");
        label_3.setVerticalAlignment(SwingConstants.TOP);
        label_3.setBounds(10, 39, 555, 56);
        panel.add(label_3);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                loadDataA();
            }
        });
        button.setBounds(10, 168, 254, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(274, 168, 244, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Reload");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.setBounds(10, 202, 144, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(274, 202, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_4.setBounds(401, 202, 117, 23);
        panel.add(button_4);
    }

    public void setData(CapManMBean cap) {
        this.cap = cap;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        // tbLocalSsn.setText(((Integer) this.cap.getLocalSsn()).toString());
        // tbRemoteSsn.setText(((Integer) this.cap.getRemoteSsn()).toString());

        tbRemoteAddressDigits.setText(this.cap.getRemoteAddressDigits());
    }

    private void loadDataA() {
        // tbLocalSsn.setText("146");
        // tbRemoteSsn.setText("146");

        tbRemoteAddressDigits.setText("");
    }

    private void loadDataB() {
        // tbLocalSsn.setText("146");
        // tbRemoteSsn.setText("146");

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
        //
        // this.cap.setLocalSsn(localSsn);
        // this.cap.setRemoteSsn(remoteSsn);

        this.cap.setRemoteAddressDigits(tbRemoteAddressDigits.getText());

        return true;
    }
}
