package org.smart.service;

import org.smart.framework.annocation.Service;
import org.smart.framework.annocation.Transaction;
import org.smart.framework.bean.FileParam;
import org.smart.framework.helper.DatabaseHelper;
import org.smart.framework.helper.UploadHelper;
import org.smart.model.Customer;

import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    // 测试使用事务 查询没有实际的意义
    @Transaction
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

    @Transaction
    public boolean createCustomer(Map<String,Object> feildMap, FileParam fileParam) {
        boolean result = DatabaseHelper.insertEntity(Customer.class,feildMap);
        if(result){
            UploadHelper.uploadFile("/temp/upload",fileParam);
        }
        return result;
    }
    public boolean updateCustomer(long id,Map<String,Object> feildMap) {
       return DatabaseHelper.updateEntity(Customer.class,id,feildMap);
    }

    public boolean deleteCustomer(long id){
        return DatabaseHelper.deleteEntity(Customer.class,id);
    }



}
