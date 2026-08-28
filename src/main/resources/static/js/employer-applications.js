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

    const shortText = fullText.substring(0, maxLength).trim() + "...";

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

const overlay = document.getElementById("confirmOverlay");
const confirmTitle = document.getElementById("confirmTitle");
const confirmMessage = document.getElementById("confirmMessage");
const confirmIcon = document.getElementById("confirmIcon");
const confirmButton = document.getElementById("confirmDialog");
const cancelButton = document.getElementById("cancelDialog");

let selectedForm = null;

document.querySelectorAll(".confirm-action").forEach(function (button) {

    button.addEventListener("click", function () {

        selectedForm = button.closest(".status-form");

        const action = button.dataset.action;

        if (action === "shortlist") {

            confirmTitle.textContent = "Shortlist Applicant";
            confirmMessage.textContent =
                "Are you sure you want to shortlist this applicant?";

            confirmIcon.innerHTML =
                '<i class="fa-solid fa-star"></i>';

        } else if (action === "accept") {

            confirmTitle.textContent = "Accept Applicant";
            confirmMessage.textContent =
                "Are you sure you want to accept this applicant?";

            confirmIcon.innerHTML =
                '<i class="fa-solid fa-check"></i>';

        } else if (action === "reject") {

            confirmTitle.textContent = "Reject Applicant";
            confirmMessage.textContent =
                "Are you sure you want to reject this applicant?";

            confirmIcon.innerHTML =
                '<i class="fa-solid fa-xmark"></i>';
        }

        overlay.classList.add("active");
        document.body.style.overflow = "hidden";

    });

});

confirmButton.addEventListener("click", function () {

    if (selectedForm) {
        selectedForm.submit();
    }

    closeDialog();

});

cancelButton.addEventListener("click", function () {
    closeDialog();
});

overlay.addEventListener("click", function (event) {

    if (event.target === overlay) {
        closeDialog();
    }

});

document.addEventListener("keydown", function (event) {

    if (event.key === "Escape" && overlay.classList.contains("active")) {
        closeDialog();
    }

});

function closeDialog() {

    overlay.classList.remove("active");
    document.body.style.overflow = "";
    selectedForm = null;

}

});
