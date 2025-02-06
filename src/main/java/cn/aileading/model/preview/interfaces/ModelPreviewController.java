package cn.aileading.model.preview.interfaces;

import cn.aileading.model.preview.application.ModelPreviewInfoApplicationService;
import cn.aileading.model.preview.domain.ModelFirstType;
import cn.aileading.model.preview.domain.ModelPreviewInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

@CrossOrigin
@RequestMapping("/")
@RestController
public class ModelPreviewController {
    @Resource
    private ModelPreviewInfoApplicationService applicationService;

    @RequestMapping("/modelPreviewInfos")
    public List<ModelPreviewInfo> modelPreviewInfos(@RequestParam("modelFirstType") String modelFirstType) {
        return applicationService.getModelPreviewInfo(ModelFirstType.valueOf(modelFirstType));
    }

    @RequestMapping("/images")
    public void getImage(@RequestParam String path, HttpServletResponse response) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(file.toPath());
        response.setContentType(contentType);

        try (FileInputStream in = new FileInputStream(file);
             OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
    }

}
