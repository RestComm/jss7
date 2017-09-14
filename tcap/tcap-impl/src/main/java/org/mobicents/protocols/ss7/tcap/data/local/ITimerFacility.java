package org.mobicents.protocols.ss7.tcap.data.local;

import java.util.concurrent.TimeUnit;

public interface ITimerFacility<T> {
  Object schedule(ITimerTask<T> task, long time, TimeUnit units) throws IllegalArgumentException;
  void cancel(Object id);
}
