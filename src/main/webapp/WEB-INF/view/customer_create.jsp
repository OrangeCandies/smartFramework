<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 15001
  Date: 2018/1/26
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>客户管理--客户创建</title>
</head>
<body>
 <c:set var="BASE" value="${pageContext.request.contextPath}"/>

<form id="customer_form" enctype="multipart/form-data">
    <table>
        <tr>
            <td>客户名称</td>
            <td>
                <input type="text" name="name" value="${customer.name}"/>
            </td>
        </tr>
        <tr>
            <td>联系人</td>
            <td>
                <input type="text" name="contact" value="${customer.contact}"/>
            </td>
        </tr>
        <tr>
            <td>电话号码</td>
            <td>
                <input type="text" name="telephone" value="${customer.telephone}"/>
            </td>
        </tr>
        <tr>
            <td>邮箱地址</td>
            <td>
                <input type="text" name="mail" value="${customer.mail}"/>
            </td>
        </tr>
        <tr>
            <td>照片</td>
            <td>
                <input type="file" name="photo" value="${customer.photo}"/>
            </td>
        </tr>
    </table>
    <button type="submit">保存</button>
</form>
    <script src="${BASE}/asset/jquery/jquery.min.js"></script>
    <script src="${BASE}/asset/jquery-form/jquery.form.min.js"></script>

    <script>
        $(function () {
            $('#customer_form').ajaxForm({
                type: 'post',
                url: '${BASE}/customer_create',
                success: function (data) {
                    if(data){
                        location.href="${BASE}/customer"
                    }
                }
            });
        });
    </script>
</body>
</html>
