package org.mobicents.protocols.ss7.tools.traceparser;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Window.Type;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Button;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.Color;

import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class TraceParserForm {

	private MAPTraceParser task = null;

	protected JFrame frmSsTraceParser;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JTextField tfFilePath;
	private JTextField tfApplicationContextFilter;
	private JTextField tfDialogIdFilter;
	private JCheckBox cbApplicationContextFilter;
	private JCheckBox cbDialogIdFilter;
	private JRadioButton rdbtnTpActerna;
	private JButton btnStart;
	private JButton btnStop;
	private JPanel pnMsgLog;
	private JCheckBox cbMsgLog;
	private JButton btnMsgLog;
	private javax.swing.Timer timer;
	private int curMsgCount = 0;

	private static Ss7ParseParameters par = null;
	private JTextField tfMsgLog;
	private JLabel lblMessagesProcessed;
	private JTextField tfMsgCnt;

	public static void main(String[] args) {

		// trying to read the ini-file
		try {
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Ss7ParseParameters.xml"));
			XMLDecoder d = new XMLDecoder(bis);
			par = (Ss7ParseParameters)d.readObject();
			d.close();
		} catch (Exception e) {
			// we ignore exceptions
		}
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TraceParserForm window = new TraceParserForm(par);
					window.frmSsTraceParser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
//		MAPTraceParser task = new MAPTraceParser();
//		task.parse(new TraceReaderDriverActerna(), "e:\\Java_workspace\\Trace\\Test\\222.p01");
//		return;

	}

	/**
	 * Create the application.
	 */
	public TraceParserForm(Ss7ParseParameters par) {
		initialize();
		setParameters(par);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSsTraceParser = new JFrame();
		frmSsTraceParser.setTitle("SS7 Trace Parser");
		frmSsTraceParser.setResizable(false);
		frmSsTraceParser.setBounds(100, 100, 535, 452);
		frmSsTraceParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frmSsTraceParser.getContentPane().add(panel, BorderLayout.SOUTH);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				if (tfFilePath.getText() == null || tfFilePath.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "The FilePath must be set");
					return;
				}

				// saving parameters
				Ss7ParseParameters newPar = new Ss7ParseParameters();

				if (rdbtnTpActerna.isSelected())
					newPar.setFileType(1);

				newPar.setSourceFilePath(tfFilePath.getText());

				if (cbApplicationContextFilter.isSelected()) {
					try {
						newPar.setApplicationContextFilter(Integer.parseInt(tfApplicationContextFilter.getText()));
					} catch (NumberFormatException ee) {
						JOptionPane.showMessageDialog(null,
								"Can not parse ApplicationContextFilter the value. \nIt should be an Integer.\nParsing without ApplicationContextFilter");
					}
				}
				if (cbDialogIdFilter.isSelected()) {
					try {
						newPar.setDialogIdFilter(Long.parseLong(tfDialogIdFilter.getText()));
					} catch (NumberFormatException ee) {
						JOptionPane.showMessageDialog(null,
								"Can not parse ApplicationContextFilter the value. \nIt should be an Integer.\nParsing without ApplicationContextFilter");
					}
				}
				if (cbMsgLog.isSelected()) {
					newPar.setMsgLogFilePath(tfMsgLog.getText());
				}

				try {
					BufferedOutputStream bis = new BufferedOutputStream(new FileOutputStream("Ss7ParseParameters.xml"));
					XMLEncoder d = new XMLEncoder(bis);
					d.writeObject(newPar);
					d.close();
				} catch (Exception ee) {
					ee.printStackTrace();
					JOptionPane.showMessageDialog(null, "Failed when saving the parameter file Ss7ParseParameters.xml: " + ee.getMessage());
				}

				btnStart.setEnabled(false);
				btnStop.setEnabled(true);

				// starting parsing
				task = new MAPTraceParser(newPar);
				task.parse();
			}
		});
		panel.add(btnStart);
		
		btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (task != null)
					task.interrupt();
			}
		});
		btnStop.setEnabled(false);
		panel.add(btnStop);
		
		JPanel panel_1 = new JPanel();
		frmSsTraceParser.getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(10, 11, 509, 58);
		panel_1.add(panel_2);
		panel_2.setLayout(null);
		
		rdbtnTpActerna = new JRadioButton("Acterna");
		buttonGroup.add(rdbtnTpActerna);
		rdbtnTpActerna.setSelected(true);
		rdbtnTpActerna.setBounds(6, 28, 109, 23);
		panel_2.add(rdbtnTpActerna);
		
		JLabel lblSelectAFile = new JLabel("Trace file type");
		lblSelectAFile.setBounds(15, 11, 154, 14);
		panel_2.add(lblSelectAFile);
		
		tfFilePath = new JTextField();
		tfFilePath.setBounds(10, 97, 447, 20);
		panel_1.add(tfFilePath);
		tfFilePath.setColumns(10);
		
		JButton btnFilePath = new JButton(". . .");
		btnFilePath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				TraceFileFilter filter = new TraceFileFilter();
				chooser.setFileFilter(filter);
				File f = new File(tfFilePath.getText());
				chooser.setSelectedFile(f);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnVal = chooser.showOpenDialog(frmSsTraceParser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f2 = chooser.getSelectedFile();
					if (f2 != null && f2.exists())
						tfFilePath.setText(f2.getPath());
					else
						JOptionPane.showMessageDialog(null, "File does not exists - try again");
				}
			}
		});
		btnFilePath.setBounds(467, 96, 52, 23);
		panel_1.add(btnFilePath);
		
		JLabel lblPathToThe = new JLabel("Trace file path");
		lblPathToThe.setBounds(10, 82, 153, 14);
		panel_1.add(lblPathToThe);
		
		this.cbApplicationContextFilter = new JCheckBox("ApplicationContext filter");
		cbApplicationContextFilter.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				tfApplicationContextFilter.setEnabled(cbApplicationContextFilter.isSelected());
			}
		});
		cbApplicationContextFilter.setBounds(10, 126, 210, 23);
		panel_1.add(cbApplicationContextFilter);
		
		cbMsgLog = new JCheckBox("Messages logging");
		cbMsgLog.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				btnMsgLog.setEnabled(cbMsgLog.isSelected());
				tfMsgLog.setEnabled(cbMsgLog.isSelected());
			}
		});
		cbMsgLog.setBounds(10, 188, 210, 23);
		panel_1.add(cbMsgLog);
		
		tfApplicationContextFilter = new JTextField();
		tfApplicationContextFilter.setEnabled(false);
		tfApplicationContextFilter.setBounds(226, 127, 153, 20);
		panel_1.add(tfApplicationContextFilter);
		tfApplicationContextFilter.setColumns(10);
		
		cbDialogIdFilter = new JCheckBox("DialogId filter");
		cbDialogIdFilter.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				tfDialogIdFilter.setEnabled(cbDialogIdFilter.isSelected());
			}
		});
		cbDialogIdFilter.setBounds(10, 157, 210, 23);
		panel_1.add(cbDialogIdFilter);
		
		tfDialogIdFilter = new JTextField();
		tfDialogIdFilter.setEnabled(false);
		tfDialogIdFilter.setColumns(10);
		tfDialogIdFilter.setBounds(226, 158, 153, 20);
		panel_1.add(tfDialogIdFilter);
		
		pnMsgLog = new JPanel();
		pnMsgLog.setBorder(new LineBorder(new Color(0, 0, 0)));
		pnMsgLog.setBounds(10, 219, 509, 58);
		panel_1.add(pnMsgLog);
		pnMsgLog.setLayout(null);
		
		tfMsgLog = new JTextField();
		tfMsgLog.setBounds(10, 27, 433, 20);
		pnMsgLog.add(tfMsgLog);
		tfMsgLog.setEnabled(false);
		tfMsgLog.setColumns(10);
		
		JLabel lblMessageLogFile = new JLabel("Message log file path");
		lblMessageLogFile.setBounds(10, 11, 153, 14);
		pnMsgLog.add(lblMessageLogFile);
		
		timer = new javax.swing.Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (task != null) {
					if (curMsgCount != task.getMsgCount()) {
						curMsgCount = task.getMsgCount();
						String s1 = ((Integer) curMsgCount).toString();
						tfMsgCnt.setText(s1);
					}
					
					if (task.isFinished()) {
						String errorMsg = task.getErrorMessage();
						btnStart.setEnabled(true);
						btnStop.setEnabled(false);
						task = null;

						if (errorMsg != null)
							JOptionPane.showMessageDialog(null, "Error: " + errorMsg);
						else
							JOptionPane.showMessageDialog(null, "Success");
					}
				}
			}
		});
		timer.start();
		
		btnMsgLog = new JButton(". . .");
		btnMsgLog.setEnabled(false);
		btnMsgLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				File f = new File(tfMsgLog.getText());
				chooser.setSelectedFile(f);
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

				int returnVal = chooser.showOpenDialog(frmSsTraceParser);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f2 = chooser.getSelectedFile();
					tfMsgLog.setText(f2.getPath());
				}
			}
		});
		btnMsgLog.setBounds(453, 26, 52, 23);
		pnMsgLog.add(btnMsgLog);
		
		lblMessagesProcessed = new JLabel("Messages processed");
		lblMessagesProcessed.setBounds(10, 300, 140, 14);
		panel_1.add(lblMessagesProcessed);
		
		tfMsgCnt = new JTextField();
		tfMsgCnt.setEditable(false);
		tfMsgCnt.setBounds(160, 297, 86, 20);
		panel_1.add(tfMsgCnt);
		tfMsgCnt.setColumns(10);
	}
	
	private void setParameters(Ss7ParseParameters par) {
		if (par != null) {
			
			switch (par.getFileType()) {
			case 1:
				rdbtnTpActerna.setSelected(true);
				break;
			}
			
			if (par.getSourceFilePath() != null) {
				tfFilePath.setText(par.getSourceFilePath());
			}
			if (par.getApplicationContextFilter() != null) {
				cbApplicationContextFilter.setSelected(true);
				tfApplicationContextFilter.setText(par.getApplicationContextFilter().toString());
			}
			if (par.getDialogIdFilter() != null) {
				cbDialogIdFilter.setSelected(true);
				tfDialogIdFilter.setText(par.getDialogIdFilter().toString());
			}
			if (par.getMsgLogFilePath() != null) {
				cbMsgLog.setSelected(true);
				tfMsgLog.setText(par.getMsgLogFilePath());
			}
		}
	}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
		}
	}
	
	private class TraceFileFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if(f.isDirectory())
				return true;
			
			String s = f.getName();
			int i1 = s.lastIndexOf('.');
			if (i1 > 0) {
				String s1 = s.substring(i1 + 1);
				if (s1.length() == 3 && s1.startsWith("p0"))
					return true;
			}

			return false;
		}

		@Override
		public String getDescription() {
			return "";
		}
		
	}
}

