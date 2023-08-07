import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Scanner;

public class TODOsLineItem {
    private final long unixTime = System.currentTimeMillis() / 1000L;
    private String jobID = null;
    private String desc = null;
    private String dateLogged = null;
    private String TODOStatus = null;

    public TODOsLineItem() {
        super();
    }

    public ArrayList<String> createLineItem() {

        Scanner kb = new Scanner(System.in);
        ArrayList<String> lineItem = new ArrayList<>();

        setJobID(kb);
        setDesc(kb);
        setDateLogged(kb);
        setTODOStatus(kb, false);
        // writeToFile();
        System.out.println("TODO entry has been recorded...");

        lineItem.add(Long.toString(unixTime));
        lineItem.add(jobID);
        lineItem.add(desc);
        lineItem.add(dateLogged);
        lineItem.add(TODOStatus);

        return lineItem;
    }

    private void setJobID(Scanner kb) {
        System.out.print("Enter the job ID: ");
        jobID = kb.nextLine().trim();
    }

    private void setDesc(Scanner kb) {
        System.out.print("TODO: ");
        desc = kb.nextLine().trim();
    }

    private void setDateLogged(Scanner kb) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        System.out.print("Date logged: ");
        if (dateLogged == null) {
            try {

                String tme = kb.nextLine();
                tme = tme.trim();
                if (tme.length() == 0) {
                    return;
                }
                LocalDate time = LocalDate.parse(tme, formatter);
                dateLogged = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(time);
            }
            catch(Exception e) {
                System.err.println("Invalid input! Please enter a valid date: ");
                setDateLogged(kb);
            }
        }
    }

    private void setTODOStatus( Scanner kb, boolean flag ) {
        if ( !flag ) {
            System.out.print("Please mark the todo status (A/I): ");
            String s1 = kb.nextLine().toLowerCase().trim();
            switch (s1) {

                case "a":       TODOStatus = "ACTIVE"; break;
                case "i":       TODOStatus = "INACTIVE"; break;
                default:        System.err.print("Invalid input, please try again: ");
                    boolean flg1 = true;
                    setTODOStatus( kb , flg1);
            }
        }
        else {
            String s1 = kb.nextLine().toLowerCase().trim();
            switch (s1) {

                case "a":       TODOStatus = "ACTIVE"; break;
                case "i":       TODOStatus = "INACTIVE"; break;
                default:        System.err.print("Invalid input, please try again: ");
                    boolean flg1 = true;
                    setTODOStatus( kb , flg1);
            }
        }
    }
}
