package com.hussain.smalltalk;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static FirebaseDatabase mDatabase;
    private EditText userName,password;
    private Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        userName = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        signIn = (Button)findViewById(R.id.signin);

        if(mAuth.getCurrentUser() != null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(),UserActivity.class));
        }
    }


    public void signin_btn(View view) {
        siginIn_User(userName.getText().toString(),password.getText().toString());
    }

    private void siginIn_User(String email, String password) {
        if (!validateForm()) {
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("------", "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w("-----", "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "SignIn Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                                Intent i = new Intent(MainActivity.this, UserActivity.class);
                                finish();
                                startActivity(i);
                    }
                }
    });
    }


    private boolean validateForm() {
        boolean valid = true;
        String email = userName.getText().toString();
        if (TextUtils.isEmpty(email)) {
            userName.setError("Required.");
            valid = false;
        } else {
            userName.setError(null);
        }

        String password1 = password.getText().toString();
        if (TextUtils.isEmpty(password1)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }
        return valid;
    }

    public void reg_btn(View view) {
        Intent intent = new Intent(this,RegisterUser.class);
        startActivity(intent);
    }
}
