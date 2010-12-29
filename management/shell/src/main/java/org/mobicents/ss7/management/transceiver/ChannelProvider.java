package org.mobicents.ss7.management.transceiver;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;

/**
 * <p>
 * Service-provider class for {@link ChannelSelector} and selectable
 * {@link ShellSelectableChannel}
 * </p>
 * <p>
 * A given invocation of the Java virtual machine maintains a single system-wide
 * default provider instance, which is returned by the {@link #provider()
 * provider} method. The first invocation of that method will locate the default
 * provider as specified below.
 * </p>
 * <p>
 * The system-wide default provider is used by the static <tt>open</tt>
 * methods of the {@link ShellChannel#open ShellChannel},
 * {@link ShellServerChannel#open ShellServerChannel}, and {@link
 * ChannelSelector#open ChannelSelector} classes.
 * </p>
 * 
 * <p>
 * A program may make use of system-wide default provider to get instance of
 * {@link MessageFactory} by calling <tt>getMessageFactory</tt>
 * </p>
 * 
 * @author amit bhayani
 * 
 */
public class ChannelProvider {

    private MessageFactory messageFactory = new MessageFactory();
    private static ChannelProvider channelProvider = new ChannelProvider();

    protected ChannelProvider() {

    }

    /**
     * Returns the system-wide default selector provider for this invocation of
     * the Java virtual machine.
     * 
     * @return Returns the system-wide default selector provider.
     */
    public static ChannelProvider provider() {
        return channelProvider;
    }

    /**
     * Opens a shell channel
     * 
     * @return The new channel
     * @throws IOException
     */
    public ShellChannel openChannel() throws IOException {
        return new ShellChannel(this, SocketChannel.open());
    }

    /**
     * Opens a server channel
     * 
     * @return The new channel
     * @throws IOException
     */
    public ShellServerChannel openServerChannel() throws IOException {
        return new ShellServerChannel(this, ServerSocketChannel.open());
    }

    /**
     * Opens a channel selector
     * 
     * @return New selector
     * @throws IOException
     */
    public ChannelSelector openSelector() throws IOException {
        return new ChannelSelector(SelectorProvider.provider().openSelector());
    }

    /**
     * Returns the system-wide default message factory for this invocation of
     * the Java virtual machine.
     * 
     * @return Returns the system-wide default message factory
     */
    public MessageFactory getMessageFactory() {
        return messageFactory;
    }
}
