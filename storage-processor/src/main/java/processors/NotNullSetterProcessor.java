package processors;

import annotation.Storage;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("annotation.Storage")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class NotNullSetterProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Storage.class)) {
            if (element.getKind().isClass()) {
                generateStorage((TypeElement) element);
            }
        }
        return true;
    }

    private void generateStorage(TypeElement  classElement) {
        String className = classElement.getSimpleName().toString();
        String storageClassName = className + "Storage";
        MethodSpec addMethod = MethodSpec.methodBuilder("add")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(ClassName.get(classElement), "value")
                .addStatement("storageList.add(value)")
                .build();
        MethodSpec removeMethod = MethodSpec.methodBuilder("remove")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(ClassName.get(classElement), "value")
                .addStatement("storageList.remove(value)")
                .build();
        TypeSpec storageClass = TypeSpec.classBuilder(storageClassName)
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(classElement)), "storageList", Modifier.PRIVATE)
                        .initializer("new $T<>()", ArrayList.class)
                        .build())
                .addMethod(addMethod)
                .addMethod(removeMethod)
                .build();
        JavaFile javaFile = JavaFile.builder(processingEnv.getElementUtils().getPackageOf(classElement).toString(), storageClass)
                .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Error writing file: " + e.getMessage());
        }
    }
}
