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

package org.restcomm.protocols.ss7.map.dialog;

import org.restcomm.protocols.ss7.map.api.dialog.MAPUserAbortChoice;
import org.restcomm.protocols.ss7.map.api.dialog.ProcedureCancellationReason;
import org.restcomm.protocols.ss7.map.api.dialog.ResourceUnavailableReason;

/**
 * MAP-UserAbortChoice ::= CHOICE { userSpecificReason [0] NULL, userResourceLimitation [1] NULL, resourceUnavailable [2]
 * ResourceUnavailableReason, applicationProcedureCancellation [3] ProcedureCancellationReason}
 *
 * @author amit bhayani
 *
 */
public class MAPUserAbortChoiceImpl implements MAPUserAbortChoice {

    protected static final int USER_SPECIFIC_REASON_TAG = 0;
    protected static final int USER_RESOURCE_LIMITATION_TAG = 1;
    protected static final int RESOURCE_UNAVAILABLE = 2;
    protected static final int APPLICATION_PROCEDURE_CANCELLATION = 3;

    private ProcedureCancellationReason procedureCancellationReason = null;
    private boolean isProcedureCancellationReason = false;

    private ResourceUnavailableReason resourceUnavailableReason = null;
    private boolean isResourceUnavailableReason = false;

    private boolean isUserResourceLimitation = false;

    private boolean isUserSpecificReason = false;

    public ProcedureCancellationReason getProcedureCancellationReason() {
        return this.procedureCancellationReason;
    }

    public ResourceUnavailableReason getResourceUnavailableReason() {
        return this.resourceUnavailableReason;
    }

    public boolean isProcedureCancellationReason() {
        return this.isProcedureCancellationReason;
    }

    public boolean isResourceUnavailableReason() {
        return this.isResourceUnavailableReason;
    }

    public boolean isUserResourceLimitation() {
        return this.isUserResourceLimitation;
    }

    public boolean isUserSpecificReason() {
        return this.isUserSpecificReason;
    }

    public void setProcedureCancellationReason(ProcedureCancellationReason procCanReasn) {
        this.procedureCancellationReason = procCanReasn;
        this.isProcedureCancellationReason = true;
    }

    public void setResourceUnavailableReason(ResourceUnavailableReason resUnaReas) {
        this.resourceUnavailableReason = resUnaReas;
        this.isResourceUnavailableReason = true;
    }

    public void setUserResourceLimitation() {
        this.isUserResourceLimitation = true;
    }

    public void setUserSpecificReason() {
        this.isUserSpecificReason = true;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("MAPUserAbortChoice [");
        if (this.isUserSpecificReason)
            sb.append(" UserSpecificReason");
        if (this.isUserResourceLimitation)
            sb.append(" UserResourceLimitation");
        if (this.isResourceUnavailableReason) {
            sb.append(" ResourceUnavailableReason=");
            if (this.resourceUnavailableReason != null)
                sb.append(this.resourceUnavailableReason.toString());
        }
        if (this.isProcedureCancellationReason) {
            sb.append(" ProcedureCancellationReason=");
            if (this.procedureCancellationReason != null)
                sb.append(this.procedureCancellationReason.toString());
        }
        sb.append("]");

        return sb.toString();
    }

}
