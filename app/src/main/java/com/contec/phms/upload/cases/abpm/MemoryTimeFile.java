package com.contec.phms.upload.cases.abpm;

import com.alibaba.cchannel.push.receiver.CPushMessageCodec;
import com.contec.phms.util.FileOperation;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class MemoryTimeFile {
    public static String readMemoryTime(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuffer sb = new StringBuffer();
        for (String temp = br.readLine(); temp != null; temp = br.readLine()) {
            sb.append(String.valueOf(temp) + " ");
        }
        br.close();
        return sb.toString();
    }

    public static void memoryTime(String path, String timeInfo, boolean isContinue) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            FileOperation.createFile(path);
        }
        FileOutputStream out = new FileOutputStream(file, isContinue);
        StringBuffer sb = new StringBuffer();
        sb.append(timeInfo);
        out.write(sb.toString().getBytes(CPushMessageCodec.UTF8));
        out.close();
    }
}
