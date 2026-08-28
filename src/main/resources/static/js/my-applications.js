document.addEventListener("DOMContentLoaded", function () {

const maxLength = 180;

document.querySelectorAll(".cover-letter").forEach(function (container) {

    const textElement = container.querySelector(".cover-letter-text");
    const seeMore = container.querySelector(".see-more");

    const fullText = textElement.dataset.fullText || "";

    if (fullText.length <= maxLength) {
        textElement.textContent = fullText;
        return;
    }

    let isExpanded = false;

    const shortText =
        fullText.substring(0, maxLength).trim() + "...";

    textElement.textContent = shortText;
    seeMore.style.display = "inline-block";

    seeMore.addEventListener("click", function (event) {

        event.preventDefault();

        if (!isExpanded) {
            textElement.textContent = fullText;
            seeMore.textContent = "See less";
            isExpanded = true;
        } else {
            textElement.textContent = shortText;
            seeMore.textContent = "See more";
            isExpanded = false;
        }

    });

});

});

let withdrawForm = null;

function openWithdrawDialog(button) {
withdrawForm = button.closest("form");

const dialog = document.getElementById("withdrawDialog");

dialog.classList.add("active");
document.body.style.overflow = "hidden";

}

function closeWithdrawDialog() {
const dialog = document.getElementById("withdrawDialog");

dialog.classList.remove("active");
document.body.style.overflow = "";

withdrawForm = null;

}

function confirmWithdraw() {

if (withdrawForm) {
    withdrawForm.submit();
}

}

document.addEventListener("click", function (event) {

const dialog = document.getElementById("withdrawDialog");

if (event.target === dialog) {
    closeWithdrawDialog();
}

});

document.addEventListener("keydown", function (event) {

if (event.key === "Escape") {
    closeWithdrawDialog();
}

});
