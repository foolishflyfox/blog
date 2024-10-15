# npm 中的 package.json 实践

package.json 是现在前端项目必备的文件，涵盖了项目依赖、项目命令、项目信息等内容。下面是一个 package.json 常有的样子。

```json
{
  "name": "my_package",
  "version": "1.0.1",
  "description": "make your package easier to find on the npm website",
  "type": "module",
  "main": "dist/index.js",
  "bin": "bin/my_package.js",
  "scripts": {
    "test": "echo \"Error: no test specified\" && exit 1"
  },
  "repository": {
    "type": "git",
    "url": "https://github.com/monatheoctocat/my_package.git"
  },
  "keywords": ["typescript","math"],
  "author": "fff",
  "contributors": ["foo", "bar"],
  "license": "ISC",
  "bugs": {
    "url": "https://github.com/monatheoctocat/my_package/issues"
  },
  "homepage": "https://github.com/monatheoctocat/my_package"
}
```

package 的字段分类:

- 描述性字段，这些信息会显示在 npm 网站对应的库页面
  - name: 包名(必填)
  - version: 包版本(必填)
  - description: 包描述
  - keywords: 关键字，用于 npm 的搜索
  - author: 包作者
  - contributors: 贡献者
  - homepage: 包相关的主页地址
  - repository: 如果是开源的库，指定开源仓库信息
  - license: 项目协议
  - bugs: 说明在开发者发现库存在 bug 时，去哪里提交 bug
- 依赖库字段
  - dependencies: 项目发布时依赖的库
  - devDependencies: 项目开发过程中依赖的辅助库
- 脚本配置
  - type: 只为 `module` 或 `commonjs`，指定默认 js 的模块类型
  - script: 命令脚本
  - config: 配置
- 文件配置
  - main: 指定程序入口，作为包时会按该字段加载包文件
  - bin: 指定可执行文件，包安装后，会为该文件创建软连接并添加可执行权限
  - files: 指定上传到 npm 的文件

描述性字段简单，略过不分析。

依赖库字段参考: todo。

## 脚本配置

config 中的配置可以在 script 中使用。例如创建一个 *foo.js* 文件:

```js
console.log("apple = " + process.env.npm_package_config_apple);
```

package.json 中内容为:

```json
{
  "name": "package-test",
  "version": "1.0.0",
  "type": "module",
  "scripts": {
    "foo": "node ./foo.js"
  },
  "config": {
    "apple": 123
  }
}
```

执行 `npm run foo` 的结果为:

```sh
> package-test@1.0.0 foo
> node ./foo.js

apple = 123
```

## main 字段

main 属性可以指定程序的主入口文件。

我们先创建一个 node_modules 文件夹。在该文件夹下建立如下的目录结构:

```
node_modules
└── fhb-pkg
    ├── dist
    │   └── test.js
    ├── index.js
    ├── main.js
    └── package.json
```

各文件内容为:

- package.json

```json
{
  "name": "fhb-pkg",
  "version": "1.0.0",
  "main": "index.js",
  "type": "module"
}
```

- index.js

```js
console.log("main 为 index.js");
export const fff = 123;
```

- main.js

```js
console.log("main 为 main.js");
export const fff = 456;
```

- dist/test.js

```js
console.log("main 为 dist/foo.js");
export const fff = 789;
```

我们修改 *foo.js* 的内容，从 fhb-pkg 包中导入 `fff` 并打印:

```js
import { fff } from "fhb-pkg";
console.log(`fff = ` + fff);
```

执行 `npm run foo` 的结果为:

```sh
main 为 index.js
fff = 123
```

将 *node_modules/fhb-pkg/package.json* 的 `main` 字段改为 `main.js`，执行 `npm run foo` 结果为:

```sh
main 为 main.js
fff = 456
```

将 *node_modules/fhb-pkg/package.json* 的 `main` 字段改为 `dist/test.js`，执行 `npm run foo` 结果为:

```sh
main 为 dist/foo.js
fff = 789
```

可见，在导入一个 npm 包时，是根据 `main` 字段确定入口地址的。

## bin 字段

有的包可以作为命令执行，例如 `vite`, `eslint`, `tsc`。我们也能创建一个作为命令执行的库。

例如我们创建一个 fff-pkg-a 项目, 其中包含2个文件: package.json 和 mybin.js

其中 mybin.js 内容为:

```js
#!/usr/bin/env node
console.log("fff-pkg-a mybin.js 被执行");
```

package.json 内容为:

```json
{
  "name": "fff-pkg-a",
  "version": "1.1.4",
  "description": "测试包发布",
  "type": "module",
  "bin": "mybin.js",
  "scripts": {},
  "keywords": [],
  "author": "",
  "license": "ISC"
}
```

通过 `npm publish` 发布后，在 package-test 项目通过 `npm i fff-pkg-a` 安装。安装完成后，查看当前 node_modules 的文件结构:

```sh
$ tree -a node_modules
node_modules
├── .bin
│   └── fff-pkg-a -> ../fff-pkg-a/mybin.js
├── .package-lock.json
└── fff-pkg-a
    ├── index.js
    ├── mybin.js
    └── package.json

3 directories, 5 files
```

可以看到 npm 为我们创建了一个 .bin 目录，该目录下即为命令可执行文件。将 package-test 的 package.json 脚本改为：

```json
{
  ...
  "scripts": {
    "bar": "fff-pkg-a"
  },
  ...
}
```

执行 `npm run bar` 结果为：

```sh
> package-test@1.0.0 bar
> fff-pkg-a

fff-pkg-a mybin.js 被执行
```

可见，`bin` 字段指定的文件，在包被 install 后，会在 node_module 的 .bin 下建立软连接，并添加执行权限。

## files 字段

files 字段是需要发布文件的配置。

默认情况下，README.md、package.json、LICENSE 文件会默认发布到 npm。

像 node_modules、package-lock.json 文件默认不会发布到 npm。

其他文件是否发布可以通过 files 字段控制，以库 [leafer-x-ruler](https://www.npmjs.com/package/leafer-x-ruler?activeTab=code) 为例，其 files 配置为:

```json
{
  ...
  "files": [
    "src",
    "types",
    "dev",
    "dist"
  ],
  ...
}
```

因此可以看到，除了默认发布的文件外，指定4个文件夹及其下的文件都被发布到 npm 了:

```sh
node_modules/leafer-x-ruler
├── LICENSE
├── README.md
├── dev
│   ├── bundle.js
│   └── index.html
├── dist
│   ├── index.cjs
│   ├── index.esm.js
│   ├── index.esm.min.js
│   ├── index.js
│   ├── index.min.cjs
│   └── index.min.js
├── package.json
├── src
│   ├── Ruler.ts
│   └── index.ts
└── types
    └── index.d.ts

5 directories, 14 files
```

而像 `playground`, `__tests__` 文件夹并没有发布上去。





