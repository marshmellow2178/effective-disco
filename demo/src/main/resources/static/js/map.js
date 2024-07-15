const markerImage = new kakao.maps.MarkerImage(
    '/img/mymarker.png', 
    new kakao.maps.Size(40, 40),
    new kakao.maps.Point(13, 34)
);
let container = document.getElementById('map'); 
let currentPos;
let map;
let geocoder = new kakao.maps.services.Geocoder();


