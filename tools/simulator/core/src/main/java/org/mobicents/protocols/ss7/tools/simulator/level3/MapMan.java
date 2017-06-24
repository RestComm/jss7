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

package org.mobicents.protocols.ss7.tools.simulator.level3;

import javolution.util.FastList;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.map.MAPStackImpl;
import org.mobicents.protocols.ss7.map.api.MAPProvider;
import org.mobicents.protocols.ss7.map.api.MAPStack;
import org.mobicents.protocols.ss7.map.api.primitives.AddressNature;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.NumberingPlan;
import org.mobicents.protocols.ss7.map.primitives.AddressStringImpl;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.common.AddressNatureType;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class MapMan implements MapManMBean, Stoppable {

    public static String SOURCE_NAME = "MAP";

    private final String name;
    private TesterHost testerHost;

    private SccpStack sccpStack;

    private MAPStackImpl mapStack;
    private MAPProvider mapProvider;

    public MapMan() {
        this.name = "???";
    }

    public MapMan(String name) {
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    public void setSccpStack(SccpStack val) {
        this.sccpStack = val;
    }

    // @Override
    // public int getRemoteSsn() {
    // return this.testerHost.getConfigurationData().getMapConfigurationData().getRemoteSsn();
    // }
    //
    // @Override
    // public void setRemoteSsn(int val) {
    // this.testerHost.getConfigurationData().getMapConfigurationData().setRemoteSsn(val);
    // this.testerHost.markStore();
    // }
    //
    // @Override
    // public int getLocalSsn() {
    // return this.testerHost.getConfigurationData().getMapConfigurationData().getLocalSsn();
    // }
    //
    // @Override
    // public void setLocalSsn(int val) {
    // this.testerHost.getConfigurationData().getMapConfigurationData().setLocalSsn(val);
    // this.testerHost.markStore();
    // }

    @Override
    public String getRemoteAddressDigits() {
        return this.testerHost.getConfigurationData().getMapConfigurationData().getRemoteAddressDigits();
    }

    @Override
    public void setRemoteAddressDigits(String val) {
        this.testerHost.getConfigurationData().getMapConfigurationData().setRemoteAddressDigits(val);
        this.testerHost.markStore();
    }

    @Override
    public String getOrigReference() {
        return this.testerHost.getConfigurationData().getMapConfigurationData().getOrigReference();
    }

    @Override
    public void setOrigReference(String val) {
        this.testerHost.getConfigurationData().getMapConfigurationData().setOrigReference(val);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getOrigReferenceAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getOrigReferenceAddressNature().getIndicator());
    }

    @Override
    public String getOrigReferenceAddressNature_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getOrigReferenceAddressNature().getIndicator()).toString();
    }

    @Override
    public void setOrigReferenceAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getMapConfigurationData()
                .setOrigReferenceAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getOrigReferenceNumberingPlan() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getOrigReferenceNumberingPlan().getIndicator());
    }

    @Override
    public String getOrigReferenceNumberingPlan_Value() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getOrigReferenceNumberingPlan().getIndicator()).toString();
    }

    @Override
    public void setOrigReferenceNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getMapConfigurationData()
                .setOrigReferenceNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public String getDestReference() {
        return this.testerHost.getConfigurationData().getMapConfigurationData().getDestReference();
    }

    @Override
    public void setDestReference(String val) {
        this.testerHost.getConfigurationData().getMapConfigurationData().setDestReference(val);
        this.testerHost.markStore();
    }

    @Override
    public AddressNatureType getDestReferenceAddressNature() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getDestReferenceAddressNature().getIndicator());
    }

    @Override
    public String getDestReferenceAddressNature_Value() {
        return new AddressNatureType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getDestReferenceAddressNature().getIndicator()).toString();
    }

    @Override
    public void setDestReferenceAddressNature(AddressNatureType val) {
        this.testerHost.getConfigurationData().getMapConfigurationData()
                .setDestReferenceAddressNature(AddressNature.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public NumberingPlanMapType getDestReferenceNumberingPlan() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getDestReferenceNumberingPlan().getIndicator());
    }

    @Override
    public String getDestReferenceNumberingPlan_Value() {
        return new NumberingPlanMapType(this.testerHost.getConfigurationData().getMapConfigurationData()
                .getDestReferenceNumberingPlan().getIndicator()).toString();
    }

    @Override
    public void setDestReferenceNumberingPlan(NumberingPlanMapType val) {
        this.testerHost.getConfigurationData().getMapConfigurationData()
                .setDestReferenceNumberingPlan(NumberingPlan.getInstance(val.intValue()));
        this.testerHost.markStore();
    }

    @Override
    public void putOrigReferenceAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setOrigReferenceAddressNature(x);
    }

    @Override
    public void putOrigReferenceNumberingPlan(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        if (x != null)
            this.setOrigReferenceNumberingPlan(x);
    }

    @Override
    public void putDestReferenceAddressNature(String val) {
        AddressNatureType x = AddressNatureType.createInstance(val);
        if (x != null)
            this.setDestReferenceAddressNature(x);
    }

    @Override
    public void putDestReferenceNumberingPlan(String val) {
        NumberingPlanMapType x = NumberingPlanMapType.createInstance(val);
        if (x != null)
            this.setDestReferenceNumberingPlan(x);
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCAP+MAP: Started");
        return sb.toString();
    }

    public boolean start() {
        try {
            this.initMap(this.sccpStack, this.testerHost.getSccpMan().getLocalSsn(), this.testerHost.getSccpMan().getLocalSsn2());
            this.testerHost.sendNotif(SOURCE_NAME, "TCAP+MAP has been started", "", Level.INFO);
            return true;
        } catch (Throwable e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting MapMan", e, Level.ERROR);
            return false;
        }
    }

    @Override
    public void stop() {
        try {
            this.stopMap();
            this.testerHost.sendNotif(SOURCE_NAME, "TCAP+MAP has been stopped", "", Level.INFO);
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping MapMan", e, Level.ERROR);
        }
    }

    @Override
    public void execute() {
    }

    private void initMap(SccpStack sccpStack, int ssn, int extraSsn) throws Exception {

        this.mapStack = new MAPStackImpl("Simulator", sccpStack.getSccpProvider(), ssn);

        if (extraSsn > 0) {
            FastList<Integer> extraSsnsNew = new FastList<Integer>();
            extraSsnsNew.add(extraSsn);
            this.mapStack.getTCAPStack().setExtraSsns(extraSsnsNew);
        }

        this.mapStack.start();
    }

    private void stopMap() {

        this.mapStack.stop();
    }

    public MAPStack getMAPStack() {
        return this.mapStack;
    }

    public SccpAddress createOrigAddress() {
        return this.testerHost.getSccpMan().createCallingPartyAddress();
    }

    public SccpAddress createDestAddress() {
        if (this.testerHost.getConfigurationData().getMapConfigurationData().getRemoteAddressDigits() == null
                || this.testerHost.getConfigurationData().getMapConfigurationData().getRemoteAddressDigits().equals("")) {
            return this.testerHost.getSccpMan().createCalledPartyAddress();
        } else {
            return this.testerHost.getSccpMan().createCalledPartyAddress(
                    this.testerHost.getConfigurationData().getMapConfigurationData().getRemoteAddressDigits(),
                    this.testerHost.getSccpMan().getRemoteSsn());
        }
    }

    public SccpAddress createDestAddress(String address, int ssn) {
        return this.testerHost.getSccpMan().createCalledPartyAddress(address, ssn);
    }

    public AddressString createOrigReference() {
        if (this.testerHost.getConfigurationData().getMapConfigurationData().getOrigReference() == null
                || this.testerHost.getConfigurationData().getMapConfigurationData().getOrigReference().equals(""))
            return null;
        else
            return new AddressStringImpl(this.testerHost.getConfigurationData().getMapConfigurationData()
                    .getOrigReferenceAddressNature(), this.testerHost.getConfigurationData().getMapConfigurationData()
                    .getOrigReferenceNumberingPlan(), this.testerHost.getConfigurationData().getMapConfigurationData()
                    .getOrigReference());
    }

    public AddressString createDestReference() {
        if (this.testerHost.getConfigurationData().getMapConfigurationData().getDestReference() == null
                || this.testerHost.getConfigurationData().getMapConfigurationData().getDestReference().equals(""))
            return null;
        else
            return new AddressStringImpl(this.testerHost.getConfigurationData().getMapConfigurationData()
                    .getDestReferenceAddressNature(), this.testerHost.getConfigurationData().getMapConfigurationData()
                    .getDestReferenceNumberingPlan(), this.testerHost.getConfigurationData().getMapConfigurationData()
                    .getDestReference());
    }

}
