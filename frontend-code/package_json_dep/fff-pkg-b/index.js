import { foo } from "fff-pkg-a";

export function bar() {
  foo();
  console.log("fff-pkg-b 【devDependencies 方式】 依赖 fff-pkg-a");
}
