package com.feng.processdispatch;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/2
 */
public class DispatchResultAdapter extends RecyclerView.Adapter<DispatchResultAdapter.DispatchResultViewHolder>{

    private Context mContext;
    private List<ResultData> mDataList;

    public DispatchResultAdapter(Context mContext, List<ResultData> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public DispatchResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DispatchResultViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_dispatch_result, null));
    }

    @Override
    public void onBindViewHolder(@NonNull DispatchResultViewHolder dispatchResultViewHolder, int i) {
        dispatchResultViewHolder.processName.setText(mDataList.get(i).getProcessName());
        dispatchResultViewHolder.startTime.setText(String.valueOf(mDataList.get(i).getStartTime()));
        dispatchResultViewHolder.completeTime.setText(String.valueOf(mDataList.get(i).getCompleteTime()));
        dispatchResultViewHolder.turnTime.setText(String.valueOf(mDataList.get(i).getTurnTime()));
        dispatchResultViewHolder.weightTurnTime.setText(
                String.format("%.2f", mDataList.get(i).getWeightTurnTime()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class DispatchResultViewHolder extends RecyclerView.ViewHolder {
        TextView processName;
        TextView startTime;
        TextView completeTime;
        TextView turnTime;
        TextView weightTurnTime;

        public DispatchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            processName = itemView.findViewById(R.id.tv_item_dispatch_result_process_name);
            startTime = itemView.findViewById(R.id.tv_item_dispatch_result_start_time);
            completeTime = itemView.findViewById(R.id.tv_item_dispatch_result_complete_time);
            turnTime = itemView.findViewById(R.id.tv_item_dispatch_result_turn_time);
            weightTurnTime = itemView.findViewById(R.id.tv_item_dispatch_result_weight_turn_time);
        }
    }
}
