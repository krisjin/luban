package org.bscl.common.collection;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 列表数据工具类
 */
public final class ListUtil {
    private ListUtil() {

    }

    /**
     * 转换两个列表的数据,封装guava List.transform 实现
     * <p/>
     * 要求所有代码中必须使用该方法进行处理，不可直接使用List.transform
     * <p/>
     * List.transform并不直接生成转换后的数据，而是在实际get数据时候进行转换
     * 这里将转换完成，封装到新的List中
     * <p/>
     *
     * @param fromList
     * @param function
     * @param <F>
     * @param <T>
     * @return
     */
    public static <F, T> List<T> transform(List<F> fromList, Function<? super F, ? extends T> function) {
        return Lists.newArrayList(Lists.transform(fromList, function));
    }

    /**
     * 遍历迭代对象，使每个迭代的元素被指定Function调用
     * <p/>
     * 该方法不关心传入Function的返回值，因此Function的返回值是{@link Void}
     * <p/>
     * 对于关心返回值的需求，请使用{@link ListUtil#transform(List, com.google.common.base.Function)}
     *
     * @param iterable
     * @param function
     * @param <F>
     */
    public static <F> void iterateViaFunc(final Iterable<F> iterable, final Function<? super F, Void> function) {
        checkNotNull(iterable);
        Iterator<F> iterator = iterable.iterator();
        checkNotNull(iterator);
        checkNotNull(function);
        while (iterator.hasNext()) {
            function.apply(iterator.next());
        }
    }


}
