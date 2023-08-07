import java.util.*;

public class ProjLineItem {

    private final long unixID = System.currentTimeMillis() / 1000L;
    private String nme = null;
    private String dsc = null;
    private String mrk = null;
    private String opnTODOs = null; // TODO: this is another feature

    // TODO: private String jobStart = null;

    public ProjLineItem() {
        super();
    }

    public ArrayList<String> createLineItem() {

        ArrayList<String> lineItem = new ArrayList<>();

        Scanner kb = new Scanner(System.in);
        setJobName(kb);
        setJobDesc(kb);
        setJobMrk(kb, false);
        // writeToFile();
        // TODO: robust me
        System.out.println("Job entry has been recorded...");

        // no parser yet
        // number of commas = total entries - 1
        lineItem.add(Long.toString(unixID));
        lineItem.add(nme);
        lineItem.add(dsc);
        lineItem.add(mrk);

        return lineItem;
    }

    private void setJobName( Scanner kb ) {
        System.out.print("Please enter the job name: ");
        nme = kb.nextLine().trim();
    }

    private void setJobDesc( Scanner kb ) {
        System.out.print("Please enter the job description: ");
        dsc = kb.nextLine().trim();
    }

    private void setJobMrk( Scanner kb, boolean flag ) {
        if ( !flag ) {

            System.out.print("Please mark the job status (A/I): ");
            String s1 = kb.nextLine().toLowerCase().trim();
            switch (s1) {

                case "a":       mrk = "ACTIVE"; break;
                case "i":       mrk = "INACTIVE"; break;
                default:        System.err.print("Invalid input, please try again: ");
                    boolean flg1 = true;
                    setJobMrk( kb , flg1);
            }
        }
        else {

            String s1 = kb.nextLine().toLowerCase().trim();
            switch (s1) {

                case "a":       mrk = "ACTIVE"; break;
                case "i":       mrk = "INACTIVE"; break;
                default:        System.err.print("Invalid input, please try again: ");
                    boolean flg1 = true;
                    setJobMrk( kb , flg1);
            }
        }
    }

    // TODO: Write setJobStart() and all other places to call FIRST
    private void setJobStart( Scanner kb ) {

    }

}
