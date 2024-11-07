package com.jdcode.levelup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.GoogleAuthProvider;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private android.widget.Toast Toast;

    TextInputEditText emailInput;
    TextInputEditText passwordInput;
    TextInputEditText confirmPasswordInput;
    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    TextInputLayout confirmPasswordLayout;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;


    private final ActivityResultLauncher<Intent> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        handleSignInResult(data);
                    }
                }
            });

    private void handleSignInResult(Intent data) {
        try {
            // Obtiene la cuenta de Google desde el resultado
            GoogleSignInAccount account = GoogleSignIn.getSignedInAccountFromIntent(data)
                    .getResult(ApiException.class);
            if (account != null) {
                // Autenticación con Firebase usando el token de Google
                firebaseAuthWithGoogle(account.getIdToken());
            }
        } catch (ApiException e) {
            Log.w("RegisterActivity", "Fallo el inicio de sesión con Google", e);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso, el usuario está autenticado con Firebase
                        Log.d("RegisterActivity", "Inicio de sesión con Google exitoso");
                        //Redirigiendo al usuario a siguient pantalla
                        android.widget.Toast.makeText(this, "LogIn Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, products.class);
                        finish();
                        startActivity(intent);



                    } else {
                        showAlert();
                        Log.w("RegisterActivity", "Error en el inicio de sesión con Firebase", task.getException());
                    }
                });
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicio de codigo
        //Variables
        Button buttonLogIn = findViewById(R.id.button_login);
        Button buttonSignUp = findViewById(R.id.button_signup);
        Button buttonGoogleSingUp = findViewById(R.id.button_google_singup);


        emailInput = findViewById(R.id.emailInput);
        emailLayout = findViewById(R.id.emailLayout);

        passwordInput = findViewById(R.id.passwordInput);
        passwordLayout = findViewById(R.id.passwordLayout);


        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);



        //Funcion de los botones
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                completeRegister();
            }
        });

        //Inicio de sesion con google
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, options);

        buttonGoogleSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });


    }
    public void openLogin(){
        finish();
    }

    public void completeRegister(){

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirmPassword = confirmPasswordInput.getText().toString().trim();
        boolean next = false;

        //VALORES OBLIGATORIOS
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            next = false;
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
            if(email.isEmpty()){
                emailLayout.setError("This field is required");
            }else{
                emailLayout.setError(null);
            }if(password.isEmpty()){
                passwordLayout.setError("This field is required");
                passwordLayout.setErrorIconDrawable(null);
                passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }else{
                passwordLayout.setError(null);
            }if(confirmPassword.isEmpty()){
                confirmPasswordLayout.setError("This field is required");
                confirmPasswordLayout.setErrorIconDrawable(null);
                confirmPasswordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }else{
                confirmPasswordLayout.setError(null);
            }
        }else{
            emailLayout.setError(null);
            passwordLayout.setError(null);
            confirmPasswordLayout.setError(null);

            if(password.length()<8){
                passwordLayout.setError("Password must be at least 8 characters");
                passwordLayout.setErrorIconDrawable(null);
                passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                next = false;
            }else{
                if(!password.equals(confirmPassword)){
                    passwordLayout.setError("Passwords do not match");
                    passwordLayout.setErrorIconDrawable(null);
                    passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

                    confirmPasswordLayout.setError("Passwords do not match");
                    confirmPasswordLayout.setErrorIconDrawable(null);
                    confirmPasswordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                    next = false;
                }else{
                    passwordLayout.setError(null);
                    confirmPasswordLayout.setError(null);
                    next = true;
                }
            }
        }

        if(next){
            registerUser(email, password);
        }


    }

    public void registerUser(String email, String password){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if(task.isSuccessful()){
                        android.widget.Toast.makeText(this, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show();
                        Log.d("FirebaseAuth", "Registro exitoso. Usuario: " + email);
                        Intent intent = new Intent(RegisterActivity.this, LogIn.class);
                        finish();
                        startActivity(intent);
                    }else{
                        //error en el registro
                        Log.e("FirebaseAuth", "Error en el registro", task.getException());
                        showAlert();
                    }
                });
    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage("Error al crear el usuario")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Cierra el AlertDialog
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}


