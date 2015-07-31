package org.openstreetmap.josm.plugins.mapillary;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores a sequence of MapillaryImage objects.
 *
 * @author nokutu
 * @see MapillaryImage
 *
 */
public class MapillarySequence {
  private final List<MapillaryAbstractImage> images;
  private String key;
  private long created_at;

  /**
   * Creates a sequence without key or timestamp. Used for
   * {@link MapillaryImportedImage} sequences.
   */
  public MapillarySequence() {
    this.images = new ArrayList<>();
  }

  /**
   * Creates a sequence object with the given parameters
   *
   * @param key
   *          The unique identifier of the sequence.
   * @param created_at
   *          The date the sequence was created.
   */
  public MapillarySequence(String key, long created_at) {
    this.images = new ArrayList<>();
    this.key = key;
    this.created_at = created_at;
  }

  /**
   * Returns all MapillaryImages objects contained by this object.
   *
   * @return A List object containing all the {@link MapillaryAbstractImage}
   *         objects that are part of the sequence.
   */
  public List<MapillaryAbstractImage> getImages() {
    return this.images;
  }

  /**
   * Returns the Epoch time when the sequence was captured.
   *
   * @return A long containing the Epoch time when the sequence was captured.
   *
   */
  public long getCreatedAt() {
    return created_at;
  }

  /**
   * Adds a new MapillaryImage object to this object.
   *
   * @param image
   */
  public synchronized void add(MapillaryAbstractImage image) {
    this.images.add(image);
  }

  /**
   * Returns the unique identifier of the sequence.
   *
   * @return A String containing the unique identifier of the sequence. null
   *         means that the sequence has been created locally for imported
   *         images.
   */
  public String getKey() {
    return this.key;
  }

  /**
   * Adds a set of MapillaryImage objects to this object.
   *
   * @param images
   */
  public synchronized void add(List<MapillaryAbstractImage> images) {
    for (MapillaryAbstractImage image : images)
      add(image);
  }

  /**
   * Removes a MapillaryImage object from this object.
   *
   * @param image
   */
  public void remove(MapillaryAbstractImage image) {
    this.images.remove(image);
  }

  /**
   * Returns the next {@link MapillaryAbstractImage} in the sequence.
   *
   * @param image
   *          The {@link MapillaryAbstractImage} object whose next image is
   *          going to be returned.
   * @return The next {@link MapillaryAbstractImage} object in the sequence.
   */
  public MapillaryAbstractImage next(MapillaryAbstractImage image) {
    if (!images.contains(image))
      throw new IllegalArgumentException();
    int i = images.indexOf(image);
    if (i == images.size() - 1)
      return null;
    return images.get(i + 1);
  }

  /**
   * Returns the previous {@link MapillaryAbstractImage} in the sequence.
   *
   * @param image
   *          The {@link MapillaryAbstractImage} object whose previous image is
   *          going to be returned.
   * @return The previous {@link MapillaryAbstractImage} object in the sequence.
   */
  public MapillaryAbstractImage previous(MapillaryAbstractImage image) {
    if (!images.contains(image))
      throw new IllegalArgumentException();
    int i = images.indexOf(image);
    if (i == 0)
      return null;
    return images.get(i - 1);
  }

  /**
   * Returns the difference of index between two {@link MapillaryAbstractImage}
   * objects belonging to the same {@link MapillarySequence}.
   *
   * @param image1
   * @param image2
   * @return The distance between two {@link MapillaryAbstractImage} objects
   *         belonging to the same {@link MapillarySequence}.
   */
  public int getDistance(MapillaryAbstractImage image1,
      MapillaryAbstractImage image2) {
    if (!this.images.contains(image1) || !this.images.contains(image2))
      throw new IllegalArgumentException();
    return Math.abs(this.images.indexOf(image1) - this.images.indexOf(image2));
  }
}