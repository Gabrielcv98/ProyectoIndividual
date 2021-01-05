package pe.pucp.dduu.proyectoindividual.Fragments;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import pe.pucp.dduu.proyectoindividual.MainActivity;
import pe.pucp.dduu.proyectoindividual.Principal;
import pe.pucp.dduu.proyectoindividual.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CrearPaso#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CrearPaso extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseAuth firebaseAuth;
    Button anadirpaso;
    EditText nombrenewpaso, paisdelpaso,urldelpaso,creadorpaso;
    public CrearPaso() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CrearPaso.
     */
    // TODO: Rename and change types and number of parameters
    public static CrearPaso newInstance(String param1, String param2) {
        CrearPaso fragment = new CrearPaso();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
        View vista;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_crear_paso, container, false);
        nombrenewpaso = vista.findViewById(R.id.nombrenewpaso);
        paisdelpaso = vista.findViewById(R.id.paisdelpaso);
        urldelpaso = vista.findViewById(R.id.urlpaso);
        creadorpaso = vista.findViewById(R.id.creadropaso);
        anadirpaso = vista.findViewById(R.id.botonCrearPaso);
        firebaseAuth = FirebaseAuth.getInstance();

        anadirpaso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = nombrenewpaso.getText().toString();
                String pais = paisdelpaso.getText().toString();
                String url = urldelpaso.getText().toString();
                String creador = creadorpaso.getText().toString();

                if(nombre.equalsIgnoreCase("") || pais.equalsIgnoreCase("") || url.equalsIgnoreCase("") || creador.equalsIgnoreCase("") ){
                    Toast.makeText(getContext(), "Debe llenar todos los campos solicitados", Toast.LENGTH_SHORT).show();

                }else{
                    Principal p1 = (Principal) getActivity();
                    String id = null;
                    p1.creartYactPaso(nombre , pais ,url,creador , id);
                    irAPrincipal();

                }

            }
        });
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


    public void irAPrincipal() {
        FragmentPrincipal fr=new FragmentPrincipal();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container,fr)
                .addToBackStack(null)
                .commit();
    }


}