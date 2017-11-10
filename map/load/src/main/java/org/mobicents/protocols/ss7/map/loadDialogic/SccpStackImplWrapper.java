/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and/or its affiliates, and individual
 * contributors as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */

package org.mobicents.protocols.ss7.map.loadDialogic;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.mtp.Mtp3PausePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3ResumePrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3StatusPrimitive;
import org.mobicents.protocols.ss7.mtp.Mtp3TransferPrimitive;
import org.mobicents.protocols.ss7.sccp.impl.SccpStackImpl;
import org.mobicents.protocols.ss7.sccp.impl.message.SccpDataNoticeTemplateMessageImpl;

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
    protected void send(SccpDataNoticeTemplateMessageImpl message) throws IOException {
        try{
            super.send(message);
        }catch(Exception e){
            throw new IOException(e);
        }
        logger.warn("Sccp msg has sent");
    }
}
