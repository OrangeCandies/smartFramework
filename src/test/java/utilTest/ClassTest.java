package utilTest;

import framework.util.ClassUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

public class ClassTest {



    @Test
    public void testGetClass(){
        Set<Class<?>> classSet = ClassUtil.getClassSet("framework.helper");
        Assert.assertNotNull(classSet);
    }
}
