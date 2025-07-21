package com.itheima.mapper;

import com.itheima.pojo.Emp;
import com.itheima.pojo.EmpQueryParam;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
    // -------------------原始分页查询原理及实现-------------------
    //    /**
    //     *查询总记录数
    //     */
    //    @Select("select count(*) from emp e left join dept d on e.dept_id = d.id")
    //    public Long count();
    //
    //    /**
    //     * 查询分页数据
    //     */
    //    @Select("select e.*, d.name deptName FROM emp e LEFT JOIN dept d on e.dept_id = d.id " +
    //            "order by e.update_time desc LIMIT #{start},#{pageSize}")
    //    public List<Emp> list(Integer start, Integer pageSize);

    // -------------------使用PageHelper插件实现分页查询-------------------
    //    select e.*, d.name deptName FROM emp e LEFT JOIN dept d on e.dept_id = d.id
    //    where e.name like '%李%' and e.gender = 1 and e.entry_date between '2010-01-01' and '2020-01-01'
    //    order by e.update_time desc LIMIT 1,5;
    //    @Select("select e.*, d.name deptName FROM emp e LEFT JOIN dept d on e.dept_id = d.id order by e.update_time desc")
    public List<Emp> list(EmpQueryParam empQueryParam);


    // -------------------新增员工信息-------------------
    @Options(useGeneratedKeys = true, keyProperty = "id")  // 开启主键回填
    @Insert("insert into emp(username, password, name, gender, phone, job, salary, image, entry_date, dept_id, create_time, update_time) " +
            "values(#{username}, #{password}, #{name}, #{gender}, #{phone}, #{job}, #{salary}, #{image}, #{entryDate}, #{deptId}, #{createTime}, #{updateTime})")
    void insert(Emp emp);

    // -------------------批量删除员工信息-------------------
    void deleteByIds(List<Integer> ids);

    // -------------------根据id查询员工信息 以及工作经理信息-------------------
    Emp getInfoById(Integer id);

    void updateById(Emp emp);

    @MapKey("pos") // 指定Map的key 消除下方的警告
    List<Map<String, Object>> countEmpJobData();

    /**
     * 统计员工性别
     * */
    @MapKey("name") // 指定Map的key 消除下方的警告
    List<Map<String, Object>> countEmpGenderData();

    /**
     * 根据用户名和密码查询员工信息
     * */
    @Select("select id, username, name from emp where username = #{username} and password = #{password}")
    Emp selectByUsernameAndPassword(Emp emp);
}
