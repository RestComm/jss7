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
import org.mobicents.protocols.ss7.map.api.MAPApplicationContext;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckData;
import org.mobicents.protocols.ss7.map.api.dialog.ServingCheckResult;
import org.mobicents.protocols.ss7.map.dialog.ServingCheckDataImpl;
import org.mobicents.protocols.ss7.map.service.supplementary.MAPServiceSupplementaryImpl;
import org.mobicents.protocols.ss7.tcap.asn.ApplicationContextNameImpl;

public class MAPServiceSupplementaryImplWrapper extends MAPServiceSupplementaryImpl {

    private int testMode = 0;

    public MAPServiceSupplementaryImplWrapper(MAPProviderImpl mapProviderImpl) {
        super(mapProviderImpl);
    }

    public ServingCheckData isServingService(MAPApplicationContext dialogApplicationContext) {
        if (this.testMode == 1) {
            // For reproducing FunctionalTestScenario.actionC MAPFunctionalTest
            // - remove temporally this comment comment
            ApplicationContextNameImpl ac = new ApplicationContextNameImpl();
            ac.setOid(new long[] { 1, 2, 3 });
            ServingCheckDataImpl i1 = new ServingCheckDataImpl(ServingCheckResult.AC_VersionIncorrect, ac);
            return i1;
        }

        return super.isServingService(dialogApplicationContext);
    }

    public void setTestMode(int testMode) {
        this.testMode = testMode;
    }

}
