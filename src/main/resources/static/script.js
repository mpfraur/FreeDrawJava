const controls = {
    sizeOfShape: document.getElementById("sizeOfShape"),
    color: document.getElementById("color"),
    isFilledToggle: document.getElementById("isFilledToggle"),
    strokeValue: document.getElementById("strokeValue"),
    shapeOption: document.getElementById("shapeOption"),
    Board: document.getElementById("Board"),
    FreeDrawMode: document.getElementById("IsFreeDrawMode")
};

let DOM = {
    canvas: document.getElementById("canvas"),
    controls: controls
};

export let ctx = DOM.canvas ? DOM.canvas.getContext("2d") : null;

if (DOM.controls.FreeDrawMode) {
    DOM.controls.FreeDrawMode.addEventListener("change", function() {
        if (this.checked) {
            DOM.controls.isFilledToggle.disabled = true;
            DOM.controls.shapeOption.disabled = true;
            DOM.controls.sizeOfShape.disabled = true;
        } else {
            DOM.controls.isFilledToggle.disabled = false;
            DOM.controls.shapeOption.disabled = false;
            DOM.controls.sizeOfShape.disabled = false;
        }
    });
}

let currentFreeDraw = null;

function getMousePos(event) {
    const rect = DOM.canvas.getBoundingClientRect();
    console.log("Calculated:", {
        x: event.clientX - rect.left - DOM.canvas.clientLeft,
        y: event.clientY - rect.top - DOM.canvas.clientTop
    });

    console.log("Offset:", {
        x: event.offsetX,
        y: event.offsetY
    });
    return {
        x: event.clientX - rect.left - DOM.canvas.clientLeft,
        y: event.clientY - rect.top - DOM.canvas.clientTop
    };
}

// code for Freedraw here
if (DOM.canvas) {
    freeDrawMode();

    //code for   Shapes here
    DOM.canvas.addEventListener("click", function(event) {

        if (DOM.controls.FreeDrawMode && DOM.controls.FreeDrawMode.checked) {
            return;
        }

        const rect = DOM.canvas.getBoundingClientRect();

        let mousePosition = {
            x: event.clientX - rect.left - DOM.canvas.clientLeft,
            y: event.clientY - rect.top - DOM.canvas.clientTop
        };

        drawShape(mousePosition);
    });
}

function drawShape(mousePosition) {
    console.log({
        shape: DOM.controls.shapeOption.value,
        color: DOM.controls.color.value,
        filled: DOM.controls.isFilledToggle.checked
    });
    let shape = new Shape(
        DOM.controls.shapeOption.value,
        DOM.controls.isFilledToggle.checked,
        DOM.controls.color.value,
        Number(DOM.controls.strokeValue.value),
        mousePosition,
        ctx,
        Number(DOM.controls.sizeOfShape.value)
    );

    let size = Number(DOM.controls.sizeOfShape.value);

    if (shape.shape === "square") {
        shape.drawSquare(size, size, mousePosition);
    } else if (shape.shape === "triangle") {
        shape.drawTriangle(size, mousePosition);
    } else if (shape.shape === "star") {
        shape.drawStar(size, mousePosition);
    } else {
        shape.drawCircle(size, mousePosition);
    }

    board.addDraw(shape);
    updateBoard();
    console.log(DOM);
    console.log(DOM.Board);
}

class Board {
    constructor(drawsList) {
        this.drawsList = drawsList;
    }

    addDraw(draw) {
        this.drawsList.push(draw);
    }

    removeLastDraw() {
        return this.drawsList.pop();
    }

    isEmpty() {
        return (this.drawsList.length === 0);
    }
}

export const board = new Board([]);

export function updateBoard() {
    let htmlList = DOM.controls.Board;
    if (!htmlList) return;
    htmlList.innerHTML = "";

    for (let i = 0; i < board.drawsList.length; i++) {
        let drawItem = board.drawsList[i];

        let listItem = document.createElement("li");
        let name = document.createElement("h3");
        let position = document.createElement("p");
        let color = document.createElement("p");

        name.textContent = drawItem.shape ? drawItem.shape : "Free Line Line";
        position.textContent = JSON.stringify(drawItem.position);
        color.textContent = drawItem.color;

        listItem.appendChild(name);
        listItem.appendChild(position);
        listItem.appendChild(color);

        htmlList.appendChild(listItem);
    }
}

export function redrawCanvas() {
    if (!ctx || !DOM.canvas) return;
    ctx.clearRect(0, 0, DOM.canvas.width, DOM.canvas.height);

    for (let draw of board.drawsList) {
        let size = draw.size || 10;

        if (draw instanceof FreeDraw) {
            draw.drawLine(draw.position);
            continue;
        }

        if (draw.shape === "square") {
            draw.drawSquare(size, size, draw.position);
        } else if (draw.shape === "triangle") {
            draw.drawTriangle(size, draw.position);
        } else if (draw.shape === "star") {
            draw.drawStar(size, draw.position);
        } else {
            draw.drawCircle(size, draw.position);
        }
    }
}

document.addEventListener("keydown", function(event) {
    if (event.ctrlKey && event.key === "z") {
        board.removeLastDraw();
        redrawCanvas();
        updateBoard();
    }
});

export class Draw {
    constructor(color, lineWidth, position, ctx) {
        this.color = color;
        this.lineWidth = lineWidth;
        this.position = position;
        this.ctx = ctx;
    }
}

export class Shape extends Draw {
    constructor(shape, isFilled, color, lineWidth, position, ctx, size) {
        super(color, lineWidth, position, ctx);
        this.shape = shape;
        this.isFilled = isFilled;
        this.size = size;
    }

    drawSquare(width, height, position) {
        this.ctx.lineWidth = this.lineWidth;
        this.ctx.fillStyle = this.color;
        this.ctx.strokeStyle = this.color;

        this.ctx.lineCap = "butt";
        this.ctx.lineJoin = "miter";

        if (this.isFilled) {
            this.ctx.fillRect(position.x, position.y, width, height);
        } else {
            this.ctx.strokeRect(position.x, position.y, width, height);
        }
    }

    drawCircle(size, position) {
        this.ctx.lineWidth = this.lineWidth;
        this.ctx.fillStyle = this.color;
        this.ctx.strokeStyle = this.color;

        this.ctx.beginPath();
        this.ctx.arc(position.x, position.y, size, 0, Math.PI * 2);

        if (this.isFilled) {
            this.ctx.fill();
        } else {
            this.ctx.stroke();
        }
    }

    drawTriangle(size, position) {
        this.ctx.lineWidth = this.lineWidth;
        this.ctx.fillStyle = this.color;
        this.ctx.strokeStyle = this.color;

        this.ctx.beginPath();
        this.ctx.moveTo(position.x, position.y);
        this.ctx.lineTo(position.x + size, position.y);
        this.ctx.lineTo(position.x + (size / 2), position.y + size);
        this.ctx.closePath();

        if (this.isFilled) {
            this.ctx.fill();
        } else {
            this.ctx.stroke();
        }
    }

    drawStar(size, position) {
        this.ctx.lineWidth = this.lineWidth;
        this.ctx.fillStyle = this.color;
        this.ctx.strokeStyle = this.color;

        let radius = size;
        let inner_radius = (size / 2);
        let points = 7;
        let angle = -Math.PI / 2 + Math.PI / points;

        this.ctx.beginPath();
        this.ctx.moveTo(position.x, position.y - radius);
        for (let i = 0; i < points * 2; i++) {

            if (i % 2 == 0) {
                this.ctx.lineTo(position.x + inner_radius * Math.cos(angle), position.y + inner_radius * Math.sin(angle));
            } else {
                this.ctx.lineTo(position.x + radius * Math.cos(angle), position.y + radius * Math.sin(angle));
            }

            angle += Math.PI / points;
        }
        this.ctx.closePath();

        if (this.isFilled) {
            this.ctx.fill();
        } else {
            this.ctx.stroke();
        }
    }
}

export class PositionObject {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }
}

export class FreeDraw extends Draw {
    constructor(color, lineWidth, position, ctx, isDrawing, pointsArray) {
        super(color, lineWidth, position, ctx);
        this.isDrawing = isDrawing;
        this.pointsArray = pointsArray;
    }

    drawLine(position) {
        this.ctx.lineWidth = this.lineWidth;
        this.ctx.fillStyle = this.color;
        this.ctx.strokeStyle = this.color;

        if (!this.pointsArray || this.pointsArray.length === 0) {
            throw new Error("The points are empty.");
        }

        this.ctx.beginPath();
        this.ctx.moveTo(position.x, position.y);

        for (let i = 0; i < this.pointsArray.length; i++) {
            let currentPoint = this.pointsArray[i];
            this.ctx.lineTo(currentPoint.x, currentPoint.y);
        }

        this.ctx.stroke();
    }
}

function freeDrawMode() {
    let activeLine = null;

    DOM.canvas.addEventListener("mousedown", function (event) {
        if (!DOM.controls.FreeDrawMode || !DOM.controls.FreeDrawMode.checked) return;
        if (event.button !== 0) return;

        const start = getMousePos(event);
        const startPoint = new PositionObject(start.x, start.y);

        activeLine = new FreeDraw(
            DOM.controls.color.value,
            Number(DOM.controls.strokeValue.value),
            startPoint,
            ctx,
            true,
            [startPoint]
        );
    });

    DOM.canvas.addEventListener("mousemove", function (event) {
        if (!activeLine || !activeLine.isDrawing) return;

        const p = getMousePos(event);
        const last = activeLine.pointsArray[activeLine.pointsArray.length - 1];

        if (activeLine.pointsArray.length === 1) {
            board.addDraw(activeLine);
            updateBoard();
        }

        activeLine.pointsArray.push(new PositionObject(p.x, p.y));

        ctx.strokeStyle = activeLine.color;
        ctx.lineWidth = activeLine.lineWidth;
        ctx.lineCap = "round";
        ctx.lineJoin = "round";
        ctx.beginPath();
        ctx.moveTo(last.x, last.y);
        ctx.lineTo(p.x, p.y);
        ctx.stroke();
    });

    function finishStroke() {
        if (!activeLine) return;
        activeLine.isDrawing = false;
        activeLine = null;
    }

    DOM.canvas.addEventListener("mouseup", finishStroke);
    DOM.canvas.addEventListener("mouseleave", finishStroke);
}

function sendDataToServerViaString(boardInstance) {
    return JSON.stringify(boardInstance.drawsList, (key, value) => {
        if (key === 'ctx') return undefined;
        return value;
    });
}

const inputOcult = document.getElementById('meuInputOcult');
if (inputOcult) {
    inputOcult.value = sendDataToServerViaString(board);
    console.log("HOLA!!!!!!");
    console.log(inputOcult.value);
}

function enviaFormulari() {
    console.log(board.drawsList);
    console.log(board.drawsList.length);
    if (inputOcult) {
        return inputOcult.value = sendDataToServerViaString(board);
    }
}

const sendBoardBtn = document.getElementById("sendBoard");
if (sendBoardBtn) {
    sendBoardBtn.addEventListener("click", function() {
        console.log(board.drawsList);
        console.log(board.drawsList.length);
        enviaFormulari();
    });
}