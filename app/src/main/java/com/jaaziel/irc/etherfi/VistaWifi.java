package com.jaaziel.irc.etherfi;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.ArrayList;

public class VistaWifi extends AppCompatActivity {

    private Button genera;
    private EditText destino, origen;
    private Spinner tipo, subtipo;

    EditText[] edt = new EditText[17];
    TextView campotrama;
    Button generar;
    String cDatos, cAdmon, cControl, finDatos, finAdmon, finControl, crc, Subtipo;
    int sub_tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_wifi);

        crc = "374B0707";
//        crc = CRC("Hola");
        campotrama = (TextView) findViewById(R.id.salida);

        edt[0] = (EditText)findViewById(R.id.version);

        //edt[1] = (EditText)findViewById(R.id.ed_subtipo);

        edt[2] = (EditText)findViewById(R.id.hacia);
        edt[3] = (EditText)findViewById(R.id.desde);

        edt[4] = (EditText)findViewById(R.id.mf);
        edt[5] = (EditText)findViewById(R.id.reintentar);
        edt[6] = (EditText)findViewById(R.id.pwr);
        edt[7] = (EditText)findViewById(R.id.mas);
        edt[8] = (EditText)findViewById(R.id.wep);
        edt[9] = (EditText)findViewById(R.id.orden);
        edt[10] = (EditText)findViewById(R.id.duracion);
        edt[11] = (EditText)findViewById(R.id.macOr);
        edt[12] = (EditText)findViewById(R.id.macDes);
        edt[13] = (EditText)findViewById(R.id.macTx);
        edt[14] = (EditText)findViewById(R.id.macRx);
        edt[15] = (EditText)findViewById(R.id.secuencia);
        edt[16] = (EditText)findViewById(R.id.datos);

        subtipo = (Spinner) findViewById(R.id.subtipo);
        genera = (Button) findViewById(R.id.genera);
        tipo = (Spinner) findViewById(R.id.tipo);
        tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (tipo.getSelectedItem().toString().equals ("Administración")) {
                    edt[14].setEnabled(false);
                    edt[14].setText("");
                    edt[13].setEnabled(true);
                    edt[13].setText("");
                    edt[16].setText("");
                    edt[16].setEnabled(true);
                    ArrayAdapter adapter = ArrayAdapter.createFromResource(VistaWifi.this,R.array.administracion,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subtipo.setAdapter(adapter);
                }
                if (tipo.getSelectedItem().toString().equals ("Control")) {
                    edt[14].setEnabled(false);
                    edt[14].setText("");
                    edt[13].setEnabled(false);
                    edt[13].setText("");
                    edt[16].setText("");
                    edt[16].setEnabled(false);
                    ArrayAdapter adapter = ArrayAdapter.createFromResource(VistaWifi.this,R.array.control,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subtipo.setAdapter(adapter);
                }
                if (tipo.getSelectedItem().toString().equals ("Datos")) {
                    edt[14].setEnabled(true);
                    edt[13].setEnabled(true);
                    edt[16].setText("");
                    edt[16].setEnabled(true);
                    ArrayAdapter adapter = ArrayAdapter.createFromResource(VistaWifi.this,R.array.datos,android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subtipo.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        //

        genera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verificaCampos ( )) return;
                if (tipo.getSelectedItem().toString().equals ("Administración")) {
                    campotrama.setText("");
                    sub_tipo = subtipo.getSelectedItemPosition( );
                    if (sub_tipo == 0) { Subtipo = "0000"; }
                    if (sub_tipo == 1) { Subtipo = "1011"; }
                    if (sub_tipo == 2) { Subtipo = "1100"; }
                    tramaAdmin ( );
                    campotrama.setText("Trama de Administración\n"+finAdmon);
                }
                if (tipo.getSelectedItem().toString().equals ("Control")) {
                    campotrama.setText("");
                    sub_tipo = subtipo.getSelectedItemPosition( );
                    if (sub_tipo == 0) { Subtipo = "1011"; }
                    if (sub_tipo == 1) { Subtipo = "1100"; }
                    if (sub_tipo == 2) { Subtipo = "1101"; }

                    tramaControl ( );
                    campotrama.setText("Trama de Control\n"+finControl);
                }
                if (tipo.getSelectedItem().toString().equals ("Datos")) {
                    campotrama.setText("");
                    sub_tipo = subtipo.getSelectedItemPosition( );
                    if (sub_tipo == 0) { Subtipo = "0000"; }
                    if (sub_tipo == 1) { Subtipo = "0100"; }
                    if (sub_tipo == 2) { Subtipo = "1000"; }
                    tramaDatos ( );
                    campotrama.setText("Trama de Datos\n"+finDatos);
                }
            }
        });

    }

    public void tramaControl ( ) {
        cControl = edt[0].getText().toString()+"01"+Subtipo+edt[2].getText().toString()
                +edt[3].getText().toString()+edt[4].getText().toString()+edt[5].getText().toString()
                +edt[6].getText().toString()+edt[7].getText().toString()+edt[8].getText().toString()
                +edt[9].getText().toString();

        if( edt[2].getText().toString().equals("0")&&edt[3].getText().toString().equals("0") ){
            int decon = Integer.parseInt(cControl,2);
            cControl = Integer.toHexString(decon); //cControl en hexa

            finControl = cControl+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[12].getText().toString()+edt[13].getText().toString()+crc; //HaciaDS=0 y DesdeDS=0
            finControl = finControl.toUpperCase();
        }
        if( edt[2].getText().toString().equals("0")&&edt[3].getText().toString().equals("1") ){
            int decon = Integer.parseInt(cControl,2);
            cControl = Integer.toHexString(decon); //cControl en hexa

            finControl = cControl+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[13].getText().toString()+edt[12].getText().toString()+crc; //HaciaDS=0 y DesdeDS=1
            finControl = finControl.toUpperCase();
        }
        if( edt[2].getText().toString().equals("1")&&edt[3].getText().toString().equals("0") ){
            int decon = Integer.parseInt(cControl,2);
            cControl = Integer.toHexString(decon); //cControl en hexa

            finControl = cControl+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[12].getText().toString()+edt[13].getText().toString()+crc; //HaciaDS=0 y DesdeDS=0
            finControl = finControl.toUpperCase();
        }
        if( edt[2].getText().toString().equals("1")&&edt[3].getText().toString().equals("1") ){
            int decon = Integer.parseInt(cControl,2);
            cControl = Integer.toHexString(decon); //cControl en hexa

            finControl = cControl+edt[10].getText().toString()+edt[13].getText().toString()+
                    edt[14].getText().toString()+edt[11].getText().toString()+crc; //HaciaDS=0 y DesdeDS=1
            finControl = finControl.toUpperCase();
        }
    }

    public void tramaAdmin ( ) {
        cAdmon = edt[0].getText().toString()+"00"+Subtipo+edt[2].getText().toString()
                +edt[3].getText().toString()+edt[4].getText().toString()+edt[5].getText().toString()
                +edt[6].getText().toString()+edt[7].getText().toString()+edt[8].getText().toString()
                +edt[9].getText().toString();

        if( edt[2].getText().toString().equals("0")&&edt[3].getText().toString().equals("0") ){
            int decadm = Integer.parseInt(cAdmon,2);
            cAdmon = Integer.toHexString(decadm); //cAdmon en hexa

            finAdmon = cAdmon+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[12].getText().toString()+edt[13].getText().toString()+
                    edt[15].getText().toString()+toHex(edt[16].getText().toString())+crc; //HaciaDS=0 y DesdeDS=0
            finAdmon = finAdmon.toUpperCase();
        }
        if( edt[2].getText().toString().equals("0")&&edt[3].getText().toString().equals("1") ){
            int decadm = Integer.parseInt(cAdmon,2);
            cAdmon = Integer.toHexString(decadm); //cAdmon en hexa

            finAdmon = cAdmon+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[13].getText().toString()+edt[12].getText().toString()+
                    edt[15].getText().toString()+toHex(edt[16].getText().toString())+crc; //HaciaDS=0 y DesdeDS=1
            finAdmon = finAdmon.toUpperCase();
        }
        if( edt[2].getText().toString().equals("1")&&edt[3].getText().toString().equals("0") ){
            int decadm = Integer.parseInt(cAdmon,2);
            cAdmon = Integer.toHexString(decadm); //cAdmon en hexa

            finAdmon = cAdmon+edt[10].getText().toString()+edt[13].getText().toString()+
                    edt[12].getText().toString()+edt[11].getText().toString()+
                    edt[15].getText().toString()+toHex(edt[16].getText().toString())+crc; //HaciaDS=1 y DesdeDS=0
            finAdmon = finAdmon.toUpperCase();
        }
        if( edt[2].getText().toString().equals("1")&&edt[3].getText().toString().equals("1") ){
            int decadm = Integer.parseInt(cAdmon,2);
            cAdmon = Integer.toHexString(decadm); //cAdmon en hexa

            finAdmon = cAdmon+edt[10].getText().toString()+edt[13].getText().toString()+
                    edt[14].getText().toString()+edt[11].getText().toString()+
                    edt[15].getText().toString()+edt[12].getText().toString()+
                    toHex(edt[16].getText().toString())+crc; //HaciaDS=1 y DesdeDS=1
            finAdmon = finAdmon.toUpperCase();
        }

    }

    public void tramaDatos ( ) {
        cDatos = edt[0].getText().toString()+"10"+Subtipo+edt[2].getText().toString()
                +edt[3].getText().toString()+edt[4].getText().toString()+edt[5].getText().toString()
                +edt[6].getText().toString()+edt[7].getText().toString()+edt[8].getText().toString()
                +edt[9].getText().toString();

        if( edt[2].getText().toString().equals("0")&&edt[3].getText().toString().equals("0") ){
            int decDatos = Integer.parseInt(cDatos,2);
            cDatos = Integer.toHexString(decDatos); //cDatos ya está en hexadecimal

            finDatos = cDatos+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[12].getText().toString()+edt[13].getText().toString()+
                    edt[15].getText().toString()+edt[14].getText().toString()+
                    toHex(edt[16].getText().toString())+crc; //trama final HaciaDS=0 y DesdeDS=0
            finDatos = finDatos.toUpperCase();
        }
        if( edt[2].getText().toString().equals("0")&&edt[3].getText().toString().equals("1") ){
            int decDatos = Integer.parseInt(cDatos,2);
            cDatos = Integer.toHexString(decDatos); //cDatos ya está en hexadecimal

            finDatos = cDatos+edt[10].getText().toString()+edt[11].getText().toString()+
                    edt[13].getText().toString()+edt[12].getText().toString()+
                    edt[15].getText().toString()+edt[14].getText().toString()+
                    toHex(edt[16].getText().toString())+crc; //trama final HaciaDS=0 y DesdeDS=1
            finDatos = finDatos.toUpperCase();
        }
        if( edt[2].getText().toString().equals("1")&&edt[3].getText().toString().equals("0") ){
            int decDatos = Integer.parseInt(cDatos,2);
            cDatos = Integer.toHexString(decDatos); //cDatos ya está en hexadecimal

            finDatos = cDatos+edt[10].getText().toString()+edt[13].getText().toString()+
                    edt[12].getText().toString()+edt[11].getText().toString()+
                    toHex(edt[15].getText().toString())+edt[14].getText().toString()+
                    toHex(edt[16].getText().toString())+crc; //trama final HaciaDS=1 y DesdeDS=0
            finDatos = finDatos.toUpperCase();
        }
        if( edt[2].getText().toString().equals("1")&&edt[3].getText().toString().equals("1") ) {
            int decDatos = Integer.parseInt(cDatos, 2);
            cDatos = Integer.toHexString(decDatos); //cDatos ya está en hexadecimal

            finDatos = cDatos + edt[10].getText().toString() + edt[13].getText().toString() +
                    edt[14].getText().toString() + edt[11].getText().toString() +
                    edt[15].getText().toString() + edt[12].getText().toString() +
                    toHex(edt[16].getText().toString()) + crc; //trama final HaciaDS=1 y DesdeDS=1
            finDatos = finDatos.toUpperCase();
        }
    }

    public static class DialogAlerta extends DialogFragment {
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
            EthernetMain.DialogAlerta dialogAlerta = new EthernetMain.DialogAlerta();
            dialogAlerta.show(fm,"Información");
        }
        if( id == 11 ){
            reiniciar();
            reiniciar();
        }
        return super.onOptionsItemSelected(item);
    }

    public void buscaError ( ) {

        if( edt[0].getText().length()<2 ){
            edt[0].setError("Deben escribirse 2 bits");
        } if( edt[2].getText().length()<1 ){
            edt[2].setError("Debe escribirse 1 bit");
        } if( edt[3].getText().length()<1 ){
            edt[3].setError("Debe escribirse 1 bit");
        } if( edt[4].getText().length()<1 ){
            edt[4].setError("Debe escribirse 1 bit");
        } if( edt[5].getText().length()<1 ){
            edt[5].setError("Debe escribirse 1 bit");
        } if( edt[6].getText().length()<1 ){
            edt[6].setError("Debe escribirse 1 bit");
        } if( edt[7].getText().length()<1 ){
            edt[7].setError("Debe escribirse 1 bit");
        } if( edt[8].getText().length()<1 ){
            edt[8].setError("Debe escribirse 1 bit");
        } if( edt[9].getText().length()<1 ){
            edt[9].setError("Debe escribirse 1 bit");
        } if( edt[10].getText().length()<4 ){
            edt[10].setError("Deben escribirse 4 caracteres\nhexadecimales de 0-F");
        } if( edt[11].getText().length()<12 ){
            edt[11].setError("Deben escribirse 12 caracteres\nhexadecimales de 0-F");
        } if( edt[12].getText().length()<12 ){
            edt[12].setError("Deben escribirse 12 caracteres\nhexadecimales de 0-F");
        } if( edt[15].getText().length()<4 ){
            edt[15].setError("Deben escribirse 4 caracteres\nhexadecimales de 0-F");
        }
    }

    public boolean verificaCampos ( ) {
        if (tipo.getSelectedItemPosition() == 2) { // Datos
            if ( edt[13].getText().length()<12||edt[14].getText().length()<12 ) {
                if( edt[13].getText().length()<12 ){
                    edt[13].setError("Deben escribirse 12 caracteres\nhexadecimales de 0-F");
                } if( edt[14].getText().length()<12 ){
                    edt[14].setError("Deben escribirse 12 caracteres\nhexadecimales de 0-F");
                }
                return false;
            }
        }

        if( edt[0].getText().length()<2||edt[2].getText().length()<1||
                edt[3].getText().length()<1||edt[4].getText().length()<1||edt[5].getText().length()<1||
                edt[6].getText().length()<1||edt[7].getText().length()<1||edt[8].getText().length()<1||
                edt[9].getText().length()<1||edt[10].getText().length()<4||edt[11].getText().length()<12||
                edt[12].getText().length()<12||edt[15].getText().length()<4) {
            Toast.makeText(VistaWifi.this, "Error: Verifique los campos", Toast.LENGTH_SHORT).show();
            buscaError();
            return false;
        }
        return true;
    }

    public void reiniciar(){
        for( int i=0; i<edt.length; i++ ){
            if (i!= 1) edt[i].setText("");
        }
        campotrama.setText("");
    }

    public String toHex( String arg ) {
        try{
            return String.format("%x", new BigInteger(1, arg.getBytes("UTF8")));
        } catch( Exception e ){
            Toast.makeText(VistaWifi.this,"Error en la conversión",Toast.LENGTH_SHORT).show();
        }
        return "";
    }
///////////////////////////
    String resultado;
    int rx;
    private ArrayList<Integer> Mx = new ArrayList <Integer> ( );
    private ArrayList <Integer> Gx = new ArrayList <Integer> ( );
    private ArrayList <Integer> res = new ArrayList <Integer> ( );
    private ArrayList <Integer> tmp = new ArrayList <Integer> ( );

    public String CRC (String sm) {
        String sg = "1010101111001101";

        char arraymx[] = sm.toCharArray();
        char arraygx[] = sg.toCharArray();

        Mx.clear ( );	res.clear ( );
        Gx.clear ( );	tmp.clear ( );

        for( int i=0; i<arraymx.length; i++ ){
            if( arraymx[i] == '1' ){
                Mx.add(1);
            } else if( arraymx[i] == '0' ){
                Mx.add(0);
            }
        }
        for( int i=0; i<arraygx.length; i++ ){
            if( arraygx[i] == '1' ){
                Gx.add(1);
            } else if( arraygx[i] == '0' ){
                Gx.add(0);
            }
        }
        rx = Gx.size( );
        for (int i = 0; i < rx - 1; i++)
            Mx.add ( 0 );

        for (int i = 0; i < rx; i++) {
            if (Gx.get (i) == Mx.get (i)) res.add (0);
            else res.add (1);
        }

        for (int current = rx; current < Mx.size ( ); current++) {
            if (res.get(0) == 0) {
                res.remove (0);
                res.add ( Mx.get ( current ) );
                if ( current == (Mx.size ( )  - 1) && res.get (0) == 1 ) {
                    division ( );
                    break;
                }
            } else {
                division ( );
                current--;
            }
        }
        tmp.clear();
        for (int i = 0; i < Mx.size ( ); i++) {
            tmp.add(Mx.get(i));
        }

        for (int i = 0; i < rx - 1; i++) {
            Mx.remove (Mx.size ( ) - 1);
        }

        for (int i = 1; i < res.size ( ); i++) {
            Mx.add ( res.get (i) );
        }
        resultado = "";

        for (int i = 0; i< Mx.size (); i++)
            resultado += Mx.get (i)+"";
        return resultado;
    }

    public void division() {
        tmp.clear ( );
        for (int i = 0; i < res.size ( ); i++)
            tmp.add (res.get (i) );
        res.clear ( );
        for (int j = 0; j < tmp.size ( ); j++) {
            if ( Gx.get (j) == tmp.get (j) ) res.add (0);
            else  res.add (1);
        }
    }
/////////////////////

}
