package ResultOBJ;

import java.util.logging.Logger;

public class SystemResult {

    String OS;
    public boolean NS = false;
    public boolean Windows = false;
    public boolean Linux = false;

    public String usrDir;
    public SystemResult() {
        super();
        Logger.global.info("System Result Info Initialized...");
    }


    public void OSChk() {

        OS = System.getProperty("os.name");
        Logger.global.info("System OS: " + OS);

        switch (OS) {

            case "Windows 11", "Windows 10":      Windows = true;
                                                  return;

            case "Linux":           Linux = true;
                                    return;

            default:                NS = true;
        }
    }

}
