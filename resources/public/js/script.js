var server_ip = document.getElementById("server-ip").className
var ws = new WebSocket("ws://" + server_ip + ":80/ws");
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
	let X1 = event.clientX - 10;
	let Y1 = event.clientY - 10;
	let X2 = event.clientX + 10;
	let Y2 = event.clientY + 10;
	
	X1 -= 8;
	X2 -= 8;
	Y1 -= 8;
	Y2 -= 8;

	console.log(event);

	nodes.forEach( (n) => {
		if( (n.x >= X1) && (n.y >= Y1) && (n.x <= X2) && (n.y <= Y2)) {
			//alert(n.name + "\nПинг: " + (n.result ? n.ping.toFixed(2) + "мс":"    :\'(") + "\nIP: " + n.ip )
			if (event.shiftKey) window.document.location.href = window.location.href +  n.name;
      else window.document.location.href = window.location.href + "nodes/" + n.name;
		}
	});
}



ws.onopen = function (event) {
    ws.onclose = function (event) {
    alert("Сервер отказался отправлять вам данные");
  }
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
