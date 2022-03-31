/*
package com.github.cryboy007.utils.auto_wrapper;//package cn.xiaoke.damowang_boot_test.util.auto_wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TestMain {


    public static void main(String[] args) {
        final List<PackField> allFields = getFieldsListWithAnnotation(TestDto.class, Where.class, Wheres.class);
        System.out.println(allFields.stream().map(field -> field.field.getName()).collect(Collectors.toList()));
        final List<PackField> allFields2 = getFieldsListWithAnnotationLoop(TestDto.class, Where.class, Wheres.class);
        System.out.println(allFields2.stream().map(field -> field.field.getName()).collect(Collectors.toList()));

        final TestDto3 testDto3 = new TestDto().setPhone("17612312312").setValue("value").setNum(123321);

//        final TreeSet<WheresGroupInfo<LuckUser, SFunction<LuckUser, ?>>> set = GroupInfoBuilder.INSTANCE.buildGroupInfo(allFields, testDto3);
//        System.out.println(JSON.toJSONString(set));
//        set.forEach(System.out::println);
    }

    public static <T,R> LambdaQueryWrapper<T> createLambdaWrapper(Class<T> clazz, Object obj) {
        final LambdaQueryWrapper<T> lambdaQuery = Wrappers.lambdaQuery(clazz);
        List<PackField> fieldList2;
        final List<PackField> fieldList = getFieldsListWithAnnotation(obj.getClass(), Where.class, Wheres.class);

        final GroupInfoBuilder<T, R> groupInfoBuilder = new GroupInfoBuilder<>(clazz,null, AutoWhereUtil.columnProcessToSFunction);
        final TreeSet<WheresGroupInfo<T, R>> wheresGroupInfos = groupInfoBuilder.buildGroupInfo(obj);

        for (WheresGroupInfo wheresGroupInfo : wheresGroupInfos) {
            wheresGroupInfo.process(clazz, lambdaQuery);
        }

        return lambdaQuery;
    }


    public static Method findMethodByColumnName(Class<?> clazz, String columnName) {
        if (!StringUtils.isCamel(columnName)) {
            columnName = StringUtils.underlineToCamel(columnName);
        }
        final String methodName = StringUtils.concatCapitalize("get", columnName);
        final Method method = ReflectionUtils.findMethod(clazz, methodName);
        if (method == null) {
            throw new RuntimeException(clazz + "的" + methodName + "方法没有找到:");
        }
        return method;
    }

    public static List<PackField> getFieldsListWithAnnotationLoop(Class<?> clazz, Class<? extends Annotation>... annotations) {
        if (clazz == null) return new ArrayList<>();
        final List<Field> set = new ArrayList<>();
        while (clazz != null) {
            final Field[] declaredFields = clazz.getDeclaredFields();
            set.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass();
        }
        return set.stream()
                .map(fieldConvertPack(annotations)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static Function<Field, PackField> fieldConvertPack(Class<? extends Annotation>[] annotations) {
        return field -> {
            final Optional<Class<? extends Annotation>> first = Stream.of(annotations)
                    .filter(field::isAnnotationPresent).findFirst();
            return first.map(an -> {
                if (an == Wheres.class) {
                    return new WheresPackField(field);
                } else {
                    return new WherePackField(field);
                }
            }).orElse(null);
        };
    }

    public static List<PackField> getFieldsListWithAnnotation(Class<?> clazz, Class<? extends Annotation>... annotations) {
        return getFieldsListWithAnnotationTailRec(clazz, new ArrayList<>()).stream()
                .map(fieldConvertPack(annotations))
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    public static List<Field> getFieldsListWithAnnotationTailRec(Class<?> clazz, List<Field> list) {
        if (clazz == null) return list;
        final Field[] declaredFields = clazz.getDeclaredFields();
        list.addAll(Arrays.asList(declaredFields));
        return getFieldsListWithAnnotationTailRec(clazz.getSuperclass(), list);
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Accessors(chain = true)
    public static class TestDto extends TestDto3 {
        @Where
        private String phone;


        private Integer current;
        private Integer pageSize;

        public String AAA;
    }

    @Data
    @Accessors(chain = true)
    public static class TestDto3 extends AAA {
        @Where
        public String BBB;
        protected String BBBB;
        @Where
        @Where
        private String value;

        private Integer index;

        @Wheres
        private Integer num;

        private List<Integer> vips;

        private LocalDateTime createStartTime;

        private LocalDateTime createEndTime;

    }

    public static class AAA {
        private String asd;
        public String CCC;
        @Where
        public String BBB;
    }


}
*/
