/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package magazinecreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Saleh
 */
public class NewClass {

    public static void main(String[] args) {
        try {
            URL google = new URL("http://mokhi.ir/jashn91/test.php?dontclose");
            BufferedReader in = new BufferedReader(new InputStreamReader(google.openStream()));
            String inputLine;

            String res = "";
            while ((inputLine = in.readLine()) != null) {
                res += inputLine + ' ';
            }
            in.close();
            
            HashMap<String,Integer> strs = new HashMap<>();
            String[] arr = res.split(" ");
            for ( int i = 0 ; i < arr.length ; i ++ )
            {
                if ( arr[i].trim().length() > 0 )
                {
                    if ( strs.containsKey(arr[i].trim()) )
                    {
                        strs.put(arr[i].trim(), strs.get(arr[i].trim()) + 1);
                    }
                    else
                    {
                        strs.put(arr[i].trim(), 1);
                    }
                }
            }
            ArrayList<String> arrs = new ArrayList<>();
            for ( String key : strs.keySet() )
            {
                arrs.add(key);
            }
            arrs.sort(new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return strs.get(o2) - strs.get(o1);
                }
            });
            
            FileOutputStream file = new FileOutputStream(new File("salam.txt"));
            for ( int i = 0 ; i < arrs.size() ; i ++ ){
                file.write((strs.get(arrs.get(i)) + "  " + arrs.get(i) + "\n").getBytes());
            }
            file.close();
        } catch (MalformedURLException me) {
//                System.out.println(me);
        } catch (IOException ioe) {
//                System.out.println(ioe);
        }

    }
}
