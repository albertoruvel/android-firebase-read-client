package itson.sushivan_empresa.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import itson.sushivan_empresa.PedidoActivity;
import itson.sushivan_empresa.R;
import itson.sushivan_empresa.Utils;
import itson.sushivan_empresa.modelo.Pedido;
import itson.sushivan_empresa.modelo.Producto;

/**
 * Created by Alberto on 30/11/2015.
 */
public class PedidosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Pedido> pedidos;
    private List<Producto> productos;
    static final int HEADER_TYPE = 0;
    static int ITEM_TYPE = 1;

    private String ultimoId;

    public PedidosAdapter(List<Pedido> pedidos){
        this.pedidos = pedidos;
        creaProductos();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = null;
        RecyclerView.ViewHolder itemHolder = null;
        view = inflater.inflate(R.layout.pedido_item, parent, false);
        itemHolder = new ViewHolder(view);

        return itemHolder;
    }

    /**@Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }**/

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Producto producto = productos.get(position);
        if(ultimoId == null){
            //crea el primer header
            ultimoId = producto.getPedidoId();
            //muestra el primer header
            //busca el pedido con el id
            Pedido pedido = buscaPedido(ultimoId);
            //setea valores
            seteaHolder(holder, producto,pedido, true);
        }else{
            if(ultimoId.equals(producto.getPedidoId())){
                //setea  aHolder
                seteaHolder(holder, producto, null, false);
            }else{
                ultimoId = producto.getPedidoId();
                Pedido pedido = buscaPedido(ultimoId);
                seteaHolder(holder, producto, pedido, true);
            }
        }

    }

    private Pedido buscaPedido(String ultimoId){
        Pedido pedido = null;
        for(Pedido ped : pedidos){
            if(ped.getId().equals(ultimoId)){
                pedido = ped;
                break;
            }
        }
        return pedido;
    }

    private void seteaHolder(final RecyclerView.ViewHolder holder, final Producto producto, final Pedido pedido, boolean muestraHeader) {
        ((ViewHolder)holder).nombre.setText(producto.getTitulo());
        ((ViewHolder)holder).precio.setText("Precio: $ " + producto.getCosto() + " MXN");
        final Context context = ((ViewHolder) holder).precio.getContext();
        ((ViewHolder)holder).ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guarda el producto
                Utils.guardaProducto(((ViewHolder) holder).nombre.getContext(), producto);
                //inicia activiidad
                context.startActivity(new Intent(context, PedidoActivity.class));
            }
        });
        ((ViewHolder)holder).terminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //termina el pedido
                //edita el pedido
                pedido.setEntregado(true);
                Firebase firebase = Utils.getFirebase()
                        .child("pedidos")
                        .child(pedido.getId());
                firebase.setValue(pedido);
                firebase.push();
                //crea un toast
                Toast.makeText(context, "Se ha dado por terminado el pedido", Toast.LENGTH_LONG)
                    .show();
                //elimina el pedido de la lista
                pedidos.remove(pedido);
                notifyDataSetChanged();
            }
        });
        if(muestraHeader){
            ((ViewHolder)holder).tituloGrupo.setText(pedido.getPerfil().getNombre());
            ((ViewHolder)holder).tituloGrupo.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return pedidos.size();
    }



    private void creaProductos(){
        productos = new ArrayList<>();
        for(Pedido pedido : pedidos){
            List lista = pedido.getCarrito();
            //agrega los productos del carrito
            for(int i = 0; i < lista.size(); i ++){
                HashMap<String, Object> map = (HashMap<String, Object>)lista.get(i);
                //crea un producto
                productos.add(creaProducto(map));
            }
        }
    }

    private Producto creaProducto(HashMap<String, Object>map){
        Producto p = new Producto();
        p.setCosto((Double) map.get("costo"));
        p.setDescripcion((String) map.get("descripcion"));
        p.setImagen((Long) map.get("imagen"));
        p.setIngredientePrincipal((String) map.get("ingredientePrincipal"));
        p.setPedidoId((String) map.get("pedidoId"));
        p.setTitulo((String)map.get("titulo"));
        return p;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, precio, tituloGrupo;
        Button ver, terminado;

        public ViewHolder(View itemView) {
            super(itemView);
            nombre = (TextView)itemView.findViewById(R.id.nombre_pedido);
            precio = (TextView)itemView.findViewById(R.id.costo_pedido);
            ver = (Button)itemView.findViewById(R.id.ver_pedido);
            terminado = (Button)itemView.findViewById(R.id.terminar_pedido);
            tituloGrupo = (TextView)itemView.findViewById(R.id.titulo_grupo);
        }
    }


}
