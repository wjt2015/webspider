package com.myspring;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.ResolvableType;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultBeanFactory implements BeanFactory, BeanDefinitionRegistry, Closeable {


    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    /**
     * 单例如何实现:
     * 1,如何保存;
     * 2,如何保证单例;
     */
    protected final Map<String, Object> singletonMap = new ConcurrentHashMap<>();


    @Override
    public void close() throws IOException {
        /**
         * 销毁单例;
         */
        synchronized (this.beanDefinitionMap) {
            this.beanDefinitionMap.forEach((name, beanDefinition) -> {
                Object instance = null;
                if (beanDefinition.isSingleton() && (instance = this.singletonMap.get(name)) != null && (beanDefinition instanceof AbstractBeanDefinition)) {
                    AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition) beanDefinition;
                    if (!Strings.isNullOrEmpty(abstractBeanDefinition.getDestroyMethodName())) {
                        try {
                            /**
                             * 执行指定的destroyMethod;
                             */
                            Class<?> beanClass = getBeanClass(abstractBeanDefinition);
                            Method destroyMethod = beanClass.getDeclaredMethod(abstractBeanDefinition.getDestroyMethodName());
                            destroyMethod.invoke(instance);
                        } catch (Exception e) {
                            log.error("销毁单例时失败!name={};instance={};", name, instance);
                        }
                    }
                }
            });
        }
        /**
         * 疑问:如果prototype bean指定了destroyMethod,如何处理?
         */

    }

    @Override
    public Object getBean(String name) throws BeansException {
        /**
         * 思考:
         * 需要做什么?
         *
         */
        Objects.requireNonNull(name, "根据beanName获得bean时,beanName不可为null!");
        return doGetBean(name);
    }

    protected Object doGetBean(String name) {

        Objects.requireNonNull(name, "根据name获得bean时,name不可为null!");
        /**
         * 优先考虑单例;
         */
        Object o = this.singletonMap.get(name);
        if (o != null) {
            return o;
        }
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
        Objects.requireNonNull(beanDefinition, "根据beanDefinition获得bean时,beanDefinition不可为null!");
        if (beanDefinition.isSingleton()) {
            synchronized (this.singletonMap) {
                /**
                 * 二次检查;
                 */
                o = this.singletonMap.get(name);
                o = (o == null ? doCreateInstance(beanDefinition) : o);

                if (o != null) {
                    this.singletonMap.put(name, o);
                }
            }
        } else {
            o = doCreateInstance(beanDefinition);
        }
        return o;
    }

    private Class<?> getBeanClass(final BeanDefinition beanDefinition) {
        Class<?> beanClass = null;
        if (beanDefinition instanceof AbstractBeanDefinition) {
            beanClass = ((AbstractBeanDefinition) beanDefinition).getBeanClass();
        } else {
            try {
                beanClass = Class.forName(beanDefinition.getBeanClassName());
            } catch (Exception e) {
                log.error("Class.forName error!beanDefinition.beanClassName={};", beanDefinition.getBeanClassName(), e);
            }
        }
        return beanClass;
    }

    protected Object doCreateInstance(BeanDefinition beanDefinition) {
        Objects.requireNonNull(beanDefinition, "创建对象实例时beanDefinition不可为null!");
        Class<?> beanClass = getBeanClass(beanDefinition);
        Object instance = null;
        try {
            if (beanClass != null) {
                if (Strings.isNullOrEmpty(beanDefinition.getFactoryMethodName())) {
                    //构造函数;
                    instance = beanClass.newInstance();
                } else {
                    //工厂方法;
                    Method method = beanClass.getDeclaredMethod(beanDefinition.getFactoryMethodName());
                    instance = method.invoke(beanClass);
                }
            } else if (beanDefinition instanceof AbstractBeanDefinition) {
                // 工厂bean构造;
                AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition) beanDefinition;
                String factoryBeanName = abstractBeanDefinition.getFactoryBeanName();
                Object factoryBean = getBean(factoryBeanName);


            }
        } catch (Exception e) {
            log.error("创建bean instance失败!beanDefinition={};beanClass={};", beanDefinition, beanClass, e);
        }
        if (instance != null) {
            //初始化;
            doInit(beanDefinition, instance);
        }
        return instance;

    }

    protected void doInit(BeanDefinition beanDefinition, Object instance) {
        if (beanDefinition != null && beanDefinition instanceof AbstractBeanDefinition) {
            AbstractBeanDefinition abstractBeanDefinition = (AbstractBeanDefinition) beanDefinition;
            try {
                Method method = instance.getClass().getDeclaredMethod(abstractBeanDefinition.getInitMethodName());
                method.invoke(instance);
            } catch (Exception e) {
                log.error("初始化instance时出错!beanDefinition={};instance={};", beanDefinition, instance);
            }
        }
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        return null;
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) throws BeansException {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return false;
    }

    @Override
    public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException {
        return false;
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public void registerAlias(String name, String alias) {

    }

    @Override
    public void removeAlias(String alias) {

    }

    @Override
    public boolean isAlias(String name) {
        return false;
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws BeanDefinitionStoreException {
        /**
         * 三个问题:
         * 1,如何存储beanDefinition;
         * 2,重名怎么办;
         * 3,需要做什么事情,代码逻辑;
         */
        Objects.requireNonNull(beanName, "beanName不可为null!");
        Objects.requireNonNull(beanDefinition, "beanDefinition不可为null");

        if (this.beanDefinitionMap.containsKey(beanName)) {
            throw new BeanDefinitionStoreException(new StringBuilder().append("name=").append(beanName).append("的bean已存在!").toString());
        }

        this.beanDefinitionMap.put(beanName, beanDefinition);

    }

    @Override
    public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {

    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
        return null;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return new String[0];
    }

    @Override
    public int getBeanDefinitionCount() {
        return 0;
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return false;
    }
}
