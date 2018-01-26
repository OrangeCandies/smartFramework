package org.smart.controller;

import org.smart.framework.annocation.Action;
import org.smart.framework.annocation.Controller;
import org.smart.framework.annocation.Inject;
import org.smart.framework.bean.Data;
import org.smart.framework.bean.FileParam;
import org.smart.framework.bean.Param;
import org.smart.framework.bean.View;
import org.smart.model.Customer;
import org.smart.service.CustomerService;

import java.util.List;
import java.util.Map;

@Controller
public class CustomerController {

    @Inject
    public CustomerService customerService;


    @Action("get:/customer")
    public View index(){
        List<Customer> customerList = customerService.getCustomerList();
        return new View("customer.jsp").addModel("customerLists",customerList);
    }

    @Action("post:/customer_create")
    public Data createSubmit(Param param){
        Map<String, Object> filedMap = param.getFiledMap();
        FileParam fileParam = param.getFile("photo");
        boolean result = customerService.createCustomer(filedMap,fileParam);
        return new Data(result);
    }
}
