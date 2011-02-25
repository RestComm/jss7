/*
 * JBoss, Home of Professional Open Source
 * Copyright XXXX, Red Hat Middleware LLC, and individual contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a full listing
 * of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU General Public License, v. 2.0.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License,
 * v. 2.0 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 */
package org.mobicents.ss7.sgw.boot;

import java.io.File;
import java.io.FileFilter;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 
 * @author amit bhayani
 * 
 */
public class FileFilterImpl implements FileFilter {

	/** The file suffixes */
	private static Set<String> fileSuffixes = new CopyOnWriteArraySet<String>();

	static {
		fileSuffixes.add("-beans.xml");
		fileSuffixes.add("-conf.xml");
	}

	public FileFilterImpl() {
	}

	public FileFilterImpl(Set<String> suffixes) {
		if (suffixes == null)
			throw new IllegalArgumentException("Null suffixes");
		fileSuffixes.clear();
		fileSuffixes.addAll(suffixes);
	}

	public boolean accept(File pathname) {
		for (String suffix : fileSuffixes) {
			String filePathName = pathname.getName(); 
			if (filePathName.endsWith(suffix))
				return true;
		}
		return false;
	}

	public Set<String> getSuffixes() {
		return fileSuffixes;
	}

	public static boolean addFileSuffix(String suffix) {
		if (suffix == null)
			throw new IllegalArgumentException("Null suffix");
		return fileSuffixes.add(suffix);
	}

	public static boolean removeFileSuffix(String suffix) {
		if (suffix == null)
			throw new IllegalArgumentException("Null suffix");
		return fileSuffixes.remove(suffix);
	}
}
