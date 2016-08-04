/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magazinecreator;

import com.jhlabs.image.GaussianFilter;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class TheMostCreator {

    public static void main(String[] args) {
        new TarinFrame(true, 0);
    }

    /**
     *
     * @author Saleh
     */
    public static class TarinFrame extends JFrame {

        public JSONObject obj;
        int offset = 0;
        String error = "";
        boolean completed = false;

        public String read(String url) {
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

        public boolean initJSON() {
            String updateStr = read("http://mokhi.ir/jashn91/tarinresult_json.php?hey=1&dontclose");
            if (updateStr.trim().length() == 0) {
                return false;
            }
            obj = new JSONObject(updateStr);
            return true;
        }

        public TarinFrame(boolean left_page, int offset) {
            this.offset = offset;
            if (!initJSON()) {
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

                Color blue_opac = new Color(blue.getRed(), blue.getGreen(), blue.getBlue(), 30);
                Color red_opac = new Color(red.getRed(), red.getGreen(), red.getBlue(), 30);
                Color orange_opac = new Color(orange.getRed(), orange.getGreen(), orange.getBlue(), 30);
                Color black_opac = new Color(black.getRed(), black.getGreen(), black.getBlue(), 30);

                int rollX[] = {0, 0, 1000, 1000};
                int rollY[] = {600, 620, 20, 0};
                int rollX_right[] = {1000, 1000, 0, 0};

                int xxx = 0;
                public final boolean DEBUG_MODE = false;
                public final int MIN_SPACE = 3;

                public int getCharacterMode(char c) // 0-eng, 1-per, 2-sym, 3-space, 4-new line
                {
                    if (c == ' ') {
                        return 3;
                    }
                    if (c == '\n') {
                        return 4;
                    }
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')) {
                        return 0;
                    }
                    if (c >= 1776 && c <= 1785) {
                        return 0;
                    }
                    if (c == 1644) {
                        return 2;
                    }
                    if ((c >= 1500 && c <= 1800)) {
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

                public String[] splitStringByMode(String str) {
                    ArrayList<String> strs = new ArrayList<>();
                    String temp = "";
                    int mode = getCharacterMode(str.charAt(0));
                    for (int i = 0; i < str.length(); i++) {
                        char c = str.charAt(i);
                        if (getCharacterMode(c) != mode) {
                            if (temp.length() > 0) {
                                strs.add(temp);
                            }
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

                public ArrayList<String> handleEng(ArrayList<String> arr) {
                    String[] ar = handleEng(arr.toArray(new String[arr.size()]));
                    ArrayList<String> str = new ArrayList<>();
                    for (int i = 0; i < ar.length; i++) {
                        str.add(ar[i]);
                    }
                    return str;
                }

                public String[] handleEng(String[] arr) {
                    int mode = getCharacterMode(arr[0].charAt(0));
                    int start_eng = (mode == 0 ? 0 : -1);
                    for (int i = 0; i < arr.length; i++) {
                        int temp_mode;
                        if ((temp_mode = getCharacterMode(arr[i].charAt(0))) != mode) {
                            if (mode == 0) {
                                if (temp_mode == 3 || temp_mode == 2) {
                                    continue;
                                }
                                // eng -> something else
                                // start from start_eng and do reverse
                                int end = i - 1;
                                if (getCharacterMode(arr[end].charAt(0)) == 3) {
                                    end--;
                                }
                                if (getCharacterMode(arr[end].charAt(0)) == 2) {
                                    end--;
                                }
                                for (int j = 0; j <= (end - start_eng) / 2; j++) {
                                    String temp = arr[start_eng + j];
                                    arr[start_eng + j] = arr[end - j];
                                    arr[end - j] = temp;
                                }
                                start_eng = -1;
                            }
                            if (temp_mode == 0) {
                                // something else -> eng
                                // set start point
                                start_eng = i;
                            }
                        }
                        mode = temp_mode;
                    }
                    if (start_eng > -1) {
                        if (mode == 0) {
                            // eng -> something else
                            // start from start_eng and do reverse
                            int end = arr.length - 1;
                            if (getCharacterMode(arr[end].charAt(0)) == 3) {
                                end--;
                            }
                            if (getCharacterMode(arr[end].charAt(0)) == 2) {
                                end--;
                            }
                            for (int j = 0; j < (end - start_eng) / 2; j++) {
                                String temp = arr[start_eng + j];
                                arr[start_eng + j] = arr[end - j];
                                arr[end - j] = temp;
                            }
                            start_eng = -1;
                        }
                    }
                    return arr;
                }

                public int stringNoSpaceWidth(Graphics g, String str, int min_space) {
                    String[] sa = handleEng(splitStringByMode(str));
                    ArrayList<String> arr = new ArrayList<>(sa.length);
                    for (int i = 0; i < sa.length; i++) {
                        arr.add(sa[i]);
                    }
                    return stringNoSpaceWidth(g, arr, min_space);
                }

                public int stringNoSpaceWidth(Graphics g, ArrayList<String> arr, int min_space) {
                    Font f = g.getFont();
                    Font e = new Font("Arial", f.getStyle(), f.getSize() - 8);

                    int size = 0;
                    for (int i = 0; i < arr.size(); i++) {
                        String word = arr.get(i);
                        int mode = getCharacterMode(word.charAt(0));
                        switch (mode) // 0-eng, 1-per, 2-sym, 3-space, 4-new line
                        {
                            case 0:
                                g.setFont(e);
                                if (word.charAt(0) >= 1776 && word.charAt(0) <= 1785) {
                                    g.setFont(f);
                                }
                                break;
                            case 1:
                                g.setFont(f);
                                break;
                            case 2:
                                boolean per = true;
                                if (i < arr.size() - 1) {
                                    char c = arr.get(i + 1).charAt(0);
                                    per = (getCharacterMode(c) != 0 || (c >= 1776 && c <= 1785));
                                }
                                if (i > 0) {
                                    char c = arr.get(i - 1).charAt(0);
                                    per = (getCharacterMode(c) != 0 || (c >= 1776 && c <= 1785));
                                }
                                if (word.contains("*")) {
                                    per = false;
                                }
                                String temp = "";
                                for (int j = 0; j < word.length(); j++) {
                                    char c = word.charAt(j);
                                    if (c == '(') {
                                        c = ')';
                                    } else if (c == ')') {
                                        c = '(';
                                    } else if (c == '[') {
                                        c = ']';
                                    } else if (c == ']') {
                                        c = '[';
                                    } else if (c == '<') {
                                        c = '>';
                                    } else if (c == '>') {
                                        c = '<';
                                    } else if (c == '\\') {
                                        c = '/';
                                    } else if (c == '/') {
                                        c = '\\';
                                    } else if (c == '{') {
                                        c = '}';
                                    } else if (c == '}') {
                                        c = '{';
                                    }
                                    temp += c;
                                }
                                word = temp;
                                word = new StringBuilder(word).reverse().toString();
                                if (f.canDisplayUpTo(word) == -1 && per) {
                                    g.setFont(f);
                                } else {
                                    g.setFont(e);
                                }
                                break;
                            case 3:
                                size += min_space;
                            case 4:
                                continue;
                        }
                        size += g.getFontMetrics().stringWidth(word);
                    }
                    g.setFont(f);
                    return size;
                }

                public void trim(ArrayList<String> line) {
                    if (line.isEmpty()) {
                        return;
                    }
                    if (getCharacterMode(line.get(0).charAt(0)) == 3) {
                        line.remove(0);
                    }
                    if (line.isEmpty()) {
                        return;
                    }
                    if (getCharacterMode(line.get(line.size() - 1).charAt(0)) == 3) {
                        line.remove(line.size() - 1);
                    }
                }

                public int drawString(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean indent_from_right) {
                    return drawString(g, str, x, y, w, h, start_y, first_shift, second_shift, false, indent_from_right);
                }

                public String toArabicNumber(String str) {
                    String str2 = "";
                    for (int i = 0; i < str.length(); i++) {
                        char c = str.charAt(i);
                        if (c >= '0' && c <= '9') {
                            c = (char) ((int) c - (int) '0' + 1776);
                        }
                        str2 += c;
                    }
                    return str2;
                }

                public int drawString(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean center, boolean indent_from_right) {
                    Font f = g.getFont();
                    Font e = new Font("Arial", f.getStyle(), f.getSize() - 8);
                    return drawString(g, e, str, x, y, w, h, start_y, first_shift, second_shift, center, indent_from_right);
                }

                public int drawString(Graphics g, Font e, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean center, boolean indent_from_right) {
                    str = toArabicNumber(str);
                    // fonts 
                    Font f = g.getFont();

                    // draw dimensions
                    if (DEBUG_MODE) {
                        Color c = g.getColor();
                        g.setColor(Color.red);
                        g.drawRect(x, y, w, h);
                        g.setColor(c);
                    }

                    ArrayList<ArrayList<String>> lines = new ArrayList<>();
                    int line_number = 0;
                    {
                        String[] arr = splitStringByMode(str);
                        ArrayList<String> line = new ArrayList<>();
                        for (String word : arr) {
                            if (word.equals("\n")) {
                                lines.add(line);
                                line = new ArrayList<>();
                                line_number++;
                                continue;
                            }
                            ArrayList<String> bagWord = new ArrayList<>();
                            bagWord.add(word);
                            int wi2 = stringNoSpaceWidth(g, line, MIN_SPACE) + stringNoSpaceWidth(g, bagWord, MIN_SPACE);
                            if (line_number == 0) {
                                wi2 += first_shift;
                            }
                            if (line_number == 1) {
                                wi2 += second_shift;
                            }
                            if (wi2 > w && !line.isEmpty()) {
                                lines.add(line);
                                line = new ArrayList<>();
                                line_number++;
                            }
                            line.add(word);
                        }
                        if (line.size() > 0) {
                            lines.add(line);
                        }
                    }

                    int yy = y;
                    line_number = 0;
                    for (int z = 0; z < lines.size(); z++) {
                        ArrayList<String> lin = lines.get(z);
                        lin = handleEng(lin);
                        trim(lin);
                        if (lin.isEmpty()) {
                            continue;
                        }
                        // draw line by line
                        int wi = stringNoSpaceWidth(g, lin, g.getFontMetrics().stringWidth(" ")) + (line_number == 0 ? first_shift : 0) + (line_number == 1 ? second_shift : 0);
                        if (wi < 0.8 * w || true) {
                            if (DEBUG_MODE) {
                                g.setColor(Color.ORANGE);
                            }
                            int xx = x + (center ? w / 2 + wi / 2 : w) - (indent_from_right ? ((line_number == 0 ? first_shift : 0) + (line_number == 1 ? second_shift : 0)) : 0);
                            line_number++;

                            int line_h = 0;
                            for (int i = 0; i < lin.size(); i++) {
                                String word = lin.get(i);
                                int mode = getCharacterMode(word.charAt(0));
                                switch (mode) // 0-eng, 1-per, 2-sym, 3-space, 4-new line
                                {
                                    case 0:
                                        g.setFont(e);
                                        if (word.charAt(0) >= 1776 && word.charAt(0) <= 1785) {
                                            g.setFont(f);
                                        }
                                        break;
                                    case 1:
                                        g.setFont(f);
                                        break;
                                    case 2:
                                        boolean per = true;
                                        if (i < lin.size() - 1) {
                                            char c = lin.get(i + 1).charAt(0);
                                            per = (getCharacterMode(c) != 0 || (c >= 1776 && c <= 1785));
                                        }
                                        if (i > 0) {
                                            char c = lin.get(i - 1).charAt(0);
                                            per = (getCharacterMode(c) != 0 || (c >= 1776 && c <= 1785));
                                        }
                                        if (word.contains("*")) {
                                            per = false;
                                        }
                                        String temp = "";
                                        for (int j = 0; j < word.length(); j++) {
                                            char c = word.charAt(j);
                                            if (c == '(') {
                                                c = ')';
                                            } else if (c == ')') {
                                                c = '(';
                                            } else if (c == '[') {
                                                c = ']';
                                            } else if (c == ']') {
                                                c = '[';
                                            } else if (c == '<') {
                                                c = '>';
                                            } else if (c == '>') {
                                                c = '<';
                                            } else if (c == '\\') {
                                                c = '/';
                                            } else if (c == '/') {
                                                c = '\\';
                                            } else if (c == '{') {
                                                c = '}';
                                            } else if (c == '}') {
                                                c = '{';
                                            }
                                            temp += c;
                                        }
                                        word = temp;
                                        word = new StringBuilder(word).reverse().toString();
                                        if (f.canDisplayUpTo(word) == -1 && per) {
                                            g.setFont(f);
                                        } else {
                                            g.setFont(e);
                                        }
                                        break;
                                    case 3:
                                        xx -= MIN_SPACE;
                                        continue;
                                }
                                g.drawString(word,
                                        xx - g.getFontMetrics().stringWidth(word),
                                        yy + g.getFontMetrics().getAscent());
                                xx -= g.getFontMetrics().stringWidth(word);
                                line_h = Math.max(line_h, g.getFontMetrics().getHeight() - 8);
                                g.setFont(f);
                            }

                            yy += line_h;
                            if (yy > y + h && z + 1 < lines.size()) {
                                yy = start_y;
                                xxx -= w + 70;
                                x = xxx;
                            }
                            continue;
                        }

                        wi = stringNoSpaceWidth(g, (lin), 0) + (line_number == 0 ? first_shift : 0) + (line_number == 1 ? second_shift : 0);
                        int words = 0;
                        for (String l : lin) {
                            if (getCharacterMode(l.charAt(0)) == 3) {
                                words++;
                            }
                        }
                        int s = w - wi;
                        double d;
                        if (words < 1) {
                            d = 0;
                        } else {
                            d = (double) s / (double) (words);
                        }

                        if (DEBUG_MODE) {
                            g.setColor(Color.GREEN);
                        }
                        int xx = x + w - (indent_from_right ? ((line_number == 0 ? first_shift : 0) + (line_number == 1 ? second_shift : 0)) : 0);
                        line_number++;

                        int line_h = 0;
                        for (int i = 0; i < lin.size(); i++) {
                            String word = lin.get(i);
                            int mode = getCharacterMode(word.charAt(0));
                            switch (mode) // 0-eng, 1-per, 2-sym, 3-space, 4-new line
                            {
                                case 0:
                                    g.setFont(e);
                                    if (word.charAt(0) >= 1776 && word.charAt(0) <= 1785) {
                                        g.setFont(f);
                                    }
                                    break;
                                case 1:
                                    g.setFont(f);
                                    break;
                                case 2:
                                    boolean per = true;
                                    if (i < lin.size() - 1) {
                                        char c = lin.get(i + 1).charAt(0);
                                        per = (getCharacterMode(c) != 0 || (c >= 1776 && c <= 1785));
                                    }
                                    if (i > 0) {
                                        char c = lin.get(i - 1).charAt(0);
                                        per = (getCharacterMode(c) != 0 || (c >= 1776 && c <= 1785));
                                    }
                                    if (word.contains("*")) {
                                        per = false;
                                    }
                                    String temp = "";
                                    for (int j = 0; j < word.length(); j++) {
                                        char c = word.charAt(j);
                                        if (c == '(') {
                                            c = ')';
                                        } else if (c == ')') {
                                            c = '(';
                                        } else if (c == '[') {
                                            c = ']';
                                        } else if (c == ']') {
                                            c = '[';
                                        } else if (c == '<') {
                                            c = '>';
                                        } else if (c == '>') {
                                            c = '<';
                                        } else if (c == '\\') {
                                            c = '/';
                                        } else if (c == '/') {
                                            c = '\\';
                                        } else if (c == '{') {
                                            c = '}';
                                        } else if (c == '}') {
                                            c = '{';
                                        }
                                        temp += c;
                                    }
                                    word = temp;
                                    word = new StringBuilder(word).reverse().toString();
                                    if (f.canDisplayUpTo(word) == -1 && per) {
                                        g.setFont(f);
                                    } else {
                                        g.setFont(e);
                                    }
                                    break;
                                case 3:
                                    xx -= d;
                                    continue;
                            }
                            g.drawString(word,
                                    xx - g.getFontMetrics().stringWidth(word),
                                    yy + g.getFontMetrics().getAscent());
                            xx -= g.getFontMetrics().stringWidth(word);
                            line_h = Math.max(line_h, g.getFontMetrics().getHeight() - 8);
                            g.setFont(f);
                        }

                        yy += line_h;
                        if (yy > y + h && z + 1 < lines.size()) {
                            yy = start_y;
                            xxx -= w + 70;
                            x = xxx;
                        }
                    }
                    g.setFont(f);
                    return yy;
                }

                public void drawRay(Graphics2D gg) {
                    int width = 2000;
                    int height = 20;
                    int glow_size = 35;
                    Color c = gg.getColor();
                    Color c2 = new Color(c.getRed(), c.getGreen(), c.getBlue(), 100);
                    BufferedImage bud = new BufferedImage(width, height + glow_size * 2, BufferedImage.OPAQUE);
                    Graphics2D g = bud.createGraphics();

                    GradientPaint rgp = new GradientPaint(0, 0, Color.BLACK, 0, glow_size, c2);
                    g.setPaint(rgp);
                    g.fillRect(0, 0, width, glow_size);

                    rgp = new GradientPaint(0, glow_size + height, c2, 0, height + glow_size * 2, Color.BLACK);
                    g.setPaint(rgp);
                    g.fillRect(0, height + glow_size, width, glow_size);

                    g.setPaint(c);
                    g.fillRect(0, glow_size, width, height);

                    bud = new GaussianFilter().filter(bud, null);
                    g = bud.createGraphics();
                    gg.drawImage(bud, 0, 0, null);
                }

                @Override
                public void paint(Graphics g2d) {
                    System.out.print("phase 1...");
                    BufferedImage buf = new BufferedImage(2480, 3508, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = buf.createGraphics();

                    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g.setColor(Color.black);
                    g.fillRect(0, 0, 2480, 3508);

                    // draw rolls
                    System.out.println(left_page);
                    g.rotate(Math.toRadians((left_page ? -15 : 15)));
                    try {
                        BufferedImage bars = ImageIO.read(new File("bars.png"));
                        g.drawImage(bars, (left_page ? -350 : 200), (left_page ? -160 : -800), null);
                    } catch (IOException ex) {
                        Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    /*
                    if (left_page) {
                        g.translate(-300, 200);
                    } else {
                        g.translate(600, -800);
                    }
                    g.setPaint(black_up);
                    g.translate(0, 100);
                    drawRay(g);

                    g.setPaint(blue_left);
                    g.translate(0, 100);
                    drawRay(g);

                    g.setPaint(red_left);
                    g.translate(0, 100);
                    drawRay(g);

                    g.setPaint(orange_left);
                    g.translate(0, 100);
                    drawRay(g);
                    if (left_page) {
                        g.translate(300, -600);
                    } else {
                        g.translate(-600, 400);
                    }
                     */
                    g.rotate(Math.toRadians((left_page ? 15 : -15)));

                    g.setColor(Color.WHITE);
                    g.setFont(new Font("B Titr", Font.BOLD, 200));
                    int tarin_place = (left_page ? 2300 - g.getFontMetrics().stringWidth("ترین ها") : 100);
                    if (offset == 0) {
                        g.drawString("ترین ها", tarin_place, g.getFontMetrics().getAscent() + 100);
                    }

                    int y = 300 + (left_page ? 200 : 400);
                    int x = (left_page ? 1750 + 30 : 1750 - 60);

                    int s = -1;
                    int i = 0;
                    ArrayList<String> keys = new ArrayList<>();
                    for (String key : obj.keySet()) {
                        keys.add(key);
                    }
                    keys.sort(new Comparator<String>() {
                        @Override
                        public int compare(String o1, String o2) {
                            return obj.getJSONObject(o2).getInt("count") - obj.getJSONObject(o1).getInt("count");
                        }
                    });
                    for (String key : keys) {
                        JSONObject best = obj.getJSONObject(key);
                        if (i < offset) {
                            i++;
                            continue;
                        }
                        int hei = 0;
                        try {
                            BufferedImage logo = ImageIO.read(new File("tarin_pdf/tarin" + best.getInt("emoji_id") + ".png"));
                            double scale_w = 180.0 / logo.getWidth(null);
                            double scale_h = 180.0 / logo.getHeight(null);
                            double scale = Math.max(scale_h, scale_w);
                            hei = (int) (logo.getHeight(null) * scale);
                            g.drawImage(logo, x, y, (int) (logo.getWidth(null) * scale), (int) (logo.getHeight(null) * scale), null);
                        } catch (IOException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        g.setFont(new Font("IRANSans", 0, 40));
                        g.setColor(Color.WHITE);
                        g.drawString(key, x + 75 - g.getFontMetrics().stringWidth(key) / 2, y + hei + 25 + g.getFontMetrics().getAscent());

                        g.setStroke(new BasicStroke(5));
                        g.setColor(new Color(140, 140, 140));
                        g.drawArc(x + 75 - 250, y + 75 - 250, 500, 500, -23, 45);

                        g.fillOval((int) (x + 75 - 25 + Math.cos(Math.toRadians(-23)) * 250),
                                (int) (y + 75 - 25 + Math.sin(Math.toRadians(-23)) * 250), 50, 50);

                        g.fillOval((int) (x + 75 - 25 + Math.cos(Math.toRadians(0)) * 250),
                                (int) (y + 75 - 25 + Math.sin(Math.toRadians(0)) * 250), 50, 50);

                        g.fillOval((int) (x + 75 - 25 + Math.cos(Math.toRadians(23)) * 250),
                                (int) (y + 75 - 25 + Math.sin(Math.toRadians(23)) * 250), 50, 50);

                        JSONArray rank = best.getJSONArray("rank");
                        JSONArray rank1 = rank.getJSONArray(0);
                        JSONArray rank2 = rank.getJSONArray(1);
                        JSONArray rank3 = rank.getJSONArray(2);
                        g.setStroke(new BasicStroke(10));
                        g.setColor(new Color(255, 193, 27));
                        g.drawArc((int) (x + 75 - 25 + Math.cos(Math.toRadians(-23)) * 250),
                                (int) (y + 75 - 25 + Math.sin(Math.toRadians(-23)) * 250), 50, 50, 0, 360);

                        g.setColor(new Color(66, 93, 110));
                        g.drawArc((int) (x + 75 - 25 + Math.cos(Math.toRadians(0)) * 250),
                                (int) (y + 75 - 25 + Math.sin(Math.toRadians(0)) * 250), 50, 50, 0,
                                (int) (((double) rank2.getInt(1) / (double) rank1.getInt(1)) * 360.0));

                        g.setColor(new Color(159, 70, 12));
                        g.drawArc((int) (x + 75 - 25 + Math.cos(Math.toRadians(23)) * 250),
                                (int) (y + 75 - 25 + Math.sin(Math.toRadians(23)) * 250), 50, 50, 0,
                                (int) (((double) rank3.getInt(1) / (double) rank1.getInt(1)) * 360.0));

                        try {
                            BufferedImage gold = ImageIO.read(new File("goldLabel.png"));
                            double scale_w = 250.0 / gold.getWidth(null);
                            double scale_h = 250.0 / gold.getHeight(null);
                            double scale = Math.max(scale_h, scale_w);
                            g.drawImage(gold, (int) (x + 120 + 20 - scale * gold.getWidth(null) / 2 + Math.cos(Math.toRadians(-25)) * 400),
                                    (int) (y + 75 + 10 - scale * gold.getHeight(null) / 2 + Math.sin(Math.toRadians(-23)) * 400),
                                    (int) (scale * gold.getWidth(null)),
                                    (int) (scale * gold.getHeight(null)), null);

                            BufferedImage silver = ImageIO.read(new File("silverLabel.png"));
                            g.drawImage(silver, (int) (x + 120 - scale * silver.getWidth(null) / 2 + Math.cos(Math.toRadians(0)) * 400),
                                    (int) (y + 75 - 10 - scale * silver.getHeight(null) / 2 + Math.sin(Math.toRadians(0)) * 400),
                                    (int) (scale * silver.getWidth(null)),
                                    (int) (scale * silver.getHeight(null)), null);

                            BufferedImage bronze = ImageIO.read(new File("bronzeLabel.png"));
                            g.drawImage(bronze, (int) (x + 120 + 15 - scale * bronze.getWidth(null) / 2 + Math.cos(Math.toRadians(25)) * 400),
                                    (int) (y + 75 - 30 - scale * bronze.getHeight(null) / 2 + Math.sin(Math.toRadians(23)) * 400),
                                    (int) (scale * bronze.getWidth(null)),
                                    (int) (scale * bronze.getHeight(null)), null);

                            g.setColor(Color.WHITE);
                            g.setStroke(new BasicStroke(1));
                            g.setFont(new Font("IRANSans", Font.BOLD, 30));

                            int xx = (int) (x + 75 + 13 + Math.cos(Math.toRadians(-23)) * 250);
                            int yy = (int) (y + 75 - 35 + Math.sin(Math.toRadians(-23)) * 250);
                            g.translate(xx, yy);
                            g.rotate(Math.toRadians(-20));
                            if (rank1.getString(0).equals("????")) {
                                drawString(g, rank1.getString(0).replace('ي', 'ی'), 85, 0, 170, 80, 0, 0, 0, true, false);
                            } else {
                                drawString(g, rank1.getString(0).replace('ي', 'ی'), 85, 0, 170, 80, 0, 0, 0, true, false);
                            }
                            g.rotate(Math.toRadians(20));
                            g.translate(-xx, -yy);

                            if (rank2.getString(0).equals("????")) {
                                drawString(g, rank2.getString(0).replace('ي', 'ی'),
                                        (int) (x + 115 + 30 + Math.cos(Math.toRadians(0)) * 300),
                                        (int) (y + 73 - 50 + Math.sin(Math.toRadians(0)) * 300) + 20, 165, 80, 0, 0, 0, true, false);
                            } else {
                                drawString(g, rank2.getString(0).replace('ي', 'ی'),
                                        (int) (x + 115 + 30 + Math.cos(Math.toRadians(0)) * 300),
                                        (int) (y + 73 - 50 + Math.sin(Math.toRadians(0)) * 300), 165, 80, 0, 0, 0, true, false);
                            }

                            xx = (int) (x + 75 + 40 + Math.cos(Math.toRadians(23)) * 300);
                            yy = (int) (y + 73 - 50 + Math.sin(Math.toRadians(23)) * 300);
                            g.translate(xx, yy);
                            g.rotate(Math.toRadians(10));
                            if (rank3.getString(0).equals("????")) {
                                drawString(g, rank3.getString(0).replace('ي', 'ی'), 40, -7, 170, 80, 0, 0, 0, true, false);
                            } else {
                                drawString(g, rank3.getString(0).replace('ي', 'ی'), 40, -7, 170, 80, 0, 0, 0, true, false);
                            }
                            g.rotate(Math.toRadians(-10));
                            g.translate(-xx, -yy);

                            g.setColor(Color.WHITE);
                            g.setFont(new Font("IRANSans", Font.BOLD, 20));
                            g.drawString(toArabicNumber(rank1.getInt(1) + "") + "", (int) (x + 78 - 25 + Math.cos(Math.toRadians(-23)) * 250) + 25 - g.getFontMetrics().stringWidth(toArabicNumber(rank1.getInt(1) + "")) / 2,
                                    (int) (y + 75 - 25 + g.getFontMetrics().getAscent() + Math.sin(Math.toRadians(-23)) * 250) + 8);
                            g.drawString(toArabicNumber(rank2.getInt(1) + "") + "", (int) (x + 75 - 25 + Math.cos(Math.toRadians(0)) * 250) + 25 - g.getFontMetrics().stringWidth(toArabicNumber(rank2.getInt(1) + "")) / 2,
                                    (int) (y + 75 - 25 + g.getFontMetrics().getAscent() + Math.sin(Math.toRadians(0)) * 250) + 8);
                            g.drawString(toArabicNumber(rank3.getInt(1) + "") + "", (int) (x + 75 - 25 + Math.cos(Math.toRadians(23)) * 250) + 25 - g.getFontMetrics().stringWidth(toArabicNumber(rank3.getInt(1) + "")) / 2,
                                    (int) (y + 75 - 25 + g.getFontMetrics().getAscent() + Math.sin(Math.toRadians(23)) * 250) + 8);

                        } catch (IOException ex) {
                            Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }

//                        x += 826 * -1;
                        x += 800 * -1;
                        y += (100 * (left_page ? 1 : -1));
                        if (x < 0) {
//                            s = 1;
//                            x = 98;
                            s = 1;
                            x = (left_page ? 1750 + 30 : 1750 - 60);
                            y += 300 + (left_page ? 0 : 600);
                        }
                        if (x > 2500) {
                            s = 1;
                            x = (left_page ? 1750 + 30 : 1750 - 60);
                            y += 300 + (left_page ? 0 : 300);
                        }
                        if (y > 3200) {
                            new TarinFrame(!left_page, i + 1);
                            break;
                        }
                        i++;
                    }
                    System.out.print("phase 2...");
                    System.out.print("phase 4...");
                    try {
                        ImageIO.write(buf, "jpg", new File("tarin-" + offset + (left_page ? "-left" : "-right") + ".jpg"));
                    } catch (IOException ex) {
                        Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    g2d.drawImage(buf, 0, 0, getWidth(), getHeight(), null);
                    System.out.println("done");
                    completed = true;
                }
            };
            this.setSize((int) (toolkit.getScreenSize().height * 0.7069555302166477), toolkit.getScreenSize().height);
            this.setLocation(toolkit.getScreenSize().width / 2 - getWidth() / 2, 0);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setUndecorated(true);
            this.setVisible(true);
            this.setLayout(null);

            panel.setSize(getSize());
            panel.setLocation(0, 0);
            this.add(panel);
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
}
