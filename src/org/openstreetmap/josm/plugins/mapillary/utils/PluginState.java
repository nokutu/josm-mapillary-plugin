// License: GPL. For details, see LICENSE file.
package org.openstreetmap.josm.plugins.mapillary.utils;

import static org.openstreetmap.josm.tools.I18n.tr;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.plugins.mapillary.gui.FinishedUploadDialog;
import org.openstreetmap.josm.plugins.mapillary.gui.NotLoggedInDialog;

/**
 * @author nokutu
 *
 */
public final class PluginState {

  private static boolean submittingChangeset;

  private static int runningDownloads;
  /** Images that have to be uploaded. */
  private static int imagesToUpload;
  /** Images that have been uploaded. */
  private static int imagesUploaded;

  private PluginState() {
    // Empty constructor to avoid instantiation
  }

  /**
   * Called when a download is started.
   */
  public static void startDownload() {
    runningDownloads++;
  }

  /**
   * Called when a download is finished.
   */
  public static void finishDownload() {
    if (runningDownloads == 0)
      throw new IllegalStateException(
          "The amount of running downlaods is less or equals to 0");
    runningDownloads--;
  }

  /**
   * Checks if there is any running download.
   *
   * @return true if the plugin is downloading; false otherwise.
   */
  public static boolean isDownloading() {
    return runningDownloads > 0;
  }

  /**
   * Checks if there is a changeset being submitted.
   *
   * @return true if the plugin is submitting a changeset false otherwise.
   */
  public static boolean isSubmittingChangeset() {
    return submittingChangeset;
  }
   /**
   * Checks if there is any running upload.
   *
   * @return true if the plugin is uploading; false otherwise.
   */
  public static boolean isUploading() {
    return imagesToUpload > imagesUploaded;
  }

  /**
   * Sets the amount of images that are going to be uploaded.
   *
   * @param amount
   *          The amount of images that are going to be uploaded.
   */
  public static void addImagesToUpload(int amount) {
    if (imagesToUpload <= imagesUploaded) {
      imagesToUpload = 0;
      imagesUploaded = 0;
    }
    imagesToUpload += amount;
  }

  public static int getImagesToUpload() {
    return imagesToUpload;
  }

  public static int getImagesUploaded() {
    return imagesUploaded;
  }

  /**
   * Called when an image is uploaded.
   */
  public static void imageUploaded() {
    imagesUploaded++;
    if (imagesToUpload == imagesUploaded && Main.main != null) {
        finishedUploadDialog();
    }
  }

  private static void finishedUploadDialog() {
    if (!SwingUtilities.isEventDispatchThread()) {
      JOptionPane pane = new JOptionPane();
      pane.setMessage(new FinishedUploadDialog());
      JDialog dlg = pane.createDialog(Main.parent, tr("Finished upload"));
      dlg.setVisible(true);
    } else {
      SwingUtilities.invokeLater( new Runnable() {
        @Override
        public void run() {
          finishedUploadDialog();
        }
      } );
    }
  }
  public static void notLoggedInToMapillaryDialog() {
    if( Main.main == null) {
        return;
    }
    JOptionPane pane = new JOptionPane();
    pane.setMessage(new NotLoggedInDialog());
    JDialog dlg = pane.createDialog(Main.parent, tr("Not Logged in to Mapillary"));
    dlg.setVisible(true);
  }



  /**
   * Returns the text to be written in the status bar.
   *
   * @return The {@code String} that is going to be written in the status bar.
   */
  public static String getUploadString() {
    return tr("Uploading: {0}", "(" + imagesUploaded + "/" + imagesToUpload + ")");
  }

  public static void setSubmittingChangeset(boolean isSubmitting) {
      submittingChangeset = isSubmitting;
  }
}
