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

package org.restcomm.protocols.ss7.inap.isup;

import org.restcomm.protocols.ss7.inap.api.INAPException;
import org.restcomm.protocols.ss7.inap.api.INAPParameterFactory;
import org.restcomm.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.restcomm.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.restcomm.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.restcomm.protocols.ss7.inap.api.primitives.LegID;
import org.restcomm.protocols.ss7.inap.api.primitives.LegType;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoDpAssignment;
import org.restcomm.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.restcomm.protocols.ss7.inap.primitives.LegIDImpl;
import org.restcomm.protocols.ss7.inap.primitives.MiscCallInfoImpl;
import org.restcomm.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.restcomm.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.restcomm.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 *
 * @author sergey vetyutnev
 *
 */
public class INAPParameterFactoryImpl implements INAPParameterFactory {

    @Override
    public CallingPartysCategoryInap createCallingPartysCategoryInap(byte[] data) {
        return new CallingPartysCategoryInapImpl(data);
    }

    @Override
    public CallingPartysCategoryInap createCallingPartysCategoryInap(CallingPartyCategory callingPartyCategory)
            throws INAPException {
        return new CallingPartysCategoryInapImpl(callingPartyCategory);
    }

    @Override
    public HighLayerCompatibilityInap createHighLayerCompatibilityInap(byte[] data) {
        return new HighLayerCompatibilityInapImpl(data);
    }

    @Override
    public HighLayerCompatibilityInap createHighLayerCompatibilityInap(UserTeleserviceInformation highLayerCompatibility)
            throws INAPException {
        return new HighLayerCompatibilityInapImpl(highLayerCompatibility);
    }

    @Override
    public RedirectionInformationInap createRedirectionInformationInap(byte[] data) {
        return new RedirectionInformationInapImpl(data);
    }

    @Override
    public RedirectionInformationInap createRedirectionInformationInap(RedirectionInformation redirectionInformation)
            throws INAPException {
        return new RedirectionInformationInapImpl(redirectionInformation);
    }

    @Override
    public LegID createLegID(boolean isSendingSideID, LegType legID) {
        return new LegIDImpl(isSendingSideID, legID);
    }

    @Override
    public MiscCallInfo createMiscCallInfo(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment) {
        return new MiscCallInfoImpl(messageType, dpAssignment);
    }
}
