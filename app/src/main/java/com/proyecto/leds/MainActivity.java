package com.proyecto.leds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.LineGraphSeries;

public class MainActivity extends AppCompatActivity {

    TextView potenciometro;
    Button btn_led;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    int estado_led = 0;
//    private LineGraphSeries mSeries;

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        potenciometro = (TextView)findViewById(R.id.txt_potenciometro);
        btn_led = (Button)findViewById(R.id.btn_led);
//        graph = (GraphView) findViewById(R.id.graph);
//        mSeries = new LineGraphSeries<>();
//        graph.addSeries(mSeries);
//        graph.getViewport().setXAxisBoundsManual(true);

        btn_led.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (estado_led == 0){
                    mDatabase.child("Casa").child("Luz").setValue(1);
                    estado_led = 1;
                }else{
                    mDatabase.child("Casa").child("Luz").setValue(0);
                    estado_led = 0;
                }
            }
        });

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("FIREBASE", snapshot.getValue().toString());
                Data post = snapshot.getValue(Data.class);
                potenciometro.setText(String.valueOf(post.getPotenciometro()));
                estado_led = post.getLuz();
                if (post.getLuz() == 1){
                    btn_led.setText("LED ENCENDIDO");
                }else{
                    btn_led.setText("LED APAGADO");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("Casa").addValueEventListener(postListener);
    }


}