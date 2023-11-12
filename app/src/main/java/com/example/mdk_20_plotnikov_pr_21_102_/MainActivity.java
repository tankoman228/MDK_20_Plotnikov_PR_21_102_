package com.example.mdk_20_plotnikov_pr_21_102_;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference db;
    private List<User> users;


    private EditText etUsername, etEmail, etPhone, etName, etPorridge, etAge;
    private Button btnSave, btnDelete;
    private RecyclerView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        db = database.getReference("Users");

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etAge = findViewById(R.id.etAge);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPorridge = findViewById(R.id.etFavouritePorridge);

        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        lv = findViewById(R.id.lv);

        users = new ArrayList<>();

        loadFromDB();

        btnSave.setOnClickListener(v -> {

            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String name = etName.getText().toString();
            String porr = etPorridge.getText().toString();
            int age = Integer.parseInt(etAge.getText().toString());

            User user = new User(username, email, phone, name, porr, age);

            String key = db.push().getKey();
            db.child(key).setValue(user);
            users.add(user);

            updateItems();

            etUsername.setText("");
            etEmail.setText("");
            etPhone.setText("");
            etName.setText("");
            etPorridge.setText("");
            etAge.setText("");
        });

        btnDelete.setOnClickListener(v -> {

            String email = etEmail.getText().toString();

            for (User user : users) {
                if (user.email.equals(email)) {
                    users.remove(user);

                    db.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                childSnapshot.getRef().removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                    updateItems();
                    etEmail.setText("");
                    return;
                }
            }

        });
    }

    private void updateItems() {
        lv.setLayoutManager(new LinearLayoutManager(this));
        lv.setAdapter(new ItemsAdapter(users));
    }

    private void loadFromDB() {

        ValueEventListener l = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                users = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    assert u != null;
                    users.add(u);
                }

                updateItems();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        db.addValueEventListener(l);
    }
}