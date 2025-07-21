package com.itheima.controller;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import com.itheima.pojo.PageResult;
import com.itheima.pojo.Result;
import com.itheima.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/emps")
public class EmpController {

    @Autowired
    private EmpService empService;

    /**
     * 分页查询员工信息
     * */
    @GetMapping("/list")
    public Result page(EmpQueryParam empQueryParam) {
        log.info("分页查询员工信息,参数:{}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增员工信息
     * */
    @PostMapping("/add")
    public Result save(@RequestBody Emp emp) {
        log.info("新增员工信息,参数:{}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 删除员工信息
     * */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除员工信息,参数:{}", ids);
        empService.delete(ids);
        return Result.success();
    }

    /**
     * 根据ID查询员工信息
     * */
    @GetMapping("/info/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据ID查询员工信息,参数:{}", id);
         Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    /**
     * 修改员工信息
     * */
    @PutMapping("/edit")
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工信息,参数:{}", emp);
        empService.update(emp);
        return Result.success();
    }
}
