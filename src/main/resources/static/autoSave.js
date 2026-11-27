console.log("Auto Loading loaded");

let DOM = {
    drawingId: document.getElementById("drawingId")
}

let responseArray = [];

export async function autoSave(board) {
    let jsonData = JSON.stringify(board);
    let drawing_id = Number(DOM.drawingId.value);

    let formData = new FormData();
    formData.append("drawingId", String(drawing_id));
    formData.append("jsonData", jsonData);

    fetch("http://localhost:8080/studio/save", {
        method: "POST",
        body: formData
    }).then(response => {
        responseArray.push(response);
    });
    console.log("AutoSaved Triggered")
}


window.autoSave = autoSave;