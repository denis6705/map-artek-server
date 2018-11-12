var server_ip = document.getElementById("server-ip").className
var ws = new WebSocket("ws://" + server_ip + ":80/ws");
var node_name = document.getElementById("name").className
var b = []
var x = []
b.push("")
x.push('x')
var chart = c3.generate({
    bindto: '#chart',
    data: {
      x : 'x',
      columns: [
        b,
        x
      ]
    },
    axis : {
      x : {
        type : 'timeseries',
        tick : {
          format: function(x) { return x.toDateString() + "::" +
           x.getHours() + ":" + x.getMinutes() + ":" + x.getSeconds();
          }
        }
      }
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
      x.push(new Date())
		}
	})
	chart.load({
        columns: [
            b,
            x
        ]
    }
    );
}
