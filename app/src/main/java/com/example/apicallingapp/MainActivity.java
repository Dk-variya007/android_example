package com.example.apicallingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String SHARED_PREFS = "shared_prefs";
    static final String NAME_LIST_KEY = "name_list";

    ArrayList<NameModel> nameList;
    RecyclerNameAdapter adapter;
    SharedPreferences sharedPreferences;
    Button pdf,apiBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI Components
        RecyclerView recyclerView = findViewById(R.id.recyclerId);
        EditText inputName = findViewById(R.id.inputName);
        Button addButton = findViewById(R.id.addButton);
        pdf = findViewById(R.id.pdfButton);
        apiBtn=findViewById(R.id.apiButton);

        apiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PostActivity.class);
                startActivity(i);
            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, PdfGenActivity.class);
                startActivity(i);
            }
        });
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        // Load data from SharedPreferences
        nameList = loadNameList();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerNameAdapter(this, nameList);
        recyclerView.setAdapter(adapter);

        // Add Button Click Listener to Add a New Name
        addButton.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (!name.isEmpty()) {
                nameList.add(new NameModel(name));
                adapter.notifyItemInserted(nameList.size() - 1);
                recyclerView.scrollToPosition(nameList.size() - 1);
                inputName.setText(""); // Clear input field

                // Save updated list to SharedPreferences
                saveNameList(nameList);

                Toast.makeText(this, "Name added and saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter a name!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<NameModel> loadNameList() {
        String json = sharedPreferences.getString(NAME_LIST_KEY, null);
        if (json != null) {
            Type type = new TypeToken<ArrayList<NameModel>>() {
            }.getType();
            return new Gson().fromJson(json, type);
        }
        return new ArrayList<>();
    }

    private void saveNameList(ArrayList<NameModel> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(NAME_LIST_KEY, json);
        editor.apply();
    }
}
