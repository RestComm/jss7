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

package org.mobicents.protocols.ss7.map.functional;

import org.mobicents.protocols.ss7.map.MAPProviderImpl;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPServiceBase;
import org.mobicents.protocols.ss7.map.api.dialog.MAPProviderAbortReason;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementary;
import org.mobicents.protocols.ss7.map.primitives.MAPExtensionContainerTest;
import org.mobicents.protocols.ss7.tcap.api.TCAPProvider;
import org.mobicents.protocols.ss7.tcap.api.tc.dialog.events.TCBeginIndication;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextName;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;

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
