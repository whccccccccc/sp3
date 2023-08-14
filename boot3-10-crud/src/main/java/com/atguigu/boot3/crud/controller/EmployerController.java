package com.atguigu.boot3.crud.controller;

import com.atguigu.boot3.crud.bean.Employer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/emp")
@Slf4j
@Tag(name = "员工管理")
public class EmployerController {


    @PostMapping
    @Operation(summary = "添加员工")
    public String add(@RequestBody Employer employer) {
        log.info("employer: {}", employer);
        return "add";
    }

    @PutMapping
    @Operation(summary = "修改员工")
    public String edit(@RequestBody Employer employer) {
        log.info("employer: {}", employer);
        return "edit";
    }

    @GetMapping
    @Operation(summary = "查询员工")
    public String get(@RequestParam(required = false) Long id) {
        log.info("employerId: {}", id);
        return "get";
    }

    @DeleteMapping
    @Operation(summary = "删除员工")
    public String delete(@RequestParam(required = false) Long id) {
        log.info("employerId: {}", id);
        return "delete";
    }
}
