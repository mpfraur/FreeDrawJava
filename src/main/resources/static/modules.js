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
    for (const d of drawingData) {
        const shapeName = d.shape ?? d.shapeName;
        if (shapeName) {
            board.addDraw(new Shape(shapeName, d.isFilled, d.color, d.lineWidth, d.position, ctx, d.size || 10));
        } else if (Array.isArray(d.pointsArray) && d.pointsArray.length > 0) {
            board.addDraw(new FreeDraw(d.color, d.lineWidth, d.position, ctx, d.isDrawing, d.pointsArray));
        } else {
            console.warn("Skipping unrecognised entry:", d);
        }
    }
}

const jsonStringEl = document.getElementById("JsonString");
const raw = jsonStringEl?.textContent.trim();
if (raw) {
    try {
        parcingToBoard(JSON.parse(raw));
        updateBoard();
        redrawCanvas();
    } catch (e) {
        console.error("Failed to load drawing", e);
    }
}