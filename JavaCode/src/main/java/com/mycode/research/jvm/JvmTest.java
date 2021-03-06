package com.mycode.research.jvm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author jiangzhen
 * @date 2019/5/27 20:43
 */
public class JvmTest {
    public static void main(String[] args)
            throws IOException, ClassNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Student stu = new Student("chuangwang", 25, "WuDa");
        Programer programer = new Programer("jiangzhen", 25, "loho");

        boolean isFinish = false;
        while (!isFinish) {
            String line = br.readLine();

            test(1,22,33);

            try {
                if (line.equals("load")) {
                } else {
                    System.out.println("Input : " + line);
                    if (TestMethod.test(line)) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void test(int x, int y, int z) {
        System.out.println(Integer.valueOf(x));
    }
}
