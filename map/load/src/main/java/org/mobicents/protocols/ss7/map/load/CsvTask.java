package org.mobicents.protocols.ss7.map.load;

import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CsvTask implements Runnable, Closeable {

    private static final String CSV_SEPARATOR = ";";

    private PrintWriter writer;
    private AtomicBoolean headerPrinted = new AtomicBoolean();
    private CsvWriter csvWrited;

    public CsvTask(CsvWriter csvWrited) throws FileNotFoundException, UnsupportedEncodingException {
        this.csvWrited = csvWrited;
        String csvFilePath = csvWrited.name() + "-" + System.currentTimeMillis() + ".csv";
        this.writer = new PrintWriter(csvFilePath, "UTF-8");
    }

    @Override
    public void run() {
        List<Counter> counters = csvWrited.getCounters();
        List<String> columns = new ArrayList<>(counters.size());
        if (headerPrinted.compareAndSet(false, true)) {
            printHeader();
        }
        for (Counter counter : counters) {
           columns.add("" + counter.get());
        }
        printRow(columns);
    }

    private void printHeader() {

        List<Counter> counters = csvWrited.getCounters();
        List<String> columnNames = new ArrayList<>(counters.size());
        for(Counter counter : counters) {
            columnNames.add(counter.getName());
        }
        printRow(columnNames);
    }

    private void printRow(List<?> columns) {
        StringBuilder csvLine = new StringBuilder();
        for (Object cAux : columns) {
            csvLine.append(cAux);
            csvLine.append(CSV_SEPARATOR);
        }
        writer.println(csvLine.toString());
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        if (writer != null)
            writer.close();
    }

}
