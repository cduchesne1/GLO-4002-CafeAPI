package ca.ulaval.glo4002;

import ca.ulaval.glo4002.context.ApplicationContext;
import ca.ulaval.glo4002.context.ProductionApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ProductionApplicationContext();

        Runnable server = new CafeServer(applicationContext);
        server.run();
    }
}
