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
     * 扩展点4：为了避免程序内的线程安全性问题，除非必要，否则不要使用静态类属性。
     * 但事实不可变的量一定是线程安全的。另外，方法具有重入性，所以局部变量线程安全。
     */
    // 扩展点23：避免特殊含义的常量散落在代码各处，统一定义。
    private static final int ILLEGAL = -1;
    private static final String FILE_NAME = "data.txt";
    private static final int RETRY = 3;

    public static void main(String[] args) {
        // 扩展点26：除非真的放弃，否则避免main方法的不正常退出，异常应当处理。
        try {
            doManage();
        } catch (FileNotFoundException e) {
            // 扩展点27：错误信息可以使用syse进行输出。
            System.err.println(String.format("找不到文件[%s]，请手动新建它。", FILE_NAME));
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            // 扩展点28：从JDK1.7开始，多种异常可以使用竖线分隔后同时处理
            System.err.println("发生了数组越界或空指针异常，课上讲过的，请检查代码。");
            e.printStackTrace();
        } catch (Throwable other) {
            System.err.println("发生了未知错误：" + other.getMessage());
            other.printStackTrace();
        }
        System.out.println("程序结束！");
    }

    private static void doManage() throws IOException {
        // 扩展点2：Scanner创建一个就够了，通过传参使用它，不要创建多个，除非要扫描不同来源。
        Scanner sc = new Scanner(System.in);
        // 扩展点3：从JDK1.7开始，右侧的泛型可以推导，所以可以省略。
        // 扩展点16：依赖倒置和接口隔离原则，尽可能使用抽象接口而非具体实现类：List。
        List<Student> list = new ArrayList<>();
        exit: while (true) {
            printWelcome();
            switch (sc.nextInt()) {
            case 1:
                // 扩展点9：从方法命名的语义上来讲，reload方法不该放在showAllStudents方法当中。下同。
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
                break exit; // 扩展点24：可以通过标签的写法指定break哪一层。
            default:
                System.out.println("您输入的功能不存在，请重试。");
                break;
            }
        }
        sc.close();
    }

    // 扩展点5：迪米特法则，如果定义的方法只有本类当中才会调用，尽量别用public，而用private。
    // 扩展点6：如果一个静态方法希望调用另一个方法，那么被调用的方法也必须是静态的，因为生命周期问题。
    private static void printWelcome() {
        // 扩展点7：应当尽量减少调用标准输出流的次数以提高性能，所以连续的打印输出，应当拼接好之后再打印。
        // 扩展点8：除非你真的需要一个线程安全的字符串构造器（不太可能），否则别用StringBuffer。
        StringBuilder sb = new StringBuilder();
        sb.append("=========== 学生管理系统 ===========\n");
        sb.append("1 - 查看所有学生\n");
        sb.append("2 - 添加学生\n");
        sb.append("3 - 删除学生\n");
        sb.append("4 - 修改学生\n");
        sb.append("5 - 退出\n");
        sb.append("请选择：");
        System.out.print(sb);
    }

    private static void showAllStudents(List<Student> list) {
        if (list.size() == 0) {
            System.out.println("当前系统中还没有学生，请添加后再查看。");
        } else {
            System.out.println("学号\t姓名\t年龄\t所在地");
            // 扩展点10：如果用不到索引值，应该优先使用for-each写法。
            // 扩展点11：如果是JDK1.8，可以写：list.stream().forEach(Consumer<?> action)。
            for (Student stu : list) {
                // 扩展点12：尽量避免字符串拼接，如果不用StringBuilder，用String.format方法也行。
                // 扩展点20：如果是JDK1.8，可以使用String的join方法更好。
                // 扩展点13：方法中可以使用可变参数，它将被当做一个数组进行处理。
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

        System.out.print("请输入姓名：");
        String name = sc.next();
        System.out.print("请输入年龄：");
        int age = sc.nextInt();
        System.out.print("请输入所在地：");
        String address = sc.next();

        Student stu = new Student(id, name, age, address);
        list.add(stu);
        System.out.println("学生添加成功！");
    }

    private static void deleteStudent(Scanner sc, List<Student> list) throws IOException {
        int id = getInputIdExist(sc, list, RETRY);
        if (id == ILLEGAL) {
            return;
        }
        // 扩展点22：一定要避免在迭代过程中变动索引或迭代器，除非立刻结束迭代。
        // 扩展点25：如果考虑并发的情况，应该对index进行再次检查。
        list.remove(getIndex(list, id));
    }

    private static void updateStudent(Scanner sc, List<Student> list) throws IOException {
        int id = getInputIdExist(sc, list, RETRY);
        if (id == ILLEGAL) {
            return;
        }
        for (Student stu : list) {
            if (id == stu.getId()) {
                System.out.print("请输入新姓名：");
                String name = sc.next();
                System.out.print("请输入新年龄：");
                String age = sc.next();
                System.out.print("请输入新所在地：");
                String address = sc.next();
                stu.setName(name);
                stu.setAge(Integer.parseInt(age));
                stu.setAddress(address);
                System.out.println("学号为" + id + "的学生修改成功！");
                break;
            }
        }
    }

    private static void reload(List<Student> list) throws IOException {
        list.clear(); // 扩展点17：尽量使用clear清空自身，而别用list.removeAll(list)。
        // 扩展点19：如果有别的进程也会修改文件内容，则这个流随用随关，不要因为内存性能而长期占用文件句柄。
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
            // 扩展点21：这里可以写flush，但是通常不应该这么做，否则性能严重下降。
        }
        writer.close();
    }

    private static Student fromStringToStudent(String line) {
        String[] array = line.split(";");
        // 扩展点18：虽然有自动装箱/拆箱，还是应该区分使用parseInt和valueOf方法。
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

    // 扩展点14：对于重复的逻辑，避免写重复代码，将它封装成为一个方法多次调用。
    // 扩展点15：设置一个重试次数，可以中和两种情况：用户希望立刻重试、用户希望返回上级菜单。
    private static int getInputIdAvailable(Scanner sc, List<Student> list, int retry) {
        return getInputId(sc, list, retry, false, "您输入的学号已经被占用。");
    }

    private static int getInputIdExist(Scanner sc, List<Student> list, int retry) {
        return getInputId(sc, list, retry, true, "您输入的学号不存在。");
    }

    private static int getInputId(Scanner sc, List<Student> list, int retry, boolean shouldExist, String msg) {
        for (int i = 0; i < retry; i++) {
            System.out.print("请输入学号：");
            int id = sc.nextInt();
            if ((isExist(id, list) && shouldExist) || (!isExist(id, list) && !shouldExist)) {
                return id;
            } else {
                String tip = i == retry - 1 ? "返回主菜单。" : "请重试。";
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

// 扩展点30：有没有发现所有代码的缩进用的都是4个空格而非1个Tab（如果你没有格式化的话）？