function startBannerSlider(){

let track = document.getElementById("bannerTrack");
let position = 0;

setInterval(function(){

position++;

track.style.transform = "translateX(-" + (position * 20) + "%)";

if(position >= 5){
position = 0;
track.style.transform = "translateX(0)";
}

},2000);

}