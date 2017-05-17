package com.streaming.download;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.Vector;

import okhttp3.ResponseBody;

public class Utils
{
    public static String md5_digest(byte[] array) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        }
        catch (Throwable e)
        {
        }
        messageDigest.update(array, 0, array.length);
        String bigInteger = new BigInteger(1, messageDigest.digest()).toString(16);
        while (bigInteger.length() < 32)
        {
            bigInteger = "0" + bigInteger;
        }
        return bigInteger;
    }

    public static boolean writeResponseBodyToDisk(String filename, ResponseBody body, boolean isSegment)
    {
        try {
            // todo change the file location/name according to your needs
            File directory = new File(Environment.getExternalStorageDirectory(),Constant.folderName);
            if(!directory.exists())
                directory.mkdir();
            File tmp_folder = new File(directory,Constant.folderTemp);
            if(!tmp_folder.exists())
                tmp_folder.mkdir();

            File downFile;
            if(isSegment)
                downFile = new File(tmp_folder,filename);
            else
                downFile = new File(directory,filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                int read;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(downFile);

                while ((read = inputStream.read(fileReader)) != -1)
                {
                    outputStream.write(fileReader, 0, read);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean mergeFiles(int total, String name)
    {
        File baseDir = new File(Environment.getExternalStorageDirectory(),Constant.folderName);
        Vector<InputStream> v = new Vector<>(total);

        for(int i=1;i<=total;i++)
        {
            try
            {
                v.add(new FileInputStream(new File(baseDir,Constant.folderTemp+File.separator+i)));
            }
            catch (FileNotFoundException ex)
            {
                ex.printStackTrace();
                return false;
            }
        }

        Enumeration<InputStream> e = v.elements();
        SequenceInputStream sequenceInputStream = new SequenceInputStream(e);
        FileOutputStream fout=null;
        try
        {
            fout= new FileOutputStream(new File(baseDir,name));
            byte[] buf = new byte[10240];
            int len;
            while ((len = sequenceInputStream.read(buf)) > 0)
            {
                fout.write(buf, 0, len);
            }
            fout.flush();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return false;
        }
        finally
        {
            for(int i=0;i<v.size();i++)
            {
                try
                {
                    v.get(i).close();
                } catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            try
            {
                if(fout!=null)
                    fout.close();
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }

        return true;
    }
}
