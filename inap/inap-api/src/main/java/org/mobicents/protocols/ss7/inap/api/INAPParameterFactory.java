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

package org.mobicents.protocols.ss7.inap.api;

import org.mobicents.protocols.ss7.inap.api.isup.CallingPartysCategoryInap;
import org.mobicents.protocols.ss7.inap.api.isup.HighLayerCompatibilityInap;
import org.mobicents.protocols.ss7.inap.api.isup.RedirectionInformationInap;
import org.mobicents.protocols.ss7.inap.api.primitives.LegID;
import org.mobicents.protocols.ss7.inap.api.primitives.LegType;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfo;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoDpAssignment;
import org.mobicents.protocols.ss7.inap.api.primitives.MiscCallInfoMessageType;
import org.mobicents.protocols.ss7.isup.message.parameter.CallingPartyCategory;
import org.mobicents.protocols.ss7.isup.message.parameter.RedirectionInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.UserTeleserviceInformation;

/**
 *
 * @author sergey vetyutnev
 *
 */
public interface INAPParameterFactory {

    CallingPartysCategoryInap createCallingPartysCategoryInap(byte[] data);

    CallingPartysCategoryInap createCallingPartysCategoryInap(CallingPartyCategory callingPartyCategory)
            throws INAPException;

    HighLayerCompatibilityInap createHighLayerCompatibilityInap(byte[] data);

    HighLayerCompatibilityInap createHighLayerCompatibilityInap(UserTeleserviceInformation highLayerCompatibility)
            throws INAPException;

    RedirectionInformationInap createRedirectionInformationInap(byte[] data);

    RedirectionInformationInap createRedirectionInformationInap(RedirectionInformation redirectionInformation)
            throws INAPException;

    LegID createLegID(boolean isSendingSideID, LegType legID);

    MiscCallInfo createMiscCallInfo(MiscCallInfoMessageType messageType, MiscCallInfoDpAssignment dpAssignment);

}
