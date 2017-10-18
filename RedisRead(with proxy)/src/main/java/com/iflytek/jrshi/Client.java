package com.iflytek.jrshi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jrshi on 17/6/3.
 */
public class Client {
    public static final List<String> logType = new ArrayList<String>();
    public static void main(String[]args){
        logType.add("log.error");
        logType.add("log.warn");
        logType.add("log.info");
        logType.add("log.debug");
        logType.add("logger.error");
        logType.add("logger.warn");
        logType.add("logger.info");
        logType.add("logger.debug");

        String strPath = "/Users/jrshi/Documents/mobile/dev/debug/GNOME_CM3/gnome-share/src/main/java/com/iflytek/gnome/share";
        getFileList(strPath);

    }

    public static void readFileByLines(String fileName) {


        File file = new File(fileName);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;

        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            boolean flag = false;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                flag = false;
                // 显示行号
                for(String logtypeImp : logType){
                    if(tempString.contains(logtypeImp)&&tempString.contains("+")){
                        int num = stringNumbers(tempString,0);
                        System.out.println("num=" + num + "line " + line + "原日志: " + tempString);
                        StringBuilder sb = new StringBuilder();
                        sb.append(logtypeImp);
                        sb.append("(\"{} ");
                        for(int index=0 ; index< num ;index++){
                            sb.append("-> {}");
                        }
                        sb.append("\" ,");
                        tempString = tempString.replace(logtypeImp+"(", sb.toString());
                        tempString = tempString.replaceAll("\\+", ",");

                        System.out.println("line " + line + "替换后: " + tempString);

                        stringBuilder.append(tempString + "\r\n");
                        flag = true;
                        break;
                    }else {
                        continue;
                    }
                }
                if(!flag){
                    stringBuilder.append(tempString + "\r\n");
                }

                line++;
            }
            reader.close();
//            writer.flush();
//            writer.close();
//            if (flag) {
//                file.delete();
//                tmpfile.renameTo(new File(file.getAbsolutePath()));
//            } else{
//                tmpfile.delete();
//            }

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
        File tmpfile = new File(file.getPath());
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(tmpfile));
            writer.write(stringBuilder.toString());
            writer.flush();
            writer.close();
        }catch (Exception e){
            System.out.println(e.getStackTrace());
        }finally {

        }

    }

    public static int stringNumbers(String str,int counter) {
        if (str.indexOf("+")==-1) {
            return counter;
        } else {
            counter++;
            System.out.println("count = " + counter + "   ," + str.substring(str.indexOf("+") + 1));
            return stringNumbers(str.substring(str.indexOf("+") + 1), counter);
        }
    }

    public static List<File> getFileList(String strPath) {
        List<File> filelist = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName();
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                }  else {
                    filelist.add(files[i]);
                    System.out.println(files[i].getPath());
                    readFileByLines(files[i].getPath());
                    continue;
                }
            }
        }
        return filelist;
    }
}
