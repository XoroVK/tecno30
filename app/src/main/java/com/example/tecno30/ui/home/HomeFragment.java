package com.example.tecno30.ui.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.example.tecno30.R;
import com.example.tecno30.ui.Database;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private Database database;
    private EditText ingresarTareas, editarTareas;
    private Button btnAgregar, btnEditar, btnEliminar;
    private ListView listarTareas;
    private ArrayList<String> tareas;
    private ArrayAdapter<String> adapter;
    private String seleccionarTareas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el diseño del fragmento de Gallery
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Configura los elementos de la interfaz
        database = new Database(getActivity());
        ingresarTareas = root.findViewById(R.id.editTextTask);
        editarTareas = root.findViewById(R.id.editTextEditTask);
        btnAgregar = root.findViewById(R.id.buttonAdd1);
        btnEditar = root.findViewById(R.id.buttonEdit);
        btnEliminar = root.findViewById(R.id.buttonDelete);
        listarTareas = root.findViewById(R.id.listViewTasks);

        // Configuración del ListView y adaptador
        tareas = database.getAllTareas();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tareas);
        listarTareas.setAdapter(adapter);

        // Configuración de botones y eventos
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tarea = ingresarTareas.getText().toString();
                if (!tarea.isEmpty()) {
                    database.insertTarea(tarea);
                    actualizarListadoTareas();
                    ingresarTareas.setText("");
                }
            }
        });

        listarTareas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                seleccionarTareas = tareas.get(position);
                editarTareas.setText(seleccionarTareas);
                editarTareas.setVisibility(View.VISIBLE);
                btnEditar.setVisibility(View.VISIBLE);
                btnEliminar.setVisibility(View.VISIBLE);
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nuevaTarea = editarTareas.getText().toString();
                if (!nuevaTarea.isEmpty()) {
                    database.updateTarea(seleccionarTareas, nuevaTarea);
                    actualizarListadoTareas();
                    LimpiarCampos();
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.deleteTarea(seleccionarTareas);
                actualizarListadoTareas();
                LimpiarCampos();
            }
        });

        return root;
    }

    public void actualizarListadoTareas() {
        tareas.clear();
        tareas.addAll(database.getAllTareas());
        adapter.notifyDataSetChanged();
    }

    public void LimpiarCampos() {
        editarTareas.setText("");
        editarTareas.setVisibility(View.GONE);
        btnEditar.setVisibility(View.GONE);
        btnEliminar.setVisibility(View.GONE);
    }
}