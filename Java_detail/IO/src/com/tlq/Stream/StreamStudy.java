package com.tlq.Stream;

import java.io.*;

/**
 * @Description: 字节流学习
 * @Author: TanLinQuan
 * @Date: 2019/12/22 19:49
 * @Version: V1.0
 **/
public class StreamStudy {


    public static void main(String[] args) {
        int c;
        try(
                FileInputStream fileInputStream = new FileInputStream(new File("D:/ImageHost/Java_detail/IO/StreamStudy.txt"));
                FileOutputStream fileOutputStream = new FileOutputStream(new File("D:/ImageHost/Java_detail/IO/OutStreamStudy.txt"));
        )
        {
            while ((c = fileInputStream.read() )!= -1){
                char c1 = (char) c;
                System.out.println(c1);
                fileOutputStream.write(c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
