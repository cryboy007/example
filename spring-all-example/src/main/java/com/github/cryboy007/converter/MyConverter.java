package com.github.cryboy007.converter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.github.cryboy007.model.Book;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;


/**
 * @ClassName MyConverter
 * @Author tao.he
 * @Since 2022/4/18 16:49
 */
@Slf4j
public class MyConverter extends AbstractHttpMessageConverter<Book> {

    public MyConverter() {
        //x-zyf 是自定义的媒体类型
        //super(new MediaType("application", "ht", StandardCharsets.UTF_8));
        super(MediaType.APPLICATION_JSON);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        //表示只支持DemoObj这个类
        //return DemoObj.class.isAssignableFrom(Book);
        //返回false则不会支持任何类，要想使用，就需要返回true
        return true;
    }

    @Override
    protected Book readInternal(Class<? extends Book> clazz, HttpInputMessage httpInputMessage) throws IOException, HttpMessageNotReadableException {
        //获得请求中的数据，得到字符串形式
        String temp = StreamUtils.copyToString(httpInputMessage.getBody(), StandardCharsets.UTF_8);

        //前端请求的格式是我们自己约定的
        String[] tempArr = temp.split("-");
        log.info("自定义Converter解析数据:{}",tempArr);
        return new Book();
    }

    @Override
    protected void writeInternal(Book book, HttpOutputMessage httpOutputMessage) throws IOException, HttpMessageNotWritableException {
        String out = "hello：" + book.getId() + "-" + book.getAuthor();
        httpOutputMessage.getBody().write(out.getBytes());
    }


//    @Configuration(
//            proxyBeanMethods = false
//    )
//    @ConditionalOnClass({ObjectMapper.class})
//    @ConditionalOnBean({ObjectMapper.class})
//    @ConditionalOnProperty(
//            name = {"spring.mvc.converters.preferred-json-mapper"},
//            havingValue = "ht-converters",
//            matchIfMissing = true
//    )
//    static class HtHttpMessageConverterConfiguration {
//        HtHttpMessageConverterConfiguration() {
//        }
//
//        @Bean
//        @ConditionalOnMissingBean(
//                value = {MappingJackson2HttpMessageConverter.class},
//                ignoredType = {"org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter", "org.springframework.data.rest.webmvc.alps.AlpsJsonHttpMessageConverter"}
//        )
//        MyConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
//           return new MyConverter();
//        }
//    }
}
