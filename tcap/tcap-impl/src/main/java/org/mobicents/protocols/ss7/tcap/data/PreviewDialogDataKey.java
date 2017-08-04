package org.mobicents.protocols.ss7.tcap.data;

public class PreviewDialogDataKey {
    public int dpc;
    public String sccpDigits;
    public int ssn;
    public long origTxId;

    public PreviewDialogDataKey(int dpc, String sccpDigits, int ssn, long txId) {
        this.dpc = dpc;
        this.sccpDigits = sccpDigits;
        this.ssn = ssn;
        this.origTxId = txId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof PreviewDialogDataKey))
            return false;
        PreviewDialogDataKey b = (PreviewDialogDataKey) obj;

        if (this.sccpDigits != null) {
            // sccpDigits + ssn
            if (!this.sccpDigits.equals(b.sccpDigits))
                return false;
        } else {
            // dpc + ssn
            if (this.dpc != b.dpc)
                return false;
        }
        if (this.ssn != b.ssn)
            return false;
        if (this.origTxId != b.origTxId)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (this.sccpDigits != null) {
            result = prime * result + ((sccpDigits == null) ? 0 : sccpDigits.hashCode());
        } else {
            result = prime * result + this.dpc;
        }
        result = prime * result + this.ssn;
        result = prime * result + (int) (this.origTxId + (this.origTxId >> 32));
        return result;
    }
}
