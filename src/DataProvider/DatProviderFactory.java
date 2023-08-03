package DataProvider;

public final class DatProviderFactory {
    // The field must be declared volatile so that double check lock would work
    // correctly.
    private static volatile DatProviderFactory instance;

    private String ourUsablePath;
    public String value;

    private DatProviderFactory(String value) {
        this.value = value;
    }

    public static DatProviderFactory getInstance(String value) {
        // The approach taken here is called double-checked locking (DCL). It
        // exists to prevent race condition between multiple threads that may
        // attempt to get singleton instance at the same time, creating separate
        // instances as a result.
        //
        // It may seem that having the `result` variable here is completely
        // pointless. There is, however, a very important caveat when
        // implementing double-checked locking in Java, which is solved by
        // introducing this local variable.
        //
        // You can read more info DCL issues in Java here:
        // https://refactoring.guru/java-dcl-issue
        DatProviderFactory result = instance;
        if (result != null) {
            return result;
        }
        synchronized(DatProviderFactory.class) {
            if (instance == null) {
                instance = new DatProviderFactory(value);
            }
            return instance;
        }
    }

    public void setOurUsablePath(final String path) {
        this.ourUsablePath = path;
    }

    public String getOurUsablePath() {

        return ourUsablePath;
    }

    public DatProvider createDataProvider() {

        DatProvider datPrv = new DatProvider(this.getOurUsablePath());
        return datPrv;
    }
}
