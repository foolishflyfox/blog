import resolve from "@rollup/plugin-node-resolve";
export default {
  input: "main.js",
  output: {
    file: "dist/mytest.js",
    format: "es",
  },
  plugins: [resolve()],
  external: ["dayjs"],
};
