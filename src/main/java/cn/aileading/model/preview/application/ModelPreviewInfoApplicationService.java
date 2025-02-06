package cn.aileading.model.preview.application;

import cn.aileading.model.preview.domain.ModelFirstType;
import cn.aileading.model.preview.domain.ModelPreviewInfo;
import cn.aileading.model.preview.domain.ModelSecondType;
import org.springframework.stereotype.Service;

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
            String path = ModelRootRegistry.MODEL_ROOT_REGISTRY.get(modelFirstType) + "\\" + modelSecondType.name();
            File[] files = new File(path).listFiles((dir, name) -> name.endsWith(".safetensors") || name.endsWith(".ckpt"));
            if (files == null || files.length == 0) {
                continue;
            }
            modelPreviewInfoList.addAll(Arrays.stream(files).map(x -> {
                String baseName = x.getName().substring(0, x.getName().lastIndexOf('.'));
                ModelPreviewInfo modelPreviewInfo = new ModelPreviewInfo().setName(modelSecondType.name() + "/" + baseName);
                String pic = path + "\\" + baseName + ".png";
                if (!new File(pic).exists()) {
                    pic = ModelRootRegistry.MODEL_ROOT_REGISTRY.get(modelFirstType) + "\\" + "empty.png";
                }
                modelPreviewInfo.setPic(pic);

                String useTipsFile = path + "\\" + baseName + "_使用说明.txt";
                if (!new File(useTipsFile).exists()) {
                    useTipsFile = ModelRootRegistry.MODEL_ROOT_REGISTRY.get(modelFirstType) + "\\" + "empty_使用说明.txt";
                }
                try {
                    modelPreviewInfo.setUseTips(Files.readString(Path.of(useTipsFile)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String linkFile = path + "\\" + baseName + "_下载地址.txt";
                if (!new File(linkFile).exists()) {
                    linkFile = ModelRootRegistry.MODEL_ROOT_REGISTRY.get(modelFirstType) + "\\" + "empty_下载地址.txt";
                }
                try {
                    modelPreviewInfo.setLink(Files.readString(Path.of(linkFile)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return modelPreviewInfo;
            }).toList());
        }

        return modelPreviewInfoList;
    }
}
