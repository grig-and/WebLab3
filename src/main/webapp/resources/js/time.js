function refresh() {
    let time = new Date()
    document.getElementById("time").innerHTML = time.toLocaleTimeString()
    document.getElementById("date").innerHTML = time.toLocaleDateString()
}

refresh()
setInterval(refresh, 13000)

