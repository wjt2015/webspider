package com.wjt.config;

import com.google.common.collect.Lists;
import com.wjt.dao.JuejinArticleMapper;
import com.wjt.model.JuejinArticleEntity;
import com.wjt.service.impl.ExistCheckerImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.spring.MySqlSessionFactoryBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.TypeConverterSupport;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.annotation.MyAutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.MyDefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

@Slf4j
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DBConfig.class})*/
public class DBConfigTest {

    private MyAnnotationConfigApplicationContext applicationContext;

    //@Resource
    private JuejinArticleMapper juejinArticleMapper;

    @Before
    public void init() {

        //applicationContext = new MyAnnotationConfigApplicationContext(DBConfig.class);
        applicationContext = new MyAnnotationConfigApplicationContext(DBConfigParam.class);

        //applicationContext = new MyAnnotationConfigApplicationContext(DBConfigBasedAnnotation.class);
        //applicationContext.register(DBConfig.class);
        juejinArticleMapper = applicationContext.getBean(JuejinArticleMapper.class);

        log.info("juejinArticleMapper={};", juejinArticleMapper);
    }


    @Test
    public void save() {
        JuejinArticleEntity juejinArticleEntity = new JuejinArticleEntity();
        juejinArticleEntity.title = "【源码分析】Java源码系列-LinkedHashMap";
        juejinArticleEntity.url = "https://juejin.im/post/5eae84daf265da7bf7328e25";
        String content = "LinkedHashMap在Map的基础上进行了扩展，提供了按序访问的能力。这个顺序通过accessOrder控制，可以是结点的插入顺序，也可以是结点的访问时间顺序。LinkedHashMap还提供了removeEldestEntry方法，可以用来删除最老访问结点。通过accessOrder和removeEldestEntry可以用来实现LRU缓存。如图所示，LinkedHashMap实现顺序访问的方法比较简单，在HashMap实现之外，还维护了一个双向链表。每当插入结点时，不仅要在Map中维护，还需要在链表中进行维护。HashMap中的put, get等方法都提供了一些钩子方法，如afterNodeAccess、afterNodeInsertion和afterNodeRemoval等。通过这些方法，LinkedHashMap可以对这些结点进行一些特性化的维护。当遍历LinkedHashMap时通过遍历链表代替遍历Map中的各个槽，从而实现按序访问。LinkedHashMap如何实现有序的LinkedHashMap在HashMap的基础上，还将每个key-value对应的Node维护在了一个额外的双向链表中。LinkedHashMap通过accessOrder可以支持按插入的顺序访问，或者按遍历的顺序访问accessOrder遍历的时候，通过便利双向链表代替遍历map的每个槽，来实现顺序访问。如何用LinkedHashMap实现LRU首先分析LRU算法有哪些特性在LinkedHashMap保证结点有序的情况下，通过设置accessOrder为true，采用按遍历顺序维护结点。;";
        juejinArticleEntity.summary = content.substring(0, 100);
        int ret = juejinArticleMapper.insertSelective(juejinArticleEntity);

        log.info("ret={};", ret);
    }

    @Test
    public void select() {
        long id = 20L;
        JuejinArticleEntity juejinArticleEntity = juejinArticleMapper.selectByPrimaryKey(id);
        log.info("juejinArticleEntity={};", juejinArticleEntity);

        id = 5L;
        juejinArticleEntity = juejinArticleMapper.selectByPrimaryKey(id);
        log.info("juejinArticleEntity={};", juejinArticleEntity);

    }

    @Test
    public void update() {
        long id = 1L;
        JuejinArticleEntity juejinArticleEntity = juejinArticleMapper.selectByPrimaryKey(id);
        log.info("juejinArticleEntity={};", juejinArticleEntity);

        juejinArticleEntity.setSummary("java ssm hashmap");

        int update = juejinArticleMapper.updateByPrimaryKeySelective(juejinArticleEntity);

        log.info("update={};", update);

    }


    /**
     * 十分钟快速教你理解Spring中的FactoryBean接口,
     * (https://zhuanlan.zhihu.com/p/97005407);
     */
    @Test
    public void factoryBean() {

    }

    @Test
    public void propertySourcesPlaceholderConfigurer() {

        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();

        MutablePropertySources mutablePropertySources = new MutablePropertySources();

        StandardEnvironment standardEnvironment = new StandardEnvironment();
        String s = standardEnvironment.resolvePlaceholders("${wjt_train_jdbc_url}");
        log.info("s={};", s);

    }

    @Test
    public void nullInstanceof() {

        String s = null;
        boolean b = (s instanceof String);
        log.info("b={};", b);
        b = (s instanceof Object);
        log.info("b={};", b);
    }

    /**
     * 属性值注入;
     */
    @Test
    public void properties() {
        String name = "dataSource";
        Object bean = this.applicationContext.getBean(name);
        log.info("bean={};", bean);
    }

    @Test
    public void annotatedElement() throws NoSuchFieldException, IOException {

        Class<?> clazz = ExistCheckerImpl.class;

        Field field = clazz.getDeclaredField("jedisPool");
        AnnotationAttributes mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(field, Resource.class);

        log.info("mergedAnnotationAttributes={};", mergedAnnotationAttributes);

        clazz = DataSourceConfig.class;
        field = clazz.getDeclaredField("driverClassName");
        mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(field, Value.class);
        String value = mergedAnnotationAttributes.getString("value");

        log.info("mergedAnnotationAttributes={};value={};", mergedAnnotationAttributes, value);

        String location = "classpath:/dao/jdbc.properties";
        /**
         * 构造propertySource;
         */
        PropertySource propertySource = new ResourcePropertySource(location);
        /**
         * 构造environment;
         */
        StandardEnvironment standardEnvironment = new StandardEnvironment();
        standardEnvironment.getPropertySources().addFirst(propertySource);

        log.info("propertySource={};", propertySource);
        String[] propertyNames = ((ResourcePropertySource) propertySource).getPropertyNames();
        log.info("---------properties:");
        for (String propertyName : propertyNames) {
            log.info("propertyName={};propertyValue={};", propertyName, propertySource.getProperty(propertyName));
        }

        log.info("environment_resolve:");
        log.info("value={};resolvedValue={};", value, standardEnvironment.resolvePlaceholders(value));

        String resolvedValue = null;
        Field[] fields = clazz.getDeclaredFields();
        log.info("++++++fields:");
        /**
         * 解析每个注解@Autowired、@Value的取值;
         */
        for (Field fieldt : fields) {
            AnnotationAttributes autowiredAnnotation = findAutowiredAnnotation(fieldt);
            if (autowiredAnnotation != null) {
                value = autowiredAnnotation.getString("value");
                resolvedValue = standardEnvironment.resolvePlaceholders(value);
            }
            log.info("field={};autowiredAnnotation={};resolvedValue={};", fieldt, autowiredAnnotation, resolvedValue);
        }

    }

    /**
     * spring类型转换器;
     */
    @Test
    public void typeConverter() {

        TypeConverter typeConverter = new SimpleTypeConverter();
        Long t = typeConverter.convertIfNecessary("12323", Long.class);
        log.info("t={};", t);

        Integer v = typeConverter.convertIfNecessary("-234235", int.class);
        log.info("v={};", v);

        Double aDouble = typeConverter.convertIfNecessary("-24390.00924", Double.class);
        log.info("aDouble={};", aDouble);

    }

    @Test
    public void dependencyDescriptor(){
        //DependencyDescriptor dependencyDescriptor=new DependencyDescriptor();


    }

    /**
     * 函数参数名称和类型;
     */
    @Test
    public void parameterNameDiscoverer() {

        ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
        Class<MyDefaultListableBeanFactory> beanFactoryClass = MyDefaultListableBeanFactory.class;
        Method[] declaredMethods = beanFactoryClass.getDeclaredMethods();
        log.info("declaredMethods={};", Arrays.asList(declaredMethods));

        log.info("+++method_parameter:");
        for (Method method : declaredMethods) {

            Class<?>[] parameterTypes = method.getParameterTypes();
            Class<?> returnType = method.getReturnType();
            String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
            System.out.println("--------");
            System.out.println("parameterTypes=" + Arrays.asList(parameterTypes) + ";returnType=" + returnType);
            System.out.println(method + ";parameterNames:" + Arrays.asList(parameterNames));
        }


    }


    private static final Set<Class<? extends Annotation>> AUTOWIRED_ANNOTATION_TYPES =
            new LinkedHashSet<Class<? extends Annotation>>(Lists.newArrayList(Autowired.class, Value.class));

    private static AnnotationAttributes findAutowiredAnnotation(AccessibleObject ao) {
        //log.info("ao={};ao.getAnnotations={};", ao, ao.getAnnotations());
        if (ao.getAnnotations().length > 0) {
            AnnotationAttributes annotationAttributes = new AnnotationAttributes();

            Autowired annotationAutowired = ao.getAnnotation(Autowired.class);
            if (annotationAutowired != null) {
                annotationAttributes.put("required", annotationAutowired.required());
            }

            Value annotationValue = ao.getAnnotation(Value.class);
            if (annotationValue != null) {
                //AnnotationAttributes annotationAttributes = new AnnotationAttributes(Value.class);
                annotationAttributes.put("value", annotationValue.value());
            }
            annotationAttributes = (annotationAttributes.size() > 0 ? annotationAttributes : null);

            return annotationAttributes;


/*            for (Class<? extends Annotation> type : AUTOWIRED_ANNOTATION_TYPES) {

                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null) {
                    return attributes;
                }
            }*/

        }
        return null;
    }



/*

    private static AnnotationAttributes findAutowiredAnnotationStd(AccessibleObject ao) {
        if (ao.getAnnotations().length > 0) {
            for (Class<? extends Annotation> type : AUTOWIRED_ANNOTATION_TYPES) {
                AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ao, type);
                if (attributes != null) {
                    return attributes;
                }
            }
        }
        return null;
    }


    private static InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {
        LinkedList<InjectionMetadata.InjectedElement> elements = new LinkedList<InjectionMetadata.InjectedElement>();
        Class<?> targetClass = clazz;

        do {
            final LinkedList<InjectionMetadata.InjectedElement> currElements =
                    new LinkedList<InjectionMetadata.InjectedElement>();

            ReflectionUtils.doWithLocalFields(targetClass, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                    AnnotationAttributes ann = findAutowiredAnnotation(field);
                    if (ann != null) {
                        if (Modifier.isStatic(field.getModifiers())) {
                            log.warn("Autowired annotation is not supported on static fields: " + field);
                            return;
                        }
                        boolean required = (ann.containsKey("required") && ann.getBoolean("required") == true);
                        currElements.add(new AutowiredFieldElement(field, required));
                    }
                }
            });

            ReflectionUtils.doWithLocalMethods(targetClass, new ReflectionUtils.MethodCallback() {
                @Override
                public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                    Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(method);
                    if (!BridgeMethodResolver.isVisibilityBridgeMethodPair(method, bridgedMethod)) {
                        return;
                    }
                    AnnotationAttributes ann = findAutowiredAnnotation(bridgedMethod);
                    if (ann != null && method.equals(ClassUtils.getMostSpecificMethod(method, clazz))) {
                        if (Modifier.isStatic(method.getModifiers())) {
                            log.warn("Autowired annotation is not supported on static methods: " + method);
                            return;
                        }
                        if (method.getParameterTypes().length == 0) {
                            log.warn("Autowired annotation should only be used on methods with parameters: " +
                                    method);
                        }
                        boolean required = determineRequiredStatus(ann);
                        PropertyDescriptor pd = BeanUtils.findPropertyForMethod(bridgedMethod, clazz);
                        currElements.add(new AutowiredMethodElement(method, required, pd));
                    }
                }
            });

            elements.addAll(0, currElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);

        return new InjectionMetadata(clazz, elements);
    }

    private static boolean determineRequiredStatus(AnnotationAttributes ann) {
        return (!ann.containsKey("required") || ann.getBoolean("required"));
    }


    */
/**
 * Class representing injection information about an annotated field.
 *//*

    //private class AutowiredFieldElement extends InjectionMetadata.InjectedElement {
    public static class AutowiredFieldElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached = false;

        private volatile Object cachedFieldValue;

        public AutowiredFieldElement(Field field, boolean required) {
            super(field, null);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            Field field = (Field) this.member;
            Object value;
            if (this.cached) {
                value = resolvedCachedArgument(beanName, this.cachedFieldValue);
            } else {
                DependencyDescriptor desc = new DependencyDescriptor(field, this.required);
                desc.setContainingClass(bean.getClass());
                Set<String> autowiredBeanNames = new LinkedHashSet<String>(1);
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                try {
                    value = beanFactory.resolveDependency(desc, beanName, autowiredBeanNames, typeConverter);
                } catch (BeansException ex) {
                    log.error("resolveDependency error!bean={};desc={};beanName={};autowiredBeanNames={};typeConverter={};pvs={};", bean, desc, beanName, autowiredBeanNames, typeConverter, pvs);
                    throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(field), ex);
                }
                synchronized (this) {
                    if (!this.cached) {
                        if (value != null || this.required) {
                            this.cachedFieldValue = desc;
                            registerDependentBeans(beanName, autowiredBeanNames);
                            if (autowiredBeanNames.size() == 1) {
                                String autowiredBeanName = autowiredBeanNames.iterator().next();
                                if (beanFactory.containsBean(autowiredBeanName)) {
                                    if (beanFactory.isTypeMatch(autowiredBeanName, field.getType())) {
                                        this.cachedFieldValue = new MyAutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(
                                                desc, autowiredBeanName, field.getType());
                                    }
                                }
                            }
                        } else {
                            this.cachedFieldValue = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (value != null) {
                ReflectionUtils.makeAccessible(field);
                field.set(bean, value);
            }
        }
    }


    */
/**
 * Class representing injection information about an annotated method.
 *//*

    //private class AutowiredMethodElement extends InjectionMetadata.InjectedElement {

    public static class AutowiredMethodElement extends InjectionMetadata.InjectedElement {

        private final boolean required;

        private volatile boolean cached = false;

        private volatile Object[] cachedMethodArguments;

        public AutowiredMethodElement(Method method, boolean required, PropertyDescriptor pd) {
            super(method, pd);
            this.required = required;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            if (checkPropertySkipping(pvs)) {
                return;
            }
            Method method = (Method) this.member;
            Object[] arguments;
            if (this.cached) {
                // Shortcut for avoiding synchronization...
                arguments = resolveCachedArguments(beanName);
            } else {
                Class<?>[] paramTypes = method.getParameterTypes();
                arguments = new Object[paramTypes.length];
                DependencyDescriptor[] descriptors = new DependencyDescriptor[paramTypes.length];
                Set<String> autowiredBeans = new LinkedHashSet<String>(paramTypes.length);
                TypeConverter typeConverter = beanFactory.getTypeConverter();
                for (int i = 0; i < arguments.length; i++) {
                    MethodParameter methodParam = new MethodParameter(method, i);
                    DependencyDescriptor currDesc = new DependencyDescriptor(methodParam, this.required);
                    currDesc.setContainingClass(bean.getClass());
                    descriptors[i] = currDesc;
                    try {
                        Object arg = beanFactory.resolveDependency(currDesc, beanName, autowiredBeans, typeConverter);
                        if (arg == null && !this.required) {
                            arguments = null;
                            break;
                        }
                        arguments[i] = arg;
                    } catch (BeansException ex) {
                        throw new UnsatisfiedDependencyException(null, beanName, new InjectionPoint(methodParam), ex);
                    }
                }
                synchronized (this) {
                    if (!this.cached) {
                        if (arguments != null) {
                            this.cachedMethodArguments = new Object[paramTypes.length];
                            for (int i = 0; i < arguments.length; i++) {
                                this.cachedMethodArguments[i] = descriptors[i];
                            }
                            registerDependentBeans(beanName, autowiredBeans);
                            if (autowiredBeans.size() == paramTypes.length) {
                                Iterator<String> it = autowiredBeans.iterator();
                                for (int i = 0; i < paramTypes.length; i++) {
                                    String autowiredBeanName = it.next();
                                    if (beanFactory.containsBean(autowiredBeanName)) {
                                        if (beanFactory.isTypeMatch(autowiredBeanName, paramTypes[i])) {
                                            this.cachedMethodArguments[i] = new MyAutowiredAnnotationBeanPostProcessor.ShortcutDependencyDescriptor(
                                                    descriptors[i], autowiredBeanName, paramTypes[i]);
                                        }
                                    }
                                }
                            }
                        } else {
                            this.cachedMethodArguments = null;
                        }
                        this.cached = true;
                    }
                }
            }
            if (arguments != null) {
                try {
                    ReflectionUtils.makeAccessible(method);
                    method.invoke(bean, arguments);
                } catch (InvocationTargetException ex) {
                    throw ex.getTargetException();
                }
            }
        }

        private Object[] resolveCachedArguments(String beanName) {
            if (this.cachedMethodArguments == null) {
                return null;
            }
            Object[] arguments = new Object[this.cachedMethodArguments.length];
            for (int i = 0; i < arguments.length; i++) {
                arguments[i] = resolvedCachedArgument(beanName, this.cachedMethodArguments[i]);
            }
            return arguments;
        }
    }
*/


    /**
     * mvn clean test -Dtest=com.wjt.config.DBConfigTest#jdbc
     * <p>
     * 参考:
     * (https://www.jianshu.com/p/c0acbd18794c);
     * (https://docs.oracle.com/javase/8/docs/api/java/sql/package-summary.html);
     * (https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html);
     *
     * @throws ClassNotFoundException
     */
    @Test
    public void jdbc() {
        final String url = "jdbc:mysql://127.0.0.1:3306/web_train?useAffectedRows=true&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&connectTimeout=1000&socketTimeout=1000&autoReconnect=true";
        //final String url="jdbc:mysql://127.0.0.1:3306/web_train";
        final Properties properties = new Properties();
        //useAffectedRows=true&allowMultiQueries=true&characterEncoding=utf8&useUnicode=true&useSSL=false&serverTimezone=Asia/Shanghai&autoReconnect=true&failOverReadOnly=false&maxReconnects=10&connectTimeout=1000&socketTimeout=1000&autoReconnect=true
        properties.setProperty("user", "root");
        properties.setProperty("password", "linux2014");

        final String driverClassName = "com.mysql.cj.jdbc.Driver";
        //加载mysql驱动;
        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            log.error("load driver class error!", e);
        }
        //建立mysql连接;
        try (Connection conn = DriverManager.getConnection(url, properties)) {
            conn.setAutoCommit(true);

            DatabaseMetaData databaseMetaData = conn.getMetaData();
            int holdability = conn.getHoldability();

            log.info("typeMap={};", conn.getTypeMap());

            insertTask(conn);
            //deleteTask(conn);

        } catch (Exception e) {
            log.error("db conn error!", e);
        } finally {

        }
    }

    private static void insertTask(final Connection conn) {
        final String sql = "insert into juejin_article(title,url,summary) values(?,?,?)";
        //conn.setTransactionIsolation();
        //插入记录;
        new MySqlConnTask() {
            @Override
            public Object doTask(Connection conn, Object obj) {

                try (PreparedStatement preparedStatement = conn.prepareStatement(sql, new int[]{1, 2, 3})) {

                    preparedStatement.setString(1, "RocketMQ架构");
                    preparedStatement.setString(2, "https://juejin.im/post/6844903830874750990");
                    preparedStatement.setString(3, "Apache RocketMQ是一个分布式消息和流处理平台，具有低延迟，高性能和高可靠性，亿万级容量和灵活的可扩展性。它由四部分组成：名称服务器，代理服务器，生产者和消费者。它们中的每一个都可以水平扩展，而不会出现单点故障。如上图所示。");

                    int update = preparedStatement.executeUpdate();
                    ResultSet resultSet = preparedStatement.getResultSet();
                    ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                    log.info("update={};resultSet={};generatedKeys={};", update, resultSet, generatedKeys);

                    //resultSet(resultSet);
                    resultSet(generatedKeys, new int[]{1});

                } catch (SQLException e) {
                    log.error("preparedStatement error!", e);
                }

                return null;
            }
        }.doTask(conn, null);
    }

    private static void resultSet(final ResultSet resultSet, final int[] columnIdxes) {
        try {
            while (resultSet.next()) {
                //log.info("resultSet={};", resultSet);
                final StringBuilder stringBuilder = new StringBuilder();
                for (int idx : columnIdxes) {
                    stringBuilder.append(resultSet.getObject(idx)).append(",");
                }
                log.info("row={};", stringBuilder.toString());
            }
        } catch (Exception e) {
            log.error("resultSet error!", e);
        }

    }


    private static void deleteTask(final Connection conn) {
        final String sql = "delete from juejin_article where id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(sql, new int[]{1, 2, 3})) {
            preparedStatement.setLong(1, 11L);
            int update = preparedStatement.executeUpdate();
            log.info("update={};", update);
        } catch (Exception e) {
            log.error("preparedStatement error!", e);
        } finally {

        }
    }

    /**
     * 利用mysql连接执行任务;
     */
    static interface MySqlConnTask {
        Object doTask(final Connection conn, Object obj);
    }


    /**
     * 参考(https://ke.qq.com/webcourse/index.html#cid=2024404&term_id=102125540&taid=8106965521589204);
     */
    @Test
    public void mybatisProxy() {
        Proxy proxy;

        MySqlSessionFactoryBean mySqlSessionFactoryBean;
    }


}


