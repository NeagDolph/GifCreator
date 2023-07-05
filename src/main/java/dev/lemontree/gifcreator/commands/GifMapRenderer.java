package dev.lemontree.gifcreator.commands;

import dev.lemontree.gifcreator.json.Gif;
import dev.lemontree.gifcreator.json.Network;
import dev.lemontree.gifcreator.util.Giphy;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class GifMapRenderer extends MapRenderer {
    private BufferedImage[] frames;
    private int currentFrame;

    public GifMapRenderer(String gifSearch) throws IOException {
        Giphy giphy = new Giphy();

        String encoded = URLEncoder.encode(gifSearch);

        List<Gif> gifs;

        try {
            gifs = giphy.getGifSearch(encoded);
        } catch (IOException e) {
            throw new RuntimeException("No results for <" + gifSearch + ">");
        }

        if (gifs == null || gifs.size() == 0) {
            throw new RuntimeException("No results for <" + gifSearch + ">");
        }

        List<String> gifUrls = giphy.createUrlList(gifs);

        for (String url : gifUrls) {
            InputStream gifStream = null;

            try {
                gifStream = Network.getFile(url);
                generateFrames(gifStream);

                gifStream.close();

                return;
            } catch (IIOException exc) {
                System.out.println("Corrupted image");
//                exc.printStackTrace();
            } catch (IOException exc) {
                System.out.println("Error retrieving image");
            }

            if (gifStream != null) gifStream.close();
        }

        throw new RuntimeException("Error retrieving gif");
    }

    private void generateFrames(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new IOException("Error retrieving filestream for file");
        }

        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();

        ImageInputStream iis = ImageIO.createImageInputStream(inputStream);

        reader.setInput(iis);
        int numOfFrames = reader.getNumImages(true);

        frames = new BufferedImage[numOfFrames];

        // Initialize a frame to hold the combined image
        BufferedImage combined = new BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2dCombined = combined.createGraphics();

        // Set the rendering hints for better quality
        g2dCombined.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2dCombined.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2dCombined.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Initialize a blank image with the size of the gif
        BufferedImage blank = new BufferedImage(reader.getWidth(0), reader.getHeight(0), BufferedImage.TYPE_INT_ARGB);


        for (int i = 0; i < numOfFrames; i++) {
            BufferedImage frame = reader.read(i);

            // Retrieve frame metadata
            IIOMetadata metadata = reader.getImageMetadata(i);
            IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode imageDescriptor = (IIOMetadataNode) root.getElementsByTagName("ImageDescriptor").item(0);
            IIOMetadataNode graphicControl = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            int x = Integer.parseInt(imageDescriptor.getAttribute("imageLeftPosition"));
            int y = Integer.parseInt(imageDescriptor.getAttribute("imageTopPosition"));
            String disposalMethod = graphicControl.getAttribute("disposalMethod");

            // Draw the scaled image onto a new frame image
            frames[i] = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D frameG2d = frames[i].createGraphics();

            Graphics g2dBlank;
            if (disposalMethod.equals("doNotDispose")) {
                // Draw the frame onto the combined image at the correct position
                g2dBlank = blank.getGraphics();
                g2dBlank.drawImage(frame, x, y, null);
                g2dBlank.dispose();

                // Draw the combined image onto the blank image
                g2dCombined.drawImage(blank, 0, 0, 128, 128, null);
                g2dBlank.clearRect(0, 0, blank.getWidth(), blank.getHeight());
                g2dBlank.dispose();
            } else {
                g2dCombined.drawImage(frame, 0, 0, 128, 128, null);
            }

            frameG2d.drawImage(combined, 0, 0, 128, 128, null);
            frameG2d.dispose();
        }

        g2dCombined.dispose();
    }

    @Override
    public void render(@NotNull MapView view, @NotNull MapCanvas canvas, @NotNull Player player) {
        if (frames != null && currentFrame < frames.length && frames[currentFrame] != null) {
            currentFrame = (currentFrame + 1) % frames.length;
            canvas.drawImage(0, 0, frames[currentFrame]);
        }
    }
}
