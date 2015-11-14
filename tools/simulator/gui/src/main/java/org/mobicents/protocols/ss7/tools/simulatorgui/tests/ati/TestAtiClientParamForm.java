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

package org.mobicents.protocols.ss7.tools.simulatorgui.tests.ati;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;

import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.api.service.mobility.subscriberInformation.DomainType;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.level3.NumberingPlanMapType;
import org.mobicents.protocols.ss7.tools.simulator.tests.ati.AtiDomainType;
import org.mobicents.protocols.ss7.tools.simulator.tests.ati.SubscriberIdentityType;
import org.mobicents.protocols.ss7.tools.simulator.tests.ati.TestAtiClientManMBean;
import org.mobicents.protocols.ss7.tools.simulatorgui.M3uaForm;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class TestAtiClientParamForm extends JDialog {

    private static final long serialVersionUID = 5106250858311037393L;

    private TestAtiClientManMBean atiClient;

    private JComboBox cbAddressNature;
    private JComboBox cbNumberingPlan;
    private JRadioButton rbImsi;
    private JRadioButton rbMsisdn;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final ButtonGroup buttonGroup_1 = new ButtonGroup();
    private JCheckBox cbLocationInformation;
    private JCheckBox cbSubscriberState;
    private JCheckBox cbCurrentLocation;
    private JRadioButton rbCsDomain;
    private JRadioButton rbPsDomain;
    private JCheckBox cbImei;
    private JCheckBox cbMsClassmark;
    private JCheckBox cbMnpRequestedInfo;
    private JTextField tbGsmSCFAddress;
    private JRadioButton rbDomainType_NoValue;

    public TestAtiClientParamForm(JFrame owner) {
        super(owner, true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("ATI test client settings");
        setBounds(100, 100, 585, 584);
        getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel.setBounds(10, 59, 511, 94);
        getContentPane().add(panel);

        JLabel lblParametersForAddresses = new JLabel("Parameters for AddressString creation");
        lblParametersForAddresses.setBounds(10, 0, 266, 14);
        panel.add(lblParametersForAddresses);

        JLabel lblAddressnature = new JLabel("AddressNature");
        lblAddressnature.setBounds(10, 28, 174, 14);
        panel.add(lblAddressnature);

        JLabel lblNumberingplan = new JLabel("NumberingPlan");
        lblNumberingplan.setBounds(10, 59, 174, 14);
        panel.add(lblNumberingplan);

        cbAddressNature = new JComboBox();
        cbAddressNature.setBounds(194, 25, 307, 20);
        panel.add(cbAddressNature);

        cbNumberingPlan = new JComboBox();
        cbNumberingPlan.setBounds(194, 56, 307, 20);
        panel.add(cbNumberingPlan);

        JButton button = new JButton("Load default values for side A");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                loadDataA();
            }
        });
        button.setBounds(10, 488, 246, 23);
        getContentPane().add(button);

        JButton button_1 = new JButton("Load default values for side B");
        button_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadDataB();
            }
        });
        button_1.setBounds(266, 488, 255, 23);
        getContentPane().add(button_1);

        JButton button_2 = new JButton("Reload");
        button_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                reloadData();
            }
        });
        button_2.setBounds(10, 522, 144, 23);
        getContentPane().add(button_2);

        JButton button_3 = new JButton("Save");
        button_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (saveData()) {
                    getJFrame().dispose();
                }
            }
        });
        button_3.setBounds(180, 522, 117, 23);
        getContentPane().add(button_3);

        JButton button_4 = new JButton("Cancel");
        button_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getJFrame().dispose();
            }
        });
        button_4.setBounds(404, 522, 117, 23);
        getContentPane().add(button_4);

        JPanel pnSubscriberIdentityType = new JPanel();
        pnSubscriberIdentityType.setBounds(10, 11, 511, 37);
        getContentPane().add(pnSubscriberIdentityType);
        pnSubscriberIdentityType.setLayout(null);

        rbImsi = new JRadioButton("IMSI");
        buttonGroup.add(rbImsi);
        rbImsi.setBounds(299, 7, 88, 23);
        pnSubscriberIdentityType.add(rbImsi);

        rbMsisdn = new JRadioButton("MSISDN");
        buttonGroup.add(rbMsisdn);
        rbMsisdn.setBounds(405, 7, 88, 23);
        pnSubscriberIdentityType.add(rbMsisdn);

        JLabel lblNewLabel = new JLabel("Subscriber identity type");
        lblNewLabel.setBounds(10, 11, 243, 14);
        pnSubscriberIdentityType.add(lblNewLabel);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
        panel_1.setBounds(10, 164, 562, 209);
        getContentPane().add(panel_1);

        JLabel lblRequestedInfoIn = new JLabel("Requested info in ATI request");
        lblRequestedInfoIn.setBounds(10, 0, 266, 14);
        panel_1.add(lblRequestedInfoIn);

        cbLocationInformation = new JCheckBox("Location Information");
        cbLocationInformation.setBounds(10, 21, 511, 23);
        panel_1.add(cbLocationInformation);

        cbSubscriberState = new JCheckBox("Subscriber State");
        cbSubscriberState.setBounds(10, 47, 511, 23);
        panel_1.add(cbSubscriberState);

        cbCurrentLocation = new JCheckBox("Current Location");
        cbCurrentLocation.setBounds(10, 73, 511, 23);
        panel_1.add(cbCurrentLocation);

        JLabel lblDomainType = new JLabel("Domain Type");
        lblDomainType.setBounds(10, 103, 130, 14);
        panel_1.add(lblDomainType);

        rbCsDomain = new JRadioButton("csDomain");
        buttonGroup_1.add(rbCsDomain);
        rbCsDomain.setBounds(236, 99, 88, 23);
        panel_1.add(rbCsDomain);

        rbPsDomain = new JRadioButton("psDomain");
        buttonGroup_1.add(rbPsDomain);
        rbPsDomain.setBounds(340, 99, 88, 23);
        panel_1.add(rbPsDomain);

        cbImei = new JCheckBox("Imei");
        cbImei.setBounds(10, 124, 511, 23);
        panel_1.add(cbImei);

        cbMsClassmark = new JCheckBox("Ms Classmark");
        cbMsClassmark.setBounds(10, 150, 511, 23);
        panel_1.add(cbMsClassmark);

        cbMnpRequestedInfo = new JCheckBox("Mnp Requested Info");
        cbMnpRequestedInfo.setBounds(10, 176, 511, 23);
        panel_1.add(cbMnpRequestedInfo);

        rbDomainType_NoValue = new JRadioButton("No value");
        buttonGroup_1.add(rbDomainType_NoValue);
        rbDomainType_NoValue.setBounds(132, 99, 88, 23);
        panel_1.add(rbDomainType_NoValue);

        JLabel lblGsmScfAddress = new JLabel("Gsm SCF address digits");
        lblGsmScfAddress.setBounds(10, 387, 361, 14);
        getContentPane().add(lblGsmScfAddress);

        tbGsmSCFAddress = new JTextField();
        tbGsmSCFAddress.setColumns(10);
        tbGsmSCFAddress.setBounds(392, 384, 177, 20);
        getContentPane().add(tbGsmSCFAddress);
    }

    public void setData(TestAtiClientManMBean atiClient) {
        this.atiClient = atiClient;

        this.reloadData();
    }

    private JDialog getJFrame() {
        return this;
    }

    private void reloadData() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, this.atiClient.getAddressNature());
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, this.atiClient.getNumberingPlan());

        if (this.atiClient.getSubscriberIdentityType().intValue() == SubscriberIdentityType.VALUE_IMSI) {
            this.rbImsi.setSelected(true);
        } else {
            this.rbMsisdn.setSelected(true);
        }

        this.cbLocationInformation.setSelected(this.atiClient.isGetLocationInformation());
        this.cbSubscriberState.setSelected(this.atiClient.isGetSubscriberState());
        this.cbCurrentLocation.setSelected(this.atiClient.isGetCurrentLocation());
        this.cbImei.setSelected(this.atiClient.isGetImei());
        this.cbMsClassmark.setSelected(this.atiClient.isGetMsClassmark());
        this.cbMnpRequestedInfo.setSelected(this.atiClient.isGetMnpRequestedInfo());

        switch (this.atiClient.getGetRequestedDomain().intValue()) {
        case AtiDomainType.NO_VALUE:
            this.rbDomainType_NoValue.setSelected(true);
            break;
        case 0:
            this.rbCsDomain.setSelected(true);
            break;
        default:
            this.rbPsDomain.setSelected(true);
            break;
        }

        this.tbGsmSCFAddress.setText(this.atiClient.getGsmSCFAddress());
    }

    private void loadDataA() {
        M3uaForm.setEnumeratedBaseComboBox(cbAddressNature, new AddressNatureType(AddressNature.international_number.getIndicator()));
        M3uaForm.setEnumeratedBaseComboBox(cbNumberingPlan, new NumberingPlanMapType(NumberingPlan.ISDN.getIndicator()));

        this.rbMsisdn.setSelected(true);

        this.cbLocationInformation.setSelected(true);
        this.cbSubscriberState.setSelected(false);
        this.cbCurrentLocation.setSelected(false);
        this.cbImei.setSelected(false);
        this.cbMsClassmark.setSelected(false);
        this.cbMnpRequestedInfo.setSelected(false);

        this.rbDomainType_NoValue.setSelected(true);

        this.tbGsmSCFAddress.setText("000");
    }

    private void loadDataB() {
        loadDataA();
    }

    private boolean saveData() {
        this.atiClient.setAddressNature((AddressNatureType) cbAddressNature.getSelectedItem());
        this.atiClient.setNumberingPlan((NumberingPlanMapType) cbNumberingPlan.getSelectedItem());

        if (this.rbImsi.isSelected())
            this.atiClient.setSubscriberIdentityType(new SubscriberIdentityType(SubscriberIdentityType.VALUE_IMSI));
        else
            this.atiClient.setSubscriberIdentityType(new SubscriberIdentityType(SubscriberIdentityType.VALUE_ISDN));

        this.atiClient.setGetLocationInformation(this.cbLocationInformation.isSelected());
        this.atiClient.setGetSubscriberState(this.cbSubscriberState.isSelected());
        this.atiClient.setGetCurrentLocation(this.cbCurrentLocation.isSelected());
        this.atiClient.setGetImei(this.cbImei.isSelected());
        this.atiClient.setGetMsClassmark(this.cbMsClassmark.isSelected());
        this.atiClient.setGetMnpRequestedInfo(this.cbMnpRequestedInfo.isSelected());

        if (this.rbDomainType_NoValue.isSelected())
            this.atiClient.setGetRequestedDomain(new AtiDomainType(AtiDomainType.NO_VALUE));
        else if (this.rbCsDomain.isSelected())
            this.atiClient.setGetRequestedDomain(new AtiDomainType(DomainType.csDomain.getType()));
        else
            this.atiClient.setGetRequestedDomain(new AtiDomainType(DomainType.psDomain.getType()));

        this.atiClient.setGsmSCFAddress(this.tbGsmSCFAddress.getText());

        return true;
    }
}
