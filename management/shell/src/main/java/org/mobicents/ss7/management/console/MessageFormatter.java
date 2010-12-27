package org.mobicents.ss7.management.console;

import javolution.text.TextBuilder;

import org.mobicents.ss7.management.shell.Command;

public class MessageFormatter {

    protected void displayCommand(TextBuilder tx, int charsPerLine,
            Command cmd, int leftPad, int descPad) {
        this.createPad(tx, leftPad);
        tx.append(cmd.toString());
        this.createPad(tx, descPad);

    }

    private void createPad(TextBuilder tx, int pad) {
        for (int i = 0; i < pad; i++) {
            tx.append(' ');
        }
    }
}
