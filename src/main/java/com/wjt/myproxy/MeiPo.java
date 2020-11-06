package com.wjt.myproxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@Slf4j
@AllArgsConstructor
public class MeiPo implements InvocationHandler {

    private Person person;

    public Object getInstance() {
        Object instance = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{Person.class}, this::invoke);

        log.info("instance={};",instance);
        return instance;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("我是媒婆!");
        log.info("您是小星星,女!");
        log.info("开始海选 ... ");
        person.findLove();
        log.info("如果合适,就继续发展!");
        log.info("proxy={};method={};args={};", proxy, method, args);
        //return method.invoke(this.person,args);
        return null;
    }
}
