package com.example.noirsplendor;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class AuthActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button registerButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Inicializar vistas
        emailEditText = findViewById(R.id.userRegistro);
        passwordEditText = findViewById(R.id.contraRegistro);
        registerButton = findViewById(R.id.registrar);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithEmailAndPassword();
            }
        });

        findViewById(R.id.buttonGoogle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Iniciar sesión exitoso, autenticar con Firebase
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            // Error al iniciar sesión con Google
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Inicio de sesión exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Aquí puedes actualizar la interfaz de usuario, mostrar un mensaje de bienvenida, etc.
                            Toast.makeText(AuthActivity.this, "Inicio de sesión exitoso como " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            // Redirigir a la actividad Login.class
                            Intent intent = new Intent(AuthActivity.this, Camisas.class);
                            startActivity(intent);
                            finish(); // Cerrar esta actividad para evitar que el usuario regrese a ella con el botón Atrás
                        } else {
                            // Error al iniciar sesión
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void registerWithEmailAndPassword() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(AuthActivity.this, "Registro exitoso como " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            // Redirigir al usuario a la actividad de inicio de sesión
                            Intent intent = new Intent(AuthActivity.this, Camisas.class);
                            startActivity(intent);
                            finish(); // Cerrar esta actividad para evitar que el usuario regrese a ella con el botón Atrás
                        } else {
                            // Error en el registro
                            Toast.makeText(AuthActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void registerUserWithEmailAndPassword(String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            Toast.makeText(AuthActivity.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
                            // Redirigir a la actividad Login.class
                            Intent intent = new Intent(AuthActivity.this, Login.class);
                            startActivity(intent);
                            finish(); // Cerrar esta actividad para evitar que el usuario regrese a ella con el botón Atrás
                        } else {
                            // Error en el registro
                            Toast.makeText(AuthActivity.this, "Error al registrar usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}




    /*Button btn_registrar;
    EditText user, contra;
    FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;*/



    /*

        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        user = findViewById(R.id.userRegistro);
        contra = findViewById(R.id.contraRegistro);
        btn_registrar = findViewById(R.id.registrar);


        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailUser = user.getText().toString().trim();
                String contraUser = contra.getText().toString().trim();

                if (emailUser.isEmpty() && contraUser.isEmpty()) {
                    Toast.makeText(AuthActivity.this, "Ingresar los datos", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(emailUser, contraUser);
                }
            }
        });*/

    /*private void registerUser(String emailUser, String contraUser) {
        mAuth.createUserWithEmailAndPassword(emailUser, contraUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String, Object> map = new HashMap<>();

                map.put("user", emailUser);
                map.put("contra", contraUser);

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        finish();
                        startActivity(new Intent(AuthActivity.this, Login.class));
                        Toast.makeText(AuthActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AuthActivity.this, "Error al guardar", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AuthActivity.this, "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        });
    }*/
