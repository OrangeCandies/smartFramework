package org.smart.service;

import org.smart.framework.annocation.Service;
import org.smart.framework.helper.DatabaseHelper;
import org.smart.model.Customer;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {


    public List<Customer> getCustomerList(){
        List<Customer> customers = null;
        String sql = "SELECT * FROM customer";
        customers = DatabaseHelper.queryEntityList(Customer.class,sql);
        return  customers;
    }

    public Customer getCustomer(long id){
        String sql = "SELECT * FROM customer WHERE id = ?";
        Customer customer = DatabaseHelper.queryEntity(Customer.class,sql,id);
        return customer;
    }

    public boolean createCustomer(Map<String,Object> feildMap) {
        return DatabaseHelper.insertEntity(Customer.class,feildMap);
    }

    public boolean updateCustomer(long id,Map<String,Object> feildMap) {
       return DatabaseHelper.updateEntity(Customer.class,id,feildMap);
    }

    public boolean deleteCustomer(long id){
        return DatabaseHelper.deleteEntity(Customer.class,id);
    }



}