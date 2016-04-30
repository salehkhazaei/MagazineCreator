/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magazinecreator;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Saleh
 */
public class FileFetch {

    public static BufferedImage fetchImage(String url) {
        try {
            File f = new File(url);
            if (f.exists()) {
                return ImageIO.read(f);
            } else {
                URL u = new URL("http://mokhi.ir/jashn91/" + url.replace(" ", "%20"));
                try {
                    ReadableByteChannel rbc = Channels.newChannel(u.openStream());
                    FileOutputStream fos = new FileOutputStream(f);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    fos.close();
                    rbc.close();
                    return ImageIO.read(f);
                } catch (Exception ex) {
                    return ImageIO.read(u);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(FileFetch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
