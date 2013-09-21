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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall.primitive;

import java.io.Serializable;

/**
 *
 BackwardServiceInteractionInd ::= SEQUENCE { conferenceTreatmentIndicator [1] OCTET STRING (SIZE(1)) OPTIONAL, --
 * acceptConferenceRequest 'xxxx xx01'B -- rejectConferenceRequest 'xxxx xx10'B -- if absent from Connect or
 * ContinueWithArgument, -- then CAMEL service does not affect conference treatement callCompletionTreatmentIndicator [2] OCTET
 * STRING (SIZE(1)) OPTIONAL, -- acceptCallCompletionServiceRequest 'xxxx xx01'B, -- rejectCallCompletionServiceRequest 'xxxx
 * xx10'B -- if absent from Connect or ContinueWithArgument, -- then CAMEL service does not affect call completion treatment ...
 * }
 *
 * @author sergey vetyutnev
 *
 */
public interface BackwardServiceInteractionInd extends Serializable {

    ConferenceTreatmentIndicator getConferenceTreatmentIndicator();

    CallCompletionTreatmentIndicator getCallCompletionTreatmentIndicator();

}