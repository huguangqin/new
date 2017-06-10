package cn.itcast.demo;

import java.io.Serializable;

public class Student implements Serializable {

    // ��չ��1�����е�POJO��Ӧ��ʵ�����л��ӿڣ���ָ�����кţ������Ƿ��õ���
    private static final long serialVersionUID = 1L;

    private int id; // ѧ��
    private String name; // ����
    private int age; // ����
    private String address; // ���ڵ�
    
    // ��չ��29��POJO�е�boolean�������͵����Զ�Ӧ��getter����Ӧ����ΪisXxx������������Ӧ����isǰ׺��

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
