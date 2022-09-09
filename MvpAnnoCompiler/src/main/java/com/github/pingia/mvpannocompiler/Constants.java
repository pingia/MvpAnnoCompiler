package com.github.pingia.mvpannocompiler;

public final class Constants {
    public static final String PROTOTYPE_IMPL_CLASS_SUPER_CLASS = "cn.sunline.common.api.TinyProtoTypeApiImplWrapper";
    public static final String STATIC_IMPL_CLASS_SUPER_CLASS = "cn.sunline.common.api.TinyApiImplWrapper";
    public static final String IMPL_CLASS_PARAM_TYPE_CLASS = "cn.sunline.common.api.AbstractTinyApi";
    public static final String CALLBACK_FUNCTION_INTERFACE = "cn.sunline.common.api.ICallbackFunction";

    public static final String BASE_PRESENTER= "com.github.pingia.ui.framework.architecture.BasePresenter";
    public static final String RESPONSE_DATA_HANDLER  = "cn.sunline.basenetworklib2.net.IResponseDataHandler";
    public static final String RESPONSE_DATA_RESULT = "cn.sunline.basenetworklib2.net.IResult";


    public static final String MISSING_TYPE_CLASS_ANNO_PROP = "接口: %s 必须有'typeClass'注解属性";
    public static final String MISSING_IMPL_CLASS_ANNO_PROP = "接口: %s 必须有'implClass'注解属性";
    public static final String MISSING_EXTENDS_INTERFACE = "接口: %s 必须继承接口: %s";
    public static final String MISSING_IMPL_CLASS_IMPLEMENTS_INTERFACE = "'implClass'注解属性指向的类: %s 必须实现接口: %s";
    public static final String MISSING_IMPL_CLASS_DECLARE_PKGNAME = "'implClass'注解属性指向的类: %s 必须声明包名: %s";
    public static final String MISSING_API_NAME_ANNO_PROP = "接口: %s 必须通过注解属性: 'tmlApiName'或'bridgeApiName'来声明TML Api或JS桥 Api名称";
    public static final String MISSING_IMPL_CLASS_GENERIC_PARAM = "'implClass'注解属性指向的类: %s 必须包含继承自: %s 的泛型参数";
    public static final String MULTIPLE_IMPL_CLASS_GENERIC_PARAM = "'implClass'注解属性指向的类: %s 仅能包含一个继承自: %s 的泛型参数";
    public static final String MISSING_IMPL_CLASS_EXTENDS_CLASS = "'implClass'注解属性指向的类: %s 必须继承自类: %s";
    public static final String MISSING_CALLBACK_METHOD_PARAM_ANNO_PROP = "接口: %s, 方法: %s , 参数: %s 是回调参数，必须添加注解: %s";
    public static final String ILLEGAL_METHOD_PARAM = "接口: %s, 方法: %s , 参数: %s 的类型是: %s，目前仅支持: java.lang.String，com.alibaba.fastjson.JSONObject，cn.sunline.common.api.ICallbackFunction";
    public static final String WRONG_METHOD_PARAM_ORDER = "接口: %s, 方法: %s , 参数: %s, 必须在回调参数: %s 之前声明";



    public static final int TYPE_MISSING_TYPE_CLASS_ANNO_PROP = 0;
    public static final int TYPE_MISSING_IMPL_CLASS_ANNO_PROP = 1;
    public static final int TYPE_MISSING_EXTENDS_INTERFACE = 2;
    public static final int TYPE_MISSING_IMPL_CLASS_IMPLEMENTS_INTERFACE = 3;
    public static final int TYPE_MISSING_IMPL_CLASS_DECLARE_PKGNAME = 4;
    public static final int TYPE_MISSING_API_NAME_ANNO_PROP = 5;
    public static final int TYPE_MISSING_IMPL_CLASS_GENERIC_PARAM = 6;
    public static final int TYPE_MULTIPLE_IMPL_CLASS_GENERIC_PARAM = 7;
    public static final int TYPE_MISSING_IMPL_CLASS_EXTENDS_CLASS = 8;
    public static final int TYPE_MISSING_CALLBACK_METHOD_PARAM_ANNO_PROP = 9;
    public static final int TYPE_ILLEGAL_METHOD_PARAM = 10;
    public static final int TYPE_WRONG_METHOD_PARAM_ORDER = 11;
}
