package utilTest;

import org.smart.framework.helper.DatabaseHelper;
import org.smart.model.Customer;

import java.util.List;

public class ClassTest {




    public static void main(String[] args){
        List<Customer> customers = null;
        String sql = "SELECT * FROM customer";
        customers = DatabaseHelper.queryEntityList(Customer.class,sql);
        System.out.println(customers);
    }
}
