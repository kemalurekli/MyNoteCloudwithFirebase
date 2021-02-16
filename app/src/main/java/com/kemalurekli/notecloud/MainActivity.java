package com.kemalurekli.notecloud;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText et_input;
    private Button bt_push;
    private ListView lv_notes;
    ArrayList<String> arrayList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_input = findViewById(R.id.et_input);
        bt_push = findViewById(R.id.bt_push);
        lv_notes = findViewById(R.id.lv_notes);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,arrayList);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("data");
        
        getNotes();
        bt_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNote = et_input.getText().toString().trim();
                String sKey = databaseReference.push().getKey();
                if (sKey!=" "){
                    databaseReference.child(sKey).child("value").setValue(sNote);
                }
            }
        });
    }

    private void getNotes() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String sNote = dataSnapshot.child("value").getValue(String.class);
                    arrayList.add(sNote);
                }
                lv_notes.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

               Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}