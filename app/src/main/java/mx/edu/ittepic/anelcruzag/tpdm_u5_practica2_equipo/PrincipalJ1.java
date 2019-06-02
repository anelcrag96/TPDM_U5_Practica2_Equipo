package mx.edu.ittepic.anelcruzag.tpdm_u5_practica2_equipo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalJ1 extends AppCompatActivity {
    private Button ingresarJ1;
    private EditText nombreJ1, telefonoJ1;

    private DatabaseReference realtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_j1);
        ingresarJ1 = findViewById(R.id.btnIngresar);
        nombreJ1 = findViewById(R.id.txtNombreJ1);
        telefonoJ1 = findViewById(R.id.txtTelefonoJ1);

        realtime = FirebaseDatabase.getInstance().getReference();

        ingresarJ1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar(nombreJ1.getText().toString(), telefonoJ1.getText().toString())) {
                    finish();
                    metodoInsertarDatos(nombreJ1.getText().toString(), telefonoJ1.getText().toString());
                    Intent ventana = new Intent(PrincipalJ1.this, PrincipalJ2.class);
                    ventana.putExtra("bandera", "0");
                    startActivity(ventana);
                }//if
            }
        });
        consultarJuegoEnCurso();
    }// onCreate


    private void metodoInsertarDatos(String usuario, String telefono) {
        Juego juego = new Juego(usuario, "", telefono, "", "", "", "", "", "NO", "NO");
        realtime.child("lobby").child("juego1").setValue(juego).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Éxito! Inseratado correctamente");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("Error! No insertado");
            }//onFailure
        });
    }// metodoInsertarDatos


    private boolean validar(String nombre, String telefono) {
        System.out.println("CLICK " + nombre + " " + telefono);
        if (!nombre.isEmpty() || !telefono.isEmpty()) {
            return true;
        }//if
        return false;
    }//validar


    private void consultarJuegoEnCurso() {
        realtime.child("lobby").child("juego1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Juego juego = dataSnapshot.getValue(Juego.class);
                if (!juego.finish.isEmpty()) {
                    if (juego.sobre2.isEmpty() && juego.ready2.isEmpty()) {
                        Intent ventana = new Intent(PrincipalJ1.this, PrincipalJ2.class);
                        ventana.putExtra("bandera", "1");
                        startActivity(ventana);
                    }//if
                }//if
            }//onDataChange

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }//onCancelled
        });
    }//consultarTodos
}//class
