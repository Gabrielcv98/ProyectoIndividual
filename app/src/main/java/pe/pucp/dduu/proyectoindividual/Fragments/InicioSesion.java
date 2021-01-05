package pe.pucp.dduu.proyectoindividual.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import pe.pucp.dduu.proyectoindividual.MainActivity;
import pe.pucp.dduu.proyectoindividual.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InicioSesion#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InicioSesion extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public InicioSesion() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static InicioSesion newInstance() {
        InicioSesion fragment = new InicioSesion();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    View vista;
    Button btnIniciarSesion,btnRegistrar;
    EditText inputEmail, inputContra;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista= inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        btnIniciarSesion = (Button) vista.findViewById(R.id.buttonIniciarSesionPri);
        btnRegistrar = (Button) vista.findViewById(R.id.buttonRegistrarseInicio);
        inputEmail = (EditText) vista.findViewById(R.id.editTextSesion);
        inputContra = (EditText) vista.findViewById(R.id.editTextTextPassword);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String correo = inputEmail.getText().toString();
                String contra = inputContra.getText().toString();
                MainActivity m1 = (MainActivity) getActivity();
                m1.iniciarSesion(correo,contra);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar_usuario();
            }
        });


        return vista;
    }
    void registrar_usuario(){
        Registro fr=new Registro();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.esc_reutilizable,fr)
                .addToBackStack(null)
                .commit();

    }
}