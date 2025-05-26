# AIGC BI 智能数据分析平台

![image](https://github.com/user-attachments/assets/5d821e7e-06de-439d-ab38-79f39bdb0a85)
![image](https://github.com/user-attachments/assets/ee5ec6d5-e40b-44ab-8690-d561e7a40ccc)




## 一、项目简介

AIGC BI 是一款基于 **React + SpringBoot + MQ + AIGC** 的智能数据分析平台。  
用户只需：  

1. **导入原始数据集**（CSV/Excel）  
2. **输入分析诉求**（自由文本）  

即可自动生成：  

- **可视化图表**（柱状图/折线图/饼图等）  
- **智能分析结论**（自然语言报告）  

实现真正的“零门槛”数据分析，大幅降低人力成本，提升分析效率。

## 二、核心功能

- **智能分析（同步/异步）**  
- **多种图表类型**：柱状图、折线图、饼图……  
- **结果管理**：我的图表列表、详情回顾  
- **高并发限流**：基于 Redis+Redisson  
- **任务调度**：RabbitMQ 消息队列、JDK 线程池  
- **AIGC 引擎**：腾讯云 Deepseek SDK  

## 三、技术栈

| 层   | 技术栈                                                   |
| ---- | -------------------------------------------------------- |
| 前端 | React 18 + Ant Design Pro + ECharts                      |
| 后端 | SpringBoot 2.x                                           |
| 数据 | MySQL 8.x + MyBatis-Plus/MyBatis X                       |
| 缓存 | Redis + Redisson                                         |
| 消息 | RabbitMQ                                                 |
| AIGC | 腾讯云 Deepseek SDK                                      |
| 其他 | Swagger + Knife4j 接口文档、Hutool、Apache Commons Utils |

## 四、环境与依赖

- JDK 1.8+
- Node.js 14+
- MySQL 8.x
- Redis 6.x
- RabbitMQ 3.x

## 六、安装与启动

1. 克隆仓库

   ```bash
   git clone https://github.com/RJLante/AIGC_BI.git
   cd AIGC_BI
   ```

2. 后端配置

   - 进入后端目录：`springboot-init-master`

   - 编辑 `src/main/resources/application.yml`，示例：

     ```yaml
     yaml复制编辑spring:
       datasource:
         url: jdbc:mysql://localhost:3306/aigc_bi?useUnicode=true&characterEncoding=utf8
         username: root
         password: 123456
       redis:
         host: localhost
         port: 6379
     rabbitmq:
       host: localhost
       port: 5672
       username: guest
       password: guest
     deepseek:
       appid: YOUR_TENCENTCLOUD_APPID
       secretId: YOUR_SECRET_ID
       secretKey: YOUR_SECRET_KEY
     ```

   - 启动服务：

     ```bash
     mvn clean spring-boot:run
     ```

3. 前端启动

   - 进入前端目录：`rdbi-frontend`

   - 安装依赖并启动：

     ```bash
     npm install
     npm start
     ```

   - 浏览器访问：`http://localhost:3000`

## 七、使用示例

### 1. 智能分析（同步）

1. 上传 `CSV/Excel` 原始数据
2. 填写“分析目标”文本
3. 选择“图表类型”
4. 点击“提交”

> 页面右侧实时展示 AIGC 分析结论与图表

### 2. 智能分析（异步）

异步任务在大数据量或复杂分析需求时更稳定，完成后可在“我的图表”列表中查看。

## 八、项目结构

```bash
AIGC_BI/
├── springboot-init-master/    # 后端项目
│   ├── src/main/java/…        # Controller/Service/Mapper/Entity
│   ├── src/main/resources/    # application.yml、mybatis 配置
│   └── pom.xml
├── rdbi-frontend/             # 前端项目
│   ├── src/components/…       # 业务组件
│   ├── src/pages/…            # 页面路由
│   └── package.json
└── docs/                      # 文档及截图
```

## 九、API 文档

启动后访问：

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/doc.html` （Knife4j）
