package com.funbox.ecommerce.funbox;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Наиль on 28.07.2015.
 */
public class WorkCSVFiles {

    InputStream inputStream;
    Context context;

    public WorkCSVFiles(InputStream inputStream) {
        this.inputStream = inputStream;
        this.context = context;
    }


    public ArrayList<FieldCsv> readFile() throws FileNotFoundException {
        ArrayList<FieldCsv> csvArrayList = new ArrayList<FieldCsv>();
        String line = "";
        File dir = Environment.getExternalStorageDirectory();
        File f = new File(dir, "/data.csv");
        Log.d("test", "testL: " + f.toString());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(f));
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String[] row = line.split(",");
                int count = Integer.parseInt(row[2].replace("\"", "").replace(" ", ""));
                // проверим у товара количество больше 0, если да то товар можно добавить в тип данных и пользователь сможет купить его
                if (count > 0) {
                    csvArrayList.add(new FieldCsv(row[0].replace("\"", ""), row[1].replace("\"", "").replace(" ", ""), count));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvArrayList;
    }

    public void saveFile(ArrayList<FieldCsv> list) {
        File dir = Environment.getExternalStorageDirectory();
        File f = new File(dir, "/data.csv");

        try {
            FileWriter writer = writer = new FileWriter(f);
            for (int i = 0; i < list.size(); i++) {
                writer.append("\"" + list.get(i).getTitle() + "\"");
                writer.append(", ");
                writer.append("\"" + list.get(i).getPrice() + "\"");
                writer.append(", ");
                writer.append("\"" + Integer.toString(list.get(i).getNumber()) + "\"");
                writer.append('\n');
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
