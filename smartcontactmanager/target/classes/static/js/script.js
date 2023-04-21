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