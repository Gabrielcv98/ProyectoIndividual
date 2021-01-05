package pe.pucp.dduu.proyectoindividual;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import pe.pucp.dduu.proyectoindividual.Entidades.Usuario;
import pe.pucp.dduu.proyectoindividual.Fragments.FragmentPrincipal;
import pe.pucp.dduu.proyectoindividual.Fragments.InicioSesion;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        fm.beginTransaction().replace(R.id.esc_reutilizable, new InicioSesion()).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // User has already signed in, navigate to home
            Intent intent = new Intent(MainActivity.this, Principal.class);
            startActivity(intent);}

    }
    public void iniciarSesion (final String email, String contra) {

        if (!email.equals("") && !contra.equals("")){
            try {

                firebaseAuth.signInWithEmailAndPassword(email, contra).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            DocumentReference dR = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
                            dR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    ingresoExitoso(documentSnapshot.getString("Tipo de usuario") , documentSnapshot.getString("Aka"));
                                    Usuario usuario = new Usuario();
                                    //usuario = obtenerDatosFireStore();

                                }
                            });


                        }else {
                            Toast.makeText(getApplicationContext(),"Error: el usuario no se pudo autenticar correctamente",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Error error){
                Toast.makeText(getApplicationContext(),"Usted no esta registrado", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Error: debe colocar sus datos", Toast.LENGTH_SHORT).show();
        }
    }
    public void Registro (String correo , String contra , final String aka, final String nombre, final String apellido , final Uri uri) {
        if (!correo.equals("") && !contra.equals("")) {
            if (contra != null || aka != null || nombre != null || apellido != null || uri != null) {
                firebaseAuth.createUserWithEmailAndPassword(correo, contra)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "createUserWithEmail:success", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = firebaseAuth.getCurrentUser();
                                    DocumentReference df = firebaseFirestore.collection("users").document(user.getUid());
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("nombre", nombre);
                                    userData.put("apellido", apellido);
                                    userData.put("aka", aka);
                                    userData.put("tipoUsuario", "Visitante");
                                    subirArchivoImage(uri, user.getUid());
                                    df.set(userData);
                                    ingresoExitoso("Visitante", aka);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
            Toast.makeText(this, "Obtene todos los datos por favor", Toast.LENGTH_SHORT).show();
        }
    }
    public void ingresoExitoso(String tipousuario , String aka){
        Bundle params = new Bundle();
        ArrayList<String> arrayParams = new ArrayList<>();
        arrayParams.add(tipousuario);
        arrayParams.add(aka);
        params.putStringArrayList("info",arrayParams);
        Intent i = new Intent(getApplicationContext(), Principal.class);
        i.putExtras(params);
        startActivity(i);
    }
    public void subirArchivoImage(Uri uri, String id) {

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


            //subir archivo a firebase storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                storageReference.child("imagenes/"+ id+ ".jpg").putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Log.d("infoApp", "subida exitosa");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("infoApp", "error en la subida");
                                e.printStackTrace();
                            }
                        });



    }





}