package net.common.utils.enums;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 从0开始的可索引枚举接口
 * <p/>
 * 实现此接口的枚举各元素的index可以不连续<br/>
 * <b>建议:</b>
 * 此接口实现类的元素保持index的连续以节省空间
 * <p/>
 * User : krisibm@163.com
 * Date: 2015/5/27
 * Time: 8:46
 */
public interface IndexedEnum {
    /**
     * 取得该枚举元素的索引值
     *
     * @return 返回>=0的索引值
     */
    public abstract int getIndex();

    /**
     * 支持可索引枚举的工具类
     */
    static final class Util {

        private Util() {
        }

        private static final Logger log = LoggerFactory.getLogger(Util.class);

        /**
         * 索引预警上限值，索引超过该上限可能存在空间浪费问题
         */
        private static final int WARNNING_MAX_INDEX = 1000;

        /**
         * 将枚举个元素放置ArrayList中，各元素在列表中的序列即为原则的index<br/>
         * 如果有不连续的index，则列表中空缺的位置由null填充
         *
         * @param enums 枚举元素数组
         * @param <E>   枚举泛型
         * @return 包含所有数组元素的List
         */
        public static <E extends IndexedEnum> List<E> toIndexes(E[] enums) {
            Preconditions.checkState((enums != null && enums.length > 0),
                    "The Array of Enum[%s] cannot be null or empty.", enums.getClass().getComponentType().getName());

            int maxIndex = Integer.MIN_VALUE;
            int curIdx = 0;
            // 取得最大index的值，该值+1的结果就是目标容器的size
            for (E enm : enums) {
                curIdx = enm.getIndex();
                Preconditions.checkArgument(curIdx >= 0, "The index of Enum[%s] must be >= 0.", enm);
                if (curIdx > maxIndex) {
                    maxIndex = curIdx;
                }
            }

            if (maxIndex >= WARNNING_MAX_INDEX) {
                log.warn("The index of Enum[{}] exceed threshold:{}.There is wasting memory probably.",
                        enums.getClass().getComponentType().getName(), WARNNING_MAX_INDEX);
            }

            List<E> instances = new ArrayList<E>(maxIndex + 1);
            // 容器暂用null填充
            for (int i = 0; i < maxIndex + 1; i++) {
                instances.add(null);
            }
            for (E enm : enums) {
                curIdx = enm.getIndex();
                // 索引必须唯一
                Preconditions.checkState(instances.get(curIdx) == null,
                        "The index of Enum[%s] is not unique.", enums.getClass().getComponentType().getName());
                instances.set(curIdx, enm);
            }
            return instances;
        }

        /**
         * 根据指定序列从枚举列表中取得相应的枚举元素
         * <p/>
         * <b>注意：如果index参数小于0或者大于枚举元素最大索引值，返回null</b>
         *
         * @param values 枚举列表
         * @param index  目标序列
         * @param <E>    枚举泛型
         * @return
         */
        public static <E extends IndexedEnum> E valueOf(List<E> values, int index) {
            if (index < 0 || index >= values.size()) {
                return null;
            }
            return values.get(index);
        }
    }

}
