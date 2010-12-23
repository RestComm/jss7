package org.mobicents.ss7.management.transceiver;

import java.io.IOException;

public class ShellProvider {

    public static ShellProvider open() {
        return new ShellProvider();
    }
    
    public ShellChannel openChannel() throws IOException {
        return ShellChannel.open(this);
    }
    
    public ShellServerChannel openServerChannel() throws IOException {
        return ShellServerChannel.open(this);
    }
    
    public ShellSelector openSelector() throws IOException {
        return ShellSelector.open();
    }
}
