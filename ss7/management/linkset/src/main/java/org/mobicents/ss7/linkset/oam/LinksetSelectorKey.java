/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.ss7.linkset.oam;

import org.mobicents.protocols.stream.api.SelectorKey;
import org.mobicents.protocols.stream.api.Stream;
import org.mobicents.protocols.stream.api.StreamSelector;

/**
 * 
 * @author amit bhayani
 *
 */
public class LinksetSelectorKey implements SelectorKey {

	private boolean isValid;
	private boolean isReadable;
	private boolean isWritable;

	private LinksetStream linkSet = null;
	private LinksetSelector linkSetSelector = null;

	private Object attachment;

	public LinksetSelectorKey(LinksetStream linkSet, LinksetSelector linkSetSelector) {
		this.linkSet = linkSet;
		this.linkSetSelector = linkSetSelector;
	}

	public void attach(Object paramObject) {
		this.attachment = paramObject;
	}

	public Object attachment() {
		return this.attachment;
	}

	public void cancel() {
		linkSetSelector.unregister(linkSet);
	}

	public Stream getStream() {
		return this.linkSet;
	}

	public StreamSelector getStreamSelector() {
		return this.linkSetSelector;
	}

	public boolean isReadable() {
		return isReadable;
	}

	public boolean isValid() {
		return isValid;
	}

	public boolean isWriteable() {
		return isWritable;
	}

}
