package com.jaaziel.irc.etherfi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;

public class EthernetMain extends AppCompatActivity {

    EditText macfuente, macdestino, datos, campoTrama;
    Button generar;
    String tramaFinal, pream, tipo, sof, crc, relleno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ethernet_main);

        macfuente = findViewById(R.id.edit_fue);
        macdestino = findViewById(R.id.edit_dest);
        datos = findViewById(R.id.edit_datos);
        campoTrama = findViewById(R.id.campo_trama);
        generar = findViewById(R.id.generador);
        pream = "AAAAAAAAAAAAAA";
        tipo = "0806";
        sof = "AB";
        crc = "374B0707";

        generar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( (macdestino.getText().length()<12)||(macfuente.getText().length()<12) ){

                    if( macdestino.getText().length()<12 ){
                        macdestino.setError("Escribe una dirección MAC destino\n" +
                                "de 12 caracteres hexadecimales\n" +
                                "Valores válidos de 0-9 y A-F");
                        Toast.makeText(EthernetMain.this,"Error: Verifique los campos",Toast.LENGTH_SHORT).show();
                    }
                    if( macfuente.getText().length()<12 ) {
                        macfuente.setError("Escribe una dirección MAC fuente\n" +
                                "de 12 caracteres hexadecimales\n" +
                                "Valores válidos de 0-9 y A-F");
                        Toast.makeText(EthernetMain.this, "Error: Verifique los campos", Toast.LENGTH_SHORT).show();
                    }
                    /*if( datos.getText().equals("") ){
                        Toast.makeText(EthernetMain.this, "Ingrese algun dato", Toast.LENGTH_SHORT).show();
                    }*/
                } else {
                    campoTrama.setText("");
                    relleno = relleno(datos.getText().toString());
                    tramaFinal = pream+sof+macdestino.getText().toString().toUpperCase()
                            +macfuente.getText().toString().toUpperCase()+tipo
                            +toHex(datos.getText().toString()).toUpperCase()+relleno+crc;
                    campoTrama.setTextColor(Color.BLACK);
                    campoTrama.setText(tramaFinal);
                }
            }
        });
    }

    public static class DialogAlerta extends DialogFragment{
        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            String st = "Jaaziel Isai Rebollar Calzada\n" +
                    "Miguel Ángel Reséndiz García";
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
            builder.setMessage(st).setTitle("Integrantes de equipo")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            return builder.create();
        }
    }

    public String toHex( String arg ) {
        try{
            return String.format("%x", new BigInteger(1, arg.getBytes("UTF8")));
        } catch( Exception e ){
            Toast.makeText(EthernetMain.this,"Error en la conversión",Toast.LENGTH_SHORT).show();
        }
        return "";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(menu.NONE,11,menu.NONE,"Reiniciar campos");
        menu.add(menu.NONE,10,menu.NONE,"Acerca de la App");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == 10 ){
            FragmentManager fm = getSupportFragmentManager();
            DialogAlerta dialogAlerta = new DialogAlerta();
            dialogAlerta.show(fm,"Información");
        }
        if( id == 11 ){
            reinicio();
        }
        return super.onOptionsItemSelected(item);
    }

    public int byt( String tx ){
        return (toHex(tx).length()/4)*2;
    }

    public void reinicio(){
        macdestino.setText("");
        macfuente.setText("");
        datos.setText("");
        campoTrama.setText("");
    }

    public String relleno( String datos ){
        int cou = byt(datos)+18;
        String res = "";
        while( cou < 46 ){
            cou++;
            res+="00";
        }
        return res+res+res+res;
    }
}
