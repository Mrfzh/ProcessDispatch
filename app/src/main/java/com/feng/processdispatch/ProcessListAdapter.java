package com.feng.processdispatch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/2
 */
public class ProcessListAdapter extends RecyclerView.Adapter<ProcessListAdapter.ProcessListViewHolder>{

    private Context mContext;
    private List<ProcessData> mDataList;

    public ProcessListAdapter(Context mContext, List<ProcessData> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ProcessListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ProcessListViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_process_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessListViewHolder processListViewHolder, int i) {
        processListViewHolder.processName.setText(mDataList.get(i).getProcessName());
        processListViewHolder.commitTime.setText(String.valueOf(mDataList.get(i).getCommitTime()));
        processListViewHolder.serviceTime.setText(String.valueOf(mDataList.get(i).getServiceTime()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ProcessListViewHolder extends RecyclerView.ViewHolder {
        TextView processName;
        TextView commitTime;
        TextView serviceTime;

        public ProcessListViewHolder(@NonNull View itemView) {
            super(itemView);
            processName = itemView.findViewById(R.id.tv_item_process_list_process_name);
            commitTime = itemView.findViewById(R.id.tv_item_process_list_commit_time);
            serviceTime = itemView.findViewById(R.id.tv_item_process_list_service_time);
        }
    }
}
