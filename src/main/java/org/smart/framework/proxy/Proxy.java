package org.smart.framework.proxy;

public interface Proxy {

    Object doProxy(ProxyChain proxyChain) throws Exception;
}
