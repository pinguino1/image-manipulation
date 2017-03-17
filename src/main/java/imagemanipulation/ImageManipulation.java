/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package imagemanipulation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
// import org.imgscalr.Scalr;
// import org.imgscalr.Scalr.Method;

/**
 *
 * @author alumne
 */
public class ImageManipulation {
    
    public ImageManipulation() {
        
    }
    
   /**
     *
     * @param image The image to be scaled
     * @param imageType Target image type, e.g. TYPE_INT_RGB
     * @param newWidth The required width
     * @param newHeight The required width
     *
     * @return The scaled image
     */
    public BufferedImage scaleImage(BufferedImage image, int imageType,
            int newWidth, int newHeight) {
        // Make sure the aspect ratio is maintained, so the image is not distorted
        double thumbRatio = (double) newWidth / (double) newHeight;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        double aspectRatio = (double) imageWidth / (double) imageHeight;

        if (thumbRatio < aspectRatio) {
            newHeight = (int) (newWidth / aspectRatio);
        } else {
            newWidth = (int) (newHeight * aspectRatio);
        }

        // Draw the scaled image
        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
                imageType);
        Graphics2D graphics2D = newImage.createGraphics();
        //graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        //Best Quality
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);

        return newImage;
    }
    
    /**
     *
     * @param src The image to be cropped
     * @param rect rectangle to crop
     *
     * @return The crop image
     */
    private BufferedImage cropImage(BufferedImage src, Rectangle rect) {
      BufferedImage dest = src.getSubimage(0, 0, rect.width, rect.height);
      return dest; 
   }
    
     /**
     * 
     * Creating new BufferedImage copy
     * @param image The image to be copied
     *
     * @return new BufferedImage copy
     */
    public BufferedImage createBufferedImage(BufferedImage image)  {
          ColorModel cm = image.getColorModel();
          boolean premultiplied = cm.isAlphaPremultiplied();
          WritableRaster raster = image.copyData(image.getRaster());
          return new BufferedImage(cm, raster, premultiplied, null);
     }
    
    /**
     *
     * @param src The image to be converted to Grey
     *
     * @return The grey image
     */
    private BufferedImage greyConversion(BufferedImage src) throws IOException {

        BufferedImage greyImg = createBufferedImage(src);
	// Loop through all the pixels in the image (w = width, h = height)		
	for(int w = 0; w < greyImg.getWidth() ; w++) {			
            for(int h = 0 ; h < greyImg.getHeight() ; h++)	{				
                // BufferedImage.getRGB() saves the colour of the pixel as a single integer.				
                // use Color(int) to grab the RGB values individually.				
                Color color = new Color(greyImg.getRGB(w, h));								
                // use the RGB values to get their average.				
                int averageColor = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);				
                // create a new Color object using the average colour as the red, green and blue				
                // colour values				
                Color avg = new Color(averageColor, averageColor, averageColor);								
                // set the pixel at that position to the new Color object using Color.getRGB().				
                greyImg.setRGB(w, h, avg.getRGB());			
            }		
        }				
        return greyImg;
    }

    
    public static void main(String argv[]) {
        ImageManipulation im = new ImageManipulation();
	// Canvie les imatges per la ruta fins a les vostres.
	String mySourceImage = "/home/alumne/my-source-image.png";
	String myOutputImage = "/home/alumne/my-output-image.png";
	String myOutputImageGrey = "/home/alumne/my-output-image-grey.png";
        try {
            
            BufferedImage in = javax.imageio.ImageIO.read(new java.io.File(mySourceImage));
            
            // Graphics2D
            BufferedImage out = im.scaleImage(in, BufferedImage.TYPE_INT_RGB, 200, 300);
            javax.imageio.ImageIO.write(out, "JPG", new java.io.File(myOutputImageGrey));
            
            // Library to Scale Images
            // http://www.thebuzzmedia.com/software/imgscalr-java-image-scaling-library/
            //BufferedImage out2 = Scalr.resize(in, Method.QUALITY, 300, 400);
            //javax.imageio.ImageIO.write(out2, "JPG", new java.io.File("/home/alumne/Descargas/scaled1.jpg"));
            
            /* Crop Image */ 
            Rectangle r = new Rectangle(100,200);
            out = im.cropImage(in, r);
            javax.imageio.ImageIO.write(out, "JPG", new java.io.File(myOutputImage));
            
            /* Convert Grey Image */ 
            out = im.greyConversion(in);
            javax.imageio.ImageIO.write(out, "JPG", new java.io.File(myOutputImageGrey));
            
                        
            
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}