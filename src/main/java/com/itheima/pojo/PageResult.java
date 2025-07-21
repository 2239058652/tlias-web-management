package com.itheima.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 86135
 * 分页结果封装类
 * date 2023/10/16 14:57
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total;  // 总记录数
    private List<T> rows;  //结果列表
}
