package utilTest;

import org.junit.Test;
import org.smart.framework.HelperLoader;
import org.smart.framework.helper.BeanHelper;
import org.smart.model.Customer;
import org.smart.service.CustomerService;

import java.util.List;

public class ClassTest {



    @Test
    public void testGetClass(){
        HelperLoader.init();
        CustomerService customerService = BeanHelper.getBean(CustomerService.class);
        List<Customer> customerList = customerService.getCustomerList();
        if(customerList != null)
        for(Customer c:customerList){
            System.out.println(c);
        }
    }
}
