# 新建 electron 应用

可以通过 `pnpm create @quick-start/electron` 来创建 electron 项目。创建项目时会有一些选项供开发者选择：

- **Add Electron updater plugin?**: 是否添加应用更新功能；
- **Enable Electron download mirror proxy?**: 下载 Electron 相关资源时是否使用镜像代理，该配置会影响 _.npmrc_ 和 _electron-builder.yml_ 文件的内容，例如选择了 Yes，在 _.npmrc_ 下将新增:

```
electron_mirror=https://npmmirror.com/mirrors/electron/
electron_builder_binaries_mirror=https://npmmirror.com/mirrors/electron-builder-binaries/
```

在 electron-builder.yml 下将新增：

```yaml
electronDownload:
  mirror: https://npmmirror.com/mirrors/electron/
```

编译中出现如下报错：

```
Search string not found: "/supportedTSExtensions = .*(?=;)/"
(Use `node --trace-uncaught ...` to show where the exception was thrown)
```

解决方案：

- vue-tsc 版本切换成固定的 2.0.29
- typescript 版本切换成固定的 5.6.2
