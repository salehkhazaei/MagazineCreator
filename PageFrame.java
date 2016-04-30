package magazinecreator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class PageFrame extends JFrame {

    JSONObject obj;
    int user_id = 0;
    HashSet<Character> chars;

    public static String read(String url) {
        for (int i = 0; i < 10; i++) {
            try {
                URL google = new URL(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(google.openStream()));
                String inputLine;

                String res = "";
                while ((inputLine = in.readLine()) != null) {
                    res += inputLine + '\n';
                }
                in.close();
                return res;
            } catch (MalformedURLException me) {
//                System.out.println(me);
            } catch (IOException ioe) {
//                System.out.println(ioe);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.err.println("could not fetch " + url);
        return "";
    }

    public boolean initJSON(int id) {
        String updateStr = read("http://saleh-khazaei.com/jashn91/majale_json.php?dontclose=0&user=" + id);
        if (updateStr.trim().length() == 0) {
            return false;
        }
        obj = new JSONObject(updateStr);
        return true;
    }

    public PageFrame(int id, HashSet<Character> chars) {
        this.chars = chars;
        this.user_id = id;
        if (!initJSON(id)) {
            this.dispose();
            return;
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        JPanel panel = new JPanel() {
            Color blue_up = new Color(1, 161, 153);
            Color blue_down = new Color(1, 161, 153);
            Color blue_left = new Color(0, 184, 174);
            Color blue_right = new Color(4, 144, 143);

            Color red_up = new Color(186, 45, 2);
            Color red_down = new Color(186, 45, 2);
            Color red_left = new Color(208, 50, 2);
            Color red_right = new Color(167, 40, 7);

            Color orange_up = new Color(245, 183, 12);
            Color orange_down = new Color(245, 183, 12);
            Color orange_left = new Color(247, 205, 0);
            Color orange_right = new Color(246, 166, 27);

            Color black_up = new Color(72, 97, 101);
            Color black_right = new Color(59, 80, 85);

            Color blue = blue_left;
            Color red = red_left;
            Color orange = orange_right;
            Color black = black_right;

            Color blue_opac = new Color(blue.getRed(), blue.getGreen(), blue.getBlue(), 50);
            Color red_opac = new Color(red.getRed(), red.getGreen(), red.getBlue(), 50);
            Color orange_opac = new Color(orange.getRed(), orange.getGreen(), orange.getBlue(), 50);
            Color black_opac = new Color(black.getRed(), black.getGreen(), black.getBlue(), 50);

            int tri_leftX[] = {0, 0, 115};
            int tri_leftY[] = {0, 230, 115};

            int tri_rightX[] = {115, 230, 230};
            int tri_rightY[] = {115, 0, 230};

            int tri_upX[] = {0, 115, 230};
            int tri_upY[] = {0, 115, 0};

            int tri_downX[] = {0, 115, 230};
            int tri_downY[] = {230, 115, 230};

            int edgeX[] = {0, 100, 550, 0};
            int edgeY[] = {2375, 3385, 3508, 3508};

            int darkedgeX[] = {100, 550, 880};
            int darkedgeY[] = {3385, 3508, 3508};

            int rollX[] = {0, 0, 2480, 2480};
            int rollY[] = {600, 800, 200, 0};

            int xxx = 0;
            public final boolean DEBUG_MODE = true;
            public final int MIN_SPACE = 3;

            public void drawTriangles(Graphics2D g, Color up, Color down, Color left, Color right, int num) {
                g.setPaint(up);
                g.fillPolygon(tri_upX, tri_upY, 3);
                g.setPaint(right);
                g.fillPolygon(tri_rightX, tri_rightY, 3);

                for (int i = 0; i < num - 1; i++) {
                    g.translate(230, 0);
                    g.setPaint(up);
                    g.fillPolygon(tri_upX, tri_upY, 3);
                    g.setPaint(right);
                    g.fillPolygon(tri_rightX, tri_rightY, 3);
                    g.setPaint(left);
                    g.fillPolygon(tri_leftX, tri_leftY, 3);
                    g.setPaint(down);
                    g.fillPolygon(tri_downX, tri_downY, 3);
                }
            }

            public int getCharacterMode(char c) // 0-eng, 1-per, 2-sym, 3-space, 4-new line
            {
                if (c == ' ') {
                    return 3;
                }
                if (c == '\n') {
                    return 4;
                }
                if ((c > 'a' && c < 'z') || (c > 'A' && c < 'Z') || (c > '0' && c < '9')) {
                    return 0;
                }
                if ((c > 1500 && c < 1800)) {
                    return 1;
                }
                return 2;
            }

            public boolean containsDifferent(String str) {
                int mode = getCharacterMode(str.charAt(0));
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    if (getCharacterMode(c) != mode) {
                        return true;
                    }
                }
                return false;
            }

            public String[] splitEng(String str) {
                ArrayList<String> strs = new ArrayList<>();
                String temp = "";
                int mode = getCharacterMode(str.charAt(0));
                for (int i = 0; i < str.length(); i++) {
                    char c = str.charAt(i);
                    if (getCharacterMode(c) != mode) {
                        strs.add(temp);
                        temp = "";
                    }
                    mode = getCharacterMode(c);
                    temp += c;
                }
                if (temp.length() > 0) {
                    strs.add(temp);
                }
                return (String[]) strs.toArray(new String[strs.size()]);
            }

            public int stringNoSpaceWidth(Graphics g, String str, int min_space) {
                Font f = g.getFont();
                Font e = new Font("Arial", 0, f.getSize() - 8);

                int size = 0;
                StringTokenizer token = new StringTokenizer(str, " ");
                while (token.hasMoreTokens()) {
                    String word = token.nextToken();
                    if (containsDifferent(word)) {
                        String[] arr = splitEng(word);
                        for (int i = 0; i < arr.length; i++) {
                            int mode = getCharacterMode(arr[i].charAt(0));
                            if (mode == 1) // per
                            {
                                g.setFont(f);
                            } else {
                                g.setFont(e);
                            }
                            size += g.getFontMetrics().stringWidth(arr[i]);
                        }
                        size += min_space;
                    } else {
                        int mode = getCharacterMode(word.charAt(0));
                        if (mode == 1) // per
                        {
                            g.setFont(f);
                        } else {
                            g.setFont(e);
                        }
                        size += g.getFontMetrics().stringWidth(word) + min_space;
                    }
                    g.setFont(f);
                }
                return size;
            }

            public void addChars(String str) {
                for (int i = 0; i < str.length(); i++) {
                    chars.add(str.charAt(i));
                }
            }

            public int drawString(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift) {
                return drawString(g, str, x, y, w, h, start_y, first_shift, false);
            }

            public int drawString(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift, boolean center) {
                Font f = g.getFont();
                Font e = new Font("Arial", 0, f.getSize() - 8);

                Color c = g.getColor();
                g.setColor(Color.red);
                g.drawRect(x, y, w, h);
                g.setColor(c);

                ArrayList<String> lines = new ArrayList<>();
                boolean first_line = true;
                {
                    String word = "";
                    StringTokenizer token = new StringTokenizer(str, " ");
                    String line = "";
                    while (token.hasMoreTokens()) {
                        word = token.nextToken();
                        if (word.equals("<BR>")) {
                            lines.add(line.trim());
                            line = "";
                            first_line = false;
                            continue;
                        }
                        int wi2 = stringNoSpaceWidth(g, (line + word), MIN_SPACE);
                        if (first_line) {
                            wi2 += first_shift;
                        }
                        if (wi2 > w) {
                            lines.add(line.trim());
                            line = word + " ";
                            first_line = false;
                            continue;
                        }
                        line += word + " ";
                    }
                    lines.add(line.trim());
                }
                int yy = y;
                first_line = true;
                for (String lin : lines) {
                    if (lin.trim().length() == 0) {
                        continue;
                    }
                    // draw line by line
                    int wi = stringNoSpaceWidth(g, (lin), 0) + (first_line ? first_shift : 0);
                    if (g.getFontMetrics().stringWidth((lin)) + (first_line ? first_shift : 0) < 0.8 * w) {
                        if (containsDifferent(lin)) {
                            g.setColor(Color.ORANGE);
                            int xxx = x + w - (first_line ? first_shift : 0);
                            String words[] = lin.split(" ");
                            first_line = false;
                            for (int i = 0; i < words.length; i++) {
                                String word = words[i];
                                if (containsDifferent(word)) {
                                    String arr[] = splitEng(word);
                                    for (int k = 0; k < arr.length; k++) {
                                        int mode = getCharacterMode(arr[k].charAt(0));
                                        if (mode == 1) // per
                                        {
                                            g.setFont(f);
                                        } else {
                                            g.setFont(e);
                                        }
                                        g.drawString(arr[k],
                                                xxx - g.getFontMetrics().stringWidth(arr[k]),
                                                yy + g.getFontMetrics().getAscent());
                                        xxx -= g.getFontMetrics().stringWidth(arr[k]);
                                    }
                                } else {
                                    int mode = getCharacterMode(word.charAt(0));
                                    if (mode == 1) // per
                                    {
                                        g.setFont(f);
                                    } else {
                                        g.setFont(e);
                                    }
                                    g.drawString(word,
                                            xxx - g.getFontMetrics().stringWidth(word),
                                            yy + g.getFontMetrics().getAscent());
                                    xxx -= g.getFontMetrics().stringWidth(word);
                                }
                                xxx -= MIN_SPACE;
                                g.setFont(f);
                            }
                        } else {
                            g.setColor(Color.BLUE);
                            g.drawString(lin,
                                    x + w - g.getFontMetrics().stringWidth(lin) - (first_line ? first_shift : 0),
                                    yy + g.getFontMetrics().getAscent());
                            first_line = false;
                        }

                        yy += g.getFontMetrics().getHeight() + 10;
                        if (yy > y + h) {
                            yy = start_y;
                            xxx -= w + 70;
                            x = xxx;
                        }
                        continue;
                    }
                    int words = lin.split(" ").length;
                    int s = w - wi;
                    double d;
                    if (words <= 1) {
                        d = 0;
                    } else {
                        d = (double) s / (double) (words - 1);
                    }
                    String word = "";
                    StringTokenizer token = new StringTokenizer(lin, " ");
                    int xx = x + w - (first_line ? first_shift : 0);
                    g.setColor(Color.GREEN);
                    while (token.hasMoreTokens()) {
                        word = token.nextToken();
                        if (containsDifferent(word)) {
                            String arr[] = splitEng(word);
                            for (int k = 0; k < arr.length; k++) {
                                int mode = getCharacterMode(arr[k].charAt(0));
                                if (mode == 1) // per
                                {
                                    g.setFont(f);
                                } else {
                                    g.setFont(e);
                                }
                                g.drawString(arr[k],
                                        xx - g.getFontMetrics().stringWidth(arr[k]),
                                        yy + g.getFontMetrics().getAscent());
                                xx -= g.getFontMetrics().stringWidth(arr[k]);
                            }
                        } else {
                            int mode = getCharacterMode(word.charAt(0));
                            if (mode == 1) // per
                            {
                                g.setFont(f);
                            } else {
                                g.setFont(e);
                            }
                            g.drawString(word,
                                    xx - g.getFontMetrics().stringWidth(word),
                                    yy + g.getFontMetrics().getAscent());
                            xx -= g.getFontMetrics().stringWidth(word);
                        }
                        xx -= d;
                        g.setFont(f);
                    }
                    yy += g.getFontMetrics().getHeight() + 10;
                    if (yy > y + h) {
                        yy = start_y;
                        xxx -= w + 70;
                        x = xxx;
                    }
                    first_line = false;
                }
                return yy;
            }

//            @Override
//            public void paint(Graphics g2d) {
            public JPanel paintPic() {
                System.out.print("phase 1...");
                BufferedImage buf = new BufferedImage(2480, 3508, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = buf.createGraphics();

                int majorid = obj.getInt("majorid");
                Color defaultc = null;
                Color default_up = null;
                Color default_down = null;
                Color default_left = null;
                Color default_right = null;
                switch (majorid) {
                    case 1:
                        defaultc = orange;
                        default_up = orange_up;
                        default_down = orange_down;
                        default_left = orange_left;
                        default_right = orange_right;
                        break;
                    case 2:
                        defaultc = red;
                        default_up = red_up;
                        default_down = red_down;
                        default_left = red_left;
                        default_right = red_right;
                        break;
                    case 3:
                        defaultc = blue;
                        default_up = blue_up;
                        default_down = blue_down;
                        default_left = blue_left;
                        default_right = blue_right;
                        break;
                }

                g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setColor(Color.white);
                g.fillRect(0, 0, 2480, 3508);

                /*
                GradientPaint rgp = new GradientPaint(0, 0, new Color(209, 213, 212), 0, 2000, Color.WHITE);

                // paint big shadow
                g.rotate(Math.toRadians(-45));
                g.setPaint(rgp);
                g.fillRect(-615, 400, 1120, 2000);
                g.rotate(Math.toRadians(45));

                // remove extra part
                g.setColor(Color.white);
                g.fillRect(0, 0, 50, 2000);

                // draw triangles
                g.translate(1560, 0);
                drawTriangles(g, orange_up, orange_down, orange_left, orange_right, 4);
                g.translate(-2250, 0);

                g.translate(1790, 230);
                drawTriangles(g, red_up, red_down, red_left, red_right, 3);
                g.translate(-2250, -230);

                g.translate(2020, 460);
                drawTriangles(g, blue_up, blue_down, blue_left, blue_right, 2);
                g.translate(-2250, -460);

                g.translate(2250, 690);
                drawTriangles(g, black_up, black_up, black_right, black_right, 1);
                g.translate(-2250, -690);

                // draw rolls
                g.translate(0, 2200);
                g.setPaint(orange_opac);
                g.fillPolygon(rollX, rollY, 4);

                g.translate(0, 200);
                g.setPaint(red_opac);
                g.fillPolygon(rollX, rollY, 4);

                g.translate(0, 200);
                g.setPaint(blue_opac);
                g.fillPolygon(rollX, rollY, 4);

                g.translate(0, 200);
                g.setPaint(black_opac);
                g.fillPolygon(rollX, rollY, 4);
                g.translate(0, -2800);

                // draw edge
                g.setPaint(defaultc);
                g.fillPolygon(edgeX, edgeY, 4);
                g.setPaint(defaultc.darker());
                g.fillPolygon(darkedgeX, darkedgeY, 3);
                 */
                System.out.print("phase 2...");
                try {
                    // draw profile pic
                    String picUrl = obj.getString("pic");
                    if (picUrl.trim().length() > 0) {
                        URL url;
                        try {
                            url = new URL("http://saleh-khazaei.com/jashn91/" + picUrl.replace(" ", "%20"));
                            Image image = ImageIO.read(url);
                            BufferedImage img_buf = new BufferedImage(660, 660, BufferedImage.TYPE_INT_RGB);
                            Graphics2D gg = img_buf.createGraphics();

                            double scale_w = 660.0 / image.getWidth(null);
                            double scale_h = 660.0 / image.getHeight(null);
                            double scale = Math.max(scale_h, scale_w);
                            gg.drawImage(image, -((int) (image.getWidth(null) * scale) - 660) / 2, -((int) (image.getHeight(null) * scale) - 660) / 2, (int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), null);

                            g.drawImage(img_buf, 50, 0, null);

                        } catch (MalformedURLException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (Exception e) {
                }

                // draw profile details background
                g.setColor(defaultc);
                g.fillRect(50, 660, 660, 260);

                // draw profile details
                g.setColor(Color.white);
                g.setFont(new Font("IRANSans", Font.BOLD, 45));

                try {
                    g.drawString("گرایش: " + obj.getString("major"), 700 - g.getFontMetrics().stringWidth("گرایش: " + obj.getString("major")), 710);
                    addChars(obj.getString("major"));
                } catch (Exception e) {
                }
                try {
                    g.drawString("اهل: " + obj.getString("city"), 700 - g.getFontMetrics().stringWidth("اهل: " + obj.getString("city")), 770);
                    addChars(obj.getString("city"));
                } catch (Exception e) {
                }
                try {
                    g.drawString("تاریخ تولد: " + obj.getString("birth"), 700 - g.getFontMetrics().stringWidth("تاریخ تولد: " + obj.getString("birth")), 830);
                    addChars(obj.getString("birth"));
                } catch (Exception e) {
                }

                g.setFont(new Font("Arial", Font.BOLD, 32));
                try {
                    g.drawString(obj.getString("email"), 370 - g.getFontMetrics().stringWidth(obj.getString("email")) / 2, 880);
                    addChars(obj.getString("email"));
                } catch (Exception e) {
                }

                // draw name & id
                g.setColor(defaultc);
                Font font = new Font("IRANSans", Font.BOLD, 100);
                g.setFont(font);

                int x = 1153 - g.getFontMetrics().stringWidth(obj.getString("name")) / 2;
                g.drawString(obj.getString("name"), x, g.getFontMetrics().getHeight() - 40);
                addChars(obj.getString("name"));

                g.setColor(defaultc.darker());
                g.fillRect(x - 30, g.getFontMetrics().getHeight() + 10, g.getFontMetrics().stringWidth(obj.getString("name")) + 70, 5);
                g.setColor(defaultc);

                int y = 10 + g.getFontMetrics().getHeight();
                g.setFont(new Font("B Koodak", 0, 70));
                x = 1153 - g.getFontMetrics().stringWidth(obj.getString("id")) / 2;
                g.drawString(obj.getString("id"), x, y + g.getFontMetrics().getHeight());
                addChars(obj.getString("id"));

                // draw major logo
                /*
                try {
                    BufferedImage logo = ImageIO.read(new File((majorid == 1 ? "softwareLogo.png" : (majorid == 2 ? "hardwareLogo.png" : "itLogo.png"))));
                    g.drawImage(logo, 10, 570, null);
                } catch (IOException ex) {
                    Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                 */
                // draw page pic
                int height_pic = 0;
                boolean havePic = false;
                int picOpt = 0;
                System.out.print("phase 3...");
                try {
                    String picUrl = obj.getString("pagePic");
                    picOpt = obj.getInt("picopt");
                    if (picUrl.trim().length() > 0) {
                        havePic = true;
                        try {
                            URL url = new URL("http://saleh-khazaei.com/jashn91/" + picUrl.replace(" ", "%20"));
                            Image image = ImageIO.read(url);

                            /*                            if (image.getWidth(null) + 50 > image.getHeight(null)) {
                                horiz = true;
                            } */
                            BufferedImage img_buf;
                            int mid;
                            switch (picOpt) {
                                case 1:
                                    height_pic = (int) (750 * ((double) image.getHeight(null) / (double) image.getWidth(null)));
                                    mid = 780 + 375;
                                    img_buf = new BufferedImage(750, height_pic, BufferedImage.TYPE_INT_ARGB);
                                    break;
                                case 2:
                                    height_pic = (int) (1570 * ((double) image.getHeight(null) / (double) image.getWidth(null)));
                                    mid = 780 + 785;
                                    img_buf = new BufferedImage(1570, height_pic, BufferedImage.TYPE_INT_ARGB);
                                    break;
                                default:
                                    height_pic = 700;
                                    mid = 780 + 785;
                                    img_buf = new BufferedImage(1570, 700, BufferedImage.TYPE_INT_ARGB);
                                    break;
                            }

                            Graphics2D gg = img_buf.createGraphics();

                            double scale_w = (float) img_buf.getWidth() / image.getWidth(null);
                            double scale_h = (float) img_buf.getHeight() / image.getHeight(null);
                            double scale = Math.min(scale_h, scale_w);
                            gg.drawImage(image, -((int) (image.getWidth(null) * scale) - img_buf.getWidth()) / 2, -((int) (image.getHeight(null) * scale) - img_buf.getHeight()) / 2, (int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), null);

                            g.drawImage(img_buf, mid - img_buf.getWidth() / 2, 3450 - height_pic, null);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (Exception e) {
                }

                // draw short answers 
                g.setFont(new Font("B Titr", Font.BOLD, 50));
                g.setColor(Color.BLACK);
                g.drawString("کوتاه پاسخ ها", 700 - g.getFontMetrics().stringWidth("کوتاه پاسخ ها"), 990);

                g.setFont(new Font("B Lotus", 0, 40));
                String strShortA = "";
                g.setColor(Color.BLACK);
                if (obj.has("shortAs")) {
                    JSONObject array = obj.getJSONObject("shortAs");
                    ArrayList<OrderedString> shortAs = new ArrayList<>();
                    for (int i = 0; i < array.keySet().size(); i++) {
                        String key = (String) array.keySet().toArray()[i];
                        JSONObject ans = (JSONObject) array.get(key);
                        if (key.contains("؟")) {
                            shortAs.add(new OrderedString(ans.getInt("prior"), key + " " + ans.getString("text")));
                        } else {
                            shortAs.add(new OrderedString(ans.getInt("prior"), key + ": " + ans.getString("text")));
                        }
                        addChars(key);
                        addChars(ans.getString("text"));
                    }
                    shortAs.sort(new Comparator<OrderedString>() {
                        @Override
                        public int compare(OrderedString o1, OrderedString o2) {
                            return o1.prior - o2.prior;
                        }
                    });
                    for (int i = 0; i < shortAs.size(); i++) {
                        strShortA += shortAs.get(i).str + " <BR> ";
                    }
                    drawString(g, strShortA, 50, 1020, 650, 2400, 4000, 0);
                }

                int y_main = 500;
                // draw long answers
                g.setFont(new Font("B Lotus", 0, 50));
                drawString(g, "این قسمت یک متن در مورد شما نوشته خواهد شد :)", 750, 500, 1350, g.getFontMetrics().getHeight() * 2 + 20, 5000, 50);

                y_main += g.getFontMetrics().getHeight() * 2 + 25;

                String longA = obj.getString("longA");
                if (longA.trim().length() > 0) {
                    addChars(longA);

                    g.setFont(new Font("B Titr", Font.BOLD, 50));
                    g.drawString("خاطره", 2200 - g.getFontMetrics().stringWidth("خاطره"), y_main + g.getFontMetrics().getAscent());

                    y_main += g.getFontMetrics().getHeight() + 10;

                    g.setFont(new Font("B Lotus", 0, 40));
                    y_main = drawString(g, longA, 750, y_main, 1560, 350, 5000, 60);

                    y_main += g.getFontMetrics().getHeight() + 10;
                }

                y_main = Math.max(870, y_main);
                // draw diaries

                int max_y = 3508;
                boolean draw_qoute = false;
                g.setColor(Color.BLACK);
                if (obj.has("khatere")) {
                    JSONObject array = obj.getJSONObject("khatere");
                    int yy = y_main;
                    xxx = 1600;

                    ArrayList<OrderedString> khaterat = new ArrayList<>();
                    for (int i = 0; i < array.keySet().size(); i++) {
                        String key = (String) array.keySet().toArray()[i];
                        JSONObject ans = (JSONObject) array.get(key);
                        khaterat.add(new OrderedString(ans.getInt("prior"), ans.getString("text"), key));
                        addChars(key);
                        addChars(ans.getString("text"));
                    }
                    khaterat.sort(new Comparator<OrderedString>() {
                        @Override
                        public int compare(OrderedString o1, OrderedString o2) {
                            return o1.prior - o2.prior;
                        }
                    });

                    for (int i = 0; i < khaterat.size(); i++) {
                        String key = khaterat.get(i).key;
                        String ans = khaterat.get(i).str;

                        if (yy > 3400) {
                            yy = y_main;
                            xxx -= 820;
                        }
                        if (havePic) {
                            switch (picOpt) {
                                case 1:
                                    if (xxx < 1000 && yy > 3450 - height_pic - 100) {
                                        yy = y_main;
                                        xxx -= 820;
                                    }
                                    break;
                                default:
                                    if (yy > 3450 - height_pic - 100) {
                                        yy = y_main;
                                        xxx -= 820;
                                    }
                                    break;
                            }
                        }
                        if (xxx < 620) {
                            break;
                        }
                        if (((xxx < 1000 && yy > 1100) || (i > array.keySet().size() * 0.75)) && !draw_qoute) {
                            draw_qoute = true;
                            // draw qoute
                            String qoute = obj.getString("qoute");
                            if (qoute.trim().length() > 0) {
                                for (int f = 80; f > 40; f--) {
                                    g.setFont(new Font("IRANSans", Font.BOLD, f));
                                    if (g.getFontMetrics().stringWidth("“" + qoute + "”") < 750) {
                                        break;
                                    }
                                }

                                g.setColor(Color.DARK_GRAY);
                                int ytmp = 0;
                                addChars("“" + qoute + "”");
                                if (g.getFontMetrics().stringWidth("“" + qoute + "”") < 750) {
                                    g.drawString("“" + qoute + "”", xxx + 375 - g.getFontMetrics().stringWidth("“" + qoute + "”") / 2,
                                            yy + g.getFontMetrics().getAscent());
                                } else {
                                    ytmp = drawString(g, "“" + qoute + "”", xxx, yy - 10 + g.getFontMetrics().getAscent(), 750, 3400 - yy - 10 + g.getFontMetrics().getHeight(), 5000, 0, true);
                                }

                                int wide = Math.min(750, g.getFontMetrics().stringWidth("“" + qoute + "”"));

                                g.setColor(defaultc);
                                g.fillRect(xxx + 375 - wide / 2,
                                        yy,
                                        wide,
                                        10);
                                if (ytmp != 0) {
                                    yy = ytmp - g.getFontMetrics().getAscent();
                                }
                                g.fillRect(xxx + 375 - wide / 2,
                                        yy + g.getFontMetrics().getHeight(),
                                        wide,
                                        10);
                                yy += 30 + g.getFontMetrics().getAscent() + 50;
                                g.setColor(Color.BLACK);
                            }
                        }
                        if (xxx < 1000 && yy > max_y - 100) {
                            break;
                        }

                        g.setFont(new Font("B Lotus", Font.BOLD, 40));
                        g.drawString(key + ":", xxx + 750 - g.getFontMetrics().stringWidth(key + ":"), yy + g.getFontMetrics().getAscent());
                        yy += g.getFontMetrics().getHeight();

                        g.setFont(new Font("B Lotus", 0, 40));
                        if (havePic) {
                            switch (picOpt) {
                                case 1:
                                    if (xxx < 1000) {
                                        yy = drawString(g, ans, xxx, yy, 750, 3450 - height_pic - 100 - yy, y_main, 20) + 20;
                                    } else {
                                        yy = drawString(g, ans, xxx, yy, 750, 3400 - yy, y_main, 20) + 20;
                                    }
                                    break;
                                default:
                                    yy = drawString(g, ans, xxx, yy, 750, 3450 - height_pic - 100 - yy, y_main, 20) + 20;
                                    break;
                            }
                        } else {
                            yy = drawString(g, ans, xxx, yy, 750, 3400 - yy, y_main, 20) + 20;
                        }
                    }
                }

                // draw tarin boxes
                /*
                if (obj.has("tarin")) {
                    JSONObject tarin = obj.getJSONObject("tarin");
                    g.setColor(Color.WHITE);
                    int xi = 750;
                    int yi = 280;

                    g.setFont(new Font("IRANSans", Font.BOLD, 30));
                    for (int i = 0; i < tarin.keySet().size(); i++) {
                        String best = (String) tarin.keySet().toArray()[i];
                        int place = (int) tarin.get(best);

                        g.fillRect(xi, yi, g.getFontMetrics().stringWidth(best) + 20, g.getFontMetrics().getHeight());
                        xi += g.getFontMetrics().stringWidth(best) + 20 + 20;
                        if (xi > 1700) {
                            xi = 750;
                            yi += g.getFontMetrics().getHeight() + 20;
                        }
                    }
                }
                 */
                System.out.print("phase 4...");
                try {
                    ImageIO.write(buf, "jpg", new File("pages/" + user_id + "_" + Math.abs((user_id * 11) % 9999) + ".jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("done " + user_id);
                //g2d.drawImage(buf, 0, 0, getWidth(), getHeight(), null);
                //PageFrame.this.setVisible(false);
                //PageFrame.this.dispose();
                return this;
            }
        }.paintPic();
        //this.setSize((int) (toolkit.getScreenSize().height * 0.7069555302166477), toolkit.getScreenSize().height);
        //this.setLocation(toolkit.getScreenSize().width / 2 - getWidth() / 2, 0);
        //this.setUndecorated(true);
        //this.setVisible(true);
        //this.setLayout(null);

        //panel.setSize(getSize());
        //panel.setLocation(0, 0);
        //this.add(panel);
    }

    public class OrderedString {

        public int prior;
        public String str;
        public String key;

        public OrderedString(int prior, String str) {
            this.prior = prior;
            this.str = str;
            this.key = "";
        }

        public OrderedString(int prior, String str, String key) {
            this.prior = prior;
            this.str = str;
            this.key = key;
        }
    }
}
