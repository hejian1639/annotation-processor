package processor.javac.apt;

import processor.annotation.CustomAnnotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;

//指定该注解处理器可以解决的类型，需要完整的包名+类命
@SupportedAnnotationTypes({"processor.annotation.CustomAnnotation"})
//指定编译的JDK版本
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class CustomAnnotationProcessor extends AbstractProcessor {

    //这里就是处理注解的process函数
    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        System.out.println("process CustomAnnotation");
        //创建动态代码，实际上就是创建一个String, 写入到文件里
        //然后文件会被解释为.class文件

        StringBuilder builder = new StringBuilder()
                .append("package com.zhangjian.annotationprocessor.generated;\n\n")
                .append("public class GeneratedClass {\n\n")
                .append("\tpublic String getMessage() {\n")
                .append("\t\treturn \"");

        //获取所有被CustomAnnotation修饰的代码元素
        for (Element element : roundEnvironment.getElementsAnnotatedWith(CustomAnnotation.class)) {
            String objectType = element.getSimpleName().toString();
            builder.append(objectType).append(" exists!\\n");
        }

        builder.append("\";\n")
                .append("\t}\n")
                .append("}\n");

        //将String写入并生成.class文件
        try {
            JavaFileObject source = processingEnv.getFiler().createSourceFile(
                    "com.riil.baymax.test.GeneratedClass");

            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            //
        }

        return true;
    }
}