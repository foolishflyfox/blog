const good = {
  sale: 0,
};
const goodSaleTag = document.getElementById("goodSale");
function changeGoodSaleTag() {
  goodSaleTag.innerText = good.sale;
}const turnoverTag = document.getElementById("turnover");
function changeTurnoverTag() {
  turnoverTag.innerHTML = good.sale * 9.9;
}function updatePage() {
  changeGoodSaleTag();
  changeTurnoverTag();
}
updatePage();
window.setInterval(() => {
  good.sale += 1;
  updatePage();
}, 1500);