package com.example.tecno30.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tecno30.R;

public class GalleryFragment extends Fragment {

    private static final String BROKER_URL = "tcp://broker.emqx.io:1883";
    private static final String CLIENT_ID = "mqttx_6f06c8d1";
    private static final String SUBSCRIPTION_TOPIC = "tecnoprint/msg";

    private MqttHandler mqttHandler;

    private EditText editTextMessage;
    private TextView textViewStatus;
    private TextView textViewReceived;
    private Button buttonPublish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Crear e inicializar el manejador MQTT
        mqttHandler = new MqttHandler();

        // Configurar un listener para manejar mensajes recibidos del broker
        mqttHandler.setMessageListener((topic, message) -> {
            if (getActivity() != null) {
                // Actualizar el TextView con el mensaje recibido
                getActivity().runOnUiThread(() -> textViewReceived.setText("Received [" + topic + "]: " + message));
            }
        });

        // Conectar al broker MQTT
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        // Suscribirse al tópico definido
        mqttHandler.subscribe(SUBSCRIPTION_TOPIC);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        // Inicializar vistas de la interfaz de usuario
        editTextMessage = view.findViewById(R.id.editTextMessage);
        textViewStatus = view.findViewById(R.id.textViewStatus);
        textViewReceived = view.findViewById(R.id.textViewReceived);
        buttonPublish = view.findViewById(R.id.buttonPublish);

        // Configurar el botón para publicar mensajes
        buttonPublish.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                mqttHandler.publish(SUBSCRIPTION_TOPIC, message);
                textViewStatus.setText("Last Published: " + message);
            } else {
                Toast.makeText(getContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mqttHandler != null) {
            mqttHandler.disconnect();
        }
    }
}
