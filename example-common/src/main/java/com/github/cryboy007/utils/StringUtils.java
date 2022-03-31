package com.github.cryboy007.utils;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * @ClassName StringUtils
 * @Author tao.he
 * @Since 2022/3/31 14:24
 */
public class StringUtils {
    /**
     * 判断字符串是不是驼峰命名
     *
     * <li> 包含 '_' 不算 </li>
     * <li> 首字母大写的不算 </li>
     *
     * @param str 字符串
     * @return 结果
     */
    public static boolean isCamel(String str) {
        return Character.isLowerCase(str.charAt(0)) && !str.contains(StringPool.UNDERSCORE);
    }

}
