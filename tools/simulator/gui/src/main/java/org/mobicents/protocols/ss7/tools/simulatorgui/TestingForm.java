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

import javax.management.Notification;
import javax.swing.JDialog;
import javax.swing.JFrame;

import org.mobicents.protocols.ss7.tools.simulator.management.TesterHostMBean;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.JScrollPane;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.Dialog.ModalityType;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TestingForm extends JDialog {

	private static final long serialVersionUID = -3725574796168603069L;

	private DefaultTableModel model = new DefaultTableModel();
	private javax.swing.Timer tm;

	private JPanel panel;
	protected TesterHostMBean host;
	private JTextField tbL1State;
	private JTable tNotif;
	private JButton btStart;
	private JButton btStop;
	private JButton btRefresh;
	private JPanel panel_b;
	protected JPanel panel_c;
	private JPanel panel_a_1;
	private JLabel lblLState_1;
	private JPanel panel_a_but;
	private JPanel panel_a;
	private JScrollPane scrollPane;
	private JLabel lblLState;
	private JTextField tbL2State;
	private JLabel lblLState_2;
	private JTextField tbL3State;
	private JLabel lblTestingState;
	private JLabel lbTestState;
	
	public TestingForm(JFrame owner) {
		super(owner, true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (getDefaultCloseOperation() == JDialog.DO_NOTHING_ON_CLOSE) {
					JOptionPane.showMessageDialog(getJFrame(), "Before exiting you must Stop the testing process");
				}
			}
		});
		setBounds(100, 100, 772, 677);
		
		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(3, 1, 0, 0));
		
		panel_a = new JPanel();
		panel.add(panel_a);
		panel_a.setLayout(new BorderLayout(0, 0));
		
		panel_a_1 = new JPanel();
		panel_a.add(panel_a_1);
		GridBagLayout gbl_panel_a_1 = new GridBagLayout();
		gbl_panel_a_1.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_a_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel_a_1.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_a_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_a_1.setLayout(gbl_panel_a_1);
		
		lblLState_1 = new JLabel("L1 state");
		GridBagConstraints gbc_lblLState_1 = new GridBagConstraints();
		gbc_lblLState_1.weightx = 1.0;
		gbc_lblLState_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblLState_1.gridx = 0;
		gbc_lblLState_1.gridy = 0;
		panel_a_1.add(lblLState_1, gbc_lblLState_1);
		
		tbL1State = new JTextField();
		GridBagConstraints gbc_tbL1State = new GridBagConstraints();
		gbc_tbL1State.insets = new Insets(0, 0, 5, 5);
		gbc_tbL1State.fill = GridBagConstraints.HORIZONTAL;
		gbc_tbL1State.weightx = 4.0;
		gbc_tbL1State.gridx = 1;
		gbc_tbL1State.gridy = 0;
		panel_a_1.add(tbL1State, gbc_tbL1State);
		tbL1State.setMinimumSize(new Dimension(100, 20));
		tbL1State.setPreferredSize(new Dimension(400, 20));
		tbL1State.setEditable(false);
		tbL1State.setColumns(10);
		
		lblLState = new JLabel("L2 state");
		GridBagConstraints gbc_lblLState = new GridBagConstraints();
		gbc_lblLState.insets = new Insets(0, 0, 5, 5);
		gbc_lblLState.gridx = 0;
		gbc_lblLState.gridy = 1;
		panel_a_1.add(lblLState, gbc_lblLState);
		
		tbL2State = new JTextField();
		tbL2State.setPreferredSize(new Dimension(400, 20));
		tbL2State.setMinimumSize(new Dimension(100, 20));
		tbL2State.setEditable(false);
		tbL2State.setColumns(10);
		GridBagConstraints gbc_tbL2State = new GridBagConstraints();
		gbc_tbL2State.insets = new Insets(0, 0, 5, 5);
		gbc_tbL2State.fill = GridBagConstraints.HORIZONTAL;
		gbc_tbL2State.gridx = 1;
		gbc_tbL2State.gridy = 1;
		panel_a_1.add(tbL2State, gbc_tbL2State);
		
		lblLState_2 = new JLabel("L3 state");
		GridBagConstraints gbc_lblLState_2 = new GridBagConstraints();
		gbc_lblLState_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblLState_2.gridx = 0;
		gbc_lblLState_2.gridy = 2;
		panel_a_1.add(lblLState_2, gbc_lblLState_2);
		
		tbL3State = new JTextField();
		tbL3State.setPreferredSize(new Dimension(400, 20));
		tbL3State.setMinimumSize(new Dimension(100, 20));
		tbL3State.setEditable(false);
		tbL3State.setColumns(10);
		GridBagConstraints gbc_tbL3State = new GridBagConstraints();
		gbc_tbL3State.insets = new Insets(0, 0, 5, 5);
		gbc_tbL3State.fill = GridBagConstraints.HORIZONTAL;
		gbc_tbL3State.gridx = 1;
		gbc_tbL3State.gridy = 2;
		panel_a_1.add(tbL3State, gbc_tbL3State);
		
		lblTestingState = new JLabel("Testing state");
		GridBagConstraints gbc_lblTestingState = new GridBagConstraints();
		gbc_lblTestingState.gridheight = 3;
		gbc_lblTestingState.insets = new Insets(0, 0, 5, 5);
		gbc_lblTestingState.gridx = 0;
		gbc_lblTestingState.gridy = 3;
		panel_a_1.add(lblTestingState, gbc_lblTestingState);
		
		lbTestState = new JLabel("-");
		GridBagConstraints gbc_lbTestState = new GridBagConstraints();
		gbc_lbTestState.gridheight = 3;
		gbc_lbTestState.insets = new Insets(0, 0, 0, 5);
		gbc_lbTestState.gridx = 1;
		gbc_lbTestState.gridy = 3;
		panel_a_1.add(lbTestState, gbc_lbTestState);
		
		panel_a_but = new JPanel();
		panel_a.add(panel_a_but, BorderLayout.SOUTH);
		
		btStart = new JButton("Start");
		panel_a_but.add(btStart);
		
		btStop = new JButton("Stop");
		panel_a_but.add(btStop);
		btStop.setEnabled(false);
		
		btRefresh = new JButton("Refresh state");
		panel_a_but.add(btRefresh);
		btRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshState();
			}
		});
		btStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					host.stop();
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(getJFrame(), "Exception: " + e1.toString());
				}
				btStart.setEnabled(true);
				btStop.setEnabled(false);
				setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			}
		});
		btStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btStart.setEnabled(false);
				btStop.setEnabled(true);
				setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
				try {
					host.start();
				} catch (Throwable e1) {
					JOptionPane.showMessageDialog(getJFrame(), "Exception: " + e1.toString());
				}
			}
		});
		
		panel_b = new JPanel();
		panel.add(panel_b);
		panel_b.setLayout(new GridLayout(1, 1, 0, 0));

		tNotif = new JTable();
		tNotif.setFillsViewportHeight(true);
		tNotif.setBorder(new LineBorder(new Color(0, 0, 0)));
		tNotif.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tNotif.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"TimeStamp", "Source", "Message", "UserData"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tNotif.getColumnModel().getColumn(0).setPreferredWidth(46);
		tNotif.getColumnModel().getColumn(1).setPreferredWidth(89);
		tNotif.getColumnModel().getColumn(2).setPreferredWidth(221);
		tNotif.getColumnModel().getColumn(3).setPreferredWidth(254);

		scrollPane = new JScrollPane(tNotif);
		panel_b.add(scrollPane);

		model = (DefaultTableModel) tNotif.getModel();
		
		panel_c = new JPanel();
		panel.add(panel_c);
		panel_c.setLayout(new BorderLayout(0, 0));
	}

	public void setData(final TesterHostMBean host) {
		this.host = host;

		this.tm = new javax.swing.Timer(5000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refreshState();
			}
		});
		this.tm.start();
	}

	private JDialog getJFrame() {
		return this;
	}

	public void refreshState() {

		tbL1State.setText(this.host.getL1State());
		tbL2State.setText(this.host.getL2State());
		tbL3State.setText(this.host.getL3State());
		lbTestState.setText(this.host.getTestTaskState());
	}

	public void sendNotif(Notification notif) {
		Vector newRow = new Vector();
		newRow.add(notif.getTimeStamp());
		newRow.add(notif.getType());
		newRow.add(notif.getMessage());
		newRow.add(notif.getUserData());
		model.getDataVector().add(0,newRow);

		model.newRowsAdded(new TableModelEvent(model));
	}
}
