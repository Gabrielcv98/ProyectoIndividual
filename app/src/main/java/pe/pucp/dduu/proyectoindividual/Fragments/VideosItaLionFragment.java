package pe.pucp.dduu.proyectoindividual.Fragments;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.InputStream;

import pe.pucp.dduu.proyectoindividual.Principal;
import pe.pucp.dduu.proyectoindividual.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideosItaLionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosItaLionFragment extends Fragment {

    View vista;
    Uri videoUri;
    Button btnsubirVideo;
    String tipo;
    public VideosItaLionFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VideosItaLionFragment newInstance(String param1, String param2) {
        VideosItaLionFragment fragment = new VideosItaLionFragment();

        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {


            Principal a1 = (Principal) getActivity();
            a1.listarArchivos();


        super.onCreate(savedInstanceState);

    }
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=  inflater.inflate(R.layout.fragment_videos_ita_lion, container, false);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        btnsubirVideo = vista.findViewById(R.id.subirVideo);

        DocumentReference referenceUser = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
        referenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       tipo =  documentSnapshot.get("tipoUsuario").toString().toLowerCase();

                        if(tipo.equalsIgnoreCase("visitante")){

                            btnsubirVideo.setVisibility(View.GONE);}
                       }});



        if (btnsubirVideo != null){
        btnsubirVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("video/*");
                startActivityForResult(i,2);

            }
        });}

        OnBackPressedDispatcher onBackPressedDispatcher = requireActivity().getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                FragmentPrincipal fr=new FragmentPrincipal();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container,fr)
                        .addToBackStack(null)
                        .commit();
            }
        });


        return vista;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            if(resultCode ==-1){
                videoUri = data.getData();
                Principal a1 = (Principal) getActivity();
                a1.subirArchivoVideo(videoUri);
                reload();

            }
        }
    }

    private void reload(){
        getActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, new VideosItaLionFragment())
                .commit();
    }


}