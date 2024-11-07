package com.jdcode.levelup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LogIn extends AppCompatActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    GoogleSignInClient googleSignInClient;

    TextInputLayout emailLayout;
    TextInputLayout passwordLayout;
    TextInputEditText emailInput;
    TextInputEditText passwordInput;

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
                        Intent intent = new Intent(this, products.class);
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
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //VARIABLES
        Button buttonLogin = findViewById(R.id.button_login);
        Button buttonSignUp = findViewById(R.id.button_signup);
        Button buttonGoogleLogin = findViewById(R.id.button_google_login);

        emailInput = findViewById(R.id.emailInput);
        emailLayout = findViewById(R.id.emailLayout);

        passwordInput = findViewById(R.id.passwordInput);
        passwordLayout = findViewById(R.id.passwordLayout);


        //LOGICA DE BOTONES
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();

                //openProducts();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        //GOOGLE LOGIC
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, options);

        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });



    }

    public void startLogin(){
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean next = false;

        if (email.isEmpty() || password.isEmpty()) {
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
            }
        }else{
            next = true;
        }

        if(next){
            loginUser(email, password);
        }

    }


    public void loginUser(String email, String password){
       auth.getInstance()
               .signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->{
                   if(task.isSuccessful())
                   {
                       FirebaseUser user = auth.getCurrentUser();
                       if(user != null){
                           Log.d("Login","Inicio de sesión exitoso: " + user.getEmail() );
                           Toast.makeText(this, "Inicio de sesión exitoso: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                           //Redirigiendo a la siguiente pagina
                           Intent intent = new Intent(this, products.class);
                           finish();
                           startActivity(intent);
                       }
                   }else{
                       showAlert();
                   }
               });
    }





    public void openRegister (){
        Intent intent = new Intent(LogIn.this, RegisterActivity.class);
        startActivity(intent);
    }
    public void openProducts(){
        Intent intent = new Intent(LogIn.this, products.class);
        finish();
        startActivity(intent);
    }

    public void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Error al iniciar sesion")
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