package com.epam.rd.autotasks.springemployeecatalog.service;


import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")

public class EmployeeController {

    @Autowired
    private  EmployeeService employeeService;


    @GetMapping
    public Page<Employee> getAllEmployees( @RequestParam(defaultValue = "0") int page,
                                           @RequestParam(defaultValue = "10") int size,
                                           @RequestParam(defaultValue = "lastName")
                                           String sort) {
          return employeeService.getAll(page , size  , Sort.by(sort));
    }

    @GetMapping("{employee_id}")
    public Employee getEmployee(@PathVariable String employee_id,
                                @RequestParam(required = false , defaultValue = "false") boolean full_chain) {
        return employeeService.get(employee_id , full_chain);
    }

    @GetMapping("by_manager/{managerId}")
    public Page<Employee> getEmployeesByManagerId(@PathVariable String managerId ,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "lastName") String sort) {
        return employeeService.getByManagerId(managerId , page , size , Sort.by(sort));
    }

    @GetMapping("/by_department")
    public Page<Employee> getEmployeesByDepartment(
            @PathVariable String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sort
    ) {

        return employeeService.getEmployeesByDepartment(department, page ,size , sort);
    }

}
