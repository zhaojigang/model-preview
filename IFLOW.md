# Model Preview 项目

## 项目概述

这是一个基于 Spring Boot 3.4.2 的模型预览后端服务，用于管理和预览 AI 模型文件。该项目提供 REST API 接口，用于浏览不同类型的 AI 模型，包括模型名称、预览图片、使用说明和下载链接等信息。

### 主要技术栈

- **框架**: Spring Boot 3.4.2
- **Java 版本**: Java 21
- **构建工具**: Maven
- **依赖管理**: Lombok 1.18.36
- **架构模式**: 三层架构（interfaces - application - domain）

### 项目结构

```
src/main/java/cn/aileading/model/preview/
├── ModelPreviewApplication.java      # Spring Boot 启动类
├── application/                      # 应用服务层
│   ├── ModelPreviewInfoApplicationService.java  # 模型预览信息查询服务
│   └── ModelRootRegistry.java        # 模型根路径注册表
├── domain/                           # 领域模型层
│   ├── ModelFirstType.java           # 模型一级类型枚举
│   └── ModelPreviewInfo.java         # 模型预览信息实体
└── interfaces/                       # 接口层
    └── ModelPreviewController.java   # REST API 控制器
```

## 构建和运行

### 前置要求

- Java 21 或更高版本
- Maven 3.6+
- 系统属性 `baseModelPath` 需要设置，指向 ComfyUI 模型根目录

### 构建项目

```bash
mvn clean package
```

### 运行项目

**方式一：使用 Maven**
```bash
mvn spring-boot:run
```

**方式二：使用 Java 命令（需要先构建）**
```bash
java -DbaseModelPath=/path/to/comfyui/models -jar target/model-preview-1.0.0.jar
```

**方式三：使用 Maven Wrapper**
```bash
./mvnw spring-boot:run
```

或在 Windows 上：
```cmd
mvnw.cmd spring-boot:run
```

### 系统属性配置

运行时必须设置 `baseModelPath` 系统属性，指向 ComfyUI 模型的根目录：

```bash
-DbaseModelPath=/absolute/path/to/comfyui/models
```

## 开发约定

### 代码风格

- 使用 Lombok 简化代码（`@Data`, `@Accessors(chain = true)`）
- 遵循 Spring Boot 最佳实践
- 使用链式调用模式（通过 `@Accessors(chain = true)`）

### 架构模式

项目采用清晰的三层架构：

1. **interfaces 层**: 负责 HTTP 请求处理，使用 `@RestController` 和 `@RequestMapping`
2. **application 层**: 业务逻辑处理，使用 `@Service` 注解
3. **domain 层**: 领域模型定义，包括枚举和实体类

### 模型类型

#### 一级类型（ModelFirstType）
- `checkpoint`: 检查点模型
- `lora`: LoRA 模型
- `diffusionModel`: 扩散模型
- `controlNet`: ControlNet 模型
- `upscaleModels`: 放大模型
- `vae`: VAE 模型

#### 二级类型（配置在 application.properties）
默认支持：sdxl, flux, sd15, pony, hunyuan, qwen, video, framer, skyreels, kontext, wan, audio, instantID, infiniteYou, nunchaku

### 支持的模型文件格式

- `.safetensors`
- `.ckpt`
- `.pt`

### 配置文件

主要配置文件位于 `src/main/resources/application.properties`：

```properties
spring.application.name=model-preview
model.second.types=sdxl,flux,sd15,pony,hunyuan,qwen,video,framer,skyreels,kontext,wan,audio,instantID,infiniteYou,nunchaku
```

## API 接口

### 获取模型预览信息

**端点**: `GET /modelPreviewInfos`

**参数**:
- `modelFirstType` (必需): 模型一级类型（checkpoint, lora, diffusionModel, controlNet, upscaleModels, vae）

**返回**: 模型预览信息列表（包含 name, pic, useTips, link）

### 获取模型图片

**端点**: `GET /images`

**参数**:
- `path` (必需): 图片文件的绝对路径

**返回**: 图片文件流

## 相关文档

详细文档请参考：https://github.com/zhaojigang/model-preview-front

## 注意事项

1. 运行前必须设置 `baseModelPath` 系统属性
2. 模型目录结构应符合 ComfyUI 标准格式
3. 每个模型文件可以配套以下文件：
   - `{模型名}.png`: 预览图片
   - `{模型名}_使用说明.txt`: 使用说明
   - `{模型名}_下载地址.txt`: 下载链接
4. 如果配套文件不存在，系统会自动创建空文件