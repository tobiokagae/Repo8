package com.example.tugas8;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton btnCreate;
    RecyclerView rvNotes;
    SearchView searchView;
    NotesAdapter notesAdapter;
    DBConfig dbConfig;

    final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    loadDataFromDatabase();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvNotes = findViewById(R.id.rv_notes);
        searchView = findViewById(R.id.search_view);
        btnCreate = findViewById(R.id.btn_create);

        btnCreate.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateActivity.class);
            resultLauncher.launch(intent);
        });

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        DBConfig dbConfig = new DBConfig(this);
        List<Notes> notes = dbConfig.getAllNotes();

        if (notesAdapter == null){
            notesAdapter = new NotesAdapter(notes, this, resultLauncher);
            rvNotes.setLayoutManager(new LinearLayoutManager(this));
            rvNotes.setAdapter(notesAdapter);
        } else {
            notesAdapter.setNotes(notes);
            notesAdapter.notifyDataSetChanged();
        }
    }
}