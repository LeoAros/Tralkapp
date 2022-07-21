package com.example.tralkapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tralkapp.DB.DBHelper;
import com.example.tralkapp.ENTIDADES.User;

public class LoginActivity extends AppCompatActivity {
    private ImageView imgLogoLogin;
    private EditText edtEmail,edtPass;
    private Button btnLogin;
    private DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imgLogoLogin = findViewById(R.id.imgLogoLogin);
        imgLogoLogin.setImageResource(R.drawable.huemul);
        edtEmail = findViewById(R.id.editTextTextEmailAddress);
        edtPass = findViewById(R.id.editTextTextPassword);
        btnLogin = findViewById(R.id.btnLogin);
        DB = new DBHelper(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = edtEmail.getText().toString();
                String txtPass = edtPass.getText().toString();
                if(txtEmail.isEmpty() || txtPass.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Rellene todos los campos.", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkUserPass = DB.checkUserPass(txtEmail,txtPass);
                    if(checkUserPass){
                        Cursor res = DB.getUser("user","email",txtEmail);
                        res.moveToFirst();
//                        String id_user = String.valueOf(res.getInt(0));
                        String[] arregloUser = new String[]{res.getString(0),res.getString(1),res.getString(2),res.getString(3),res.getString(4),res.getString(5)};
                        login(arregloUser);
                    }else {
                        Toast.makeText(LoginActivity.this, "Error al iniciar sesión.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkSession();
    }

    public void checkSession(){
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        int userID = sessionManagement.getSession();

        if(userID != -1){
            moveToMainActivity();
        }
    }

    public void login(String[] usuario) {
        User user = new User(Integer.valueOf(usuario[0]),usuario[1],usuario[2],usuario[3],usuario[4],Integer.valueOf(usuario[5]));
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        sessionManagement.saveSession(user);

        moveToMainActivity();
    }

    private void moveToMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}