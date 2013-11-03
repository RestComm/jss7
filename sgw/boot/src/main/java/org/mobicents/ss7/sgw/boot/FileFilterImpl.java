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
