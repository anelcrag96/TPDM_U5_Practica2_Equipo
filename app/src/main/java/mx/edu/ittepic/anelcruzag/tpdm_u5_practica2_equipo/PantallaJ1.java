package mx.edu.ittepic.anelcruzag.tpdm_u5_practica2_equipo;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PantallaJ1 extends AppCompatActivity {
    private DatabaseReference realtime;
    String[] nom = {"", "piedra", "papel", "tijera"};
    TextView nombre_J1, estado_J1, nombre_J2, estado_J2, ganadorJ1, perdedorJ1, iniciar;
    ImageView objeto1, objeto2;
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    int contador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_j1);

        nombre_J1 = findViewById(R.id.lblNombre_J1);
        estado_J1 = findViewById(R.id.lblEstado_J1);

        nombre_J2 = findViewById(R.id.lblNombre_J2);
        estado_J2 = findViewById(R.id.lblEstado_J2);

        ganadorJ1 = findViewById(R.id.lblGanadorJ1);
        perdedorJ1 = findViewById(R.id.lblPerdedorJ1);

        iniciar = findViewById(R.id.iniciar);

        objeto1 = findViewById(R.id.objeto1);
        objeto2 = findViewById(R.id.objeto2);

        realtime = FirebaseDatabase.getInstance().getReference();

        contador = 0;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensor == null) {
            finish();
        }//if

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                if (iniciar.getVisibility() == View.VISIBLE) {
                    if (x < -5 && contador == 0) {
                        contador++;
                    }//if
                    else if (x > 5 && contador == 1) {
                        contador++;
                    }//else if
                    if (contador == 2) {
                        int num = (int) ((Math.random() * 3) + 1);
                        System.out.println("RANDOM: " + num);
                        establecer_img(num);
                        metodoEnviarObjeto("" + num);
                        contador = 0;
                    }//if
                }//if
                else {
                    int id = getResources().getIdentifier("blanco", "drawable", getPackageName());
                    objeto1.setImageResource(id);
                    System.out.println("INVISIBLE");
                }//else
            }// onSensorChanged

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }//onAccuracyChanged
        };
    }//onCreate

    private void metodoEnviarObjeto(String num) {
        System.out.println("Objeto1 " + num);
        realtime.child("lobby").child("juego1").child("objeto1").setValue(num);
    }//metodoEnviarObjeto

    @Override
    protected void onStart() {
        consultarJuegoEnCurso();
        super.onStart();
    }//onStart

    private void consultarJuegoEnCurso() {
        ganadorJ1.setVisibility(View.INVISIBLE);
        perdedorJ1.setVisibility(View.INVISIBLE);

        realtime.child("lobby").child("juego1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Juego juego = dataSnapshot.getValue(Juego.class);
                System.out.println("ENTRO1");
                if (juego.ready2.equals("")) {
                    nombre_J2.setVisibility(View.INVISIBLE);
                    iniciar.setVisibility(View.INVISIBLE);
                    estado_J2.setText("Esperando...");
                    estado_J2.setTextColor(Color.rgb(0, 0, 0));
                }//if
                else {
                    estado_J2.setText("¡Listo!");
                    nombre_J2.setVisibility(View.VISIBLE);
                    nombre_J2.setText("" + juego.sobre2);
                    iniciar.setVisibility(View.VISIBLE);
                }//else
                nombre_J1.setText("" + juego.sobre1);
                if (juego.finish.equals("")) {
                }// if
                metodoGanador(juego.objeto1, juego.objeto2);
                if (juego.objeto2.equals("1")) {
                    // Piedra
                    establecer_img1(1);
                }//if
                if (juego.objeto2.equals("2")) {
                    //Papel
                    establecer_img1(2);
                }//if
                else if (juego.objeto2.equals("3")) {
                    //Tijera
                    establecer_img1(3);
                }//else if
            }// onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }//onCancelled
        });
    }// consultarTodos

    private void metodoGanador(String objeto1, String objeto2) {
        // Gana piedra
        System.out.println("OBJETO1: " + objeto1 + " OBJETO2: " + objeto2);
        ganadorJ1.setVisibility(View.INVISIBLE);
        perdedorJ1.setVisibility(View.INVISIBLE);
        switch (objeto1) {
            case "1":
                if ((objeto1.equals("1") && objeto2.equals("3"))) {
                    ganadorJ1.setVisibility(View.VISIBLE);
                    sonido();
                }//if
                else if ((objeto1.equals("1") && objeto2.equals("2"))) {
                    perdedorJ1.setVisibility(View.VISIBLE);
                }//else if
                break;
            case "2":
                if ((objeto1.equals("2") && objeto2.equals("1"))) {
                    ganadorJ1.setVisibility(View.VISIBLE);
                    sonido();
                }//if
                else if ((objeto1.equals("2") && objeto2.equals("3"))) {
                    perdedorJ1.setVisibility(View.VISIBLE);
                }//else if
                break;
            case "3":
                if ((objeto1.equals("3") && objeto2.equals("2"))) {
                    ganadorJ1.setVisibility(View.VISIBLE);
                    sonido();
                }//if
                else if ((objeto1.equals("3") && objeto2.equals("1"))) {
                    perdedorJ1.setVisibility(View.VISIBLE);
                }//else if
                break;
        }//switch
    }// metodoGanador

    public void establecer_img1(int numero) {
        int id = getResources().getIdentifier(nom[numero], "drawable", getPackageName());
        objeto2.setImageResource(id);
    }//establecer_img1

    public void sonido() {
        // Acceder al recurso de tipo sonido
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.winner);
        mediaPlayer.start();
    }// sonido

    public void iniciar() {
        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }// iniciar

    public void detener() {
        sensorManager.unregisterListener(sensorEventListener);
    }// detener

    @Override
    protected void onPause() {
        super.onPause();
        detener();
    }//onPause

    @Override
    protected void onResume() {
        super.onResume();
        iniciar();
    }//onResume

    public void establecer_img(int numero) {
        int id = getResources().getIdentifier(nom[numero], "drawable", getPackageName());
        objeto1.setImageResource(id);
    }//establecer_img

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }//onBackPressed
}
