var ws = new WebSocket("ws://127.0.0.1:80/ws");
ctx = document.getElementById("canvas").getContext("2d");
ctx.font = "italic 10pt Arial";
map = new Image();
map.src = "images/map.png";
red = new Image();
red.src = "images/red.png";
green = new Image();
green.src = "images/green.png";

map.onload = function() {
	ctx.drawImage(map,0,0,1980,1020);
}

ws.onmessage = function (event) {
	var nodes = JSON.parse(event.data);
	for(node in nodes) {
		if (nodes[node].result == true){
			ctx.drawImage(green, nodes[node].x, nodes[node].y, 10, 10);
			ctx.fillStyle = "green";
			ctx.fillText(nodes[node].name, nodes[node].x-10, nodes[node].y-2);
		} else {
			ctx.drawImage(red,nodes[node].x, nodes[node].y, 10, 10);
			ctx.fillStyle = "red";
			ctx.fillText(nodes[node].name, nodes[node].x-10, nodes[node].y-2);	
		}
	}
}	
	