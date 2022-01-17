package io.metersphere.plugin.parallel.params;

import io.metersphere.plugin.core.MsParameter;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParallelControllerParameters extends MsParameter {

    /**
     * 公共Cookie
     */
    private boolean enableCookieShare;
    /**
     * 是否停止继续
     */
    private Boolean onSampleError;

    /**
     * 是否是导入/导出操作
     */
    private boolean isOperating;
    /**
     * 项目ID，支持单接口执行
     */
    private String projectId;

    private String scenarioId;

    private String reportType;

    private List<String> csvFilePaths = new ArrayList<>();
}
