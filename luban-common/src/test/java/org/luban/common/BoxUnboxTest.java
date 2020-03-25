package org.luban.common;

/**
 * 谈谈Integer i = new Integer(xxx)和Integer i =xxx;这两种方式的区别。
 * 当然，这个题目属于比较宽泛类型的。但是要点一定要答上，我总结一下主要有以下这两点区别：
 * 1）第一种方式不会触发自动装箱的过程；而第二种方式会触发；
 * 2）在执行效率和资源占用上的区别。第二种方式的执行效率和资源占用在一般性情况下要优于第一种情况（注意这并不是绝对的）。
 * User: krisjin
 * Date: 2016/10/8
 */
public class BoxUnboxTest {
    public static void main(String[] args) {
        Integer i1 = 100;
        Integer i2 = 100;
        Integer i3 = 129;
        Integer i4 = 129;

        System.out.println(i1 == i2);
        System.out.println(i3 == i4);

        Double d1 = 1.0;
        Double d2 = 1.0;
        Double d3 = 100.0;
        Double d4 = 100.0;
        System.out.println(d1 == d2);
        System.out.println(d3 == d4);


        Boolean b1 = false;
        Boolean b2 = false;
        Boolean b3 = true;
        Boolean b4 = true;
        System.out.println(b1 == b2);
        System.out.println(b3 == b4);

        System.out.println("-----------------------------------------------------");
        test2();
    }

    /**
     * 当 “==” 运算符的两个操作数都是 包装器类型的引用，则是比较指向的是否是同一个对象，
     * 而如果其中有一个操作数是表达式（即包含算术运算）则比较的是数值（即会触发自动拆箱的过程）。
     * 另外，对于包装器类型，equals方法并不会进行类型转换。明白了这2点之后，上面的输出结果便一目了然：
     */
    public static void test2() {
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Long g = 3L;
        Long h = 2L;

        System.out.println(c == d);
        System.out.println(e == f);
        System.out.println(c == (a + b));
        System.out.println(c.equals(a + b));
        System.out.println(g == (a + b));
        System.out.println(g.equals(a + b));
        System.out.println(g.equals(a + h));
    }
}

