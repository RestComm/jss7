/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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

package org.restcomm.protocols.ss7.map.functional;

import org.restcomm.protocols.ss7.map.MAPProviderImpl;
import org.restcomm.protocols.ss7.map.api.MAPException;
import org.restcomm.protocols.ss7.map.api.MAPServiceBase;
import org.restcomm.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.restcomm.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.restcomm.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.restcomm.protocols.ss7.tcap.api.TCAPProvider;
import org.restcomm.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.restcomm.protocols.ss7.tcap.asn.ApplicationContextName;
import org.restcomm.protocols.ss7.tcap.asn.comp.Component;

/**
 *
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class MAPProviderImplWrapper extends MAPProviderImpl {

    private int testMode = 0;

    private final MAPServiceSupplementary mapServiceSupplementaryTest = new MAPServiceSupplementaryImplWrapper(this);

    public MAPProviderImplWrapper(TCAPProvider tcapProvider) {
        super("Test", tcapProvider);

        for (MAPServiceBase serv : this.mapServices) {
            if (serv instanceof MAPServiceSupplementary) {
                this.mapServices.remove(serv);
                break;
            }
        }

        this.mapServices.add(this.mapServiceSupplementaryTest);
    }

    public MAPServiceSupplementary getMAPServiceSupplementary() {
        return this.mapServiceSupplementaryTest;
    }

    public void setTestMode(int testMode) {
        this.testMode = testMode;
    }

    public void onTCBegin(TCBeginIndication tcBeginIndication) {
        ApplicationContextName acn = tcBeginIndication.getApplicationContextName();
        Component[] comps = tcBeginIndication.getComponents();

        if (this.testMode == 1) {
            try {
                this.fireTCAbortProvider(tcBeginIndication.getDialog(), MAPProviderAbortReason.invalidPDU,
                        MAPExtensionContainerTest.GetTestExtensionContainer(), false);
            } catch (MAPException e) {
                loger.error("Error while firing TC-U-ABORT. ", e);
            }
            return;
        }

        super.onTCBegin(tcBeginIndication);
    }
}
