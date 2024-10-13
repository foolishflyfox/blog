# lodash 和 lodash-es 的区别

lodash 是前端常用的工具库，其有一个 es 版本 lodash-es，两者的 api 一致，那 lodash-es 相比于 lodash 有什么优势呢？下面通过实验说明两者的区别。

## 实验

### 创建实验项目

通过创建一个 Vite + Vue 的项目进行此次实验。

```sh
$ pnpm create vite
✔ Project name: … lodash-es-test
✔ Select a framework: › Vue
✔ Select a variant: › JavaScript

Scaffolding project in /Users/foolishflyfox/Code/Year2024/Mon10/day12/lodash-es-test...

Done. Now run:

  cd lodash-es-test
  pnpm install
  pnpm run dev

$ cd lodash-es-test 
$ pnpm i
Packages: +32
++++++++++++++++++++++++++++++++
Progress: resolved 69, reused 32, downloaded 0, added 32, done

dependencies:
+ vue 3.5.12

devDependencies:
+ @vitejs/plugin-vue 5.1.4
+ vite 5.4.8

Done in 7.5s
```

### 引入 lodash / lodash-es 测试

通过 `pnpm add lodash lodash-es` 引入两个包。

#### 原始编译

未使用 lodash 时，编译的情况为:

```sh
pnpm build

> vite build

vite v5.4.8 building for production...
✓ 16 modules transformed.
dist/index.html                  0.46 kB │ gzip:  0.29 kB
dist/assets/index-BJLh6Aef.css   1.27 kB │ gzip:  0.65 kB
dist/assets/index-Bx4YFdMW.js   60.84 kB │ gzip: 24.57 kB
✓ built in 568ms
```

#### 调用 lodash 的函数

在 main.js 添加如下代码:

```js
import _ from "lodash";

console.log(_.random(0, 100));
```

通过 `pnpm build` 进行项目编译:

```sh
$ pnpm build

> vite build

vite v5.4.8 building for production...
✓ 19 modules transformed.
dist/index.html                   0.46 kB │ gzip:  0.29 kB
dist/assets/index-BJLh6Aef.css    1.27 kB │ gzip:  0.65 kB
dist/assets/index-B7Ld9m_2.js   134.04 kB │ gzip: 51.63 kB
✓ built in 1.03s
```

可以看到创建的 js 增大了 74kB，编译时间也差不多增加了一倍。

#### 调用 lodash-es 的函数

main.js 中添加的代码改为:

```js
import * as _ from "lodash-es";

console.log(_.random(0, 100));
```

编译结果为:

```sh
$ pnpm build

> lodash-es-test@0.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day12/lodash-es-test
> vite build

vite v5.4.8 building for production...
✓ 656 modules transformed.
dist/index.html                  0.46 kB │ gzip:  0.29 kB
dist/assets/index-BJLh6Aef.css   1.27 kB │ gzip:  0.65 kB
dist/assets/index-DhWitYOy.js   63.46 kB │ gzip: 25.63 kB
✓ built in 1.14s
```

#### 调用 lodash-es 的多个函数

main.js 中添加的代码修改为：

```js
import * as _ from "lodash-es";

console.log(_.random(0, 100));
console.log(_.upperCase("ac"));
```

编译结果为:

```sh
$x pnpm build

> lodash-es-test@0.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day12/lodash-es-test
> vite build

vite v5.4.8 building for production...
✓ 656 modules transformed.
dist/index.html                  0.46 kB │ gzip:  0.29 kB
dist/assets/index-BJLh6Aef.css   1.27 kB │ gzip:  0.65 kB
dist/assets/index-Dg1Odjl_.js   67.14 kB │ gzip: 27.54 kB
✓ built in 1.25s
```

## 总结

||编译后js包大小|js gzip后大小|编译用时|
|---|---|---|---|
|原始打包|60.84 kB|24.57 kB|568ms|
|调用lodash的1个函数|134.04 kB|51.63 kB|1.03s|
|调用lodash-es的1个函数|63.46 kB|25.63 kB|1.14s|
|调用lodash-es的2个函数|67.14 kB|27.54 kB|1.25s|

结论: 

1. lodash-es 因为使用了 es 语法，vite 在编译时会使用 Tree-Shaking 技术，只将使用到的 lodash-es 中的函数打包到 js 文件中。而 lodash 不能使用 Tree-Shaking 技术，会将整个 lodash 包都打进编译后的 js 文件中，导致编译后的 js 文件较大；
2. 使用 lodash-es 因为存在 Tree-Shaking，编译时间会比使用 lodash 稍长一点；
