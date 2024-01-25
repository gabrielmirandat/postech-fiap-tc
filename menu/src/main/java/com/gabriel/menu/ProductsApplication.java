package com.gabriel.menu;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ProductsApplication {
    public static void main(String... args) {
        System.out.println("Running main method");
        Quarkus.run(args);
    }
}
