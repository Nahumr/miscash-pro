package com.popm.miscash.Carritoc;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.popm.miscash.Compras.Admin_SQLite;
import com.popm.miscash.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RvCarrito extends RecyclerView.Adapter<RvCarrito.ItemViewH>{

    public static class ItemViewH extends RecyclerView.ViewHolder{
        ImageButton item_add,item_sub;
        TextView item_nombre, item_cantidad,item_subtotal,item_vuelto;
        Button item_eliminar;
        ItemViewH (View view){
            super(view);
            item_add = view.findViewById(R.id.CI_Agregar);
            item_sub = view.findViewById(R.id.CI_Eliminar);
            item_nombre = view.findViewById(R.id.CI_Nombre);
            item_cantidad = itemView.findViewById(R.id.CI_Cantidad);
            item_subtotal = view.findViewById(R.id.CI_subtotal);
            item_vuelto = view.findViewById(R.id.CI_vuelto);
        }
    }

    List<CarritoItem> items;
    Context context;

    public RvCarrito(List<CarritoItem> items, Context context) {
        this.items = items;
        this.context = context;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ItemViewH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carrito_item, viewGroup, false);
        ItemViewH ivh = new ItemViewH(v);
        return ivh;
    }

    @Override
    public void onBindViewHolder(final ItemViewH itemViewH, final int i) {


            final Admin_SQLite admin = new Admin_SQLite(context);

            itemViewH.item_nombre.setText(items.get(i).getProducto_nombre());
            itemViewH.item_cantidad.setText("Cantidad: "+String.valueOf(items.get(i).getProducto_cantidad()));
            itemViewH.item_subtotal.setText("Subtotal: $"+String.valueOf(redondeo(items.get(i).getProducto_precio(),items.get(i).getProducto_cantidad())));
            itemViewH.item_vuelto.setText("Te devolvemos: $"+String.valueOf(redondeo(items.get(i).producto_vuelto,items.get(i).getProducto_cantidad())) );
            itemViewH.item_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    items.get(i).setProducto_cantidad(items.get(i).getProducto_cantidad()+1);
                    itemViewH.item_cantidad.setText("Cantidad: "+String.valueOf(items.get(i).getProducto_cantidad()));
                    itemViewH.item_subtotal.setText("Subtotal: $"+String.valueOf(redondeo(items.get(i).getProducto_precio(),items.get(i).getProducto_cantidad())));
                    itemViewH.item_vuelto.setText("Te devolvemos: $"+String.valueOf(redondeo(items.get(i).producto_vuelto,items.get(i).getProducto_cantidad())) );
                    admin.actualizar(items.get(i).getProducto_cb(),items.get(i).getProducto_cantidad());
                }
            });

            itemViewH.item_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (items.get(i).getProducto_cantidad()-1 > 0){
                        items.get(i).setProducto_cantidad(items.get(i).getProducto_cantidad()-1);
                        itemViewH.item_cantidad.setText("Cantidad: "+String.valueOf(items.get(i).getProducto_cantidad()));
                        itemViewH.item_subtotal.setText("Subtotal: $"+String.valueOf(redondeo(items.get(i).getProducto_precio(),items.get(i).getProducto_cantidad())));
                        itemViewH.item_vuelto.setText("Te devolvemos: $"+String.valueOf(redondeo(items.get(i).producto_vuelto,items.get(i).getProducto_cantidad())) );
                        admin.actualizar(items.get(i).getProducto_cb(),items.get(i).getProducto_cantidad());
                    }else {
                        admin.eliminar(items.get(i).getProducto_cb());
                        items.remove(i);
                        update(i);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    float redondeo (Float cantidad, int precio){
        float total = cantidad*precio;

        BigDecimal bd = new BigDecimal(Float.toString(total));
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);

        return total;
    }



    public void update(int i){
        this.notifyItemRemoved(i);
    }



}
