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

package org.mobicents.protocols.ss7.tools.traceparser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import org.apache.log4j.BasicConfigurator;

import org.mobicents.protocols.ss7.sccp.SccpProtocolVersion;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TraceParserForm {

    private SS7TraceParser task = null;
    private String parseData = "";
    private String persistDir = null;
    private String persistFile = "Ss7ParseParameters.xml";

    protected JFrame frmSsTraceParser;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JTextField tfFilePath;
    private JTextField tfApplicationContextFilter;
    private JTextField tfDialogIdFilter;
    private JCheckBox cbApplicationContextFilter;
    private JCheckBox cbDialogIdFilter;
    private JRadioButton rdbtnTpActerna;
    private JRadioButton rdbtnTpSimpleSeq;
    private JRadioButton rdbtnTpPcap;
    private JRadioButton rdbtnHexStream;
    private JButton btnStart;
    private JButton btnStop;
    private JPanel pnMsgLog;
    private JCheckBox cbMsgLog;
    private JButton btnMsgLog;
    private javax.swing.Timer timer;
    private int curMsgCount = 0;
    private JCheckBox cbOpcDpcFilter;
    private JCheckBox cbRecordingDialogMessageChain;
    private JButton btnRecordingDialogMessageChain;

    private static Ss7ParseParameters par = null;
    private JTextField tfMsgLog;
    private JLabel lblMessagesPerformed;
    private JTextField tfMsgCnt;
    private JCheckBox cbTcapData;
    private JCheckBox cbDialogDet;
    private JCheckBox cbCompDet;
    private JTextField tfDialogIdFilter2;
    private JPanel panel_3;
    private JRadioButton rdbtnMap;
    private JLabel lblProtocol;
    private JRadioButton rdbtnCap;
    private JRadioButton rdbtnIsup;
    private JRadioButton rdbtnITU;
    private JRadioButton rdbtnANSI;
    private final ButtonGroup buttonGroup_1 = new ButtonGroup();
    private JButton btnGetStatisticInfo;
    private JTextField tfOpcDpcFilter;
    private JTextField tfRecordingDialogMessageChain;
    private final ButtonGroup buttonGroup_2 = new ButtonGroup();

    public static void main(String[] args) {

        setupLog4j();

        // trying to read the ini-file
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream("Ss7ParseParameters.xml"));
            XMLDecoder d = new XMLDecoder(bis);
            par = (Ss7ParseParameters) d.readObject();
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

        // MAPTraceParser task = new MAPTraceParser();
        // task.parse(new TraceReaderDriverActerna(), "e:\\Java_workspace\\Trace\\Test\\222.p01");
        // return;

    }

    private static void setupLog4j() {
        BasicConfigurator.configure();
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
        frmSsTraceParser.setBounds(100, 100, 569, 716);
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
                    newPar.setFileTypeN(ParseDriverType.Acterna);
                if (rdbtnTpSimpleSeq.isSelected())
                    newPar.setFileTypeN(ParseDriverType.SimpleSeq);
                if (rdbtnTpPcap.isSelected())
                    newPar.setFileTypeN(ParseDriverType.Pcap);
                if (rdbtnHexStream.isSelected())
                    newPar.setFileTypeN(ParseDriverType.HexStream);

                if (rdbtnMap.isSelected())
                    newPar.setParseProtocol(ParseProtocol.Map);
                if (rdbtnCap.isSelected())
                    newPar.setParseProtocol(ParseProtocol.Cap);
                if (rdbtnIsup.isSelected())
                    newPar.setParseProtocol(ParseProtocol.Isup);

                if (rdbtnITU.isSelected())
                    newPar.setSccpProtocolVersion(SccpProtocolVersion.ITU);
                if (rdbtnANSI.isSelected())
                    newPar.setSccpProtocolVersion(SccpProtocolVersion.ANSI);

                newPar.setSourceFilePath(tfFilePath.getText());

                if (cbApplicationContextFilter.isSelected()) {
                    try {
                        newPar.setApplicationContextFilter(Integer.parseInt(tfApplicationContextFilter.getText()));
                    } catch (NumberFormatException ee) {
                        JOptionPane
                                .showMessageDialog(null,
                                        "Can not parse ApplicationContextFilter the value. \nIt should be an Integer.\nParsing without ApplicationContextFilter");
                    }
                }
                if (cbDialogIdFilter.isSelected()) {
                    try {
                        if (!tfDialogIdFilter.getText().equals(""))
                            newPar.setDialogIdFilter(Long.parseLong(tfDialogIdFilter.getText()));
                    } catch (NumberFormatException ee) {
                        JOptionPane
                                .showMessageDialog(null,
                                        "Can not parse ApplicationContextFilter the value. \nIt should be an Integer.\nParsing without ApplicationContextFilter");
                    }
                    try {
                        if (!tfDialogIdFilter2.getText().equals(""))
                            newPar.setDialogIdFilter2(Long.parseLong(tfDialogIdFilter2.getText()));
                    } catch (NumberFormatException ee) {
                        JOptionPane
                                .showMessageDialog(null,
                                        "Can not parse ApplicationContextFilter2 the value. \nIt should be an Integer.\nParsing without ApplicationContextFilter2");
                    }
                }
                if (cbOpcDpcFilter.isSelected()) {
                    try {
                        String[] ss = tfOpcDpcFilter.getText().split(",");
                        int ii1 = ss.length;
                        Integer[] ires = new Integer[ii1];
                        for (int i1 = 0; i1 < ii1; i1++) {
                            String s = ss[i1];
                            s = s.trim();
                            ires[i1] = Integer.parseInt(s);
                        }
                        newPar.setOpcDpcFilter(ires);
                    } catch (NumberFormatException ee) {
                        JOptionPane.showMessageDialog(null,
                                "Can not parse OpcDpcFilter the value. \nIt should be a set of Integers.\nParsing without OpcDpcFilter");
                    }
                }
                if (cbMsgLog.isSelected()) {
                    newPar.setMsgLogFilePath(tfMsgLog.getText());
                }
                if (cbTcapData.isSelected()) {
                    newPar.setTcapMsgData(true);
                }
                if (cbDialogDet.isSelected()) {
                    newPar.setDetailedDialog(true);
                }
                if (cbCompDet.isSelected()) {
                    newPar.setDetailedComponents(true);
                }
                if (cbRecordingDialogMessageChain.isSelected()) {
                    newPar.setMessageChainFilePath(tfRecordingDialogMessageChain.getText());
                }

                try {
                    BufferedOutputStream bis = new BufferedOutputStream(new FileOutputStream(persistFile));
                    XMLEncoder d = new XMLEncoder(bis);
                    d.writeObject(newPar);
                    d.close();
                } catch (Exception ee) {
                    ee.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Failed when saving the parameter file Ss7ParseParameters.xml: " + ee.getMessage());
                }

                btnStart.setEnabled(false);
                btnGetStatisticInfo.setEnabled(false);
                btnStop.setEnabled(true);

                // starting parsing
                task = new SS7TraceParser(persistDir, newPar);
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

        btnGetStatisticInfo = new JButton("Get statistic info");
        btnGetStatisticInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                getStatisticInfo();
            }
        });
        panel.add(btnGetStatisticInfo);

        JPanel panel_1 = new JPanel();
        frmSsTraceParser.getContentPane().add(panel_1, BorderLayout.CENTER);
        panel_1.setLayout(null);

        JPanel panel_2 = new JPanel();
        panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_2.setBounds(10, 11, 543, 58);
        panel_1.add(panel_2);
        panel_2.setLayout(null);

        rdbtnTpActerna = new JRadioButton("Acterna");
        rdbtnTpActerna.setSelected(true);
        buttonGroup.add(rdbtnTpActerna);
        rdbtnTpActerna.setBounds(6, 28, 109, 23);
        panel_2.add(rdbtnTpActerna);

        JLabel lblSelectAFile = new JLabel("Trace file type");
        lblSelectAFile.setBounds(15, 11, 154, 14);
        panel_2.add(lblSelectAFile);

        rdbtnTpSimpleSeq = new JRadioButton("Simple sequence");
        buttonGroup.add(rdbtnTpSimpleSeq);
        rdbtnTpSimpleSeq.setBounds(137, 28, 126, 23);
        panel_2.add(rdbtnTpSimpleSeq);

        rdbtnTpPcap = new JRadioButton("Pcap");
        buttonGroup.add(rdbtnTpPcap);
        rdbtnTpPcap.setBounds(275, 28, 101, 23);
        panel_2.add(rdbtnTpPcap);

        rdbtnHexStream = new JRadioButton("Hex stream");
        buttonGroup.add(rdbtnHexStream);
        rdbtnHexStream.setBounds(380, 27, 127, 25);
        panel_2.add(rdbtnHexStream);

        tfFilePath = new JTextField();
        tfFilePath.setBounds(20, 187, 481, 20);
        panel_1.add(tfFilePath);
        tfFilePath.setColumns(10);

        JButton btnFilePath = new JButton(". . .");
        btnFilePath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                String filterName = null;
                if (rdbtnTpActerna.isSelected())
                    filterName = "Acterna";
                if (rdbtnTpSimpleSeq.isSelected())
                    filterName = "Simple";
                if (rdbtnTpPcap.isSelected())
                    filterName = "Pcap";
                if (rdbtnHexStream.isSelected())
                    filterName = "HexStream";
                TraceFileFilter filter = new TraceFileFilter(filterName);
                chooser.setFileFilter(filter);
                chooser.addChoosableFileFilter(filter);
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
        btnFilePath.setBounds(511, 186, 52, 23);
        panel_1.add(btnFilePath);

        JLabel lblPathToThe = new JLabel("Trace file path");
        lblPathToThe.setBounds(20, 172, 153, 14);
        panel_1.add(lblPathToThe);

        this.cbApplicationContextFilter = new JCheckBox("ApplicationContext filter");
        cbApplicationContextFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                tfApplicationContextFilter.setEnabled(cbApplicationContextFilter.isSelected());
            }
        });
        cbApplicationContextFilter.setBounds(20, 216, 210, 23);
        panel_1.add(cbApplicationContextFilter);

        cbMsgLog = new JCheckBox("Messages logging");
        cbMsgLog.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                btnMsgLog.setEnabled(cbMsgLog.isSelected());
                tfMsgLog.setEnabled(cbMsgLog.isSelected());
                cbTcapData.setEnabled(cbMsgLog.isSelected());
                cbDialogDet.setEnabled(cbMsgLog.isSelected());
                cbCompDet.setEnabled(cbMsgLog.isSelected());
            }
        });
        cbMsgLog.setBounds(20, 308, 210, 23);
        panel_1.add(cbMsgLog);

        tfApplicationContextFilter = new JTextField();
        tfApplicationContextFilter.setEnabled(false);
        tfApplicationContextFilter.setBounds(236, 217, 153, 20);
        panel_1.add(tfApplicationContextFilter);
        tfApplicationContextFilter.setColumns(10);

        cbDialogIdFilter = new JCheckBox("DialogId filter");
        cbDialogIdFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                tfDialogIdFilter.setEnabled(cbDialogIdFilter.isSelected());
                tfDialogIdFilter2.setEnabled(cbDialogIdFilter.isSelected());
            }
        });
        cbDialogIdFilter.setBounds(20, 247, 210, 23);
        panel_1.add(cbDialogIdFilter);

        tfDialogIdFilter = new JTextField();
        tfDialogIdFilter.setEnabled(false);
        tfDialogIdFilter.setColumns(10);
        tfDialogIdFilter.setBounds(236, 248, 153, 20);
        panel_1.add(tfDialogIdFilter);

        pnMsgLog = new JPanel();
        pnMsgLog.setBorder(new LineBorder(new Color(0, 0, 0)));
        pnMsgLog.setBounds(10, 338, 553, 136);
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
                        btnGetStatisticInfo.setEnabled(true);
                        btnStop.setEnabled(false);
                        parseData = task.getStatisticData();
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

        cbTcapData = new JCheckBox("Store in the log TCAP message source data");
//        cbTcapData.addItemListener(new ItemListener() {
//            public void itemStateChanged(ItemEvent e) {
//                cbDialogDet.setEnabled(cbTcapData.isSelected());
//                cbCompDet.setEnabled(cbTcapData.isSelected());
//            }
//        });
        cbTcapData.setBounds(10, 50, 296, 23);
        pnMsgLog.add(cbTcapData);

        cbDialogDet = new JCheckBox("Write dialog portion details");
        cbDialogDet.setBounds(10, 76, 296, 23);
        pnMsgLog.add(cbDialogDet);

        cbCompDet = new JCheckBox("Write components portion details");
        cbCompDet.setBounds(10, 102, 296, 23);
        pnMsgLog.add(cbCompDet);

        lblMessagesPerformed = new JLabel("Messages performed");
        lblMessagesPerformed.setBounds(10, 592, 140, 14);
        panel_1.add(lblMessagesPerformed);

        tfMsgCnt = new JTextField();
        tfMsgCnt.setEditable(false);
        tfMsgCnt.setBounds(160, 589, 86, 20);
        panel_1.add(tfMsgCnt);
        tfMsgCnt.setColumns(10);

        tfDialogIdFilter2 = new JTextField();
        tfDialogIdFilter2.setEnabled(false);
        tfDialogIdFilter2.setColumns(10);
        tfDialogIdFilter2.setBounds(399, 248, 153, 20);
        panel_1.add(tfDialogIdFilter2);

        panel_3 = new JPanel();
        panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_3.setLayout(null);
        panel_3.setBounds(10, 69, 543, 58);
        panel_1.add(panel_3);

        rdbtnMap = new JRadioButton("MAP");
        buttonGroup_1.add(rdbtnMap);
        rdbtnMap.setSelected(true);
        rdbtnMap.setBounds(6, 28, 109, 23);
        panel_3.add(rdbtnMap);

        lblProtocol = new JLabel("Protocol");
        lblProtocol.setBounds(15, 11, 154, 14);
        panel_3.add(lblProtocol);

        rdbtnCap = new JRadioButton("CAP");
        buttonGroup_1.add(rdbtnCap);
        rdbtnCap.setBounds(119, 28, 99, 23);
        panel_3.add(rdbtnCap);

        rdbtnIsup = new JRadioButton("ISUP");
        buttonGroup_1.add(rdbtnIsup);
        rdbtnIsup.setBounds(228, 28, 99, 23);
        panel_3.add(rdbtnIsup);

        cbOpcDpcFilter = new JCheckBox("Opc / Dpc filter (Ex: \"120,130\")");
        cbOpcDpcFilter.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent arg0) {
                tfOpcDpcFilter.setEnabled(cbOpcDpcFilter.isSelected());
            }
        });
        cbOpcDpcFilter.setBounds(20, 278, 210, 23);
        panel_1.add(cbOpcDpcFilter);

        tfOpcDpcFilter = new JTextField();
        tfOpcDpcFilter.setEnabled(false);
        tfOpcDpcFilter.setColumns(10);
        tfOpcDpcFilter.setBounds(236, 279, 317, 20);
        panel_1.add(tfOpcDpcFilter);

        cbRecordingDialogMessageChain = new JCheckBox("Recording of dialog message chains log");
        cbRecordingDialogMessageChain.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                tfRecordingDialogMessageChain.setEnabled(cbRecordingDialogMessageChain.isSelected());
                btnRecordingDialogMessageChain.setEnabled(cbRecordingDialogMessageChain.isSelected());
            }
        });
        cbRecordingDialogMessageChain.setBounds(20, 486, 338, 23);
        panel_1.add(cbRecordingDialogMessageChain);

        JPanel pnDialogMessageMessage = new JPanel();
        pnDialogMessageMessage.setBorder(new LineBorder(new Color(0, 0, 0)));
        pnDialogMessageMessage.setBounds(10, 516, 553, 65);
        panel_1.add(pnDialogMessageMessage);
        pnDialogMessageMessage.setLayout(null);

        JLabel lblDialogMessageMessage = new JLabel("Dialog message message chains log file path");
        lblDialogMessageMessage.setBounds(10, 11, 265, 14);
        pnDialogMessageMessage.add(lblDialogMessageMessage);

        tfRecordingDialogMessageChain = new JTextField();
        tfRecordingDialogMessageChain.setBounds(10, 27, 433, 20);
        pnDialogMessageMessage.add(tfRecordingDialogMessageChain);
        tfRecordingDialogMessageChain.setEnabled(false);
        tfRecordingDialogMessageChain.setColumns(10);

        btnRecordingDialogMessageChain = new JButton(". . .");
        btnRecordingDialogMessageChain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
                File f = new File(tfRecordingDialogMessageChain.getText());
                chooser.setSelectedFile(f);
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                int returnVal = chooser.showOpenDialog(frmSsTraceParser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File f2 = chooser.getSelectedFile();
                    tfRecordingDialogMessageChain.setText(f2.getPath());
                }
            }
        });
        btnRecordingDialogMessageChain.setBounds(453, 26, 52, 23);
        pnDialogMessageMessage.add(btnRecordingDialogMessageChain);
        btnRecordingDialogMessageChain.setEnabled(false);

        JPanel panel_4 = new JPanel();
        panel_4.setLayout(null);
        panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_4.setBounds(10, 128, 543, 37);
        panel_1.add(panel_4);

        rdbtnITU = new JRadioButton("ITU-T");
        buttonGroup_2.add(rdbtnITU);
        rdbtnITU.setSelected(true);
        rdbtnITU.setBounds(200, 7, 109, 23);
        panel_4.add(rdbtnITU);

        JLabel lblProtocolMua = new JLabel("Protocol M3ua / SCCP");
        lblProtocolMua.setBounds(15, 11, 154, 14);
        panel_4.add(lblProtocolMua);

        rdbtnANSI = new JRadioButton("ANSI");
        buttonGroup_2.add(rdbtnANSI);
        rdbtnANSI.setBounds(313, 7, 99, 23);
        panel_4.add(rdbtnANSI);
    }

    private void getStatisticInfo() {
        StatisticResultFm fm = new StatisticResultFm(frmSsTraceParser, parseData);
        fm.setVisible(true);
    }

    private void setParameters(Ss7ParseParameters par) {
        if (par != null) {

            switch (par.getFileTypeN()) {
                case Acterna:
                    rdbtnTpActerna.setSelected(true);
                    break;
                case SimpleSeq:
                    rdbtnTpSimpleSeq.setSelected(true);
                    break;
                case Pcap:
                    rdbtnTpPcap.setSelected(true);
                    break;
                case HexStream:
                    rdbtnHexStream.setSelected(true);
                    break;
            }

            switch (par.getParseProtocol()) {
                case Map:
                    rdbtnMap.setSelected(true);
                    break;
                case Cap:
                    rdbtnCap.setSelected(true);
                    break;
                case Isup:
                    rdbtnIsup.setSelected(true);
                    break;
            }

            switch (par.getSccpProtocolVersion()) {
                case ITU:
                    rdbtnITU.setSelected(true);
                    break;
                case ANSI:
                    rdbtnANSI.setSelected(true);
                    break;
            }

            if (par.getSourceFilePath() != null) {
                tfFilePath.setText(par.getSourceFilePath());
            }
            if (par.getApplicationContextFilter() != null) {
                cbApplicationContextFilter.setSelected(true);
                tfApplicationContextFilter.setText(par.getApplicationContextFilter().toString());
            }
            if (par.getDialogIdFilter() != null || par.getDialogIdFilter2() != null) {
                cbDialogIdFilter.setSelected(true);
                if (par.getDialogIdFilter() != null)
                    tfDialogIdFilter.setText(par.getDialogIdFilter().toString());
                if (par.getDialogIdFilter2() != null)
                    tfDialogIdFilter2.setText(par.getDialogIdFilter2().toString());
            }
            if (par.getOpcDpcFilter() != null) {
                cbOpcDpcFilter.setSelected(true);
                StringBuilder sb = new StringBuilder();
                if (par.getOpcDpcFilter() != null) {
                    boolean nonFirst = false;
                    for (Integer I1 : par.getOpcDpcFilter()) {
                        if (I1 != null) {
                            int i1 = I1;
                            if (nonFirst)
                                sb.append(", ");
                            else
                                nonFirst = true;
                            sb.append(i1);
                        }
                    }
                }
                tfOpcDpcFilter.setText(sb.toString());
            }
            if (par.getMsgLogFilePath() != null) {
                cbMsgLog.setSelected(true);
                tfMsgLog.setText(par.getMsgLogFilePath());
            }
            if (par.getTcapMsgData()) {
                cbTcapData.setSelected(true);
            }
            if (par.getDetailedDialog()) {
                cbDialogDet.setSelected(true);
            }
            if (par.getDetailedComponents()) {
                cbCompDet.setSelected(true);
            }
            if (par.getMessageChainFilePath() != null) {
                cbRecordingDialogMessageChain.setSelected(true);
                tfRecordingDialogMessageChain.setText(par.getMessageChainFilePath());
            }
        }
    }

    /**
     * @return the persistenceDir
     */
    public String getPersistDir() {
        return persistDir;
    }

    /**
     * @param persistenceDir the persistenceDir to set
     */
    public void setPersistDir(String persistDir) {
        this.persistDir = persistDir;
        this.persistFile = persistDir + File.separator + "Ss7ParseParameters.xml";
    }

    private class SwingAction extends AbstractAction {
        public SwingAction() {
            putValue(NAME, "SwingAction");
            putValue(SHORT_DESCRIPTION, "Some short description");
        }

        public void actionPerformed(ActionEvent e) {
        }
    }
}
