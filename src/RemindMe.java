import DataProvider.DatProvider;
import DataProvider.DatProviderFactory;
import ExceptionClasses.DataAccessException;
import ResultOBJ.DirectoryResult;
import ResultOBJ.SystemResult;

import java.io.File;
import java.lang.reflect.Array;
import java.nio.file.*;
import java.util.ArrayList;
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
        // TODO: can't access this yet... reading practical Java

        Logger.global.fine("Initial DataFactory initialized successfully on startup...");

    }
    private static void initLogHandler(LogManager global_log_manager_prototype) {

        /* TODO: define a logs config mabye log.properties and Java Preferences API

        Logger myLogger = Logger.getLogger("RemindMe");

        Logger.global.info("No logs yet...");

         */
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

                // do nothing?
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

                // do nothing?
                Logger.global.fine("No local archive folder located on the system."); // incl as a method for this state
                createRemindMeArchive(p3.toString());
            }
        }
        else if ("NS".equals(sysOS)) {

            // TODO: LOG THIS
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

                case 3:                 System.out.println("Adding TOOD now.");
                                        addTODO();
                case 4:

                default:

            }

        }
        catch(Exception e) {

        }
    }

    private static void addProject(){

        DatProvider datPrv = fc1.createDataProvider();


        ArrayList<List<String>> listOfJobs = new ArrayList<List<String>>();
        int line = 0;
        try {

            listOfJobs = datPrv.loadProjectCSVData_Session(listOfJobs, line);

        } catch (DataAccessException dae) {

            dae.printStackTrace();

            // using this print statement to test the list
            System.out.print("Size of List: " + listOfJobs.size());

            // TODO: currently doesn't do much with knowing that there is a
            // incomplete list
            return;
        }

        // Create project line item
        ProjLineItem itm = new ProjLineItem();

        ArrayList<String> lne = new ArrayList<>();
        lne = itm.createLineItem();

        listOfJobs.add(lne);

        // just need to test inputs
        for (List<String> lin: listOfJobs
             ) {
            for (String str : lin
                 ) {
                System.out.println(str);
            }
        }

        // TODO: havent done the write yet
        // not going to be formatted and ready for block write
        // the application should write at once incase any interruption

        // TODO: just have to make sure DataProvider has a usable path always
        // datPrv.blockWrite(listOfJobs);

        // TODO: not reaching here...
        System.out.println("Project successfully added.");

    }

    private static void addTODO(){

        DatProvider datPrv = fc1.createDataProvider();
        ArrayList<List<String>> listOfTODOs = new ArrayList<List<String>>();

        int lne = 0;
        try {

            listOfTODOs = datPrv.loadTODOsCSVData_Session(listOfTODOs, lne);

        } catch (DataAccessException dae) {

            dae.printStackTrace();
            // using this print statement to test the list
            System.out.print("Size of List: " + listOfTODOs.size());

            // TODO: currently doesn't do much with knowing that there is a
            // incomplete list
            return;
        }

        // TODO: missing the write

        // just need to test inputs
        for (List<String> lin: listOfTODOs
        ) {
            for (String str : lin
            ) {
                System.out.print(str);
            }
            System.out.println();
        }
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

        boolean sysInitGo;
        sysInitGo = ker.systemDependenciesChk();

        if (sysInitGo) {
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
