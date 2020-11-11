package com.wjt.service;

public interface ScheduledTask {
    void exec();
    void setCron(String cronExp);
    String getCron();
}
