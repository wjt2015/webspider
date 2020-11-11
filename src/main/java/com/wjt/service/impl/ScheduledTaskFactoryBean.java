package com.wjt.service.impl;

import com.wjt.service.ScheduledTask;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

@Slf4j
@AllArgsConstructor
public class ScheduledTaskFactoryBean implements FactoryBean<ScheduledTask> {

    private String cronExp;
    @Override
    public ScheduledTask getObject() throws Exception {
        MyScheduledTask myScheduledTask=new MyScheduledTask();
        myScheduledTask.setCron(this.cronExp);
        return myScheduledTask;
    }

    @Override
    public Class<?> getObjectType() {
        return MyScheduledTask.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
