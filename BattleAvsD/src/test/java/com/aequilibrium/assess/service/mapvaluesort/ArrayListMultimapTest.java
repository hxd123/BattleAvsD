package com.aequilibrium.assess.service.mapvaluesort;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class ArrayListMultimapTest {

    public static final String Grade_NAME_1 = "一年级";
    public static final String Grade_NAME_2 = "二年级";

    class Student {
        String id;
        String name;
    }

    // Multimap的创建
    // 创建一个ArrayListMultimap
    Multimap<String, Student> multimap = ArrayListMultimap.create();

    // Multimap的put方法
    // 往Multimap中添加键值对
    public Multimap<String, Student> createStudent() {
        for (int i = 0; i < 5; i++) {
            Student student = new Student();
            student.id = "1000" + i;
            student.name = "Tom" + i;
            multimap.put(Grade_NAME_1, student);
        }
        for (int i = 0; i < 5; i++) {
            Student student = new Student();
            student.id = "2000" + i;
            student.name = "Lily" + i;
            multimap.put(Grade_NAME_2, student);
        }
        return multimap;
    }

    public static void main(String[] args) {
        ArrayListMultimapTest multimapTest = new ArrayListMultimapTest();
        Multimap<String, ArrayListMultimapTest.Student> multimap = multimapTest.createStudent();
        // 输出Multimap
        System.out.println("==========输出Multimap==========");
        System.out.println(multimap);
        System.out.print("\n");
        // Multimap的size方法
        System.out.println("==========Multimap的size方法==========");
        System.out.println(multimap.size());
        System.out.print("\n");
        // Multimap的get方法
        System.out.println("==========Multimap的get方法==========");
        for (ArrayListMultimapTest.Student s : multimap.get(ArrayListMultimapTest.Grade_NAME_1)) {
            System.out.println(ArrayListMultimapTest.Grade_NAME_1 + " " + s.id + " " + s.name);
        }
        for (ArrayListMultimapTest.Student s : multimap.get(ArrayListMultimapTest.Grade_NAME_2)) {
            System.out.println(ArrayListMultimapTest.Grade_NAME_2 + " " + s.id + " " + s.name);
        }
        System.out.print("\n");
        // Multimap的keys方法和keySet方法
        System.out.println("==========Multimap的keys方法和keySet方法==========");
        System.out.println(multimap.keys());
        System.out.println(multimap.keySet());
    }
}

