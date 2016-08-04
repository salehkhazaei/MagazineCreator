/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magazinecreator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import static magazinecreator.PageFrame.read;
import org.json.JSONObject;

/**
 *
 * @author Saleh
 */
public class MagazineCreator {

    public static void handleReq(int id, String key, boolean left, String er) throws MalformedURLException, IOException {
        String url = "http://mokhi.ir/jashn91/majale_response.php?dontclose";
        String charset = "UTF-8";
        File binaryFile = new File("pages/" + id + "_" + Math.abs((id * 11) % 9999) + (left ? "-left" : "-right") + ".jpg");
        String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
        String CRLF = "\r\n"; // Line separator required by multipart/form-data.

        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        try (
                OutputStream output = connection.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);) {
            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"" + (left ? "left" : "right") + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(key).append(CRLF).flush();

            // Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"error\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(er).append(CRLF).flush();

            // Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(binaryFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        } catch (IOException ex) {
            Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Request is lazily fired whenever you need to obtain information about response.
        int responseCode = ((HttpURLConnection) connection).getResponseCode();
        System.out.println(responseCode); // Should be 200
        System.out.println(((HttpURLConnection) connection).getResponseMessage());
    }

    public static void main(String[] args) {
        int mode = 2;
        HashSet<Character> chars = new HashSet<>();
        if (mode == 1) {
            int count = 0;
            int sleep = 1;
            try {
                Scanner sc = new Scanner(new File("reqs.txt"));
                count = sc.nextInt();
            } catch (Exception ex) {
                Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Start");
            while (true) {
                String updateStr = read("http://mokhi.ir/jashn91/majale_request.php?dontclose");
                if (updateStr.trim().length() != 0 && !updateStr.trim().equals("[]")) {
                    System.out.println(updateStr);
                    JSONObject obj = new JSONObject(updateStr);
                    sleep = 5;
                    for (int i = 0; i < obj.keySet().size(); i++) {
                        try {
                            String key = (String) obj.keySet().toArray()[i];
                            String ans = obj.getString(key);
                            int id = Integer.parseInt(ans);
                            count++;
                            System.out.println("handling request [" + count + "]: " + id);
                            PageFrame frame = new PageFrame(id, chars, false, 0);
                            frame = new PageFrame(id, chars, true, 0);
                            handleReq(id, key, false, frame.error);
                            handleReq(id, key, true, frame.error);
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    FileOutputStream fos;
                    try {
                        fos = new FileOutputStream(new File("reqs.txt"));
                        fos.write((count + "").getBytes());
                        fos.flush();
                        fos.close();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                sleep = Math.min(10, sleep + 5);
                System.out.println("Sleep [" + sleep + "]");
                try {
                    Thread.sleep(sleep * 1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else if (mode == 2) {
            {
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(new File("chars.txt"));
                    for (int i = 1500; i < 1800; i++) {
                        fos.write(("\tcase " + i + ": // '" + (char) i + "'\n").getBytes());
                    }
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            PageFrame frame;
            boolean left_page = false;
            frame = new PageFrame(9113026, chars, left_page, 0);
            left_page = frame.left_page;
            frame = null;
            for (int i = 9131001; i < 9131090; i++) {
                if (i == 9131014) {
                    continue;
                }
                left_page = !left_page;
                frame = new PageFrame(i, chars, left_page, 0);
                left_page = frame.left_page;
                if (!frame.completed) {
                    left_page = !left_page;
                }
                frame = null;
            }
            left_page = !left_page;
            frame = new PageFrame(9131801, chars, left_page, 0);
            left_page = frame.left_page;
            frame = null;
            for (int i = 9131901; i < 9131911; i++) {
                if (i == 9131902) {
                    continue;
                }
                left_page = !left_page;
                frame = new PageFrame(i, chars, left_page, 0);
                left_page = frame.left_page;
                if (!frame.completed) {
                    left_page = !left_page;
                }
                frame = null;
            }
            left_page = !left_page;
            frame = new PageFrame(9133093, chars, left_page, 0);
            left_page = frame.left_page;
            frame = null;
            left_page = !left_page;
            frame = new PageFrame(9231513, chars, left_page, 0);
            left_page = frame.left_page;
            frame = null;
            left_page = !left_page;
            frame = new PageFrame(9131014, chars, left_page, 0);
            left_page = frame.left_page;
            frame = null;
            left_page = !left_page;
            frame = new PageFrame(9131902, chars, left_page, 0);
            left_page = frame.left_page;
            frame = null;
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(new File("chars.txt"));
                for (Character c : chars.toArray(new Character[chars.size()])) {
                    fos.write(("\tcase " + (int) c + ": // '" + c + "'\n").getBytes());
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MagazineCreator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
