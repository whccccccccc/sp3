package com.atguigu.boot3.crud.bean;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "Department", description = "部门")
public class Department {

    private Long id;
    @Schema(description = "部门名称")
    private String name;
}
