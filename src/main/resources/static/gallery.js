import { updateBoard, redrawCanvas } from "./script.js";
import { parcingToBoard } from "./modules.js";

const jsonViewEl = document.getElementById("JsonViewDrawing");

if (jsonViewEl) {
    let drawingData = JSON.parse(jsonViewEl.textContent);
    console.log(drawingData);
    parcingToBoard(drawingData);
    updateBoard();
    redrawCanvas();
}