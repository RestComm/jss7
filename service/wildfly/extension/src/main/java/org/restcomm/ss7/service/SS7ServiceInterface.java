package org.restcomm.ss7.service;

import org.restcomm.ss7.management.console.ShellExecutor;

public interface SS7ServiceInterface {
    ShellExecutor getBeanSctpShellExecutor();

    ShellExecutor getBeanM3uaShellExecutor();

    ShellExecutor getBeanSccpExecutor();

    ShellExecutor getBeanTcapExecutor();

//    Ss7Management getSs7Management();
}