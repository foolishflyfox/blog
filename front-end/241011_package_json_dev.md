# 透彻演示 dependencies 和 devDependencies 区别

todo: 查阅官方资料，说明两者区别。

## 实战

实战概述: 我们创建两个 npm 包 fff-pkg-a 和 fff-pkg-b，其中 fff-pkg-b 依赖于 fff-pkg-a，分别以 dependencies 和 devDependencies 两种方式依赖。之后我们创建一个测试项目 test，查看不同依赖方式的效果。

### 创建 fff-pkg-a 包

首先，我们创建文件夹 *fff-pkg-a*，进入该文件夹并通过 `pnpm init` 命令进行初始化，创建 index.js 文件。最终该文件夹下有 package.json 和 index.js 两个文件。内容分别为:

- package.json:

```json
{
  "name": "fff-pkg-a",
  "version": "1.1.1",
  "description": "测试包发布",
  "main": "index.js",
  "type": "module",
  "scripts": {},
  "keywords": [],
  "author": "",
  "license": "ISC"
}
```

**注意: 要将 package.json 的 type 字段设为 module 类型，否则 index.js 不能使用 export 语法。**

- index.js

```js
export function foo() {
  console.log("fff-pkg-a@1.1.1");
}
```

最后通过 `npm publish` 发布 fff-pkg-a 包，发布 npm 包参考 [如何发布一个自己的 npm 包？](https://i-fanr.com/2023/03/29/npm-package/)。

### 创建 fff-pkg-b 包

创建 fff-pkg-b 文件夹，通过 `pnpm init` 初始化创建 package.json 文件。

通过 `pnpm add fff-pkg-a` 以 dependencies 的方式安装依赖，并创建 index.js，最终，package.json 和 index.js 的内容分别为:

- package.json

```json
{
  "name": "fff-pkg-b",
  "version": "1.2.1",
  "description": "",
  "main": "index.js",
  "type": "module",
  "scripts": {},
  "keywords": [],
  "author": "",
  "license": "ISC",
  "dependencies": {
    "fff-pkg-a": "1.1.1"
  }
}
```

- index.js

```js
import { foo } from "fff-pkg-a";

export function bar() {
  foo();
  console.log("fff-pkg-b 【dependencies 方式】 依赖 fff-pkg-a");
}
```

### 创建测试项目

创建文件夹 test，初始化项目，引入 fff-pkg-b 的依赖:

```sh
$ mkdir test
$ cd test
$ pnpm init
$ pnpm add fff-pkg-b@1.2.1
```

创建 index.mjs :

```js
import { bar } from "fff-pkg-b";

bar();
```

执行 `node index.mjs`:

```js
$ node index.mjs
fff-pkg-a@1.1.1
fff-pkg-b 【dependencies 方式】 依赖 fff-pkg-a
```

我们正确地调用了 fff-pkg-b 中的 bar 方法，而 bar 方法也正确地调用了 fff-pkg-a 中的 foo 方法。

查看 node_modules 中的文件结构:

```
$ tree -a node_modules
node_modules
├── .modules.yaml
├── .pnpm
│   ├── fff-pkg-a@1.1.1
│   │   └── node_modules
│   │       └── fff-pkg-a
│   │           ├── index.js
│   │           └── package.json
│   ├── fff-pkg-b@1.2.1
│   │   └── node_modules
│   │       ├── fff-pkg-a -> ../../fff-pkg-a@1.1.1/node_modules/fff-pkg-a
│   │       └── fff-pkg-b
│   │           ├── index.js
│   │           └── package.json
│   ├── lock.yaml
│   └── node_modules
│       └── fff-pkg-a -> ../fff-pkg-a@1.1.1/node_modules/fff-pkg-a
└── fff-pkg-b -> .pnpm/fff-pkg-b@1.2.1/node_modules/fff-pkg-b
```

因为 test 项目显式依赖 fff-pkg-b，因此在 node_modules 根目录下有 fff-pkg-b 软连接，指向 fff-pkg-b 文件夹。并且 .pnpm 目录下有 fff-pkg-a@1.1.1，该文件夹被 fff-pkg-b@1.2.1 中的一个软连接所引用，表明了 fff-pkg-b 依赖于 fff-pkg-a。

### 修改依赖方式为 devDependencies

修改 fff-pkg-b 中依赖方式及版本号，package.json 的内容变为:

```json
{
  "name": "fff-pkg-b",
  "version": "1.2.3",
  "description": "",
  "main": "index.js",
  "type": "module",
  "scripts": {},
  "keywords": [],
  "author": "",
  "license": "ISC",
  "devDependencies": {
    "fff-pkg-a": "1.1.1"
  }
}
```

并修改 index.js 的打印信息:

```js
import { foo } from "fff-pkg-a";

export function bar() {
  foo();
  console.log("fff-pkg-b 【devDependencies 方式】 依赖 fff-pkg-a");
}
```

通过 `npm publish` 重新发布 fff-pkg-b。

删除 test 项目中的 node_modules，并将 package.json 中对 fff-pkg-a 的依赖变为 1.2.3:

```json
{
  "name": "test",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {},
  "dependencies": {
    "fff-pkg-b": "1.2.3"
  }
}
```

此时执行 `node index.mjs` 将报错:

```
$ node index.mjs 
node:internal/modules/esm/resolve:844
  throw new ERR_MODULE_NOT_FOUND(packageName, fileURLToPath(base), null);
        ^

Error [ERR_MODULE_NOT_FOUND]: Cannot find package 'fff-pkg-a' imported from test/node_modules/.pnpm/fff-pkg-b@1.2.3/node_modules/fff-pkg-b/index.js
    at packageResolve (node:internal/modules/esm/resolve:844:9)
    at moduleResolve (node:internal/modules/esm/resolve:901:20)
    at defaultResolve (node:internal/modules/esm/resolve:1121:11)
    at ModuleLoader.defaultResolve (node:internal/modules/esm/loader:396:12)
    at ModuleLoader.resolve (node:internal/modules/esm/loader:365:25)
    at ModuleLoader.getModuleJob (node:internal/modules/esm/loader:240:38)
    at ModuleWrap.<anonymous> (node:internal/modules/esm/module_job:85:39)
    at link (node:internal/modules/esm/module_job:84:36) {
  code: 'ERR_MODULE_NOT_FOUND'
}

Node.js v20.10.0
```

报错信息提示 fff-pkg-b 的依赖 fff-pkg-a 不能找到，可见，devDependencies 类型的依赖，不会被递归下载，此时查看 node_modules 的文件树:

```
$ tree -a node_modules
node_modules
├── .modules.yaml
├── .pnpm
│   ├── fff-pkg-b@1.2.3
│   │   └── node_modules
│   │       └── fff-pkg-b
│   │           ├── index.js
│   │           └── package.json
│   └── lock.yaml
└── fff-pkg-b -> .pnpm/fff-pkg-b@1.2.3/node_modules/fff-pkg-b
```

可见的确没有下载 fff-pkg-a。如果此时我们在 test 中手动引入 fff-pkg-a 包会怎么样呢？

### 手动引入缺失的依赖

为了区分，我们先修改并发布一个更高版本的 fff-pkg-a 库。修改 fff-pkg-a 的 index.js:

```js
export function foo() {
  console.log("fff-pkg-a@1.1.2");
}
```
并将 fff-pkg-a 中 package.json 的 version 字段修改为 1.1.2，重新发布 fff-pkg-a。

在 test 项目中，通过 `pnpm add fff-pkg-a@1.1.2` 引入 fff-pkg-a。

此时执行结果为:

```
$ node index.mjs
fff-pkg-a@1.1.2
fff-pkg-b 【devDependencies 方式】 依赖 fff-pkg-a
```

查询正常执行，node_modules 中的树结果为:

```
$ tree -a node_modules
node_modules
├── .modules.yaml
├── .pnpm
│   ├── fff-pkg-a@1.1.2
│   │   └── node_modules
│   │       └── fff-pkg-a
│   │           ├── index.js
│   │           └── package.json
│   ├── fff-pkg-b@1.2.3
│   │   └── node_modules
│   │       └── fff-pkg-b
│   │           ├── index.js
│   │           └── package.json
│   └── lock.yaml
├── fff-pkg-a -> .pnpm/fff-pkg-a@1.1.2/node_modules/fff-pkg-a
└── fff-pkg-b -> .pnpm/fff-pkg-b@1.2.3/node_modules/fff-pkg-b
```

此时，如果我们引入 fff-pkg-b@1.2.1 版本会怎么样呢？即 test 自身引入的是 fff-pkg-a@1.1.2，fff-pkg-b@1.2.1 以 dependencies 引入的是 fff-pkg-a@1.1.1，那输出的会是什么结果？

### 多个 fff-pkg-a 版本并存

修改 test 中 packag-pkg-b 的版本为 1.2.1:

```json
{
  "name": "test",
  "version": "1.0.0",
  "description": "",
  "main": "index.js",
  "scripts": {},
  "dependencies": {
    "fff-pkg-a": "1.1.2",
    "fff-pkg-b": "1.2.1"
  }
}
```

删除 node_modules 后重新通过 `pnpm i` 安装依赖。

执行结果为：

```
$ node index.mjs
fff-pkg-a@1.1.1
fff-pkg-b 【dependencies 方式】 依赖 fff-pkg-a
```

可以看到 node_modules 的确存在多个版本的 fff-pkg-a 并存，并且 fff-pkg-b 使用的是通过其自身 package.json 指定的 fff-pkg-a 版本。

```
$ tree -a node_modules
node_modules
├── .modules.yaml
├── .pnpm
│   ├── fff-pkg-a@1.1.1
│   │   └── node_modules
│   │       └── fff-pkg-a
│   │           ├── index.js
│   │           └── package.json
│   ├── fff-pkg-a@1.1.2
│   │   └── node_modules
│   │       └── fff-pkg-a
│   │           ├── index.js
│   │           └── package.json
│   ├── fff-pkg-b@1.2.1
│   │   └── node_modules
│   │       ├── fff-pkg-a -> ../../fff-pkg-a@1.1.1/node_modules/fff-pkg-a
│   │       └── fff-pkg-b
│   │           ├── index.js
│   │           └── package.json
│   └── lock.yaml
├── fff-pkg-a -> .pnpm/fff-pkg-a@1.1.2/node_modules/fff-pkg-a
└── fff-pkg-b -> .pnpm/fff-pkg-b@1.2.1/node_modules/fff-pkg-b
```

### 总结

上述实验可以总结如下：

1. fff-pkg-b 以 dependencies 方式引入 fff-pkg-a@1.1.1，test 使用 fff-pkg-a@1.1.1 中的 foo 函数
2. fff-pkg-b 以 devDependencies 方式引入 fff-pkg-a@1.1.1，因为 devDependencies 的依赖不能被递归安装，test 执行报错
3. fff-pkg-b 以 devDependencies 方式引入 fff-pkg-a@1.1.1，test 显式依赖 fff-pkg-a@1.1.2，test 使用 fff-pkg-a@1.1.2 中的 foo 函数
4. fff-pkg-b 以 dependencies 方式引入 fff-pkg-a@1.1.1，test 显式依赖 fff-pkg-a@1.1.2，test 使用 fff-pkg-a@1.1.1 中的 foo 函数

因此，dependencies 与 devDependencies 最主要的区别是，作为依赖包时，能否被递归安装。


