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
import java.util.Arrays;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.BitSetStrictLength;
import org.mobicents.protocols.asn.External;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.tcap.asn.comp.PAbortCauseType;

/**
 * <p>
 * As per the ITU-T Rec Q.773 Table 30/Q.773 – Dialogue Portion
 * </p>
 * <br/>
 * <p>
 * The DialogPortion consist of Dialogue Portion Tag (0x6B), Dialogue Portion Length; External Tag (0x28), External Length;
 * Structured or unstructured dialogue
 * </p>
 * <br/>
 * <p>
 * As per the Table 33/Q.773 –Structured Dialogue is represented as
 * </p>
 * <br/>
 * <p>
 * Object Identifier Tag (0x06), Object Identifier Length; Dialogue-as-ID value; Single-ASN.1-type Tag (0xa0), Single-ASN.1-type
 * Length; Dialogue PDU
 * </p>
 * <br/>
 * <p>
 * As per the Table 37/Q.773 – Dialogue-As-ID Value is represented OID. Please look {@link DialogPortionImpl._DIALG_STRUCTURED}
 * </p>
 * <br/>
 * <p>
 * As per the Table 34/Q.773 – Unstructured Dialogue is represented as
 * </p>
 * <br/>
 * <p>
 * Object Identifier Tag (0x06), Object Identifier Length; Unidialogue-as-ID value; Single-ASN.1-type Tag (0xa0),
 * Single-ASN.1-type Length; Unidirectional Dialogue PDU
 * </p>
 * <br/>
 * <p>
 * As per the Table 36/Q.773 – Unidialogue-As-ID Value is represented as OID. Please look {@link DialogPortionImpl._DIALG_UNI}
 * </p>
 *
 *
 *
 * @author baranowb
 * @author amit bhayani
 * @author sergey vetyutnev
 *
 */
public class DialogPortionImpl implements DialogPortion {

    // Encoded OID, dont like this....
    private static final long[] _DIALG_UNI = new long[] { 0, 0, 17, 773, 1, 2, 1 };
    private static final long[] _DIALG_STRUCTURED = new long[] { 0, 0, 17, 773, 1, 1, 1 };

    // MANDATORY - in sequence, our payload
    private DialogAPDU dialogAPDU;

    private boolean uniDirectional;

    private External ext = new External();

    public DialogPortionImpl() {
        super();
        // our defs, will be those are overiden on read in super class, but for
        // DialogPortion, this is what we want.
        setOid(true);
        setAsn(true);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.DialogPortion#isUnidirectional()
     */
    public boolean isUnidirectional() {
        return this.uniDirectional;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.ss7.tcap.asn.DialogPortion#setUnidirectional( boolean)
     */
    public void setUnidirectional(boolean flag) {
        if (flag) {
            ext.setOidValue(_DIALG_UNI);
            // super.oidValue = _DIALG_UNI;
        } else {
            ext.setOidValue(_DIALG_STRUCTURED);
            // super.oidValue = _DIALG_STRUCTURED;
        }
        this.uniDirectional = flag;

    }

    /**
     * @return the dialogAPDU
     */
    public DialogAPDU getDialogAPDU() {
        return dialogAPDU;
    }

    /**
     * @param dialogAPDU the dialogAPDU to set
     */
    public void setDialogAPDU(DialogAPDU dialogAPDU) {
        // FIXME: check content VS apdu TYPE ?
        this.dialogAPDU = dialogAPDU;
    }

    public String toString() {
        return "DialogPortion[dialogAPDU=" + dialogAPDU + ", uniDirectional=" + uniDirectional + "]";
    }

    public void encode(AsnOutputStream aos) throws EncodeException {

        try {
            aos.writeTag(Tag.CLASS_APPLICATION, false, _TAG);
            int pos = aos.StartContentDefiniteLength();

            if (this.dialogAPDU == null) {
                throw new EncodeException("No APDU in DialogPortion is defined when encoding DialogPortion");
            }

            if (this.getOidValue() == null) {
                throw new EncodeException("No setUnidirectional() for DialogPortion is defined when encoding DialogPortion");
            }

            AsnOutputStream aos2 = new AsnOutputStream();
            dialogAPDU.encode(aos2);
            ext.setEncodeType(aos2.toByteArray());

            ext.encode(aos);

            aos.FinalizeContent(pos);

        } catch (AsnException e) {
            throw new EncodeException("AsnException when encoding DialogPortion: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.protocols.asn.External#decode(org.mobicents.protocols.asn .AsnInputStream)
     */

    public void decode(AsnInputStream aisA) throws ParseException {

        // TAG has been decoded already, now, lets get LEN
        try {
            AsnInputStream ais = aisA.readSequenceStream();

            int tag = ais.readTag();
            if (tag != Tag.EXTERNAL)
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding DialogPortion: wrong value of tag, expected EXTERNAL, found: " + tag);

            ext.decode(ais);

            if (!isAsn() || !isOid()) {
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding DialogPortion: Oid and Asd parts not found");
            }

            // Check Oid
            if (Arrays.equals(_DIALG_UNI, this.getOidValue()))
                this.uniDirectional = true;
            else if (Arrays.equals(_DIALG_STRUCTURED, this.getOidValue()))
                this.uniDirectional = false;
            else
                throw new ParseException(PAbortCauseType.IncorrectTxPortion, null,
                        "Error decoding DialogPortion: bad Oid value");

            AsnInputStream loaclAsnIS = new AsnInputStream(ext.getEncodeType());

            // now lets get APDU
            tag = loaclAsnIS.readTag();
            this.dialogAPDU = TcapFactory.createDialogAPDU(loaclAsnIS, tag, isUnidirectional());

        } catch (IOException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null, "IOException when decoding DialogPortion: "
                    + e.getMessage(), e);
        } catch (AsnException e) {
            throw new ParseException(PAbortCauseType.BadlyFormattedTxPortion, null,
                    "AsnException when decoding DialogPortion: " + e.getMessage(), e);
        }
    }

    @Override
    public byte[] getEncodeType() throws AsnException {
        return ext.getEncodeType();
    }

    @Override
    public void setEncodeType(byte[] data) {
        ext.setEncodeType(data);
    }

    @Override
    public BitSetStrictLength getEncodeBitStringType() throws AsnException {
        return ext.getEncodeBitStringType();
    }

    @Override
    public void setEncodeBitStringType(BitSetStrictLength data) {
        ext.setEncodeBitStringType(data);
    }

    @Override
    public boolean isOid() {
        return ext.isOid();
    }

    @Override
    public void setOid(boolean oid) {
        ext.setOid(oid);
    }

    @Override
    public boolean isInteger() {
        return ext.isInteger();
    }

    @Override
    public void setInteger(boolean integer) {
        ext.setInteger(integer);
    }

    @Override
    public boolean isObjDescriptor() {
        return ext.isObjDescriptor();
    }

    @Override
    public void setObjDescriptor(boolean objDescriptor) {
        ext.setObjDescriptor(objDescriptor);
    }

    @Override
    public long[] getOidValue() {
        return ext.getOidValue();
    }

    @Override
    public void setOidValue(long[] oidValue) {
        ext.setOidValue(oidValue);
    }

    @Override
    public long getIndirectReference() {
        return ext.getIndirectReference();
    }

    @Override
    public void setIndirectReference(long indirectReference) {
        ext.setIndirectReference(indirectReference);
    }

    @Override
    public boolean isAsn() {
        return ext.isAsn();
    }

    @Override
    public void setAsn(boolean asn) {
        ext.setAsn(asn);
    }

    @Override
    public boolean isOctet() {
        return ext.isOctet();
    }

    @Override
    public void setOctet(boolean octet) {
        ext.setOctet(octet);
    }

    @Override
    public boolean isArbitrary() {
        return ext.isArbitrary();
    }

    @Override
    public void setArbitrary(boolean arbitrary) {
        ext.setArbitrary(arbitrary);
    }
}
