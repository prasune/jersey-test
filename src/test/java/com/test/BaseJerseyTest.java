package com.test;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseJerseyTest extends JerseyTest {

    @Override
    public TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    public ServletDeploymentContext configureDeployment() {
        return ServletDeploymentContext.forServlet(new ServletContainer(configure())).build();
    }

    @Override
    protected abstract ResourceConfig configure();

    protected <T> ResourceConfig configure(Class<T> resourceClass, Object...objectsToBind) {
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                for (Object objectToBind : objectsToBind) {
                    bind(objectToBind).to((Class) objectToBind.getClass());
                }
            }
        };
        return new ResourceConfig(resourceClass).register(binder);
    }
}
