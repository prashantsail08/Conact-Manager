console.log("this is js script file");

const toggleSidebar=()=>{
	
	if($('.sidebar').is(":visible"))
	{
		//true
		//colse the sidebar
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
		
	}else{
		//false
		//open the sidebar
				$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
	
	
};

const search = () =>{
	//console.log("Searching...");
	
	let query = $("#search-input").val();

	if(query == ""){
		$(".search-result").html();
	}
	else{
		//search
		//console.log(query);

		//sending request to server
		let url = `http://localhost:8080/search/${query}`;

		fetch(url).then((Response) =>{
			return Response.json();
		}).then((data) => {
			//data....
			//console.log(data);

			let text = `<div class='list-group'>`

			data.forEach(contact =>{
				text +=`<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'>${contact.name}</a>`
			});

			text +=`</div>`

			$(".search-result").html(text);
			$(".search-result").show();

		});


	}
};