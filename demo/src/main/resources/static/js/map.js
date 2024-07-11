const markerImage = new kakao.maps.MarkerImage(
		    '/img/mymarker.png', 
		    new kakao.maps.Size(40, 40),
		    new kakao.maps.Point(13, 34)
		);
let container = document.getElementById('map'); 
let currentPos = new kakao.maps.LatLng(127.269311, 37.715133);
let options = {
	    center: currentPos,
	    draggable: false, //지도변경금지
	    level: 4
	};
let map = new kakao.maps.Map(container, options);
let geocoder = new kakao.maps.services.Geocoder();

navigator.geolocation.getCurrentPosition(success);

function drawMap(currentPos){
	map.relayout();
	map.setCenter(currentPos);
	map.panTo(currentPos)
	
	
}