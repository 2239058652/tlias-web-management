package com.itheima.controller;

import com.itheima.anno.ALog;
import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/depts")
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 查询所有部门
     */
    // @RequestMapping(value = "/depots", method = RequestMethod.GET)  // method请求方式
    // @CrossOrigin(origins = "*")  // 允许跨域
    @ALog
    @GetMapping("/list")
    public Result list() {
        System.out.println("查询所有部门");
        List<Dept> deptList = deptService.findAll();
        log.info("查询所有部门{}",deptList);
        return Result.success(deptList);
    }

    /**
     * 删除部门
     */
    @ALog
    @DeleteMapping("/delete")   // @RequestParam(value = "id", required = false) 可以省略，参数名称一致时可以省略
    public Result delete(@RequestParam(value = "id", required = false) Integer id) {
        System.out.println("根据部门删除部门id:" + id);
        deptService.deleteById(id);
        return Result.success();
    }

    /**
     * 新增部门
     * */
    @PostMapping("/add")
    public Result addDept(@RequestBody Dept dept) {
        System.out.println("新增部门" + dept);
        deptService.addDept(dept);
        return Result.success();
    }

    /**
     * 根据id查询部门
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        System.out.println("根据id查询部门id:" + id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     */
    @PutMapping("/update")
    public Result updateDept(@RequestBody Dept dept) {
        System.out.println("修改部门" + dept);
        deptService.updateDept(dept);
        return Result.success();
    }
}


