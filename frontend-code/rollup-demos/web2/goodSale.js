export const good = {
  sale: 0,
};
const goodSaleTag = document.getElementById("goodSale");
export function changeGoodSaleTag() {
  goodSaleTag.innerText = good.sale;
}
