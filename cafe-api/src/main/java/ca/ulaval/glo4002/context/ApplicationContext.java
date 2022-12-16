package ca.ulaval.glo4002.context;

import org.glassfish.jersey.server.ResourceConfig;

public interface ApplicationContext {
    ResourceConfig initializeResourceConfig();

    int getPort();
}
