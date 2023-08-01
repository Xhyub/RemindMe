package ResultOBJ;

public class DirectoryResult {
    private final static String TEMPDIR = "java.io.tmpdir";
    private final static String USERDIR = "user.dir";
    private final static String HOMEDIR = "user.home";
    private final static String FILESEPARATOR = "file.separator";


    public DirectoryResult() {
        super();
    }

    public static String getHmeDir() {

        return System.getProperty(DirectoryResult.HOMEDIR);

    }
}
