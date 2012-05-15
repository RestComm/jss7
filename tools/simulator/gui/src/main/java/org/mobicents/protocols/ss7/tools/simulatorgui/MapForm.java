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

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.mobicents.protocols.ss7.tools.simulator.level3.MapManMBean;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class MapForm extends JDialog {

	private MapManMBean map;
	
	private static final long serialVersionUID = -2799708291291364182L;
	private JTextField tbLocalSsn;
	private JTextField tbRemoteSsn;
	private JTextField tbLocalPc;
	private JTextField tbRemotePc;
	private JTextField tbOrigReference;
	private JTextField tbDestReference;

	public MapForm(JFrame owner) {
		super(owner, true);

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setTitle("MAP settings");
		setBounds(100, 100, 535, 324);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblLocalSsn = new JLabel("Local SSN");
		lblLocalSsn.setBounds(10, 76, 90, 14);
		panel.add(lblLocalSsn);
		
		tbLocalSsn = new JTextField();
		tbLocalSsn.setColumns(10);
		tbLocalSsn.setBounds(203, 73, 129, 20);
		panel.add(tbLocalSsn);
		
		JButton button = new JButton("Load default values for side A");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataA();
			}
		});
		button.setBounds(10, 216, 254, 23);
		panel.add(button);
		
		JButton button_1 = new JButton("Load default values for side B");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataB();
			}
		});
		button_1.setBounds(274, 216, 244, 23);
		panel.add(button_1);
		
		JButton button_2 = new JButton("Reload");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reloadData();
			}
		});
		button_2.setBounds(10, 250, 144, 23);
		panel.add(button_2);
		
		JButton button_3 = new JButton("Save");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (saveData()) {
					getJFrame().dispose();
				}
			}
		});
		button_3.setBounds(274, 250, 117, 23);
		panel.add(button_3);
		
		JButton button_4 = new JButton("Cancel");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getJFrame().dispose();
			}
		});
		button_4.setBounds(401, 250, 117, 23);
		panel.add(button_4);
		
		tbRemoteSsn = new JTextField();
		tbRemoteSsn.setColumns(10);
		tbRemoteSsn.setBounds(203, 104, 129, 20);
		panel.add(tbRemoteSsn);
		
		tbLocalPc = new JTextField();
		tbLocalPc.setColumns(10);
		tbLocalPc.setBounds(203, 11, 129, 20);
		panel.add(tbLocalPc);
		
		tbRemotePc = new JTextField();
		tbRemotePc.setColumns(10);
		tbRemotePc.setBounds(203, 42, 129, 20);
		panel.add(tbRemotePc);
		
		JLabel lblLocalPc = new JLabel("Local PC");
		lblLocalPc.setBounds(10, 14, 90, 14);
		panel.add(lblLocalPc);
		
		JLabel lblRemotePc = new JLabel("Remote PC");
		lblRemotePc.setBounds(10, 45, 90, 14);
		panel.add(lblRemotePc);
		
		JLabel lblRemoteSsn = new JLabel("Remote SSN");
		lblRemoteSsn.setBounds(10, 107, 90, 14);
		panel.add(lblRemoteSsn);
		
		tbOrigReference = new JTextField();
		tbOrigReference.setColumns(10);
		tbOrigReference.setBounds(203, 135, 270, 20);
		panel.add(tbOrigReference);
		
		tbDestReference = new JTextField();
		tbDestReference.setColumns(10);
		tbDestReference.setBounds(203, 166, 270, 20);
		panel.add(tbDestReference);
		
		JLabel lblOriginationReferenceString = new JLabel("Origination reference string");
		lblOriginationReferenceString.setBounds(10, 138, 169, 14);
		panel.add(lblOriginationReferenceString);
		
		JLabel lblDestinationReferenceString = new JLabel("Destination reference string");
		lblDestinationReferenceString.setBounds(10, 169, 169, 14);
		panel.add(lblDestinationReferenceString);
	}

	public void setData(MapManMBean map) {
		this.map = map;

		this.reloadData();
	}

	private JDialog getJFrame() {
		return this;
	}

	private void reloadData() {
		tbLocalPc.setText(((Integer) this.map.getLocalPc()).toString());
		tbRemotePc.setText(((Integer) this.map.getRemotePc()).toString());
		tbLocalSsn.setText(((Integer) this.map.getLocalSsn()).toString());
		tbRemoteSsn.setText(((Integer) this.map.getRemoteSsn()).toString());

		tbOrigReference.setText(this.map.getOrigReference());
		tbDestReference.setText(this.map.getDestReference());
	}

	private void loadDataA() {
		tbLocalPc.setText("1");
		tbRemotePc.setText("2");
		tbLocalSsn.setText("8");
		tbRemoteSsn.setText("8");

		tbOrigReference.setText("");
		tbDestReference.setText("");
	}

	private void loadDataB() {
		tbLocalPc.setText("2");
		tbRemotePc.setText("1");
		tbLocalSsn.setText("8");
		tbRemoteSsn.setText("8");

		tbOrigReference.setText("");
		tbDestReference.setText("");
	}

	private boolean saveData() {
		int localPc = 0;
		int remotePc = 0;
		int localSsn = 0;
		int remoteSsn = 0;
		try {
			localPc = Integer.parseInt(tbLocalPc.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Local Pc value: " + e.toString());
			return false;
		}
		try {
			remotePc = Integer.parseInt(tbRemotePc.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Remote Pc value: " + e.toString());
			return false;
		}
		try {
			localSsn = Integer.parseInt(tbLocalSsn.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Local Ssn value: " + e.toString());
			return false;
		}
		try {
			remoteSsn = Integer.parseInt(tbRemoteSsn.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Exception when parsing Remote Ssn value: " + e.toString());
			return false;
		}

		this.map.setLocalPc(localPc);
		this.map.setRemotePc(remotePc);
		this.map.setLocalSsn(localSsn);
		this.map.setRemoteSsn(remoteSsn);

		this.map.setOrigReference(tbOrigReference.getText());
		this.map.setDestReference(tbDestReference.getText());
		
		return true;
	}
}

