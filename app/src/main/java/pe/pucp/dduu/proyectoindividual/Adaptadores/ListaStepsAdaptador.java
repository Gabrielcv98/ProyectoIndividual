package pe.pucp.dduu.proyectoindividual.Adaptadores;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import pe.pucp.dduu.proyectoindividual.Entidades.Steps;
import pe.pucp.dduu.proyectoindividual.Fragments.CrearPaso;
import pe.pucp.dduu.proyectoindividual.Fragments.EditarPaso;
import pe.pucp.dduu.proyectoindividual.Principal;
import pe.pucp.dduu.proyectoindividual.R;

public class ListaStepsAdaptador extends RecyclerView.Adapter<ListaStepsAdaptador.ViewHolder> {
    private Context context;
    private List<Steps> steps;
    private LayoutInflater inflater;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public ListaStepsAdaptador(Context context, List<Steps> steps) {
        this.context = context;
        this.steps = steps;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListaStepsAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_pasos,null);

        return new ListaStepsAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaStepsAdaptador.ViewHolder holder, int position) {
        holder.blindData(steps.get(position));
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
    public void setItems(List<Steps> newSteps){
        steps = newSteps;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconoPais;
        TextView nombre,creador,editLink , linkBorrar;
         Button linkStep;
        ViewHolder(View itemView){
            super(itemView);
            iconoPais = itemView.findViewById(R.id.iconopais);
            nombre = itemView.findViewById(R.id.nombrepaso);
            creador = itemView.findViewById(R.id.nombrecreador);
            linkStep = itemView.findViewById(R.id.linkbutton);
            editLink = itemView.findViewById(R.id.linkEdit);
            linkBorrar = itemView.findViewById(R.id.linkBorrar);
        }
        void blindData(final Steps step){
            nombre.setText(step.getNombre());
            creador.setText(step.getCreador());
            editLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, Principal.class);

                    intent.putExtra("nombrestepEdit",step.getNombre() );
                    intent.putExtra("creadorstepEdit",step.getCreador() );
                    intent.putExtra("paisstepEdit",step.getPais() );
                    intent.putExtra("linkvideostepEdit",step.getUrl());
                    intent.putExtra("idstepEdit",step.getId());


                    context.startActivity(intent);



                    }

                });


            linkStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri uri = Uri.parse(step.getUrl());
                    if(uri != null) {
                        Intent i = new Intent(Intent.ACTION_VIEW, uri);

                        try {
                            context.startActivity(i);
                        }catch (ActivityNotFoundException e){
                            Log.d("AYAYAI", "error es "  + e.getMessage());
                        }
                        finally {
                            Toast.makeText(context, "No existe esa URL", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

            linkBorrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Mensaje de alerta")
                            .setMessage("¿Estás seguro que de sea borrar este paso?")
                            .setPositiveButton("Sí",null)
                            .setNegativeButton("Cancelar",null)
                            .show();
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseFirestore = FirebaseFirestore.getInstance();
                            firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid()).collection("steps").document(step.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "El paso ha sido eliminado", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("AYAYAI", "onFailure: " + e);
                                    Toast.makeText(context, "Ha ocurrido un error y no se ha borrado", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent inte = new Intent(context, Principal.class);
                            context.startActivity(inte);
                            dialog.dismiss();
                        }
                    });
                    Button negativeButo = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    negativeButo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                }
            });
        }

    }


}
