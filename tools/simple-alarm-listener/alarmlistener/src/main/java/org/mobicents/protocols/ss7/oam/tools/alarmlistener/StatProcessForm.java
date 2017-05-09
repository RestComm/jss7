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

package org.mobicents.protocols.ss7.oam.tools.alarmlistener;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

import java.awt.GridLayout;
import java.awt.Component;

import javax.swing.JSplitPane;

import org.mobicents.protocols.ss7.oam.common.statistics.CounterProviderManagementMBean;
import org.mobicents.protocols.ss7.oam.common.statistics.api.ComplexValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterCampaign;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDef;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterDefSet;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterType;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValue;
import org.mobicents.protocols.ss7.oam.common.statistics.api.CounterValueSet;

/**
*
* @author sergey vetyutnev
*
*/
public class StatProcessForm extends JDialog {

    private JTextArea taResult;
    private CounterProviderManagementMBean statHost;
    private JTextField tbCampaignName;
    private JTextField tbCounterSetName;
    private JTextField tbDuration;
    private JCheckBox cbShortCampaign;

    /**
     * Create the dialog.
     */
    public StatProcessForm(CounterProviderManagementMBean statHost) {

        this.statHost = statHost;

        setBounds(100, 100, 916, 721);
        getContentPane().setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane();
        splitPane.setResizeWeight(0.5);
        getContentPane().add(splitPane, BorderLayout.CENTER);

        JPanel panelA = new JPanel();
        splitPane.setLeftComponent(panelA);
        panelA.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelA.setAlignmentY(Component.TOP_ALIGNMENT);
        panelA.setLayout(null);
        {
            JButton btGetCountersSetList = new JButton("getCounterDefSetList");
            btGetCountersSetList.setBounds(10, 11, 159, 25);
            panelA.add(btGetCountersSetList);

            JButton btCounterDefSet = new JButton("getCounterDefSet");
            btCounterDefSet.setBounds(10, 44, 159, 25);
            panelA.add(btCounterDefSet);

            JButton btCampaignsList = new JButton("getCampaignsList");
            btCampaignsList.setBounds(10, 76, 159, 25);
            panelA.add(btCampaignsList);

            JButton btnCreateCampaign = new JButton("createCampaign");
            btnCreateCampaign.setBounds(10, 109, 159, 25);
            panelA.add(btnCreateCampaign);

            JButton btDestroyCampaign = new JButton("destroyCampaign");
            btDestroyCampaign.setBounds(10, 141, 159, 25);
            panelA.add(btDestroyCampaign);

            JButton btCampaign = new JButton("getCampaign");
            btCampaign.setBounds(10, 170, 159, 25);
            panelA.add(btCampaign);

            JButton btLastCounterValues = new JButton("getLastCounterValues");
            btLastCounterValues.setBounds(10, 199, 186, 25);
            panelA.add(btLastCounterValues);

            JLabel lblNewLabel = new JLabel("campaignName");
            lblNewLabel.setBounds(10, 235, 129, 16);
            panelA.add(lblNewLabel);

            JLabel lblCountersetname = new JLabel("counterDefSetName");
            lblCountersetname.setBounds(10, 268, 129, 16);
            panelA.add(lblCountersetname);

            JLabel lblDuration = new JLabel("duration");
            lblDuration.setBounds(10, 300, 129, 16);
            panelA.add(lblDuration);

            cbShortCampaign = new JCheckBox("Short campaign (duration in seconds, for normal campaigns duration in minutes)");
            cbShortCampaign.setBounds(10, 330, 495, 23);
            panelA.add(cbShortCampaign);

            tbCampaignName = new JTextField();
            tbCampaignName.setBounds(149, 234, 199, 22);
            panelA.add(tbCampaignName);
            tbCampaignName.setText("camp1");
            tbCampaignName.setColumns(10);

            tbCounterSetName = new JTextField();
            tbCounterSetName.setBounds(149, 267, 199, 22);
            panelA.add(tbCounterSetName);
            tbCounterSetName.setColumns(10);

            tbDuration = new JTextField();
            tbDuration.setBounds(149, 299, 199, 22);
            panelA.add(tbDuration);
            tbDuration.setText("5");
            tbDuration.setColumns(10);

            JButton btSelectCampaign = new JButton(". . .");
            btSelectCampaign.setBounds(360, 233, 36, 25);
            panelA.add(btSelectCampaign);

            JButton btSelectCounterSet = new JButton(". . .");
            btSelectCounterSet.setBounds(360, 266, 36, 25);
            panelA.add(btSelectCounterSet);

            JButton btDuration = new JButton(". . .");
            btDuration.setBounds(360, 298, 36, 25);
            panelA.add(btDuration);

            JPanel panelB = new JPanel();
            splitPane.setRightComponent(panelB);
            panelB.setAlignmentX(Component.RIGHT_ALIGNMENT);
            panelB.setAlignmentY(Component.TOP_ALIGNMENT);
            panelB.setLayout(new GridLayout(1, 0, 0, 0));

            JScrollPane scrollPane = new JScrollPane();
            panelB.add(scrollPane);

            taResult = new JTextArea();
            taResult.setEditable(false);
            scrollPane.setViewportView(taResult);
            btDuration.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doSelectDuration();
                }
            });
            btSelectCounterSet.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doSelectCounterSet();
                }
            });
            btSelectCampaign.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doSelectCampaign();
                }
            });
            btLastCounterValues.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doLastCounterValues();
                }
            });
            btCampaign.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doGetCampaign();
                }
            });
            btDestroyCampaign.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doDestroyCampaign();
                }
            });
            btnCreateCampaign.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doCreateCampaign();
                }
            });
            btCampaignsList.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    doCampaignsList();
                }
            });
            btCounterDefSet.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    doCounterDefSet();
                }
            });
            btGetCountersSetList.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doGetCountersSetList();
                }
            });
        }
    }

    private void doLastCounterValues() {
        String campaignName = this.tbCampaignName.getText();
        CounterValueSet res = this.statHost.getLastCounterValues(campaignName);

        StringBuilder sb = new StringBuilder();

        sb.append("Campaign: ");
        sb.append(campaignName);
        sb.append("\n");
        sb.append("\n");
        if (res != null) {
            sb.append("StartTime=");
            sb.append(res.getStartTime());
            sb.append("\n");
            sb.append("EndTime=");
            sb.append(res.getEndTime());
            sb.append("\n");
            sb.append("Duration minutes=");
            sb.append(res.getDuration());
            sb.append("\n");
            sb.append("Duration seconds=");
            sb.append(res.getDurationSeconds());
            sb.append("\n");

            for (CounterValue cv : res.getCounterValues()) {
                sb.append("LastCounterValueSet=");
                sb.append(cv.getObjectName());
                sb.append("\tCounterName=");
                sb.append(cv.getCounterDef().getCounterName());
                sb.append("\tValue=");
                switch (cv.getCounterDef().getCounterType()) {
                case Summary:
                case Summary_Cumulative:
                    sb.append(cv.getLongValue());
                    break;
                case SummaryDouble:
                    sb.append(cv.getDoubleValue());
                    break;
                case Minimal:
                    sb.append(cv.getLongValue());
                    break;
                case Maximal:
                    sb.append(cv.getLongValue());
                    break;
                case Average:
                    sb.append(cv.getDoubleValue());
                    break;
                case ComplexValue:
                    sb.append("Complex");
                    break;
                }
                sb.append("\n");

                if (cv.getCounterDef().getCounterType() == CounterType.ComplexValue) {
                    for (ComplexValue comp : cv.getComplexValue()) {
                        sb.append("\tKey=");
                        sb.append(comp.getKey());
                        sb.append("\tValue=");
                        sb.append(comp.getValue());
                        sb.append("\n");
                    }
                }
            }
        } else {
            sb.append("null");
        }

        this.taResult.setText(sb.toString());
    }

    private void doDestroyCampaign() {
        String campaignName = this.tbCampaignName.getText();
        String res = "OK";
        try {
            this.statHost.destroyCampaign(campaignName);
        } catch (Exception e) {
           res = e.toString();
        }

        this.taResult.setText(res);
    }

    private void doSelectCampaign() {
        ListSelector fm = new ListSelector(this.statHost.getCampaignsList(), this.tbCampaignName.getText(), "Campaign selection");
        fm.setModal(true);
        fm.setVisible(true);
        String s = fm.getResult();
        if (s != null) {
            this.tbCampaignName.setText(s);
        }
    }

    private void doGetCampaign() {
        String campaignName = this.tbCampaignName.getText();
        CounterCampaign res = this.statHost.getCampaign(campaignName);

        StringBuilder sb = new StringBuilder();

        sb.append("Campaign: ");
        sb.append(campaignName);
        sb.append("\n");
        sb.append("\n");
        if (res != null) {
            sb.append("Name=");
            sb.append(res.getName());
            sb.append("\n");
            sb.append("CounterSetName=");
            sb.append(res.getCounterSetName());
            sb.append("\n");
            sb.append("Duration=");
            sb.append(res.getDuration());
            sb.append("\n");
            if (res.isShortCampaign())
                sb.append("shortCampaign");
            else
                sb.append("normalCampaign");
            sb.append("\n");
            sb.append("LastCounterValueSet=");
            sb.append(res.getLastCounterValueSet() != null ? "Exists" : "Does not exist");
            sb.append("\n");
        } else {
            sb.append("null");
        }

        this.taResult.setText(sb.toString());
    }

    private void doCampaignsList() {
        String[] ss = this.statHost.getCampaignsList();
        StringBuilder sb = new StringBuilder();

        sb.append("GetCampaignsList:");
        sb.append("\n");
        sb.append("\n");
        if (ss != null) {
            for (String s : ss) {
                sb.append(s);
                sb.append("\n");
            }
        } else {
            sb.append("null");
        }

        this.taResult.setText(sb.toString());
    }

    private void doCounterDefSet() {
        String counterSetName = this.tbCounterSetName.getText();
        CounterDefSet res = this.statHost.getCounterDefSet(counterSetName);

        StringBuilder sb = new StringBuilder();

        sb.append("CounterDefSet: ");
        sb.append(counterSetName);
        sb.append("\n");
        sb.append("\n");
        if (res != null) {
            sb.append("Name=");
            sb.append(res.getName());
            sb.append("\n");
            for (CounterDef cd : res.getCounterDefs()) {
                sb.append(cd.getCounterName());
                sb.append("\t- ");
                sb.append(cd.getCounterType());
                sb.append("\t- ");
                sb.append(cd.getDescription());
                sb.append("\n");
            }
        } else {
            sb.append("null");
        }

        this.taResult.setText(sb.toString());
    }

    private void doSelectCounterSet() {
        ListSelector fm = new ListSelector(this.statHost.getCounterDefSetList(), this.tbCounterSetName.getText(), "CounterSet selection");
        fm.setModal(true);
        fm.setVisible(true);
        String s = fm.getResult();
        if (s != null) {
            this.tbCounterSetName.setText(s);
        }
    }

    private void doSelectDuration() {
        String[] lst = new String[] { "5", "10", "15", "20", "30", "60" };
        ListSelector fm = new ListSelector(lst, this.tbDuration.getText(), "Duration selection (min)");
        fm.setModal(true);
        fm.setVisible(true);
        String s = fm.getResult();
        if (s != null) {
            this.tbDuration.setText(s);
        }
    }

    private void doCreateCampaign() {
        String campaignName = this.tbCampaignName.getText();
        String counterSetName = this.tbCounterSetName.getText();
        int duration;
        try {
            duration = Integer.parseInt(this.tbDuration.getText());
        } catch (NumberFormatException e) {
            this.taResult.setText("NumberFormatException: field - duration");
            return;
        }

        String s = "OK";
        try {
            //TODO: extend GUI for outputFormat field, now is hardcoded to verbose
            if (this.cbShortCampaign.isSelected())
                this.statHost.createShortCampaign(campaignName, counterSetName, duration, 1);
            else
                this.statHost.createCampaign(campaignName, counterSetName, duration, 1);
        } catch (Exception e) {
            s = e.toString();
        }

        this.taResult.setText(s);
    }

    private void doGetCountersSetList() {
        String[] ss = this.statHost.getCounterDefSetList();
        StringBuilder sb = new StringBuilder();

        sb.append("GetCountersSetList:");
        sb.append("\n");
        sb.append("\n");
        if (ss != null) {
            for (String s : ss) {
                sb.append(s);
                sb.append("\n");
            }
        } else {
            sb.append("null");
        }

        this.taResult.setText(sb.toString());
    }
}
