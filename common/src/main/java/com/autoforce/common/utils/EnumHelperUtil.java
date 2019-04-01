package com.autoforce.common.utils;

/**
 * Created by xlh on 2019/1/29.
 */
public class EnumHelperUtil {

    /**
     * indexOf,传入的参数ordinal指的是需要的枚举值在设定的枚举类中的顺序，以0开始
     * T
     *
     * @param clazz   枚举类型的class对象
     * @param ordinal 下标 从0开始
     * @return
     */
    public static <T extends Enum<T>> T indexOf(Class<T> clazz, int ordinal) {
        return (T) clazz.getEnumConstants()[ordinal];
    }

}
