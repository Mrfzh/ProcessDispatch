package com.feng.processdispatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";

    private static final int METHOD_FCFS = 0;   // 先来先服务
    private static final int METHOD_SPF = 1;    // 最短作业优先
    private static final int METHOD_HRN = 2;    // 响应比高者优先

    private EditText mProcessNameEt;
    private EditText mServiceTimeEt;
    private EditText mCommitTimeEt;
    private Button mCommitBtn;
    private Button mClearBtn;
    private Spinner mMethodSp;
    private Button mRunBtn;
    private RecyclerView mProcessListRv;
    private RecyclerView mDispatchResultRv;
    private TextView mAverTurnTimeTv;         // 平均周转时间
    private TextView mWeightAverTurnTimeTv;   // 带权平均周转时间

    private int mMethod = 0;    // 执行的算法
    private ProcessListAdapter mProcessListAdapter;
    private List<ProcessData> mProcessDataList = new ArrayList<>();
    private DispatchResultAdapter mDispatchResultAdapter;
    private List<ResultData> mResultDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);   //隐藏标题栏
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mProcessNameEt = findViewById(R.id.et_main_process_name);
        mServiceTimeEt = findViewById(R.id.et_main_service_time);
        mCommitTimeEt = findViewById(R.id.et_main_commit_time);

        mCommitBtn = findViewById(R.id.btn_main_commit_process);
        mCommitBtn.setOnClickListener(this);
        mClearBtn = findViewById(R.id.btn_main_clear_process);
        mClearBtn.setOnClickListener(this);
        mRunBtn = findViewById(R.id.btn_main_run);
        mRunBtn.setOnClickListener(this);

        mMethodSp = findViewById(R.id.sp_main_method_list);
        mMethodSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // 先来先服务
                        mMethod = METHOD_FCFS;
                        break;
                    case 1:
                        // 最短作业优先
                        mMethod = METHOD_SPF;
                        break;
                    case 2:
                        // 响应比高者优先
                        mMethod = METHOD_HRN;
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mProcessListRv = findViewById(R.id.rv_main_process_list);
        mProcessListRv.setLayoutManager(new LinearLayoutManager(this));
        mDispatchResultRv = findViewById(R.id.rv_main_dispatch_result);
        mDispatchResultRv.setLayoutManager(new LinearLayoutManager(this));

        mAverTurnTimeTv = findViewById(R.id.tv_main_dispatch_result_average_turn_time);
        mWeightAverTurnTimeTv = findViewById(R.id.tv_main_dispatch_result_weight_average_turn_time);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main_commit_process:
                // 提交进程
                if (!checkCommit()) {
                    break;
                }
                ProcessData processData = new ProcessData(mProcessNameEt.getText().toString().trim(),
                        Integer.parseInt(mCommitTimeEt.getText().toString()),
                        Integer.parseInt(mServiceTimeEt.getText().toString()));
                mProcessDataList.add(processData);
                // 按照到达时间排序
                Collections.sort(mProcessDataList, new Comparator<ProcessData>() {
                    @Override
                    public int compare(ProcessData o1, ProcessData o2) {
                        return o1.getCommitTime() - o2.getCommitTime();
                    }
                });
                // 显示信息
                if (mProcessListAdapter == null) {
                    mProcessListAdapter = new ProcessListAdapter(MainActivity.this, mProcessDataList);
                    mProcessListRv.setAdapter(mProcessListAdapter);
                } else {
                    mProcessListAdapter.notifyDataSetChanged();
                }
                // 清空输入
                mServiceTimeEt.setText("");
                mProcessNameEt.setText("");
                mCommitTimeEt.setText("");
                break;
            case R.id.btn_main_clear_process:
                // 清空提交的进程
                mProcessDataList.clear();
                if (mProcessListAdapter != null) {
                    mProcessListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.btn_main_run:
                if (!checkRun()) {
                    break;
                }
                // 隐藏软键盘
                hideSoftKeyboard(this);
                // 先重置结果
                mResultDataList.clear();
                // 根据不同的算法对进程进行调度
                switch (mMethod) {
                    case METHOD_FCFS:
                        // 执行先来先服务算法
                        doFCFS();
                        break;
                    case METHOD_SPF:
                        // 执行最短作业优先算法
                        doSPF();
                        break;
                    case METHOD_HRN:
                        // 执行响应比高者优先算法
                        doHRN();
                        break;
                    default:
                        break;
                }
                // 显示结果
                // 1. 计算平均周转时间和带权平均周转时间
                float turnTimeSum = 0f;
                float weightTurnTimeSum = 0f;
                for (int i = 0; i < mResultDataList.size(); i++) {
                    turnTimeSum += mResultDataList.get(i).getTurnTime();
                    weightTurnTimeSum += mResultDataList.get(i).getWeightTurnTime();
                }
                float averTurnTime = turnTimeSum / mResultDataList.size();
                float weightAverTurnTime = weightTurnTimeSum / mResultDataList.size();
                mAverTurnTimeTv.setText(String.format("%.2f",averTurnTime));
                mWeightAverTurnTimeTv.setText(String.format("%.2f",weightAverTurnTime));
                // 2. 更新列表
                if (mDispatchResultAdapter == null) {
                    mDispatchResultAdapter = new DispatchResultAdapter(MainActivity.this, mResultDataList);
                    mDispatchResultRv.setAdapter(mDispatchResultAdapter);
                } else {
                    mDispatchResultAdapter.notifyDataSetChanged();
                }
                break;
            default:
                break;
        }
    }

    private boolean checkCommit() {
        if (mProcessNameEt.getText().toString().trim().equals("") ||
                mServiceTimeEt.getText().toString().equals("") ||
                 mCommitTimeEt.getText().toString().equals("")) {
            showShortToast("请先填写完整进程名称、到达时间和服务时间");
            return false;
        }
        if (Integer.parseInt(mServiceTimeEt.getText().toString()) == 0) {
            showShortToast("服务时间不能为 0");
            return false;
        }

        return true;
    }

    private boolean checkRun() {
        if (mProcessDataList.isEmpty()) {
            showShortToast("请先添加进程");
            return false;
        }

        return true;
    }

    private void showShortToast(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 执行先来先服务算法
     */
    private void doFCFS() {
        // 进行调度
        int startTime;          // 开始时间
        int completeTime = 0;   // 结束时间
        float turnTime;         // 周转时间
        float weightTurnTime;   // 带权周转时间
        for (int i = 0; i < mProcessDataList.size(); i++) {
            ProcessData processData = mProcessDataList.get(i);
            startTime = Math.max(completeTime, processData.getCommitTime());
            completeTime = startTime + processData.getServiceTime();    // 完成时间 = 开始时间 + 服务时间
            turnTime = completeTime - processData.getCommitTime();    // 周转时间 = 完成时间 - 到达时间
            weightTurnTime = turnTime / processData.getServiceTime();   // 带权周转时间 = 周转时间 / 服务时间
            mResultDataList.add(new ResultData(processData.getProcessName(), startTime, completeTime,
                    turnTime, weightTurnTime));
        }
    }

    /**
     * 执行短作业优先算法
     */
    private void doSPF() {
        int currTime = mProcessDataList.get(0).getCommitTime();
        boolean[] hasCompleted = new boolean[mProcessDataList.size()];  // 当前进程是否执行完毕
        int finishNum = 0;  // 完成任务的进程数
        while (finishNum < mProcessDataList.size()) {
            int index = -1; // 当前需要执行的进程
            int minServiceTime = Integer.MAX_VALUE;
            boolean hasFound = false;
            for (int i = 0; i < mProcessDataList.size(); i++) {
                if (hasCompleted[i] || mProcessDataList.get(i).getCommitTime() > currTime) {
                    continue;
                }
                hasFound = true;
                if (mProcessDataList.get(i).getServiceTime() < minServiceTime) {
                    minServiceTime = mProcessDataList.get(i).getServiceTime();
                    index = i;
                }
            }
            if (!hasFound) {
                // 更新 currTime
                int temp = Integer.MAX_VALUE;
                for (int i = 0; i < mProcessDataList.size(); i++) {
                    int currCommitTime = mProcessDataList.get(i).getCommitTime();
                    if (currCommitTime > currTime) {
                        temp = Math.min(temp, currCommitTime);
                    }
                }
                currTime = temp;
                continue;
            }
            // 执行当前进程
            hasCompleted[index] = true;
            finishNum++;
            int completeTime = currTime + mProcessDataList.get(index).getServiceTime();
            float turnTime = completeTime - mProcessDataList.get(index).getCommitTime();
            float weightTurnTime = turnTime / mProcessDataList.get(index).getServiceTime();
            mResultDataList.add(new ResultData(mProcessDataList.get(index).getProcessName(),
                    currTime, completeTime,turnTime, weightTurnTime));
            currTime = completeTime;
        }
    }

    /**
     * 执行高响应比优先算法
     */
    private void doHRN() {
        // 各进程根据优先权，优先权高的执行，每经过 1 个时间单位都要重新计算一次各进程的优先权
        // 优先权 = (等待时间 + 服务时间) / 服务时间
        int currTime = mProcessDataList.get(0).getCommitTime();   // 当前时间
        boolean[] hasCompleted = new boolean[mProcessDataList.size()];  // 记录该进程是否已执行
        int finishNum = 0;  // 完成任务的进程数
        while (finishNum < mProcessDataList.size()) {
            int maxPriority = Integer.MIN_VALUE;
            int maxIndex = -1;
            // 比较各进程的优先权
            boolean hasFound = false;
            for (int i = 0; i < mProcessDataList.size(); i++) {
                if (hasCompleted[i] || mProcessDataList.get(i).getCommitTime() > currTime) {
                    continue;
                }
                hasFound = true;
                int currPriority = (currTime - mProcessDataList.get(i).getCommitTime()
                        + mProcessDataList.get(i).getServiceTime()) /
                        mProcessDataList.get(i).getServiceTime();
                if (currPriority > maxPriority) {
                    maxPriority = currPriority;
                    maxIndex = i;
                }
            }
            if (!hasFound) {
                // 更新 currTime
                int temp = Integer.MAX_VALUE;
                for (int i = 0; i < mProcessDataList.size(); i++) {
                    int currCommitTime = mProcessDataList.get(i).getCommitTime();
                    if (currCommitTime > currTime) {
                        temp = Math.min(temp, currCommitTime);
                    }
                }
                currTime = temp;
                continue;
            }
            // 执行优先级最高的进程
            finishNum++;
            int completeTime = currTime + mProcessDataList.get(maxIndex).getServiceTime();
            float turnTime = completeTime - mProcessDataList.get(maxIndex).getCommitTime();
            float weightTurnTime = turnTime / mProcessDataList.get(maxIndex).getServiceTime();
            mResultDataList.add(new ResultData(mProcessDataList.get(maxIndex).getProcessName(),
                    currTime, completeTime, turnTime, weightTurnTime));
            hasCompleted[maxIndex] = true;
            currTime = completeTime;
        }
    }

}
