
var origin = window.location.origin;
var projLoc = "/test"
var baseUrl = origin + projLoc;
var ts = 0;

console.log(baseUrl);

$(document).ready(function(){
	
	$("#tweet").click(function(){
		tweet = new Object();
		tweet.message = $("#tweetArea").val();
		console.log(JSON.stringify(tweet));
		
		$.ajax({
			type:"POST",
			url: baseUrl+"/tweet",
			data: JSON.stringify(tweet),
			dataType:"json",
			contentType: "application/json",
			success: function(returnObj, status, xhr){
				alert("Tweeted Successfully");
				$("#tweetArea").val("");
			},
			error: function(jqXHR, textStatus, errorThrown ){
				alert("Error:"+errorThrown);
			}
		});
		
	});
	
	$("#refresh").click(function(){
		//jsonObj = tryThis();
		$.get(baseUrl+"/tweet", function(jsonObj){
			if(jsonObj!=undefined && jsonObj.length>0){
				for(i=0; i<jsonObj.length; i++){
					var item = jsonObj[i];
					var templateTr = $("#template_table :first-child").children(".template_row").clone();
					
					from = templateTr.children(".template_cellFrom");
					from.html("@"+item.payload.fromUser);
					from.removeClass("template_cellFrom");
					from.addClass("tweetFrom");
					
					message = templateTr.children(".template_cellMessage");
					message.html(item.payload.text);
					message.removeClass("template_cellMessage");
					message.addClass("tweetMessage");
					
					templateTr.removeClass("template_row");
					templateTr.addClass("tweetRow");
					
					$(".timeline_rows").prepend(templateTr);
					
					console.log(templateTr.html());
				}
			}
		});
	});
});

function tryThis(){
	str = "["
	  		+ "{"
		    +"\"payload\": {"
		    +"\"text\": \"text"+ts+"\","
		    +"\"createdAt\": 1433822412000,"
		    +"\"fromUser\": \"HarvardBiz\","
		    +"\"retweet\": false,"
		    +"\"unmodifiedText\": \"text"+ts+"\""
		    +"},"
		    +"\"headers\": {"
		    +"\"id\": \"e8133b2e\","
		    +"\"timestamp\": 1433822682168"
		    +"}"
		  +"},"
		  +"{"
		  +"\"payload\": {"
			  +"\"text\": \"text"+(ts+1)+"\","
			  +"\"createdAt\": 1433822412000,"
			  +"\"fromUser\": \"HarvardBiz\", "     
			  +"\"retweet\": false,"
			  +"\"unmodifiedText\": \"text"+(ts+1)+"\""
				  +"},"
				  +"\"headers\": {"
					  +"\"id\": \"0dacd39514db\","
					  +"\"timestamp\": 1433822682168"
					  +"}"
					  +"}"
					  +"]";
	ts+=2;
	var obj = JSON.parse(str);
	return obj;
}