var ws = new WebSocket("ws://127.0.0.1:8080/ws");
ctx = document.getElementById("canvas").getContext("2d");
ctx.font = "italic 10pt Arial";
map = new Image();
map.src = "images/map.png";
red = new Image();
red.src = "images/red.png";
green = new Image();
green.src = "images/green.png";
var nodes = {};
map.onload = function() {
	ctx.drawImage(map,0,0,1980,1020);
}



canvas.onclick = function(event) {
	let X1 = event.layerX - 10;
	let Y1 = event.layerY - 10;
	let X2 = event.layerX + 10;
	let Y2 = event.layerY + 10;

	console.log(event);

	nodes.forEach( (n) => {
		if( (n.x >= X1) && (n.y >= Y1) && (n.x <= X2) && (n.y <= Y2)) {
			//alert(n.name + "\nПинг: " + (n.result ? n.ping.toFixed(2) + "мс":"    :\'(") + "\nIP: " + n.ip )
			window.document.location.href = window.location.href + "nodes/" + n.name;
		}
	})
}


ws.onmessage = function (event) {
	ctx.drawImage(map,0,0,1980,1020);
	nodes = JSON.parse(event.data);
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
