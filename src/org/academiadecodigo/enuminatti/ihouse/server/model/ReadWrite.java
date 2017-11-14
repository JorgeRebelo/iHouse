package org.academiadecodigo.enuminatti.ihouse.server.model;

import java.io.*;

/**
 * Created by codecadet on 14/11/17.
 */
public class ReadWrite {

    public void write(String content, String filepath) {

        FileWriter fileWriter;

        try {
            fileWriter = new FileWriter(filepath);

            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(content);

            bufferedWriter.flush();

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String content) {

        FileReader reader = null;

        try {

            reader = new FileReader(content);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader bufferedReader = new BufferedReader(reader);

        String result = null;

        try {

            result = bufferedReader.readLine();
            bufferedReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}

