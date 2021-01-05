package pe.pucp.dduu.proyectoindividual.Adaptadores;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import pe.pucp.dduu.proyectoindividual.Descargar;
import pe.pucp.dduu.proyectoindividual.Entidades.Steps;
import pe.pucp.dduu.proyectoindividual.Entidades.VideosT;
import pe.pucp.dduu.proyectoindividual.Fragments.VideosItaLionFragment;
import pe.pucp.dduu.proyectoindividual.Principal;
import pe.pucp.dduu.proyectoindividual.R;


public class ListaVideosTAdaptador extends RecyclerView.Adapter<ListaVideosTAdaptador.ViewHolder>{

    private Context context;
    private List<VideosT> videosTList;
    private LayoutInflater inflater;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private List<VideosT> videosCompa;
    String  tipo;

    public ListaVideosTAdaptador(Context context, List<VideosT> videosTList) {
        this.context = context;
        this.videosTList = videosTList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListaVideosTAdaptador.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.listavideos,null);

        return new ListaVideosTAdaptador.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaVideosTAdaptador.ViewHolder holder, int position) {
        holder.blindData(videosTList.get(position));
    }

    @Override
    public int getItemCount() {
        return videosTList.size();
    }
    public void setItems(List<VideosT> newVideosT){
        videosTList = newVideosT;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconoItalion;
        TextView nombre,descarga,fecha,borrarvideo;
        Button vervideo;
        ViewHolder(View itemView){
            super(itemView);
            iconoItalion = itemView.findViewById(R.id.italionimage);
            nombre = itemView.findViewById(R.id.nombrevideo);
            descarga = itemView.findViewById(R.id.descargarvideo);
            fecha = itemView.findViewById(R.id.fechavideo);
            borrarvideo = itemView.findViewById(R.id.linkBorrarVideos);


            DocumentReference referenceUser = firebaseFirestore.collection("users").document(firebaseAuth.getCurrentUser().getUid());
            referenceUser.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    tipo =  documentSnapshot.get("tipoUsuario").toString().toLowerCase();

                    if(tipo.equalsIgnoreCase("visitante")){

                        borrarvideo.setVisibility(View.GONE);}
                }});




            vervideo = itemView.findViewById(R.id.botonver);

        }
        void blindData(final VideosT videosT) {
            nombre.setText(videosT.getNombre());
            fecha.setText(videosT.getFecha());
            vervideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(videosT.getDescarga().getResult() != null) {
                        Intent i = new Intent(Intent.ACTION_VIEW, videosT.getDescarga().getResult());

                        try {
                            context.startActivity(i);
                        }catch (ActivityNotFoundException e){
                            Log.d("AYAYAI", "error es "  + e.getMessage());
                        }

                    }


                }
            });
            descarga.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Descargar descargar = new Descargar();
                    Uri uriDescarga;


                    uriDescarga = videosT.getDescarga().getResult();
                    descargar.downloadFile(uriDescarga, videosT.getNombre(), context);
                }

            });

            borrarvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    final AlertDialog dialog = new AlertDialog.Builder(context)
                            .setTitle("Mensaje de alerta")
                            .setMessage("¿Estás seguro que de sea borrar este video?")
                            .setPositiveButton("Sí",null)
                            .setNeutralButton("Cancelar",null)
                            .show();
                    Button neutralboton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                    Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);


                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            firebaseAuth = FirebaseAuth.getInstance();
                            firebaseStorage = FirebaseStorage.getInstance();
                            firebaseStorage.getReference().child("general").child(firebaseAuth.getCurrentUser().getUid() + ".mp4").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                                @Override
                                public void onSuccess(ListResult listResult) {
                                    for (StorageReference ref : listResult.getItems()) {

                                        final VideosT compaVideos = new VideosT();
                                        compaVideos.setNombre(ref.getName());
                                        if(compaVideos.getNombre().equalsIgnoreCase(videosT.getNombre()))
                                        {
                                            firebaseStorage.getReference().child("general").child(compaVideos.getNombre()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Video borrado exitosamente", Toast.LENGTH_SHORT).show();
                                                    try {
                                                        wait();
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d("AYAYAI", "onFailure: " +e.getMessage());
                                                    Toast.makeText(context, "El video no se pudo borrar", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        break;}

                                        }

                                }

                            });


                            dialog.dismiss();

                        }
                    });


                    neutralboton.setOnClickListener(new View.OnClickListener() {
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
