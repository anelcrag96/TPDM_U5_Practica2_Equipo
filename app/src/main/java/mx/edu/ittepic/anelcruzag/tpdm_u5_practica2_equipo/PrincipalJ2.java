package mx.edu.ittepic.anelcruzag.tpdm_u5_practica2_equipo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PrincipalJ2 extends AppCompatActivity {
    EditText telefonoJ2, nombreJ2;
    TextView lblTelefonoJ2, lblNombreJ2;
    Button retarJ2, ingresarJ2;
    String bandera;

    private static final int MY_PERMISSIONS_REQUEST_RECIEVER_SMS = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;

    DatabaseReference realtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_j2);
        lblTelefonoJ2=findViewById(R.id.lblTelefonoJ2);
        telefonoJ2 = findViewById(R.id.txtTelefonoJ2);
        retarJ2 = findViewById(R.id.btnRetarJ2);

        lblNombreJ2=findViewById(R.id.lblNombreJ2);
        nombreJ2 = findViewById(R.id.txtNombreJ2);
        ingresarJ2 = findViewById(R.id.btnIngresarJ2);


        Intent intent = getIntent();
        bandera = intent.getStringExtra("bandera");
        validar(bandera);
        realtime = FirebaseDatabase.getInstance().getReference();
        permisos();

        retarJ2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (!telefonoJ2.getText().toString().isEmpty()) {
                    metodoInsertarDatos("Jugador1", telefonoJ2.getText().toString());
                    enviarMensaje(telefonoJ2.getText().toString());
                    Intent ventana = new Intent(PrincipalJ2.this, PantallaJ1.class);
                    startActivity(ventana);
                    finishAffinity();
                }//if
            }
        });

        ingresarJ2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                metodoInsertarDatosP2(nombreJ2.getText().toString());

                Intent ventana = new Intent(PrincipalJ2.this, PantallaJ2.class);
                startActivity(ventana);
                finish();
            }
        });
    }//onCreate

    private void validar(String bandera) {
        System.out.println("BANDERA: " + bandera);
        if (bandera.equals("0")) {
            retarJ2.setVisibility(View.VISIBLE);
            lblTelefonoJ2.setVisibility(View.VISIBLE);
            telefonoJ2.setVisibility(View.VISIBLE);
        }//if
        else {
            ingresarJ2.setVisibility(View.VISIBLE);
            lblNombreJ2.setVisibility(View.VISIBLE);
            nombreJ2.setVisibility(View.VISIBLE);
        }//else
    }//validar


    private void consultarJuegoEnCurso() {
        realtime.child("lobby").child("juego1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Juego juego = dataSnapshot.getValue(Juego.class);
                System.out.println("ENTRO1");
                if (juego.ready1.equals("")) {
                    retarJ2.setVisibility(View.VISIBLE);
                    telefonoJ2.setVisibility(View.VISIBLE);
                }//if
            }// onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }//onCancelled
        });
    }//consultarTodos

    private void metodoInsertarDatos(String usuario, String telefono) {
        // Game juego = new Game("", usuario, "", telefono, "", "", "", "1", "NO", "NO");
        System.out.println("TELEFONO " + telefono + " " + usuario);
        realtime.child("lobby").child("juego1").child("num2").setValue(telefono);
        realtime.child("lobby").child("juego1").child("ready1").setValue("1");
        finish();
    }//metodoInsertarDatos

    private void metodoInsertarDatosP2(String usuario) {
        realtime.child("lobby").child("juego1").child("sobre2").setValue(usuario);
        realtime.child("lobby").child("juego1").child("ready2").setValue("1");
        finish();
    }// metodoInsertarDatos

    private void enviarMensaje(String celular) {
        SmsManager sms = SmsManager.getDefault();
        ArrayList<String> parts = sms.divideMessage("Te reto a un juego de piedra, papel o tijera!\nIngresa al lobby para jugar. ¡SUERTE!");
        sms.sendMultipartTextMessage(celular, null, parts, null, null);
        System.out.println("Número " + celular);
        Toast.makeText(this, "Rival retado!", Toast.LENGTH_LONG).show();
        telefonoJ2.setText("");
    }// enviarMensaje


    private void permisos() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }//if
        }//if

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {
            }//if
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSIONS_REQUEST_RECIEVER_SMS);
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_RECIEVER_SMS);
            }//else
        }//if
    }//permisos
}//class
