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

package org.mobicents.protocols.ss7.tcapAnsi.tc.component;

import org.mobicents.protocols.ss7.tcapAnsi.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcapAnsi.api.ComponentPrimitiveFactory;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcapAnsi.asn.InvokeImpl;
import org.mobicents.protocols.ss7.tcapAnsi.asn.TcapFactory;

/**
 * @author baranowb
 *
 */
public class ComponentPrimitiveFactoryImpl implements ComponentPrimitiveFactory {

    private TCAPProviderImpl provider;

    public ComponentPrimitiveFactoryImpl(TCAPProviderImpl tcaProviderImpl) {
        this.provider = tcaProviderImpl;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#createTCInvokeRequest()
     */
    public Invoke createTCInvokeRequestNotLast() {

        InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke();
        t.setNotLast(true);
        t.setProvider(provider);
        return t;
    }

    public Invoke createTCInvokeRequestLast() {

        InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke();
        t.setNotLast(false);
        t.setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCInvokeRequest()
     */
    public Invoke createTCInvokeRequestNotLast(InvokeClass invokeClass) {

        InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke(invokeClass);
        t.setNotLast(true);
        t.setProvider(provider);
        return t;
    }

    public Invoke createTCInvokeRequestLast(InvokeClass invokeClass) {

        InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke(invokeClass);
        t.setNotLast(false);
        t.setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCRejectRequest()
     */
    public Reject createTCRejectRequest() {

        return TcapFactory.createComponentReject();
    }

    public ReturnError createTCReturnErrorRequest() {

        return TcapFactory.createComponentReturnError();
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCResultRequest(boolean)
     */
    public ReturnResultLast createTCResultLastRequest() {

        return TcapFactory.createComponentReturnResultLast();

    }

    public ReturnResultNotLast createTCResultNotLastRequest() {

        return TcapFactory.createComponentReturnResultNotLast();
    }

    public OperationCode createOperationCode() {
        return TcapFactory.createOperationCode();
    }

    public ErrorCode createErrorCode() {
        return TcapFactory.createErrorCode();
    }

    public Parameter createParameter() {
        return TcapFactory.createParameter();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory#createParameter(int, int, boolean)
     */
    public Parameter createParameter(int tag, int tagClass, boolean isPrimitive) {
        Parameter p = TcapFactory.createParameter();
        p.setTag(tag);
        p.setTagClass(tagClass);
        p.setPrimitive(isPrimitive);
        return p;
    }
}
