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

package org.mobicents.protocols.ss7.tcapAnsi.asn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformationElement;
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

    private static final Logger logger = Logger.getLogger(TcapFactory.class); // listenres

    public static DialogPortion createDialogPortion(AsnInputStream ais) throws ParseException {
        DialogPortionImpl dpi = new DialogPortionImpl();
        dpi.decode(ais);
        return dpi;
    }

    public static DialogPortion createDialogPortion() {
        return new DialogPortionImpl();
    }

    public static ProtocolVersion createProtocolVersionFull() {
        return new ProtocolVersionImpl();
    }

    public static ProtocolVersion createProtocolVersionEmpty() {
        ProtocolVersionImpl pv = new ProtocolVersionImpl();
        pv.setT1_114_1996Supported(false);
        pv.setT1_114_2000Supported(false);
        return pv;
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

    public static UserInformationElement createUserInformationElement() {
        return new UserInformationElementImpl();
    }

    public static UserInformationElement createUserInformationElement(AsnInputStream localAis) throws ParseException {
        UserInformationElementImpl ui = new UserInformationElementImpl();
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

    public static TCConversationMessage createTCConversationMessage(AsnInputStream localAis) throws ParseException {
        TCConversationMessageImpl tc = new TCConversationMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCConversationMessage createTCConversationMessage() {
        TCConversationMessageImpl tc = new TCConversationMessageImpl();
        return tc;
    }

    public static TCResponseMessage createTCResponseMessage(AsnInputStream localAis) throws ParseException {
        TCResponseMessageImpl tc = new TCResponseMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCResponseMessage createTCResponseMessage() {
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

    public static TCQueryMessage createTCQueryMessage(AsnInputStream localAis) throws ParseException {
        TCQueryMessageImpl tc = new TCQueryMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCQueryMessage createTCQueryMessage() {
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

    public static Parameter createParameterSet() {
        ParameterImpl p = new ParameterImpl();
        p.setTagClass(Tag.CLASS_PRIVATE);
        p.setTag(Parameter._TAG_SET);
        p.setPrimitive(false);
        return p;
    }

    public static Parameter createParameterSequence() {
        ParameterImpl p = new ParameterImpl();
        p.setTagClass(Tag.CLASS_PRIVATE);
        p.setTag(Parameter._TAG_SEQUENCE);
        p.setPrimitive(false);
        return p;
    }

    public static Parameter createParameter(AsnInputStream localAis)
            throws ParseException {
        ParameterImpl p = new ParameterImpl();
        p.decode(localAis);
        return p;
    }

    public static Parameter createParameter(int tag, AsnInputStream localAis, boolean singleParameterInAsn)
            throws ParseException {
        ParameterImpl p = new ParameterImpl();
        p.setTag(tag);
        if (singleParameterInAsn)
            p.setSingleParameterInAsn();
        p.decode(localAis);
        return p;
    }

    public static Reject createComponentReject() {

        return new RejectImpl();
    }

    public static ReturnResultLast createComponentReturnResultLast() {

        return new ReturnResultLastImpl();
    }

    public static ReturnResultNotLast createComponentReturnResultNotLast() {

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

    public static TransactionID readTransactionID(AsnInputStream ais) throws ParseException {

        try {
            int tag = ais.readTag();
            if (tag != TCQueryMessage._TAG_TRANSACTION_ID || ais.getTagClass() != Tag.CLASS_PRIVATE || !ais.isTagPrimitive())
                throw new ParseException(PAbortCause.IncorrectTransactionPortion,
                        "Error decoding TransactionID: bad tag or tagClass or not primitive for TransactionId, found tagClass=" + ais.getTagClass() + ", tag="
                                + tag);

            byte[] buf = ais.readOctetString();
            if (buf.length != 0 && buf.length != 4 && buf.length != 8)
                throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion,
                        "Error decoding TransactionID: originatingTransactionId bad length, must be 0, 4 or 8, found =" + buf.length);
            TransactionID res = new TransactionID();
            if (buf.length == 4) {
                res.setFirstElem(buf);
            }
            if (buf.length == 8) {
                byte[] firstElem = new byte[4];
                byte[] secondElem = new byte[4];
                System.arraycopy(buf, 0, firstElem, 0, 4);
                System.arraycopy(buf, 4, secondElem, 0, 4);
                res.setFirstElem(firstElem);
                res.setSecondElem(secondElem);
            }
            return res;
        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, "IOException while decoding TransactionID: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredTransactionPortion, "AsnException while decoding TransactionID: " + e.getMessage(), e);
        }
    }

    public static Component[] readComponents(AsnInputStream ais) {

        List<Component> cps = new ArrayList<Component>();
        AsnInputStream compAis = null;
        try {
            int tag = ais.getTag();
            if (tag != TCQueryMessage._TAG_COMPONENT_SEQUENCE || ais.getTagClass() != Tag.CLASS_PRIVATE || ais.isTagPrimitive()) {
                throw new ParseException(RejectProblem.generalUnrecognisedComponentType, "Bad component sequence tag or tag class or is primitive, tag=" + tag
                        + ", tagClass=" + ais.getTagClass());
            }

            try {
                compAis = ais.readSequenceStream();
            } catch (IOException e) {
                throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding Components: " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding Components: " + e.getMessage(), e);
            }
        } catch (ParseException e) {
            logger.error("Local Reject: " + e.getProblem() + ", " + e.getMessage(), e);

            if (e.getProblem() != null) {
                Reject rej = TcapFactory.createComponentReject();
                rej.setLocalOriginated(true);
                rej.setCorrelationId(e.getInvokeId());
                rej.setProblem(e.getProblem());
                cps.add(rej);
            }
        }

        while (compAis != null && compAis.available() > 0) {
            try {
                Component c = TcapFactory.createComponent(compAis);
                if (c == null) {
                    break;
                }
                cps.add(c);
            } catch (ParseException e) {
                logger.error("Local Reject: " + e.getProblem() + ", " + e.getMessage(), e);

                if (e.getProblem() != null) {
                    Reject rej = TcapFactory.createComponentReject();
                    rej.setLocalOriginated(true);
                    rej.setCorrelationId(e.getInvokeId());
                    rej.setProblem(e.getProblem());
                    cps.add(rej);
                }
            }
        }

        Component[] res = new Component[cps.size()];
        cps.toArray(res);
        return res;
    }

    public static Component createComponent(AsnInputStream localAis) throws ParseException {

        try {
            int tag = localAis.readTag();

            Component c = null;
            if (localAis.getTagClass() != Tag.CLASS_PRIVATE) {
                localAis.advanceElement();
                throw new ParseException(RejectProblem.generalUnrecognisedComponentType, "Error decoding a component: bad tag class: " + localAis.getTagClass());
            }

            switch (tag) {
            case Invoke._TAG_INVOKE_LAST:
            case Invoke._TAG_INVOKE_NOT_LAST:
                c = createComponentInvoke();
                c.decode(localAis);
                break;
            case ReturnResultNotLast._TAG_RETURN_RESULT_NOT_LAST:
                c = createComponentReturnResultNotLast();
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
                throw new ParseException(RejectProblem.generalUnrecognisedComponentType, "Error decoding a component: bad tag: " + tag);
            }

            return c;
        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding component: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding component: " + e.getMessage(), e);
        }
    }

    public static Parameter readParameter(AsnInputStream localAis) throws ParseException {
        if (localAis.available() == 0)
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "Parameter is not found when decoding a Parameter");

        int tag;
        try {
            tag = localAis.readTag();
            if ((tag != Parameter._TAG_SEQUENCE && tag != Parameter._TAG_SET) || localAis.getTagClass() != Tag.CLASS_PRIVATE || localAis.isTagPrimitive()) {
                throw new ParseException(RejectProblem.generalIncorrectComponentPortion,
                        "Parameters sequence/set has bad tag or tag class or is primitive: tag=" + tag + ", tagClass=" + localAis.getTagClass());
            }
            Parameter par = TcapFactory.createParameter(localAis);
            return par;
        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding parameter: " + e.getMessage(), e);
        }
    }

    public static byte[] readComponentId(AsnInputStream localAis, int minLen, int maxLen) throws ParseException {
        try {
            int tag = localAis.readTag();
            if (tag != Component._TAG_INVOKE_ID || localAis.getTagClass() != Tag.CLASS_PRIVATE || !localAis.isTagPrimitive()) {
                throw new ParseException(RejectProblem.generalIncorrectComponentPortion, "ComponentID has bad tag or tag class or is not primitive: tag=" + tag
                        + ", tagClass=" + localAis.getTagClass());
            }
            byte[] buf = localAis.readOctetString();
            if (buf.length < minLen || buf.length > maxLen) {
                throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "ComponentID has bad length: " + buf.length + ", minLength=" + minLen
                        + ", maxLength=" + maxLen);
            }
            return buf;
        } catch (IOException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "IOException while decoding ComponentId: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(RejectProblem.generalBadlyStructuredCompPortion, "AsnException while decoding ComponentId: " + e.getMessage(), e);
        }
    }
}

