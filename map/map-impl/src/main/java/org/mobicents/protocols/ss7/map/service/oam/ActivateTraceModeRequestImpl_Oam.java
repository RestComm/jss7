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

package org.mobicents.protocols.ss7.map.service.oam;

import org.mobicents.protocols.ss7.map.api.MAPDialog;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.GSNAddress;
import org.mobicents.protocols.ss7.map.api.primitives.IMSI;
import org.mobicents.protocols.ss7.map.api.primitives.MAPExtensionContainer;
import org.mobicents.protocols.ss7.map.api.service.oam.ActivateTraceModeRequest_Oam;
import org.mobicents.protocols.ss7.map.api.service.oam.MAPDialogOam;
import org.mobicents.protocols.ss7.map.api.service.oam.MDTConfiguration;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceDepthList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceEventList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceInterfaceList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceNETypeList;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceReference;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceReference2;
import org.mobicents.protocols.ss7.map.api.service.oam.TraceType;

/**
*
* @author sergey vetyutnev
*
*/
public class ActivateTraceModeRequestImpl_Oam extends ActivateTraceModeRequestImpl_Base implements ActivateTraceModeRequest_Oam {

    public ActivateTraceModeRequestImpl_Oam() {
        super();
    }

    public ActivateTraceModeRequestImpl_Oam(IMSI imsi, TraceReference traceReference, TraceType traceType, AddressString omcId,
            MAPExtensionContainer extensionContainer, TraceReference2 traceReference2, TraceDepthList traceDepthList, TraceNETypeList traceNeTypeList,
            TraceInterfaceList traceInterfaceList, TraceEventList traceEventList, GSNAddress traceCollectionEntity, MDTConfiguration mdtConfiguration) {

        super(imsi, traceReference, traceType, omcId, extensionContainer, traceReference2, traceDepthList, traceNeTypeList, traceInterfaceList, traceEventList,
                traceCollectionEntity, mdtConfiguration);
    }

    public MAPDialogOam getMAPDialog() {
        MAPDialog mapDialog = super.getMAPDialog();
        return (MAPDialogOam) mapDialog;
    }

}
