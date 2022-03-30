package com.github.cryboy007.stream;

/**
 * @ClassName StreamProducer
 * @Author tao.he
 * @Since 2022/3/30 10:24
 */

import com.github.cryboy007.model.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


/**
 * 消息生产者

 */
@Component
@RequiredArgsConstructor
@Slf4j
public class StreamProducer {
    private final RedisTemplate redisTemplate;

    public void sendRecord(String streamKey) {
        Book book = Book.create();
        log.info("产生一本书的信息:[{}]", book);

        ObjectRecord<String, Book> record = StreamRecords.newRecord()
                .in(streamKey)
                .ofObject(book)
                .withId(RecordId.autoGenerate());

        RecordId recordId = redisTemplate.opsForStream()
                .add(record);

        log.info("返回的record-id:[{}]", recordId);
    }

}
