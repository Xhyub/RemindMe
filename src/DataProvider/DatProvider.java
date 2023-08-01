package DataProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Logger;


public class DatProvider {

    public DatProvider() {
        super();
    }

    public boolean perfChkTsk(String path) {

        boolean jobsCSVChk = getResourceOne(path);
        boolean TODOsCSVChk = getResourceTwo(path);

        if ( !jobsCSVChk || !TODOsCSVChk ) {

            return false;
        }

        return true;
    }

    public boolean getResourceTwo(final String path) {

        Path p1 = Paths.get(path);
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
                                        createResourceTwo(path);
                                        break; // return true;

                    case "y":           // no GUI for this
                                        // let user provide files at the cmd line
                                        break;

                    case "e":           Logger.global.fine("Exit by user");
                                        return false;

                    default:            System.out.println("Please select an option");
                                        getResourceTwo(path);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean getResourceOne(final String path) {

        Path p1 = Paths.get(path);
        Path p2 = p1.resolve("jobs.csv");
        if ( !Files.exists(p2) ) {

            // CORE FILE MISSING
            // ask user to provide or create new
            System.out.println("Core file is missing. Would you like to supply your own? (Y/N)");
            Scanner kb = new Scanner(System.in);
            try {
                // TODO: Replace block with a nice switch statement
                String in = kb.nextLine().toLowerCase();
                switch (in) {

                    case "n":           // application to create
                                        createResourceOne(path);
                                        break; // return true;

                    case "y":           // no GUI for this
                                        // let user provide files at the cmd line
                                        break;

                    case "e":           Logger.global.fine("Exit by user");
                                        return false;

                    default:            System.out.println("Please select an option");
                                        getResourceOne(path);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void createResourceTwo(final String path) throws IOException {

        Path p1 = Paths.get(path);
        Path p2 = p1.resolve("TODOs.csv");

        File FILE_NAME = new File(p2.toString());
        boolean success = FILE_NAME.createNewFile();

        assert(success);

        Logger.global.info("The application has created TODOs.csv");

    }

    private void createResourceOne(final String path) throws IOException {

        Path p1 = Paths.get(path);
        Path p2 = p1.resolve("jobs.csv");

        File FILE_NAME = new File(p2.toString());
        boolean success = FILE_NAME.createNewFile();

        assert(success);

        // add Project headers
        // CSVHelper help = new CSVHelper();


        Logger.global.info("The application has created jobs.csv");

    }

    private void loadJobsCSVData() {


    }

    private void loadTODOsCSVData() {


    }

    private void writeLine(){

    }
}
