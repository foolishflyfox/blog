function updatePage() {
  changeGoodSaleTag();
  changeTurnoverTag();
}
updatePage();
window.setInterval(() => {
  goodSale += 1;
  updatePage();
}, 1500);
