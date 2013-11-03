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

package org.mobicents.protocols.ss7.map.loadDialogic;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataMessageImpl;

public class SccpStackImplWrapper extends SccpStackImpl {

    private Logger logger;

    public SccpStackImplWrapper(String name, Logger logger) {
        super(name);
        this.logger = logger;
    }

    @Override
    public void onMtp3PauseMessage(Mtp3PausePrimitive msg) {
        logger.warn("Mtp3PausePrimitive received: dpc=" + msg.getAffectedDpc());
        super.onMtp3PauseMessage(msg);
    }

    @Override
    public void onMtp3ResumeMessage(Mtp3ResumePrimitive msg) {
        logger.warn("Mtp3ResumePrimitive received: dpc=" + msg.getAffectedDpc());
        super.onMtp3ResumeMessage(msg);
    }

    @Override
    public void onMtp3StatusMessage(Mtp3StatusPrimitive msg) {
        logger.warn("Mtp3StatusPrimitive received");
        super.onMtp3StatusMessage(msg);
    }

    @Override
    public void onMtp3TransferMessage(Mtp3TransferPrimitive mtp3Msg) {
        logger.warn("Mtp3TransferPrimitive received");
        super.onMtp3TransferMessage(mtp3Msg);
    }

    @Override
    protected void send(SccpDataMessageImpl message) throws IOException {
        super.send(message);
        logger.warn("Sccp msg has sent");
    }
}
