package com.myspring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.MyAnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ConfigurationClassUtils {

    public static void processConfigBeanDefinitions(MyAnnotationConfigApplicationContext applicationContext,
                                                    MetadataReaderFactory metadataReaderFactory,
                                                    SourceExtractor sourceExtractor, ProblemReporter problemReporter, ResourceLoader resourceLoader,
                                                    BeanNameGenerator componentScanBeanNameGenerator) throws IOException, ClassNotFoundException {

        final ResourcePatternResolver resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        BeanDefinitionRegistry registry = applicationContext;

        Set<Class<?>> candidateConfigSet = new HashSet<>();
        Set<Class<?>> newCandidateConfigSet = new HashSet<>();
        final Set<String> processedClassNameSet = new HashSet<>();
        final Set<Class<?>> allComponentScanClasses = new HashSet<>();

        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(Configuration.class);

        /**
         * 初始化首批Configuration类集合;
         */
        for (String beanNameForAnnotation : beanNamesForAnnotation) {
            RootBeanDefinition beanDefinition = (RootBeanDefinition) (applicationContext.getBeanDefinition(beanNameForAnnotation));
            if (!processedClassNameSet.contains(beanDefinition.getBeanClassName())) {
                candidateConfigSet.add(beanDefinition.getBeanClass());

                processedClassNameSet.add(beanDefinition.getBeanClassName());
            }
        }

        while (candidateConfigSet.size() > 0) {

            /**
             * 逐个处理每个@Configuration class;
             * 1、加载bean definition+bean_method;
             * 2、@Import;
             * 3、@ComponentScan;
             */
            for (Class<?> candidateConfigClass : candidateConfigSet) {
                //防止重复;
                processedClassNameSet.add(candidateConfigClass.getName());

                Import importAnn = candidateConfigClass.getAnnotation(Import.class);
                if (importAnn != null) {
                    Class<?>[] importedClasses = importAnn.value();
                    if (importedClasses != null && importedClasses.length > 0) {
                        //将未被处理过且有@Configuration注解的类加入;
                        Set<Class<?>> newConfigClasses = Arrays.asList(importedClasses).stream().filter(importedClass -> !processedClassNameSet.contains(importedClass.getName()) && importedClass.isAnnotationPresent(Configuration.class)).collect(Collectors.toSet());
                        newCandidateConfigSet.addAll(newConfigClasses);
                    }
                }

                ComponentScan componentScanAnn = candidateConfigClass.getAnnotation(ComponentScan.class);
                if (componentScanAnn != null) {
                    Class<?>[] classes = componentScanAnn.basePackageClasses();
                    //todo;找到指定目录下的所有类;
                    for (Class<?> clazz : classes) {
                        allComponentScanClasses.addAll(findAllComponentScanClasses(clazz, environment, resourcePatternResolver, metadataReaderFactory, processedClassNameSet));
                    }
                }
            }
            /**
             * ;
             */
            candidateConfigSet = newCandidateConfigSet;
            newCandidateConfigSet = new HashSet<>();
        }

    }

    public static void configClazzToRootBeanDefinition(BeanDefinitionRegistry registry, Class<?> clazz, BeanNameGenerator beanNameGenerator) {
        //@Configuration;
        AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(clazz);
        String configBeanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
        registry.registerBeanDefinition(configBeanName, beanDefinition);

        //@Bean;
        Method[] declaredMethods = clazz.getDeclaredMethods();
        if (declaredMethods != null && declaredMethods.length > 0) {
            for (Method method : declaredMethods) {
                if (method.isAnnotationPresent(Bean.class) && Modifier.isPublic(method.getModifiers())) {

                    Bean beanAnn = method.getAnnotation(Bean.class);

                    Class<?> returnType = method.getReturnType();
                    String beanName = method.getName();

                    beanDefinition = new AnnotatedGenericBeanDefinition(returnType);
                    beanDefinition.setFactoryMethodName(method.getName());
                    beanDefinition.setFactoryBeanName(configBeanName);

                    beanDefinition.setInitMethodName(beanAnn.initMethod());
                    beanDefinition.setDestroyMethodName(beanAnn.destroyMethod());

                    registry.registerBeanDefinition(beanName, beanDefinition);
                }
            }
        }

    }

    public static Set<Class<?>> findAllComponentScanClasses(final Class<?> componentScanClass, Environment environment,
                                                            ResourcePatternResolver resourcePatternResolver, MetadataReaderFactory metadataReaderFactory,
                                                            Set<String> processedClassNameSet) throws IOException, ClassNotFoundException {

        final Set<Class<?>> allComponentScanClassess = new HashSet<>();
        //类->类名->包名->包路径;
        String packageName = ClassUtils.getPackageName(componentScanClass);
        String resourcePath = ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(packageName));

        //包路径->class路径->class资源文件集合;
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourcePath + '/' + "**/*.class";
        log.info("resourcePatternResolver={};packageSearchPath={};", resourcePatternResolver, packageSearchPath);

        Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
        log.info("resources={};", Arrays.asList(resources));
        if (resources != null && resources.length > 0) {
            for (Resource resource : resources) {
                //class资源文件->clazz;
                if (resource.isReadable()) {
                    MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
                    String className = metadataReader.getClassMetadata().getClassName();
                    AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                    if ((annotationMetadata.hasAnnotation(Component.class.getName()) || annotationMetadata.hasMetaAnnotation(Component.class.getName())) && (!processedClassNameSet.contains(className))) {
                        Class<?> clazz = Class.forName(className);
                        allComponentScanClassess.add(clazz);

                        processedClassNameSet.add(className);
                    }
                }
            }
        }

        return allComponentScanClassess;
    }


/*
    public static Set<Class<?>> findClassesInPackage(String packageName) throws IOException {
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packageName);

    }
*/


}
