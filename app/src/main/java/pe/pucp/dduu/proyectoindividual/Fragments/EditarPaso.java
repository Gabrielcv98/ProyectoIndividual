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
import android.widget.TextView;

import org.w3c.dom.Text;

import pe.pucp.dduu.proyectoindividual.Principal;
import pe.pucp.dduu.proyectoindividual.R;


public class EditarPaso extends Fragment {


    EditText nombreEt , creadorEt, urlEt , paisEt;
    TextView idEdit;
    Button botonEditar;
    public EditarPaso() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditarPaso newInstance() {

        return null;
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
        vista=  inflater.inflate(R.layout.fragment_editar_paso, container, false);
        nombreEt = vista.findViewById(R.id.nombrenewpasoEdit);
        creadorEt = vista.findViewById(R.id.creadropasoEdit);
        urlEt = vista.findViewById(R.id.urlpasoEdit);
        paisEt = vista.findViewById(R.id.paisdelpasoEdit);
        botonEditar = vista.findViewById(R.id.botonEditarPaso);
        idEdit =vista.findViewById(R.id.idStepEdit);

        nombreEt.setText(this.getArguments().getString("nombrestepEdit"));
        creadorEt.setText(this.getArguments().getString("creadorstepEdit"));
        urlEt.setText(this.getArguments().getString("paisstepEdit"));
        paisEt.setText(this.getArguments().getString("linkvideostepEdit"));
        idEdit.setText(this.getArguments().getString("idstepEdit"));;

        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreEt.getText().toString();
                String pais = paisEt.getText().toString();
                String url = urlEt.getText().toString();
                String creador = creadorEt.getText().toString();
                String id = idEdit.getText().toString();

                Principal p1 = (Principal) getActivity();
                p1.creartYactPaso(nombre , pais ,url,creador , id);
                irAPrincipal();



            }
        });
        OnBackPressedDispatcher onBackPressedDispatcher = requireActivity().getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

              irAPrincipal();

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