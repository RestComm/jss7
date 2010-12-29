package org.mobicents.ss7.management.console;

/**
 * Listener interface for receiving {@link String} data from {@link Console}.
 * The class that is interested in processing command implements this interface,
 * and the {@link Console} object is registered with instance of this class,
 * using the setConsole method.
 * 
 * @author amit bhayani
 * 
 */
public interface ConsoleListener {

    /**
     * Invoked when {@link Console} reads next line of data
     * 
     * @param consoleInput
     */
    public void commandEntered(String consoleInput);

    /**
     * Set the {@link Console} for this listnere
     * 
     * @param console
     */
    public void setConsole(Console console);

}
