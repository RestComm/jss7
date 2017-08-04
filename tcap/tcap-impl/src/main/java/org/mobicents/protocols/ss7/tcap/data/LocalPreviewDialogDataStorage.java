package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.sccp.parameter.SccpAddress;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.api.TCAPException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by piotr.sokolowski on 2017-06-09.
 */
public class LocalPreviewDialogDataStorage implements IPreviewDialogDataStorage {
    ConcurrentHashMap<PreviewDialogDataKey,LocalPreviewDialogData> dataMap=new ConcurrentHashMap<>();
    TCAPProviderImpl provider;
    long curDialogId;

    public void setProvider(TCAPProviderImpl provider) {
        this.provider=provider;
    }

    private Long getAvailableTxIdPreview() throws TCAPException {
        while (true) {
            if (this.curDialogId < this.provider.getStack().getDialogIdRangeStart())
                this.curDialogId = this.provider.getStack().getDialogIdRangeStart() - 1;
            if (++this.curDialogId > this.provider.getStack().getDialogIdRangeEnd())
                this.curDialogId = this.provider.getStack().getDialogIdRangeStart();
            Long id = this.curDialogId;
            return id;
        }
    }

    @Override
    public void removeDialog(IDialog d) {
        PreviewDialogDataKey key;
        key = ((PreviewDialogImpl) d).getPreviewDialogDataKey1();
        if(key!=null) {
            dataMap.remove(key);
        }
        key = ((PreviewDialogImpl) d).getPreviewDialogDataKey2();
        if(key!=null) {
            dataMap.remove(key);
        }

        ((PreviewDialogImpl) d).stopIdleTimer();
    }

    @Override
    public void clear() {
        dataMap.clear();
    }



    @Override
    public IDialog getPreviewDialog(PreviewDialogDataKey ky1, PreviewDialogDataKey ky2, SccpAddress localAddress, SccpAddress remoteAddress, int seqControl) {
        LocalPreviewDialogData data;
        if(ky1!=null) {
            data = dataMap.get(ky1);
            if (data != null) {
                if (data.getPreviewDialogDataKey1().equals(ky1))
                    return new PreviewDialogImpl(provider, data, false);
                else
                    return new PreviewDialogImpl(provider, data, true);
            }
        }
        data=dataMap.get(ky2);
        if(data!=null) {
            if(ky1!=null) {
                data.setPreviewDialogDataKey2(ky1);
                dataMap.put(ky1, data);
            }
            return new PreviewDialogImpl(provider, data, true);
        }
        return null;
    }


    @Override
    public PreviewDialogImpl createPreviewDialog(PreviewDialogDataKey ky, SccpAddress localAddress, SccpAddress remoteAddress, int seqControl) throws TCAPException {
        Long dialogId=getAvailableTxIdPreview();
        if(dataMap.size()>=provider.getStack().getMaxDialogs())
            throw new TCAPException("Maximum number of dialogs exceeded");

        LocalPreviewDialogData data=new LocalPreviewDialogData(provider,dialogId);
        data.setLocalAddress(localAddress);
        data.setRemoteAddress(remoteAddress);
        data.setPreviewDialogDataKey1(ky);
        data.setLocalTransactionIdObject(ky.origTxId);
        data.setLocalTransactionId(ky.origTxId);
        data.setDialogId(dialogId);
        data.setStructured(true);
        data.setSeqControl(seqControl);
        data.setIdleTaskTimeout(provider.getStack().getDialogIdleTimeout());
        PreviewDialogImpl dialog=new PreviewDialogImpl(provider,data,false);
        data.setDialog(dialog);
        dataMap.put(ky,data);
        dialog.startIdleTimer();
        return dialog;
    }

    @Override
    public int getSize() {
        return dataMap.size();
    }
}
