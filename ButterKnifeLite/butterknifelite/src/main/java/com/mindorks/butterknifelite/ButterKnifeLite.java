package com.mindorks.butterknifelite;

import android.app.Activity;
import android.view.View;
import com.mindorks.butterknifelite.annotations.BindView;
import com.mindorks.butterknifelite.annotations.OnClick;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by janisharali on 16/08/16.
 */

/**
 * Static class with annotation extraction methods
 */
public class ButterKnifeLite {


    private static final String TAG = "ButterKnifeLite";

    /**
     * List of object to be used for extracting annotations and then dispose when unbinded
     */
    private static List<Object> mObjectList = new ArrayList<>();


    /**
     * annotations for activity class
     * @param target is the activity with annotation
     */
    public static void bind(final Activity target){
        bindViews(target, target.getClass().getFields(), target.findViewById(android.R.id.content));
        createOnClick(target, target.getClass().getMethods(), target.findViewById(android.R.id.content));
        mObjectList.add(target);
    }


    /**
     * annotations for any class with the inflated view from XML or the root view
     * @param obj is any class instance with annotations
     * @param promptsView is the inflated view from the XML
     */
    public static void bind(final Object obj, View promptsView){
        bindViews(obj, obj.getClass().getFields(), promptsView);
        createOnClick(obj, obj.getClass().getMethods(), promptsView);
        mObjectList.add(obj);
    }


    /**
     * initiate the onclick listener for the annotated public methods
     * @param obj is any class instance with annotations
     * @param methods list of methods in the class with annotation
     * @param rootView is the inflated view from the XML
     */
    private static void createOnClick(final Object obj, Method[] methods, View rootView){
        for(final Method method : methods){
            Annotation annotation = method.getAnnotation(OnClick.class);
            if(annotation instanceof OnClick){
                OnClick onClick = (OnClick) annotation;
                int id = onClick.value();
                View view =  rootView.findViewById(id);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            method.invoke(obj);
                        }catch (IllegalAccessException e){
                            e.printStackTrace();
                        }
                        catch (InvocationTargetException e){
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }


    /**
     * initiate the view for the annotated public fields
     * @param obj is any class instance with annotations
     * @param fields list of methods in the class with annotation
     * @param rootView is the inflated view from the XML
     */
    private static void bindViews(final Object obj, Field[] fields, View rootView){
        for(final Field field : fields) {
            Annotation annotation = field.getAnnotation(BindView.class);
            if (annotation instanceof BindView) {
                BindView bindView = (BindView) annotation;
                int id = bindView.value();
                View view = rootView.findViewById(id);
                try {
                    field.set(obj, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * detaches the view from the object list
     * @param obj is any class instance with annotations, stored in the list
     */
    public static void unbind(Object obj){
       if(mObjectList.contains(obj)){
           mObjectList.remove(obj);
       }
    }
}
