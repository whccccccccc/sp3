package com.atguigu.boot3.crud.controller;

import com.atguigu.boot3.crud.bean.Department;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dept")
@Slf4j
@Tag(name = "部门管理")
public class DepartmentController {

    @PostMapping
    @Operation(summary = "添加部门")
    public String add(@RequestBody Department department) {
        log.info("department: {}", department);
        return "add";
    }

    @PutMapping
    @Operation(summary = "修改部门")
    public String edit(@RequestBody Department department) {
        log.info("department: {}", department);
        return "edit";
    }

    @GetMapping
    @Operation(summary = "查询部门")
    public String get(@RequestParam(required = false) Long id) {
        log.info("departmentId: {}", id);
        return "get";
    }

    @DeleteMapping
    @Operation(summary = "删除部门")
    public String delete(@RequestParam(required = false) Long id) {
        log.info("departmentId: {}", id);
        return "delete";
    }
}
