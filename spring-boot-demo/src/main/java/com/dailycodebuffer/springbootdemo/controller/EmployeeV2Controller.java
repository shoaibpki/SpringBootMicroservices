package com.dailycodebuffer.springbootdemo.controller;

import com.dailycodebuffer.springbootdemo.model.Employee;
import com.dailycodebuffer.springbootdemo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/employees")
public class EmployeeV2Controller {

    @Qualifier(value = "employeeV2ServiceImpl")
    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public Employee save(@RequestBody Employee emp){
        return employeeService.save(emp);
    }
    @GetMapping
    public List<Employee> getAllEmployees(){
        return  employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable String id){
        return employeeService.getEmployeeById(id);
    }

    @DeleteMapping("/{id}")
    public String delEmpById(@PathVariable String id){
        return employeeService.delEmpById(id);
    }
}

