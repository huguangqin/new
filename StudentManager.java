package cn.itcast.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StudentManager {

    /*
     * ��չ��4��Ϊ�˱�������ڵ��̰߳�ȫ�����⣬���Ǳ�Ҫ������Ҫʹ�þ�̬�����ԡ�
     * ����ʵ���ɱ����һ�����̰߳�ȫ�ġ����⣬�������������ԣ����Ծֲ������̰߳�ȫ��
     */
    // ��չ��23���������⺬��ĳ���ɢ���ڴ��������ͳһ���塣
    private static final int ILLEGAL = -1;
    private static final String FILE_NAME = "data.txt";
    private static final int RETRY = 3;

    public static void main(String[] args) {
        // ��չ��26��������ķ������������main�����Ĳ������˳����쳣Ӧ������
        try {
            doManage();
        } catch (FileNotFoundException e) {
            // ��չ��27��������Ϣ����ʹ��syse���������
            System.err.println(String.format("�Ҳ����ļ�[%s]�����ֶ��½�����", FILE_NAME));
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            // ��չ��28����JDK1.7��ʼ�������쳣����ʹ�����߷ָ���ͬʱ����
            System.err.println("����������Խ����ָ���쳣�����Ͻ����ģ�������롣");
            e.printStackTrace();
        } catch (Throwable other) {
            System.err.println("������δ֪����" + other.getMessage());
            other.printStackTrace();
        }
        System.out.println("���������");
    }

    private static void doManage() throws IOException {
        // ��չ��2��Scanner����һ���͹��ˣ�ͨ������ʹ��������Ҫ�������������Ҫɨ�費ͬ��Դ��
        Scanner sc = new Scanner(System.in);
        // ��չ��3����JDK1.7��ʼ���Ҳ�ķ��Ϳ����Ƶ������Կ���ʡ�ԡ�
        // ��չ��16���������úͽӿڸ���ԭ�򣬾�����ʹ�ó���ӿڶ��Ǿ���ʵ���ࣺList��
        List<Student> list = new ArrayList<>();
        exit: while (true) {
            printWelcome();
            switch (sc.nextInt()) {
            case 1:
                // ��չ��9���ӷ���������������������reload�������÷���showAllStudents�������С���ͬ��
                reload(list);
                showAllStudents(list);
                break;
            case 2:
                addStudent(sc, list);
                save(list);
                break;
            case 3:
                deleteStudent(sc, list);
                save(list);
                break;
            case 4:
                updateStudent(sc, list);
                save(list);
                break;
            case 5:
                break exit; // ��չ��24������ͨ����ǩ��д��ָ��break��һ�㡣
            default:
                System.out.println("������Ĺ��ܲ����ڣ������ԡ�");
                break;
            }
        }
        sc.close();
    }

    // ��չ��5�������ط����������ķ���ֻ�б��൱�вŻ���ã���������public������private��
    // ��չ��6�����һ����̬����ϣ��������һ����������ô�����õķ���Ҳ�����Ǿ�̬�ģ���Ϊ�����������⡣
    private static void printWelcome() {
        // ��չ��7��Ӧ���������ٵ��ñ�׼������Ĵ�����������ܣ����������Ĵ�ӡ�����Ӧ��ƴ�Ӻ�֮���ٴ�ӡ��
        // ��չ��8�������������Ҫһ���̰߳�ȫ���ַ�������������̫���ܣ����������StringBuffer��
        StringBuilder sb = new StringBuilder();
        sb.append("=========== ѧ������ϵͳ ===========\n");
        sb.append("1 - �鿴����ѧ��\n");
        sb.append("2 - ���ѧ��\n");
        sb.append("3 - ɾ��ѧ��\n");
        sb.append("4 - �޸�ѧ��\n");
        sb.append("5 - �˳�\n");
        sb.append("��ѡ��");
        System.out.print(sb);
    }

    private static void showAllStudents(List<Student> list) {
        if (list.size() == 0) {
            System.out.println("��ǰϵͳ�л�û��ѧ��������Ӻ��ٲ鿴��");
        } else {
            System.out.println("ѧ��\t����\t����\t���ڵ�");
            // ��չ��10������ò�������ֵ��Ӧ������ʹ��for-eachд����
            // ��չ��11�������JDK1.8������д��list.stream().forEach(Consumer<?> action)��
            for (Student stu : list) {
                // ��չ��12�����������ַ���ƴ�ӣ��������StringBuilder����String.format����Ҳ�С�
                // ��չ��20�������JDK1.8������ʹ��String��join�������á�
                // ��չ��13�������п���ʹ�ÿɱ����������������һ��������д���
                String line = String.format("%s\t%s\t%s\t%s", stu.getId(), stu.getName(), stu.getAge(),
                        stu.getAddress());
                System.out.println(line);
            }
        }
    }

    private static void addStudent(Scanner sc, List<Student> list) {
        int id = getInputIdAvailable(sc, list, RETRY);
        if (id == ILLEGAL) {
            return;
        }

        System.out.print("������������");
        String name = sc.next();
        System.out.print("���������䣺");
        int age = sc.nextInt();
        System.out.print("���������ڵأ�");
        String address = sc.next();

        Student stu = new Student(id, name, age, address);
        list.add(stu);
        System.out.println("ѧ����ӳɹ���");
    }

    private static void deleteStudent(Scanner sc, List<Student> list) throws IOException {
        int id = getInputIdExist(sc, list, RETRY);
        if (id == ILLEGAL) {
            return;
        }
        // ��չ��22��һ��Ҫ�����ڵ��������б䶯��������������������̽���������
        // ��չ��25��������ǲ����������Ӧ�ö�index�����ٴμ�顣
        list.remove(getIndex(list, id));
    }

    private static void updateStudent(Scanner sc, List<Student> list) throws IOException {
        int id = getInputIdExist(sc, list, RETRY);
        if (id == ILLEGAL) {
            return;
        }
        for (Student stu : list) {
            if (id == stu.getId()) {
                System.out.print("��������������");
                String name = sc.next();
                System.out.print("�����������䣺");
                String age = sc.next();
                System.out.print("�����������ڵأ�");
                String address = sc.next();
                stu.setName(name);
                stu.setAge(Integer.parseInt(age));
                stu.setAddress(address);
                System.out.println("ѧ��Ϊ" + id + "��ѧ���޸ĳɹ���");
                break;
            }
        }
    }

    private static void reload(List<Student> list) throws IOException {
        list.clear(); // ��չ��17������ʹ��clear�������������list.removeAll(list)��
        // ��չ��19������б�Ľ���Ҳ���޸��ļ����ݣ��������������أ���Ҫ��Ϊ�ڴ����ܶ�����ռ���ļ������
        BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
        String line;
        while ((line = reader.readLine()) != null) {
            Student stu = fromStringToStudent(line);
            list.add(stu);
        }
        reader.close();
    }

    private static void save(List<Student> list) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
        for (Student stu : list) {
            StringBuilder sb = new StringBuilder();
            sb.append(stu.getId()).append(";");
            sb.append(stu.getName()).append(";");
            sb.append(stu.getAge()).append(";");
            sb.append(stu.getAddress());
            writer.write(sb.toString());
            writer.newLine();
            // ��չ��21���������дflush������ͨ����Ӧ����ô�����������������½���
        }
        writer.close();
    }

    private static Student fromStringToStudent(String line) {
        String[] array = line.split(";");
        // ��չ��18����Ȼ���Զ�װ��/���䣬����Ӧ������ʹ��parseInt��valueOf������
        int id = Integer.parseInt(array[0]);
        String name = array[1];
        int age = Integer.parseInt(array[2]);
        String address = array[3];
        return new Student(id, name, age, address);
    }

    private static boolean isExist(int id, List<Student> list) {
        for (Student stu : list) {
            if (id == stu.getId()) {
                return true;
            }
        }
        return false;
    }

    // ��չ��14�������ظ����߼�������д�ظ����룬������װ��Ϊһ��������ε��á�
    // ��չ��15������һ�����Դ����������к�����������û�ϣ���������ԡ��û�ϣ�������ϼ��˵���
    private static int getInputIdAvailable(Scanner sc, List<Student> list, int retry) {
        return getInputId(sc, list, retry, false, "�������ѧ���Ѿ���ռ�á�");
    }

    private static int getInputIdExist(Scanner sc, List<Student> list, int retry) {
        return getInputId(sc, list, retry, true, "�������ѧ�Ų����ڡ�");
    }

    private static int getInputId(Scanner sc, List<Student> list, int retry, boolean shouldExist, String msg) {
        for (int i = 0; i < retry; i++) {
            System.out.print("������ѧ�ţ�");
            int id = sc.nextInt();
            if ((isExist(id, list) && shouldExist) || (!isExist(id, list) && !shouldExist)) {
                return id;
            } else {
                String tip = i == retry - 1 ? "�������˵���" : "�����ԡ�";
                System.out.println(msg + tip);
            }
        }
        return ILLEGAL;
    }

    private static int getIndex(List<Student> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i).getId()) {
                return i;
            }
        }
        return ILLEGAL;
    }

}

// ��չ��30����û�з������д���������õĶ���4���ո����1��Tab�������û�и�ʽ���Ļ�����