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
import org.mobicents.protocols.ss7.cap.CAPStackImpl;
import org.mobicents.protocols.ss7.cap.api.CAPProvider;
import org.mobicents.protocols.ss7.cap.api.CAPStack;
import org.mobicents.protocols.ss7.sccp.SccpStack;
import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class CapMan implements CapManMBean, Stoppable {

    public static String SOURCE_NAME = "CAP";

    private final String name;
    private TesterHost testerHost;

    private SccpStack sccpStack;

    private CAPStackImpl capStack;
    private CAPProvider capProvider;

    public CapMan() {
        this.name = "???";
    }

    public CapMan(String name) {
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
    // return this.testerHost.getConfigurationData().getCapConfigurationData().getRemoteSsn();
    // }
    //
    // @Override
    // public void setRemoteSsn(int val) {
    // this.testerHost.getConfigurationData().getCapConfigurationData().setRemoteSsn(val);
    // this.testerHost.markStore();
    // }
    //
    // @Override
    // public int getLocalSsn() {
    // return this.testerHost.getConfigurationData().getCapConfigurationData().getLocalSsn();
    // }
    //
    // @Override
    // public void setLocalSsn(int val) {
    // this.testerHost.getConfigurationData().getCapConfigurationData().setLocalSsn(val);
    // this.testerHost.markStore();
    // }

    @Override
    public String getRemoteAddressDigits() {
        return this.testerHost.getConfigurationData().getCapConfigurationData().getRemoteAddressDigits();
    }

    @Override
    public void setRemoteAddressDigits(String val) {
        this.testerHost.getConfigurationData().getCapConfigurationData().setRemoteAddressDigits(val);
        this.testerHost.markStore();
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("TCAP+CAP: Started");
        return sb.toString();
    }

    public boolean start() {
        try {
            this.initCap(this.sccpStack, this.testerHost.getSccpMan().getLocalSsn(), this.testerHost.getSccpMan().getLocalSsn2());
            this.testerHost.sendNotif(SOURCE_NAME, "TCAP+CAP has been started", "", Level.INFO);
            return true;
        } catch (Throwable e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting CapMan", e, Level.ERROR);
            return false;
        }
    }

    @Override
    public void stop() {
        try {
            this.stopCap();
            this.testerHost.sendNotif(SOURCE_NAME, "TCAP+CAP has been stopped", "", Level.INFO);
        } catch (Exception e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping CapMan", e, Level.ERROR);
        }
    }

    @Override
    public void execute() {
    }

    private void initCap(SccpStack sccpStack, int ssn, int extraSsn) throws Exception {

        this.capStack = new CAPStackImpl("Simulator", sccpStack.getSccpProvider(), ssn);

        if (extraSsn > 0) {
            FastList<Integer> extraSsnsNew = new FastList<Integer>();
            extraSsnsNew.add(extraSsn);
            this.capStack.getTCAPStack().setExtraSsns(extraSsnsNew);
        }

        this.capStack.start();
    }

    private void stopCap() {

        this.capStack.stop();
    }

    public CAPStack getCAPStack() {
        return this.capStack;
    }

    public SccpAddress createOrigAddress() {
        return this.testerHost.getSccpMan().createCallingPartyAddress();
    }

    public SccpAddress createDestAddress() {
        if (this.testerHost.getConfigurationData().getCapConfigurationData().getRemoteAddressDigits() == null
                || this.testerHost.getConfigurationData().getCapConfigurationData().getRemoteAddressDigits().equals("")) {
            return this.testerHost.getSccpMan().createCalledPartyAddress();
        } else {
            return this.testerHost.getSccpMan().createCalledPartyAddress(
                    this.testerHost.getConfigurationData().getCapConfigurationData().getRemoteAddressDigits(),
                    this.testerHost.getSccpMan().getRemoteSsn());
        }
    }

    public SccpAddress createDestAddress(String address, int ssn) {
        return this.testerHost.getSccpMan().createCalledPartyAddress(address, ssn);
    }

}
