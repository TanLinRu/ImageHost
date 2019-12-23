package com.tlq.Stream;

import java.io.*;

/**
 * @Description: 字符流学习
 * @Author: TanLinQuan
 * @Date: 2019/12/22 23:58
 * @Version: V1.0
 **/
public class CharStreamStudy {

    public static void main(String[] args) {
        int c;
        try (
                FileReader fileReader = new FileReader(new File("D:/ImageHost/Java_detail/IO/StreamStudy.txt"));
                FileWriter fileWriter = new FileWriter(new File("D:/ImageHost/Java_detail/IO/OutStreamStudy.txt"));
                ){
            while ((c = fileReader.read()) != -1){
                char c1 = (char) c;
                System.out.println(c1);
                fileWriter.append(c1);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
