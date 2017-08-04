package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.tcap.asn.comp.Invoke;

/**
 * Created by piotr.sokolowski on 2017-06-07.
 */
public interface IPreviewDialogData extends IDialogDataBase {
    PreviewDialogDataKey getPreviewDialogDataKey1();
    PreviewDialogDataKey getPreviewDialogDataKey2();

    void setPreviewDialogDataKey1(PreviewDialogDataKey val);
    void setPreviewDialogDataKey2(PreviewDialogDataKey val);

    ITCAPOperation newTCAPOperation(boolean sideB, PreviewDialogImpl previewDialog, Invoke ci);
    ITCAPOperation getTCAPOperation(boolean sideB, Long invokeId);
    void freeTcapOperation(ITCAPOperation operation);

    void setUpperDialog(Object o);
    Object getUpperDialog();
}
