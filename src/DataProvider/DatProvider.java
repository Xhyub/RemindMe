package DataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;


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
                final String nme = "projID";

                List<String> header = new ArrayList<>();
                header.add(unixID);
                header.add(dsc);
                header.add(mrk);
                header.add(dte);
                header.add(dur);
                header.add(nme);

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

    public ArrayList<ArrayList<String>> loadProjectCSVData() {


        return null;

    }

    private void loadTODOsCSVData() {


    }

    public void blockWrite(ArrayList<ArrayList<String>> line) {

    }
}
