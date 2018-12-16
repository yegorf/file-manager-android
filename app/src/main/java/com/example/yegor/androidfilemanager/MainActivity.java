package com.example.yegor.androidfilemanager;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String currFile = null;
    private ArrayAdapter<String> adapter;
    private List<File> items;
    private GestureDetectorCompat gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private int positio = -1;
    private ArrayList<String> copyList = new ArrayList<>();
    private ArrayList<String> copyNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        currFile = new File("").getAbsolutePath();

        FingerController fingerController = new FingerController();
        fingerController.setActivity(this);
        gestureDetector = new GestureDetectorCompat(this, fingerController);

        SeparationController separationController = new SeparationController(this);
        scaleGestureDetector = new ScaleGestureDetector(this, separationController);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positio = position;
               // parent.getChildAt(position).setBackgroundColor(Color.GREEN);
                view.setBackgroundColor(Color.GREEN);
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                scaleGestureDetector.onTouchEvent(event);
                return false;
            }
        });

        showList();
    }

    public void open() {
        for(int i=0; i<items.size(); i++) {

            if(i == positio) {
                if(items.get(i).canRead()) {
                    currFile = currFile + "/" + items.get(i).getName();
                    Log.w("kek",items.get(i).getName());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ошибка открытия!", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        try {
            showList();
        } catch (Exception e) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ошибка открытия!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void close() {
        if (!currFile.equals("/")) {
            File file = new File(currFile);
            System.out.println(file.getParent());
            currFile = file.getParent();
            showList();
        }
    }

    public void showList() {
        items = getFileList(currFile);
        List<String> list = new ArrayList<>();

        for(File f : items) {
            list.add(f.getName());
        }
        adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
    }

    public ArrayList<File> getFileList(String name){
        ArrayList<File> files = new ArrayList<>();
        File f = new File(name);
        if (f.canRead()) {
            for (File file : f.listFiles()) {
                files.add(file);
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ошибка открытия!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return files;
    }


    public void copy() {
        for(int i=0; i<items.size(); i++) {
            if(i == positio) {
                items.get(i).getName();
                copyList.add(items.get(i).getAbsolutePath());
                copyNameList.add(items.get(i).getName());
            }
        }
    }


    private FileScanner fileScanner = new FileScanner();
    public void paste() throws IOException {
        String dop = null;
        for(int i=0; i<items.size(); i++) {
            if (i == positio) {
                dop = items.get(i).getAbsolutePath(); //Tochno
            }
        }

        String file = copyList.get(0);

        String newPathForCopy = dop + "/" + copyNameList.get(0);
        Log.w("kek", "PASTE " + newPathForCopy);


        if (fileScanner.copyFileOrDirectory(file, newPathForCopy)) {
            Toast.makeText(MainActivity.this, "Скопировано", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Ошибка копирования", Toast.LENGTH_SHORT).show();
        }
        showList();
    }
}

