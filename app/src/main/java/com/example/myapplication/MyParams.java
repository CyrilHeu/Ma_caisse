package com.example.myapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyParams {
    public int n_added_lines_buttons;
    public boolean b_init_params = false;
    public FirebaseFirestore db;

    public MyParams(FirebaseFirestore db_) {
        db = db_;

        DocumentReference docRef = db.collection("api").document("params");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        n_added_lines_buttons = Integer.valueOf(document.get("added_lines").toString());

                        Log.d("12345678a", "DocumentSnapshot data: " + document.getData());
                        b_init_params = true;
                    } else {
                        Log.d("12345678a", "No such document");
                    }
                } else {
                    Log.d("12345678a", "get failed with ", task.getException());
                }
            }
        });
    }

    public int getN_added_lines_buttons() {
        return n_added_lines_buttons;
    }

    public void add_one_line_buttons() {
        Map<String, Object> obj = new HashMap<>();
        obj.put("added_lines", String.valueOf(n_added_lines_buttons+1));
        n_added_lines_buttons = n_added_lines_buttons+1;
        db.collection("api").document("params")
                .set(obj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("12345678a", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("12345678a", "Error writing document", e);
                    }
                });
    }
}
