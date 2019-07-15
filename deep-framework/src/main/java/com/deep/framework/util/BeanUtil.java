package com.deep.framework.util;

import com.deep.framework.annotation.Operation;
import com.deep.framework.graph.Tenser;
import com.deep.framework.operation.Node;

import java.lang.reflect.Method;

public class BeanUtil {

    public static boolean isOperation(Node node) {
        try {
            Method method = node.getClass().getMethod("compute");
            return method.getAnnotation(Operation.class) != null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isArray(Object o) {
        if (o == null)
            return false;
        return o.getClass().isArray();
    }

    public static boolean isNotArray(Object o) {
        if (o == null)
            return true;
        return !o.getClass().isArray();
    }

    public static boolean isNone(Tenser o) {
        return o.getName().equals("None");
    }

    public static boolean isNotNone(Tenser o) {
        return !o.getName().equals("None");
    }
}
