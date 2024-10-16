import babel from "@rollup/plugin-babel";

export default {
  input: "index.js",
  output: {
    file: "dist/bundle.js",
  },
  plugins: [
    babel({
      exclude: "**/node_modules/**",
      presets: [
        [
          "@babel/preset-env",
          {
            targets: {
              chrome: "110",
            },
          },
        ],
      ],
    }),
  ],
};
