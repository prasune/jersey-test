package com.test.resource;

import com.test.BaseJerseyTest;
import com.test.model.Greeting;
import com.test.service.GreetingService;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GreetingResourceTest extends BaseJerseyTest {

    // do not rename this to setUp(), JerseyTest will get overridden
    @BeforeAll
    public void before() throws Exception {
        super.setUp();
    }

    // do not rename this to tearDown(), JerseyTest will get overridden
    @AfterAll
    public void after() throws Exception {
        super.tearDown();
    }

    @Mock
    GreetingService greetingService;

    @Override
    protected ResourceConfig configure() {
        MockitoAnnotations.openMocks(this);
        return configure(GreetingResource.class, greetingService);
    }

    @Test
    public void givenGetHiGreeting_whenCorrectRequest_thenResponseIsOkAndContainsHi() {
        Mockito.when(greetingService.doSomeProcessing(anyString(), anyString(), any(HttpServletRequest.class)))
                .thenReturn(Response.ok(new Greeting()).build());

        Response response = target("/greetings/1324").request()
                .get();

        Assertions.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Assertions.assertEquals(MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));

        Greeting content = response.readEntity(Greeting.class);
        Assertions.assertNotNull(content);
    }
}
