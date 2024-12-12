# 将一个 vue 项目改造为 electron 应用

## 环境搭建

通过 `pnpm create vite` 创建一个 vue 项目：

```sh
$ pnpm create vite
✔ Project name: … vue-electron
✔ Select a framework: › Vue
✔ Select a variant: › TypeScript
```

此时通过 `pnpm dev` 将启动 5173 端口提供前端页面服务。

## 调试启动 electron 应用

### 安装相关库

- `"electron": "^33.2.1"`: 最主要的 electron 开发库；
- `"electron-vite": "^2.3.0"`: 在 vite 下开发 electron 应用的库，注意：2.3.0 版本的 electron-vite 依赖的 vite 库是 ^4.0.0 或 ^5.0.0，如果 vite 版本是 6，需要降版本；
- `"concurrently": "^9.1.0"`: 该库用于同时启动命令；

在项目根目录添加 electron.js 文件：

```js
import { app, BrowserWindow } from "electron";
// import * as path from "path";

function createWindow() {
  const win = new BrowserWindow({
    width: 800,
    height: 600,
    webPreferences: {
      //   preload: path.join(__dirname, "preload.js"), // 可选
    },
  });

  win.loadURL("http://localhost:5173"); // Vite 的开发服务器地址
}

app.whenReady().then(createWindow);

app.on("window-all-closed", () => {
  if (process.platform !== "darwin") {
    app.quit();
  }
});

app.on("activate", () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});
```

在 package.json 添加 `"main": "electron.js"` 配置 electron 的服务入口。

在 package.json 中的 scripts 字段添加命令：

```js
"scripts": {
    "dev": "vite",
    "build": "vue-tsc -b && vite build",
    "preview": "vite preview",
    "electron": "electron .",
    "dev:electron": "concurrently \"npm run dev\" \"npm run electron\""
}
```

通过 `pnpm dev:electron` 即可启动 electron 应用。
