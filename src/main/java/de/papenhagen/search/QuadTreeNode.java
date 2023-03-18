package de.papenhagen.search;


import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuadTreeNode {

    /**
     * Represents the whole rectangle of this node.
     * ---------
     * |       |
     * |       |
     * |       |
     * ---------
     */
    protected Rectangle2D.Double mBounds;

    /**
     * Represents the top left node of this node.
     * ---------
     * | x |   |
     * |---|---|
     * |   |   |
     * ---------
     */
    protected QuadTreeNode mTopLeftNode;

    /**
     * Represents the top right node of this node.
     * ---------
     * |   | x |
     * |---|---|
     * |   |   |
     * ---------
     */
    protected QuadTreeNode mTopRightNode;

    /**
     * Represents the bottom left node of this node.
     * ---------
     * |   |   |
     * |---|---|
     * | x |   |
     * ---------
     */
    protected QuadTreeNode mBottomLeftNode;

    /**
     * Represents the bottom right node of this node.
     * ---------
     * |   |   |
     * |---|---|
     * |   | x |
     * ---------
     */
    protected QuadTreeNode mBottomRightNode;

    /**
     * List of points of interest A.K.A neighbours inside this node,
     * this list is only filled in the deepest nodes.
     */
    protected List<Neighbour> mNeighbours = new ArrayList<>();

    /**
     * Creates a new node.
     *
     * @param latitude       node's Y start point
     * @param longitude      node's X start point
     * @param latitudeRange  node's height
     * @param longitudeRange node's width
     */
    public QuadTreeNode(final double latitude, final double longitude, final double latitudeRange, final double longitudeRange) {
        this.mBounds = new Rectangle2D.Double(longitude, latitude, longitudeRange, latitudeRange);
    }

    /**
     * Adds a neighbour in the quadtree.
     * This method will navigate and create nodes if necessary, until the smallest (deepest) node is reached
     *
     * @param neighbour       Neighbour
     * @param deepestNodeSize the deepest Node size
     */
    public void addNeighbour(final Neighbour neighbour, final double deepestNodeSize) {
        final double halfSize = this.mBounds.width * .5f;
        if (halfSize < deepestNodeSize) {
            this.mNeighbours.add(neighbour);
            return;
        }

        final QuadTreeNode node = locateAndCreateNodeForPoint(neighbour.latitude(), neighbour.longitude());
        node.addNeighbour(neighbour, deepestNodeSize);
    }

    /**
     * Removes a neighbour from the quadtree.
     *
     * @param id the neighbour's id
     * @return if the neighbour existed and was removed
     */
    public boolean removeNeighbour(final long id) {
        for (Neighbour neighbor : this.mNeighbours) {
            if (id == neighbor.id()) {
                this.mNeighbours.remove(neighbor);
                return true;
            }
        }

        if (this.mTopLeftNode != null) {
            return this.mTopLeftNode.removeNeighbour(id);
        }

        if (this.mBottomLeftNode != null) {
            return this.mBottomLeftNode.removeNeighbour(id);
        }

        if (this.mTopRightNode != null) {
            return this.mTopRightNode.removeNeighbour(id);
        }

        if (this.mBottomRightNode != null) {
            return this.mBottomRightNode.removeNeighbour(id);
        }

        return false;
    }

    /**
     * Recursively search for neighbours inside the given rectangle.
     *
     * @param neighbourSet     a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    public void findNeighboursWithinRectangle(final Set<Neighbour> neighbourSet, final Rectangle2D.Double rangeAsRectangle) {
        boolean end;

        // In case of containing the whole area of interest
        if (this.mBounds.contains(rangeAsRectangle)) {
            end = true;

            // If end is true, it means that we are on the deepest node
            // otherwise we should keep going deeper

            if (this.mTopLeftNode != null) {
                this.mTopLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (this.mBottomLeftNode != null) {
                this.mBottomLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (this.mTopRightNode != null) {
                this.mTopRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (this.mBottomRightNode != null) {
                this.mBottomRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }


            if (end) {
                addNeighbors(true, neighbourSet, rangeAsRectangle);
            }


            return;
        }

        // In case of intersection with the area of interest
        if (this.mBounds.intersects(rangeAsRectangle)) {
            end = true;

            // If end is true, it means that we are on the deepest node
            // otherwise we should keep going deeper

            if (this.mTopLeftNode != null) {
                this.mTopLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (this.mBottomLeftNode != null) {
                this.mBottomLeftNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (this.mTopRightNode != null) {
                this.mTopRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (this.mBottomRightNode != null) {
                this.mBottomRightNode.findNeighboursWithinRectangle(neighbourSet, rangeAsRectangle);
                end = false;
            }

            if (end) {
                addNeighbors(false, neighbourSet, rangeAsRectangle);
            }
        }
    }

    /**
     * Adds neighbours to the found set.
     *
     * @param contains         if the rangeAsRectangle is contained inside the node
     * @param neighborSet      a set to be filled by this method
     * @param rangeAsRectangle the area of interest
     */
    private void addNeighbors(final boolean contains, final Set<Neighbour> neighborSet, final Rectangle2D.Double rangeAsRectangle) {
        if (contains) {
            neighborSet.addAll(this.mNeighbours);
            return;
        }

        neighborSet.addAll(findAll(rangeAsRectangle));
    }

    /**
     * If the rangeAsRectangle is not contained inside this node we have to,
     * search for neighbours that are contained inside the rangeAsRectangle.
     *
     * @param rangeAsRectangle the area of interest
     * @return a Set of Neighnours
     */
    private Set<Neighbour> findAll(final Rectangle2D.Double rangeAsRectangle) {
        return this.mNeighbours.stream()
                .filter(neighbor -> rangeAsRectangle.contains(neighbor.longitude(), neighbor.latitude()))
                .collect(Collectors.toSet());
    }

    /**
     * These methods find and returns in which of the 4 child nodes the latitude and longitude is located.
     * If the node does not exist, it is created.
     *
     * @param latitude  as Latitude of a Point
     * @param longitude as Longitude of a Point
     * @return the node that contains the desired latitude and longitude
     */
    protected QuadTreeNode locateAndCreateNodeForPoint(final double latitude, final double longitude) {
        double halfWidth = this.mBounds.width * .5f;
        double halfHeight = this.mBounds.height * .5f;

        if (longitude < this.mBounds.x + halfWidth) {
            if (latitude < this.mBounds.y + halfHeight) {
                if (this.mTopLeftNode == null) {
                    this.mTopLeftNode = new QuadTreeNode(this.mBounds.y, this.mBounds.x, halfHeight, halfWidth);
                }
                return this.mTopLeftNode;
            }
            if (this.mBottomLeftNode == null) {
                this.mBottomLeftNode = new QuadTreeNode(this.mBounds.y + halfHeight, this.mBounds.x, halfHeight, halfWidth);
            }

            return this.mBottomLeftNode;
        }

        if (latitude < this.mBounds.y + halfHeight) {
            if (this.mTopRightNode == null) {
                this.mTopRightNode = new QuadTreeNode(this.mBounds.y, this.mBounds.x + halfWidth, halfHeight, halfWidth);
            }
            return this.mTopRightNode;
        }
        if (this.mBottomRightNode == null) {
            this.mBottomRightNode = new QuadTreeNode(this.mBounds.y + halfHeight, this.mBounds.x + halfWidth, halfHeight, halfWidth);
        }

        return this.mBottomRightNode;
    }

}
