package org.restcomm.ss7.service;

import org.restcomm.protocols.ss7.oam.common.jmxss7.Ss7Management;
import org.restcomm.ss7.management.console.ShellExecutor;

public interface SS7ServiceInterface {
    ShellExecutor getBeanSctpShellExecutor();

    ShellExecutor getBeanM3uaShellExecutor();

    ShellExecutor getBeanSccpExecutor();

    ShellExecutor getBeanTcapExecutor();

    Ss7Management getSs7Management();
}