var server_ip = document.getElementById("server-ip").className;
var ws = new WebSocket("ws://" + server_ip + ":80/db");
var node_name = document.getElementById("name").className
var nodes = []
var b = []
var x = []

var btn = document.getElementById("btn");
var inputtime1 = document.getElementById("time1");
var inputtime2 = document.getElementById("time2");

inputtime1.value = (new Date()).toISOString().slice(0,19);
inputtime2.value = (new Date()).toISOString().slice(0,19);

btn.onclick = function(event) {
  time1 = (new Date(inputtime1.value)).getTime()
  time2 = (new Date(inputtime2.value)).getTime()
  ws.send(JSON.stringify( { command: "get-pings-for-node",
                            "node-name": node_name,
                            time1: time1,
                            time2: time2 }))
}




ws.onmessage = function(event) {
  nodes = JSON.parse(event.data);
  b = []
  x = []
  b.push(node_name)
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
    }});

    nodes.forEach( (n)  => {
      b.push(n.ping)
      x.push(new Date(parseInt(n.time)))
    })
    chart.load({
      columns: [
        b,
        x
      ]
    })
}


