package org.smart.controller;

import org.smart.framework.annocation.Action;
import org.smart.framework.annocation.Controller;
import org.smart.framework.annocation.Inject;
import org.smart.framework.bean.Param;
import org.smart.framework.bean.View;
import org.smart.model.Customer;
import org.smart.service.CustomerService;

import java.util.List;

@Controller
public class CustomerController {

    @Inject
    public CustomerService customerService;


    @Action("get:/customer")
    public View index(Param param){
        List<Customer> customerList = customerService.getCustomerList();
        System.out.println("In controller");
        return new View("customer.jsp").addModel("customerLists",customerList);
    }
}
