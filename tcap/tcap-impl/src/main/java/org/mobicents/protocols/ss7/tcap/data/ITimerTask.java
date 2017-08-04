package org.mobicents.protocols.ss7.tcap.data;

import org.mobicents.protocols.ss7.tcap.TCAPProviderImpl;

import java.io.Serializable;

/**
 * Created by piotr.sokolowski on 2017-06-02.
 */
public interface ITimerTask extends Serializable {
    String getId();
    void handleTimeEvent(TCAPProviderImpl tpi);
}
