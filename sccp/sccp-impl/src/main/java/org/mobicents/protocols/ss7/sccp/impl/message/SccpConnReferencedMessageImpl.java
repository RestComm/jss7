package org.mobicents.protocols.ss7.sccp.impl.message;

import org.mobicents.protocols.ss7.sccp.message.SccpConnMessage;
import org.mobicents.protocols.ss7.sccp.parameter.LocalReference;

public abstract class SccpConnReferencedMessageImpl extends SccpMessageImpl implements SccpConnMessage {
    protected LocalReference destinationLocalReferenceNumber;

    // for AK, CREF, DT1, DT2 messages it isn't sent over network, used in send message methods only
    protected LocalReference sourceLocalReferenceNumber;

    protected SccpConnReferencedMessageImpl(int maxDataLen, int type, int sls, int localSsn) {
        super(maxDataLen, type, sls, localSsn);
    }

    protected SccpConnReferencedMessageImpl(int maxDataLen, int type, int incomingOpc, int incomingDpc, int incomingSls, int networkId) {
        super(maxDataLen, type, incomingOpc, incomingDpc, incomingSls, networkId);
    }

    public LocalReference getDestinationLocalReferenceNumber() {
        return destinationLocalReferenceNumber;
    }

    public void setDestinationLocalReferenceNumber(LocalReference number) {
        this.destinationLocalReferenceNumber = number;
    }

    public LocalReference getSourceLocalReferenceNumber() {
        return sourceLocalReferenceNumber;
    }

    public void setSourceLocalReferenceNumber(LocalReference sourceLocalReferenceNumber) {
        this.sourceLocalReferenceNumber = sourceLocalReferenceNumber;
    }
}
