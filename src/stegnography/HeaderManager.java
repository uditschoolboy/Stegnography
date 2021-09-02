package stegnography;

import java.io.File;

class HeaderManager {
    final static int HEADER_LENGTH = 25;
    static String formHeader(String filename, long filesize)
    {
        int forsize = 8, forsep = 1;
        int forname = HEADER_LENGTH - forsize - forsep;
        String fs = String.valueOf(filesize);
        while(fs.length() < forsize) fs = "#" + fs;


        File f = new File(filename);
        String fn = f.getName();
        if(fn.length() > forname)
        {
            int start = fn.length() - forname;
            fn = fn.substring(start);
        }
        else
        {
            while(fn.length() < forname) fn = "#" + fn;
        }
        return fn + "~" + fs;
    }
    static int getFileSize(String header)
    {
        int forsize = 8, forsep = 1;
        String temp = header.substring(HEADER_LENGTH - forsize);
        temp = temp.replaceAll("#", " ");
        temp = temp.trim();
        return Integer.parseInt(temp);
    }
    static String getFileName(String header)
    {
        int forsize = 8, forsep = 1;
        int forname = HEADER_LENGTH - forsize - forsep;
        String temp = header.substring(0, forname);
        temp = temp.replaceAll("#", " ");
        temp = temp.trim();
        if(temp.startsWith(".")) temp = "untitled" + temp;
        return temp;
    }
}
