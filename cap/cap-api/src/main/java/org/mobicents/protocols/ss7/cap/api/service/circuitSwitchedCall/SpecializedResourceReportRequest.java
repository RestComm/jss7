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

package org.mobicents.protocols.ss7.cap.api.service.circuitSwitchedCall;

import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

/**
 *
<code>
specializedResourceReport OPERATION ::= {
  ARGUMENT SpecializedResourceReportArg
  RETURN RESULT FALSE
  ALWAYS RESPONDS FALSE
  CODE opcode-specializedResourceReport
}
-- Direction: gsmSRF -> gsmSCF, Timer: Tsrr
-- This operation is used as the response to a PlayAnnouncement operation when the announcement
-- completed report indication is set.

CAP V2 & V3: SpecializedResourceReportArg::=NULL

CAP V4: SpecializedResourceReportArg ::= CHOICE {
  allAnnouncementsComplete [50] NULL,
  firstAnnouncementStarted [51] NULL
}
</code>
 *
 * @author sergey vetyutnev
 *
 */
public interface SpecializedResourceReportRequest extends CircuitSwitchedCallMessage {

    boolean getAllAnnouncementsComplete();

    boolean getFirstAnnouncementStarted();

    Long getLinkedId();

    void setLinkedId(Long val);

    Invoke getLinkedInvoke();

    void setLinkedInvoke(Invoke val);

}
