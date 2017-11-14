package org.academiadecodigo.enuminatti.ihouse.server.model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by codecadet on 14/11/17.
 */
public class ReadWrite {

    public void write(String content, String filepath) {

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

        try {
            bufferedWriter.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
