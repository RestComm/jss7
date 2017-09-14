package org.mobicents.protocols.ss7.tcap.data.local;

import org.apache.log4j.Logger;
import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;
import org.mobicents.protocols.ss7.tcap.data.IDialog;

public class IdleTimerTask implements ITimerTask<TCAPProviderImpl> {
    public static final Logger logger= Logger.getLogger(IdleTimerTask.class);
    final Long dialogId;
    public IdleTimerTask(Long dialogId) {
        this.dialogId=dialogId;
    }

    @Override
    public String getId() {
        return "Dialog/IdleTimer/"+dialogId;
    }

    @Override
    public void handleTimeEvent(TCAPProviderImpl tpi) {
        IDialog dlg = tpi.getDialogDataStorage().getDialog(dialogId);
        dlg.getDialogLock().lock();
        try {
            if (dlg == null) {
                logger.warn("Handle Idle Timer - dialog not found :" + dialogId);
                return;
            }
            dlg.handleIdleTimeout();
        } finally {
            dlg.getDialogLock().unlock();
        }
    }
}
