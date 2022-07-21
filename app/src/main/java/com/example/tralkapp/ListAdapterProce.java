package com.example.tralkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapterProce extends RecyclerView.Adapter<ListAdapterProce.ViewHolder> {
    private List<ListProcedimientos> mData;
    private LayoutInflater mInflater;
    private Context context;
    final ListAdapterProce.OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(ListProcedimientos item);
    }

    public ListAdapterProce(List<ListProcedimientos> itemList,Context context,ListAdapterProce.OnItemClickListener listener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListAdapterProce.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = mInflater.inflate(R.layout.list_procedimiento,null);
        return new ListAdapterProce.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapterProce.ViewHolder holder,final int position){
        holder.bindData(mData.get(position));
    }

    public void setItem(List<ListProcedimientos> items){
        mData=items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,fecha,proce;

        ViewHolder(View itemView){
            super(itemView);

            nombre = itemView.findViewById(R.id.nombreProce);
            fecha = itemView.findViewById(R.id.fechaProce);
            proce = itemView.findViewById(R.id.estadoProce);
        }

        void bindData(final ListProcedimientos item){
            nombre.setText(item.getNombre());
            fecha.setText(item.getFecha());
            proce.setText(item.getProce());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
