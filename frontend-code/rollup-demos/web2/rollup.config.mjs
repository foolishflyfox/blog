import terser from "@rollup/plugin-terser";

export default {
  input: "main.js",
  output: {
    compact: true,
    file: "dist/bundle.js",
  },
  plugins: [terser()],
};
