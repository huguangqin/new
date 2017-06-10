package cn.itcast.demo;

import java.io.Serializable;

public class Student implements Serializable {

    // 扩展点1：所有的POJO都应该实现序列化接口，并指定序列号，无论是否用到。
    private static final long serialVersionUID = 1L;

    private int id; // 学号
    private String name; // 姓名
    private int age; // 年龄
    private String address; // 所在地
    
    // 扩展点29：POJO中的boolean基本类型的属性对应的getter方法应命名为isXxx，且属性名不应带有is前缀。

    public Student() {
    }

    public Student(int id, String name, int age, String address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
