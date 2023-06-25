package io.metersphere.plugin.parallel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import io.metersphere.plugin.core.MsParameter;
import io.metersphere.plugin.core.MsTestElement;
import io.metersphere.plugin.core.utils.LogUtil;
import io.metersphere.plugin.parallel.utils.ElementUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import com.blazemeter.jmeter.controller.ParallelSampler;

import java.util.LinkedList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MsParallelController extends MsTestElement {
    public MsParallelController() {

    }

    private String clazzName = "io.metersphere.plugin.parallel.controller.MsParallelController";

    @JSONField(ordinal = 21)
    private Boolean limitMaxThread;
    @JSONField(ordinal = 22)
    private String maxThreads;

    @Override
    public void toHashTree(HashTree tree, List<MsTestElement> hashTree, MsParameter config) {
        LogUtil.info("===========开始转换MsDummySampler ==================");
        if (!this.isEnable()) {
            return;
        }
        ParallelSampler initParallelSampler = initParallelSampler(config);
        if (initParallelSampler != null) {
            final HashTree groupTree = tree.add(initParallelSampler);
            if (CollectionUtils.isNotEmpty(hashTree)) {
                hashTree.forEach(el -> {
                    // 给所有孩子加一个父亲标志
                    el.setParent(this);
                    el.toHashTree(groupTree, el.getHashTree(), config);
                });
            }
        } else {
            LogUtil.error("Connect Sampler 生成失败");
            throw new RuntimeException("Connect Sampler生成失败");
        }
    }

    public ParallelSampler initParallelSampler(MsParameter config) {
        try {
            ParallelSampler parallelSampler = new ParallelSampler();
            // base 执行时需要的参数
            String id = StringUtils.isNotEmpty(this.getId()) ? this.getId() : this.getResourceId();
            ElementUtil.setBaseParams(parallelSampler, this.getParent(), config, id, this.getIndex());

            parallelSampler.setEnabled(this.isEnable());
            String name = ElementUtil.getParentName(this.getParent());
            if (StringUtils.isNotEmpty(name)) {
                parallelSampler.setName(this.getName() + ElementUtil.SEPARATOR + name);
            }
            parallelSampler.setProperty(TestElement.GUI_CLASS, "com.blazemeter.jmeter.controller.ParallelControllerGui");
            parallelSampler.setProperty(TestElement.TEST_CLASS, "com.blazemeter.jmeter.controller.ParallelSampler");

            parallelSampler.setProperty("PARENT_SAMPLE", false);
            if (BooleanUtils.isTrue(this.getLimitMaxThread())) {
                parallelSampler.setProperty("LIMIT_MAX_THREAD_NUMBER", true);
                parallelSampler.setProperty("MAX_THREAD_NUMBER", this.getMaxThreads());
            }

            return parallelSampler;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
