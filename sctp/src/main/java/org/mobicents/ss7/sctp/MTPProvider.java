package org.mobicents.ss7.sctp;

public interface MTPProvider {

	public void addMtpListener(MTPListener lst);

	public void removeMtpListener(MTPListener lst);

	public void send(int si, int ssi, byte[] msg);

	public void close() throws IllegalStateException;
}
