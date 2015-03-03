/*
 * @(#)ImageUtil.java	2.9.3.1 02/03/15
 *
 * Copyright (c) 1999-2015 Musiques Tangentes. All Rights Reserved.
 *
 * This file is part of Algem.
 * Algem is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Algem is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Algem. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package net.algem.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import net.algem.accounting.AccountUtil;

/**
 * Utility class for image operations.
 * 
 * @author <a href="mailto:jmg@musiques-tangentes.asso.fr">Jean-Marc Gobat</a>
 * @version 2.9.3.1
 */
public class ImageUtil
{

  public static final String IMAGE_PATH = "/resources/images/";
  public static final String PHOTO_PATH = "/resources/photos/";
  public static final String DEFAULT_PHOTO_ID = "/resources/images/idphoto.png";
  public static final String ALGEM_LOGO = "logo.png";
  public static final String SEARCH_ICON = "cherche.png";
  public static final String CAL_ICON = "cal.gif";
  public static final String NO_CONFLICT_ICON = "feuvert.gif";
  public static final String CONFLICT_ICON = "feurouge.gif";
  public static final String DELETE_ICON = "quitter_trans_12x12.png";
  public static final String TAB_CLOSING_ICON = "quitter_trans_12x12.png";
  public static final String [] DEFAULT_IMG_EXTENSIONS = {".jpg",".jpg",".JPG",".JPEG",".png",".PNG"};
  public static final int PHOTO_ID_HEIGHT = 130;
  private static final int PHOTO_ID_WIDTH = 100;

  public ImageUtil() {
  }

  /**
   * Redimensionne les photos proportionnellement à la hauteur d'une photo d'identité (PHOTO_ID_HEIGHT).
   * @param img BufferedImage
   * @return BufferedImage
   */
  public static BufferedImage rescale(BufferedImage img) {
  	return rescale(img, PHOTO_ID_WIDTH, PHOTO_ID_HEIGHT);
  }
  
  
  /**
   * Proportional resizing of an image.
   * @param img 
   * @param nw new width
   * @param nh new heigth
   * @return a buffered image
   */
  public static BufferedImage rescale(BufferedImage img, int nw, int nh) {
  	int w = img.getWidth();
    int h = img.getHeight();
    double width = (double) w;
    double height = (double) h;
    double rapport = 1;
    int newWidth = 0;
    if (width >= height) {
      rapport = width / height;
      newWidth = (int) (nh * rapport);
    } else {
      rapport = height / width;
      newWidth = (int) (nh / rapport);
    }
    BufferedImage dimg = new BufferedImage(newWidth, nh, img.getType());
    Graphics2D g = dimg.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g.drawImage(img, 0, 0, newWidth, nh, 0, 0, w, h, null);
    g.dispose();
    return dimg;
  }

  /**
   * Cropping width to comply to passport photo.
   * @param img BufferedImage
   * @return BufferedImage
   */
  public static BufferedImage formatPhoto(BufferedImage img) {
    try {
      int w = img.getWidth();
      int h = img.getHeight();
      int x = 0;
      //System.out.println("w = "+w+" h = "+h);
      double width = img.getWidth();
      if (width > PHOTO_ID_WIDTH) {
        double half_width = width / 2;
        //System.out.println("half width "+half_width);
        x = (int) (half_width - (PHOTO_ID_WIDTH / 2));
        //System.out.println("x = "+x);      
        BufferedImage img2 = img.getSubimage(x, 0, PHOTO_ID_WIDTH, h);
        return img2;
      } else {
        return img;
      }
    } catch (Exception e) {
      GemLogger.log("#formatPhoto "+e.getMessage());
      return null;
    }
  }

  /**
   * Converts a number of points in mm.
   * @param points
   * @return value in mm
   */
  public static double toMM(int points) {
    double res = (points / 72d) * 25.4;
    return AccountUtil.round(res);
  }

  /**
   * Converts mm value in inches.
   * @param mm 
   * @return value in inches
   */
  public static double toInch(double mm) {
    double res = (mm / 25.4);
    return AccountUtil.round(res);
  }

  /**
   * Converts mm value in points.
   * @param mm 
   * @return a value in points
   */
  public static int toPoints(double mm) {
    return (int) Math.round(toPoints2(toInch(mm)));
  }

  /**
   * Gets the icon with name {@literal file}.
   * @param file file name
   * @return an imageicon
   */
  public static ImageIcon createImageIcon(String file) {
    ImageIcon img = null;
    String path = ImageUtil.IMAGE_PATH + file;
    URL url = new ImageUtil().getClass().getResource(path);
    if (url != null) {
      img = new ImageIcon(url);
    }
    return img;
  }

  /**
   * Converts inches to points (1/72 inch) value.
   * @param inches 
   * @return a value in points
   */
  private static double toPoints2(double inches) {
    return inches * 72;
  }

}
