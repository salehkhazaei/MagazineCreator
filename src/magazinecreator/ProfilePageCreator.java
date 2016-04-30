/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magazinecreator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import static magazinecreator.PageFrame.read;
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class ProfilePageCreator {

    public static void main(String[] args) {

        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(9113026);
        for (int i = 9131001; i < 9131090; i++) {
            ids.add(i);
        }
        for (int i = 9131901; i < 9131911; i++) {
            ids.add(i);
        }
        ids.add(9131801);
        ids.add(9133093);
        ids.add(9231513);

        BufferedImage buf = new BufferedImage(2480, 3508, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buf.createGraphics();

        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillRect(0, 0, 2480, 3508);

        int x = 0;
        int y = 0;
        int i = 0;
        int temp = 0;
        System.out.println("start");
        for (Integer id : ids) {
            String updateStr = read("http://mokhi.ir/jashn91/majale_json.php?dontclose=0&user=" + id);
            if (updateStr.trim().length() == 0) {
                continue;
            }
            JSONObject obj = new JSONObject(updateStr);
            try {
                // draw profile pic
                String picUrl = obj.getString("pic");
                if (picUrl.trim().length() > 0) {
                    Image image = FileFetch.fetchImage(picUrl);
                    BufferedImage img_buf = new BufferedImage(660, 660, BufferedImage.TYPE_INT_RGB);
                    Graphics2D gg = img_buf.createGraphics();

                    double scale_w = 660.0 / image.getWidth(null);
                    double scale_h = 660.0 / image.getHeight(null);
                    double scale = Math.max(scale_h, scale_w);
                    gg.drawImage(image, -((int) (image.getWidth(null) * scale) - 660) / 2, -((int) (image.getHeight(null) * scale) - 660) / 2, (int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), null);

                    g.drawImage(img_buf, x, y, 496, 438, null);
                    temp++;
                    System.out.println("done [" + temp + "]");
                    x += 496;
                    if (x >= 2480) {
                        x = 0;
                        y += 438;
                    }
                    if (y > 3508) {
                        try {
                            ImageIO.write(buf, "jpg", new File("page" + i + ".jpg"));
                        } catch (IOException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        g.setColor(Color.white);
                        g.fillRect(0, 0, 2480, 3508);
                        i++;
                        y = 0;
                    }
                }
            } catch (Exception e) {
            }
        }
        i++;
        try {
            ImageIO.write(buf, "jpg", new File("page" + i + ".jpg"));
        } catch (IOException ex) {
            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
