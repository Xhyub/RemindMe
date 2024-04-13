package DataProvider;

import ExceptionClasses.DataAccessException;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import static DataProvider.CSVHelper.parseCSVLine;

public class DatProvider {

    private String pathToArchive;

    public DatProvider(final String path) {
        super();
        this.pathToArchive = path;
        System.out.println("Instantiated a DatProvider: " + pathToArchive);
    }

    public boolean perfChkTsk() {

        boolean jobsCSVChk = getResourceOne();
        boolean TODOsCSVChk = getResourceTwo();

        if ( !jobsCSVChk || !TODOsCSVChk ) {

            return false;
        }

        return true;
    }

    public boolean getResourceTwo() {

        Path p1 = Paths.get(pathToArchive);
        Path p2 = p1.resolve("TODOs.csv");
        if ( !Files.exists(p2) ) {

            // CORE FILE MISSING
            // ask user to provide or create new
            System.out.println("Core file is missing. Would you like to supply your own? (Y/N)");
            Scanner kb = new Scanner(System.in);
            try {

                String in = kb.nextLine().toLowerCase();
                switch (in) {

                    case "n":           // application to create
                                        if ( !createResourceTwo() ) {

                                            getResourceTwo();
                                        }
                                        else {
                                            break;
                                        }

                    case "y":           // no GUI for this
                                        // let user provide files at the cmd line
                                        break;

                    case "e":           Logger.global.fine("Exit by user");
                                        return false;

                    default:            System.out.println("Please select an option");
                                        getResourceTwo();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean getResourceOne() {

        Path p1 = Paths.get(pathToArchive);
        Path p2 = p1.resolve("jobs.csv");
        if ( !Files.exists(p2) ) {

            // CORE FILE MISSING
            // ask user to provide or create new
            System.out.println("Core file is missing. Would you like to supply your own? (Y/N)");
            Scanner kb = new Scanner(System.in);
            try {
                String in = kb.nextLine().toLowerCase();
                switch (in) {

                    case "n":           // application to create
                                        if ( !createResourceOne() ) {

                                            getResourceOne();
                                        }
                                        else {
                                            break;
                                        }

                    case "y":           // no GUI for this
                                        // let user provide files at the cmd line
                                        break;

                    case "e":           Logger.global.fine("Exit by user");
                                        return false;

                    default:            System.out.println("Please select an option");
                                        getResourceOne();
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    private boolean createResourceTwo() throws IOException {

        // (1) change path string
        // (2) change header strings
        // (3) change the last printed line

        Path p1 = Paths.get(pathToArchive);
        Path p2 = p1.resolve("TODOs.csv");

        File FILE_NAME = new File(p2.toString());
        boolean success = FILE_NAME.createNewFile();

        assert(success);
        class local {

            // if the returned class is Red in your IDE it's a problem and
            // by fixing it will tell you why
            private boolean generateCSVHeader(CSVHelper help) {

                final String unixID = "unixID";
                final String dsc = "desc";
                final String mrk = "marked"; // TODO: feature allows todos to be parsed as active/inactive
                final String dte = "dateLogged";
                final String dur = "duration"; // TODO: feature allows duration to be calculated from the date logged
                final String pid = "projID";

                List<String> header = new ArrayList<>();
                header.add(unixID);
                header.add(dsc);
                header.add(mrk);
                header.add(dte);
                header.add(dur);
                header.add(pid);

                try {

                    help.writeLine(header);
                    Logger.global.fine("The application has written headers.");

                }
                catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

                return true;
            }
        }

        PrintWriter writer;
        boolean res = false;
        try {

            writer = new PrintWriter(new FileOutputStream(FILE_NAME, true));
            CSVHelper help = new CSVHelper(writer);
            // add Project headers
            res = new local().generateCSVHeader(help);

        } catch (Exception e) {
            Logger.global.info("An underlying output stream could not be opened. Please check the resource file to see if headers have been updated.");
            e.printStackTrace();
        }

        if ( !res ) {
            // bad write need to call again
            System.err.println("There was an error writing to this resource. Please try again.");

            // reset directory conditions
            FILE_NAME.delete();
            return false;
        }
        else {
            System.out.println("The application has created TODOs.csv.");
        }

        return true;

    }

    private boolean createResourceOne() throws IOException {

        Path p1 = Paths.get(pathToArchive);
        Path p2 = p1.resolve("jobs.csv");

        File FILE_NAME = new File(p2.toString());
        boolean success = FILE_NAME.createNewFile();

        assert(success);
        class local {

            // if the returned class is Red in your IDE it's a problem and
            // by fixing it will tell you why
            private boolean generateCSVHeader(CSVHelper help) {

                final String unixID = "unixID";
                final String nme = "projName";
                final String dsc = "projDesc";
                final String mrk = "marked"; // active/inactive
                final String opnTODOs = "openTODOs"; // TODO: feature allows open todos to be counted

                List<String> header = new ArrayList<>();
                header.add(unixID);
                header.add(nme);
                header.add(dsc);
                header.add(mrk);
                header.add(opnTODOs);

                try {

                    help.writeLine(header);
                    Logger.global.fine("The application has written headers.");

                }
                catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

                return true;
            }
        }

        PrintWriter writer;
        boolean res = false;
        try {

            writer = new PrintWriter(new FileOutputStream(FILE_NAME, true));
            CSVHelper help = new CSVHelper(writer);
            // add Project headers
            res = new local().generateCSVHeader(help);

        } catch (Exception e) {
            Logger.global.info("An underlying output stream could not be opened. Please check the resource file to see if headers have been updated.");
            e.printStackTrace();
        }

        if ( !res ) {
            // bad write need to call again
            System.err.println("There was an error writing to this resource. Please try again.");

            // reset directory conditions
            FILE_NAME.delete();
            return false;
        }
        else {

            System.out.println("The application has created jobs.csv.");
        }

        return true;

    }

    public ArrayList<List<String>> loadProjectCSVData_Session(ArrayList<List<String>> storage) throws DataAccessException {

        ArrayList<List<String>> csvLines = storage;

        Path p1 = Paths.get(pathToArchive);
        Path p2 = p1.resolve("jobs.csv");

        File f = new File(p2.toString());
        if ( f.exists() ) {

            BufferedReader r;
            try {
                // try simple IO open stream
                r = new BufferedReader(new FileReader(f));

            } catch (Exception e) {
                throw new DataAccessException("Failed to open an input stream. Please retry.");
            }
            String lne;
            CSVHelper loader = new CSVHelper();
            try {
                // don't add the header
                while ((lne = r.readLine()) != null) {
                    List<String> fields = loader.parseCSVLine(lne);
                    csvLines.add(fields);
                }
                r.close();

            } catch (Exception e) {
                e.printStackTrace();

                throw new DataAccessException("The application failed to read from a resource. " +
                        "Refer to the line number.");
            }
        }
        else {
            // throw this so user can call again
            throw new DataAccessException("The application was unable to read a resource. You can retry.");
        }
        return csvLines;
    }

    public ArrayList<List<String>> loadTODOsCSVData_Session(ArrayList<List<String>> storage) throws DataAccessException {

        // create a local copy of the parameters passed
        ArrayList<List<String>> csvLines = storage;

        Path p1 = Paths.get(pathToArchive);
        Path p2 = p1.resolve("TODOs.csv");

        File f = new File(p2.toString());
        if ( f.exists() ) {

            BufferedReader r;
            try {
                r = new BufferedReader(new FileReader(f));

            } catch (Exception e) {
                throw new DataAccessException("Failed to open an input stream. Please retry.");
            }
            String lne;
            CSVHelper loader = new CSVHelper();
            try {

                // don't add the header
                while ((lne = r.readLine()) != null ) {
                    List<String> fields = loader.parseCSVLine(lne);
                    csvLines.add(fields);
                }
                r.close();

            } catch (Exception e) {
                e.printStackTrace();

                throw new DataAccessException("The application failed to read from a resource. " +
                        "Refer to the line number.");
            }
        }
        else {
            // terminating upon unsuccessful read of file
            throw new DataAccessException("The application was unable to read a resource. You can retry.");
        }

        return csvLines;
    }

    public void loadCSVDataForListDisp() throws DataAccessException {

        ArrayList<List<String>> listOfJobs = new ArrayList<List<String>>();
        ArrayList<List<String>> listOfTODOs = new ArrayList<List<String>>();

        // TODO: put me into TRY/CATCH after the predecessor is sorted
        listOfJobs = loadProjectCSVData_Session(listOfJobs);
        listOfTODOs = loadTODOsCSVData_Session(listOfTODOs);

        // for every project line item
        int jobIndex = 0;
        for (List<String> proLne :
             listOfJobs) {

            // TODO: Add formatting features
            System.out.println("[" + jobIndex + "] " + proLne);

            // print it's TODOs
            for (Iterator<List<String>> iter=listOfTODOs.listIterator();iter.hasNext();)
            {
                List<String> line = iter.next();
                // test condition for the matching job index
                // TODO: Feature that sets inactive/active
                if (Integer.toString(jobIndex).equals(line.get(1).trim())  // ||
                        // "INACTIVE".equals(line.get(4).trim())) {
                ){

                    String fmt_lne;
                    fmt_lne = "    " + line.get(2).trim() + " ~ " + line.get(3).trim() + " ~ " + line.get(4).trim();
                    System.out.println(fmt_lne);
                }
                // TODO: Feature that orders the TODOs by date
            }

            jobIndex++;

        }
    }

    public void blockWrite(ArrayList<List<String>> list, final String resource) {

        if ("jobs.csv".equals(resource)) {

            Path p1 = Paths.get(pathToArchive);
            Path p2 = p1.resolve(resource);
            File FILE_NAME = new File(p2.toString());
            if (FILE_NAME.exists()) {

                PrintWriter writer;
                try {

                    writer = new PrintWriter(new FileOutputStream(FILE_NAME, false));
                    CSVHelper help = new CSVHelper(writer);

                    for (List<String> line :
                            list) {

                        try {

                            help.writeLine(line);

                        }
                        catch (Exception e){
                            // e.printStackTrace();
                            blockWrite(list, resource);
                        }
                    }
                } catch (Exception e) {
                    Logger.global.info("An underlying output stream could not be opened. Please check the resource file to see if headers have been updated.");
                    e.printStackTrace();
                }
                Logger.global.info("Write performed jobs.csv.");
            }
        }
        else if ("TODOs.csv".equals(resource)) {

            Path p1 = Paths.get(pathToArchive);
            Path p2 = p1.resolve(resource);
            File FILE_NAME = new File(p2.toString());
            if (FILE_NAME.exists()) {

                PrintWriter writer;
                try {

                    writer = new PrintWriter(new FileOutputStream(FILE_NAME, false));
                    CSVHelper help = new CSVHelper(writer);

                    for (List<String> line :
                            list) {

                        // write here
                        // what if errors occur?
                        try {

                            help.writeLine(line);
                        }
                        catch (Exception e){
                            // e.printStackTrace();
                            blockWrite(list, resource);
                        }
                    }
                } catch (Exception e) {
                    Logger.global.info("An underlying output stream could not be opened. " +
                            "Please check the resource file to see if headers have been updated.");
                    e.printStackTrace();
                }
                Logger.global.info("Write performed TODOs.csv.");
            }
        }
    }

    // this is a feature and could be added upstream?
    public void checkResourceEmpty() {

    }
}
