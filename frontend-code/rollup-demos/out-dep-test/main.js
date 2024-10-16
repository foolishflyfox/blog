import dayjs from "dayjs";
import chalk from "chalk";

export function useDayjsFunc() {
  console.log(dayjs().format("YYYY-MM-DD HH:mm:ss"));
}

export function useChalkFunc() {
  console.log(chalk.blueBright("hello, world"));
}
