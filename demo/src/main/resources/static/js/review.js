let reviewContents = document.querySelectorAll(".review_content");
let showClass = 'show';

reviewContents.forEach(function(p){
	if(p.offsetHeight >= 72 && p.id != "") { //1rem = 10px
		let btn = document.createElement("span");
		btn.classList.add('btn');
		btn.classList.add('btn-sm');
		btn.classList.add('btn-toggle');
		btn.innerText = "more";
		btn.setAttribute('id','more'+p.id.substring(7)); //p태그 id 필수
		btn.addEventListener('click', function(event){
			let btnId = event.target.id.substring(4);
			let targetContent = document.querySelector('#content'+btnId);
			if(targetContent.classList.contains(showClass)){
				targetContent.classList.remove(showClass);
				event.target.innerText = "more";
			}else{
				targetContent.classList.add(showClass);
				event.target.innerText = "hide";
			}
		})
		p.parentElement.appendChild(btn);
	}
});