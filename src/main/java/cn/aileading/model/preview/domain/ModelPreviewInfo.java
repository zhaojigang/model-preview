package cn.aileading.model.preview.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 模型文件信息
 */
@Data
@Accessors(chain = true)
public class ModelPreviewInfo {
    private String name;
    private String pic;
    private String useTips;
    private String link;
}
