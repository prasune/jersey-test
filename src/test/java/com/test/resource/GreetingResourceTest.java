package com.test.resource;

import com.test.model.Greeting;
import com.test.service.GreetingService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GreetingResourceTest extends JerseyTest {

    @BeforeAll
    public void setup() throws Exception {
        super.setUp();
    }

    @AfterAll
    public void teardown() throws Exception {
        super.tearDown();
    }

    GreetingService greetingService;

    @Override
    protected ResourceConfig configure() {
        greetingService = Mockito.mock(GreetingService.class);
        Mockito.when(greetingService.doSomeProcessing(anyString(), anyString(), any(HttpServletRequest.class)))
                .thenReturn(Response.ok(new Greeting()).build());
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
                bind(greetingService).to(GreetingService.class);
            }
        };
        return new ResourceConfig(GreetingResource.class).register(binder);
    }

    @Override
    public TestContainerFactory getTestContainerFactory() {
        return new GrizzlyWebTestContainerFactory();
    }

    @Override
    public ServletDeploymentContext configureDeployment() {
        return ServletDeploymentContext.forServlet(new ServletContainer(configure())).build();
    }

    @Test
    public void givenGetHiGreeting_whenCorrectRequest_thenResponseIsOkAndContainsHi() {
        Response response = target("/greetings/1324").request()
                .get();

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

        Greeting content = response.readEntity(Greeting.class);
        Assertions.assertNotNull(content);
    }
}
