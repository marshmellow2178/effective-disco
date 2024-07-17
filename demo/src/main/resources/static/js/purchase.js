let purchaseInfos = document.querySelectorAll(".purchase_info");
	
window.addEventListener('load', function(){
	purchaseInfos.forEach((item)=>{
		let orderId = Number(item.id.split("_")[1]);
		fetch("/cmp/purchase/list?id="+orderId, {
	    	method: 'get',
	    	headers:{'content-type': 'text'},
    	})
    	.then((response)=>response.json())
    	.then((list)=>{
    		list.forEach(data=>{
    			let content = document.createElement('tr');
	    		content.innerHTML = "<td>"+data.name+"</td>"+
	    		'<td>'+data.count+"</td>"+
	    		'<td>'+data.price+"</td>";
	    		item.appendChild(content);
    		});
	    });
	});
});