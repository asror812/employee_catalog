package com.epam.rd.autotasks.springemployeecatalog.service;

import com.epam.rd.autotasks.springemployeecatalog.domain.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Service
public class EmployeeService {

    @Autowired
    private EntityManager entityManager;


    @Transactional
    public Page<Employee> getAll(int page , int size , Sort sort) {

        Pageable pageable = PageRequest.of(page, size , sort);

        String p = "select e from Employee e order by e."  + sort ;
        TypedQuery<Employee> query = entityManager.createQuery(p, Employee.class);

        query.setFirstResult(page * size);
        query.setMaxResults(size);


        String count = "SELECT COUNT(e) FROm Employee e" ;
        int countOfEmployees =  entityManager.createQuery(count , Employee.class).getResultList().size();
        List<Employee> employees = query.getResultList();

        return new PageImpl<>(employees, pageable, countOfEmployees);
    }


    public Employee get(String employeeId , boolean fullChain) {

        String p = "select e from Employee e where e.employeeId = :employeeId ";

        Employee employee = entityManager.createQuery(p, Employee.class)
                .setParameter("employeeId", employeeId)
                .getSingleResult();

        if(fullChain) {
            return  employeeWithFullChain(employee);
        }

         return employee;
    }

    public Page<Employee> getByManagerId(String managerId , int page , int size , Sort sort) {

        Pageable pageable = PageRequest.of(page , size , sort);
        String p = "select e from Employee e where e.managerId = :managerId ORDER BY e."  + sort ;
        TypedQuery<Employee> query = entityManager.createQuery(p, Employee.class);

        query.setParameter("managerId", managerId);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        List<Employee> employees = query.getResultList();


        String count = "SELECT COUNT(e) FROm Employee e" ;
        int countOfEmployees =  entityManager.createQuery(count , Employee.class).getResultList().size();

        return new PageImpl<>(employees, pageable, countOfEmployees);
    }

    public Employee employeeWithFullChain(Employee employee) {
        Employee currentManager = employee.getManager();
        while (currentManager != null) {
            currentManager = currentManager.getManager();
        }
        return employee;
    }

    public Page<Employee> getEmployeesByDepartment(Long departmentId, String departmentName, int page, int size, String sort) {

        Pageable pageable = PageRequest.of(page , size , Sort.by(sort));

        int countOfEmployees = 0;
        TypedQuery<Employee> query = null;
        if (departmentId != null) {
            String p = "select e from Employee e where e.departmentId = :departmentId ";
            query = entityManager.createQuery(p, Employee.class);
            query.setParameter("departmentId", departmentId);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            String count = "SELECT COUNT(e) FROM Employee e";
            countOfEmployees = entityManager.createQuery(count, Employee.class).getResultList().size();

        }
        else {
            String p = "select e from Employee e where e.departmentName = :departmentName";
            query = entityManager.createQuery(p, Employee.class);
            query.setParameter("departmentName", departmentName);
            query.setFirstResult(page * size);
            query.setMaxResults(size);
            String count = "SELECT COUNT(e) FROM Employee e";
            countOfEmployees = entityManager.createQuery(count, Employee.class).getResultList().size();
        }

        return new PageImpl<>(query.getResultList(), pageable, countOfEmployees);
    }
}
