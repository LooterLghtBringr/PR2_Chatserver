package com.company;

import java.io.*;

public class Logger {

    private String path;
    FileWriter pw;
    File f;

    public Logger(String path) {
        this.path = path;
        f = new File(path);
    }

    public void writeLogEntry(String entry)
    {
        try {
            pw = new FileWriter(f,true);
            pw.write(entry);
            pw.write("\n");
            pw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
