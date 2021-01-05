package pe.pucp.dduu.proyectoindividual;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pe.pucp.dduu.proyectoindividual.Adaptadores.ListaStepsAdaptador;
import pe.pucp.dduu.proyectoindividual.Adaptadores.ListaVideosTAdaptador;

import pe.pucp.dduu.proyectoindividual.Entidades.Steps;
import pe.pucp.dduu.proyectoindividual.Entidades.VideosT;
import pe.pucp.dduu.proyectoindividual.Fragments.EditarPaso;
import pe.pucp.dduu.proyectoindividual.Fragments.FragmentPrincipal;
import pe.pucp.dduu.proyectoindividual.Fragments.VideosItaLionFragment;

public class Principal extends AppCompatActivity implements DrawerLocker {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    private NavigationView navigationView;
private Long l;
    private Toolbar toolbar;
    private TextView nombreperfil , akaperfil , tipoUsuario;

    private DrawerLayout drawerLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    ArrayList<String> infoSesion;
    List<Steps> stepsList;
    List<VideosT> videostrainList;
    ImageView imagePerfil;
    private  int random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        fragmentManager = getSupportFragmentManager();
        navigationView = findViewById(R.id.navprincial);
        drawerLayout = findViewById(R.id.drawerLayaout);

        View v = navigationView.getHeaderView(0);
        imagePerfil = v.findViewById(R.id.imagenperfil);
        nombreperfil = v.findViewById(R.id.nombreperfil);
        akaperfil = v.findViewById(R.id.akaperfil);
        tipoUsuario = v.findViewById(R.id.tipoUsuarioperfil);



        FragmentPrincipal fragmentPrincipal = new FragmentPrincipal();


            DocumentReference referenceUser = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
            referenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    nombreperfil.setText(documentSnapshot.get("nombre").toString());
                    akaperfil.setText(documentSnapshot.get("aka").toString());
                    tipoUsuario.setText(documentSnapshot.get("tipoUsuario").toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("AYAYAI", e.getMessage());
                }
            });

        toolbar("Ita Lion Family");
        menuItem();
            StorageReference reference =
                    FirebaseStorage.getInstance().getReference().child("imagenes").child(firebaseAuth.getCurrentUser().getUid() + ".jpg");

            Log.d("AYAYAI", reference.getName());

            Glide.with(this).load(reference).into(imagePerfil);




            fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
                @Override
                public void onBackStackChanged() {
                    if (fragmentManager.getBackStackEntryCount() > 0) {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                    } else {
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    }
                }
            });

        if (getIntent().hasExtra("nombrestepEdit")) {
            Bundle bundle = new Bundle();
            bundle.putString("nombrestepEdit", getIntent().getStringExtra("nombrestepEdit"));
            bundle.putString("creadorstepEdit", getIntent().getStringExtra("creadorstepEdit"));
            bundle.putString("paisstepEdit", getIntent().getStringExtra("paisstepEdit"));
            bundle.putString("linkvideostepEdit", getIntent().getStringExtra("linkvideostepEdit"));
            bundle.putString("idstepEdit", getIntent().getStringExtra("idstepEdit"));
            EditarPaso editarPaso = new EditarPaso();

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            editarPaso.setArguments(bundle);
            fragmentTransaction.replace(R.id.container, editarPaso).addToBackStack(null);
            fragmentTransaction.commit();

        }else {
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, fragmentPrincipal).addToBackStack(null);
            fragmentTransaction.commit();

        }
    }

    public void toolbar(String titulo) {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_action_menu);
            ab.setDisplayHomeAsUpEnabled(true);

            ab.setTitle(titulo);



        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:

                drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }




    public void menuItem() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {



                switch (item.getItemId()) {
                    case R.id.galeria:

                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.container,  new FragmentPrincipal())
                                .commit();
                        break;
                    case R.id.ubica:

                       Intent intent = new Intent(Principal.this , VerUbicacion.class);
                        intent.putExtra("latitud", -12.083271);
                        intent.putExtra("longitud", -77.096245);
                       startActivity(intent);
                        break;
                    case R.id.biog:

                        getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.container, new VideosItaLionFragment())
                                .commit();
                        break;
                    case R.id.cerrarSesion:
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);



                }
                return false;

            }
        });
    }

    @Override
    public void setDrawerLocked(boolean shouldLock) {
        if (shouldLock) {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }
    public void listarSteps(){

        stepsList = new ArrayList<>();


        firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("steps").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    stepsList.clear();

                    if(error != null){
                        Log.d("AYAYAI" , error.getMessage());
                        Toast.makeText(Principal.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();

                    }
                    for (DocumentChange doc : value.getDocumentChanges() ){
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            Steps step = doc.getDocument().toObject(Steps.class);
                            step.setId(doc.getDocument().getId());
                            stepsList.add(step);

                        }

                    }
                    ListaStepsAdaptador adapt = new ListaStepsAdaptador(Principal.this,stepsList);
                    RecyclerView rv = findViewById(R.id.recyclerSteps);
                    rv.setHasFixedSize(true);
                    rv.setLayoutManager(new LinearLayoutManager(Principal.this));
                    rv.setAdapter(adapt);

                }
            });
        }

    public void descargarDocumento(String child) {
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission == PackageManager.PERMISSION_GRANTED) {
            //descarga archivo de firebase storage
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference storageReference1 = storageReference.child("general");

            File externalStoragePublicDirectory =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            File file = new File(externalStoragePublicDirectory, child);

            storageReference1.getFile(file)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("infoApp", "descarga exitosa");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("infoApp", "error en la descarga");
                            e.printStackTrace();
                        }
                    });

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
        }
    }

    public void listarArchivos() {
        videostrainList = new ArrayList<>();
        StorageReference reference = FirebaseStorage.getInstance().getReference();

        reference.child("general").listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference ref : listResult.getItems()) {

                            final VideosT videosT = new VideosT();

                            videosT.setNombre(ref.getName());
                            ref.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                                @Override
                                public void onSuccess(StorageMetadata storageMetadata) {
                                    l = storageMetadata.getCreationTimeMillis();
                                    videosT.setFecha(l.toString());
                                }
                            });
                            videosT.setDescarga(ref.getDownloadUrl());
                            videostrainList.add(videosT);
                            Log.d("AYAYAI", "elemento: " + ref.getDownloadUrl());

                        }
                        ListaVideosTAdaptador adapt = new ListaVideosTAdaptador(Principal.this,videostrainList);
                        RecyclerView rv = findViewById(R.id.recyclerVideosTraining);
                        rv.setHasFixedSize(true);
                        rv.setLayoutManager(new LinearLayoutManager(Principal.this));
                        rv.setAdapter(adapt);
                        /*for (StorageReference ref : listResult.getPrefixes()) {
                            Log.d("infoApp", "carpeta: " + ref.getName());
                            ref.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    for (StorageReference ref2 : listResult.getItems()) {
                                        Log.d("infoApp", "elemento: " + ref2.getName());
                                    }
                                }
                            });
                        }*/

                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                        Log.d("AYAYAI", "Error al listar");
                    }

                });
    }
    public void creartYactPaso(final String nombre, String pais, String url , String creador,String id){


        Map<String,Object> stepData = new HashMap<>();
        stepData.put("nombre" , nombre);
        stepData.put("creador" , creador);
        stepData.put("pais" , pais);
        stepData.put("url" , url);

        if(id !=null){
            DocumentReference df = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("steps").document(id);
            df.update(stepData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(Principal.this, nombre + " " +"ha sido actualizado", Toast.LENGTH_SHORT).show();


                }
            });
        }else{
        DocumentReference df = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("steps").document();
        df.set(stepData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Principal.this, nombre + " " + "ha sido añadido", Toast.LENGTH_SHORT).show();


            }
        });
        }

    } public void subirArchivoVideo(Uri uri) {

        Random ran = new Random();
        int r = ran.nextInt(200);
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE);


        //subir archivo a firebase storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        storageReference.child("general/").child(firebaseAuth.getCurrentUser().getUid()+ r + ".mp4").putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("infoApp", "video subido exitoso");
                        Toast.makeText(Principal.this, "Se ha subido correctamente el video", Toast.LENGTH_SHORT).show();
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("infoApp", "error en la subida");
                        e.printStackTrace();
                    }
                });

    }



}
