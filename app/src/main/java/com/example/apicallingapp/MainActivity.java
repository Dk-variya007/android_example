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

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final String SHARED_PREFS = "shared_prefs";
    static final String NAME_LIST_KEY = "name_list";


    private DrawerLayout drawerLayout;

    ArrayList<NameModel> nameList;
    RecyclerNameAdapter adapter;
    SharedPreferences sharedPreferences;
    Button pdf, apiBtn, webBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up DrawerLayout
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Handle NavigationView item clicks
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        // Initialize UI Components
        RecyclerView recyclerView = findViewById(R.id.recyclerId);
        EditText inputName = findViewById(R.id.inputName);
        Button addButton = findViewById(R.id.addButton);
        pdf = findViewById(R.id.pdfButton);
        apiBtn = findViewById(R.id.apiButton);
        webBtn = findViewById(R.id.webViewId);

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
        webBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, WebViewActivity.class);
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
