package com.github.cryboy007.cache.inteceptor;

/**
 * @ClassName NoArgsWhereHelper
 * @Author tao.he
 * @Since 2022/4/6 14:33
 */
public class NoArgsWhereHelper {
    protected static final ThreadLocal<Boolean> LOCAL = ThreadLocal.withInitial(() -> false);

    protected static void setLocal() {
        LOCAL.set(true);
    }

    public static boolean getLocal() {
        return LOCAL == null ? false : LOCAL.get();
    }

    public static void clear() {
        LOCAL.remove();
    }

    public static void setNoArgsWhere() {
        setLocal();
    }

    private NoArgsWhereHelper() {
        throw new IllegalStateException("Utility class");
    }
}
