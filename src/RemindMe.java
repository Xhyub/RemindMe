import DataProvider.DatProvider;
import DataProvider.DatProviderFactory;
import ExceptionClasses.DataAccessException;
import ResultOBJ.DirectoryResult;
import ResultOBJ.SystemResult;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.*;

import static java.util.logging.LogManager.getLogManager;

public class RemindMe {

    private static String userID;
    private static String dateTime;
    private static String sysOS;
    private static DatProviderFactory fc1;

    /*
    * Only a basic constructor, later additions can include passing
    * system info via data.
     */
    public RemindMe() {
        super();
    }
    private static void initWithSysInfo() {
        SystemResult sysRes = new SystemResult();
        sysRes.OSChk(); // won't test this because we are only testing a few
                        // system defined parameters, have faith

        if (sysRes.Linux)
            sysOS = "Linux";

        else if (sysRes.Windows)
            sysOS = "Windows";

        else if (sysRes.NS) {
            sysOS = "Not supported";
        }

    }
    private static void factorySetup() {
        fc1 = fc1.getInstance("My Data Factory");

        // TODO: log more developer related context
        // TODO: No logging yet... reading practical Java

        Logger.global.fine("DataFactory initialized successfully on startup...");

    }
    private static void initLogHandler(LogManager global_log_manager_prototype) {

    }
    private static void setUser() {

    }
    private boolean systemDependenciesChk() {

        Path pDance = null;

        if ("Linux".equals(sysOS)) {

            // set up targets
            DirectoryResult dirRes = new DirectoryResult();
            String homeDirectory = dirRes.getHmeDir();

            Path p1 = Paths.get(homeDirectory);
            Path p2 = p1.resolve("Downloads");
            Path p3 = p2.resolve("RemindMeArchive");
            pDance = p3;

            if ( !Files.exists(p3) ) {

                Logger.global.fine("No local archive folder located on the system."); // incl as a method for this state
                createRemindMeArchive(p3.toString());
            }

        }

        else if ("Windows".equals(sysOS)) {

            // set up targets
            DirectoryResult dirRes = new DirectoryResult();
            String homeDirectory = dirRes.getHmeDir();

            Path p1 = Paths.get(homeDirectory);
            Path p2 = p1.resolve("Downloads");
            Path p3 = p2.resolve("RemindMeArchive");
            pDance = p3;

            if ( !Files.exists(p3) ) {

                Logger.global.fine("No local archive folder located on the system."); // incl as a method for this state
                createRemindMeArchive(p3.toString());
            }
        }
        else if ("NS".equals(sysOS)) {

            System.err.println("The underlying operating system is not supported.");
            System.err.println("The application will now terminate.");
            System.exit(1);

        }

        // resume with an archive folder created
        fc1.setOurUsablePath(pDance.toString());
        DatProvider datPrv1 = fc1.createDataProvider();

        boolean chk = datPrv1.perfChkTsk();

        if ( !chk ) {

            return false;
        }

        boolean sessionBegin = chk;
        class local {

            // used as a check for the sub-system
            private void beginAppSession() {

            }
        }
        new local().beginAppSession();

        return sessionBegin;
    }

    /*
        * Application dependency. Creates if not found on the current system.
     */
    private static void createRemindMeArchive(final String path) {

        final File RemindMeArchive = new File(path);
        try {

            if (RemindMeArchive.mkdir()) {

                Logger.global.info("Folder created.");
            }
            else {
                createRemindMeArchive(path);
            }
        }
        catch (SecurityException sec) {

            sec.printStackTrace();
        }
    }
    private static void displayMenuText() {
        System.out.println();
        System.out.println("Please select an option. Type 3 to exit.");
        System.out.println("1. List projects and TODOs");
        System.out.println("2. Add project");
        System.out.println("3. Add TODO");
        System.out.println("4. Exit");
    }
    private static void getCommandFromUser() {

        Scanner kb = new Scanner(System.in);
        int in = 0;

        String day = null;
        String active = null;
        String reminder = null;

        try {

            in = kb.nextInt();
            switch(in) {

                case 1:                 printTheActiveList();
                                        break;

                case 2:                 System.out.println("Adding project now.");
                                        addProject();
                                        break;

                case 3:                 System.out.println("Adding TODO now.");
                                        addTODO();
                                        break;
                case 4:                 System.out.println("Exiting the application.");
                                        System.exit(0);

                case 5:                 //
                                        System.out.println("Removing TODO");
                                        removeTODO();
                default:

            }

        }
        catch(Exception e) {

        }
    }

    private static void addProject(){

        DatProvider datPrv = fc1.createDataProvider();

        ArrayList<List<String>> listOfJobs = new ArrayList<List<String>>();
        // TODO: Feature that keeps a incomplete list upon bad read
        int lne_err = 0;
        try {

            listOfJobs = datPrv.loadProjectCSVData_Session(listOfJobs);

        } catch (DataAccessException dae) {

            dae.printStackTrace();
            return;
        }

        // Create project line item
        ProjLineItem itm = new ProjLineItem();

        ArrayList<String> lne = new ArrayList<>();
        lne = itm.createLineItem();

        listOfJobs.add(lne);

        /* just need to test inputs
        for (List<String> lin: listOfJobs
             ) {
            for (String str : lin
                 ) {
                System.out.println(str);
            }
        }
        */

        datPrv.blockWrite(listOfJobs, "jobs.csv");

        System.out.println("Project successfully added.");

    }

    private static void addTODO(){

        DatProvider datPrv = fc1.createDataProvider();

        ArrayList<List<String>> listOfTODOs = new ArrayList<List<String>>();
        int lne = 0; // technical debt
        try {
            // TODO: Needs a load to display the available projects
            listOfTODOs = datPrv.loadTODOsCSVData_Session(listOfTODOs);

        } catch (DataAccessException dae) {

            dae.printStackTrace();

            // TODO: Feature that keeps a incomplete list upon bad read
            // System.out.print("Size of List: " + listOfTODOs.size());

            return;
        }

        TODOsLineItem todo = new TODOsLineItem();

        ArrayList<String> line = new ArrayList<>();
        line = todo.createLineItem();
        listOfTODOs.add(line);

        datPrv.blockWrite(listOfTODOs, "TODOs.csv");

        System.out.println("TODO successfully added.");

    }

    private static void removeTODO()
    {
        // define the precondition for consistency...
        // touch both resource files
        // consider the case where these don't exist
        // consider the case where these are not properly formatted
        // TODO: adding the format headers is a good start
        // this is done in the prelim checks
        // consider the case where they do exist
        // E-T-L

        // dataProv =
        // perform a load
        DatProvider datPrv = fc1.createDataProvider();
        ArrayList<List<String>> listOfTODOs = new ArrayList<List<String>>();
        try {
            listOfTODOs = datPrv.loadTODOsCSVData_Session(listOfTODOs);
        } catch (DataAccessException dae) {

            dae.printStackTrace();

        }

        // which todo?
        int todInd = 0;
        for (List<String> tod : listOfTODOs)
        {
            System.out.println("[" + todInd++ + "]" + tod);
        }
        // get some based input from the user
        System.out.print("Select the TODO to remove: ");
        Scanner kb = new Scanner(System.in);
        int ind = kb.nextInt();

        // remove by index
        for (Iterator<List<String>> iter = listOfTODOs.listIterator(); iter.hasNext();)
        {
            List<String> line = iter.next();
            // TODO: Feature that sets inactive/active
            if (Integer.toString(ind).equals(line.get(1).trim())
            ){
                iter.remove();
                System.out.println();
            }
            // TODO: Feature that orders the TODOs by date
        }

        for (List<String> tod : listOfTODOs)
        {
            System.out.println(tod);
        }
        // write back keeping all things consistent
    }

    private static void printTheActiveList() {

        DatProvider datPrv = fc1.createDataProvider();

        try {

            datPrv.loadCSVDataForListDisp();
        } catch (DataAccessException dae) {

        }

    }

    public static void main(String[] args){

        // start remind me loop
        RemindMe ker = new RemindMe();

        LogManager obj = getLogManager();
        ker.factorySetup();
        ker.initLogHandler(obj);
        ker.initWithSysInfo();

        boolean progStGo;
        progStGo = ker.systemDependenciesChk();

        if (progStGo) {
            while (1 > 0) {
                ker.displayMenuText();
                ker.getCommandFromUser();
            }
        }
        else {

            System.out.println("Application will now terminate.");
            Logger.global.fine("Application dependencies were not resolved.");
        }
    }
}
