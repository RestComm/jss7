package org.mobicents.protocols.ss7.tcap.data;

import java.util.concurrent.TimeUnit;

public interface ITimerFacility {
  Object schedule(ITimerTask task,long time, TimeUnit units) throws IllegalArgumentException;
  void cancel(Object id);
}
