package com.aequilibrium.assess.service.mapvaluesort;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class User implements Serializable, Comparator<User> {

    private static final Logger LOGGER_FACTORY = LoggerFactory.getLogger(User.class);

    private int id;

    private String name;

    private int age;

    public User() {
    }

    public User(Integer id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int compare(User o1, User o2) {
        return Ints.compare(o1.getAge(), o2.getAge());
    }

    public static void main(String[] args) {
        User user = new User();
        User u1 = new User(1, "Tom", 25);
        User u2 = new User(2, "Wind", 30);
        User u3 = new User(3, "William", 28);
        List<User> users = Lists.newArrayList(u1, u2, u3);

        Ordering ordering = Ordering.from(user);
        System.out.println("排序前：" + JSON.toJSONString(users));

        System.out.println("======排序后======");
        Collections.sort(users, ordering);
        System.out.println("sort result：" + JSON.toJSONString(users));
        // 从大至小，第二个参数是取几个
        List<User> max = Ordering.from(user).greatestOf(users, 1);
        User maxUser = max.get(0);
        System.out.println("maxUser = " + JSON.toJSONString(maxUser));

        // 从小直大
        List<User> min = Ordering.from(user).leastOf(users, 1);
        User minUser = min.get(0);
        System.out.println("minUser = " + JSON.toJSONString(minUser));

        //多参数排序
        InnerClass innerClass = new InnerClass();
        Ordering<User> secondaryOrdering = Ordering.from(innerClass).compound(user);
        Collections.sort(users, secondaryOrdering);
        Iterator<User> cityIterator = users.iterator();
        while (cityIterator.hasNext()) {
            System.out.println("多参数：" + JSON.toJSONString(cityIterator.next()));
        }

        //反转，以最大的值开始输出结果
        Ordering<User> reverse = Ordering.from(user).reverse();
        Collections.sort(users, reverse);
        Iterator<User> cityByRainfallIterator = users.iterator();
        while (cityByRainfallIterator.hasNext()) {
            System.out.println("反转：" + JSON.toJSONString(cityByRainfallIterator.next()));
        }
        //取1个最大值
        Ordering<User> orderingMax = Ordering.from(user);
        List<User> userMax = orderingMax.greatestOf(users, 1);
        System.out.println("最大年龄" + JSON.toJSONString(userMax.get(0)));

        //取1个最小值
        Ordering<User> orderingMin = Ordering.from(user);
        List<User> userMin = orderingMin.leastOf(users, 1);
        System.out.println("最小年龄" + JSON.toJSONString(userMin.get(0)));

        //null排序在第一个
        users.add(null);
        Ordering<User> nullsFirst = Ordering.from(user).nullsFirst();
        Collections.sort(users, nullsFirst);
        System.out.println("null排序在第一个" + JSON.toJSONString(users));
        //null排序在最后一个
        Ordering<User> nullsLast = Ordering.from(user).nullsLast();
        Collections.sort(users, nullsLast);
        System.out.println("null排序在最后" + JSON.toJSONString(users));
    }

}

class InnerClass implements Comparator<User> {
    @Override
    public int compare(User o1, User o2) {
        return Ints.compare(o1.getId(), o2.getId());
    }

}
