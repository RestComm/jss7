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

package org.mobicents.protocols.ss7.tcapAnsi;

/**
*
* @author sergey vetyutnev
*
*/
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
