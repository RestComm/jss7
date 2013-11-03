/*
 * TeleStax, Open Source Cloud Communications
 * Copyright 2011-2013, Telestax Inc and individual contributors
 * by the @authors tag.
 *
 * This program is free software: you can redistribute it and/or modify
 * under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package org.mobicents.ss7.management.console;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ServiceLoader;

import org.jboss.jreadline.complete.CompleteOperation;
import org.jboss.jreadline.complete.Completion;
import org.jboss.jreadline.console.Config;
import org.jboss.jreadline.console.settings.Settings;

/**
 *
 *
 * @author amit bhayani
 *
 */
public class ConsoleImpl implements Console {

    private final org.jboss.jreadline.console.Console console;

    private final CommandContext cmdCtx;

    private ServiceLoader<CommandHandler> commandHandlerLoader = ServiceLoader.load(CommandHandler.class);

    protected static List<CommandHandler> commandHandlerList = new ArrayList<CommandHandler>();

    private final CommandHistory history = new HistoryImpl();

    /**
     * @throws IOException
     *
     */
    public ConsoleImpl(CommandContext cmdCtx) throws IOException {
        super();
        this.console = new org.jboss.jreadline.console.Console();
        this.cmdCtx = cmdCtx;

        for (CommandHandler commandHandler : commandHandlerLoader) {
            commandHandlerList.add(commandHandler);
            List<CommandLineCompleter> commandLineCompleterList = commandHandler.getCommandLineCompleterList();
            for (CommandLineCompleter commandLineCompleter : commandLineCompleterList) {
                this.addCompleter(commandLineCompleter);
            }
        }

        // Add Histor
        HistoryHandler historyHandler = new HistoryHandler();
        commandHandlerList.add(historyHandler);
        for (CommandLineCompleter commandLineCompleter : historyHandler.getCommandLineCompleterList()) {
            this.addCompleter(commandLineCompleter);
        }

        // Add SS7 Connect
        ConnectHandler connectHandler = new ConnectHandler();
        commandHandlerList.add(connectHandler);
        for (CommandLineCompleter commandLineCompleter : connectHandler.getCommandLineCompleterList()) {
            this.addCompleter(commandLineCompleter);
        }

        // Add SS7 DisConnect
        DisconnectHandler disconnectHandler = new DisconnectHandler();
        commandHandlerList.add(disconnectHandler);
        for (CommandLineCompleter commandLineCompleter : disconnectHandler.getCommandLineCompleterList()) {
            this.addCompleter(commandLineCompleter);
        }

        // Add Exit handler
        ExitHandler exitHandler = new ExitHandler();
        commandHandlerList.add(exitHandler);
        for (CommandLineCompleter commandLineCompleter : exitHandler.getCommandLineCompleterList()) {
            this.addCompleter(commandLineCompleter);
        }
    }

    public void stop() {
        try {
            this.console.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#addCompleter(org.mobicents
     * .ss7.management.console.CommandLineCompleter)
     */
    @Override
    public void addCompleter(final CommandLineCompleter completer) {
        console.addCompletion(new Completion() {
            @Override
            public void complete(CompleteOperation co) {
                int offset = completer.complete(cmdCtx, co.getBuffer(), co.getCursor(), co.getCompletionCandidates());

                // TODO : Not sure as of now to set offset
                // co.setOffset(offset);
            }
        });
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#isUseHistory()
     */
    @Override
    public boolean isUseHistory() {
        return !Settings.getInstance().isHistoryDisabled();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#setUseHistory(boolean)
     */
    @Override
    public void setUseHistory(boolean useHistory) {
        Settings.getInstance().setHistoryDisabled(!useHistory);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#getHistory()
     */
    @Override
    public CommandHistory getHistory() {
        return history;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#setHistoryFile(java.io.File)
     */
    @Override
    public void setHistoryFile(File f) {
        Settings.getInstance().setHistoryFile(f);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#clearScreen()
     */
    @Override
    public void clearScreen() {
        try {
            console.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#printColumns(java.util. Collection)
     */
    @Override
    public void printColumns(Collection<String> list) {
        String[] newList = new String[list.size()];
        list.toArray(newList);
        try {
            console.pushToConsole(org.jboss.jreadline.util.Parser.formatCompletions(newList, console.getTerminalHeight(),
                    console.getTerminalWidth()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#print(java.lang.String)
     */
    @Override
    public void print(String line) {
        try {
            console.pushToConsole(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#printNewLine()
     */
    @Override
    public void printNewLine() {
        try {
            console.pushToConsole(Config.getLineSeparator());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#readLine(java.lang.String)
     */
    @Override
    public String readLine(String prompt) {
        try {
            return console.read(prompt);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.mobicents.ss7.management.console.Console#readLine(java.lang.String, java.lang.Character)
     */
    @Override
    public String readLine(String prompt, Character mask) {
        try {
            return console.read(prompt, mask);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    class HistoryImpl implements CommandHistory {

        @SuppressWarnings("unchecked")
        @Override
        public List<String> asList() {
            return console.getHistory().getAll();
        }

        @Override
        public boolean isUseHistory() {
            return !Settings.getInstance().isHistoryDisabled();
        }

        @Override
        public void setUseHistory(boolean useHistory) {
            Settings.getInstance().setHistoryDisabled(!useHistory);
        }

        @Override
        public void clear() {
            console.getHistory().clear();
        }

        @Override
        public void setMaxSize(int maxSize) {
            Settings.getInstance().setHistorySize(maxSize);
        }

        @Override
        public int getMaxSize() {
            return Settings.getInstance().getHistorySize();
        }
    }
}
