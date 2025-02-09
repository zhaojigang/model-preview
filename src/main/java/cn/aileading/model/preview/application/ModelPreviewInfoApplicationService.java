package cn.aileading.model.preview.application;

import cn.aileading.model.preview.domain.ModelFirstType;
import cn.aileading.model.preview.domain.ModelPreviewInfo;
import cn.aileading.model.preview.domain.ModelSecondType;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ModelPreviewInfoApplicationService {

    public List<ModelPreviewInfo> getModelPreviewInfo(ModelFirstType modelFirstType) {
        List<ModelPreviewInfo> modelPreviewInfoList = new ArrayList<>();

        for (ModelSecondType modelSecondType : ModelSecondType.values()) {
            /*
             * 1. 获取模型所在地址
             */
            String path = ModelRootRegistry.MODEL_ROOT_REGISTRY.get(modelFirstType) + File.separator + modelSecondType.name();
            if (!new File(path).exists()) {
                continue;
            }

            /*
             * 2. 获取模型列表
             */
            File[] files = new File(path).listFiles((dir, name) -> name.endsWith(".safetensors") || name.endsWith(".ckpt"));
            if (files == null || files.length == 0) {
                continue;
            }

            /*
             * 3. 处理模型返回体
             */
            modelPreviewInfoList.addAll(Arrays.stream(files).map(x -> {
                String baseName = x.getName().substring(0, x.getName().lastIndexOf('.'));
                ModelPreviewInfo modelPreviewInfo = new ModelPreviewInfo().setName(modelSecondType.name() + File.separator + baseName).setPic(path + File.separator + baseName + ".png");
                String useTipsFile = path + File.separator + baseName + "_使用说明.txt";
                if (!new File(useTipsFile).exists()) {
                    try {
                        new File(useTipsFile).createNewFile();
                    } catch (IOException e) {
                        // 静默
                    }
                }
                try {
                    modelPreviewInfo.setUseTips(Files.readString(Path.of(useTipsFile)));
                } catch (IOException e) {
                    // 静默
                }

                String linkFile = path + File.separator + baseName + "_下载地址.txt";
                if (!new File(linkFile).exists()) {
                    try {
                        new File(linkFile).createNewFile();
                    } catch (IOException e) {
                        // 静默
                    }
                }
                try {
                    modelPreviewInfo.setLink(Files.readString(Path.of(linkFile)));
                } catch (IOException e) {
                    // 静默
                }

                return modelPreviewInfo;
            }).toList());
        }

        return modelPreviewInfoList;
    }
}
