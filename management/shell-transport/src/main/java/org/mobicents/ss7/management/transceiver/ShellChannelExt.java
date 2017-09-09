package org.mobicents.ss7.management.transceiver;

import java.io.IOException;
import java.nio.channels.spi.AbstractSelectableChannel;

import org.mobicents.ss7.management.console.SimplePrincipal;

/**
 * A selectable channel for message connecting sockets.
 *
 * @author olena radiboh
 *
 */
public class ShellChannelExt extends ShellChannel {
    private String userName = null;
    private String password = null;
    private SimplePrincipal principal = null;

    public ShellChannelExt(ChannelProvider provider, AbstractSelectableChannel channel) throws IOException {
        super(provider, channel);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public SimplePrincipal getPrincipal() {
        return principal;
    }

    public void setPrincipal(SimplePrincipal principal) {
        this.principal = principal;
    }

}