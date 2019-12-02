package com.feng.processdispatch;

/**
 * @author Feng Zhaohao
 * Created on 2019/12/2
 */
public class ResultData {
    private String processName;
    private int startTime;
    private int completeTime;
    private float turnTime;
    private float weightTurnTime;

    public ResultData(String processName, int startTime,
                      int completeTime, float turnTime, float weightTurnTime) {
        this.processName = processName;
        this.startTime = startTime;
        this.completeTime = completeTime;
        this.turnTime = turnTime;
        this.weightTurnTime = weightTurnTime;
    }

    public float getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(float turnTime) {
        this.turnTime = turnTime;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(int completeTime) {
        this.completeTime = completeTime;
    }


    public float getWeightTurnTime() {
        return weightTurnTime;
    }

    public void setWeightTurnTime(float weightTurnTime) {
        this.weightTurnTime = weightTurnTime;
    }
}
