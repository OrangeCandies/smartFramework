package org.smart.framework.bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Requset {
    private String requsetPath;
    private String requsetMethod;
    public Requset(String requsetMethod,String requsetPath){
        this.requsetPath = requsetPath;
        this.requsetMethod = requsetMethod;
    }

    public String getRequsetPath() {
        return requsetPath;
    }

    public String getRequsetMethod() {
        return requsetMethod;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this,o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "Requset{" +
                "requsetPath='" + requsetPath + '\'' +
                ", requsetMethod='" + requsetMethod + '\'' +
                '}';
    }
}
