package com.dailycodebuffer.springbootdemo.service;

import com.dailycodebuffer.springbootdemo.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee save(Employee emp);
    List<Employee> getAllEmployees();
    Employee getEmployeeById(String id);
    String delEmpById(String id);
}
