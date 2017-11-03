package org.mobicents.ss7.service;

import org.mobicents.protocols.ss7.oam.common.jmxss7.Ss7Management;
import org.mobicents.ss7.management.console.ShellExecutor;

public interface SS7ServiceInterface {
    ShellExecutor getBeanSctpShellExecutor();

    ShellExecutor getBeanM3uaShellExecutor();

    ShellExecutor getBeanSccpExecutor();

    ShellExecutor getBeanTcapExecutor();

    Ss7Management getSs7Management();
}