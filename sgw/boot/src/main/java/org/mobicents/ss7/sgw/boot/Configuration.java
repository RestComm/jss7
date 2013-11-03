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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 *
 * @author kulikov
 */
public class Configuration {

    private static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n "
            + "<deployment xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
            + "xsi:schemaLocation=\"urn:jboss:bean-deployer:2.0 bean-deployer_2_0.xsd\" "
            + "xmlns=\"urn:jboss:bean-deployer:2.0\">\n";

    private static final String FOOTER = "</deployment>";

    private File file;
    private File directory;

    private boolean isDirectory;
    private long lastModified;

    private Logger logger = Logger.getLogger(Configuration.class);

    public Configuration(String s, File directory) {
        this.directory = directory;
    }

    public String getBeans() throws IOException {
        String config = "";
        File[] files = directory.listFiles();
        for (File file : files) {
            if (!file.isDirectory()) {
                logger.info("Configuring " + file);
                String description = read(file);

                int p1 = description.indexOf("<xml");
                int p2 = description.indexOf('>', p1);

                description = description.substring(p2 + 1);

                p1 = description.indexOf("<deployment");
                p2 = description.indexOf('>', p1);

                description = description.substring(p2 + 1);

                description = description.replaceFirst("</deployment>", "");
                config += "\n" + description;
            } else {
                logger.info("Reading directory " + file);
                Configuration conf = new Configuration("", file);
                config += conf.getBeans();
            }
        }
        return config;
    }

    public URL getConfig() throws IOException {
        String s = HEADER + getBeans() + FOOTER;

        // creating temp dir on one level top
        File tempDir = new File(directory.getParent() + File.separator + "temp");
        tempDir.mkdir();

        // creating aggegated deployement descriptor
        String temp = tempDir.getPath() + File.separator + "deployment-beans.xml";

        // write descriptor
        FileOutputStream fout = new FileOutputStream(temp, false);
        fout.write(s.getBytes());
        fout.flush();
        fout.close();

        File deployment = new File(temp);
        return deployment.toURI().toURL();
    }

    private String read(File file) throws IOException {
        // create local buffer
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        // opening file
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
        } catch (FileNotFoundException e) {
        }

        // fetching data from file to local buffer
        try {
            int b = -1;
            while ((b = fin.read()) != -1) {
                bout.write(b);
            }
        } finally {
            fin.close();
        }

        // creating string
        return new String(bout.toByteArray());
    }

    public Configuration(File file) {
        this.file = file;
        this.isDirectory = file.isDirectory();
        this.lastModified = file.lastModified();
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public URL getURL() {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public long lastModified() {
        return lastModified;
    }

    public void update(long lastModified) {
        this.lastModified = lastModified;
    }
}