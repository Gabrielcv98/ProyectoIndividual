package pe.pucp.dduu.proyectoindividual;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;


import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;


public class Descargar {


    public void downloadFile(Uri uri , String nombre , Context context) {


        File externalStoragePublicDirectory =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);


        File file = new File(externalStoragePublicDirectory, nombre);
        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle("Descargando video")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverRoaming(true);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        Toast.makeText(context, "Descarga Completada", Toast.LENGTH_SHORT).show();
    }

}
