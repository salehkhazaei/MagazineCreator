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
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class PageFrame extends JFrame {

    public JSONObject obj;
    int user_id = 0;
    HashSet<Character> chars;
    String error = "";
    boolean completed = false;
    boolean left_page;

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
        String updateStr = read("http://mokhi.ir/jashn91/majale_json.php?hey=1&dontclose=0&pdf&user=" + id);
        if (updateStr.trim().length() == 0) {
            return false;
        }
        obj = new JSONObject(updateStr);
        return true;
    }

    public PageFrame(int id, HashSet<Character> chars, boolean left_page, int offset) {
        this.left_page = left_page;
        this.chars = chars;
        this.user_id = id;
        if (!initJSON(id)) {
            this.dispose();
            return;
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        this.error = new JPanel() {
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

            int tri_leftX[] = {0, 0, 115};
            int tri_leftY[] = {0, 230, 115};

            int tri_rightX[] = {115, 230, 230};
            int tri_rightY[] = {115, 0, 230};

            int tri_upX[] = {0, 115, 230};
            int tri_upY[] = {0, 115, 0};

            int tri_downX[] = {0, 115, 230};
            int tri_downY[] = {230, 115, 230};

            int edgeX[] = {0, 50, 550, 0};
            int edgeY[] = {2375, 3435, 3508, 3508};

            int darkedgeX[] = {50, 550, 880};
            int darkedgeY[] = {3435, 3508, 3508};

            int edgeX_right[] = {2480, 2430, 1930, 2480};
            int darkedgeX_right[] = {2430, 1930, 1600};

            int rollX[] = {0, 0, 2480, 2480};
            int rollY[] = {600, 800, 200, 0};
            int rollY_right[] = {0, 200, 800, 600};

            int xxx = 0;
            public final boolean DEBUG_MODE = false;
            public final boolean SHOW_GRAPHICS = true;
            public final int MIN_SPACE = 3;

            public void drawTriangles(Graphics2D g, Color up, Color down, Color left, Color right, int num, boolean left_page) {
                if (left_page) {
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
                } else {
                    for (int i = 0; i < num - 1; i++) {
                        g.setPaint(up);
                        g.fillPolygon(tri_upX, tri_upY, 3);
                        g.setPaint(right);
                        g.fillPolygon(tri_rightX, tri_rightY, 3);
                        g.setPaint(left);
                        g.fillPolygon(tri_leftX, tri_leftY, 3);
                        g.setPaint(down);
                        g.fillPolygon(tri_downX, tri_downY, 3);
                        g.translate(230, 0);
                    }
                    g.setPaint(up);
                    g.fillPolygon(tri_upX, tri_upY, 3);
                    g.setPaint(left);
                    g.fillPolygon(tri_leftX, tri_leftY, 3);
                    g.translate(-230 * (num - 1), 0);
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

            public int stringHeight(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean indent_from_right) {
                return stringHeight(g, str, x, y, w, h, start_y, first_shift, second_shift, false, indent_from_right);
            }

            public int stringHeight(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean center, boolean indent_from_right) {
                Font f = g.getFont();
                Font e = new Font("Arial", f.getStyle(), f.getSize() - 8);
                return stringHeight(g, e, str, x, y, w, h, start_y, first_shift, second_shift, center, indent_from_right);
            }

            public int stringHeight(Graphics g, Font e, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean center, boolean indent_from_right) {
                int hh = 0;
                str = toArabicNumber(str);
                // fonts 
                Font f = g.getFont();

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
                        if (wi2 > w) {
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
                    if (wi < 0.8 * w) {
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
                            xx -= g.getFontMetrics().stringWidth(word);
                            line_h = Math.max(line_h, g.getFontMetrics().getHeight() + 10);
                            g.setFont(f);
                        }
                        yy += line_h;
                        hh += line_h;
                        if (yy > y + h && z + 1 < lines.size()) {
                            yy = start_y;
                            x -= w + 70;
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
                        xx -= g.getFontMetrics().stringWidth(word);
                        line_h = Math.max(line_h, g.getFontMetrics().getHeight() + 10);
                        g.setFont(f);
                    }

                    yy += line_h;
                    hh += line_h;
                    if (yy > y + h && z + 1 < lines.size()) {
                        yy = start_y;
                        x -= w + 70;
                    }
                }
                g.setFont(f);
                return hh;
            }

            public void addChars(String str) {
                for (int i = 0; i < str.length(); i++) {
                    chars.add(str.charAt(i));
                }
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

            public int drawString(Graphics g, String str, int x, int y, int w, int h, int start_y, int first_shift, int second_shift, boolean indent_from_right) {
                return drawString(g, str, x, y, w, h, start_y, first_shift, second_shift, false, indent_from_right);
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
                        if (wi2 > w) {
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
                    if (wi < 0.8 * w) {
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
                            line_h = Math.max(line_h, g.getFontMetrics().getHeight() + 10);
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
                        line_h = Math.max(line_h, g.getFontMetrics().getHeight() + 10);
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

//            @Override
//            public void paint(Graphics g2d) {
            public String paintPic() {
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
                g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g.setColor(Color.white);
                g.fillRect(0, 0, 2480, 3508);

                if (SHOW_GRAPHICS) {

                    if (left_page) {
                        GradientPaint rgp = new GradientPaint(1240, 400, new Color(209, 213, 212), 1240, 2000, Color.WHITE);
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
                        drawTriangles(g, orange_up, orange_down, orange_left, orange_right, 4, true);
                        g.translate(-2250, 0);

                        g.translate(1790, 230);
                        drawTriangles(g, red_up, red_down, red_left, red_right, 3, true);
                        g.translate(-2250, -230);

                        g.translate(2020, 460);
                        drawTriangles(g, blue_up, blue_down, blue_left, blue_right, 2, true);
                        g.translate(-2250, -460);

                        g.translate(2250, 690);
                        drawTriangles(g, black_up, black_up, black_right, black_right, 1, true);
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
                    } else {
                        // paint big shadow
                        GradientPaint rgp = new GradientPaint(1800, -1500, new Color(209, 213, 212), 1800, 200, Color.WHITE);
                        g.rotate(Math.toRadians(45));
                        g.setPaint(rgp);
                        g.fillRect(1250, -1500, 1120, 2000);
                        g.rotate(Math.toRadians(-45));

                        // remove extra part
                        g.setColor(Color.white);
                        g.fillRect(2480 - 50, 0, 50, 2000);

                        // draw triangles
                        g.translate(0, 0);
                        drawTriangles(g, orange_up, orange_down, orange_left, orange_right, 4, left_page);
                        g.translate(0, 0);

                        g.translate(0, 230);
                        drawTriangles(g, red_up, red_down, red_left, red_right, 3, left_page);
                        g.translate(0, -230);

                        g.translate(0, 460);
                        drawTriangles(g, blue_up, blue_down, blue_left, blue_right, 2, left_page);
                        g.translate(0, -460);

                        g.translate(0, 690);
                        drawTriangles(g, black_up, black_up, black_right, black_right, 1, left_page);
                        g.translate(0, -690);

                        // draw rolls
                        g.translate(0, 2200);
                        g.setPaint(orange_opac);
                        g.fillPolygon(rollX, rollY_right, 4);

                        g.translate(0, 200);
                        g.setPaint(red_opac);
                        g.fillPolygon(rollX, rollY_right, 4);

                        g.translate(0, 200);
                        g.setPaint(blue_opac);
                        g.fillPolygon(rollX, rollY_right, 4);

                        g.translate(0, 200);
                        g.setPaint(black_opac);
                        g.fillPolygon(rollX, rollY_right, 4);
                        g.translate(0, -2800);

                        // draw edge
                        g.setPaint(defaultc);
                        g.fillPolygon(edgeX_right, edgeY, 4);
                        g.setPaint(defaultc.darker());
                        g.fillPolygon(darkedgeX_right, darkedgeY, 3);
                    }
                }

                System.out.print("phase 2...");
                try {
                    // draw profile pic
                    String picUrl = obj.getString("pic");
                    if (picUrl.trim().length() > 0) {
                        URL url;
                        Image image = FileFetch.fetchImage(picUrl);
                        BufferedImage img_buf = new BufferedImage(660, 660, BufferedImage.TYPE_INT_RGB);
                        Graphics2D gg = img_buf.createGraphics();

                        double scale_w = 660.0 / image.getWidth(null);
                        double scale_h = 660.0 / image.getHeight(null);
                        double scale = Math.max(scale_h, scale_w);
                        gg.drawImage(image, -((int) (image.getWidth(null) * scale) - 660) / 2, -((int) (image.getHeight(null) * scale) - 660) / 2, (int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), null);

                        g.drawImage(img_buf, (left_page ? 50 : 1770), 0, null);
                    }
                } catch (Exception e) {
                }

                // draw profile details background
                g.setColor(defaultc);
                g.fillRect((left_page ? 50 : 1770), 660, 660, 260);

                // draw profile details
                g.setColor(Color.white);
                g.setFont(new Font("IRANSans", Font.BOLD, 45));

                try {
                    g.drawString("گرایش: " + obj.getString("major"), (left_page ? 700 : 2420) - g.getFontMetrics().stringWidth("گرایش: " + obj.getString("major")), 710);
                    addChars(obj.getString("major"));
                } catch (Exception e) {
                }
                try {
                    g.drawString("اهل: " + obj.getString("city"), (left_page ? 700 : 2420) - g.getFontMetrics().stringWidth("اهل: " + obj.getString("city")), 770);
                    addChars(obj.getString("city"));
                } catch (Exception e) {
                }
                try {
                    g.drawString("تاریخ تولد: " + toArabicNumber(obj.getString("birth")), (left_page ? 700 : 2420) - g.getFontMetrics().stringWidth("تاریخ تولد: " + toArabicNumber(obj.getString("birth"))), 830);
                    addChars(obj.getString("birth"));
                } catch (Exception e) {
                }

                g.setFont(new Font("Arial", Font.BOLD, 32));
                try {
                    g.drawString(obj.getString("email"), (left_page ? 380 : 2110) - g.getFontMetrics().stringWidth(obj.getString("email")) / 2, 880);
                    addChars(obj.getString("email"));
                } catch (Exception e) {
                }

                // draw name & id
                g.setColor(defaultc);
                Font font = new Font("IRANSans", Font.BOLD, (obj.getString("id").equals("9131009") || obj.getString("id").equals("9131045") ? 80 : 100));
                g.setFont(font);

                int x = (left_page ? 1153 : 1300) - g.getFontMetrics().stringWidth(obj.getString("name")) / 2;
                int y = 0;

                g.drawString(obj.getString("name"), x, y + g.getFontMetrics().getAscent());
                addChars(obj.getString("name"));

                g.setColor(defaultc.darker());
                g.fillRect(x - 30, y + g.getFontMetrics().getHeight() - 13, g.getFontMetrics().stringWidth(obj.getString("name")) + 70, 5);
                g.setColor(defaultc);

                x = (left_page ? 1153 : 1300) - g.getFontMetrics().stringWidth(toArabicNumber(obj.getString("id"))) / 2;
                y += g.getFontMetrics().getHeight();

                g.drawString(toArabicNumber(obj.getString("id")), x, y + g.getFontMetrics().getAscent() - 30);
                addChars(obj.getString("id"));

                // draw major logo
                if (SHOW_GRAPHICS) {
                    try {
                        BufferedImage logo = ImageIO.read(new File((majorid == 1 ? "softwareLogo.png" : (majorid == 2 ? "hardwareLogo.png" : "itLogo.png"))));
                        g.drawImage(logo, left_page ? 15 : 1760, 570, null);
                    } catch (IOException ex) {
                        Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // draw page pic
                int height_pic = 0;
                boolean havePic = false;
                int picOpt = 0;
                if (offset == 0) {
                    System.out.print("phase 3...");
                    try {
                        String picUrl = obj.getString("pagePic");
                        picOpt = obj.getInt("picopt");
                        if (picUrl.trim().length() > 0) {
                            havePic = true;
                            Image image = FileFetch.fetchImage(picUrl);

                            BufferedImage img_buf;
                            int mid;
                            switch (picOpt) {
                                case 1:
                                    height_pic = (int) (750 * ((double) image.getHeight(null) / (double) image.getWidth(null)));
                                    mid = (left_page ? 780 + 375 : 50 + 1295);
                                    img_buf = new BufferedImage(750, height_pic, BufferedImage.TYPE_INT_ARGB);
                                    break;
                                case 2:
                                    height_pic = (int) (1570 * ((double) image.getHeight(null) / (double) image.getWidth(null)));
                                    mid = (left_page ? 780 + 785 : 50 + 885);
                                    img_buf = new BufferedImage(1570, height_pic, BufferedImage.TYPE_INT_ARGB);
                                    break;
                                default:
                                    height_pic = 700;
                                    mid = (left_page ? 780 + 785 : 50 + 885);
                                    img_buf = new BufferedImage(1570, 700, BufferedImage.TYPE_INT_ARGB);
                                    break;
                            }

                            Graphics2D gg = img_buf.createGraphics();

                            double scale_w = (float) img_buf.getWidth() / image.getWidth(null);
                            double scale_h = (float) img_buf.getHeight() / image.getHeight(null);
                            double scale = Math.min(scale_h, scale_w);
                            gg.drawImage(image, -((int) (image.getWidth(null) * scale) - img_buf.getWidth()) / 2, -((int) (image.getHeight(null) * scale) - img_buf.getHeight()) / 2, (int) (image.getWidth(null) * scale), (int) (image.getHeight(null) * scale), null);

                            g.drawImage(img_buf, mid - img_buf.getWidth() / 2, 3450 - height_pic, null);
                        }
                    } catch (Exception e) {
                    }
                }
                // draw short answers 
                g.setFont(new Font("B Titr", Font.BOLD, 50));
                g.setColor(Color.BLACK);
                g.drawString("کوتاه پاسخ ها", (left_page ? 700 : 2430) - g.getFontMetrics().stringWidth("کوتاه پاسخ ها"), 990);

                g.setFont(new Font("B Lotus", 0, 40));
                String strShortA = "";
                String short_ans_rem = "[Empty]";
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
                        strShortA += shortAs.get(i).str + " \n ";
                    }
                    short_ans_rem = "[Size = " + shortAs.size() + "]";
                    int yu = drawString(g, strShortA, (left_page ? 50 : 1780), 1020, 650, 2400, 4000, 0, 0, true);
                    if (yu > 4000) {
                        short_ans_rem += "{Error: " + (yu - 4000) + "pixels are out of page! (about " + ((yu - 4000) / g.getFontMetrics().getHeight()) + " line(s))";

                    }
                }

                int y_main = 500;
                int shift_x = 0;
                // draw long answers
                y_main += g.getFontMetrics().getHeight() * 2 + 25;
                // draw tarin boxes
                if (obj.has("tarin")) {
                    JSONObject tarin = obj.getJSONObject("tarin");
                    g.setColor(Color.WHITE);
                    int start_x = (left_page ? 750 : 670);
                    int end_x = 1850;
                    int xi = start_x;
                    int yi = 280;

                    g.setFont(new Font("IRANSans", Font.BOLD, 30));
                    ArrayList<OrderedString> tarins = new ArrayList<>();
                    for (int i = 0; i < tarin.keySet().size(); i++) {
                        String best = (String) tarin.keySet().toArray()[i];
                        int place = (int) tarin.get(best);
                        tarins.add(new OrderedString(place, best));
                    }
                    tarins.sort(new Comparator<OrderedString>() {
                        @Override
                        public int compare(OrderedString o1, OrderedString o2) {
                            if (o1.prior != o2.prior) {
                                return o1.prior - o2.prior;
                            } else {
                                Font f = new Font("IRANSans", Font.BOLD, 30);
                                return g.getFontMetrics(f).stringWidth(o1.str) - g.getFontMetrics(f).stringWidth(o2.str);
                            }
                        }
                    });

                    for (int i = 0; i < tarins.size(); i++) {
                        String best = tarins.get(i).str;
                        int place = tarins.get(i).prior;

                        switch (i % 4) {
                            case 0:
                                g.setColor(orange_right);
                                break;
                            case 1:
                                g.setColor(red_right);
                                break;
                            case 2:
                                g.setColor(blue_right);
                                break;
                            case 3:
                                g.setColor(black_right);
                                break;
                        }
                        if (xi + g.getFontMetrics().stringWidth(best) + 45 > (left_page ? end_x : 1765)) {
                            if (left_page == false) {
                                start_x -= 105;
                            } else {
                                end_x += 105;
                            }
                            xi = start_x;
                            yi += 105;
                        }
                        g.fillRoundRect(xi, yi, g.getFontMetrics().stringWidth(best) + 45, g.getFontMetrics().getHeight(), 30, 30);

                        if (SHOW_GRAPHICS) {
                            try {
                                BufferedImage logo = ImageIO.read(new File((place == 1 ? "gold.png" : (place == 2 ? "silver.png" : (place == 3 ? "bronze.png" : "forth.png")))));
                                g.drawImage(logo, xi - 35, yi - 10, 70, 110, null);
                                g.setColor(Color.WHITE);
                                g.drawString(toArabicNumber(place + ""), (int) (xi - (g.getFontMetrics().stringWidth(toArabicNumber(place + "")) / 2.0)), yi + 32);
                                g.drawString(best, xi + 40, yi + 35);
                            } catch (IOException ex) {
                                Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                        xi += g.getFontMetrics().stringWidth(best) + 85;
                    }
                    if (xi > (left_page ? 2030 : 1610)) {
                        yi += 105;
                    } else {
                        yi += 50;
                    }

                    if (yi > y_main) {
                        shift_x += yi - y_main;
                        y_main = yi;
                    }
                }
                g.setColor(Color.BLACK);

                String longA = obj.getString("longA");
                if (longA.trim().length() > 0) {
                    addChars(longA);

                    g.setFont(new Font("B Titr", Font.BOLD, 50));
                    g.drawString("خاطره",
                            (left_page ? 2170 - g.getFontMetrics().stringWidth("خاطره") + shift_x : 1750 - g.getFontMetrics().stringWidth("خاطره")),
                            y_main + g.getFontMetrics().getAscent());

                    y_main += g.getFontMetrics().getHeight() + 10;

                    g.setFont(new Font("B Lotus", 0, 40));
                    if (left_page) {
                        y_main = drawString(g, longA, 780, y_main, 1560, 500, y_main, 120 - shift_x, 60 - shift_x, true);
                    } else {
                        y_main = drawString(g, longA, 140, y_main, 1610, 500, y_main, 120 - shift_x, 60 - shift_x, false);
                    }
                    y_main += g.getFontMetrics().getHeight() + 10;
                }
                y_main = Math.max(870, y_main);
                // draw diaries

                int max_y = 3508;
                boolean draw_qoute = false;
                g.setColor(Color.BLACK);
                int yy = y_main;
                int remaining_offset = -1;
                if (obj.has("khatere")) {
                    JSONObject array = obj.getJSONObject("khatere");
                    xxx = (left_page ? 1600 : 1000);

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

                    int i = 0;
                    khatereloop:
                    for (; i < khaterat.size(); i++) {
                        if (i < offset) {
                            continue;
                        }
                        String key = khaterat.get(i).key;
                        String ans = khaterat.get(i).str;

                        if (yy > 3400) {
                            yy = y_main;
                            xxx -= 820;
                        }
                        if (havePic) {
                            switch (picOpt) {
                                case 1:
                                    if ((left_page ? xxx < 800 : xxx > 800) && yy > 3450 - height_pic - 100) {
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
                        if (xxx < (left_page ? 620 : 0)) {
                            break;
                        }
                        if ((((left_page ? xxx < 800 : xxx > 800) && yy > 1400)) && !draw_qoute && offset == 0) {
                            draw_qoute = true;
                            // draw qoute
                            String qoute = obj.getString("qoute");
                            if (qoute.trim().length() > 0) {
                                for (int f = 80; f > 35; f--) {
                                    g.setFont(new Font("IRANSans", Font.BOLD, f));
                                    if (stringNoSpaceWidth(g, "”" + qoute + "“", g.getFontMetrics().stringWidth(" ")) <= 750) {
                                        break;
                                    }
                                }

                                g.setColor(Color.DARK_GRAY);
                                int ytmp = 0;
                                addChars("“" + qoute + "”");
                                int wide = Math.min(740, stringNoSpaceWidth(g, "”" + qoute + "“", g.getFontMetrics().stringWidth(" ")));
                                ytmp = drawString(g, "”" + qoute + "“", xxx + (750 - wide) / 2, yy + 15, wide, 3400, 5000, 0, 0, true, true);
                                g.setColor(defaultc);
                                g.fillRect(xxx + 375 - wide / 2,
                                        yy - 5,
                                        wide,
                                        10);
                                if (ytmp != 0) {
                                    yy = ytmp;
                                }
                                g.fillRect(xxx + 375 - wide / 2,
                                        yy - 5,
                                        wide,
                                        10);
                                yy += 30;
                                g.setColor(Color.BLACK);
                            }
                            i--;
                            continue;
                        }
                        if ((left_page ? xxx < 800 : xxx > 800) && yy > max_y - 100) {
                            break;
                        }

                        int name_x = xxx + 750 - g.getFontMetrics().stringWidth(key + ":");
                        int name_y = yy + g.getFontMetrics().getAscent();

                        yy += g.getFontMetrics().getHeight();

                        g.setFont(new Font("B Lotus", 0, 40));
                        if (havePic) {
                            switch (picOpt) {
                                case 1:
                                    if ((left_page ? xxx < 800 : xxx > 800)) {
                                        if (xxx < 800 && yy + stringHeight(g, ans, xxx, yy, 750, 3450 - height_pic - 100 - yy, y_main, 20, 0, true) > 3450 - height_pic - 100) {
                                            break khatereloop;
                                        }
                                        yy = drawString(g, ans, xxx, yy, 750, 3450 - height_pic - 100 - yy, y_main, 20, 0, true) + g.getFontMetrics().getHeight() / 2;
                                    } else {
                                        if (xxx < 800 && yy + stringHeight(g, ans, xxx, yy, 750, 3400 - yy, y_main, 20, 0, true) > 3400) {
                                            break khatereloop;
                                        }
                                        yy = drawString(g, ans, xxx, yy, 750, 3400 - yy, y_main, 20, 0, true) + g.getFontMetrics().getHeight() / 2;
                                    }
                                    break;
                                default:
                                    if (xxx < 800 && yy + stringHeight(g, ans, xxx, yy, 750, 3450 - height_pic - 100 - yy, y_main, 20, 0, true) > 3450 - height_pic - 100) {
                                        break khatereloop;
                                    }
                                    yy = drawString(g, ans, xxx, yy, 750, 3450 - height_pic - 100 - yy, y_main, 20, 0, true) + g.getFontMetrics().getHeight() / 2;
                                    break;
                            }
                        } else {
                            if (xxx < 800 && yy + stringHeight(g, ans, xxx, yy, 750, 3400 - yy, y_main, 20, 0, true) > 3400) {
                                break;
                            }
                            yy = drawString(g, ans, xxx, yy, 750, 3400 - yy, y_main, 20, 0, true) + g.getFontMetrics().getHeight() / 2;
                        }
                        g.setFont(new Font("B Lotus", Font.BOLD, 40));
                        g.drawString(key + ":", name_x, name_y);
                    }
                    System.out.println(i + " " + khaterat.size());
                    if (i < khaterat.size()) {
                        remaining_offset = i;
                    }
                }
                if (!draw_qoute && offset == 0) {
                    yy = Math.max(yy, 1500);
                    if (xxx > 800) {
                        xxx -= 820;
                        yy = 1500;
                    }
                    draw_qoute = true;
                    // draw qoute
                    String qoute = obj.getString("qoute");
                    if (qoute.trim().length() > 0) {
                        for (int f = 80; f > 35; f--) {
                            g.setFont(new Font("IRANSans", Font.BOLD, f));
                            if (stringNoSpaceWidth(g, "”" + qoute + "“", g.getFontMetrics().stringWidth(" ")) <= 750) {
                                break;
                            }
                        }

                        g.setColor(Color.DARK_GRAY);
                        int ytmp = 0;
                        addChars("“" + qoute + "”");
                        int wide = Math.min(740, stringNoSpaceWidth(g, "”" + qoute + "“", g.getFontMetrics().stringWidth(" ")));
                        ytmp = drawString(g, "”" + qoute + "“", xxx + (750 - wide) / 2, yy + 15, wide, 3400, 5000, 0, 0, true, true);
                        g.setColor(defaultc);
                        g.fillRect(xxx + 375 - wide / 2,
                                yy - 5,
                                wide,
                                10);
                        if (ytmp != 0) {
                            yy = ytmp;
                        }
                        g.fillRect(xxx + 375 - wide / 2,
                                yy - 5,
                                wide,
                                10);
                        yy += 30;
                        g.setColor(Color.BLACK);
                    }
                }
                System.out.print("phase 4...");
                try {
                    ImageIO.write(buf, "jpg", new File("pages/" + user_id + "_" + Math.abs((user_id * 11) % 9999) + "_" + offset + (left_page ? "-left" : "-right") + ".jpg"));
                } catch (IOException ex) {
                    Logger.getLogger(PageFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("done " + user_id);
                completed = true;
                System.out.println("offset:" + remaining_offset);
                if (remaining_offset >= 0) {
                    PageFrame f = new PageFrame(id, chars, !left_page, remaining_offset);
                    PageFrame.this.left_page = f.left_page;
                }
                //g2d.drawImage(buf, 0, 0, getWidth(), getHeight(), null);
                //PageFrame.this.setVisible(false);
                //PageFrame.this.dispose();
                return short_ans_rem;
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
