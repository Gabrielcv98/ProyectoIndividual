package pe.pucp.dduu.proyectoindividual.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import pe.pucp.dduu.proyectoindividual.Entidades.Usuario;
import pe.pucp.dduu.proyectoindividual.MainActivity;
import pe.pucp.dduu.proyectoindividual.Principal;
import pe.pucp.dduu.proyectoindividual.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentPrincipal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentPrincipal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String aka;
    private String tipousuario;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View vista;
    TextView sesionAka;
    TextView sesionTipo;
    Usuario usuario;
    Button crearPaso;
    public FragmentPrincipal() {
        // Required empty public constructor
    }

    public static FragmentPrincipal newInstance(String param1, String param2) {
        FragmentPrincipal fragment = new FragmentPrincipal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Principal a1 = (Principal) getActivity();
        a1.listarSteps();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         vista = inflater.inflate(R.layout.fragment_principal, container, false);
        crearPaso = vista.findViewById(R.id.botonanadirpaso);


         crearPaso.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 irAcrearPaso();
             }
         });

        OnBackPressedDispatcher onBackPressedDispatcher = requireActivity().getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
            }


        });
         return vista;
    }
    public void irAcrearPaso(){
        CrearPaso crearPaso = new CrearPaso();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,crearPaso)
                .addToBackStack(null)
                .commit();
    }


}