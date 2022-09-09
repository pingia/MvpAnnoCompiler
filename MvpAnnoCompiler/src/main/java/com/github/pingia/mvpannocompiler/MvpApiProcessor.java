package com.github.pingia.mvpannocompiler;

import com.github.pingia.mvpannotation.BusiPage;
import com.github.pingia.mvpannotation.GetModelMethod;
import com.github.pingia.mvpannotation.ListQueryMethod;
import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class MvpApiProcessor extends AbstractProcessor {
    private javax.lang.model.util.Types mTypeUtils;
    private Messager mMessager;
    private Filer mFiler;
    private Elements mElementUtils;

    private List<String> busiNameList = new ArrayList<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mTypeUtils = processingEnvironment.getTypeUtils();
        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
        mElementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        //仅处理TinyTmlApi和TinyJsBridgeApi
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(BusiPage.class.getCanonicalName());
        annotations.add(ListQueryMethod.class.getCanonicalName());
        annotations.add(GetModelMethod.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            info("you process-------------------");

            for (Element annotatedElement : roundEnvironment.getElementsAnnotatedWith(BusiPage.class)) {
                if (annotatedElement.getKind() != ElementKind.INTERFACE) {
                    throw new ProcessingException(annotatedElement, "Only interfaces can be annotated with @%s",
                            BusiPage.class.getSimpleName());
                }

                // We can cast it, because we know that it of ElementKind.CLASS
                TypeElement typeElement = (TypeElement) annotatedElement;

                BusiPage anno = typeElement.getAnnotation(BusiPage.class);
                String busiName = anno.busiName();
                if(busiNameList.contains(busiName))
                {
                    error("current busi name: " + busiName + ", has exist, jump over to next...");
                    continue;       //同一个业务名称，只会产生一个业务，所以你不要命名同一个业务
                }

                MvpApiElementObj w = new MvpApiElementObj(typeElement, processingEnv );
                w.checkValidTypeElement();
                w.generateCode(mFiler);
            }

        }catch (ProcessingException e){
            error(e.getElement(), e.getMessage());
        }

        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    private void error(Element e, String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args), e);
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }

    private void info(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, String.format(msg, args));
    }

}
