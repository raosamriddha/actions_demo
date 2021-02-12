/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.utility;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Android like wrapper to system.out.print
 *
 * @author Anuj Pathak
 */
public class sLog {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern(/*"yyyy/MM/dd*/ "HH:mm:ss.SSS");

    public static void d(Object o, String msg) {
        System.out.println(dtf.format(LocalDateTime.now()) + ": " + o.getClass().getSimpleName() + ": " + msg);
    }

    public static void d(String o, String msg) {
        System.out.println(dtf.format(LocalDateTime.now()) + ": " + o + ": " + msg);
    }

    public static void d(Object o, Object d) {
        System.out.println(dtf.format(LocalDateTime.now()) + ": " + o.getClass().getSimpleName() + ": " + gson.toJson(d));
    }

    public static void d(Object o) {
        System.out.println(dtf.format(LocalDateTime.now()) + ": " + o.getClass().getSimpleName() + ": " + gson.toJson(o));
    }

    public static void e(Object o, String msg) {
        System.err.println(dtf.format(LocalDateTime.now()) + ": " + o.getClass().getSimpleName() + ": " + msg);
    }

}
