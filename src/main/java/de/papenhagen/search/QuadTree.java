package de.papenhagen.search;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class QuadTree {

    private static final int TOTAL_X_DEGREES = 360; // -180 to 180 - longitude
    private static final int TOTAL_Y_DEGREES = 180; // -90 to 90   - latitude
    private static final int NORMALIZE_X = 180;
    private static final int NORMALIZE_Y = 90;

    private final QuadTreeNode mRootNode;

    public QuadTree() {
        this.mRootNode = new QuadTreeNode(0, 0, TOTAL_Y_DEGREES, TOTAL_X_DEGREES);
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP2",
              justification = "Extra Constructor with a root node")
    public QuadTree(final QuadTreeNode rootNode) {
        this.mRootNode = rootNode;
    }

    public synchronized void addNeighbour(final long id, final double latitude, final double longitude) {
        final Neighbour neighbour = new Neighbour(id, normalizeLatitude(latitude), normalizeLongitude(longitude));

        this.mRootNode.addNeighbour(neighbour, kmToDegree(100));
    }

    public void removeNeighbour(final long id) {
        this.mRootNode.removeNeighbour(id);
    }

    public Set<Neighbour> findNeighbours(final double latitude, final double longitude, final double rangeInKm) {
        final Set<Neighbour> neighbourSet = new HashSet<>();
        double rangeInDegrees = kmToDegree(rangeInKm);

        Rectangle2D.Double areaOfInterest = getRangeAsRectangle(normalizeLatitude(latitude), normalizeLongitude(longitude), rangeInDegrees);
        this.mRootNode.findNeighboursWithinRectangle(neighbourSet, areaOfInterest);

        return neighbourSet;
    }

    public Set<Long> findNeighboursIds(final double latitude, final double longitude, final double rangeInKm) {
        Set<Neighbour> neighbourSet = findNeighbours(latitude, longitude, rangeInKm);

        return neighbourSet.stream()
                .map(Neighbour::id)
                .collect(Collectors.toSet());
    }

    private double normalizeLatitude(final double latitude) {
        return latitude + NORMALIZE_Y;
    }

    private double normalizeLongitude(final double longitude) {
        return longitude + NORMALIZE_X;
    }

    private Rectangle2D.Double getRangeAsRectangle(final double latitude, final double longitude, final double range) {
        /*
           We need to centralize the point and have the range on every direction
         */
        return new Rectangle2D.Double(Math.max(longitude - range, 0),
                Math.max(latitude - range, 0),
                Math.min(range * 2, TOTAL_X_DEGREES),
                Math.min(range * 2, TOTAL_Y_DEGREES));
    }

    private static double kmToDegree(final double km) {
        return km / 111.f;
    }

}
