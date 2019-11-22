package com.rk.jdbc.postman.view.history;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rk.jdbc.R;
import com.rk.jdbc.postman.data.model.ApiDbDto;
import com.rk.jdbc.postman.view.PostmanActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by user1 on 31/7/19.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    List<ApiDbDto> list;

    public HistoryAdapter(Context context, List<ApiDbDto> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_custom_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv_1.setText(list.get(position).getUrl());
        holder.tv_2.setText(list.get(position).getRequest());
        holder.tv_3.setText(getDate(list.get(position).getCreated_date()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostmanActivity.class);
                ApiDbDto dto = list.get(position);
                intent.putExtra("postman",dto);
                context.startActivity(intent);
            }
        });
    }

    private String getDate(long created_date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return  sdf.format(created_date);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_1, tv_2, tv_3;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_1 = itemView.findViewById(R.id.tv_1);
            tv_2 = itemView.findViewById(R.id.tv_2);
            tv_3 = itemView.findViewById(R.id.tv_3);
        }
    }
}
