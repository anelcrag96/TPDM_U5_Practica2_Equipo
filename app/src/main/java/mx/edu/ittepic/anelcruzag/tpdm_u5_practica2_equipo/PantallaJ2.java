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

public class PantallaJ2 extends AppCompatActivity {
    private DatabaseReference realtime;
    String[] nom = {"", "piedra", "papel", "tijera"};
    TextView nombre1, estado1, nombre2, estado2, ganador, perdedor, iniciar;
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

        nombre1 = findViewById(R.id.lblNombre_J1);
        estado1 = findViewById(R.id.lblEstado_J1);

        nombre2 = findViewById(R.id.lblNombre_J2);
        estado2 = findViewById(R.id.lblEstado_J2);

        ganador = findViewById(R.id.lblGanadorJ1);
        perdedor = findViewById(R.id.lblPerdedorJ1);

        iniciar = findViewById(R.id.iniciar);

        objeto1 = findViewById(R.id.objeto1);
        objeto2 = findViewById(R.id.objeto2);

        realtime = FirebaseDatabase.getInstance().getReference();

        contador = 0;
        // Sensor manager es quien se encarga de saber que sensor vamos a administrar.
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // Validar si el dispositivo cuenta con el sensor
        if (sensor == null) {
            finish();
        }// if

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];

                if (iniciar.getVisibility() == View.VISIBLE) {
                    if (x < -5 && contador == 0) {
                        contador++;
                    } else if (x > 5 && contador == 1) {
                        contador++;
                    }

                    if (contador == 2) {
                        int num = (int) ((Math.random() * 3) + 1);
                        System.out.println("RANDOM: " + num);
                        establecer_img(num);
                        metodoEnviarObjeto("" + num);
                        contador = 0;
                    }
                } else {
                    int id = getResources().getIdentifier("blanco", "drawable", getPackageName());
                    objeto1.setImageResource(id);
                    System.out.println("INVISIBLE");
                }
            }// onSensorChanged

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }//onCreate

    @Override
    protected void onStart() {
        consultarJuegoEnCurso();
        super.onStart();
    }

    private void metodoEnviarObjeto(String num) {
        System.out.println("Objeto2 " + num);
        realtime.child("lobby").child("juego1").child("objeto2").setValue(num);
    }

    private void consultarJuegoEnCurso() {
        ganador.setVisibility(View.INVISIBLE);
        perdedor.setVisibility(View.INVISIBLE);

        realtime.child("lobby").child("juego1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Juego juego = dataSnapshot.getValue(Juego.class);
                System.out.println("ENTRO1");

                if (juego.ready2.equals("")) {
                    nombre2.setVisibility(View.INVISIBLE);
                    iniciar.setVisibility(View.INVISIBLE);
                    estado2.setText("Esperando ...");
                    estado2.setTextColor(Color.rgb(0, 0, 0));
                } else {
                    estado2.setText("¡Listo!");
                    nombre2.setVisibility(View.VISIBLE);
                    nombre2.setText("" + juego.sobre1);
                    iniciar.setVisibility(View.VISIBLE);
                }
                nombre1.setText("" + juego.sobre2);
                if (juego.objeto1.equals("1")) {
                    establecer_img1(1);
                } else if (juego.objeto1.equals("2")) {
                    establecer_img1(2);
                } else if (juego.objeto1.equals("3")) {
                    establecer_img1(3);
                }

                if (ganador.getVisibility() == View.VISIBLE) {
                    Toast.makeText(PantallaJ2.this, "Terminar el juego!", Toast.LENGTH_LONG).show();
                }
            }// onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }// consultarTodos


    private void metodoGanador(String objeto1, String objeto2) {
        // Gana piedra
        System.out.println("OBJETO1: " + objeto1 + " OBJETO2: " + objeto2);
        ganador.setVisibility(View.INVISIBLE);
        perdedor.setVisibility(View.INVISIBLE);
        switch (objeto1) {
            case "1":
                if ((objeto1.equals("1") && objeto2.equals("3"))) {

                    perdedor.setVisibility(View.VISIBLE);

                } else if ((objeto1.equals("1") && objeto2.equals("2"))) {
                    sonido();
                    ganador.setVisibility(View.VISIBLE);
                }
                break;

            case "2":

                if ((objeto1.equals("2") && objeto2.equals("1"))) {

                    perdedor.setVisibility(View.VISIBLE);

                } else if ((objeto1.equals("2") && objeto2.equals("3"))) {
                    sonido();
                    ganador.setVisibility(View.VISIBLE);
                }
                break;

            case "3":
                if ((objeto1.equals("3") && objeto2.equals("2"))) {
                    ganador.setVisibility(View.VISIBLE);
                    sonido();
                } else if ((objeto1.equals("3") && objeto2.equals("1"))) {
                    perdedor.setVisibility(View.VISIBLE);
                }
                break;
        }
    }// metodoGanador


    public void sonido() {
        // Acceder al recurso de tipo sonido

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.winner);
        mediaPlayer.start();

    }// sonido


    public void iniciar() {
        sensorManager.registerListener(sensorEventListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }// iniciar


    public void detener() {
        sensorManager.unregisterListener(sensorEventListener);
    }// detener


    @Override
    protected void onPause() {
        super.onPause();
        detener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        iniciar();
    }

    public void establecer_img(int numero) {
        int id = getResources().getIdentifier(nom[numero], "drawable", getPackageName());
        objeto1.setImageResource(id);

    }

    public void establecer_img1(int numero) {
        int id = getResources().getIdentifier(nom[numero], "drawable", getPackageName());
        objeto2.setImageResource(id);

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
