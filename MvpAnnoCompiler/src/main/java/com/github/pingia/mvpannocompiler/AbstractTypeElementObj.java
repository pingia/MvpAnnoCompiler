package com.github.pingia.mvpannocompiler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;

public abstract class AbstractTypeElementObj {
    protected TypeElement mTypeEl;
    protected ProcessingEnvironment env;
    public AbstractTypeElementObj(TypeElement typeElement, ProcessingEnvironment environment){
        mTypeEl = typeElement;
        this.env = environment;
    }

    public abstract void checkValidTypeElement() throws ProcessingException;
    public abstract void generateCode(Filer filer);
}
