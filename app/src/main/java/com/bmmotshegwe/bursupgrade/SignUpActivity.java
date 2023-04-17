package com.bmmotshegwe.bursupgrade;

/*
 *  @author: Monei Bakang Motshegwe
 *  phoneNumber: +26772711192
 *  email: bmmotshegwe@burs.org.bw
 *  line: 9315
 *  date: 01/02/2023
 * */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    /********************************************************/
    // Firebase
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    /********************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        /********************************************************/
        // Firebase
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        /********************************************************/

        // Initializing and declaration
        // TextViews
        TextView firstNameTextView = (TextView) findViewById(R.id.firstNameTextView);
        TextView lastNameTextView = (TextView) findViewById(R.id.lastNameTextView);
        TextView emailImageView = (TextView) findViewById(R.id.emailImageView);
        TextView genderTextView = (TextView) findViewById(R.id.genderTextView);
        TextView DOBTextView = (TextView) findViewById(R.id.DOBTextView);
        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
        TextView phoneNumberTextView = (TextView) findViewById(R.id.phoneNumberTextView);
        TextView passwordTextView = (TextView) findViewById(R.id.passwordTextView);
        TextView rePassTextView = (TextView) findViewById(R.id.rePassTextView);

        // EditText
        EditText firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        EditText lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);
        EditText genderEditText = (EditText) findViewById(R.id.genderEditText);
        EditText DOBEditText = (EditText) findViewById(R.id.DOBEditText);
        EditText addressEditText = (EditText) findViewById(R.id.addressEditText);
        EditText phoneEditText = (EditText) findViewById(R.id.editTextPhone);
        EditText passwordEditText = (EditText) findViewById(R.id.editTextTextPassword);
        EditText rePassEditText = (EditText) findViewById(R.id.rePassEditText);

        // Button
        Button signUpBtn = (Button) findViewById(R.id.signUpBtn);

        // OnClickListener
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                String gender = genderEditText.getText().toString().trim();
                String DOB = DOBEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String phoneNumber = phoneEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String rePassword = rePassEditText.getText().toString().trim();

                // Validating EditTexts if are empty
                if(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || gender.isEmpty()
                        || DOB.isEmpty() || address.isEmpty() || phoneNumber.isEmpty()
                        || password.isEmpty() || rePassword.isEmpty()){
                    // Alert Dialogue
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Fill in everything");
                    builder.setMessage("Fields are empty!! Please fill in everything");
                    builder.show();
                }
                // Validating password lengths
                if(password.length() < 6 || rePassword.length() < 6){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Short Assignment");
                    builder.setMessage("Make sure your passwords have more than 6 digits!!");
                    builder.show();
                }
                // Checking if passwords are the same
                if(password == rePassword && rePassword == password){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Passwords do not match");
                    builder.setMessage("Passwords are not the same, please enter same passwords!!");
                    builder.show();
                }
                else{
                    registerUser(email, firstName, lastName, gender, DOB, address, phoneNumber, password, rePassword);
                }
            }
        });
    }
    public void registerUser(String email, String firstName, String lastName, String gender, String DOB, String Address, String phoneNumber, String password, String rePassword){
        mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>(){
            public void onSuccess(AuthResult authResult){
                HashMap<String,Object> map = new HashMap<>();
                map.put("email", email);
                map.put("firstName", firstName);
                map.put("lastName", lastName);
                map.put("gender", gender);
                map.put("DOB", DOB);
                map.put("Address", Address);
                map.put("PhoneNumber", phoneNumber);
                map.put("Password", password);
                map.put("RePassword", rePassword);
                mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map)
                        .addOnCompleteListener(new OnCompleteListener<Void>(){
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(SignUpActivity.this,"Successfully Registered", Toast.LENGTH_LONG).show();
                                    Intent intent= new Intent(SignUpActivity.this, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this,e.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}