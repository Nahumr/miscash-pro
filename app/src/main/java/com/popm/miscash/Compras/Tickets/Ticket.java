package com.popm.miscash.Compras.Tickets;

import java.util.Date;

public class Ticket {

    String id_ticket;
    String tienda;
    Date fecha;
    Float total,vuelto;
    String status;

    public String getStatus() {
        return status;
    }

    public String getId_ticket() {
        return id_ticket;
    }

    public String getTienda() {
        return tienda;
    }

    public Date getFecha() {
        return fecha;
    }

    public Float getTotal() {
        return total;
    }

    public Float getVuelto() {
        return vuelto;
    }


    public Ticket(String id_ticket, String tienda, Date fecha, Float total, Float vuelto, String status) {
        this.id_ticket = id_ticket;
        this.tienda = tienda;
        this.fecha = fecha;
        this.total = total;
        this.vuelto = vuelto;
        this.status=status;

    }
}
