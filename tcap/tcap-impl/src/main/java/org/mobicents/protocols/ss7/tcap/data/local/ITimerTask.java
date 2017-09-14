package org.mobicents.protocols.ss7.tcap.data.local;

import java.io.Serializable;

/**
 * Created by piotr.sokolowski on 2017-06-02.
 */
public interface ITimerTask<T> extends Serializable {
    String getId();
    void handleTimeEvent(T provider);
}
