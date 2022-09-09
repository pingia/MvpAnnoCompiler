package com.github.pingia.mvpannocompiler;

import javax.lang.model.element.Element;

public class ProcessingException extends Exception {
    private Element element;
    public ProcessingException(Element element, String msg,Object... args ){
        super(String.format(msg, args));
        this.element = element;

    }

    public static ProcessingException throwExp(Element element, int type,Object... args ) throws ProcessingException{
        throw new ProcessingException(element, getDesc(type), args);
    }

    public Element getElement() {
        return this.element;
    }

    private static String getDesc(int type){
        switch (type){
            case Constants.TYPE_MISSING_TYPE_CLASS_ANNO_PROP:
                return Constants.MISSING_TYPE_CLASS_ANNO_PROP;
            case Constants.TYPE_MISSING_IMPL_CLASS_ANNO_PROP:
                return Constants.MISSING_IMPL_CLASS_ANNO_PROP;
            case Constants.TYPE_MISSING_EXTENDS_INTERFACE:
                return Constants.MISSING_EXTENDS_INTERFACE;
            case Constants.TYPE_MISSING_IMPL_CLASS_IMPLEMENTS_INTERFACE:
                return Constants.MISSING_IMPL_CLASS_IMPLEMENTS_INTERFACE;
            case Constants.TYPE_MISSING_IMPL_CLASS_DECLARE_PKGNAME:
                return Constants.MISSING_IMPL_CLASS_DECLARE_PKGNAME;
            case Constants.TYPE_MISSING_API_NAME_ANNO_PROP:
                return Constants.MISSING_API_NAME_ANNO_PROP;
            case Constants.TYPE_MISSING_IMPL_CLASS_GENERIC_PARAM:
                return Constants.MISSING_IMPL_CLASS_GENERIC_PARAM;
            case Constants.TYPE_MULTIPLE_IMPL_CLASS_GENERIC_PARAM:
                return Constants.MULTIPLE_IMPL_CLASS_GENERIC_PARAM;
            case Constants.TYPE_MISSING_IMPL_CLASS_EXTENDS_CLASS:
                return Constants.MISSING_IMPL_CLASS_EXTENDS_CLASS;
            case Constants.TYPE_MISSING_CALLBACK_METHOD_PARAM_ANNO_PROP:
                return Constants.MISSING_CALLBACK_METHOD_PARAM_ANNO_PROP;
            case Constants.TYPE_ILLEGAL_METHOD_PARAM:
                return Constants.ILLEGAL_METHOD_PARAM;
            case Constants.TYPE_WRONG_METHOD_PARAM_ORDER:
                return Constants.WRONG_METHOD_PARAM_ORDER;
        }
        return "";
    }
}
