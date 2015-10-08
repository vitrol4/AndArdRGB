package br.edu.ifspsaocarlos.luminariargb;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Set;

public class MenuActivity extends AppCompatActivity {

    //Componentes
    ListView lista;
    ArrayList<String> s = new ArrayList<>();

    //Intent
    Intent intentComunicacaBluetooth;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        intentComunicacaBluetooth = new Intent(this, MainActivity.class);

        try {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            for (BluetoothDevice bt : pairedDevices) {
                s.add(bt.getAddress());
            }

        } catch (Exception e) {
            s.add(e.toString());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        lista = (ListView) findViewById(R.id.lista);

        try {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, s);
            lista.setAdapter(arrayAdapter);

        } catch (Exception e) {
            // TODO: handle exception
        }

        //Seta o Evento da Lista
        lista.setClickable(true);

        try {

            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    VariaveisGlobal.enderecoBluetoothCliente = lista.getItemAtPosition(position).toString();
                    startActivity(intentComunicacaBluetooth);
                }
            });


        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
