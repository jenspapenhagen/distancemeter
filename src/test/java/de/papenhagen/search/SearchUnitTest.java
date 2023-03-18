package de.papenhagen.search;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.util.Random;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchUnitTest {

    private static QuadTree mQuadTree;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void testing() {
        //given
        QuadTreeNode quadTreeNode = new QuadTreeNode(0, 0, 180, 360);
        mQuadTree = new QuadTree(quadTreeNode);
        fill();

        //when
        Set<Neighbour> neighbours = mQuadTree.findNeighbours(50, 50, 20);

        //then
        assertThat(neighbours).isNotNull();
        assertThat(neighbours).isNotEmpty();
    }

    private static void fill() {
        Random random = new Random();
        for (int i = 0; i < 1_000_000; i++) {
            mQuadTree.addNeighbour(i, random.nextDouble() * 180 - 90, random.nextDouble() * 360 - 180);
        }
    }
}
