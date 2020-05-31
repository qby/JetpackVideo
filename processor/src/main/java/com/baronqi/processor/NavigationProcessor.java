package com.baronqi.processor;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baronqi.libannotation.ActivityDestination;
import com.baronqi.libannotation.FragmentDestination;
import com.google.auto.service.AutoService;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import javax.annotation.processing.Processor;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.baronqi.libannotation.ActivityDestination",
        "com.baronqi.libannotation.FragmentDestination"})
public class NavigationProcessor extends AbstractProcessor {

    private static final String TAG = "NavigationProcessor";
    private static final String OUTPUT_FILE_NAME = "destination.json";
    private Messager mMessager;
    private Filer filer;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mMessager = processingEnvironment.getMessager();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "process baronqi");

        Set<? extends Element> activityDestinations = roundEnvironment.getElementsAnnotatedWith(ActivityDestination.class);
        Set<? extends Element> fragmentDestinations = roundEnvironment.getElementsAnnotatedWith(FragmentDestination.class);

        if (activityDestinations.isEmpty() && fragmentDestinations.isEmpty()) {
            return true;
        }

        HashMap<String, JSONObject> destMap = new HashMap<>();
        handleDestination(activityDestinations, ActivityDestination.class, destMap);
        handleDestination(fragmentDestinations, FragmentDestination.class, destMap);

        OutputStreamWriter writer = null;
        FileOutputStream fos = null;
        try {
            FileObject resource = filer.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
            String path = resource.toUri().getPath();
            String appPath = path.substring(0, path.indexOf("app") + 4);
            String assetsPath = appPath + "src/main/assets";

            File file = new File(assetsPath);
            if (!file.exists()) {
                file.mkdirs();
            }

            File outputFile = new File(file, OUTPUT_FILE_NAME);
            if (outputFile.exists()) {
                outputFile.delete();
            }

            outputFile.createNewFile();
            String content = JSON.toJSONString(destMap);


            fos = new FileOutputStream(outputFile);
            writer = new OutputStreamWriter(fos, "UTF-8");
            writer.write(content);
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void handleDestination(Set<? extends Element> elements,
                                   Class<? extends Annotation> annotationClazz,
                                   HashMap<String, JSONObject> destMap) {

        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            String clazzName = typeElement.getQualifiedName().toString();
            int id = Math.abs(clazzName.hashCode());
            String path = null;
            boolean login = false;
            boolean isDefault = false;
            boolean isActivity = false;

            Annotation annotation = typeElement.getAnnotation(annotationClazz);
            if (annotation instanceof ActivityDestination) {
                ActivityDestination dest = (ActivityDestination) annotation;
                path = dest.path();
                login = dest.login();
                isDefault = dest.isDefault();
                isActivity = true;

            } else if (annotation instanceof FragmentDestination) {
                FragmentDestination dest = (FragmentDestination) annotation;
                path = dest.path();
                login = dest.login();
                isDefault = dest.isDefault();
                isActivity = false;

            }

            if (destMap.containsKey(path)) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "different page must use different path");
            } else {
                JSONObject object = new JSONObject();
                object.put("id", id);
                object.put("path", path);
                object.put("login", login);
                object.put("isDefault", isDefault);
                object.put("clazzName", clazzName);
                object.put("isActivity", isActivity);

                destMap.put(path, object);
            }


        }
    }
}
