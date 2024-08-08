const markerImage = new kakao.maps.MarkerImage(
    '/img/mymarker.png', 
    new kakao.maps.Size(40, 40),
    new kakao.maps.Point(0, 40)
);
let container = document.getElementById('map'); 
let currentPos;
let map;
let geocoder = new kakao.maps.services.Geocoder();
let ps = new kakao.maps.services.Places();
let placeMarkers = []; //검색된 장소 마커
let mapRange = 500; //검색 범위


function searchAddrFromCoords(coords, callback) {
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}//지도에 현재위치와 주소표시하기

function placesSearchCB(data, status) {
    placeMarkers.forEach(m => {
        m.setMap(null); //이전 마커 클리어
    });
    if (status === kakao.maps.services.Status.OK) {
        for (var i = 0; i < data.length; i++) {
            displayMarker(data[i]);
        }
    }
}//검색된 목록 -> 마커