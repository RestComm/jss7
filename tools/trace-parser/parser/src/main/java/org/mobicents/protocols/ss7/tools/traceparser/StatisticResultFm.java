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

package org.mobicents.protocols.ss7.tools.traceparser;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JScrollPane;

/**
*
* @author sergey vetyutnev
*
*/
public class StatisticResultFm extends JDialog {

    public StatisticResultFm(JFrame owner, String data) {
        super(owner, true);

        setBounds(new Rectangle(150, 150, 450, 350));

        JScrollPane scrollPane = new JScrollPane();
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        textArea.setText(data);
    }

}
