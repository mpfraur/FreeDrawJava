import {board, ctx, FreeDraw, redrawCanvas} from "./script";

let gallery = document.getElementById("galleryContainer");


function createCanvasObject(name, board) {
    let header = "<h2>" + name + "</h2>";
    let canvas = document.createElement("canvas");
    canvas.width = 800;
    canvas.height = 600;
    redrawCanvas();
}

function redrawCanvasInGallery(board, ctx) {
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


