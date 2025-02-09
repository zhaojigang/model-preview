package cn.aileading.model.preview.application;

import cn.aileading.model.preview.domain.ModelFirstType;

import java.io.File;
import java.util.Map;

public class ModelRootRegistry {
    private final static String baseComfyUIModelPath = System.getProperty("baseModelPath");

    public static final Map<ModelFirstType, String> MODEL_ROOT_REGISTRY = Map.of(
            ModelFirstType.checkpoint, baseComfyUIModelPath + File.separator + "checkpoints",
            ModelFirstType.lora, baseComfyUIModelPath + File.separator + "loras",
            ModelFirstType.diffusionModel, baseComfyUIModelPath + File.separator + "diffusion_models");

}
