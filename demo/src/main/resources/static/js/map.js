const markerImage = new kakao.maps.MarkerImage(
    '/img/mymarker.png', 
    new kakao.maps.Size(40, 40),
    new kakao.maps.Point(13, 34)
);
const favMarkerImage = new kakao.maps.MarkerImage(
    '/img/fav_marker.png', 
    new kakao.maps.Size(40, 40),
    new kakao.maps.Point(13, 34)
);
let container = document.getElementById('map'); 
let currentPos;
let map;
let geocoder = new kakao.maps.services.Geocoder();

navigator.geolocation.getCurrentPosition(success);

function drawMap(currentPos){
	let options = {
			center:currentPos,
		    draggable: false, //지도변경금지
		    level: 4
		};
	map = new kakao.maps.Map(container, options);
}