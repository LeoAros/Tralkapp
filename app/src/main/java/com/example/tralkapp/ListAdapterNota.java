package com.example.tralkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapterNota extends RecyclerView.Adapter<ListAdapterNota.ViewHolder> {
    private List<ListNotas> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapterNota.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ListNotas item);
    }

    public ListAdapterNota(List<ListNotas> itemList,Context context,ListAdapterNota.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListAdapterNota.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = mInflater.inflate(R.layout.list_notas,null);
        return new ListAdapterNota.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterNota.ViewHolder holder,final int position){
        holder.bindData(mData.get(position));
    }

    public void setItem(List<ListNotas> items){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,fecha,nota;

        ViewHolder(View itemView){
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreNota);
            fecha = itemView.findViewById(R.id.fechaNota);
            nota = itemView.findViewById(R.id.estadoNota);
        }

        void bindData(final ListNotas item){
            nombre.setText(item.getNombre());
            fecha.setText(item.getFecha());
            nota.setText(item.getNota());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
