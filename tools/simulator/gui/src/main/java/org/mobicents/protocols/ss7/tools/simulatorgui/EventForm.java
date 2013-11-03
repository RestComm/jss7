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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class EventForm extends JDialog {

    private static final long serialVersionUID = 5638054828861986117L;

    private TestingForm testingForm;

    private JTextArea taTime;
    private JTextArea taSource;
    private JTextArea taMessage;
    private JTextArea taUserData;

    public EventForm(TestingForm testingForm) {
        super(testingForm, true);
        setModalityType(ModalityType.MODELESS);

        setBounds(new Rectangle(150, 150, 450, 300));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent arg0) {
                formClosing();
            }
        });
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        this.testingForm = testingForm;

        setTitle("Event details");

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.CENTER);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[] { 0, 0, 0 };
        gbl_panel.rowHeights = new int[] { 20, 0, 0, 0, 0 };
        gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
        gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
        panel.setLayout(gbl_panel);

        JLabel lblTime = new JLabel("Event time");
        GridBagConstraints gbc_lblTime = new GridBagConstraints();
        gbc_lblTime.insets = new Insets(0, 0, 5, 5);
        gbc_lblTime.gridx = 0;
        gbc_lblTime.gridy = 0;
        panel.add(lblTime, gbc_lblTime);

        taTime = new JTextArea();
        taTime.setEditable(false);
        GridBagConstraints gbc_taTime = new GridBagConstraints();
        gbc_taTime.insets = new Insets(0, 0, 5, 0);
        gbc_taTime.fill = GridBagConstraints.BOTH;
        gbc_taTime.gridx = 1;
        gbc_taTime.gridy = 0;
        panel.add(taTime, gbc_taTime);

        JLabel lblSource = new JLabel("Source");
        GridBagConstraints gbc_lblSource = new GridBagConstraints();
        gbc_lblSource.insets = new Insets(0, 0, 5, 5);
        gbc_lblSource.gridx = 0;
        gbc_lblSource.gridy = 1;
        panel.add(lblSource, gbc_lblSource);

        taSource = new JTextArea();
        taSource.setEditable(false);
        GridBagConstraints gbc_taSource = new GridBagConstraints();
        gbc_taSource.insets = new Insets(0, 0, 5, 0);
        gbc_taSource.fill = GridBagConstraints.BOTH;
        gbc_taSource.gridx = 1;
        gbc_taSource.gridy = 1;
        panel.add(taSource, gbc_taSource);

        JLabel lblMessage = new JLabel("Message");
        GridBagConstraints gbc_lblMessage = new GridBagConstraints();
        gbc_lblMessage.insets = new Insets(0, 0, 5, 5);
        gbc_lblMessage.gridx = 0;
        gbc_lblMessage.gridy = 2;
        panel.add(lblMessage, gbc_lblMessage);

        taMessage = new JTextArea();
        taMessage.setEditable(false);
        GridBagConstraints gbc_taMessage = new GridBagConstraints();
        gbc_taMessage.insets = new Insets(0, 0, 5, 0);
        gbc_taMessage.fill = GridBagConstraints.BOTH;
        gbc_taMessage.gridx = 1;
        gbc_taMessage.gridy = 2;
        panel.add(taMessage, gbc_taMessage);

        JLabel lblUserdata = new JLabel("UserData");
        GridBagConstraints gbc_lblUserdata = new GridBagConstraints();
        gbc_lblUserdata.insets = new Insets(0, 0, 0, 5);
        gbc_lblUserdata.gridx = 0;
        gbc_lblUserdata.gridy = 3;
        panel.add(lblUserdata, gbc_lblUserdata);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 1;
        gbc_scrollPane.gridy = 3;
        panel.add(scrollPane, gbc_scrollPane);

        taUserData = new JTextArea();
        taUserData.setEditable(false);
        taUserData.setLineWrap(true);
        scrollPane.setViewportView(taUserData);
    }

    public void setData(String eventTime, String source, String msg, String userData) {
        this.taTime.setText(eventTime);
        this.taSource.setText(source);
        this.taMessage.setText(msg);
        this.taUserData.setText(userData);
    }

    private void formClosing() {
        this.testingForm.eventFormClose();
    }
}
