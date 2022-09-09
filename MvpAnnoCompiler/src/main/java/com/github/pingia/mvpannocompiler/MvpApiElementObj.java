package com.github.pingia.mvpannocompiler;

import com.github.pingia.mvpannotation.BusiPage;
import com.github.pingia.mvpannotation.GetModelMethod;
import com.github.pingia.mvpannotation.ListQueryMethod;
import com.github.pingia.mvpannotation.SingleFragmentPage;
import com.github.pingia.mvpcodegen.BusiContractTemplateBean;
import com.github.pingia.mvpcodegen.BusiPageListMethod;
import com.github.pingia.mvpcodegen.BusiPageMethod;
import com.github.pingia.mvpcodegen.BusiPageMethodParam;
import com.github.pingia.mvpcodegen.BusiPageMethodUIConfig;
import com.github.pingia.mvpcodegen.MvpApiGenerateHelper;
import com.github.pingia.mvpcodegen.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


public class MvpApiElementObj extends AbstractTypeElementObj {
    private BusiPage api_anno;
    private SingleFragmentPage single_page_api_anno;
    private TypeElement basePresenterQuClassElement;
    private TypeElement singleFragmentClassElement;
    private Types mTypeUtils;
    private BusiContractTemplateBean mApiObj;

    public MvpApiElementObj(TypeElement typeElement, ProcessingEnvironment environment){
        super(typeElement,environment);
        mTypeUtils = env.getTypeUtils();
        api_anno   = mTypeEl.getAnnotation(BusiPage.class);
        single_page_api_anno = mTypeEl.getAnnotation(SingleFragmentPage.class);
    }

    @Override
    public void checkValidTypeElement() throws ProcessingException{
        try {
            api_anno.basePresenter();
        }catch (MirroredTypeException e){
            basePresenterQuClassElement = CompilerUtils.getClassFieldFromAnnotation(env, e);;
        }

        String interfaceName = mTypeEl.getQualifiedName().toString();

        if(!CompilerUtils.isExend(basePresenterQuClassElement, Constants.BASE_PRESENTER)
            || !CompilerUtils.isInterface(basePresenterQuClassElement,Constants. RESPONSE_DATA_HANDLER)){
            //如果是presenter的直接子类，并且他不是响应的接口实现类

        }


        mApiObj  = createTinyApiObject();
    }

    private BusiContractTemplateBean createTinyApiObject() throws ProcessingException{
        BusiContractTemplateBean api = new BusiContractTemplateBean();

        PackageElement pkgEl = env.getElementUtils().getPackageOf( mTypeEl);

        api.setPackageName(pkgEl.getQualifiedName().toString());
        api.setBusiName(api_anno.busiName());
        api.setBaseLoadPresenterQualifiedClassName(basePresenterQuClassElement.getQualifiedName().toString());
        api.setPageLayoutResId(api_anno.pageLayoutId());
        api.setPageTitleLayoutResId(api_anno.pageTitleLayoutId());
        api.setPageViewIntType(api_anno.pageType().getPageType());
        if(null != single_page_api_anno) {
            try {
                single_page_api_anno.fragmentClass();
            } catch (MirroredTypeException e) {
                singleFragmentClassElement = CompilerUtils.getClassFieldFromAnnotation(env, e);
                ;
            }

            api.setSingleFragmentClassQualifiedClassName(singleFragmentClassElement.getQualifiedName().toString());
        }

        String interfaceName = mTypeEl.getQualifiedName().toString();

        List<? extends Element> childElements = mTypeEl.getEnclosedElements();
        List<BusiPageMethod> methods = new ArrayList<>();
        List<BusiPageListMethod> listMethods = new ArrayList<>();

        for (Element childEl : childElements){
            if(childEl instanceof ExecutableElement){
                BusiPageMethod method = new BusiPageMethod();
                methods.add(method);

                ExecutableElement ee = (ExecutableElement)childEl;
                method.setMethodName(ee.getSimpleName().toString());
                method.setReturnType(CompilerUtils.getDeclaredTypeName(env, ee.getReturnType(), false));
                TypeMirror typeArgumentTypeMirror = CompilerUtils.getTypeArgumentFromDeclaredType(ee.getReturnType());
                method.setReturnTypeArgument(null == typeArgumentTypeMirror ? null: typeArgumentTypeMirror.toString());

                //提取修饰符
                Set<String> modifier_strs = new HashSet<>();
                for (Modifier modifier : ee.getModifiers()){
                    modifier_strs.add(modifier.toString());
                }
                method.setModifiers(modifier_strs);

                GetModelMethod methodAnno = ee.getAnnotation(GetModelMethod.class);
                if(null != methodAnno) {
                    TypeMirror returnTypeMirror = ee.getReturnType();
                    TypeElement returnTypeElement = (TypeElement) mTypeUtils.asElement(returnTypeMirror);
                    boolean isNormalResult = CompilerUtils.isInterface(returnTypeElement, Constants.RESPONSE_DATA_RESULT);
                    if(!isNormalResult){        //没有继承iResult接口,报错
                        ProcessingException.throwExp(mTypeEl, 99, "返回参数必须继承IResult");
                        continue;
                    }else {
                        method.setGetModelMethod(true);
                        method.setMethodName(method.getMethodName());
                        method.setAsync(methodAnno.isAsync());  //暂未用到，默认异步方法
                        method.setMethodDesc(methodAnno.desc());
                        method.setMethodDescResId(methodAnno.descResId());

                        BusiPageMethodUIConfig uiConfig = new BusiPageMethodUIConfig();
                        uiConfig.setShowRequestLoading(methodAnno.showBlockLoading());
                        uiConfig.setShowResultSuccess(methodAnno.showSuccessResultMsg());
                        uiConfig.setShowResultFail(methodAnno.showFailResultMsg());

                        method.setConfig(uiConfig);
                    }

                }else{
                    method.setGetModelMethod(false);
                }

                //提取
                ListQueryMethod listMethodAnno = ee.getAnnotation(ListQueryMethod.class);
                if(null != listMethodAnno){
                    BusiPageListMethod listMethod = new BusiPageListMethod();
                    listMethod.setFenyeQuery(listMethodAnno.isFenye());
                    listMethod.setPageSize(listMethodAnno.perSize());

                    listMethods.add(listMethod);
                }

                //提取方法参数用来作为变量
                List<BusiPageMethodParam> commonMethodParams = new ArrayList<>();
                for(VariableElement ve : ee.getParameters()) {
                    BusiPageMethodParam tmp = new BusiPageMethodParam();
                    String paramTypeStr = ve.asType().toString();

                    tmp.setParamType(paramTypeStr);
                    tmp.setParamName(ve.getSimpleName().toString());
                    commonMethodParams.add(tmp);
                }
                method.setParams(commonMethodParams);

            }
        }

        api.setMethods(methods);
        api.setListMethods(listMethods);
        return api;
    }

    @Override
    public void generateCode(Filer filer){
        if(null == mApiObj) return;

        env.getMessager().printMessage(Diagnostic.Kind.NOTE, "prepared to generate code for api: " + mApiObj);
        String baseAptPath = Utils.getAptSourceDirPath(filer);
        if(null == baseAptPath) return;

        String busi_module_path = baseAptPath.substring(0, baseAptPath.indexOf("build"));    //使用了注解的工程路径
        env.getMessager().printMessage(Diagnostic.Kind.NOTE, "with annotation module root Path: " + busi_module_path);

        //产生自动化代码
        String outputJavaSourcePath =
                                                                new File(busi_module_path, "src/main/java").getAbsolutePath();
        String assetsPath = busi_module_path + "src/main/assets";

        MvpApiGenerateHelper generator = new MvpApiGenerateHelper(filer, outputJavaSourcePath, assetsPath);
        generator.generateAll(mApiObj);

    }

}
