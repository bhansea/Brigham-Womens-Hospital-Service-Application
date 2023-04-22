package edu.wpi.punchy_pegasi;

import com.jsoniter.spi.JsoniterSpi;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        // !!! No code should be run before App.launch as that is where the singleton
        // !!! is loaded
        App.launch(App.class, args);
    }
}
