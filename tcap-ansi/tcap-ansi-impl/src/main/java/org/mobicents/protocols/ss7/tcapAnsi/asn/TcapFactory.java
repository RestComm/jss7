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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Component;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultNotLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCQueryMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCConversationMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCResponseMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.TCUniMessage;
import org.mobicents.protocols.ss7.tcapAnsi.api.tc.component.InvokeClass;

/**
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public final class TcapFactory {

    public static DialogPortion createDialogPortion(AsnInputStream ais) throws ParseException {
        DialogPortionImpl dpi = new DialogPortionImpl();
        dpi.decode(ais);
        return dpi;
    }

    public static DialogPortion createDialogPortion() {
        return new DialogPortionImpl();
    }

    public static ProtocolVersion createProtocolVersion() {
        return new ProtocolVersionImpl();
    }

    public static ProtocolVersion createProtocolVersion(AsnInputStream ais) throws ParseException {
        ProtocolVersionImpl pv = new ProtocolVersionImpl();
        pv.decode(ais);
        return pv;
    }

    public static ApplicationContext createApplicationContext(long[] oid) {
        ApplicationContextImpl acn = new ApplicationContextImpl();
        acn.setOid(oid);
        return acn;
    }

    public static ApplicationContext createApplicationContext(long val) {
        ApplicationContextImpl acn = new ApplicationContextImpl();
        acn.setInteger(val);
        return acn;
    }

    public static ApplicationContext createApplicationContext(AsnInputStream ais) throws ParseException {
        ApplicationContextImpl acn = new ApplicationContextImpl();
        acn.decode(ais);
        return acn;
    }

    public static UserInformation createUserInformation() {
        return new UserInformationImpl();
    }

    public static UserInformation createUserInformation(AsnInputStream localAis) throws ParseException {
        UserInformationImpl ui = new UserInformationImpl();
        ui.decode(localAis);
        return ui;
    }

    public static TCUniMessage createTCUniMessage(AsnInputStream localAis) throws ParseException {
        TCUniMessageImpl tc = new TCUniMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCUniMessage createTCUniMessage() {
        TCUniMessageImpl tc = new TCUniMessageImpl();
        return tc;
    }

    public static TCConversationMessage createTCContinueMessage(AsnInputStream localAis) throws ParseException {
        TCConversationMessageImpl tc = new TCConversationMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCConversationMessage createTCContinueMessage() {
        TCConversationMessageImpl tc = new TCConversationMessageImpl();
        return tc;
    }

    public static TCResponseMessage createTCEndMessage(AsnInputStream localAis) throws ParseException {
        TCResponseMessageImpl tc = new TCResponseMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCResponseMessage createTCEndMessage() {
        TCResponseMessageImpl tc = new TCResponseMessageImpl();
        return tc;
    }

    public static TCAbortMessage createTCAbortMessage(AsnInputStream localAis) throws ParseException {
        TCAbortMessageImpl tc = new TCAbortMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCAbortMessage createTCAbortMessage() {
        TCAbortMessageImpl tc = new TCAbortMessageImpl();
        return tc;
    }

    public static TCQueryMessage createTCBeginMessage(AsnInputStream localAis) throws ParseException {
        TCQueryMessageImpl tc = new TCQueryMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCQueryMessage createTCBeginMessage() {
        TCQueryMessageImpl tc = new TCQueryMessageImpl();
        return tc;
    }

    public static OperationCode createOperationCode() {
        OperationCodeImpl oc = new OperationCodeImpl();
        return oc;
    }

    public static OperationCode createOperationCode(AsnInputStream localAis) throws ParseException {
        OperationCodeImpl oc = new OperationCodeImpl();
        oc.decode(localAis);
        return oc;
    }

    public static Parameter createParameter() {
        ParameterImpl p = new ParameterImpl();
        return p;
    }

    public static Parameter createParameter(AsnInputStream localAis)
            throws ParseException {
        ParameterImpl p = new ParameterImpl();
        p.decode(localAis);
        return p;
    }

    public static Component createComponent(AsnInputStream localAis) throws ParseException {

        try {
            try {
                int tag = localAis.readTag();

                Component c = null;
                if (localAis.getTagClass() != Tag.CLASS_PRIVATE) {
                    throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalUnrecognisedComponentType,
                            "Error decoding a component: bad tag class: " + localAis.getTagClass());
                }

                switch (tag) {
                case Invoke._TAG_INVOKE_LAST:
                case Invoke._TAG_INVOKE_NOT_LAST:
                    c = createComponentInvoke();
                    c.decode(localAis);
                    break;
                case ReturnResultNotLast._TAG_RETURN_RESULT_NOT_LAST:
                    c = createComponentReturnResult();
                    c.decode(localAis);
                    break;
                case ReturnResultLast._TAG_RETURN_RESULT_LAST:
                    c = createComponentReturnResultLast();
                    c.decode(localAis);
                    break;
                case ReturnError._TAG_RETURN_ERROR:
                    c = createComponentReturnError();
                    c.decode(localAis);
                    break;
                case Reject._TAG_REJECT:
                    c = createComponentReject();
                    c.decode(localAis);
                    break;
                default:
                    localAis.advanceElement();
                    throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalUnrecognisedComponentType,
                            "Error decoding a component: bad tag: " + tag);
                }

                return c;
            } catch (IOException e) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                        "IOException while decoding component: " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, RejectProblem.generalBadlyStructuredCompPortion,
                        "AsnException while decoding component: " + e.getMessage(), e);
            }
        } catch (ParseException e) {
            if (e.getProblem() != null) {
                Reject rej = TcapFactory.createComponentReject();
                rej.setLocalOriginated(true);
                rej.setCorrelationId(e.getInvokeId());
                rej.setProblem(e.getProblem());

                return rej;
            } else {
                throw e;
            }
        }
    }

    public static Reject createComponentReject() {

        return new RejectImpl();
    }

    public static ReturnResultLast createComponentReturnResultLast() {

        return new ReturnResultLastImpl();
    }

    public static ReturnResultNotLast createComponentReturnResult() {

        return new ReturnResultNotLastImpl();
    }

    public static Invoke createComponentInvoke() {
        return new InvokeImpl();
    }

    public static Invoke createComponentInvoke(InvokeClass invokeClass) {
        return new InvokeImpl(invokeClass);
    }

    public static ReturnError createComponentReturnError() {
        return new ReturnErrorImpl();
    }

    public static ErrorCode createErrorCode(AsnInputStream ais) throws ParseException {
        ErrorCode p = createErrorCode();
        p.decode(ais);
        return p;
    }

    public static ErrorCode createErrorCode() {
        ErrorCode p = new ErrorCodeImpl();
        return p;
    }
}
