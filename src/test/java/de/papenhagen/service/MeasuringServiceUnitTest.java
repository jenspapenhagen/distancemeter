package de.papenhagen.service;

import de.papenhagen.enities.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class MeasuringServiceUnitTest {

    @Mock
    private InfoCrawler infoCrawler;

    MeasuringService service;

    @BeforeEach
    void setUp() {
        service = new MeasuringService();
    }

    @Test
    public void testngCallableMeasuring() {
        //given
        final double plz1 = 22525;
        final double plz2 = 22113;

        when(infoCrawler.callBackend(eq(plz1))).thenReturn(new Root(53.551086, 9.993682, "Hamburg", 22525));
        when(infoCrawler.callBackend(eq(plz2))).thenReturn(new Root(53.441086, 9.983682, "Hamburg", 22113));

        //when
        final double measuring = service.callableMeasuring(plz1, plz1);

        //then
        assertThat(measuring).isEqualTo(28);
    }

}