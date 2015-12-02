package itson.sushivan_empresa.modelo;

import java.util.List;
import java.util.UUID;

/**
 * Created by Alberto on 30/11/2015.
 */
public class Pedido {
    private String id;
    private Perfil perfil;
    private List<Producto> carrito;
    private boolean entregado;

    public Pedido() {
        id = UUID.randomUUID().toString();
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public List<Producto> getCarrito() {
        return carrito;
    }

    public void setCarrito(List<Producto> carrito) {
        this.carrito = carrito;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isEntregado() {
        return entregado;
    }

    public void setEntregado(boolean entregado) {
        this.entregado = entregado;
    }
}
