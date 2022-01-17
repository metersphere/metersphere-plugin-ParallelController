package io.metersphere.plugin.parallel;

import io.metersphere.plugin.core.api.UiScriptApi;
import io.metersphere.plugin.core.ui.PluginResource;
import io.metersphere.plugin.core.ui.UiScript;
import io.metersphere.plugin.core.utils.LogUtil;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class UiScriptApiImpl extends UiScriptApi  {
    /**
     * 企业版插件增加 这个方法
     *
     * @return 是否是企业版插件
     */
    public boolean xpack() {
        return false;
    }

    @Override
    public PluginResource init() {
        LogUtil.info("开始初始化脚本内容 ");
        List<UiScript> uiScripts = new LinkedList<>();
        String scriptString = getJson("/json/ui_parallel.json");
        UiScript script = new UiScript("parallel_controller", "并行控制器", "io.metersphere.plugin.parallel.controller.MsParallelController", scriptString);
        script.setJmeterClazz("GenericController");

        // 添加可选参数
        script.setFormOption(getJson("/json/parallel-ui_form.json"));

        uiScripts.add(script);
        LogUtil.info("初始化脚本内容结束 ");
        return new PluginResource("parallel-v1.0.1", uiScripts);
    }

    @Override
    public String customMethod(String req) {
        LogUtil.info("Parallel Controller 自定义方法");
        return null;
    }

    public String getJson(String path) {
        try {
            InputStream in = UiScriptApiImpl.class.getResourceAsStream(path);
            String json = org.apache.commons.io.IOUtils.toString(in);
            return json;
        } catch (Exception ex) {
            LogUtil.error(ex.getMessage());
        }
        return null;
    }
}
