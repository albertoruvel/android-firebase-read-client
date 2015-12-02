package itson.sushivan_empresa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import itson.sushivan_empresa.modelo.Pedido;
import itson.sushivan_empresa.modelo.Producto;

public class PedidoActivity extends AppCompatActivity {

    private Producto producto;
    private TextView nombre, descripcion, precio;
    private Button terminado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);
        //obtiene el pedido
        this.producto = Utils.obtenProductoGuardado(this);
        //obtiene los controles
        nombre = (TextView)findViewById(R.id.nombre_producto);
        descripcion = (TextView)findViewById(R.id.descripcion_producto);
        precio = (TextView)findViewById(R.id.precio_producto);
        terminado = (Button)findViewById(R.id.boton_terminar);
        //muestra la info
        nombre.setText(producto.getTitulo());
        descripcion.setText(producto.getDescripcion());
        precio.setText("Precio: $ " + producto.getCosto() + " MXN");
        terminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase firebase = Utils.getFirebase()
                        .child("pedidos")
                        .child(producto.getPedidoId());
                Toast.makeText(PedidoActivity.this, "Se ha terminado el producto", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
        });
    }
}
