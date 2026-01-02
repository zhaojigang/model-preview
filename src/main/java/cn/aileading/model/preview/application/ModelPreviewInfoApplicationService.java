package cn.aileading.model.preview.application;

import cn.aileading.model.preview.domain.ModelFirstType;
import cn.aileading.model.preview.domain.ModelPreviewInfo;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${model.file.suffixes}")
    private String modelFileSuffixes;

    private List<String> getModelFileSuffixes() {
        return Arrays.asList(modelFileSuffixes.split(","));
    }
    public List<ModelPreviewInfo> getModelPreviewInfo(ModelFirstType modelFirstType) {
        List<ModelPreviewInfo> modelPreviewInfoList = new ArrayList<>();

        /*
         * 1. 获取模型根目录
         */
        String rootPath = ModelRootRegistry.MODEL_ROOT_REGISTRY.get(modelFirstType);
        File rootDir = new File(rootPath);
        if (!rootDir.exists() || !rootDir.isDirectory()) {
            return modelPreviewInfoList;
        }

        /*
         * 2. 读取根目录下的所有直接子项（文件和文件夹）
         */
        File[] rootItems = rootDir.listFiles();
        if (rootItems == null) {
            return modelPreviewInfoList;
        }

        /*
         * 3. 处理每个子项
         */
        for (File item : rootItems) {
            if (item.isFile()) {
                // 处理根目录下的直接文件
                if (isModelFile(item)) {
                    modelPreviewInfoList.add(processModelFile(item, null));
                }
            } else if (item.isDirectory()) {
                // 处理子文件夹下的所有文件
                processDirectory(item, item.getName(), modelPreviewInfoList);
            }
        }

        return modelPreviewInfoList;
    }

    /**
     * 判断是否为模型文件
     */
    private boolean isModelFile(File file) {
        String name = file.getName();
        List<String> suffixes = getModelFileSuffixes();
        return suffixes.stream().anyMatch(name::endsWith);
    }

    /**
     * 处理单个模型文件
     */
    private ModelPreviewInfo processModelFile(File file, String relativePath) {
        String baseName = file.getName().substring(0, file.getName().lastIndexOf('.'));
        String path = file.getParent();

        String name = (relativePath == null || relativePath.isEmpty()) ? baseName : (relativePath + File.separator + baseName);

        ModelPreviewInfo modelPreviewInfo = new ModelPreviewInfo()
                .setName(name)
                .setPic(path + File.separator + baseName + ".png");

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
    }

    /**
     * 递归处理目录下的所有模型文件
     */
    private void processDirectory(File directory, String relativePath, List<ModelPreviewInfo> modelPreviewInfoList) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile() && isModelFile(file)) {
                modelPreviewInfoList.add(processModelFile(file, relativePath));
            }
        }
    }
}
