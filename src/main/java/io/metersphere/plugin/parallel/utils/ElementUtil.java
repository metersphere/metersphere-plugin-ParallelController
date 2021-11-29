package io.metersphere.plugin.parallel.utils;

import io.metersphere.plugin.core.MsTestElement;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ElementUtil {
    public static final String STEP_DELIMITER = "^@~@^";
    public static final String SEPARATOR = "<->";

    public static void getScenarioSet(MsTestElement element, List<String> id_names) {
        if (StringUtils.equals(element.getType(), "scenario")) {
            id_names.add(element.getResourceId() + "_" + element.getName());
        }
        if (element.getParent() == null) {
            return;
        }
        getScenarioSet(element.getParent(), id_names);
    }

    public static String getFullPath(MsTestElement element, String path) {
        if (element.getParent() == null) {
            return path;
        }
        path = StringUtils.isEmpty(element.getName()) ? element.getType() : element.getName() + ElementUtil.STEP_DELIMITER + path;
        return getFullPath(element.getParent(), path);
    }


    public static String getParentName(MsTestElement parent) {
        if (parent != null) {
            // 获取全路径以备后面使用
            String fullPath = getFullPath(parent, new String());
            return fullPath + ElementUtil.SEPARATOR + parent.getName();
        }
        return "";
    }

    public static String getFullIndexPath(MsTestElement element, String path) {
        if (element == null || element.getParent() == null) {
            return path;
        }
        path = element.getIndex() + "_" + path;
        return getFullIndexPath(element.getParent(), path);
    }
}
