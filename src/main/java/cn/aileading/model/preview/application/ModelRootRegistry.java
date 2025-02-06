package cn.aileading.model.preview.application;

import cn.aileading.model.preview.domain.ModelFirstType;

import java.util.Map;

public class ModelRootRegistry {
    public static final Map<ModelFirstType, String> MODEL_ROOT_REGISTRY = Map.of(ModelFirstType.checkpoint, "D:\\ai\\self_use_package\\ComfyUI_windows_portable\\ComfyUI\\models\\checkpoints",
            ModelFirstType.lora, "D:\\ai\\self_use_package\\ComfyUI_windows_portable\\ComfyUI\\models\\loras",
            ModelFirstType.diffusionModel, "D:\\ai\\self_use_package\\ComfyUI_windows_portable\\ComfyUI\\models\\diffusion_models");

}
