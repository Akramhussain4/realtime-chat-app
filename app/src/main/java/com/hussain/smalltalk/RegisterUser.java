package com.hussain.smalltalk;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity {

    private EditText Rname,Remail,Rpass;
    private Button register;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        mAuth = FirebaseAuth.getInstance();

        Rname = (EditText) findViewById(R.id.input_name);
        Remail= (EditText) findViewById(R.id.input_email);
        Rpass = (EditText) findViewById(R.id.input_password);
        register = (Button) findViewById(R.id.btn_signup);
    }

    public void register_btn(View view) {
        register_user(Remail.getText().toString(),Rpass.getText().toString());
    }

    private void register_user(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("******", "createUserWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterUser.this, "SignUp Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {
                            updateUser();
                            FirebaseUser user = mAuth.getCurrentUser();
                            String UserID=user.getEmail().replace("@","").replace(".","");
                            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference ref1= mRootRef.child("User").child(UserID);
                            ref1.child("Name").setValue(Rname.getText().toString().trim());
                            ref1.child("Email").setValue(user.getEmail());
                            Toast.makeText(RegisterUser.this, "Account Created!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUser() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            UserProfileChangeRequest update = new UserProfileChangeRequest.Builder().setDisplayName(Rname.getText().toString()).build();
            user.updateProfile(update).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d("******", "Updation Completed:" + task.isSuccessful());
                    }
                }
            });
        }
    }
}
