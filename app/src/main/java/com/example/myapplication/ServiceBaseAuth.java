package com.example.myapplication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ServiceBaseAuth {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private int count_co = 0;
    public ServiceBaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    public void createUser(String email, String pwd){
        /*mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("123456A", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("123456A", "createUserWithEmail:failure", task.getException());


                        }
                    }
                });*/
    }
    public void authUser(String email, String pwd, MainActivity mainActivity){

        CountDownTimer timer;
        timer = new CountDownTimer(4000, 600) {
            @Override
            public void onTick(long l) {
                count_co++;
                if(count_co<3){
                    mAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(mainActivity, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("123456B", "signInWithEmail:success");
                                        user = mAuth.getCurrentUser();
                                        if(user != null){
                                            Log.d("123456B", "user != null " + user.getIdToken(true) + " / " + user.getUid());
                                            cancel();
                                        }
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("123456B", "signInWithEmail:failure", task.getException());

                                    }
                                }
                            });

                }

            }

            @Override
            public void onFinish() {
                try{
                    start();
                }catch(Exception e){
                    Log.e("Error", "Error: " + e.toString());
                }
            }
        }.start();

    }

    public FirebaseUser getUser() {
        return user;
    }
}
