package org.mobicents.ss7.management.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author amit bhayani
 * 
 */
public abstract class CommandHandlerWithHelp implements CommandHandler {

	protected void printHelp(String filename, CommandContext ctx) {
		InputStream helpInput = SecurityActions.getClassLoader(CommandHandlerWithHelp.class).getResourceAsStream(filename);
		if (helpInput != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(helpInput));
			try {
				String helpLine = reader.readLine();
				while (helpLine != null) {
					ctx.printLine(helpLine);
					helpLine = reader.readLine();
				}
			} catch (java.io.IOException e) {
				ctx.printLine("Failed to read help/help.txt: " + e.getLocalizedMessage());
			} finally {
				try {
					reader.close();
				} catch (IOException e) {

				}
			}
		} else {
			ctx.printLine("Failed to locate command description " + filename);
		}
	}
}
