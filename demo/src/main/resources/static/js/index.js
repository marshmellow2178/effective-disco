const markerImage = new kakao.maps.MarkerImage(
    '/img/mymarker.png', 
    new kakao.maps.Size(40, 40),
    new kakao.maps.Point(13, 34)
);
const ctgr_code = ['', 'MT1', 'CS2', 'PS3', 'SC4', 'AC5', 'PK6', 'OL7', 'SW8', 'BK9',
    'CT1', 'AG2', 'PO3', 'AT4', 'AD5', 'FD6', 'CE7', 'HP8', 'PM9'];
const ctgr_name = ['전체', '대형마트', '편의점', '어린이집/유치원', '학교', '학원', '주차장', '주유소/충전소',
    '지하철역', '은행', '문화시설', '중개업소', '공공기관', '관광명소', '숙박', '음식점', '카페', '병원', '약국'];

let category = document.querySelector("#category");
let container = document.getElementById('map'); 
let infowindow = new kakao.maps.InfoWindow({ zindex: 1 });
let geocoder = new kakao.maps.services.Geocoder();
let mapLevelInput=document.querySelector('#maplevel'); //input
let zoomInBtn = document.querySelector('#zoomin');
let zoomOutBtn = document.querySelector('#zoomout');
let ps = new kakao.maps.services.Places();
let form = document.querySelector('form');
let keywordInput = document.querySelector("#keyword_input");
let placeMarkers = []; //검색된 장소 마커
let mapRange = 500; //검색 범위
let destId = document.querySelector("#dest_id");
let destName = document.querySelector('#dest_name');
let destDist = document.querySelector('#dest_distance');
let destImg = document.querySelector('#dest_img');
let destAddr = document.querySelector('#dest_addr');
let currentPos = new kakao.maps.LatLng(127.269311, 37.715133);
let options = {
	    center: currentPos,
	    draggable: false, //지도변경금지
	    level: 4
	};
let map = new kakao.maps.Map(container, options);
	
form.addEventListener('submit', onFormSubmit);
zoomInBtn.addEventListener('click', zoomIn);
zoomOutBtn.addEventListener('click', zoomOut);
for (let i = 0; i < ctgr_code.length; i++) {
    let option = document.createElement('option');
    option.value = ctgr_code[i].toLowerCase();
    option.innerText = ctgr_name[i];
    category.appendChild(option);
}// 카테고리 메뉴 생성하기
category.addEventListener('change', onSelect);
navigator.geolocation.getCurrentPosition(success);

function success(pos){
	currentPos = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
	drawMap(currentPos);
}//현재위치 로드

function drawMap(currentPos){
	map.relayout();
	map.setCenter(currentPos);
	map.panTo(currentPos);
	mapLevelInput.value = map.getLevel();
	
	let currentMarker = new kakao.maps.Marker({
	    position: currentPos,
	    image: markerImage
	});
	currentMarker.setMap(map);
	
	let circle = new kakao.maps.Circle({
	    center : currentPos,
	    radius : mapRange,
	    strokeWeight: 2,
	    strokeColor: '#75B8FA',
	    strokeOpacity: 1,
	    strokeStyle: 'solid',
	    fillColor: '#CFE7FF',
	    fillOpacity: 0.1
	});
	circle.setMap(map);
	
	searchAddrFromCoords(currentPos, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
            let content = result[0].address.address_name;
            infowindow.setContent('<div style="padding:5px;font-size:12px;">' + content + '</div>');
            infowindow.open(map, currentMarker);

		    destName.innerText = '현재 위치';
			destId.href = 'javascript:void(0);';
		    destAddr.innerText = content;
		    destDist.innerText = '';
			destImg.setAttribute('src', '/img/all.png');
		}
    });
}//위치에 맞게 지도 그리고 정보창 표시

function searchAddrFromCoords(coords, callback) {
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}//지도에 현재위치와 주소표시하기
	
function zoomIn(){
    let level = map.getLevel();
    map.setLevel(--level);
    mapLevelInput.value = level;
}//확대

function zoomOut(){
    let level = map.getLevel();
    map.setLevel(++level);
    mapLevelInput.value = level;
}
//축소

function onSelect(event){ 
    infowindow.close(); //지도 클리어
    keywordInput.value = ''; //검색창 클리어
    ps.categorySearch(event.target.value, placesSearchCB, {
        location: currentPos,
        radius: mapRange
    });
}//카테고리 검색

function onFormSubmit(event){
    event.preventDefault();
    if(keywordInput.value == ''){
    	return;
    }
    infowindow.close();
    ps.keywordSearch(keywordInput.value, placesSearchCB, {
        category_group_code: category.value,
        location: currentPos,
        radius: mapRange
    });
}//키워드 || 카테고리 검색

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

function savePlace(place){
	fetch("/place/save", {
    	method: 'post',
    	headers: {
            'content-type': 'application/json'
        },
    	body: JSON.stringify({
    		id: place.id,
    		ctgr: place.category_group_code,
    		name: place.place_name,
    		addr: place.address_name,
			brand: place.category_name.split(" > ").reverse()[0],
    	}),
    })
    .then((response)=>{
    	if(response.ok){return;}
    })
    .catch(err=>console.log(err));
}//클릭 시 장소 저장


function showPlaceInfo(place){
    destName.innerText = place.place_name;
	destId.href = '/place/detail?id='+place.id;
    destAddr.innerText = place.address_name;
    destDist.innerText = '에서 '+place.distance+'m';
    
    let src = place.category_group_code;
    if(src == ''){
        src = 'all';
    }else{
		src = src.toLowerCase();
	}
    destImg.setAttribute('src', '/img/'+src+'.png');
} //장소 정보 표시

function displayMarker(place) {
    var marker = new kakao.maps.Marker({
        map: map,
        position: new kakao.maps.LatLng(place.y, place.x)
    });
    placeMarkers.push(marker);
    kakao.maps.event.addListener(marker, 'click', function () {
        infowindow.close();
        infowindow.setContent('<div style="padding:5px;font-size:12px;">' + place.place_name + '</div>');
        infowindow.open(map, marker);
        savePlace(place);
        showPlaceInfo(place);
    });
}//마커 표시하고 이벤트리스너 부착


/*-------------
HTML id list
----------------------------------
category		select		카테고리 선택
brand			select		브랜드 선택
keywordInput	input[search]	검색어 입력

map			div		지도 컨테이너
zoomin		button		지도 확대버튼
maplevel	input[number]	지도 레벨표시
zoomout		button		지도 축소버튼

dest_img		img		목적지 이미지
dest_id			a		목적지 아이디
dest_name		b		목적지 이름
dest_addr		p		목적지 주소
dest_distance	span		목적지까지 거리
*/



	



	
	