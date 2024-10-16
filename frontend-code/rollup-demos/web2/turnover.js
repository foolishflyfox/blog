import { good } from "./goodSale";

const turnoverTag = document.getElementById("turnover");
export function changeTurnoverTag() {
  turnoverTag.innerHTML = good.sale * 9.9;
}
