package ca.ulaval.glo4002.cafe.small.cafe.api.layout;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ca.ulaval.glo4002.cafe.api.layout.LayoutResource;
import ca.ulaval.glo4002.cafe.application.layout.LayoutService;
import ca.ulaval.glo4002.cafe.application.layout.payload.LayoutPayload;
import ca.ulaval.glo4002.cafe.domain.CafeName;

import jakarta.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LayoutResourceTest {
    private static final CafeName A_CAFE_NAME = new CafeName("Bob");
    private static final LayoutPayload A_LAYOUT_PAYLOAD = new LayoutPayload(A_CAFE_NAME, new ArrayList<>());

    private LayoutService layoutService;
    private LayoutResource cafeResource;

    @BeforeEach
    public void createCafeResource() {
        layoutService = mock(LayoutService.class);
        cafeResource = new LayoutResource(layoutService);
    }

    @Test
    public void whenGettingLayout_shouldGetLayout() {
        when(layoutService.getLayout()).thenReturn(A_LAYOUT_PAYLOAD);

        cafeResource.layout();

        verify(layoutService).getLayout();
    }

    @Test
    public void whenGettingLayout_shouldReturn200() {
        when(layoutService.getLayout()).thenReturn(A_LAYOUT_PAYLOAD);

        Response response = cafeResource.layout();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
