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

package org.restcomm.protocols.ss7.tools.simulatorgui.tests.ati;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.restcomm.protocols.ss7.tools.simulator.tests.ati.ATIReaction;
import org.restcomm.protocols.ss7.tools.simulator.tests.ati.TestAtiServerManMBean;
import org.restcomm.protocols.ss7.tools.simulatorgui.M3uaForm;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JComboBox;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestAtiServerParamForm extends JDialog {

    private static final long serialVersionUID = -6495066621733616114L;

    private TestAtiServerManMBean atiServer;

    private JComboBox cbATIReaction = new JComboBox();

    public TestAtiServerParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("ATI test server settings");
        setBounds(100, 100, 640, 226);
        getContentPane().setLayout(null);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataA();
            }
        });
        button.setBounds(10, 123, 246, 23);
        getContentPane().add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, 123, 255, 23);
        getContentPane().add(button_1);

        JButton button_2 = new JButton("Cancel");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_2.setBounds(404, 157, 117, 23);
        getContentPane().add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, 157, 117, 23);
        getContentPane().add(button_3);

        JButton button_4 = new JButton("Reload");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_4.setBounds(10, 157, 144, 23);
        getContentPane().add(button_4);

        JLabel lblReactionForAti = new JLabel("Reaction for ATI request");
        lblReactionForAti.setBounds(10, 14, 290, 14);
        getContentPane().add(lblReactionForAti);

        cbATIReaction = new JComboBox();
        cbATIReaction.setBounds(310, 11, 309, 20);
        getContentPane().add(cbATIReaction);
    }

    public void setData(TestAtiServerManMBean atiServer) {
        this.atiServer = atiServer;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbATIReaction, this.atiServer.getATIReaction());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbATIReaction, new ATIReaction(ATIReaction.VAL_RETURN_SUCCESS));
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        this.atiServer.setATIReaction((ATIReaction) cbATIReaction.getSelectedItem());

        return true;
    }
}
