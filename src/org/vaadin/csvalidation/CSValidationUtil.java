package org.vaadin.csvalidation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility functions.
 * 
 * @author magi
 */
public class CSValidationUtil {
    /**
     * Just for reading a damn file from a package using class loader.
     * 
     * @param classloader a class loader to use for loading the file
     * @param filename the file name. Package path should be given with slashes.
     * @return the contents of the file
     **/
    public static String readFile(ClassLoader classloader, String filename) {
        InputStream istream = classloader.getResourceAsStream(filename);
        if (istream == null)
            return null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(istream));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                istream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return sb.toString();
    }
}
