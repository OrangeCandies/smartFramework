package org.smart.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.annocation.Transaction;
import org.smart.framework.helper.DatabaseHelper;

import java.lang.reflect.Method;

public class TransactionProxy implements Proxy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionProxy.class);

    private static final ThreadLocal<Boolean> FLAG_HOLDER = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Exception {

        Object result = null;
        boolean flag = FLAG_HOLDER.get();
        Method method =  proxyChain.getTargetMethod();
        if(!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG_HOLDER.set(true);
            try{
                DatabaseHelper.benginTransaction();
                LOGGER.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DatabaseHelper.commitTransaction();
                LOGGER.debug("end transaction");
            } catch (Throwable throwable) {
                LOGGER.error("transaction rollback");
                DatabaseHelper.rollbackTransaction();
                throwable.printStackTrace();
                throw new RuntimeException(throwable);
            }finally {
                FLAG_HOLDER.remove();
            }
        }else{
            try {
                result = proxyChain.doProxyChain();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return result;
    }
}
