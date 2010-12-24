package org.mobicents.protocols.ss7.management.console;

public class Shell {

    private final ConsoleListener listener;
    private final Console console;

    public Shell() {
        listener = new ConsoleListenerImpl();
        console = new Console(System.in, System.out, listener);
    }

    public static void main(String args[]) {
        // TODO : Oleg what about command line arguments?
        Shell shell = new Shell();
        shell.start();

    }

    private void start() {
        console.start();
    }

    private class ConsoleListenerImpl implements ConsoleListener {

        public void commandEntered(String consoleInput) {
            System.out.println(consoleInput);
            if (consoleInput.compareTo("exit") == 0) {
                console.stop();
            }
        }

    }

}
