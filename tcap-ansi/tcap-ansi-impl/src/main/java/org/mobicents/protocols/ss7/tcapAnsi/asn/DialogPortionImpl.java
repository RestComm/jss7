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
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ApplicationContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.Confidentiality;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.DialogPortion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.EncodeException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ParseException;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.ProtocolVersion;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.SecurityContext;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.UserInformation;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.PAbortCause;
import org.mobicents.protocols.ss7.tcapAnsi.api.asn.comp.RejectProblem;

/**
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class DialogPortionImpl implements DialogPortion {

    private ProtocolVersion protocolVersion;
    private ApplicationContext applicationContext;
    private UserInformation[] userInformation;
    private SecurityContext securityContext;
    private Confidentiality confidentiality;


    public DialogPortionImpl() {
        super();
    }


    @Override
    public ProtocolVersion getProtocolVersion() {
        return protocolVersion;
    }

    @Override
    public void setProtocolVersion(ProtocolVersion val) {
        protocolVersion = val;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext val) {
        applicationContext = val;
    }

    @Override
    public UserInformation[] getUserInformation() {
        return userInformation;
    }

    @Override
    public void setUserInformation(UserInformation[] val) {
        userInformation = val;
    }

    @Override
    public SecurityContext getSecurityContext() {
        return securityContext;
    }

    @Override
    public void setSecurityContext(SecurityContext val) {
        securityContext = val;
    }

    @Override
    public Confidentiality getConfidentiality() {
        return confidentiality;
    }

    @Override
    public void setConfidentiality(Confidentiality val) {
        confidentiality = val;
    }

    public void decode(AsnInputStream aisA) throws ParseException {

        this.protocolVersion = null;
        this.applicationContext = null;
        this.userInformation = null;
        this.securityContext = null;
        this.confidentiality = null;

        try {
            AsnInputStream ais = aisA.readSequenceStream();

            while (true) {
                if (ais.available() == 0)
                    break;

                int tag = ais.readTag();

                if (ais.getTagClass() == Tag.CLASS_PRIVATE) {
                    switch (tag) {
                    case ProtocolVersion._TAG_PROTOCOL_VERSION:
                        if (!ais.isTagPrimitive())
                            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                    "Error decoding DialogPortion: ProtocolVersion must be primitive");
                        this.protocolVersion = TcapFactory.createProtocolVersion(ais);
                        break;

                    case ApplicationContext._TAG_INTEGER:
                    case ApplicationContext._TAG_OBJECT_ID:
                        if (!ais.isTagPrimitive())
                            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                    "Error decoding DialogPortion: ApplicationContext must be primitive");
                        this.applicationContext = TcapFactory.createApplicationContext(ais);
                        break;

                    case UserInformation._TAG_USER_INFORMATION:
                        // TODO: implement it
                        ais.advanceElement();
                        break;

                    default:
                        throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                "Error decoding DialogPortion: Bad tag or tag class: tag=" + tag + ", tagClass=" + ais.getTagClass());
                    }
                } else if (ais.getTagClass() == Tag.CLASS_CONTEXT_SPECIFIC) {
                    switch (tag) {
                    case SecurityContext._TAG_SECURITY_CONTEXT_INTEGER:
                    case SecurityContext._TAG_SECURITY_CONTEXT_OID:
                        if (!ais.isTagPrimitive())
                            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                    "Error decoding DialogPortion: SecurityContext must be primitive");
                        // TODO: implement it
                        ais.advanceElement();
                        break;

                    case Confidentiality._TAG_CONFIDENTIALITY:
                        // TODO: implement it
                        ais.advanceElement();
                        break;

                    default:
                        throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                                "Error decoding DialogPortion: Bad tag or tag class: tag=" + tag + ", tagClass=" + ais.getTagClass());
                    }
                } else {
                    throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                            "Error decoding DialogPortion: Bad tag or tag class: tag=" + tag + ", tagClass=" + ais.getTagClass());
                }
            }


//            int tag = ais.readTag();
//            if (tag != Tag.EXTERNAL)
//                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
//                        "Error decoding DialogPortion: wrong value of tag, expected EXTERNAL, found: " + tag);
//
//            ext.decode(ais);
//
//            if (!isAsn() || !isOid()) {
//                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
//                        "Error decoding DialogPortion: Oid and Asd parts not found");
//            }
//
//            // Check Oid
//            if (Arrays.equals(_DIALG_UNI, this.getOidValue()))
//                this.uniDirectional = true;
//            else if (Arrays.equals(_DIALG_STRUCTURED, this.getOidValue()))
//                this.uniDirectional = false;
//            else
//                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
//                        "Error decoding DialogPortion: bad Oid value");
//
//            AsnInputStream loaclAsnIS = new AsnInputStream(ext.getEncodeType());
//
//            // now lets get APDU
//            tag = loaclAsnIS.readTag();
//            this.dialogAPDU = TcapFactory.createDialogAPDU(loaclAsnIS, tag, isUnidirectional());


        } catch (IOException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                    "IOException while decoding DialogPortion: " + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCause.BadlyStructuredDialoguePortion, RejectProblem.transactionBadlyStructuredTransPortion,
                    "AsnException while decoding DialogPortion: " + e.getMessage(), e);
        }
    }

    public void encode(AsnOutputStream aos) throws EncodeException {

        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, DialogPortion._TAG_DIALOG_PORTION);
            int pos = aos.StartContentDefiniteLength();

            if (this.protocolVersion != null) {
                this.protocolVersion.encode(aos);
            }

            if (this.applicationContext != null) {
                this.applicationContext.encode(aos);
            }

            if (this.userInformation != null && this.userInformation.length > 0) {
                // TODO: implement it
            }

            if (this.securityContext != null) {
                this.securityContext.encode(aos);
            }

            if (this.confidentiality != null) {
                this.confidentiality.encode(aos);
            }

            aos.FinalizeContent(pos);

        } catch (AsnException e) {
            throw new EncodeException("AsnException when encoding DialogPortion: " + e.getMessage(), e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DialogPortion[");

        if (this.getProtocolVersion() != null) {
            sb.append("ProtocolVersion=");
            sb.append(this.getProtocolVersion());
            sb.append(", ");
        }
        if (this.getApplicationContext() != null) {
            sb.append("ApplicationContext=");
            sb.append(this.getApplicationContext());
            sb.append(", ");
        }
        if (this.getUserInformation() != null && this.getUserInformation().length > 0) {
            sb.append("UserInformation=[");
            int i1 = 0;
            for (UserInformation ui : this.getUserInformation()) {
                if (i1 == 0)
                    i1 = 1;
                else
                    sb.append(", ");
                sb.append(ui.toString());
            }
            sb.append("], ");
        }
        if (this.getSecurityContext() != null) {
            sb.append("SecurityContext=");
            sb.append(this.getSecurityContext());
            sb.append(", ");
        }
        if (this.getConfidentiality() != null) {
            sb.append("Confidentiality=");
            sb.append(this.getConfidentiality());
            sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
