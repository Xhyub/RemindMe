package DataProvider;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

/*
*       This implementation does not permit "" found within fields
*
*
 */
public class CSVHelper
{
    private PrintWriter w;
    private PrintWriter r;

    public CSVHelper() {
        super();
    }
    public CSVHelper(PrintWriter w) {
        // declare a writer instance with output stream intact
        this.w = w;
    }
    public void writeLine(List<String> values)
            throws Exception
    {
        boolean firstVal = true;

        for (String val : values)  {

            if (!firstVal) {
                w.write(",");
            }
            w.write("\"");
            for (int i=0; i<val.length(); i++) {
                char ch = val.charAt(i);
                if (ch=='\"') {
                    w.write("\"");  //writes an extra quote
                }
                w.write(ch);
                if (w.checkError()) {
                    Logger.global.info("Error occurred during write.");
                }
            }
            w.write("\"");
            firstVal = false;

        }
        w.write("\r"); // TODO: may need to check if this is writing properly as per RFC
    }
    /**
     * Returns a null when the input stream is empty
     */
    public static List<String> parseLine(Reader r) throws Exception {
        int ch = r.read();
        while (ch == '\r') {
            ch = r.read();
        }
        if (ch<0) {
            return null;
        }
        Vector<String> store = new Vector<String>();
        StringBuffer curVal = new StringBuffer();
        boolean inquotes = false;
        boolean started = false;
        while (ch>=0) {
            if (inquotes) {
                started=true;
                if (ch == '\"') {
                    inquotes = false;
                }
                else {
                    curVal.append((char)ch);
                }
            }
            else {
                if (ch == '\"') {
                    inquotes = true;
                    if (started) {
                        // if this is the second quote in a value, add a quote
                        // this is for the double quote in the middle of a value
                        curVal.append('\"');
                    }
                }
                else if (ch == ',') {
                    store.add(curVal.toString());
                    curVal = new StringBuffer();
                    started = false;
                }
                else if (ch == '\r') {
                    //ignore LF characters
                }
                else if (ch == '\n') {
                    //end of a line, break out
                    break;
                }
                else {
                    curVal.append((char)ch);
                }
            }
            ch = r.read();
        }
        store.add(curVal.toString());
        return store;
    }

    public static List<String> parseCSVLine(String line) {
        List<String> fieldsList = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean insideQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if (c == ',' && !insideQuotes) {
                fieldsList.add(currentField.toString());
                currentField.setLength(0); // Clear the current field
            } else {
                currentField.append(c);
            }
        }

        // Add the last field
        fieldsList.add(currentField.toString());

        return fieldsList;
    }
}