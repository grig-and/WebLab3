if (typeof r === 'undefined') {
    r = 0;
}

let canvas = document.getElementById("canvas");
let ctx = canvas.getContext("2d");


function draw() {
    let rStr = document.getElementById("j_idt10:input_r").value;
    let rValue = parseFloat(rStr);
    ctx.fillStyle = "#ffffff";
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    let r = canvas.width / 3;
    let zero = canvas.width / 2;

    //circle
    ctx.beginPath();
    ctx.lineWidth = 0.1
    ctx.arc(zero, zero, r, -Math.PI, -Math.PI / 2);
    ctx.lineTo(zero, zero);
    ctx.fillStyle = "#0000ff";
    ctx.fill();
    ctx.stroke();

    //triangle
    ctx.beginPath();
    ctx.moveTo(zero, zero);
    ctx.lineTo(zero - r / 2, zero);
    ctx.lineTo(zero, zero + r / 2);
    ctx.lineTo(zero, zero);
    ctx.fillStyle = "#0000ff";
    ctx.fill();
    ctx.stroke();

    //rectangle
    ctx.beginPath();
    ctx.moveTo(zero, zero);
    ctx.lineTo(zero + r, zero);
    ctx.lineTo(zero + r, zero - r / 2);
    ctx.lineTo(zero, zero - r / 2);
    ctx.lineTo(zero, zero);
    ctx.fillStyle = "#0000ff";
    ctx.fill();
    ctx.stroke();

    ctx.lineWidth = 1

    ctx.beginPath();
    ctx.moveTo(zero, 0 + 10);
    ctx.lineTo(zero, canvas.height - 10);
    ctx.moveTo(zero, 0 + 10);
    ctx.lineTo(zero - 6, 20);
    ctx.moveTo(zero, 0 + 10);
    ctx.lineTo(zero + 6, 20);
    ctx.stroke();

    ctx.beginPath();
    ctx.moveTo(canvas.width - 10, zero);
    ctx.lineTo(10, zero);
    ctx.moveTo(canvas.width - 10, zero);
    ctx.lineTo(canvas.width - 20, zero - 6);
    ctx.moveTo(canvas.width - 10, zero);
    ctx.lineTo(canvas.width - 20, zero + 6);
    ctx.stroke();


    ctx.beginPath();
    drawText("Y", zero + 6, zero - r - 20);
    drawDash(zero, zero - r, 'y');
    drawText(rValue.toString(), zero + 6, zero - r + 4);
    drawDash(zero, zero - r / 2, 'y');
    drawText((rValue / 2).toString(), zero + 6, zero - r / 2 + 4);
    drawDash(zero, zero + r / 2, 'y');
    drawText((rValue / 2).toString(), zero + 6, zero + r / 2 + 4);
    drawDash(zero, zero + r, 'y');
    drawText(rValue.toString(), zero + 6, zero + r + 4);

    drawDash(zero - r, zero, 'x');
    drawText(rValue.toString(), zero - r - 2, zero - 8, true);
    drawDash(zero - r / 2, zero, 'x');
    drawText((rValue / 2).toString(), zero - r / 2 - 2, zero - 8, true);
    drawDash(zero + r / 2, zero, 'x');
    drawText((rValue / 2).toString(), zero + r / 2 - 2, zero - 8, true);
    drawDash(zero + r, zero, 'x');
    drawText(rValue.toString(), zero + r - 2, zero - 8, true);
    drawText("X", zero + r + 20, zero - 8);
    ctx.stroke();
}

function drawDash(x, y, axis) {
    if (axis == 'x') {
        ctx.moveTo(x, y - 4)
        ctx.lineTo(x, y + 4)
    } else if (axis == 'y') {
        ctx.moveTo(x - 4, y)
        ctx.lineTo(x + 4, y)
    }
}

function drawText(text, x, y, centered) {
    if (text == "NaN") {
        text = "?";
    }
    ctx.fillStyle = "#000000";
    ctx.fillText(text, centered ? (x - text.length / 2 * 4 + 1) : x, y);
    ctx.fillStyle = "#0000ff";
}

function refresh() {
    draw();
    drawPoints();
}

window.onload = function () {
    draw()
    drawPoints()
}


function checkHit(x, y, r) {
    if (x >= 0 && y >= 0 && x <= r && y <= r / 2) {
        return true;
    }
    if (x <= 0 && y <= 0 && y >= -r / 2 - x) {
        return true;
    }
    if (x <= 0 && y >= 0 && x * x + y * y <= r * r) {
        return true;
    }
    return false;
}


canvas.onclick = function (event) {
    let r = parseFloat(document.getElementById("j_idt10:input_r").value)
    if (r == 0) {
        alert("R is not selected");
        return;
    }

    let xC = event.offsetX;
    let yC = event.offsetY;
    let rC = canvas.width / 3;
    let zero = canvas.width / 2;

    console.log(xC, yC, rC, zero)

    if (xC > zero - rC && xC < zero + rC && yC > zero - rC && yC < zero + rC) { //?
        let xValue = ((xC - zero) / rC) * r
        let yValue = ((zero - yC) / rC) * r

        if (xValue > 3) {
            xValue = 3;
        }
        if (xValue < -3) {
            xValue = -3;
        }
        // debugger
        console.log(xValue, yValue, checkHit(xValue, yValue, r))
        // call remote method addPoint
        addPoint([{name: 'x', value: xValue}, {name: 'y', value: yValue}, {name: 'r', value: r}])
            .then((response) => {
                console.log(response)
                draw();
                drawPoints();
            })
    }
}

document.getElementById("j_idt10:submit").addEventListener("click", function () {
    setTimeout(() => {
        draw()
        drawPoints()
    }, 200)
});

document.getElementById("j_idt10:delete").addEventListener("click", function () {
    draw()
});


function drawPoints() {
    let table = document.getElementById("result");
    let rows = table.rows;
    let rC = canvas.width / 3;
    let zero = canvas.width / 2;


    for (let i = 1; i < rows.length; i++) {
        let x = parseFloat(rows[i].cells[0].innerText)
        let y = parseFloat(rows[i].cells[1].innerText)
        let r = parseFloat(document.getElementById("j_idt10:input_r").value)
        let result = rows[i].cells[5].innerText;
        let xC = (x / r) * rC + zero;
        let yC = zero - (y / r) * rC;
        ctx.beginPath();
        ctx.arc(xC, yC, 3, 0, 2 * Math.PI);
        if (result === "true") {
            ctx.fillStyle = "#00ff00";
        } else {
            ctx.fillStyle = "#ff0000";
        }
        ctx.fill();
        ctx.stroke();
    }
}




