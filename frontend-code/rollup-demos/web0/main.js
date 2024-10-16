var goodSale = 0;
var goodSaleTag = document.getElementById("goodSale");
var turnoverTag = document.getElementById("turnover");
function changeGoodSaleTag() {
  goodSaleTag.innerText = goodSale;
}
function changeTurnoverTag() {
  turnoverTag.innerHTML = goodSale * 9.9;
}
function updatePage() {
  changeGoodSaleTag();
  changeTurnoverTag();
}
updatePage();
window.setInterval(() => {
  goodSale += 1;
  updatePage();
}, 1500);
