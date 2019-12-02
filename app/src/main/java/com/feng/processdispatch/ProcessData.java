package com.feng.processdispatch;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/2
 */
public class ProcessData {
    private String processName;
    private int commitTime;
    private int serviceTime;

    public ProcessData(String processName, int commitTime, int serviceTime) {
        this.processName = processName;
        this.commitTime = commitTime;
        this.serviceTime = serviceTime;
    }

    public int getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(int commitTime) {
        this.commitTime = commitTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
}
