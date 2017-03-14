package org.mobicents.protocols.ss7.map.load.mapp;

import org.mobicents.protocols.ss7.map.api.MAPDialogListener;
import org.mobicents.protocols.ss7.map.api.service.supplementary.MAPServiceSupplementaryListener;

public interface MAPScenario extends MAPDialogListener, MAPServiceSupplementaryListener {
    void init(MAPpContext ctx);
    void createDialog(MAPpContext ctx) throws Exception ;
}
