package com.tao.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MessageTo
 * @Author tao.he
 * @Since 2022/1/20 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageTo {
    private String message;

    private Long userId;

}
