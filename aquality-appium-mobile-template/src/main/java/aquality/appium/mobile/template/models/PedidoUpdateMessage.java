package aquality.appium.mobile.template.models;

public class PedidoUpdateMessage {
    private String pedidoId;
    private String tipo;
    private String nuevoEstado;
    private Location nuevaUbicacion;

    public PedidoUpdateMessage(String pedidoId, String tipo) {
        this.pedidoId = pedidoId;
        this.tipo = tipo;
    }

    public String getPedidoId() { 
        return pedidoId; 
    }

    public String getTipo() { 
        return tipo; 
    }

    public String getNuevoEstado() { 
        return nuevoEstado; 
    }

    public void setNuevoEstado(String nuevoEstado) { 
        this.nuevoEstado = nuevoEstado; 
    }

    public Location getNuevaUbicacion() { 
        return nuevaUbicacion; 
    }

    public void setNuevaUbicacion(Location nuevaUbicacion) { 
        this.nuevaUbicacion = nuevaUbicacion; 
    }
}
