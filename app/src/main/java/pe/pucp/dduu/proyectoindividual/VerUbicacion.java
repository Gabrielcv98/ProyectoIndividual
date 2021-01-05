package pe.pucp.dduu.proyectoindividual;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class VerUbicacion extends AppCompatActivity implements OnMapReadyCallback {

    //Initialize variable
    GoogleMap gMap;
    Double latitud1;
    Double longitud1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ubicacion);

        //obtain the SupportMapFragment
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map1);
        supportMapFragment.getMapAsync(this);
        latitud1 = getIntent().getDoubleExtra("latitud",0);
        longitud1 = getIntent().getDoubleExtra("longitud", 0);
        Toast.makeText(this, "Tocar la pantalla para ver la ubicación", Toast.LENGTH_SHORT).show();

    }


    LatLng latLng1;


    public LatLng getLatLng1() {
        return latLng1;
    }

    public void setLatLng1(LatLng latLng1) {
        this.latLng1 = latLng1;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e("AYAYAI", "Falló el style");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("AYAYAI", "No se pudo encontrar el estilo Error: ", e);
        }

        //Asign variable
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //creating marker
                MarkerOptions markerOptions = new MarkerOptions();
                //set marker position
                markerOptions.position(latLng);
                //set latitude and loca
                latLng = new LatLng(latitud1, longitud1);

                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                //add marker on map
                gMap.addMarker(markerOptions.position(latLng).title("Casa ItaLion"));
                Log.d("infoApp", latLng.latitude +"/" + latLng.longitude);

            }
        });
    }
}