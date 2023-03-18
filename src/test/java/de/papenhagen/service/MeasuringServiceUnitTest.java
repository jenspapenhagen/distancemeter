package de.papenhagen.service;

import de.papenhagen.enities.Root;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MeasuringServiceUnitTest {


    private AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = org.mockito.MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void testngCallableMeasuring() {
        //given
        final int plz1 = 22525;
        final int plz2 = 22113;

        final Root hamburg = new Root(53.551086, 9.993682, "Hamburg", plz1);
        final Root farAwayFromHamburg = new Root(55.441086, 12.983682, "far away from Hamburg", plz2);

        //when
        final double measuring = MeasuringService.calculateDistance(hamburg, farAwayFromHamburg);

        //then
        assertThat(measuring).isEqualTo(285.6615797629339);
    }

}