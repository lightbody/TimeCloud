package net.lightbody.timecloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class IOUtils {
    private static final int BUFFER = 4096;

    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }

        out.close();
        in.close();
    }

    public static void nonClosingCopy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
    }

    public static String readFully(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, length, "UTF-8"));
        }

        in.close();

        return sb.toString();
    }

    public static String readFully(InputStreamReader in) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] buffer = new char[BUFFER];
        int length;
        while ((length = in.read(buffer)) != -1) {
            sb.append(new String(buffer, 0, length));
        }

        in.close();

        return sb.toString();
    }
}
