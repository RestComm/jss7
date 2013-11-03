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

package org.mobicents.protocols.ss7.tools.simulator.level1;

import org.apache.log4j.Level;
import org.mobicents.protocols.ss7.mtp.Mtp3UserPart;
import org.mobicents.protocols.ss7.tools.simulator.Stoppable;
import org.mobicents.protocols.ss7.tools.simulator.management.TesterHost;
import org.mobicents.ss7.hardware.dialogic.DialogicMtp3UserPart;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class DialogicMan implements DialogicManMBean, Stoppable {

    public static String SOURCE_NAME = "Dialogic";

    private final String name;
    private TesterHost testerHost;
    private DialogicMtp3UserPart dialogic;
    private boolean isStarted = false;

    public DialogicMan() {
        this.name = "???";
    }

    public DialogicMan(String name) {
        this.name = name;
    }

    public void setTesterHost(TesterHost testerHost) {
        this.testerHost = testerHost;
    }

    @Override
    public int getSourceModuleId() {
        return this.testerHost.getConfigurationData().getDialogicConfigurationData().getSourceModuleId();
    }

    @Override
    public void setSourceModuleId(int val) {
        this.testerHost.getConfigurationData().getDialogicConfigurationData().setSourceModuleId(val);
        this.testerHost.markStore();
    }

    @Override
    public int getDestinationModuleId() {
        return this.testerHost.getConfigurationData().getDialogicConfigurationData().getDestinationModuleId();
    }

    @Override
    public void setDestinationModuleId(int val) {
        this.testerHost.getConfigurationData().getDialogicConfigurationData().setDestinationModuleId(val);
        this.testerHost.markStore();
    }

    @Override
    public String getState() {
        StringBuilder sb = new StringBuilder();
        sb.append("IsStarted: ");
        sb.append(isStarted);

        return sb.toString();
    }

    public boolean start() {
        try {
            this.initDialogic(this.getSourceModuleId(), this.getDestinationModuleId());
            this.isStarted = true;
            this.testerHost.sendNotif(SOURCE_NAME, "Dialogic has been started", "", Level.INFO);
            return true;
        } catch (Throwable e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when starting DialogicMan", e, Level.ERROR);
            return false;
        }
    }

    public void stop() {
        try {
            this.isStarted = false;
            this.stopDialogic();
            this.testerHost.sendNotif(SOURCE_NAME, "Dialogic has been stopped", "", Level.INFO);
        } catch (Throwable e) {
            this.testerHost.sendNotif(SOURCE_NAME, "Exception when stopping DialogicMan", e, Level.ERROR);
        }
    }

    @Override
    public void execute() {
    }

    private void initDialogic(int sourceModuleId, int destinationModuleId) throws Exception {

        this.dialogic = new DialogicMtp3UserPart();
        this.dialogic.setSourceModuleId(sourceModuleId);
        this.dialogic.setDestinationModuleId(destinationModuleId);

        // set 8 threads for delivering messages
        this.dialogic.setDeliveryMessageThreadCount(8);

        this.dialogic.start();
    }

    private void stopDialogic() throws Exception {
        if (this.dialogic != null)
            this.dialogic.stop();
        this.dialogic = null;
    }

    public Mtp3UserPart getMtp3UserPart() {
        return this.dialogic;
    }

}
