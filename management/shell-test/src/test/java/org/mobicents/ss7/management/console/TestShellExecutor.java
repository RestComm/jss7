package org.mobicents.ss7.management.console;

public class TestShellExecutor implements ShellExecutor {

    public static final String INVALID_COMMAND = "Invalid Command";
    public static final String SLEEP_20 = "Successfully executed command";
    public static final String SLEEP_40 = "No response from server";

    @Override
    public boolean handles(String command) {
        return "test".equals(command);
    }

    @Override
    public String execute(String[] options) {
        if (options[0].equals("test")) {
            return this.executeTest(options);
        }
        return INVALID_COMMAND;
    }

    private String executeTest(String[] args) {
        try {
            if (args.length != 2) {
                return INVALID_COMMAND;
            }

            if (args[1] == null) {
                return INVALID_COMMAND;
            }

            String rasCmd = args[1];

            if (rasCmd.equals("sleep20")) {
                return this.sleep20(args);
            } else if (rasCmd.equals("sleep40")) {
                return this.sleep40(args);
            } else if (rasCmd.equals("nosleep")) {
                return this.nosleep(args);
            }
            return INVALID_COMMAND;
        } catch (Exception e) {
            // logger.error(String.format("Error while executing comand %s", Arrays.toString(args)), e);
            return e.getMessage();
        }
    }

    private String nosleep(String[] args) {
        
        return String.format(SLEEP_20);
    }

    private String sleep40(String[] args) {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ie) {
            return "Interrupted";
        }
        return String.format(SLEEP_40);
    }

    private String sleep20(String[] args) throws Exception {        
        try {
            Thread.sleep(20000);
        } catch (InterruptedException ie) {
        }
        return String.format(SLEEP_20);
    }

}
