package pe.pucp.dduu.proyectoindividual.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import pe.pucp.dduu.proyectoindividual.Entidades.Usuario;
import pe.pucp.dduu.proyectoindividual.MainActivity;
import pe.pucp.dduu.proyectoindividual.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Registro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Registro extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Registro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Registro.
     */
    // TODO: Rename and change types and number of parameters
    public static Registro newInstance(String param1, String param2) {
        Registro fragment = new Registro();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View vista;
    Button btnCancelar, btnRegistro,btnSubir;
    Context context;
    EditText txtUser, txtPwd, txtNames, txtApellidos ,txtAka , txtContacto;
    ImageView perfilregistro;
    InputStream inputStreamG = null;
    Uri imageUri;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_registro, container, false);

        txtUser = (EditText) vista.findViewById(R.id.emailRegistro);
        txtPwd = (EditText) vista.findViewById(R.id.contraRegistro);
        txtNames= (EditText) vista.findViewById(R.id.nombre);

        txtApellidos = (EditText) vista.findViewById(R.id.apellidos);
        txtAka = (EditText) vista.findViewById(R.id.aka);

        perfilregistro = (ImageView) vista.findViewById(R.id.imagenperfilregistro);

        btnCancelar = (Button) vista.findViewById(R.id.buttonCancelar);
        btnRegistro = (Button) vista.findViewById(R.id.buttonAceptarRegistro);
        btnSubir = (Button) vista.findViewById(R.id.subirimagenperfil);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciar_sesion();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nombre = txtNames.getText().toString();
                String apellidos = txtApellidos.getText().toString();
                String correo = txtUser.getText().toString();
                String contra = txtPwd.getText().toString();
                String aka = txtAka.getText().toString();


                MainActivity m1 = (MainActivity) getActivity();
                m1.Registro(correo , contra ,aka,nombre,apellidos , imageUri );
            }
        });

        btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i,1);
            }
        });

        OnBackPressedDispatcher onBackPressedDispatcher = requireActivity().getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               iniciar_sesion();
            }


        });


        return vista;
    }

    public void iniciar_sesion() {
        InicioSesion fr=new InicioSesion();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.esc_reutilizable,fr)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode ==-1){
               imageUri = data.getData();
                InputStream inputStream = null;
                try {

                  inputStream = getContext().getContentResolver().openInputStream(imageUri);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    inputStreamG = inputStream;
                }
                BitmapFactory.decodeStream(inputStream);
                perfilregistro.setImageURI(imageUri);




            }
        }
    }
}