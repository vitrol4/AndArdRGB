package br.edu.ifspsaocarlos.luminariargb;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    SeekBar seekbarR;
    SeekBar seekbarG;
    SeekBar seekbarB;
    TextView value;

    private View view = null;


    int red = 255;
    int blue = 255;
    int green = 255;

    Handler h;

    final int RECIEVE_MESSAGE = 1;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private StringBuilder sb = new StringBuilder();

    private static final String TAG = "RGB-DEBUG";

    ConnectedThread mConnectedThread;


    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    private static String address = "";

    public void onCreate(Bundle savedInstanceState) {
        try {

            address = VariaveisGlobal.enderecoBluetoothCliente;

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            value = (TextView) findViewById(R.id.textview);
            seekbarR = (SeekBar) findViewById(R.id.seekbarr);
            seekbarG = (SeekBar) findViewById(R.id.seekbarg);
            seekbarB = (SeekBar) findViewById(R.id.seekbarb);
            view = findViewById(R.id.rgbView);


            h = new Handler() {
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case RECIEVE_MESSAGE:
                            byte[] readBuf = (byte[]) msg.obj;
                            String strIncom = new String(readBuf, 0, msg.arg1);
                            sb.append(strIncom);
                            int endOfLineIndex = sb.indexOf("\r\n");
                            if (endOfLineIndex > 0) {
                                String sbprint = sb.substring(0, endOfLineIndex);
                                sb.delete(0, sb.length());
                            }
                            break;
                    }
                }
            };


            btAdapter = BluetoothAdapter.getDefaultAdapter();

            checkBTState();

        } catch (Exception e) {

        }


        seekbarR.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                red = (progress - 255) * -1;
                value.setText("O Valor do Vermelho " + red);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                value.setText("O Valor do Vermelho " + red);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                value.setText("O Valor do Vermelho " + red);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                pintaPainel();
            }
        });

        seekbarB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blue = (progress - 255) * -1;
                value.setText("O Valor do Azul " + blue);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                value.setText("O Valor do Azul " + blue);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                value.setText("O Valor do Azul " + blue);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }
        });


        seekbarG.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                green = (progress - 255) * -1;
                value.setText("O Valor do Verde " + green);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                value.setText("O Valor do Verde " + green);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                value.setText("O Valor do Verde " + green);
                mConnectedThread.write(red + "," + green + "," + blue + "\n");
                Log.d(TAG, red + "," + green + "," + blue + "\n");
                pintaPainel();
            }
        });

    }

    public void pintaPainel() {
        view.setBackgroundColor(Color.rgb(255 - red, 255 - green, 255 - blue));
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }
        }
        return device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    @Override
    public void onResume() {
        super.onResume();
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }
        btAdapter.cancelDiscovery();
        try {
            btSocket.connect();

        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        if (btAdapter == null) {
            errorExit("Fatal Error", "Bluetooth not support");
        } else {
            if (btAdapter.isEnabled()) {

            } else {

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message) {
        Toast.makeText(getBaseContext(), title + " - " + message, Toast.LENGTH_LONG).show();
        finish();
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;


            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;


            while (true) {
                try {

                    bytes = mmInStream.read(buffer);
                    h.obtainMessage(RECIEVE_MESSAGE, bytes, -1, buffer).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }


        public void write(String message) {
            byte[] msgBuffer = message.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {

            }
        }

    }
}
