package org.mobicents.protocols.ss7.tcap.data.local;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.data.IDialog;
import org.mobicents.protocols.ss7.tcap.data.PreviewDialogDataKey;

public class PreviewIdleTimerTask implements ITimerTask<TCAPProviderImpl> {
    public static final Logger logger= Logger.getLogger(PreviewIdleTimerTask.class);
    final PreviewDialogDataKey key;
    PreviewIdleTimerTask(PreviewDialogDataKey key) {
        this.key=key;
    }

    @Override
    public String getId() {
        return "PreviewDialog/IdleTimer/"+key;
    }

    @Override
    public void handleTimeEvent(TCAPProviderImpl tpi) {
        IDialog dlg=tpi.getPreviewDialogDataStorage().getPreviewDialog(key, null, null, null, 0);

        if(dlg!=null) {
            dlg.handleIdleTimeout();
        } else {
            logger.warn("Timeout occurred for preview dialog, which no longer exists:"+key);
        }
    }
}

