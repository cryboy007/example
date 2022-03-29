package com.github.cryboy007.utils;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * <p>
 * 公用转换DTO
 * </p>
 *
 * @author lei.mao
 * @since 2021/1/1
 */
@ToString
public class CommonConvertUtil {

    public static <S, T> T convertTo(S s, Class<T> clazz, BiConsumer<S, T> consumer) {
        if (Objects.isNull(s)) {
            return null;
        }

        T t = null;
        try {
            t = clazz.getConstructor().newInstance();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(s, t);

        T finalT = t;
        Optional.ofNullable(consumer).ifPresent(stBiConsumer -> stBiConsumer.accept(s, finalT));
        return t;

    }

    public static <S, T> List<T> convertToList(Collection<S> sList, Class<T> clazz, BiConsumer<S, T> consumer) {
        if (CollectionUtils.isEmpty(sList)) {
            return Collections.emptyList();
        }

        List<T> tList;
        if (sList instanceof Page) {
            Page<S> sPage = (Page<S>) sList;
            Page<T> tPage = new Page<>();
            tPage.setPageNum(sPage.getPageNum());
            tPage.setPageSize(sPage.getPageSize());
            tPage.setPages(sPage.getPages());
            tPage.setTotal(sPage.getTotal());
            tList = tPage;
        } else {
            tList = Lists.newArrayList();
        }
        sList.forEach(e -> tList.add(convertTo(e, clazz, consumer)));
        return tList;
    }

    public static <S, T> T convertTo(S s, Class<T> clazz) {
        return convertTo(s, clazz, null);
    }

    public static <S, T> List<T> convertToList(Collection<S> sList, Class<T> clazz) {
        return convertToList(sList, clazz, null);
    }

    public static <S, T> PageInfo<T> convertToPageInfo(List<S> sList, Class<T> clazz) {
        return PageInfo.of(convertToList(sList, clazz));
    }


}