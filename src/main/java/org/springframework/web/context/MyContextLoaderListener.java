/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.web.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Bootstrap listener to start up and shut down Spring's root {@link WebApplicationContext}.
 * Simply delegates to {@link ContextLoader} as well as to {@link ContextCleanupListener}.
 *
 * <p>This listener should be registered after {@link org.springframework.web.util.Log4jConfigListener}
 * in {@code web.xml}, if the latter is used.
 *
 * <p>As of Spring 3.1, {@code MyContextLoaderListener} supports injecting the root web
 * application context via the {@link #MyContextLoaderListener(WebApplicationContext)}
 * constructor, allowing for programmatic configuration in Servlet 3.0+ environments.
 * See {@link org.springframework.web.WebApplicationInitializer} for usage examples.
 *
 * @author Juergen Hoeller
 * @author Chris Beams
 * @see #setContextInitializers
 * @see org.springframework.web.WebApplicationInitializer
 * @see org.springframework.web.util.Log4jConfigListener
 * @since 17.02.2003
 */
public class MyContextLoaderListener extends MyContextLoader implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(MyContextLoaderListener.class);

    /**
     * Create a new {@code MyContextLoaderListener} that will create a web application
     * context based on the "contextClass" and "contextConfigLocation" servlet
     * context-params. See {@link ContextLoader} superclass documentation for details on
     * default values for each.
     * <p>This constructor is typically used when declaring {@code MyContextLoaderListener}
     * as a {@code <listener>} within {@code web.xml}, where a no-arg constructor is
     * required.
     * <p>The created application context will be registered into the ServletContext under
     * the attribute name {@link WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE}
     * and the Spring application context will be closed when the {@link #contextDestroyed}
     * lifecycle method is invoked on this listener.
     *
     * @see ContextLoader
     * @see #MyContextLoaderListener(WebApplicationContext)
     * @see #contextInitialized(ServletContextEvent)
     * @see #contextDestroyed(ServletContextEvent)
     */
    public MyContextLoaderListener() {
    }

    /**
     * Create a new {@code MyContextLoaderListener} with the given application context. This
     * constructor is useful in Servlet 3.0+ environments where instance-based
     * registration of listeners is possible through the {@link javax.servlet.ServletContext#addListener}
     * API.
     * <p>The context may or may not yet be {@linkplain
     * org.springframework.context.ConfigurableApplicationContext#refresh() refreshed}. If it
     * (a) is an implementation of {@link ConfigurableWebApplicationContext} and
     * (b) has <strong>not</strong> already been refreshed (the recommended approach),
     * then the following will occur:
     * <ul>
     * <li>If the given context has not already been assigned an {@linkplain
     * org.springframework.context.ConfigurableApplicationContext#setId id}, one will be assigned to it</li>
     * <li>{@code ServletContext} and {@code ServletConfig} objects will be delegated to
     * the application context</li>
     * <li>{@link #customizeContext} will be called</li>
     * <li>Any {@link org.springframework.context.ApplicationContextInitializer ApplicationContextInitializer}s
     * specified through the "contextInitializerClasses" init-param will be applied.</li>
     * <li>{@link org.springframework.context.ConfigurableApplicationContext#refresh refresh()} will be called</li>
     * </ul>
     * If the context has already been refreshed or does not implement
     * {@code ConfigurableWebApplicationContext}, none of the above will occur under the
     * assumption that the user has performed these actions (or not) per his or her
     * specific needs.
     * <p>See {@link org.springframework.web.WebApplicationInitializer} for usage examples.
     * <p>In any case, the given application context will be registered into the
     * ServletContext under the attribute name {@link
     * WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE} and the Spring
     * application context will be closed when the {@link #contextDestroyed} lifecycle
     * method is invoked on this listener.
     *
     * @param context the application context to manage
     * @see #contextInitialized(ServletContextEvent)
     * @see #contextDestroyed(ServletContextEvent)
     */
    public MyContextLoaderListener(WebApplicationContext context) {
        super(context);
    }


    /**
     * Initialize the root web application context.
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        final long start = System.currentTimeMillis();
        log.info("test;start to initialize context!");
        initWebApplicationContext(event.getServletContext());
        log.info("test;finish initializing context!elapsed={}ms;", (System.currentTimeMillis() - start));
    }


    /**
     * Close the root web application context.
     */
    @Override
    public void contextDestroyed(ServletContextEvent event) {
        final long start = System.currentTimeMillis();
        log.info("test;start to destroy context;");
        closeWebApplicationContext(event.getServletContext());
        ContextCleanupListener.cleanupAttributes(event.getServletContext());
        log.info("test;finish destroying context!elapsed={}ms;", (System.currentTimeMillis() - start));
    }

}
