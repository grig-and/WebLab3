function refresh() {
    let time = new Date()
    document.getElementById("time").innerHTML = time.toLocaleTimeString()
    // document.getElementById("date").innerHTML = time.toLocaleDateString()
}

refresh()
setInterval(refresh, 13000)

const eventSource = new EventSource("http://localhost:13400/WL3-1.0-SNAPSHOT/sse")
eventSource.onmessage = function(event) {
    document.getElementById("sse").innerHTML = event.data;
}
