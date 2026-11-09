import { board, Shape, FreeDraw, ctx, updateBoard } from "./script.js";
import { redrawCanvas } from "./script.js";

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

export function parcingToBoard(drawingData) {
    let contentList = drawingData.content;

    for (let i = 0; i < contentList.length; i++) {
        let objectShapeDTO = contentList[i];
        if ("shapeName" in objectShapeDTO) {
            let size = objectShapeDTO.size || 10;
            let newShape = new Shape(objectShapeDTO.shapeName, objectShapeDTO.isFilled, objectShapeDTO.color, objectShapeDTO.lineWidth, objectShapeDTO.position, ctx, size);
            board.addDraw(newShape);
        } else {
            let objectFreeDrawDTO = contentList[i];
            let newFreeDraw = new FreeDraw(objectFreeDrawDTO.color, objectFreeDrawDTO.lineWidth, objectFreeDrawDTO.position, ctx, objectFreeDrawDTO.isDrawing, objectFreeDrawDTO.pointsArray);
            board.addDraw(newFreeDraw);
        }
    }
}

const jsonStringEl = document.getElementById("JsonString");
if (jsonStringEl) {
    let drawingData = JSON.parse(jsonStringEl.textContent);
    console.log(drawingData);
    parcingToBoard(drawingData);
    updateBoard();
    redrawCanvas();
}