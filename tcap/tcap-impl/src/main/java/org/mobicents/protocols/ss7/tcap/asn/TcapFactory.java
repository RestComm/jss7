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

package org.mobicents.protocols.ss7.tcap.asn;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.api.tc.component.InvokeClass;
import org.mobicents.protocols.ss7.tcap.asn.comp.Component;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.ErrorCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.GeneralProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCode;
import org.mobicents.protocols.ss7.tcap.asn.comp.OperationCodeType;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Parameter;
import org.mobicents.protocols.ss7.tcap.asn.comp.Problem;
import org.mobicents.protocols.ss7.tcap.asn.comp.ProblemType;
import org.mobicents.protocols.ss7.tcap.asn.comp.Reject;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnError;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResult;
import org.mobicents.protocols.ss7.tcap.asn.comp.ReturnResultLast;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCAbortMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCBeginMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCContinueMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCEndMessage;
import org.mobicents.protocols.ss7.tcap.asn.comp.TCUniMessage;

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

    public static DialogAPDU createDialogAPDU(AsnInputStream ais, int tag, boolean unidirectional) throws ParseException {

        if (ais.getTagClass() != Tag.CLASS_APPLICATION)
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "Error decoding dialog APDU: wrong tag class for APDU, found: " + ais.getTagClass());

        if (unidirectional) {
            // only one
            if (tag != DialogAPDU._TAG_UNIDIRECTIONAL) {
                throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                        "Error decoding dialog APDU: wrong tag for APDU, found: " + tag);
            } else {
                // create UNIPDU

                DialogUniAPDUImpl d = new DialogUniAPDUImpl();
                d.decode(ais);
                return d;
            }

        } else {

            if (tag == DialogAPDU._TAG_REQUEST) {
                DialogRequestAPDUImpl d = new DialogRequestAPDUImpl();
                d.decode(ais);
                return d;
            }
            if (tag == DialogAPDU._TAG_RESPONSE) {
                DialogResponseAPDUImpl d = new DialogResponseAPDUImpl();
                d.decode(ais);
                return d;

            }
            if (tag == DialogAPDU._TAG_ABORT) {
                DialogAbortAPDUImpl da = new DialogAbortAPDUImpl();
                da.decode(ais);
                return da;
            }

            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null, "Wrong tag for APDU, found: " + tag);
        }
    }

    public static DialogRequestAPDU createDialogAPDURequest() {
        return new DialogRequestAPDUImpl();
    }

    public static DialogResponseAPDU createDialogAPDUResponse() {
        return new DialogResponseAPDUImpl();
    }

    public static DialogUniAPDU createDialogAPDUUni() {
        return new DialogUniAPDUImpl();
    }

    public static DialogAbortAPDU createDialogAPDUAbort() {
        return new DialogAbortAPDUImpl();
    }

    public static ProtocolVersion createProtocolVersion() {
        return new ProtocolVersionImpl();
    }

    public static ProtocolVersion createProtocolVersion(AsnInputStream ais) throws ParseException {
        ProtocolVersionImpl pv = new ProtocolVersionImpl();
        pv.decode(ais);
        return pv;
    }

    public static ApplicationContextName createApplicationContextName(long[] oid) {
        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
        acn.setOid(oid);
        return acn;
    }

    public static ApplicationContextName createApplicationContextName(AsnInputStream ais) throws ParseException {
        ApplicationContextNameImpl acn = new ApplicationContextNameImpl();
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

    public static Result createResult() {
        return new ResultImpl();
    }

    public static Result createResult(AsnInputStream localAis) throws ParseException {
        ResultImpl ui = new ResultImpl();
        ui.decode(localAis);
        return ui;
    }

    public static ResultSourceDiagnostic createResultSourceDiagnostic() {
        return new ResultSourceDiagnosticImpl();
    }

    public static ResultSourceDiagnostic createResultSourceDiagnostic(AsnInputStream localAis) throws ParseException {
        ResultSourceDiagnosticImpl ui = new ResultSourceDiagnosticImpl();
        ui.decode(localAis);
        return ui;
    }

    public static AbortSource createAbortSource() {
        AbortSourceImpl as = new AbortSourceImpl();
        return as;
    }

    public static AbortSource createAbortSource(AsnInputStream localAis) throws ParseException {
        AbortSourceImpl as = new AbortSourceImpl();
        as.decode(localAis);
        return as;
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

    public static TCContinueMessage createTCContinueMessage(AsnInputStream localAis) throws ParseException {
        TCContinueMessageImpl tc = new TCContinueMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCContinueMessage createTCContinueMessage() {
        TCContinueMessageImpl tc = new TCContinueMessageImpl();
        return tc;
    }

    public static TCEndMessage createTCEndMessage(AsnInputStream localAis) throws ParseException {
        TCEndMessageImpl tc = new TCEndMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCEndMessage createTCEndMessage() {
        TCEndMessageImpl tc = new TCEndMessageImpl();
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

    public static TCBeginMessage createTCBeginMessage(AsnInputStream localAis) throws ParseException {
        TCBeginMessageImpl tc = new TCBeginMessageImpl();
        tc.decode(localAis);
        return tc;
    }

    public static TCBeginMessage createTCBeginMessage() {
        TCBeginMessageImpl tc = new TCBeginMessageImpl();
        return tc;
    }

    public static OperationCode createOperationCode() {
        OperationCodeImpl oc = new OperationCodeImpl();
        return oc;
    }

    public static OperationCode createOperationCode(int tag, AsnInputStream localAis) throws ParseException {
        OperationCodeImpl oc = new OperationCodeImpl();
        oc.setOperationType(OperationCode._TAG_GLOBAL == tag ? OperationCodeType.Global : OperationCodeType.Local);
        oc.decode(localAis);
        return oc;
    }

    public static Parameter createParameter() {
        ParameterImpl p = new ParameterImpl();
        return p;
    }

    public static Parameter createParameter(int tag, AsnInputStream localAis, boolean singleParameterInAsn)
            throws ParseException {
        ParameterImpl p = new ParameterImpl();
        p.setTag(tag);
        // p.setPrimitive(localAis.isTagPrimitive());
        // p.setTagClass(localAis.getTagClass());
        if (singleParameterInAsn)
            p.setSingleParameterInAsn();
        p.decode(localAis);
        return p;
    }

    public static Component createComponent(AsnInputStream localAis) throws ParseException {

        try {
            try {
                int tag = localAis.readTag();

                Component c = null;
                if (localAis.getTagClass() != Tag.CLASS_CONTEXT_SPECIFIC) {
                    throw new ParseException(null, GeneralProblemType.UnrecognizedComponent,
                            "Error decoding a component: bad tag class: " + localAis.getTagClass());
                }

                switch (tag) {
                    case Invoke._TAG:
                        c = createComponentInvoke();
                        c.decode(localAis);
                        break;
                    case ReturnResult._TAG:
                        c = createComponentReturnResult();
                        c.decode(localAis);
                        break;
                    case ReturnResultLast._TAG:
                        c = createComponentReturnResultLast();
                        c.decode(localAis);
                        break;
                    case ReturnError._TAG:
                        c = createComponentReturnError();
                        c.decode(localAis);
                        break;
                    case Reject._TAG:
                        c = createComponentReject();
                        c.decode(localAis);
                        break;
                    default:
                        localAis.advanceElement();
                        throw new ParseException(null, GeneralProblemType.UnrecognizedComponent,
                                "Error decoding a component: bad tag: " + tag);
                }

                return c;
            } catch (IOException e) {
                throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent,
                        "IOException while decoding a component: " + e.getMessage(), e);
            } catch (AsnException e) {
                throw new ParseException(null, GeneralProblemType.BadlyStructuredComponent,
                        "AsnException while decoding a component: " + e.getMessage(), e);
            }
        } catch (ParseException e) {
            if (e.getProblem() != null) {
                Reject rej = TcapFactory.createComponentReject();
                rej.setLocalOriginated(true);
                rej.setInvokeId(e.getInvokeId());
                Problem problem = new ProblemImpl();
                problem.setGeneralProblemType(e.getProblem());
                rej.setProblem(problem);

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

    public static ReturnResult createComponentReturnResult() {

        return new ReturnResultImpl();
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

    public static Problem createProblem(ProblemType pt, AsnInputStream ais) throws ParseException {
        Problem p = createProblem(pt);
        p.decode(ais);
        return p;
    }

    public static Problem createProblem(ProblemType pt) {
        Problem p = new ProblemImpl();
        p.setType(pt);
        return p;
    }

    public static ErrorCode createErrorCode(int tag, AsnInputStream ais) throws ParseException {
        ErrorCode p = createErrorCode();
        ((ErrorCodeImpl) p).setErrorCodeType(ErrorCode._TAG_GLOBAL == tag ? ErrorCodeType.Global : ErrorCodeType.Local);
        p.decode(ais);
        return p;
    }

    public static ErrorCode createErrorCode() {
        ErrorCode p = new ErrorCodeImpl();
        return p;
    }
}
