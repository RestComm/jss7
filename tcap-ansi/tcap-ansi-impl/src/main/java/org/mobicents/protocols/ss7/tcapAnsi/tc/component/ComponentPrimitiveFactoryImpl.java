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
    public Invoke createTCInvokeRequest() {

        InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke();
        t.setProvider(provider);
        return t;
    }

    /*
     * (non-Javadoc)
     *
     * @seeorg.mobicents.protocols.ss7.tcap.api.ComponentPrimitiveFactory# createTCInvokeRequest()
     */
    public Invoke createTCInvokeRequest(InvokeClass invokeClass) {

        InvokeImpl t = (InvokeImpl) TcapFactory.createComponentInvoke(invokeClass);
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

    public ReturnResultNotLast createTCResultRequest() {

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
