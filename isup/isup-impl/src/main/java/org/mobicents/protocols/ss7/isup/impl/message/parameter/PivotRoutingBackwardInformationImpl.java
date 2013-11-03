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

/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski
 *         </a>
 *
 */
package org.mobicents.protocols.ss7.isup.impl.message.parameter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.mobicents.protocols.ss7.isup.ParameterException;
import org.mobicents.protocols.ss7.isup.message.parameter.InvokingPivotReason;
import org.mobicents.protocols.ss7.isup.message.parameter.PivotRoutingBackwardInformation;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeCallIdentifier;
import org.mobicents.protocols.ss7.isup.message.parameter.ReturnToInvokingExchangeDuration;

/**
 * Start time:16:16:18 2009-04-05<br>
 * Project: mobicents-isup-stack<br>
 *
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class PivotRoutingBackwardInformationImpl extends AbstractInformationParameterBaseImpl implements
        PivotRoutingBackwardInformation {

    private static final Map<Integer, Class<? extends AbstractInformationImpl>> tagMapping;

    static{
        Map<Integer, Class<? extends AbstractInformationImpl>> tmp = new HashMap<Integer, Class<? extends AbstractInformationImpl>>();
        tmp.put(INFORMATION_RETURN_TO_INVOKING_EXCHANGE_DURATION,ReturnToInvokingExchangeDurationImpl.class);
        tmp.put(INFORMATION_RETURN_TO_INVOKING_EXCHANGE_CALL_ID,ReturnToInvokingExchangeCallIdentifierImpl.class);
        tmp.put(INFORMATION_INVOKING_PIVOT_REASON,InvokingPivotReasonImpl.class);

        tagMapping = Collections.unmodifiableMap(tmp);
    }
    public PivotRoutingBackwardInformationImpl(byte[] pivotRoutingIndicators) throws ParameterException {
        super();
        decode(pivotRoutingIndicators);
    }

    public PivotRoutingBackwardInformationImpl() {
        super();

    }

    public int getCode() {

        return _PARAMETER_CODE;
    }

    @Override
    protected Map<Integer, Class<? extends AbstractInformationImpl>> getTagMapping() {
        return tagMapping;
    }

    @Override
    public void setReturnToInvokingExchangeDuration(ReturnToInvokingExchangeDuration... duration) {
        super.setInformation(duration);
    }

    @Override
    public ReturnToInvokingExchangeDuration[] getReturnToInvokingExchangeDuration() {
        return (ReturnToInvokingExchangeDuration[]) super.getInformation(ReturnToInvokingExchangeDuration.class);
    }

    @Override
    public void setReturnToInvokingExchangeCallIdentifier(ReturnToInvokingExchangeCallIdentifier... cid) {
        super.setInformation(cid);
    }

    @Override
    public ReturnToInvokingExchangeCallIdentifier[] getReturnToInvokingExchangeCallIdentifier() {
        return (ReturnToInvokingExchangeCallIdentifier[]) super.getInformation(ReturnToInvokingExchangeCallIdentifier.class);
    }

    @Override
    public void setInvokingPivotReason(InvokingPivotReason... reason) {
        super.setInformation(reason);
    }

    @Override
    public InvokingPivotReason[] getInvokingPivotReason() {
        return (InvokingPivotReason[]) super.getInformation(InvokingPivotReason.class);
    }
}
