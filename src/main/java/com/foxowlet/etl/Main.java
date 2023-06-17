package com.foxowlet.etl;


import com.foxowlet.etl.core.Pipeline;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class Main {
    private static final String DEFAULT_KEY = "fruitShop";

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.getEnvironment().setActiveProfiles(getShopKey(args));
        context.scan("com.foxowlet.etl");
        context.refresh();
        context.getBean(Pipeline.class).run();
    }

    private static String getShopKey(String[] args) {
        return args.length == 0 ? DEFAULT_KEY : args[0];
    }
}
