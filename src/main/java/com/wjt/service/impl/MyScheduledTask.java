package com.wjt.service.impl;

import com.wjt.service.ScheduledTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class MyScheduledTask implements ScheduledTask {

    private String cronExp;

    private AtomicLong id = new AtomicLong(0);

    @Override
    public void exec() {
        log.info("exec_id={};", id.getAndIncrement());
    }

    @Override
    public void setCron(String cronExp) {
        this.cronExp = cronExp;
    }

    @Override
    public String getCron() {
        return this.cronExp;
    }
}
