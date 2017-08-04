package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public interface IPreviewDialogDataStorage {
    void removeDialog(IDialog d);
    void clear();

    IDialog getPreviewDialog(PreviewDialogDataKey ky1, PreviewDialogDataKey ky2, SccpAddress localAddress, SccpAddress remoteAddress, int seqControl);

    PreviewDialogImpl createPreviewDialog(PreviewDialogDataKey ky, SccpAddress localAddress, SccpAddress address, int seqControl) throws TCAPException;

    /*
                            di = pdi;

                            pdi.setLastACN(di.getApplicationContextName());
                            pdi.getLocalPrevewDialogData().setOperationsSentB(pdi.operationsSent);
                            pdi.getLocalPrevewDialogData().setOperationsSentA(pdi.operationsSentA);
     */


int getSize();

    void setProvider(TCAPProviderImpl tcapProvider);
}
