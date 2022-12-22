package ca.ulaval.glo4002.cafe.small.cafe.api.layout;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.layout.LayoutResource;
import ca.ulaval.glo4002.cafe.application.CafeService;
import ca.ulaval.glo4002.cafe.application.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.CafeName;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LayoutResourceTest {
    private static final CafeName A_CAFE_NAME = new CafeName("Bob");
    private static final LayoutPayload A_LAYOUT_PAYLOAD = new LayoutPayload(A_CAFE_NAME, new ArrayList<>());

    private CafeService cafeService;
    private LayoutResource cafeResource;

    @BeforeEach
    public void createCafeResource() {
        cafeService = mock(CafeService.class);
        cafeResource = new LayoutResource(cafeService);
    }

    @Test
    public void whenGettingLayout_shouldGetLayout() {
        when(cafeService.getLayout()).thenReturn(A_LAYOUT_PAYLOAD);

        cafeResource.layout();

        verify(cafeService).getLayout();
    }

    @Test
    public void whenGettingLayout_shouldReturn200() {
        when(cafeService.getLayout()).thenReturn(A_LAYOUT_PAYLOAD);

        Response response = cafeResource.layout();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
