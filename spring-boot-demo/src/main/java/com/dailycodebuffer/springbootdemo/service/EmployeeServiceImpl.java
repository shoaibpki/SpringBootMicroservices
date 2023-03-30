package com.dailycodebuffer.springbootdemo.service;

import com.dailycodebuffer.springbootdemo.errors.EmployeeNotFoundException;
import com.dailycodebuffer.springbootdemo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    List<Employee> employees = new ArrayList<>();
    @Override
    public Employee save(Employee emp) {
        if (emp.getId() == null ||
            emp.getId().isEmpty()){
            emp.setId(UUID.randomUUID().toString());
        }
        employees.add(emp);
        return emp;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employees;
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employees
                .stream()
                .filter(employee -> employee.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(""+
                        "Employee Not Found with Id: "+id));
    }

    @Override
    public String delEmpById(String id) {
        Employee employee =
                employees.stream()
                        .findFirst()
                        .filter(e -> e.getId().equalsIgnoreCase(id))
                        .orElseThrow(() ->
                                new EmployeeNotFoundException("Employee not found with id: "+ id));
        employees.remove(employee);
        return "Employee successfully removed! ";
    }
}
