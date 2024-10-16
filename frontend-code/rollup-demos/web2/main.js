import { changeGoodSaleTag, good } from "./goodSale";
import { changeTurnoverTag } from "./turnover";

function updatePage() {
  changeGoodSaleTag();
  changeTurnoverTag();
}
updatePage();
window.setInterval(() => {
  good.sale += 1;
  updatePage();
}, 1500);
