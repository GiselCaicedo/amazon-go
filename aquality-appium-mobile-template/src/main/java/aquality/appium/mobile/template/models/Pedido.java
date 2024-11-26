package aquality.appium.mobile.template.models;

import java.util.UUID;
import java.util.Date;

public class Pedido {
    private String id;
    private String clienteId;
    private String repartidorId;
    private String estado;
    private Location ubicacionEntrega;
    private Location ubicacionActual;
    private Date fechaCreacion;
    private double total;

    public Pedido() {
        this.id = UUID.randomUUID().toString();
        this.fechaCreacion = new Date();
        this.estado = "PENDIENTE";
    }

    public String getId() { 
        return id; 
    }

    public String getClienteId() { 
        return clienteId; 
    }

    public void setClienteId(String clienteId) { 
        this.clienteId = clienteId; 
    }

    public String getRepartidorId() { 
        return repartidorId; 
    }

    public void setRepartidorId(String repartidorId) { 
        this.repartidorId = repartidorId; 
    }

    public String getEstado() { 
        return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
    }

    public Location getUbicacionEntrega() { 
        return ubicacionEntrega; 
    }

    public void setUbicacionEntrega(Location ubicacionEntrega) { 
        this.ubicacionEntrega = ubicacionEntrega; 
    }

    public Location getUbicacionActual() { 
        return ubicacionActual; 
    }

    public void setUbicacionActual(Location ubicacionActual) { 
        this.ubicacionActual = ubicacionActual; 
    }

    public Date getFechaCreacion() { 
        return fechaCreacion; 
    }

    public double getTotal() { 
        return total; 
    }

    public void setTotal(double total) { 
        this.total = total; 
    }
}
