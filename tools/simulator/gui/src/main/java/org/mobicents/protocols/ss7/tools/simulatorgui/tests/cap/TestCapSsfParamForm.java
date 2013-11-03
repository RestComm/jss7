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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.cap;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.mobicents.protocols.ss7.tools.simulator.common.CapApplicationContextSsf;
import org.mobicents.protocols.ss7.tools.simulator.tests.cap.TestCapSsfManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestCapSsfParamForm extends JDialog {

    private TestCapSsfManMBean capSsf;

    private JComboBox cbCapApplicationContext;

    public TestCapSsfParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("CAP SSF test settings");
        setBounds(100, 100, 540, 189);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        cbCapApplicationContext = new JComboBox();
        cbCapApplicationContext.setBounds(266, 11, 255, 20);
        panel.add(cbCapApplicationContext);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 84, 246, 23);
        panel.add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, 84, 255, 23);
        panel.add(button_1);

        JButton button_2 = new JButton("Cancel");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_2.setBounds(404, 118, 117, 23);
        panel.add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, 118, 117, 23);
        panel.add(button_3);

        JButton button_4 = new JButton("Reload");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_4.setBounds(10, 118, 144, 23);
        panel.add(button_4);

        JLabel label = new JLabel("CAP application context");
        label.setBounds(10, 14, 204, 14);
        panel.add(label);
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
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbCapApplicationContext, new CapApplicationContextSsf(
                CapApplicationContextSsf.VAL_CAP_V1_gsmSSF_to_gsmSCF));
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        this.capSsf.setCapApplicationContext((CapApplicationContextSsf) cbCapApplicationContext.getSelectedItem());

        return true;
    }

}
