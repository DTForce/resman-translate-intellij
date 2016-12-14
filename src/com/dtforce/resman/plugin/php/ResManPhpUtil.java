package com.dtforce.resman.plugin.php;

import com.dtforce.resman.plugin.parser.ResManProperty;
import com.dtforce.resman.plugin.util.PropertyReference;
import com.dtforce.resman.plugin.util.ResManUtil;
import com.jetbrains.php.lang.psi.elements.ClassConstantReference;

import java.util.List;

public class ResManPhpUtil {

    public static PropertyReference extractPropertyReference(ClassConstantReference classConstantReference)
    {
        return new PropertyReference(
                classConstantReference.getClassReference().getName(),
                classConstantReference.getName()
        );
    }


    public static List<ResManProperty> findProperties(ClassConstantReference classConstantReference)
    {
        return ResManUtil.findProperties(
                classConstantReference.getProject(),
                extractPropertyReference(classConstantReference)
        );
    }

}
