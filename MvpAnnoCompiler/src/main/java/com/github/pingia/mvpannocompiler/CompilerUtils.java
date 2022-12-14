package com.github.pingia.mvpannocompiler;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

public final class CompilerUtils {

    public static TypeMirror getTypeArgumentFromDeclaredType(TypeMirror typeMirror){
        if(!(typeMirror instanceof DeclaredType)) return null;
        DeclaredType type = (DeclaredType) typeMirror;

        List<? extends TypeMirror> typeArguments = type.getTypeArguments();
        return typeArguments == null || typeArguments.size() == 0 ? null: typeArguments.get(0);
    }

    /**
     * Method to return the declared type name of the provided TypeMirror.
     * @param processingEnv Processing environment
     * @param type The type (mirror)
     * @param box Whether to (auto)box this type
     * @return The type name (e.g "java.lang.String")
     */
    public static String getDeclaredTypeName(ProcessingEnvironment processingEnv, TypeMirror type, boolean box) {
        if (type == null || type.getKind() == TypeKind.NULL
                || type.getKind() == TypeKind.WILDCARD) {
            return "java.lang.Object";
        }

        if (type.getKind() == TypeKind.ARRAY) {
            TypeMirror comp = ((ArrayType) type).getComponentType();
            return getDeclaredTypeName(processingEnv, comp, false);
        }

        boolean isPrimitive = type.getKind().isPrimitive();
        if (box && isPrimitive) {
            type = processingEnv.getTypeUtils()
                    .boxedClass((PrimitiveType) type).asType();
        }
        else if (isPrimitive) {
            return ((PrimitiveType) type).toString();
        }

        Element el = processingEnv.getTypeUtils().asElement(type);
        return null == el ? type.toString():el.toString();
    }

    /**
     * ???????????????????????????Class??????
     * @param env
     * @param mte
     * @return
     */
    public static TypeElement getClassFieldFromAnnotation(ProcessingEnvironment env, MirroredTypeException mte){
        TypeMirror classTypeMirror =  mte.getTypeMirror();
        TypeElement classTypeElement = (TypeElement) env.getTypeUtils().asElement(classTypeMirror);
        //??????canonicalName
        String canonicalName= classTypeElement.getQualifiedName().toString();
        //??????simple name
        String simpleName = classTypeElement.getSimpleName().toString();

        return classTypeElement;
    }

    /**
     *?????????????????????????????????????????????????????????
     * 1???????????????????????????????????????????????????+?????????
     * 2??????????????????????????????????????????????????????????????????????????? T,M
     *
     * @param typeElement
     * @param typeParamBoundsStr  ??????????????????+?????????
     * @return  Pair
     *          first ----?????????????????????????????????????????????????????????????????? true: ?????? false:?????????
     *          second ---- ?????????????????????????????????????????????????????????
     */
    public static AbstractMap.SimpleImmutableEntry<Boolean, String> getTypeParamInfoFromElement(TypeElement typeElement, String typeParamBoundsStr){
        List<? extends TypeParameterElement> typeParameterElements = typeElement.getTypeParameters();
        Iterator<? extends TypeParameterElement> i = typeParameterElements.iterator();

        boolean found = false;
        final StringBuilder sb = new StringBuilder();

        for (;;) {
            TypeParameterElement el = i.next();
            sb.append(el.toString());

            for (TypeMirror tm: el.getBounds()){
                if(tm.toString().contains(typeParamBoundsStr)){
                    found = true;
                    break;
                }
            }

            if(found) break;

            if (!i.hasNext()) break;
            sb.append(',');
        }

        return new AbstractMap.SimpleImmutableEntry<>(found, sb.toString());
    }


    public static boolean isExend(TypeElement el, String s){
        TypeMirror tm = el.getSuperclass();
        return tm.toString().contains(s);
    }

    public static boolean isInterface(TypeElement el,String s){
        List<? extends TypeMirror> list = el.getInterfaces();

        for (TypeMirror tm : list){
            if(tm.toString().contains(s))
                return true;
        }

        return false;
    }
}
