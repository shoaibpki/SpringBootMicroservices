package com.dailycodebuffer.springbootdemo.service;

import com.dailycodebuffer.springbootdemo.entity.EmployeeEntity;
import com.dailycodebuffer.springbootdemo.model.Employee;
import com.dailycodebuffer.springbootdemo.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeV2ServiceImpl implements EmployeeService{

    @Autowired
    EmployeeRepository employeeRepository;
    @Override
    public Employee save(Employee emp) {
        if (emp.getId() == null ||
                emp.getId().isEmpty()){
            emp.setId(UUID.randomUUID().toString());
        }

        EmployeeEntity entity = new EmployeeEntity();
        BeanUtils.copyProperties(emp, entity);
        employeeRepository.save(entity);

        return emp;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<EmployeeEntity> employeeEntityList = employeeRepository.findAll();
        List<Employee> employees = employeeEntityList
                .stream()
                .map(employeeEntity -> {
                    Employee employee = new Employee();
                    BeanUtils.copyProperties(employeeEntity,employee);
                    return employee;
                })
                .collect(Collectors.toList());
        return employees;
    }

    @Override
    public Employee getEmployeeById(String id) {
        EmployeeEntity employee = employeeRepository.findById(id).get();

        Employee emp = new Employee();
        BeanUtils.copyProperties(employee, emp);
        return emp;
    }

    @Override
    public String delEmpById(String id) {
        employeeRepository.deleteById(id);
        return "Employee successfully removed! ";
    }
}
