package com.iflytek.jrshi.ViewRedis;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrshi on 17/10/9.
 */
public class ReadKey {
    public static List<String> readFileByLines(String fileName) {

        List<String> list = new ArrayList();

        File file = new File(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;

        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean flag = false;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {


                    list.add(tempString);
//                    System.out.println("pnumber = " + tempString);


            }
            reader.close();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
       return list;

    }
}
