package com.myspring;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PreBuildSingletonBeanFactory extends DefaultBeanFactory {


    public void preInstantiateSingleton() {
        synchronized (this.beanDefinitionMap) {
            this.beanDefinitionMap.forEach((name, beanDefinition) -> {
                Object instance = null;
                if (beanDefinition.isSingleton() && (instance = this.singletonMap.get(name)) == null) {
                    instance = getBean(name);
                    if (instance != null) {
                        this.singletonMap.put(name, instance);
                    }
                }
            });
        }
    }

}
