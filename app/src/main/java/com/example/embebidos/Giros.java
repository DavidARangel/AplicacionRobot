package com.example.embebidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class Giros extends AppCompatActivity {

    private Button connectButton;
    private BluetoothSocket socket;
    private static final int REQUEST_PERMISSION = 1;

    // ID único para la comunicación Bluetooth
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giros);

        connectButton = findViewById(R.id.btnCon2);

        // Configurar la conexión Bluetooth
        connectButton.setOnClickListener(view -> {
            setupBluetooth();
        });

    }

    private void checkBluetoothPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // El permiso no ha sido concedido, se solicita al usuario
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
        } else {
            // El permiso ya ha sido concedido
            getBondedDevices();
        }
    }

    // Manejar el resultado de la solicitud de permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // El permiso ha sido concedido
                getBondedDevices();
            } else {
                // El permiso ha sido denegado
                // Aquí puedes manejar el caso en que el usuario deniegue el permiso
            }
        }
    }

    // Obtener los dispositivos Bluetooth emparejados
    private void getBondedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null) {
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            // Aquí puedes realizar las acciones necesarias con los dispositivos emparejados
        }
    }

    private void setupBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            checkBluetoothPermission();
            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            BluetoothDevice targetDevice = buscarDispositivo(pairedDevices, "BT04-A");
            if (targetDevice != null) {
                try {
                    socket = targetDevice.createRfcommSocketToServiceRecord(MY_UUID);
                    socket.connect();
                    // La conexión Bluetooth se estableció correctamente, mostrar un Toast
                    Toast.makeText(this, "Conexión Bluetooth establecida", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error al establecer la conexión Bluetooth", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    private BluetoothDevice buscarDispositivo(Set<BluetoothDevice> devices, String name) {
        checkBluetoothPermission();
        for (BluetoothDevice device : devices) {
            if (device.getName().equals(name)) {
                return device;
            }
        }

        return null;
    }



}