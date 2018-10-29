var server_ip = document.getElementById("server-ip").className
var ws = new WebSocket("ws://" + server_ip + ":80/ws");
var node_name = document.getElementById("name").className
var b = []
b.push("")
var chart = c3.generate({
    bindto: '#chart',
    data: {
      columns: [
        b

      ]
    }
});
var initialized = false
ws.onmessage = function(e) {
	let nodes = JSON.parse(e.data);
	nodes.forEach( (n) => {
		if(n.name == node_name) {
      if(initialized == false){
        b[0] =  n.name +  "( " + n.ip + " )";
        initialized = true;
      }
			b.push(n.ping)
		}
	})
	chart.load({
        columns: [
            b
        ]
    });
}
