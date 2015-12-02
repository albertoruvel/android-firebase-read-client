package itson.sushivan_empresa;

import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import itson.sushivan_empresa.adapter.PedidosAdapter;
import itson.sushivan_empresa.modelo.Pedido;
import itson.sushivan_empresa.modelo.Perfil;
import itson.sushivan_empresa.modelo.Producto;

public class MainActivity extends AppCompatActivity {

    private Firebase firebase = new Firebase("https://itson-sushivan.firebaseio.com/");

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private TextView textView;
    private CoordinatorLayout coordinatorLayout;

    private List<Pedido> pedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //obtiene todos los pedidos
        obtenPedidos();
        //los controles
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coord_layout);
    }

    private void obtenPedidos(){
        Query queryRef = firebase.orderByChild("pedidos");
        pedidos = new ArrayList<>();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //por cada pedido, obtiene los productos del carrito
                //y los agrega a la lista para crear recycler view
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                for (String key : map.keySet()) {
                    //obtiene el carrito
                    Map<String, Object> mapaPedido = (Map<String, Object>) map.get(key);
                    boolean entregado = (Boolean) mapaPedido.get("entregado");

                    //muestra solo los que no han sido entregados
                    if(entregado)continue;
                    //obtiene el id
                    final String id = (String) mapaPedido.get("id");
                    List<Producto> lista = (List<Producto>) mapaPedido.get("carrito");
                    Map<String, Object> mapaPerfil = (Map<String, Object>) mapaPedido.get("perfil");
                    Perfil perfil = getPefil(mapaPerfil);

                    Pedido pedido = new Pedido();
                    pedido.setId(id);
                    pedido.setEntregado(entregado);
                    pedido.setCarrito(lista);
                    pedido.setPerfil(perfil);
                    pedidos.add(pedido);
                }
                adapter = new PedidosAdapter(pedidos);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private Perfil getPefil(Map<String, Object> mapaPerfil) {
        Perfil perfil = new Perfil();
        perfil.setDireccion((String)mapaPerfil.get("direccion"));
        perfil.setEmail((String) mapaPerfil.get("email"));
        perfil.setNoCuenta((String) mapaPerfil.get("noCuenta"));
        perfil.setNombre((String) mapaPerfil.get("nombre"));
        perfil.setTelefono((String) mapaPerfil.get("telefono"));

        return perfil;
    }
}
