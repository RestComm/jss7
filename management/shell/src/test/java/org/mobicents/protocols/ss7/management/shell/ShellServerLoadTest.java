package org.mobicents.protocols.ss7.management.shell;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

import org.mobicents.protocols.ss7.management.shell.ShellCmdListener;
import org.mobicents.protocols.ss7.management.shell.ShellCommand;
import org.mobicents.protocols.ss7.management.shell.server.ShellServer;

public class ShellServerLoadTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		ShellServerLoadTest loadTest = new ShellServerLoadTest();
		loadTest.execute();
	}

	private void execute() throws Exception {
		ShellServer cliServer = new ShellServer();
		cliServer.setLocalAddress("127.0.0.1");
		cliServer.setLocalPort(3435);

		cliServer.setCliCmdListener(new MTPManagement());
		cliServer.start();

		while (cliServer.isActive()) {
			cliServer.perform();
		}

		// System.out.println("Total Execution time in nano sec = "
		// + (cliServer.getTotalExecutionTime() ));
		// System.out.println("Avg Execution Time in nano sec = "
		// + cliServer.getTotalExecutionTime() / ( 10000000.0));
		// System.out.println("Max Execution Time in nanosec = "
		// + (cliServer.getMaxExecutionTime() ));
		// System.out
		// .println("Max Hanppened on = " + cliServer.getMaxhappenedOn());
		//		
		// System.out
		// .println("Ticks = " + cliServer.getTick());

		// long[] executionTimes = cliServer.getExecutionTimes();
		//
		// try {
		// FileWriter fstream = new FileWriter("out.csv");
		// BufferedWriter out = new BufferedWriter(fstream);
		// for (int i = 0; i < 1000000; i++) {
		// out.write(""+(int) executionTimes[i]);
		// out.write(",");
		// }
		// out.close();
		// } catch (Exception e) {// Catch exception if any
		// System.err.println("Error: " + e.getMessage());
		// }
	}

	class MTPManagement implements ShellCmdListener {

		private byte[] showLinkSet = "MTPManagement.showLinkSet()".getBytes();
		private byte[] addLink = "MTPManagement.addLink()".getBytes();
		private byte[] addLinkSet = "MTPManagement.addLinkSet()".getBytes();
		private byte[] adjacentPointCode = "MTPManagement.adjacentPointCode()"
				.getBytes();
		private byte[] channel = "MTPManagement.channel()".getBytes();
		private byte[] code = "MTPManagement.code()".getBytes();
		private byte[] deleteLink = "MTPManagement.deleteLink()".getBytes();
		private byte[] deleteLinkSet = "MTPManagement.deleteLinkSet()"
				.getBytes();
		private byte[] inhibit = "MTPManagement.inhibit()".getBytes();
		private byte[] localIp = "MTPManagement.localIpPort()".getBytes();

		// Methods for CLI
		public void showLinkSet(boolean stats, TextBuilder linksetName,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(showLinkSet);
		}

		public void addLink(TextBuilder arg0, TextBuilder arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(addLink);
		}

		public void addLinkSet(TextBuilder arg0, int type,ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(addLinkSet);
		}

		public void adjacentPointCode(TextBuilder arg0, int arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(adjacentPointCode);
		}

		public void channel(TextBuilder linksetName, TextBuilder arg0,
				int arg1, ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(channel);
		}

		public void code(TextBuilder linksetName, TextBuilder arg0, int arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(code);
		}

		public void deleteLink(TextBuilder linksetName, TextBuilder arg0,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(deleteLink);
		}

		public void deleteLinkSet(TextBuilder arg0, ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(deleteLinkSet);
		}

		public void inhibit(TextBuilder arg0, TextBuilder arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(inhibit);
		}

		public void localIpPort(TextBuilder arg0, TextBuilder arg1, int arg2,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void localPointCode(TextBuilder arg0, int arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void networkIndicator(TextBuilder arg0, ShellCommand arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void noshutdownLink(TextBuilder linksetName, TextBuilder arg0,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void noshutdownLinkSet(TextBuilder arg0, ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void shutdownLink(TextBuilder linkSetName, TextBuilder arg0,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void shutdownLinkSet(TextBuilder arg0, ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void span(TextBuilder linksetName, TextBuilder arg0, int arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

		public void uninhibit(TextBuilder arg0, TextBuilder arg1,
				ByteBuffer byteBuffer) {
			byteBuffer.clear();
			byteBuffer.put(localIp);
		}

	}

}
