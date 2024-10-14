# npm-run-all 使用实践

参考: [npm-run-all](https://github.com/mysticatea/npm-run-all)

## 背景

在前端开发中，你是否存在以下烦恼:

1. 写 package.json 的 scripts 命令时，命令太过冗长，例如编译命令 `build` 需要执行清理 `clean`, 编译css `build:css`, 编译js `build:js`, 编译html `build:html` 命令，则 `build` 命令需要写成 `build: npm run clean && npm run build:css && npm run build:js && npm run build:html`，这个命令太过冗长，`npm run` 都是重复的，能不能写成 `build: clean build:css build:js build:html`，甚至能不能更简单地写成 `build: clean build:*`。
2. 通常 `build` 的过程是先进行类型检查，后进行编译: `build: npm run typecheck && vite build`，两者是串行的关系，能不能使两个命令并行执行以加快编译速度？

如果你也有类似烦恼，相信这篇文章对你有用。

## 安装

```sh
# npm 安装
$ npm install npm-run-all --save-dev
# yarn 安装
$ yarn add npm-run-all --dev
# pnpm 安装
$ pnpm add -D npm-run-all
```

## 使用

`npm-run-all` 包提供了3个命令：`npm-run-all`, `run-s`, `run-p` 。

最主要的命令是 `npm-run-all`，我们可以使用该命令创建复杂的命令计划。`run-s` 和 `run-p` 是简写命令，`run-s` 用于串行任务，`run-p` 用于并行任务。

## npm-run-all 

创建一个文件夹，并通过 `pnpm init` 将该文件夹初始化为一个项目。在该项目下创建4个脚本文件:

- clean.sh:

```sh
echo "$(date): start clean"
sleep 1
echo "$(date): clean finished!"
```

- build-css.sh

```sh
echo "$(date): start build:css"
sleep 1
echo "$(date): build:css finished!"
```

- build-js.sh

```js
echo "$(date): start build:js"
sleep 3
echo "$(date): build:js finished!"
```

- build-html.sh

```js
echo "$(date): start build:html"
sleep 1
echo "$(date): build:html finished!"
```

### 串行执行多个命令

为 package.json 添加脚本命令:

```json
{
  "scripts": {
    "clean": "./clean.sh",
    "build:css": "./build-css.sh",
    "build:html": "./build-html.sh",
    "build:js": "./build-js.sh",
    "build": "npm-run-all clean build:css build:js build:html"
  }
}
```

使用 npm-run-all 后 build 命令简化了很多，执行 `pnpm build` 结果为:

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all clean build:css build:js build:html


> t1@1.0.0 clean /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./clean.sh

2024年10月14日 星期一 17时35分17秒 CST: start clean
2024年10月14日 星期一 17时35分18秒 CST: clean finished!

> t1@1.0.0 build:css /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-css.sh

2024年10月14日 星期一 17时35分18秒 CST: start build:css
2024年10月14日 星期一 17时35分19秒 CST: build:css finished!

> t1@1.0.0 build:js /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-js.sh

2024年10月14日 星期一 17时35分19秒 CST: start build:js
2024年10月14日 星期一 17时35分22秒 CST: build:js finished!

> t1@1.0.0 build:html /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-html.sh

2024年10月14日 星期一 17时35分23秒 CST: start build:html
2024年10月14日 星期一 17时35分24秒 CST: build:html finished!
```

可以看到 `clean`, `build:css`, `build:js`, `build:html` 串行执行了。

### 用通配符简化命令

将 `build` 命令修改为 `npm-run-all clean build:*`，执行 `pnpm build` 结果为:

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all clean build:*


> t1@1.0.0 clean /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./clean.sh

2024年10月14日 星期一 17时39分45秒 CST: start clean
2024年10月14日 星期一 17时39分46秒 CST: clean finished!

> t1@1.0.0 build:css /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-css.sh

2024年10月14日 星期一 17时39分46秒 CST: start build:css
2024年10月14日 星期一 17时39分47秒 CST: build:css finished!

> t1@1.0.0 build:html /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-html.sh

2024年10月14日 星期一 17时39分48秒 CST: start build:html
2024年10月14日 星期一 17时39分49秒 CST: build:html finished!

> t1@1.0.0 build:js /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-js.sh

2024年10月14日 星期一 17时39分49秒 CST: start build:js
2024年10月14日 星期一 17时39分52秒 CST: build:js finished!
```

可以看到 `clean`, `build:css`, `build:html`, `build:js` 串行执行了。执行 `build:*` 时，执行顺序按命令定义的先后顺序进行。

### 多个命令并行执行

将 `build` 命令修改为 `npm-run-all --parallel clean build:*`，执行结果为:

```sh
pnpm build

> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all --parallel clean build:*


> t1@1.0.0 build:css /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-css.sh


> t1@1.0.0 clean /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./clean.sh


> t1@1.0.0 build:html /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-html.sh


> t1@1.0.0 build:js /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-js.sh

2024年10月14日 星期一 17时49分47秒 CST: start build:css
2024年10月14日 星期一 17时49分47秒 CST: start build:html
2024年10月14日 星期一 17时49分47秒 CST: start clean
2024年10月14日 星期一 17时49分47秒 CST: start build:js
2024年10月14日 星期一 17时49分48秒 CST: build:html finished!
2024年10月14日 星期一 17时49分48秒 CST: build:css finished!
2024年10月14日 星期一 17时49分48秒 CST: clean finished!
2024年10月14日 星期一 17时49分50秒 CST: build:js finished!
```

可以看到4个命令已经并行执行了。上面 `build` 命令在 linux 可以写成 `npm run clean & npm run build:css & npm run build:js & npm run build:html`，执行结果为:

```js
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm run clean & npm run build:css & npm run build:js & npm run build:html


> t1@1.0.0 clean
> ./clean.sh


> t1@1.0.0 build:js
> ./build-js.sh


> t1@1.0.0 build:html
> ./build-html.sh


> t1@1.0.0 build:css
> ./build-css.sh

2024年10月14日 星期一 19时21分14秒 CST: start build:css
2024年10月14日 星期一 19时21分14秒 CST: start build:js
2024年10月14日 星期一 19时21分14秒 CST: start build:html
2024年10月14日 星期一 19时21分14秒 CST: start clean
2024年10月14日 星期一 19时21分15秒 CST: build:html finished!
2024年10月14日 星期一 19时21分15秒 CST: build:css finished!
2024年10月14日 星期一 19时21分15秒 CST: clean finished!
2024年10月14日 星期一 19时21分17秒 CST: build:js finished!
```

也有并行执行的效果，不过问题在于 Windows 下的 cmd.exe 并不能识别 `&`，跨平台存在问题。

另外，因为 `&` 表示命令后台运行，因此并不是在 build 执行完以后才退出命令行。因为最后一条命令 `npm run build:html` 不是后台执行，因此命令行退出时间取决于该命令的执行时间，该命令延时1s，而执行时间最长的 `npm run build:js` 延时3s，因此在命令行退出时，`npm run build:js` 并没有执行完毕，会导致在 `pnpm build` 命令结束2s后才在终端输出 `2024年10月14日 星期一 19时21分17秒 CST: build:js finished!` 的问题。

### 多命令并行时一个命令执行失败

并行执行时，如果某个命令执行的退出码不是0，则正在执行的命令将被杀死。例如，我们将 `build-html.sh` 修改为:

```sh
echo "$(date): start build:html"
sleep 2
exit 1
echo "$(date): build:html finished!"
```

则执行 `pnpm build` 的结果为:

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all --parallel clean build:*


> t1@1.0.0 clean /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./clean.sh


> t1@1.0.0 build:css /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-css.sh


> t1@1.0.0 build:html /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-html.sh


> t1@1.0.0 build:js /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./build-js.sh

2024年10月14日 星期一 19时32分48秒 CST: start clean
2024年10月14日 星期一 19时32分48秒 CST: start build:css
2024年10月14日 星期一 19时32分48秒 CST: start build:html
2024年10月14日 星期一 19时32分48秒 CST: start build:js
2024年10月14日 星期一 19时32分49秒 CST: clean finished!
2024年10月14日 星期一 19时32分49秒 CST: build:css finished!
 ELIFECYCLE  Command failed with exit code 1.
 ELIFECYCLE  Command failed.
ERROR: "build:html" exited with 1.
 ELIFECYCLE  Command failed with exit code 1.
```

可以看到，在 `build:html` 错误退出前执行完毕的 `clean`, `build:css` 都输出完成信息，而执行时间比 `build:html` 长的 `build:js` 因为 `build:html` 的错误退出，导致无法继续执行，没有输出完成信息。

### 串行与并行混合执行

上面并行的例子中存在一个问题，因为 `clean` 和其他编译命令并行，可能导致刚编译好的文件被清理掉，我们要的结果是先执行 `clean` 命令，执行完成后再并行执行3个编译命令。可以将 `build` 命令修改为 `npm-run-all --silent clean --parallel build:*`，执行结果为:

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all clean --silent --parallel build:*

2024年10月14日 星期一 21时38分20秒 CST: start clean
2024年10月14日 星期一 21时38分21秒 CST: clean finished!
2024年10月14日 星期一 21时38分21秒 CST: start build:html
2024年10月14日 星期一 21时38分21秒 CST: start build:js
2024年10月14日 星期一 21时38分21秒 CST: start build:css
2024年10月14日 星期一 21时38分22秒 CST: build:html finished!
2024年10月14日 星期一 21时38分22秒 CST: build:css finished!
2024年10月14日 星期一 21时38分25秒 CST: build:js finished!
```


如果希望执行顺序为: 先执行 `clean`, 再并行执行 `build:css build:js`, 最后执行 `build:html`，可以将 `build` 命令改为: `npm-run-all --silent clean --parallel build:css build:js --sequential build:html`, 执行结果为：

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all --silent clean --parallel build:css build:js --sequential build:html

2024年10月14日 星期一 21时42分49秒 CST: start clean
2024年10月14日 星期一 21时42分50秒 CST: clean finished!
2024年10月14日 星期一 21时42分50秒 CST: start build:js
2024年10月14日 星期一 21时42分50秒 CST: start build:css
2024年10月14日 星期一 21时42分51秒 CST: build:css finished!
2024年10月14日 星期一 21时42分53秒 CST: build:js finished!
2024年10月14日 星期一 21时42分54秒 CST: start build:html
2024年10月14日 星期一 21时42分55秒 CST: build:html finished!
```

`--sequential` 可以改为 `--serial`。

例如: `npm-run-all a b --parallel c d --sequential e f --parallel g h i` 或 `npm-run-all a b --parallel c d --serial e f --parallel g h i` 的执行步骤为：

1. 串行执行，先执行a，再执行b
2. b 执行完后，并行执行 c 和 d
3. c 和 d 都执行完后，串行执行 e 和 f
4. f 执行完后，并行执行 g、h 和 i

### 通配符

之前我们看到 `npm-run-all build:*`，其中 `*` 就是一个通配符，并且分隔符不一定是冒号，例如我们将 package.json 中的 `scripts` 改为:

```json
{
  "scripts": {
    "clean": "./clean.sh",
    "build_css": "./build-css.sh",
    "build_html": "./build-html.sh",
    "build_js": "./build-js.sh",
    "build": "npm-run-all --silent build_*"
  }
}
```
执行 `pnpm build` 的结果为:

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all --silent build_*

2024年10月14日 星期一 21时44分45秒 CST: start build:css
2024年10月14日 星期一 21时44分46秒 CST: build:css finished!
2024年10月14日 星期一 21时44分47秒 CST: start build:html
2024年10月14日 星期一 21时44分48秒 CST: build:html finished!
2024年10月14日 星期一 21时44分48秒 CST: start build:js
2024年10月14日 星期一 21时44分51秒 CST: build:js finished!
```
也是可以正常执行的。

甚至 `*` 也不一定在最后，例如 `build` 改为 `npm-run-all --silent build_*s`，将只执行 `build:css` 和 `build:js`，执行结果为：

```sh
> t1@1.0.0 build /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all --silent build_*s

2024年10月14日 星期一 21时45分53秒 CST: start build:css
2024年10月14日 星期一 21时45分54秒 CST: build:css finished!
2024年10月14日 星期一 21时45分54秒 CST: start build:js
2024年10月14日 星期一 21时45分57秒 CST: build:js finished!
```

### 传入参数

新建两个脚本用于接收命令行参数:

- foo.sh: `echo "foo: [1]=$1, [2]=$2, [3]=$3, [4]=$4, [5]=$5, [6]=$6"`
- bar.sh: `echo "bar: [1]=$1, [2]=$2, [3]=$3, [4]=$4, [5]=$5, [6]=$6"`

为 scripts 添加如下内容:

```json
{
  "scripts": {
    "test:foo": "./foo.sh",
    "test:bar": "./bar.sh",
    "test": "npm-run-all --parallel \"test:* 1 abc\"",
  }
}
```

则执行的结果为：

```sh
> t1@1.0.0 test /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all --parallel "test:* 1 abc"


> t1@1.0.0 test:foo /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./foo.sh "1" "abc"


> t1@1.0.0 test:bar /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./bar.sh "1" "abc"

foo: [1]=1, [2]=abc, [3]=, [4]=, [5]=, [6]=
bar: [1]=1, [2]=abc, [3]=, [4]=, [5]=, [6]=
```

上述的 `test` 命令改为 `"test": "npm-run-all --parallel \"test:foo 1 abc\" \"test:bar 1 abc\""` 也是相同的效果。

### 参数占位符

如果我们希望在执行命令时，再通过命令行传入参数，可以使用参数占位符。下面的例子演示了参数占位符的使用:

```json
{
  "test2": "npm-run-all \"test:foo {1}\" \"test:foo {1} {2}\" \"test:foo {@}\" \"test:foo {*}\" --"
}
```

注意，最后的 `--` 不可少，否则参数会被作为命令，例如没有 `--` 时执行 `pnpm test2 a`，结果为:

```sh
> t1@1.0.0 test2 /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all "test:foo {1}" "test:foo {1} {2}" "test:foo {@}" "test:foo {*}" "a"

ERROR: Task not found: "a"
 ELIFECYCLE  Command failed with exit code 1.
```

可以看到 a 被当做了一个任务。

加上 `--` 后，执行 `pnpm test2 1 aaa 1xy` 的结果为：

```sh
> t1@1.0.0 test2 /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> npm-run-all "test:foo {1}" "test:foo {1} {2}" "test:foo {@}" "test:foo {*}" -- "1" "aaa" "1xy"


> t1@1.0.0 test:foo /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./foo.sh "1"

foo: [1]=1, [2]=, [3]=, [4]=, [5]=, [6]=

> t1@1.0.0 test:foo /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./foo.sh "1" "aaa"

foo: [1]=1, [2]=aaa, [3]=, [4]=, [5]=, [6]=

> t1@1.0.0 test:foo /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./foo.sh "1" "aaa" "1xy"

foo: [1]=1, [2]=aaa, [3]=1xy, [4]=, [5]=, [6]=

> t1@1.0.0 test:foo /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> ./foo.sh "1 aaa 1xy"

foo: [1]=1 aaa 1xy, [2]=, [3]=, [4]=, [5]=, [6]=
```

在例子中可以很清楚的看出不同占位符的区别:

- `{1}`, `{2}`, ... : 表示一个参数，`{1}` 表示第一个参数，`{2}` 表示第二个参数;
- `{@}`: 表示所有参数;
- `{*}`: 将所有参数合并为一个参数;

### run-s / run-p

`run-s` 是 `npm-run-all -s` 或 `npm-run-all --series` 或 `npm-run-all --sequential` 的简写，表示串行执行命令。

`run-p` 是 `npm-run-all -p` 或 `npm-run-all --parallel` 的简写，表示并行执行命令。

scripts 中添加命令 `"build-s": "run-s --silent build:*"`，执行结果为:

```sh
> t1@1.0.0 build-s /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> run-s --silent build:*

2024年10月14日 星期一 21时24分26秒 CST: start build:css
2024年10月14日 星期一 21时24分27秒 CST: build:css finished!
2024年10月14日 星期一 21时24分27秒 CST: start build:html
2024年10月14日 星期一 21时24分28秒 CST: build:html finished!
2024年10月14日 星期一 21时24分29秒 CST: start build:js
2024年10月14日 星期一 21时24分32秒 CST: build:js finished!
```

可以看到3个build命令是串行的。这里使用了 `--silent` 参数，减少系统的日志输出。

添加命令 `"build-p": "run-p --silent build:*"`，执行结果为：

```sh
> t1@1.0.0 build-p /Users/foolishflyfox/Code/Year2024/Mon10/day14/t1
> run-p --silent build:*

2024年10月14日 星期一 21时26分48秒 CST: start build:html
2024年10月14日 星期一 21时26分48秒 CST: start build:js
2024年10月14日 星期一 21时26分48秒 CST: start build:css
2024年10月14日 星期一 21时26分49秒 CST: build:html finished!
2024年10月14日 星期一 21时26分49秒 CST: build:css finished!
2024年10月14日 星期一 21时26分51秒 CST: build:js finished!
```

可以看到3个命令是并行执行的。




